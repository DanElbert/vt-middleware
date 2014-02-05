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
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;

/**
 * Provides an interface for finding a user's ldap entry after a successful
 * authentication.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public interface EntryResolver
{


  /**
   * Attempts to find the LDAP entry for the supplied authentication criteria,
   * using the supplied connection. The supplied connection should <b>not</b> be
   * closed in this method.
   *
   * @param  conn  that authentication occurred on
   * @param  criteria  authentication criteria used to perform the
   * authentication
   *
   * @return  ldap entry
   *
   * @throws  LdapException  if an LDAP error occurs
   */
  LdapEntry resolve(Connection conn, AuthenticationCriteria criteria)
    throws LdapException;
}
