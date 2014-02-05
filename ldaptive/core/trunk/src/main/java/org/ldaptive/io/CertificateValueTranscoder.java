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
package org.ldaptive.io;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import org.ldaptive.LdapUtils;

/**
 * Decodes and encodes a certificate for use in an ldap attribute value.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class CertificateValueTranscoder implements ValueTranscoder<Certificate>
{

  /** PEM cert header. */
  private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----" +
    System.getProperty("line.separator");

  /** PEM cert footer. */
  private static final String END_CERT = System.getProperty("line.separator") +
    "-----END CERTIFICATE-----";


  /** {@inheritDoc} */
  @Override
  public Certificate decodeStringValue(final String value)
  {
    return decodeBinaryValue(LdapUtils.utf8Encode(value));
  }


  /** {@inheritDoc} */
  @Override
  public Certificate decodeBinaryValue(final byte[] value)
  {
    try {
      final CertificateFactory cf = CertificateFactory.getInstance("X.509");
      return cf.generateCertificate(new ByteArrayInputStream(value));
    } catch (CertificateException e) {
      throw new IllegalArgumentException(
        "Attribute value could not be decoded as a certificate",
        e);
    }
  }


  /** {@inheritDoc} */
  @Override
  public String encodeStringValue(final Certificate value)
  {
    final StringBuilder sb = new StringBuilder();
    sb.append(BEGIN_CERT);
    sb.append(LdapUtils.base64Encode(encodeBinaryValue(value)));
    sb.append(END_CERT);
    return sb.toString();
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encodeBinaryValue(final Certificate value)
  {
    try {
      return value.getEncoded();
    } catch (CertificateEncodingException e) {
      throw new IllegalArgumentException("Certificate could not be encoded", e);
    }
  }


  /** {@inheritDoc} */
  @Override
  public Class<Certificate> getType()
  {
    return Certificate.class;
  }
}
