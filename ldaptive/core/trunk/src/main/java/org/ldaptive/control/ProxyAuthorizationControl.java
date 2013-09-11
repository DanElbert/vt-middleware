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

import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.OctetStringType;

/**
 * Request control for proxy authorization. See RFC 4370. Control is defined as:
 *
 * <pre>
   controlValue ::= OCTET STRING  -- authorizationId
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ProxyAuthorizationControl extends AbstractControl
  implements RequestControl
{

  /** OID of this control. */
  public static final String OID = "2.16.840.1.113730.3.4.18";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 7001;

  /** empty byte array used for anonymous authz. */
  private static final byte[] EMPTY_AUTHZ = new byte[0];

  /** authorization identity. */
  private String authorizationId;


  /** Default constructor. */
  public ProxyAuthorizationControl()
  {
    super(OID, true);
  }


  /**
   * Creates a new proxy authorization control.
   *
   * @param  id  authorization identity
   */
  public ProxyAuthorizationControl(final String id)
  {
    super(OID, true);
    setAuthorizationId(id);
  }


  /**
   * Returns the authorization identity.
   *
   * @return  authorization identity
   */
  public String getAuthorizationId()
  {
    return authorizationId;
  }


  /**
   * Sets the authorization identity.
   *
   * @param  id  authorization identity
   */
  public void setAuthorizationId(final String id)
  {
    authorizationId = id;
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED,
        getOID(),
        getCriticality(),
        authorizationId);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::criticality=%s, authorizationId=%s]",
        getClass().getName(),
        hashCode(),
        getCriticality(),
        authorizationId);
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    return getAuthorizationId() != null ?
      OctetStringType.toBytes(getAuthorizationId()) : EMPTY_AUTHZ;
  }
}
