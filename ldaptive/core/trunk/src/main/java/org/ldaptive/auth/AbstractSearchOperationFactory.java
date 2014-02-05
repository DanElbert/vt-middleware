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
package org.ldaptive.auth;

import org.ldaptive.Connection;
import org.ldaptive.SearchOperation;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.cache.Cache;
import org.ldaptive.handler.OperationExceptionHandler;
import org.ldaptive.handler.OperationResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for authentication related classes that perform searches.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class AbstractSearchOperationFactory
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** Handler to handle search exceptions. */
  private OperationExceptionHandler<SearchRequest, SearchResult>
  searchExceptionHandler;

  /** Handlers to handle search responses. */
  private OperationResponseHandler<SearchRequest, SearchResult>[]
  searchResponseHandlers;

  /** Cache to use when performing searches. */
  private Cache<SearchRequest> searchCache;


  /**
   * Returns the search exception handler.
   *
   * @return  search exception handler
   */
  public OperationExceptionHandler<SearchRequest, SearchResult>
  getSearchExceptionHandler()
  {
    return searchExceptionHandler;
  }


  /**
   * Sets the search exception handler.
   *
   * @param  handler  search exception handler
   */
  public void setSearchExceptionHandler(
    final OperationExceptionHandler<SearchRequest, SearchResult> handler)
  {
    searchExceptionHandler = handler;
  }


  /**
   * Returns the search response handlers.
   *
   * @return  search response handlers
   */
  public OperationResponseHandler<SearchRequest, SearchResult>[]
  getSearchResponseHandlers()
  {
    return searchResponseHandlers;
  }


  /**
   * Sets the search response handlers.
   *
   * @param  handlers  search response handlers
   */
  public void setSearchResponseHandlers(
    final OperationResponseHandler<SearchRequest, SearchResult>... handlers)
  {
    searchResponseHandlers = handlers;
  }


  /**
   * Returns the search cache.
   *
   * @return  cache
   */
  public Cache<SearchRequest> getSearchCache()
  {
    return searchCache;
  }


  /**
   * Sets the search cache.
   *
   * @param  cache  to set
   */
  public void setSearchCache(final Cache<SearchRequest> cache)
  {
    searchCache = cache;
  }


  /**
   * Creates a new search operation configured with the properties on this
   * factory.
   *
   * @param  conn  to pass to the search operation
   *
   * @return  search operation
   */
  protected SearchOperation createSearchOperation(final Connection conn)
  {
    final SearchOperation op = new SearchOperation(conn);
    if (searchExceptionHandler != null) {
      op.setOperationExceptionHandler(searchExceptionHandler);
    }
    if (searchResponseHandlers != null) {
      op.setOperationResponseHandlers(searchResponseHandlers);
    }
    if (searchCache != null) {
      op.setCache(searchCache);
    }
    return op;
  }
}
