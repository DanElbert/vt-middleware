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
package org.ldaptive.ssl;

import java.net.InetAddress;
import org.ldaptive.Connection;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.DefaultConnectionFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for default hostname verification behavior.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
@Test(dependsOnGroups = { "ssl" })
public class HostnameVerifierConnectionTest
{


  /**
   * @throws  Exception  On test failure.
   */
  @BeforeClass(groups = {"ssl-hostname"}, dependsOnGroups = { "ssl" })
  public void setProperties()
  {
    System.setProperty(
      "javax.net.ssl.trustStore",
      "target/test-classes/ldaptive.truststore");
    System.setProperty("javax.net.ssl.trustStoreType", "BKS");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"ssl-hostname"}, dependsOnGroups = { "ssl" })
  public void clearProperties()
  {
    System.clearProperty("javax.net.ssl.trustStore");
    System.clearProperty("javax.net.ssl.trustStoreType");
    System.clearProperty("javax.net.ssl.trustStorePassword");
  }


  /**
   * @param  host  to connect to
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "ldapTestHost" })
  @Test(groups = {"ssl-hostname"})
  public void connectStartTLS(final String host)
    throws Exception
  {
    // attempting to use startTLS by IP address should fail
    ConnectionConfig cc = new ConnectionConfig(convertUrlToIp(host));
    cc.setUseStartTLS(true);
    Connection conn = DefaultConnectionFactory.getConnection(cc);
    try {
      conn.open();
      AssertJUnit.fail("Should have thrown Exception, no exception thrown");
    } catch (Exception e) {
      AssertJUnit.assertNotNull(e);
    } finally {
      conn.close();
    }

    // attempting to use startTLS by hostname address should succeed
    cc = new ConnectionConfig(host);
    cc.setUseStartTLS(true);
    conn = DefaultConnectionFactory.getConnection(cc);
    try {
      conn.open();
    } finally {
      conn.close();
    }
  }


  /**
   * @param  host  to connect to
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "ldapSslTestHost" })
  @Test(groups = {"ssl-hostname"})
  public void connectSSL(final String host)
    throws Exception
  {
    // attempting to use LDAPS by IP address should fail
    ConnectionConfig cc = new ConnectionConfig(convertUrlToIp(host));
    cc.setUseSSL(true);
    Connection conn = DefaultConnectionFactory.getConnection(cc);
    try {
      conn.open();
      AssertJUnit.fail("Should have thrown Exception, no exception thrown");
    } catch (Exception e) {
      AssertJUnit.assertNotNull(e);
    } finally {
      conn.close();
    }

    // attempting to use LDAPS by hostname address should succeed
    cc = new ConnectionConfig(host);
    cc.setUseSSL(true);
    conn = DefaultConnectionFactory.getConnection(cc);
    try {
      conn.open();
    } finally {
      conn.close();
    }
  }


  /**
   * Converts a DNS based url string to an IP based string.
   *
   * @param  host  url to convert
   *
   * @return  url with IP address
   *
   * @throws Exception  if the ip address cannot be determined
   */
  private String convertUrlToIp(final String host)
    throws Exception
  {
    // convert the hostname into an ip address
    String ipHost = null;
    if (host.indexOf(":") < host.lastIndexOf(":")) {
      ipHost = host.substring("ldap://".length(), host.lastIndexOf(":"));
    } else {
      ipHost = host.substring("ldap://".length());
    }
    final InetAddress ip = InetAddress.getByName(ipHost);
    ipHost = "ldap://" + ip.getHostAddress();
    if (host.indexOf(":") < host.lastIndexOf(":")) {
      ipHost = ipHost + ":" + host.substring(host.lastIndexOf(":") + 1);
    }
    return ipHost;
  }
}
