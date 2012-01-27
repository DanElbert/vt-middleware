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
package org.ldaptive.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.X509TrustManager;
import org.ldaptive.LdapUtil;

/**
 * Trust manager that delegates to {@link CertificateHostnameVerifier}. Any name
 * that verifies passes this trust manager check.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class HostnameVerifyingTrustManager implements X509TrustManager
{

  /** Hostnames to allow. */
  private String[] hostnames;

  /** Hostname verifier to use for trust. */
  private CertificateHostnameVerifier hostnameVerifier;


  /**
   * Creates a new hostname verifying trust manager. Scheme and port will be
   * removed from names if found.
   *
   * @param  verifier  that establishes trust
   * @param  names  to match against a certificate
   */
  public HostnameVerifyingTrustManager(
    final CertificateHostnameVerifier verifier,
    final String... names)
  {
    if (names != null) {
      hostnames = new String[names.length];
      for (int i = 0; i < names.length; i++) {
        final String[][] hostnameAndPort = LdapUtil.getHostnameAndPort(
          names[i]);
        if (hostnameAndPort.length > 1) {
          throw new IllegalArgumentException("Invalid hostname: " + names[i]);
        }
        hostnames[i] = hostnameAndPort[0][0].toLowerCase();
      }
    }
    hostnameVerifier = verifier;
  }


  /** {@inheritDoc} */
  @Override
  public void checkClientTrusted(
    final X509Certificate[] chain, final String authType)
    throws CertificateException
  {
    checkCertificateTrusted(chain[0]);
  }


  /** {@inheritDoc} */
  @Override
  public void checkServerTrusted(
    final X509Certificate[] chain, final String authType)
    throws CertificateException
  {
    checkCertificateTrusted(chain[0]);
  }


  /**
   * Verifies the supplied certificate using the hostname verifier with each
   * hostname.
   *
   * @param  cert  to verify
   *
   * @throws  CertificateException  if none of the hostnames verify
   */
  private void checkCertificateTrusted(final X509Certificate cert)
    throws CertificateException
  {
    for (String name : hostnames) {
      if (hostnameVerifier.verify(name, cert)) {
        return;
      }
    }
    throw new CertificateException(
      String.format(
        "Hostname '%s' does not match the hostname in the server's " +
        "certificate", Arrays.toString(hostnames)));
  }


  /** {@inheritDoc} */
  @Override
  public X509Certificate[] getAcceptedIssuers()
  {
    return new X509Certificate[0];
  }
}
