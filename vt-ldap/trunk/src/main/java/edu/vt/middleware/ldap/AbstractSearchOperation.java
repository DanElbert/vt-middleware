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
package edu.vt.middleware.ldap;

import edu.vt.middleware.ldap.cache.Cache;
import edu.vt.middleware.ldap.handler.ExtendedLdapAttributeHandler;
import edu.vt.middleware.ldap.handler.ExtendedLdapResultHandler;
import edu.vt.middleware.ldap.handler.LdapAttributeHandler;
import edu.vt.middleware.ldap.handler.LdapResultHandler;
import edu.vt.middleware.ldap.handler.SearchCriteria;

/**
 * Provides common implementation for search operations.
 *
 * @param  <Q>  type of search request
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public abstract class AbstractSearchOperation<Q extends SearchRequest>
  extends AbstractOperation<Q, LdapResult>
{

  /** Cache to use when performing searches. */
  private Cache<Q> cache;


  /**
   * Creates a new abstract search connection.
   *
   * @param  conn  to use for this operation
   * @param  c  cache
   */
  public AbstractSearchOperation(final Connection conn, final Cache<Q> c)
  {
    super(conn);
    cache = c;
  }


  /**
   * Returns the cache to check when performing search operations. When a cache
   * is provided it will be populated as new searches are performed and used
   * when a search request hits the cache.
   *
   * @return  cache
   */
  public Cache<Q> getCache()
  {
    return cache;
  }


  /**
   * Sets the cache.
   *
   * @param  c  cache to set
   */
  public void setCache(final Cache<Q> c)
  {
    cache = c;
  }


  /** {@inheritDoc} */
  @Override
  protected void initializeRequest(
    final Q request, final ConnectionConfig cc)
  {
    request.setLdapResultHandlers(
      initializeLdapResultHandlers(request, getConnection()));
  }


  /**
   * Initializes those ldap result handlers that require access to the ldap
   * connection.
   *
   * @param  request  to read result handlers from
   * @param  c  to provide to result handlers
   * @return  initialized result handlers
   */
  protected LdapResultHandler[] initializeLdapResultHandlers(
    final Q request, final Connection c)
  {
    final LdapResultHandler[] handler = request.getLdapResultHandlers();
    if (handler != null && handler.length > 0) {
      for (LdapResultHandler h : handler) {
        if (ExtendedLdapResultHandler.class.isInstance(h)) {
          ((ExtendedLdapResultHandler) h).setResultConnection(c);
        }

        final LdapAttributeHandler[] attrHandler = h.getAttributeHandlers();
        if (attrHandler != null && attrHandler.length > 0) {
          for (LdapAttributeHandler ah : attrHandler) {
            if (ExtendedLdapAttributeHandler.class.isInstance(ah)) {
              ((ExtendedLdapAttributeHandler) ah).setResultConnection(c);
            }
          }
        }
      }
    }
    return handler;
  }


  /**
   * Performs the ldap search.
   *
   * @param  request  to invoke search with
   * @return  ldap result
   * @throws LdapException if an error occurs
   */
  protected abstract Response<LdapResult> executeSearch(final Q request)
    throws LdapException;


  /** {@inheritDoc} */
  @Override
  protected Response<LdapResult> invoke(final Q request)
    throws LdapException
  {
    Response<LdapResult> response = null;
    if (cache != null) {
      final LdapResult lr = cache.get(request);
      if (lr == null) {
        response = executeSearch(request);
        cache.put(request, response.getResult());
      } else {
        response = new Response<LdapResult>(lr, null);
      }
    } else {
      response = executeSearch(request);
    }
    return response;
  }


  /**
   * Processes each ldap result handler after a search has been performed.
   *
   * @param  request  the search was performed with
   * @param  result  ldap result of the search
   * @throws LdapException if an error occurs processing a handler
   */
  protected void executeLdapResultHandlers(
    final SearchRequest request, final LdapResult result)
    throws LdapException
  {
    final LdapResultHandler[] handler = request.getLdapResultHandlers();
    if (handler != null && handler.length > 0) {
      final SearchCriteria sc = new SearchCriteria(request);
      for (int i = 0; i < handler.length; i++) {
        if (handler[i] != null) {
          handler[i].process(sc, result);
        }
      }
    }
  }
}
