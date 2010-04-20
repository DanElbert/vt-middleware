/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Sean C. Sullivan
  Author:  Middleware Services
  Email:   sean@seansullivan.com
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.password;

import java.io.IOException;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <code>PasswordGenerator</code> creates password that meet password rule
 * criteria.
 *
 * @author  Sean C. Sullivan
 * @author  Middleware Services
 * @version  $Revision$
 */
public class PasswordGenerator
{

  /** All digits. */
  protected StringBuilder digits = new StringBuilder("0123456789");

  /** All lowercase characters. */
  protected StringBuilder lowercase = new StringBuilder(
    "abcdefghijklmnopqrstuvwxyz");

  /** All uppercase characters. */
  protected StringBuilder uppercase = new StringBuilder(
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

  /**
   * All non-alphanumeric characters. does not include backslash, pipe, single
   * quote, or double quote
   */
  protected StringBuilder nonAlphanumeric = new StringBuilder(
    "`~!@#$%^&*()-_=+[{]};:<,>./?");

  /** All uppercase and lowercase characters. */
  private StringBuilder alphabetical = new StringBuilder(this.lowercase).append(
    this.uppercase);

  /** All alphabetical and digit characters. */
  private StringBuilder alphanumeric =
    new StringBuilder(this.digits).append(this.alphabetical);

  /** All characters. */
  private StringBuilder all;

  /** Source of random data. */
  private Random random;


  /**
   * Default constructor. Instantiates a <code>SecureRandom</code> for password
   * generation.
   */
  public PasswordGenerator()
  {
    this(new SecureRandom());
  }


  /**
   * Creates a new <code>PasswordGenerator</code> with the supplied random.
   *
   * @param  r  <code>Random</code>
   */
  public PasswordGenerator(final Random r)
  {
    this.random = r;
    this.all = new StringBuilder(this.alphanumeric).append(
      this.nonAlphanumeric);
  }


  /**
   * Generates a password of the supplied length which meets the requirements of
   * the supplied password rule. For length to be evaluated it must be greater
   * than the number of characters defined in the character rule.
   *
   * @param  length  <code>int</code>
   * @param  rule  <code>PasswordCharacterRule</code>
   *
   * @return  <code>String</code> - generated password
   */
  public String generatePassword(
    final int length,
    final PasswordCharacterRule rule)
  {
    if (length <= 0) {
      throw new IllegalArgumentException("length must be greater than 0");
    }

    final CharBuffer buffer = CharBuffer.allocate(length);
    if (rule != null) {
      fillRandomCharacters(this.lowercase, rule.getNumberOfLowercase(), buffer);
      fillRandomCharacters(this.uppercase, rule.getNumberOfUppercase(), buffer);
      fillRandomCharacters(
        this.alphabetical,
        rule.getNumberOfAlphabetical(),
        buffer);
      fillRandomCharacters(this.digits, rule.getNumberOfDigits(), buffer);
      fillRandomCharacters(
        this.nonAlphanumeric,
        rule.getNumberOfNonAlphanumeric(),
        buffer);
    }
    fillRandomCharacters(this.all, length - buffer.position(), buffer);
    buffer.flip();
    randomize(buffer);
    return buffer.toString();
  }


  /**
   * Fills the supplied target with count random characters from source.
   *
   * @param  source  <code>CharSequence</code> of random characters.
   * @param  count  <code>int</code> number of random characters.
   * @param  target  <code>Appendable</code> character sequence that will hold
   * characters.
   */
  protected void fillRandomCharacters(
    final CharSequence source,
    final int count,
    final Appendable target)
  {
    for (int i = 0; i < count; i++) {
      try {
        target.append(source.charAt(this.random.nextInt(source.length())));
      } catch (IOException e) {
        throw new RuntimeException("Error appending characters.", e);
      }
    }
  }


  /**
   * Randomizes the contents of the given buffer.
   *
   * @param  buffer  Character buffer whose contents will be randomized.
   */
  protected void randomize(final CharBuffer buffer)
  {
    char c;
    int n;
    for (int i = buffer.position(); i < buffer.limit(); i++) {
      n = this.random.nextInt(buffer.length());
      c = buffer.get(n);
      buffer.put(n, buffer.get(i));
      buffer.put(i, c);
    }
  }
}
