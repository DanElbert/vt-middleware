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
package org.ldaptive.asn1;

/**
 * Generic application-specific tag.
 *
 * @author  Middleware Services
 * @version  $Revision: $
 */
public class ApplicationDERTag extends AbstractDERTag
{

  /** Generic tag name "APP" for a application-specific type. */
  public static final String TAG_NAME = "APP";

  /** Application class is 01b in first two high-order bits. */
  public static final int TAG_CLASS = 0x40;


  /**
   * Creates a new application-specific tag with given tag number.
   *
   * @param  number  Tag number.
   * @param  isConstructed  True for constructed tag, false otherwise.
   */
  public ApplicationDERTag(final int number, final boolean isConstructed)
  {
    super(number, isConstructed);
  }


  /** {@inheritDoc} */
  @Override
  public String name()
  {
    return String.format("%s(%s)", TAG_NAME, getTagNo());
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return name();
  }
}
