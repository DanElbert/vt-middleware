/*
  $Id$

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

/**
 * Contains functions that run before and after all tests.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class TestControl
{

  /** Attribute to block on. */
  public static final LdapAttribute ATTR_IDLE =
    new LdapAttribute("mail", "test-idle@ldaptive.org");

  /** Attribute to block on. */
  public static final LdapAttribute ATTR_RUNNING =
    new LdapAttribute("mail", "test-running@ldaptive.org");

  /** Time to wait before checking if lock is available. */
  public static final int WAIT_TIME = 60000;

  /** Type of directory being tested. */
  private static String DIRECTORY_TYPE;


  /**
   * Used by tests to determine if Active Directory is being tested.
   *
   * @return  whether active directory is being tested
   */
  public static boolean isActiveDirectory()
  {
    return "ACTIVE_DIRECTORY".equals(DIRECTORY_TYPE);
  }


  /**
   * Used by tests to determine if Oracle Directory server is being tested.
   *
   * @return  whether oracle directory server is being tested
   */
  public static boolean isOracleDirectory()
  {
    return "ORACLE".equals(DIRECTORY_TYPE);
  }


  /**
   * Obtains the lock before running all tests.
   *
   * @param  ignoreLock  whether to check for the global test lock
   * @param  bindDn  to lock on
   *
   * @throws Exception on test failure
   */
  @BeforeSuite(alwaysRun = true)
  @Parameters({"ldapTestsIgnoreLock", "ldapBindDn"})
  public void setup(final String ignoreLock, final String bindDn)
    throws Exception
  {
    final Connection conn = TestUtils.createSetupConnection();
    if (!Boolean.valueOf(ignoreLock)) {
      boolean isTestRunning = true;
      // wait for other tests to finish
      int i = 1;
      while (isTestRunning) {
        try {
          conn.open();
          final CompareOperation compare = new CompareOperation(conn);
          isTestRunning = !compare.execute(
            new CompareRequest(bindDn, ATTR_IDLE)).getResult();
        } finally {
          conn.close();
          if (isTestRunning) {
            System.err.println("Waiting for test lock...");
            Thread.sleep(WAIT_TIME * i++);
          }
        }
      }
      try {
        conn.open();
        final ModifyOperation modify = new ModifyOperation(conn);
        modify.execute(
          new ModifyRequest(
            bindDn,
            new AttributeModification(
              AttributeModificationType.REPLACE, ATTR_RUNNING)));
        if (isAD(conn, bindDn)) {
          DIRECTORY_TYPE = "ACTIVE_DIRECTORY";
        } else if (isOracle(conn)) {
          DIRECTORY_TYPE = "ORACLE";
        } else {
          DIRECTORY_TYPE = "LDAP";
        }
      } finally {
        conn.close();
      }
    }
  }


  /**
   * Performs an object level search for the sAMAccountName attribute used by
   * Active Directory.
   *
   * @param  conn  to perform compare with
   * @param  bindDn  to perform search on
   *
   * @return  whether the supplied entry is in active directory
   *
   * @throws  Exception  On failure.
   */
  protected boolean isAD(final Connection conn, final String bindDn)
    throws Exception
  {
    final SearchOperation search = new SearchOperation(conn);
    final SearchRequest request = SearchRequest.newObjectScopeSearchRequest(
      bindDn,
      ReturnAttributes.NONE.value(),
      new SearchFilter("(sAMAccountName=*)"));
    try {
      return search.execute(request).getResult().size() == 1;
    } catch (LdapException e) {
      if (ResultCode.NO_SUCH_OBJECT == e.getResultCode() ||
          ResultCode.NO_SUCH_ATTRIBUTE == e.getResultCode()) {
        return false;
      }
      throw e;
    }
  }


  /**
   * Performs an object level search on the root DSE for the vendorName
   * attribute used by Oracle DS.
   *
   * @param  conn  to perform compare with
   *
   * @return  whether the supplied entry contains a vendorName attribute
   * identified by Oracle
   *
   * @throws  Exception  On failure.
   */
  protected boolean isOracle(final Connection conn)
    throws Exception
  {
    final SearchOperation search = new SearchOperation(conn);
    final SearchRequest request = SearchRequest.newObjectScopeSearchRequest(
      "",
      new String[] {"vendorName"});
    try {
      final LdapEntry rootDSE = search.execute(request).getResult().getEntry();
      if (rootDSE.getAttribute("vendorName") != null) {
        return rootDSE.getAttribute(
          "vendorName").getStringValue().contains("Oracle");
      }
      return false;
    } catch (LdapException e) {
      if (ResultCode.NO_SUCH_OBJECT == e.getResultCode() ||
        ResultCode.NO_SUCH_ATTRIBUTE == e.getResultCode()) {
        return false;
      }
      throw e;
    }
  }


  /**
   * Releases the lock after running all tests.
   *
   * @param  bindDn  to lock on
   *
   * @throws Exception on test failure
   */
  @AfterSuite(alwaysRun = true)
  @Parameters("ldapBindDn")
  public void teardown(final String bindDn)
    throws Exception
  {
    final Connection conn = TestUtils.createSetupConnection();
    try {
      conn.open();
      // set attribute when tests are finished
      final ModifyOperation modify = new ModifyOperation(conn);
      modify.execute(
        new ModifyRequest(
          bindDn,
          new AttributeModification[] {
            new AttributeModification(
              AttributeModificationType.REPLACE, ATTR_IDLE), }));
    } finally {
      conn.close();
    }
  }
}
