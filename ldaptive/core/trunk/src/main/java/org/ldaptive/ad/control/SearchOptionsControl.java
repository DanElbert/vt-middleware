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
package org.ldaptive.ad.control;

import org.ldaptive.LdapUtils;
import org.ldaptive.asn1.ConstructedDEREncoder;
import org.ldaptive.asn1.IntegerType;
import org.ldaptive.asn1.UniversalDERTag;
import org.ldaptive.control.AbstractControl;
import org.ldaptive.control.RequestControl;

/**
 * Request control for active directory servers to control various search
 * behaviors. Control is defined as:
 *
 * <pre>
     searchOptionsValue ::= SEQUENCE {
       flag  INTEGER
     }
 * </pre>
 *
 * See http://msdn.microsoft.com/en-us/library/cc223324.aspx
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class SearchOptionsControl extends AbstractControl
  implements RequestControl
{

  /** OID of this control. */
  public static final String OID = "1.2.840.113556.1.4.1340";

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 953;

  /** Types of flags. */
  public enum Flag {

    /** SERVER_SEARCH_FLAG_DOMAIN_SCOPE . */
    DOMAIN_SCOPE,

    /** SERVER_SEARCH_FLAG_PHANTOM_ROOT . */
    PHANTOM_ROOT
  }

  /** flag. */
  private Flag flag = Flag.DOMAIN_SCOPE;


  /** Default constructor. */
  public SearchOptionsControl()
  {
    super(OID);
  }


  /**
   * Creates a new search options control.
   *
   * @param  f  flag
   */
  public SearchOptionsControl(final Flag f)
  {
    super(OID);
    setFlag(f);
  }


  /**
   * Creates a new search options control.
   *
   * @param  f  flag
   * @param  critical  whether this control is critical
   */
  public SearchOptionsControl(final Flag f, final boolean critical)
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
        HASH_CODE_SEED,
        getOID(),
        getCriticality(),
        flag);
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
