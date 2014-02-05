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
package org.ldaptive.ad.control;

import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.ConstructedDEREncoder;
import org.ldaptive.asn1.IntegerType;
import org.ldaptive.asn1.OctetStringType;
import org.ldaptive.asn1.UniversalDERTag;
import org.ldaptive.control.AbstractControl;
import org.ldaptive.control.RequestControl;

/**
 * Request control for active directory servers to use an extended form of an
 * object distinguished name. Control is defined as:
 *
 * <pre>
   verifyNameValue ::= SEQUENCE {
     Flags       INTEGER
     ServerName  OCTET STRING
   }
 * </pre>
 *
 * <p>See http://msdn.microsoft.com/en-us/library/cc223328.aspx</p>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class VerifyNameControl extends AbstractControl implements RequestControl
{

  /** OID of this control. */
  public static final String OID = "1.2.840.113556.1.4.1338";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 977;

  /** Global catalog server to contact. */
  private String serverName;


  /** Default constructor. */
  public VerifyNameControl()
  {
    super(OID);
  }


  /**
   * Creates a new verify name control.
   *
   * @param  name  server name
   */
  public VerifyNameControl(final String name)
  {
    super(OID);
    setServerName(name);
  }


  /**
   * Creates a new verify name control.
   *
   * @param  name  server name
   * @param  critical  whether this control is critical
   */
  public VerifyNameControl(final String name, final boolean critical)
  {
    super(OID, critical);
    setServerName(name);
  }


  /**
   * Returns the server name.
   *
   * @return  server name
   */
  public String getServerName()
  {
    return serverName;
  }


  /**
   * Sets the server name.
   *
   * @param  name  server name
   */
  public void setServerName(final String name)
  {
    serverName = name;
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
        serverName);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::criticality=%s, serverName=%s]",
        getClass().getName(),
        hashCode(),
        getCriticality(),
        serverName);
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    final ConstructedDEREncoder se = new ConstructedDEREncoder(
      UniversalDERTag.SEQ,
      new IntegerType(0),
      new OctetStringType(serverName));
    return se.encode();
  }
}
