/*
  $Id$

  Copyright (C) 2007-2011 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package edu.vt.middleware.crypt.symmetric;

import java.io.File;

import edu.vt.middleware.crypt.CliHelper;
import edu.vt.middleware.crypt.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link SymmetricCli} class.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class SymmetricCliTest
{

  /** Test IV. */
  private static final String TEST_IV = "3858f62230ac3c915f300c664312c63f";

  /** Classpath location of plaintext data file. */
  private static final String TEST_PLAINTEXT =
    "src/test/resources/edu/vt/middleware/crypt/plaintext-127.txt";

  /** Key length in bits suitable for all tested ciphers. */
  private static final int KEY_LENGTH = 128;

  /** Logger instance. */
  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "testdata")
  public Object[][] createTestData()
    throws Exception
  {
    return
      new Object[][] {
        {
          "-cipher aes -iv " + TEST_IV,
          "aes",
        },
        {
          "-cipher AES -encoding base64 -iv " + TEST_IV,
          "aes-b64",
        },
        {
          "-cipher AES -encoding hex -iv " + TEST_IV,
          "aes-hex",
        },
        {
          "-cipher CAST6 -encoding hex -iv " + TEST_IV,
          "cast6-hex",
        },
        {
          "-cipher Twofish -iv " + TEST_IV,
          "twofish",
        },
      };
  }


  /**
   * @return  Test data.
   *
   * @throws  Exception  On test data generation failure.
   */
  @DataProvider(name = "testdatapbe")
  public Object[][] createPbeTestData()
    throws Exception
  {
    return
      new Object[][] {
        {
          "-cipher DESede -keysize 192 -iter 1024 ",
          "des3-pkcs5s2",
        },
        {
          "-cipher RC2 -keysize 64 -scheme pkcs5s1 -digest SHA1 -iter 2048",
          "rc2-pkcs5s1",
        },
        {
          "-cipher AES -scheme openssl -keysize 128 -iter 1000",
          "aes-128-openssl",
        },
        {
          "-cipher AES -scheme pkcs12 -digest SHA1 -keysize 256 -iter 512 " +
            "-iv " + TEST_IV,
          "aes-256-pkcs12",
        },
      };
  }


  /**
   * @param  cliFragment  CLI argument fragment in a single string.
   * @param  nameId  Name identifier to be tacked on to generated file names for
   * uniqueness and to facilitate post-mortem on failed tests.
   *
   * @throws  Exception  On test failure.
   */
  @Test(
    groups = {"cli", "symmetric"},
    dataProvider = "testdata"
  )
  public void testSymmetricCli(final String cliFragment, final String nameId)
    throws Exception
  {
    final File refFile = new File(TEST_PLAINTEXT);
    final File outDir = new File("target/test-output");
    outDir.mkdir();

    final File keyFile = new File(outDir + "/symmetric-" + nameId + ".key");
    final File cipherFile = new File(
      outDir + "/symmetric-cli-cipher-" + nameId + ".out");
    final File plainFile = new File(
      outDir + "/symmetric-cli-plain-" + nameId + ".txt");

    // Generate key
    String commandLine = cliFragment + " -genkey " + KEY_LENGTH + " -out " +
      keyFile;
    logger.info(
      "Testing symmetric key generation with command line:\n\t{}", commandLine);
    SymmetricCli.main(CliHelper.splitArgs(commandLine));
    AssertJUnit.assertTrue(keyFile.length() > 0L);

    // Encrypt plaintext
    commandLine = cliFragment + " -encrypt -in " + refFile + " -out " +
      cipherFile + " -key " + keyFile;
    logger.info(
      "Testing symmetric encryption with command line:\n\t{}", commandLine);
    SymmetricCli.main(CliHelper.splitArgs(commandLine));
    AssertJUnit.assertTrue(cipherFile.length() > 0L);

    // Decrypt ciphertext
    commandLine = cliFragment + " -decrypt -in " + cipherFile + " -out " +
      plainFile + " -key " + keyFile;
    logger.info(
      "Testing symmetric decryption with command line:\n\t{}", commandLine);
    SymmetricCli.main(CliHelper.splitArgs(commandLine));
    AssertJUnit.assertTrue(plainFile.length() > 0L);
    AssertJUnit.assertTrue(FileHelper.equal(refFile, plainFile));
  }


  /**
   * @param  cliFragment  CLI argument fragment in a single string.
   * @param  nameId  Name identifier to be tacked on to generated file names for
   * uniqueness and to facilitate post-mortem on failed tests.
   *
   * @throws  Exception  On test failure.
   */
  @Test(
    groups = {"cli", "symmetric"},
    dataProvider = "testdatapbe"
  )
  public void testSymmetricCliPbe(final String cliFragment, final String nameId)
    throws Exception
  {
    final File refFile = new File(TEST_PLAINTEXT);
    final File outDir = new File("target/test-output");
    outDir.mkdir();

    final File cipherFile = new File(
      outDir + "/pbe-cli-cipher-" + nameId + ".out");
    final File plainFile = new File(
      outDir + "/pbe-cli-plain-" + nameId + ".txt");

    // Encrypt plaintext
    String commandLine = cliFragment + " -encrypt -in " + refFile + " -out " +
      cipherFile + " -pbe S33Kr1t -salt DEADF00D00000000";
    logger.info("Testing PBE encryption with command line:\n\t{}", commandLine);
    SymmetricCli.main(CliHelper.splitArgs(commandLine));
    AssertJUnit.assertTrue(cipherFile.length() > 0L);

    // Decrypt ciphertext
    commandLine = cliFragment + " -decrypt -in " + cipherFile + " -out " +
      plainFile + " -pbe S33Kr1t -salt DEADF00D00000000";
    logger.info("Testing PBE decryption with command line:\n\t{}", commandLine);
    SymmetricCli.main(CliHelper.splitArgs(commandLine));
    AssertJUnit.assertTrue(plainFile.length() > 0L);
    AssertJUnit.assertTrue(FileHelper.equal(refFile, plainFile));
  }
}
