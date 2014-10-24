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
package org.ldaptive.asn1;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.ldaptive.LdapUtils;

/**
 * A sequence of RDN values.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class DN implements DEREncoder
{

  /** hash code seed. */
  private static final int HASH_CODE_SEED = 631;

  /** RDNs in this RDN sequence. */
  private final RDN[] rdns;


  /**
   * Creates a new DN.
   *
   * @param  names  RDNs in this sequence
   */
  public DN(final Collection<RDN> names)
  {
    rdns = names.toArray(new RDN[names.size()]);
  }


  /**
   * Creates a new DN.
   *
   * @param  names  RDNs in this sequence
   */
  public DN(final RDN... names)
  {
    rdns = names;
  }


  /**
   * Returns the RDNs in this DN.
   *
   * @return  RDNs
   */
  public RDN[] getRDNs()
  {
    return rdns;
  }


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    final List<DEREncoder> typeEncoders = new ArrayList<DEREncoder>();
    for (final RDN rdn : rdns) {
      typeEncoders.add(
        new DEREncoder() {
          @Override
          public byte[] encode()
          {
            return rdn.encode();
          }
        });
    }

    final ConstructedDEREncoder se = new ConstructedDEREncoder(
      UniversalDERTag.SEQ,
      typeEncoders.toArray(new DEREncoder[typeEncoders.size()]));
    return se.encode();
  }


  /**
   * Converts bytes in the buffer to a DN by reading from the current position
   * to the limit.
   *
   * @param  encoded  buffer containing DER-encoded data where the buffer is
   * positioned at the tag of the DN and the limit is set beyond the last byte
   * of DN data.
   *
   * @return  decoded bytes as a DN
   */
  public static DN decode(final ByteBuffer encoded)
  {
    return new DN(RDN.decode(encoded));
  }


  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o)
  {
    return LdapUtils.areEqual(this, o);
  }


  /** {@inheritDoc} */
  @Override
  public int hashCode()
  {
    return LdapUtils.computeHashCode(HASH_CODE_SEED, (Object) rdns);
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::rdns=%s]",
        getClass().getName(),
        hashCode(),
        Arrays.toString(rdns));
  }
}
