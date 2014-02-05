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
package org.ldaptive.ad;

import java.util.Collection;
import org.ldaptive.LdapAttribute;
import org.ldaptive.SortBehavior;
import org.ldaptive.ad.io.UnicodePwdValueTranscoder;

/**
 * Helper class for the active directory unicodePwd attribute. Configures a
 * binary attribute of that name and allows setting of the attribute value using
 * a string. See {@link UnicodePwdValueTranscoder}.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class UnicodePwdAttribute extends LdapAttribute
{

  /** serial version uid. */
  private static final long serialVersionUID = -6140237711183070669L;

  /** name of this attribute. */
  private static final String ATTR_NAME = "unicodePwd";

  /** transcoder used when adding string values. */
  private static final UnicodePwdValueTranscoder TRANSCODER =
    new UnicodePwdValueTranscoder();


  /** Default constructor. */
  public UnicodePwdAttribute()
  {
    super(SortBehavior.UNORDERED, true);
    setName(ATTR_NAME);
  }


  /**
   * Creates a new unicode pwd attribute.
   *
   * @param  values  of this attribute
   */
  public UnicodePwdAttribute(final String... values)
  {
    this();
    addStringValue(values);
  }


  /** {@inheritDoc} */
  @Override
  public Collection<String> getStringValues()
  {
    return getValues(TRANSCODER);
  }


  /** {@inheritDoc} */
  @Override
  public void addStringValue(final String... value)
  {
    for (String s : value) {
      addValue(TRANSCODER, value);
    }
  }


  /** {@inheritDoc} */
  @Override
  public void removeStringValue(final String... value)
  {
    for (String s : value) {
      removeBinaryValue(TRANSCODER.encodeBinaryValue(s));
    }
  }
}
