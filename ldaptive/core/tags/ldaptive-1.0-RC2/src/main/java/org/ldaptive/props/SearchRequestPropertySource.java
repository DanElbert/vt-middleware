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
package org.ldaptive.props;

import java.io.Reader;
import java.util.Properties;
import java.util.Set;
import org.ldaptive.SearchRequest;

/**
 * Reads properties specific to {@link SearchRequest} and returns an initialized
 * object of that type.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public final class SearchRequestPropertySource
  extends AbstractPropertySource<SearchRequest>
{

  /** Invoker for search request. */
  private static final SearchRequestPropertyInvoker INVOKER =
    new SearchRequestPropertyInvoker(SearchRequest.class);


  /**
   * Creates a new search request property source using the default properties
   * file.
   *
   * @param  request  search request to invoke properties on
   */
  public SearchRequestPropertySource(final SearchRequest request)
  {
    this(request, PROPERTIES_FILE);
  }


  /**
   * Creates a new search request property source.
   *
   * @param  request  search request to invoke properties on
   * @param  paths  to read properties from
   */
  public SearchRequestPropertySource(
    final SearchRequest request,
    final String... paths)
  {
    this(request, loadProperties(paths));
  }


  /**
   * Creates a new search request property source.
   *
   * @param  request  search request to invoke properties on
   * @param  readers  to read properties from
   */
  public SearchRequestPropertySource(
    final SearchRequest request,
    final Reader... readers)
  {
    this(request, loadProperties(readers));
  }


  /**
   * Creates a new search request property source.
   *
   * @param  request  search request to invoke properties on
   * @param  props  to read properties from
   */
  public SearchRequestPropertySource(
    final SearchRequest request,
    final Properties props)
  {
    this(request, PropertyDomain.LDAP, props);
  }


  /**
   * Creates a new search request property source.
   *
   * @param  request  search request to invoke properties on
   * @param  domain  that properties are in
   * @param  props  to read properties from
   */
  public SearchRequestPropertySource(
    final SearchRequest request,
    final PropertyDomain domain,
    final Properties props)
  {
    super(request, domain, props);
  }


  /** {@inheritDoc} */
  @Override
  public void initialize()
  {
    initializeObject(INVOKER);
  }


  /**
   * Returns the property names for this property source.
   *
   * @return  all property names
   */
  public static Set<String> getProperties()
  {
    return INVOKER.getProperties();
  }
}
