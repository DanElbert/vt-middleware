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
package org.ldaptive.io;

import org.ldaptive.LdapUtils;

/**
 * Value transcoder which decodes and encodes to a byte array and therefore the
 * string methods simply delegate to the binary methods.
 *
 * @param  <T>  type of object to transcode
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class AbstractBinaryValueTranscoder<T>
  implements ValueTranscoder<T>
{


  /** {@inheritDoc} */
  @Override
  public T decodeStringValue(final String value)
  {
    return decodeBinaryValue(LdapUtils.utf8Encode(value));
  }


  /** {@inheritDoc} */
  @Override
  public String encodeStringValue(final T value)
  {
    return LdapUtils.utf8Encode(encodeBinaryValue(value));
  }
}
