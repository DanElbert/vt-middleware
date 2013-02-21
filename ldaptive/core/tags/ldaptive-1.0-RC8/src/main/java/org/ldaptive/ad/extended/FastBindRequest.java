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
package org.ldaptive.ad.extended;

import java.util.Arrays;
import org.ldaptive.AbstractRequest;
import org.ldaptive.extended.ExtendedRequest;

/**
 * Contains the data required to perform a fast bind operation.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class FastBindRequest extends AbstractRequest implements ExtendedRequest
{

  /** OID of this extended request. */
  public static final String OID = "1.2.840.113556.1.4.1781";


  /** {@inheritDoc} */
  @Override
  public byte[] encode()
  {
    return null;
  }


  /** {@inheritDoc} */
  @Override
  public String getOID()
  {
    return OID;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::controls=%s]",
        getClass().getName(),
        hashCode(),
        Arrays.toString(getControls()));
  }
}
