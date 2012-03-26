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
package org.ldaptive.provider.jndi;

import java.io.IOException;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsResponse;
import org.ldaptive.LdapException;

/**
 * JNDI provider implementation of ldap operations using startTLS.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class JndiStartTLSConnection extends JndiConnection
{

  /** Start TLS response. */
  private StartTlsResponse startTlsResponse;

  /**
   * Whether to call {@link StartTlsResponse#close()} when {@link #close()} is
   * called.
   */
  private boolean stopTLSOnClose;


  /**
   * Creates a new jndi startTLS connection.
   *
   * @param  lc  ldap context
   * @param  pc  provider configuration
   */
  public JndiStartTLSConnection(
    final LdapContext lc, final JndiProviderConfig pc)
  {
    super(lc, pc);
  }


  /**
   * Creates a new jndi startTLS connection.
   *
   * @param  lc  ldap contxt
   * @param  pc  provider configuration
   * @param  tlsResponse  of successful TLS handshake
   */
  public JndiStartTLSConnection(
    final LdapContext lc,
    final JndiProviderConfig pc,
    final StartTlsResponse tlsResponse)
  {
    super(lc, pc);
    startTlsResponse = tlsResponse;
  }


  /**
   * Returns whether to call {@link StartTlsResponse#close()} when {@link
   * #close()} is called.
   *
   * @return  stop TLS on close
   */
  public boolean getStopTLSOnClose()
  {
    return stopTLSOnClose;
  }


  /**
   * Sets whether to call {@link StartTlsResponse#close()} when {@link #close()}
   * is called.
   *
   * @param  b  stop TLS on close
   */
  public void setStopTLSOnClose(final boolean b)
  {
    logger.trace("setting stopTLSOnClose: {}", b);
    stopTLSOnClose = b;
  }


  /**
   * Returns the start tls response used by this connection.
   *
   * @return  start tls response
   */
  public StartTlsResponse getStartTlsResponse()
  {
    return startTlsResponse;
  }


  /**
   * Sets the start tls response.
   *
   * @param  str  start tls response
   */
  public void setStartTlsResponse(final StartTlsResponse str)
  {
    startTlsResponse = str;
  }


  /** {@inheritDoc} */
  @Override
  public void close()
    throws LdapException
  {
    try {
      if (stopTLSOnClose) {
        if (startTlsResponse != null) {
          startTlsResponse.close();
        }
      }
    } catch (IOException e) {
      logger.error("Error stopping TLS", e);
    } finally {
      startTlsResponse = null;
      super.close();
    }
  }
}
