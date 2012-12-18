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
package org.ldaptive.concurrent;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapException;
import org.ldaptive.Response;
import org.ldaptive.SearchFilter;
import org.ldaptive.SearchResult;
import org.ldaptive.handler.SearchEntryHandler;

/**
 * Base class for aggregate search executors.
 *
 * @param  <T>  type of connection factory
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class
AbstractAggregateSearchExecutor<T extends ConnectionFactory>
  extends AbstractSearchExecutor
{

  /**
   * Creates a new abstract aggregate search executor.
   *
   * @param  es  executor service
   */
  public AbstractAggregateSearchExecutor(final ExecutorService es)
  {
    super(es);
  }


  /**
   * Performs a search operation with the supplied connection factories.
   *
   * @param  factories  to get a connection from
   * @param  filters  to search with
   *
   * @return  search results
   *
   * @throws  LdapException  if the search fails
   */
  public Collection<Response<SearchResult>> search(
    final T[] factories,
    final String... filters)
    throws LdapException
  {
    final SearchFilter[] sf = new SearchFilter[filters.length];
    for (int i = 0; i < filters.length; i++) {
      sf[i] = new SearchFilter(filters[i]);
    }
    return search(factories, sf, (String[]) null, (SearchEntryHandler[]) null);
  }


  /**
   * Performs a search operation with the supplied connection factories.
   *
   * @param  factories  to get a connection from
   * @param  filters  to search with
   *
   * @return  search results
   *
   * @throws  LdapException  if the search fails
   */
  public Collection<Response<SearchResult>> search(
    final T[] factories,
    final SearchFilter[] filters)
    throws LdapException
  {
    return
      search(factories, filters, (String[]) null, (SearchEntryHandler[]) null);
  }


  /**
   * Performs a search operation with the supplied connection factories.
   *
   * @param  factories  to get a connection from
   * @param  filters  to search with
   * @param  attrs  to return
   *
   * @return  search results
   *
   * @throws  LdapException  if the search fails
   */
  public Collection<Response<SearchResult>> search(
    final T[] factories,
    final String[] filters,
    final String... attrs)
    throws LdapException
  {
    final SearchFilter[] sf = new SearchFilter[filters.length];
    for (int i = 0; i < filters.length; i++) {
      sf[i] = new SearchFilter(filters[i]);
    }
    return search(factories, sf, attrs, (SearchEntryHandler[]) null);
  }


  /**
   * Performs a search operation with the supplied connection factories.
   *
   * @param  factories  to get a connection from
   * @param  filters  to search with
   * @param  attrs  to return
   *
   * @return  search results
   *
   * @throws  LdapException  if the search fails
   */
  public Collection<Response<SearchResult>> search(
    final T[] factories,
    final SearchFilter[] filters,
    final String... attrs)
    throws LdapException
  {
    return search(factories, filters, attrs, (SearchEntryHandler[]) null);
  }


  /**
   * Performs a search operation with the supplied connection factories.
   *
   * @param  factories  to get a connection from
   * @param  filters  to search with
   * @param  attrs  to return
   * @param  handlers  entry handlers
   *
   * @return  search results
   *
   * @throws  LdapException  if the search fails
   */
  public abstract Collection<Response<SearchResult>> search(
    final T[] factories,
    final SearchFilter[] filters,
    final String[] attrs,
    final SearchEntryHandler... handlers)
    throws LdapException;
}
