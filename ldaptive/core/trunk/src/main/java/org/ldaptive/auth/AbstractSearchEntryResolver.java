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

import java.util.Arrays;
import java.util.Iterator;
import org.ldaptive.Connection;
import org.ldaptive.DerefAliases;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.ldaptive.SearchFilter;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.SearchScope;
import org.ldaptive.handler.SearchEntryHandler;

/**
 * Base implementation for search entry resolvers.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class AbstractSearchEntryResolver
  extends AbstractSearchOperationFactory implements EntryResolver
{

  /** DN to search. */
  private String baseDn = "";

  /** Filter for searching for the user. */
  private String userFilter;

  /** Filter parameters for searching for the user. */
  private Object[] userFilterParameters;

  /** Whether to throw an exception if multiple entries are found. */
  private boolean allowMultipleEntries;

  /** Whether to use a subtree search when resolving DNs. */
  private boolean subtreeSearch;

  /** How to handle aliases. */
  private DerefAliases derefAliases;

  /** Whether to follow referrals. */
  private boolean followReferrals;

  /** @deprecated  User attributes to return. */
  @Deprecated
  private String[] retAttrs;

  /** Ldap entry handlers. */
  private SearchEntryHandler[] entryHandlers;


  /**
   * Returns the base DN.
   *
   * @return  base DN
   */
  public String getBaseDn()
  {
    return baseDn;
  }


  /**
   * Sets the base DN.
   *
   * @param  dn  base DN
   */
  public void setBaseDn(final String dn)
  {
    logger.trace("setting baseDn: {}", dn);
    baseDn = dn;
  }


  /**
   * Returns the filter used to search for the user.
   *
   * @return  filter for searching
   */
  public String getUserFilter()
  {
    return userFilter;
  }


  /**
   * Sets the filter used to search for the user.
   *
   * @param  filter  for searching
   */
  public void setUserFilter(final String filter)
  {
    logger.trace("setting userFilter: {}", filter);
    userFilter = filter;
  }


  /**
   * Returns the filter parameters used to search for the user.
   *
   * @return  filter parameters
   */
  public Object[] getUserFilterParameters()
  {
    return userFilterParameters;
  }


  /**
   * Sets the filter parameters used to search for the user.
   *
   * @param  filterParams  filter parameters
   */
  public void setUserFilterParameters(final Object[] filterParams)
  {
    logger.trace(
      "setting userFilterParameters: {}",
      Arrays.toString(filterParams));
    userFilterParameters = filterParams;
  }


  /**
   * Returns whether entry resolution should fail if multiple entries are found.
   *
   * @return  whether an exception will be thrown if multiple entries are found
   */
  public boolean getAllowMultipleEntries()
  {
    return allowMultipleEntries;
  }


  /**
   * Sets whether entry resolution should fail if multiple entries are found. If
   * false an exception will be thrown if {@link #resolve(Connection,
   * AuthenticationCriteria)} finds more than one entry matching it's filter.
   * Otherwise the first entry found is returned.
   *
   * @param  b  whether multiple entries are allowed
   */
  public void setAllowMultipleEntries(final boolean b)
  {
    logger.trace("setting allowMultipleEntries: {}", b);
    allowMultipleEntries = b;
  }


  /**
   * Returns whether subtree searching will be used.
   *
   * @return  whether the entry will be searched for over the entire base
   */
  public boolean getSubtreeSearch()
  {
    return subtreeSearch;
  }


  /**
   * Sets whether subtree searching will be used. If true, the entry will be
   * searched for over the entire {@link #getBaseDn()}. Otherwise the entry will
   * be searched for in the {@link #getBaseDn()} context.
   *
   * @param  b  whether the entry will be searched for over the entire base
   */
  public void setSubtreeSearch(final boolean b)
  {
    logger.trace("setting subtreeSearch: {}", b);
    subtreeSearch = b;
  }


  /**
   * Returns how to dereference aliases.
   *
   * @return  how to dereference aliases
   */
  public DerefAliases getDerefAliases()
  {
    return derefAliases;
  }


  /**
   * Sets how to dereference aliases.
   *
   * @param  da  how to dereference aliases
   */
  public void setDerefAliases(final DerefAliases da)
  {
    logger.trace("setting derefAliases: {}", da);
    derefAliases = da;
  }


  /**
   * Returns whether to follow referrals.
   *
   * @return  whether to follow referrals
   */
  public boolean getFollowReferrals()
  {
    return followReferrals;
  }


  /**
   * Sets whether to follow referrals.
   *
   * @param  b  whether to follow referrals
   */
  public void setFollowReferrals(final boolean b)
  {
    logger.trace("setting followReferrals: {}", b);
    followReferrals = b;
  }


  /**
   * Returns the return attributes.
   *
   * @return  attributes to return
   *
   * @deprecated  return attributes retrieved from the authentication request
   */
  @Deprecated
  public String[] getReturnAttributes()
  {
    return retAttrs;
  }


  /**
   * Sets the return attributes.
   *
   * @param  attrs  to return
   *
   * @deprecated  return attributes retrieved from the authentication request
   */
  @Deprecated
  public void setReturnAttributes(final String... attrs)
  {
    retAttrs = attrs;
  }


  /**
   * Returns the search entry handlers.
   *
   * @return  search entry handlers
   */
  public SearchEntryHandler[] getSearchEntryHandlers()
  {
    return entryHandlers;
  }


  /**
   * Sets the search entry handlers.
   *
   * @param  handlers  search entry handlers
   */
  public void setSearchEntryHandlers(final SearchEntryHandler... handlers)
  {
    entryHandlers = handlers;
  }


  /**
   * Executes an ldap search with the supplied authentication criteria.
   *
   * @param  conn  that the user attempted to bind on
   * @param  ac  authentication criteria associated with the user
   *
   * @return  search result
   *
   * @throws  LdapException  if an error occurs attempting the search
   */
  protected abstract SearchResult performLdapSearch(
    final Connection conn,
    final AuthenticationCriteria ac)
    throws LdapException;


  /**
   * Returns a search filter using {@link #userFilter} and {@link
   * #userFilterParameters}. The user parameter is injected as a named parameter
   * of 'user'. If no userFilter is defined the authentication criteria DN is
   * used is conjunction with an object level search.
   *
   * @param  ac  authentication criteria
   *
   * @return  search filter
   */
  protected SearchFilter createSearchFilter(final AuthenticationCriteria ac)
  {
    final SearchFilter filter = new SearchFilter();
    if (userFilter != null) {
      logger.debug("searching for entry using userFilter");
      filter.setFilter(userFilter);
      if (userFilterParameters != null) {
        filter.setParameters(userFilterParameters);
      }
      // assign dn as a named parameter
      filter.setParameter("user", ac.getDn());
    } else {
      logger.debug("No userFilter defined, using authenticated DN");
      filter.setFilter(ac.getDn());
    }
    return filter;
  }


  /**
   * Returns a search request for the supplied authentication criteria. If no
   * {@link #userFilter} is defined then an object level search on the
   * authentication criteria DN is returned. Otherwise the {@link #userFilter},
   * {@link #baseDn} and {@link #subtreeSearch} are used to create the search
   * request.
   *
   * @param  ac  authentication criteria containing a DN
   * @param  returnAttributes  to request
   *
   * @return  search request
   *
   * @deprecated  use {@link #createSearchRequest(AuthenticationCriteria)}
   */
  @Deprecated
  protected SearchRequest createSearchRequest(
    final AuthenticationCriteria ac,
    final String[] returnAttributes)
  {
    SearchRequest request;
    if (userFilter != null) {
      request = new SearchRequest(baseDn, createSearchFilter(ac));
      request.setReturnAttributes(returnAttributes);
      if (subtreeSearch) {
        request.setSearchScope(SearchScope.SUBTREE);
      } else {
        request.setSearchScope(SearchScope.ONELEVEL);
      }
    } else {
      request = SearchRequest.newObjectScopeSearchRequest(
        ac.getDn(),
        returnAttributes);
    }
    request.setDerefAliases(derefAliases);
    request.setFollowReferrals(followReferrals);
    request.setSearchEntryHandlers(entryHandlers);
    return request;
  }


  /**
   * Returns a search request for the supplied authentication criteria. If no
   * {@link #userFilter} is defined then an object level search on the
   * authentication criteria DN is returned. Otherwise the {@link #userFilter},
   * {@link #baseDn} and {@link #subtreeSearch} are used to create the search
   * request.
   *
   * @param  ac  authentication criteria containing a DN
   *
   * @return  search request
   */
  protected SearchRequest createSearchRequest(final AuthenticationCriteria ac)
  {
    SearchRequest request;
    if (userFilter != null) {
      request = new SearchRequest(baseDn, createSearchFilter(ac));
      request.setReturnAttributes(
        ac.getAuthenticationRequest().getReturnAttributes());
      if (subtreeSearch) {
        request.setSearchScope(SearchScope.SUBTREE);
      } else {
        request.setSearchScope(SearchScope.ONELEVEL);
      }
    } else {
      request = SearchRequest.newObjectScopeSearchRequest(
        ac.getDn(),
        ac.getAuthenticationRequest().getReturnAttributes());
    }
    request.setDerefAliases(derefAliases);
    request.setFollowReferrals(followReferrals);
    request.setSearchEntryHandlers(entryHandlers);
    return request;
  }


  /** {@inheritDoc} */
  @Override
  public LdapEntry resolve(
    final Connection conn,
    final AuthenticationCriteria ac)
    throws LdapException
  {
    logger.debug("resolve criteria={}", ac);

    final SearchResult result = performLdapSearch(conn, ac);
    logger.debug("resolved result={} for criteria={}", result, ac);

    LdapEntry entry = null;
    final Iterator<LdapEntry> answer = result.getEntries().iterator();
    if (answer != null && answer.hasNext()) {
      entry = answer.next();
      if (answer.hasNext()) {
        logger.debug("multiple results found for user={}", ac.getDn());
        if (!allowMultipleEntries) {
          throw new LdapException(
            "Found more than (1) entry for: " + ac.getDn());
        }
      }
    }
    return entry;
  }
}
