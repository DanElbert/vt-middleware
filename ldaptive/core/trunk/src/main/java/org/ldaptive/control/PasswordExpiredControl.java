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
package org.ldaptive.control;

import java.nio.ByteBuffer;
import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.OctetStringType;

/**
 * Response control indicating an expired password. See
 * http://tools.ietf.org/html/draft-vchu-ldap-pwd-policy-00. Control is
 * defined as:
 *
 * <pre>
   controlValue ::= OCTET STRING  -- always "0"
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class PasswordExpiredControl extends AbstractControl
  implements ResponseControl
{

  /** OID of this control. */
  public static final String OID = "2.16.840.1.113730.3.4.4";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 787;


  /** Default constructor. */
  public PasswordExpiredControl()
  {
    super(OID);
  }


  /**
   * Creates a new password expired control.
   *
   * @param  critical  whether this control is critical
   */
  public PasswordExpiredControl(final boolean critical)
  {
    super(OID, critical);
  }


  /** {@inheritDoc} */
  @Override
  public String getOID()
  {
    return OID;
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        getOID(),
        getCriticality());
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::criticality=%s]",
        getClass().getName(),
        hashCode(),
        getCriticality());
  }


  /** {@inheritDoc} */
  @Override
  public void decode(final byte[] berValue)
  {
    logger.trace("decoding control: {}", LdapUtils.base64Encode(berValue));
    final String value = OctetStringType.decode(ByteBuffer.wrap(berValue));
    if (!"0".equals(value)) {
      throw new IllegalArgumentException(
        "Response control value should always be '0'");
    }
  }
}
