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
package org.ldaptive.ad.control;

import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.ConstructedDEREncoder;
import org.ldaptive.asn1.IntegerType;
import org.ldaptive.asn1.UniversalDERTag;
import org.ldaptive.control.AbstractControl;
import org.ldaptive.control.RequestControl;

/**
 * Request control for active directory servers to use an extended form of an
 * object distinguished name. Control is defined as:
 *
 * <pre>
    extendedDnValue ::= SEQUENCE {
          flag  INTEGER
    }
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ExtendedDnControl extends AbstractControl
  implements RequestControl
{

  /** OID of this control. */
  public static final String OID = "1.2.840.113556.1.4.529";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 919;

  /** Types of flags. */
  public enum Flag {

    /** hexadecimal format. */
    HEXADECIMAL,

    /** standard format. */
    STANDARD
  }

  /** flags. */
  private Flag flag = Flag.STANDARD;


  /** Default constructor. */
  public ExtendedDnControl()
  {
    super(OID);
  }


  /**
   * Creates a new extended dn control.
   *
   * @param  f  flag
   */
  public ExtendedDnControl(final Flag f)
  {
    super(OID);
    setFlag(f);
  }


  /**
   * Creates a new extended dn control.
   *
   * @param  f  flag
   * @param  critical  whether this control is critical
   */
  public ExtendedDnControl(final Flag f, final boolean critical)
  {
    super(OID, critical);
    setFlag(f);
  }


  /**
   * Returns the flag.
   *
   * @return  flag
   */
  public Flag getFlag()
  {
    return flag;
  }


  /**
   * Sets the flag.
   *
   * @param  f  flag
   */
  public void setFlag(final Flag f)
  {
    flag = f;
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return
      LdapUtils.computeHashCode(
        HASH_CODE_SEED, getOID(), getCriticality(), flag);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::criticality=%s, flag=%s]",
        getClass().getName(),
        hashCode(),
        getCriticality(),
        flag);
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    final ConstructedDEREncoder se = new ConstructedDEREncoder(
      UniversalDERTag.SEQ,
      new IntegerType(getFlag().ordinal()));
    return se.encode();
  }
}
