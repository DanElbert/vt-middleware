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
package org.ldaptive.ad.extended;

import org.ldaptive.extended.AbstractExtendedResponse;

/**
 * Contains the response from a fast bind operation.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class FastBindResponse extends AbstractExtendedResponse<Void>
{


  /** {@inheritDoc} */
  @Override
  public String getOID()
  {
    return null;
  }


  /** {@inheritDoc} */
  @Override
  public void decode(final byte[] encoded) {}


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return String.format("[%s@%d]", getClass().getName(), hashCode());
  }
}
