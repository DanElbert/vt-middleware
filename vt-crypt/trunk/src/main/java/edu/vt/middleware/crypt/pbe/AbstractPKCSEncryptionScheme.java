/*
  $Id$

  Copyright (C) 2008-2009 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.crypt.pbe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.vt.middleware.crypt.CryptException;
import edu.vt.middleware.crypt.symmetric.SymmetricAlgorithm;

/**
 * Abstract base class for password-based encryption schemes defined in PKCS
 * standards that use salt and iterated hashing as the basis of the key
 * derivation function.
 * <p>
 * NOTE:  Classes derived from this class are not thread safe.  In particular,
 * care should be take to prevent multiple threads from performing encryption
 * and/or decryption concurrently.
 *
 * @author Middleware
 * @version $Revision$
 *
 */
public abstract class AbstractPKCSEncryptionScheme implements EncryptionScheme
{
  /** Key generation strategy. */
  protected KeyGenerator generator;

  /** Cipher used for encryption and decryption. */
  protected SymmetricAlgorithm cipher;


  /** {@inheritDoc} */
  public byte[] encrypt(final char[] password, final byte[] plaintext)
    throws CryptException
  {
    initCipher(generator.generate(password));
    cipher.initEncrypt();
    return cipher.encrypt(plaintext);
  }


  /** {@inheritDoc} */
  public void encrypt(
    final char[] password, final InputStream in, final OutputStream out)
    throws CryptException, IOException
  {
    initCipher(generator.generate(password));
    cipher.initEncrypt();
    cipher.encrypt(in, out);
  }


  /** {@inheritDoc} */
  public byte[] decrypt(final char[] password, final byte[] ciphertext)
    throws CryptException
  {
    initCipher(generator.generate(password));
    cipher.initDecrypt();
    return cipher.decrypt(ciphertext);
  }


  /** {@inheritDoc} */
  public void decrypt(
    final char[] password, final InputStream in, final OutputStream out)
    throws CryptException, IOException
  {
    initCipher(generator.generate(password));
    cipher.initDecrypt();
  }


  /**
   * Initializes the cipher with the given PBE derived key bytes.
   *
   * @param  derivedKey  Derived key bytes.
   */
  protected abstract void initCipher(byte[] derivedKey);
}
