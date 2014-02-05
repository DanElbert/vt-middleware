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
package org.ldaptive.ssl;

import java.security.GeneralSecurityException;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 * TLSSocketFactory implementation that uses a thread local variable to store
 * configuration. Useful for SSL configurations that can only retrieve the
 * SSLSocketFactory from getDefault().
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ThreadLocalTLSSocketFactory extends TLSSocketFactory
{

  /** Thread local instance of the ssl config. */
  private static final ThreadLocalSslConfig THREAD_LOCAL_SSL_CONFIG =
    new ThreadLocalSslConfig();


  /** {@inheritDoc} */
  @Override
  public SslConfig getSslConfig()
  {
    return THREAD_LOCAL_SSL_CONFIG.get();
  }


  /** {@inheritDoc} */
  @Override
  public void setSslConfig(final SslConfig config)
  {
    THREAD_LOCAL_SSL_CONFIG.set(config);
  }


  /**
   * This returns the default SSL socket factory.
   *
   * @return  socket factory
   */
  public static SocketFactory getDefault()
  {
    final ThreadLocalTLSSocketFactory sf = new ThreadLocalTLSSocketFactory();
    if (sf.getSslConfig() == null) {
      throw new NullPointerException("Thread local SslConfig has not been set");
    }
    try {
      sf.initialize();
    } catch (GeneralSecurityException e) {
      throw new IllegalArgumentException(
        "Error initializing socket factory",
        e);
    }
    return sf;
  }


  /**
   * Returns an instance of this socket factory configured with a hostname
   * verifying trust manager. If the supplied ssl config does not contain a
   * trust managers, {@link HostnameVerifyingTrustManager} with {@link
   * DefaultHostnameVerifier} is set.
   *
   * @param  config  to set on the socket factory
   * @param  names  to use for hostname verification
   *
   * @return  socket factory
   */
  public static SSLSocketFactory getHostnameVerifierFactory(
    final SslConfig config,
    final String[] names)
  {
    final ThreadLocalTLSSocketFactory sf = new ThreadLocalTLSSocketFactory();
    if (config != null && !config.isEmpty()) {
      sf.setSslConfig(SslConfig.newSslConfig(config));
      if (sf.getSslConfig().getTrustManagers() == null) {
        sf.getSslConfig().setTrustManagers(
          new HostnameVerifyingTrustManager(
            new DefaultHostnameVerifier(),
            names));
      }
    } else {
      sf.setSslConfig(
        new SslConfig(
          new HostnameVerifyingTrustManager(
            new DefaultHostnameVerifier(),
            names)));
    }
    try {
      sf.initialize();
    } catch (GeneralSecurityException e) {
      throw new IllegalArgumentException(e);
    }
    return sf;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::factory=%s, sslConfig=%s]",
        getClass().getName(),
        hashCode(),
        getFactory(),
        getSslConfig());
  }


  /** Thread local class for {@link SslConfig}. */
  private static class ThreadLocalSslConfig extends ThreadLocal<SslConfig> {}
}
