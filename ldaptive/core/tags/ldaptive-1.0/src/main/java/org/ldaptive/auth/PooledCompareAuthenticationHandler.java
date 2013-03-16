/*
  $Id$

  Copyright (C) 2003-2013 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.auth;

import java.util.Arrays;
import org.ldaptive.Connection;
import org.ldaptive.LdapException;
import org.ldaptive.pool.PooledConnectionFactory;
import org.ldaptive.pool.PooledConnectionFactoryManager;

/**
 * Provides an LDAP authentication implementation that leverages a pool of ldap
 * connections to perform the compare operation against the userPassword
 * attribute. The default password scheme used is 'SHA'.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class PooledCompareAuthenticationHandler
  extends AbstractCompareAuthenticationHandler
  implements PooledConnectionFactoryManager
{

  /** Connection factory. */
  private PooledConnectionFactory factory;


  /** Default constructor. */
  public PooledCompareAuthenticationHandler() {}


  /**
   * Creates a new pooled compare authentication handler.
   *
   * @param  cf  connection factory
   */
  public PooledCompareAuthenticationHandler(final PooledConnectionFactory cf)
  {
    setConnectionFactory(cf);
  }


  /** {@inheritDoc} */
  @Override
  public PooledConnectionFactory getConnectionFactory()
  {
    return factory;
  }


  /** {@inheritDoc} */
  @Override
  public void setConnectionFactory(final PooledConnectionFactory cf)
  {
    factory = cf;
  }


  /** {@inheritDoc} */
  @Override
  protected Connection getConnection()
    throws LdapException
  {
    return factory.getConnection();
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::factory=%s, passwordScheme=%s, controls=%s]",
        getClass().getName(),
        hashCode(),
        factory,
        getPasswordScheme(),
        Arrays.toString(getAuthenticationControls()));
  }
}
