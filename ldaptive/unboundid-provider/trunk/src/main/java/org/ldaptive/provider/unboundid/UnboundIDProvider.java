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
package org.ldaptive.provider.unboundid;

import java.security.GeneralSecurityException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.LdapURL;
import org.ldaptive.provider.ConnectionFactory;
import org.ldaptive.provider.Provider;
import org.ldaptive.ssl.CredentialConfig;
import org.ldaptive.ssl.DefaultHostnameVerifier;
import org.ldaptive.ssl.DefaultSSLContextInitializer;
import org.ldaptive.ssl.HostnameVerifyingTrustManager;
import org.ldaptive.ssl.SSLContextInitializer;
import org.ldaptive.ssl.TLSSocketFactory;

/**
 * UnboundID provider implementation. Provides connection factories for clear,
 * SSL, and TLS connections.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class UnboundIDProvider implements Provider<UnboundIDProviderConfig>
{

  /** Provider configuration. */
  private UnboundIDProviderConfig config = new UnboundIDProviderConfig();


  /** {@inheritDoc} */
  @Override
  public ConnectionFactory<UnboundIDProviderConfig> getConnectionFactory(
    final ConnectionConfig cc)
  {
    SocketFactory factory = config.getSocketFactory();
    SSLContext sslContext = null;
    // Unboundid does not do hostname verification by default
    // set a default hostname verifier if no trust settings have been configured
    if (cc.getUseStartTLS()) {
      sslContext = getHostnameVerifierSSLContext(cc);
    } else if (cc.getUseSSL() && factory == null) {
      factory = getHostnameVerifierSocketFactory(cc);
    }
    if (cc.getSslConfig() != null &&
        cc.getSslConfig().getEnabledCipherSuites() != null) {
      throw new UnsupportedOperationException(
        "UnboundID provider does not support the cipher suites property");
    }
    if (cc.getSslConfig() != null &&
        cc.getSslConfig().getEnabledProtocols() != null) {
      throw new UnsupportedOperationException(
        "UnboundID provider does not support the protocols property");
    }
    LDAPConnectionOptions options = config.getConnectionOptions();
    if (options == null) {
      options = getDefaultLDAPConnectionOptions(cc);
    }
    ConnectionFactory<UnboundIDProviderConfig> cf;
    if (cc.getUseStartTLS()) {
      cf = new UnboundIDStartTLSConnectionFactory(
        cc.getLdapUrl(), config, factory, sslContext, options);
    } else {
      cf = new UnboundIDConnectionFactory(
        cc.getLdapUrl(), config, factory, options);
    }
    return cf;
  }


  /**
   * Returns an SSLContext configured with a default hostname verifier. Uses a
   * {@link DefaultHostnameVerifier} if no trust managers have been configured.
   *
   * @param  cc  connection configuration
   *
   * @return  SSL Context
   */
  protected SSLContext getHostnameVerifierSSLContext(final ConnectionConfig cc)
  {
    SSLContext sslContext;
    SSLContextInitializer contextInit;
    if (cc.getSslConfig() != null &&
        cc.getSslConfig().getCredentialConfig() != null) {
      try {
        final CredentialConfig credConfig =
          cc.getSslConfig().getCredentialConfig();
        contextInit = credConfig.createSSLContextInitializer();
      } catch (GeneralSecurityException e) {
        throw new IllegalArgumentException(e);
      }
    } else {
      contextInit = new DefaultSSLContextInitializer();
    }
    if (cc.getSslConfig() != null &&
        cc.getSslConfig().getTrustManagers() != null) {
      contextInit.setTrustManagers(cc.getSslConfig().getTrustManagers());
    } else {
      final LdapURL ldapUrl = new LdapURL(cc.getLdapUrl());
      contextInit.setTrustManagers(
        new HostnameVerifyingTrustManager(
          new DefaultHostnameVerifier(), ldapUrl.getEntriesAsString()));
    }
    try {
      sslContext = contextInit.initSSLContext("TLS");
    } catch (GeneralSecurityException e) {
      throw new IllegalArgumentException(e);
    }
    return sslContext;
  }


  /**
   * Returns an SSL socket factory configured with a default hostname verifier.
   *
   * @param  cc  connection configuration
   *
   * @return  SSL socket factory
   */
  protected SocketFactory getHostnameVerifierSocketFactory(
    final ConnectionConfig cc)
  {
    final LdapURL ldapUrl = new LdapURL(cc.getLdapUrl());
    return TLSSocketFactory.getHostnameVerifierFactory(
      cc.getSslConfig(), ldapUrl.getEntriesAsString());
  }


  /**
   * Returns the default connection options for this provider.
   *
   * @param  cc  to configure options with
   *
   * @return  ldap connection options
   */
  protected LDAPConnectionOptions getDefaultLDAPConnectionOptions(
    final ConnectionConfig cc)
  {
    final LDAPConnectionOptions options = new LDAPConnectionOptions();
    options.setConnectTimeoutMillis(
      cc.getConnectTimeout() > 0 ? (int) cc.getConnectTimeout() : 0);
    options.setResponseTimeoutMillis(cc.getResponseTimeout());
    return options;
  }


  /** {@inheritDoc} */
  @Override
  public UnboundIDProviderConfig getProviderConfig()
  {
    return config;
  }


  /** {@inheritDoc} */
  @Override
  public void setProviderConfig(final UnboundIDProviderConfig jpc)
  {
    config = jpc;
  }


  /** {@inheritDoc} */
  @Override
  public UnboundIDProvider newInstance()
  {
    return new UnboundIDProvider();
  }
}
