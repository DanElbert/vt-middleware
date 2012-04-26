/*
  $Id$

  Copyright (C) 2003-2012 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.provider.jldap;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPConstraints;
import com.novell.ldap.LDAPException;

/**
 * Creates ldap connections using the JLDAP LDAPConnection class.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class JLdapConnectionFactory
  extends AbstractJLdapConnectionFactory<JLdapConnection>
{


  /**
   * Creates a new jldap connection factory.
   *
   * @param  url  of the ldap to connect to
   * @param  config  provider configuration
   * @param  constraints  connection constraints
   * @param  timeOut  time in milliseconds that operations will wait
   */
  public JLdapConnectionFactory(
    final String url,
    final JLdapProviderConfig config,
    final LDAPConstraints constraints,
    final int timeOut)
  {
    super(url, config, constraints, timeOut);
  }


  /** {@inheritDoc} */
  @Override
  protected LDAPConnection createLDAPConnection()
    throws LDAPException
  {
    return new LDAPConnection();
  }


  /** {@inheritDoc} */
  @Override
  protected JLdapConnection createJLdapConnection(
    final LDAPConnection conn, final JLdapProviderConfig config)
  {
    return new JLdapConnection(conn, config);
  }
}
