/*
  $Id$

  Copyright (C) 2003-2014 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.auth;

import org.ldaptive.Connection;
import org.ldaptive.LdapException;
import org.ldaptive.Response;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.extended.WhoAmIOperation;
import org.ldaptive.extended.WhoAmIRequest;

/**
 * Executes the {@link WhoAmIOperation} on the authenticated connection, then
 * performs an object level search on the result. Useful when users authenticate
 * with some mapped identifier, like DIGEST-MD5.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class WhoAmIEntryResolver extends AbstractSearchEntryResolver
{


  /** {@inheritDoc} */
  @Override
  protected SearchResult performLdapSearch(
    final Connection conn,
    final AuthenticationCriteria ac)
    throws LdapException
  {
    final WhoAmIOperation whoami = new WhoAmIOperation(conn);
    final Response<String> res = whoami.execute(new WhoAmIRequest());
    logger.debug("whoami operation returned {}", res);

    final String authzId = res.getResult();
    if (authzId == null) {
      throw new IllegalStateException(
        "WhoAmI operation did not return an authorization ID");
    }

    final String dn = authzId.split(":", 2)[1].trim();
    final SearchOperation search = createSearchOperation(conn);
    return search.execute(createSearchRequest(ac, dn)).getResult();
  }


  /**
   * Returns a search request for an object level search for the supplied DN.
   *
   * @param  ac  authentication criteria containing return attributes
   * @param  dn  from the who am i operation
   *
   * @return  search request
   */
  protected SearchRequest createSearchRequest(
    final AuthenticationCriteria ac,
    final String dn)
  {
    final SearchRequest request = SearchRequest.newObjectScopeSearchRequest(
      dn,
      ac.getAuthenticationRequest().getReturnAttributes());
    request.setDerefAliases(getDerefAliases());
    request.setFollowReferrals(getFollowReferrals());
    request.setSearchEntryHandlers(getSearchEntryHandlers());
    return request;
  }
}
