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
package org.ldaptive.provider.apache;

import org.apache.directory.api.ldap.model.exception.LdapOperationException;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.ldaptive.LdapException;
import org.ldaptive.LdapURL;
import org.ldaptive.ResultCode;
import org.ldaptive.provider.AbstractProviderConnectionFactory;
import org.ldaptive.provider.ConnectionException;

/**
 * Creates ldap connections using the Apache LdapNetworkConnection class.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ApacheLdapConnectionFactory
  extends AbstractProviderConnectionFactory<ApacheLdapProviderConfig>
{

  /** Connection configuration. */
  private final LdapConnectionConfig ldapConnectionConfig;

  /** Whether to startTLS on connections. */
  private final boolean useStartTLS;

  /** Timeout for responses. */
  private final long responseTimeOut;


  /**
   * Creates a new Apache LDAP connection factory.
   *
   * @param  url  of the ldap to connect to
   * @param  config  provider configuration
   * @param  lcc  connection configuration
   * @param  tls  whether to startTLS on connections
   * @param  timeOut  timeout for responses
   */
  public ApacheLdapConnectionFactory(
    final String url,
    final ApacheLdapProviderConfig config,
    final LdapConnectionConfig lcc,
    final boolean tls,
    final long timeOut)
  {
    super(url, config);
    ldapConnectionConfig = lcc;
    useStartTLS = tls;
    responseTimeOut = timeOut;
  }


  /** {@inheritDoc} */
  @Override
  protected ApacheLdapConnection createInternal(final String url)
    throws LdapException
  {
    final LdapURL ldapUrl = new LdapURL(url);
    ldapConnectionConfig.setLdapHost(ldapUrl.getLastEntry().getHostname());
    ldapConnectionConfig.setLdapPort(ldapUrl.getLastEntry().getPort());

    ApacheLdapConnection conn = null;
    boolean closeConn = false;
    try {
      final LdapNetworkConnection lc = new LdapNetworkConnection(
        ldapConnectionConfig);
      conn = new ApacheLdapConnection(lc, getProviderConfig());
      lc.connect();
      if (useStartTLS) {
        lc.startTls();
      }
      if (responseTimeOut > 0) {
        lc.setTimeOut(responseTimeOut);
      }
    } catch (LdapOperationException e) {
      closeConn = true;
      throw new ConnectionException(
        e,
        ResultCode.valueOf(e.getResultCode().getValue()));
    } catch (org.apache.directory.api.ldap.model.exception.LdapException e) {
      closeConn = true;
      throw new ConnectionException(e);
    } catch (RuntimeException e) {
      closeConn = true;
      throw e;
    } finally {
      if (closeConn) {
        try {
          if (conn != null) {
            conn.close(null);
          }
        } catch (LdapException e) {
          logger.debug("Problem tearing down connection", e);
        }
      }
    }
    return conn;
  }
}
