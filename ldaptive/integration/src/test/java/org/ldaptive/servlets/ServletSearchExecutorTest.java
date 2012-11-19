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
package org.ldaptive.servlets;

import java.io.File;
import java.io.StringWriter;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.ldaptive.AbstractTest;
import org.ldaptive.LdapEntry;
import org.ldaptive.SearchResult;
import org.ldaptive.TestUtils;
import org.ldaptive.io.Dsmlv1Writer;
import org.ldaptive.io.JsonWriter;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Unit test for {@link SearchServlet}.
 *
 * @author  Middleware Services
 * @version  $Revision$
 */
public class ServletSearchExecutorTest extends AbstractTest
{

  /** Entry created for tests. */
  private static LdapEntry testLdapEntry;

  /** To test servlets with. */
  private ServletRunner servletRunner;


  /**
   * @param  ldifFile  to create.
   * @param  webXml  web.xml for queries
   *
   * @throws  Exception  On test failure.
   */
  @Parameters({ "createEntry11", "webXml" })
  @BeforeClass(groups = {"servlet"})
  public void createLdapEntry(final String ldifFile, final String webXml)
    throws Exception
  {
    final String ldif = TestUtils.readFileIntoString(ldifFile);
    testLdapEntry = TestUtils.convertLdifToResult(ldif).getEntry();
    super.createLdapEntry(testLdapEntry);

    servletRunner = new ServletRunner(new File(webXml));
  }


  /** @throws  Exception  On test failure. */
  @AfterClass(groups = {"servlet"})
  public void deleteLdapEntry()
    throws Exception
  {
    super.deleteLdapEntry(testLdapEntry.getDn());
    servletRunner.shutDown();
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "ldifSearchServletQuery",
      "ldifSearchServletAttrs",
      "ldifSearchServletLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void ldifSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String expected = TestUtils.readFileIntoString(ldifFile);

    final ServletUnitClient sc = servletRunner.newClient();
    final WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/LdifSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    final WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/plain", response.getContentType());

    final SearchResult result =
      TestUtils.convertLdifToResult(response.getText());
    AssertJUnit.assertEquals(TestUtils.convertLdifToResult(expected), result);
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "dsmlSearchServletQuery",
      "dsmlSearchServletAttrs",
      "dsmlSearchServletLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void dsmlSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String ldif = TestUtils.readFileIntoString(ldifFile);
    final SearchResult result = TestUtils.convertLdifToResult(ldif);
    // convert ldif into dsmlv1
    final StringWriter s1w = new StringWriter();
    final Dsmlv1Writer d1w = new Dsmlv1Writer(s1w);
    d1w.write(result);
    final String dsmlv1 = s1w.toString();

    final ServletUnitClient sc = servletRunner.newClient();
    // test basic dsml query
    WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/DsmlSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/xml", response.getContentType());
    // active directory uppercases CN in the DN
    AssertJUnit.assertEquals(
      dsmlv1, response.getText().replaceAll("CN=", "cn="));
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "ldifTemplateSearchQuery",
      "ldifTemplateSearchAttrs",
      "ldifTemplateSearchLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void templatesLdifSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String expected = TestUtils.readFileIntoString(ldifFile);

    final ServletUnitClient sc = servletRunner.newClient();
    final WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/TemplatesLdifSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    final WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/plain", response.getContentType());

    final SearchResult result =
      TestUtils.convertLdifToResult(response.getText());
    AssertJUnit.assertEquals(TestUtils.convertLdifToResult(expected), result);
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "dsmlTemplateSearchQuery",
      "dsmlTemplateSearchAttrs",
      "dsmlTemplateSearchLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void templatesDsmlSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String ldif = TestUtils.readFileIntoString(ldifFile);
    final SearchResult result = TestUtils.convertLdifToResult(ldif);
    // convert ldif into dsmlv1
    final StringWriter s1w = new StringWriter();
    final Dsmlv1Writer d1w = new Dsmlv1Writer(s1w);
    d1w.write(result);
    final String dsmlv1 = s1w.toString();

    final ServletUnitClient sc = servletRunner.newClient();
    final WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/TemplatesDsmlSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    final WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/xml", response.getContentType());
    // active directory uppercases CN in the DN
    AssertJUnit.assertEquals(
      dsmlv1, response.getText().replaceAll("CN=", "cn="));
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "jsonTemplateSearchQuery",
      "jsonTemplateSearchAttrs",
      "jsonTemplateSearchLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void templatesJsonSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String ldif = TestUtils.readFileIntoString(ldifFile);
    final SearchResult result = TestUtils.convertLdifToResult(ldif);
    // convert ldif into json
    final StringWriter s1w = new StringWriter();
    final JsonWriter j1w = new JsonWriter(s1w);
    j1w.write(result);
    final String json = s1w.toString();

    final ServletUnitClient sc = servletRunner.newClient();
    final WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/TemplatesJsonSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    final WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("application/json", response.getContentType());
    // active directory uppercases CN in the DN
    AssertJUnit.assertEquals(json, response.getText().replaceAll("CN=", "cn="));
  }


  /**
   * @param  query  to search for.
   * @param  attrs  attributes to return from search
   * @param  ldifFile  to compare with
   *
   * @throws  Exception  On test failure.
   */
  @Parameters(
    {
      "ldifTemplateSearchQuery",
      "ldifTemplateSearchAttrs",
      "ldifTemplateSearchLdif"
    }
  )
  @Test(groups = {"servlet"})
  public void templatesLdifIgnoreSearchServlet(
    final String query,
    final String attrs,
    final String ldifFile)
    throws Exception
  {
    final String expected = TestUtils.readFileIntoString(ldifFile);

    final ServletUnitClient sc = servletRunner.newClient();
    WebRequest request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/TemplatesLdifIgnoreSearch");
    request.setParameter("query", query);
    request.setParameter("attrs", attrs.split("\\|"));

    WebResponse response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/plain", response.getContentType());
    final SearchResult result =
      TestUtils.convertLdifToResult(response.getText());
    AssertJUnit.assertEquals(TestUtils.convertLdifToResult(expected), result);

    request = new PostMethodWebRequest(
      "http://servlets.ldaptive.org/TemplatesLdifIgnoreSearch");
    request.setParameter("query", "df");

    response = sc.getResponse(request);

    AssertJUnit.assertNotNull(response);
    AssertJUnit.assertEquals("text/plain", response.getContentType());
    AssertJUnit.assertEquals("", response.getText());
  }
}
