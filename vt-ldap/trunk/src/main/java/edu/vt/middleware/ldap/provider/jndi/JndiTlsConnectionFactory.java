/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.ldap.provider.jndi;

import java.io.IOException;
import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import edu.vt.middleware.ldap.Credential;
import edu.vt.middleware.ldap.LdapConfig;
import edu.vt.middleware.ldap.LdapException;
import edu.vt.middleware.ldap.ResultCode;
import edu.vt.middleware.ldap.auth.AuthenticationException;
import edu.vt.middleware.ldap.provider.Connection;
import edu.vt.middleware.ldap.provider.ConnectionException;

/**
 * Creates ldap connections using the JNDI {@link InitialLdapContext} class with
 * the start tls extended operation.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class JndiTlsConnectionFactory extends AbstractJndiConnectionFactory
{
  /** ldap socket factory used for SSL and TLS. */
  protected SSLSocketFactory sslSocketFactory;

  /** hostname verifier for TLS connections. */
  protected HostnameVerifier hostnameVerifier;

  /**
   * Whether to call {@link StartTlsResponse#close()} when {@link #close()} is
   * called.
   */
  private boolean stopTlsOnClose;


  /**
   * Creates a new jndi tls connection factory.
   *
   * @param  url  of the ldap to connect to
   */
  protected JndiTlsConnectionFactory(final String url)
  {
    if (url == null) {
      throw new IllegalArgumentException("LDAP URL cannot be null");
    }
    this.ldapUrl = url;
  }


  /**
   * Returns the SSL socket factory to use for TLS connections.
   *
   * @return  SSL socket factory
   */
  public SSLSocketFactory getSslSocketFactory()
  {
    return this.sslSocketFactory;
  }


  /**
   * Sets the SSL socket factory to use for TLS connections.
   *
   * @param  sf  SSL socket factory
   */
  public void setSslSocketFactory(final SSLSocketFactory sf)
  {
    this.sslSocketFactory = sf;
  }


  /**
   * Returns the hostname verifier to use for TLS connections.
   *
   * @return  hostname verifier
   */
  public HostnameVerifier getHostnameVerifier()
  {
    return this.hostnameVerifier;
  }


  /**
   * Sets the hostname verifier to use for TLS connections.
   *
   * @param  verifier  for hostnames
   */
  public void setHostnameVerifier(final HostnameVerifier verifier)
  {
    this.hostnameVerifier = verifier;
  }


  /**
   * Returns whether to call {@link StartTlsResponse#close()} when {@link
   * #close()} is called.
   *
   * @return  stop TLS on close
   */
  public boolean getStopTlsOnClose()
  {
    return this.stopTlsOnClose;
  }


  /**
   * Sets whether to call {@link StartTlsResponse#close()} when {@link #close()}
   * is called.
   *
   * @param  b  stop TLS on close
   */
  public void setStopTlsOnClose(final boolean b)
  {
    if (this.logger.isTraceEnabled()) {
      this.logger.trace("setting stopTlsOnClose: " + b);
    }
    this.stopTlsOnClose = b;
  }


  /** {@inheritDoc} */
  protected JndiTlsConnection createInternal(
    final String url, final String dn, final Credential credential)
    throws LdapException
  {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Bind with the following parameters:");
      this.logger.debug("  url = " + url);
      this.logger.debug("  authtype = " + this.authentication);
      this.logger.debug("  dn = " + dn);
      if (this.logCredentials) {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("  credential = " + credential);
        }
      } else {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("  credential = <suppressed>");
        }
      }
      if (this.logger.isTraceEnabled()) {
        this.logger.trace("  env = " + this.environment);
      }
    }

    final Hashtable<String, Object> env = new Hashtable<String, Object>(
      this.environment);
    env.put(PROVIDER_URL, url);
    if (this.tracePackets != null) {
      env.put(TRACE, this.tracePackets);
    }

    JndiTlsConnection conn = null;
    env.put(VERSION, "3");
    try {
      conn = new JndiTlsConnection(new InitialLdapContext(env, null));
      conn.setStartTlsResponse(this.startTls(conn.getLdapContext()));
      // note that when using simple authentication (the default),
      // if the credential is null the provider will automatically revert the
      // authentication to none
      conn.getLdapContext().addToEnvironment(
        AUTHENTICATION, this.authentication);
      if (dn != null) {
        conn.getLdapContext().addToEnvironment(PRINCIPAL, dn);
        if (credential != null) {
          conn.getLdapContext().addToEnvironment(
            CREDENTIALS, credential.getBytes());
        }
      }
      conn.getLdapContext().reconnect(null);
      conn.setRemoveDnUrls(this.removeDnUrls);
      conn.setOperationRetryExceptions(
        NamingExceptionUtil.getNamingExceptions(
          this.operationRetryResultCodes));
    } catch (javax.naming.AuthenticationException e) {
      try {
        this.destroy(conn);
      } catch (LdapException e2) {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Problem tearing down connection", e2);
        }
      }
      throw new AuthenticationException(e, ResultCode.INVALID_CREDENTIALS);
    } catch (NamingException e) {
      try {
        this.destroy(conn);
      } catch (LdapException e2) {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Problem tearing down connection", e2);
        }
      }
      throw new ConnectionException(
        e, NamingExceptionUtil.getResultCode(e.getClass()));
    } catch (IOException e) {
      try {
        this.destroy(conn);
      } catch (LdapException e2) {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Problem tearing down connection", e2);
        }
      }
      throw new ConnectionException(e);
    } catch (RuntimeException e) {
      try {
        this.destroy(conn);
      } catch (LdapException e2) {
        if (this.logger.isDebugEnabled()) {
          this.logger.debug("Problem tearing down connection", e2);
        }
      }
      throw e;
    }
    return conn;
  }


  /** {@inheritDoc} */
  public void destroy(final Connection conn)
    throws LdapException
  {
    if (conn != null) {
      final JndiTlsConnection jndiConn = (JndiTlsConnection) conn;
      try {
        if (this.stopTlsOnClose) {
          this.stopTls(jndiConn.getStartTlsResponse());
        }
      } catch (IOException e) {
        if (this.logger.isErrorEnabled()) {
          this.logger.error("Error stopping TLS", e);
        }
      } finally {
        super.destroy(conn);
      }
    }
  }


  /**
   * This will attempt the StartTLS extended operation on the supplied ldap
   * context.
   *
   * @param  ctx  ldap context
   *
   * @return  start tls response
   *
   * @throws  NamingException  if an error occurs while requesting an extended
   * operation
   * @throws  IOException  if an error occurs while negotiating TLS
   */
  public StartTlsResponse startTls(final LdapContext ctx)
    throws NamingException, IOException
  {
    final StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(
      new StartTlsRequest());
    if (this.hostnameVerifier != null) {
      if (this.logger.isTraceEnabled()) {
        this.logger.trace(
          "TLS hostnameVerifier = " + this.hostnameVerifier);
      }
      tls.setHostnameVerifier(this.hostnameVerifier);
    }
    if (this.sslSocketFactory != null) {
      if (this.logger.isTraceEnabled()) {
        this.logger.trace(
          "TLS sslSocketFactory = " + this.sslSocketFactory);
      }
      tls.negotiate(this.sslSocketFactory);
    } else {
      tls.negotiate();
    }
    return tls;
  }


  /**
   * This will attempt to StopTLS with the supplied start tls response.
   *
   * @param  tls  start tls response
   *
   * @throws  IOException  if an error occurs while closing the TLS
   * connection
   */
  public void stopTls(final StartTlsResponse tls)
    throws IOException
  {
    if (tls != null) {
      tls.close();
    }
  }


  /**
   * Creates a new instance of this connection factory.
   *
   * @param  lc  ldap configuration to read connection properties from
   * @return  jndi tls connection factory
   */
  public static JndiTlsConnectionFactory newInstance(final LdapConfig lc)
  {
    final JndiTlsConnectionFactory cf = new JndiTlsConnectionFactory(
      lc.getLdapUrl());
    cf.setAuthentication(lc.getAuthtype());
    cf.setEnvironment(createEnvironment(lc));
    cf.setLogCredentials(lc.getLogCredentials());
    cf.setSslSocketFactory(lc.getSslSocketFactory());
    cf.setHostnameVerifier(lc.getHostnameVerifier());
    if (lc.getConnectionStrategy() != null) {
      cf.setConnectionStrategy(lc.getConnectionStrategy());
    }
    return cf;
  }
}