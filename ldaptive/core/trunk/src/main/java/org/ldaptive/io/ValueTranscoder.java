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
package org.ldaptive.io;

/**
 * Interface for decoding and encoding custom types for ldap attribute values.
 *
 * @param  <T>  type of value
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public interface ValueTranscoder<T>
{


  /**
   * Decodes the supplied ldap attribute value into a custom type.
   *
   * @param  value  to decode
   *
   * @return  decoded value
   */
  T decodeStringValue(String value);


  /**
   * Decodes the supplied ldap attribute value into a custom type.
   *
   * @param  value  to decode
   *
   * @return  decoded value
   */
  T decodeBinaryValue(byte[] value);


  /**
   * Encodes the supplied value into an ldap attribute value.
   *
   * @param  value  to encode
   *
   * @return  encoded value
   */
  String encodeStringValue(T value);


  /**
   * Encodes the supplied value into an ldap attribute value.
   *
   * @param  value  to encode
   *
   * @return  encoded value
   */
  byte[] encodeBinaryValue(T value);


  /**
   * Returns the type produced by this value transcoder.
   *
   * @return  type produced by this value transcoder
   */
  Class<T> getType();
}
