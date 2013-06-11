/*
  $Id$

  Copyright (C) 2003-2013 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.control.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.ldaptive.Connection;
import org.ldaptive.LdapException;
import org.ldaptive.Request;
import org.ldaptive.Response;
import org.ldaptive.SearchEntry;
import org.ldaptive.SearchRequest;
import org.ldaptive.SearchResult;
import org.ldaptive.async.AsyncRequest;
import org.ldaptive.async.AsyncSearchOperation;
import org.ldaptive.async.handler.AsyncRequestHandler;
import org.ldaptive.async.handler.ExceptionHandler;
import org.ldaptive.control.SyncRequestControl;
import org.ldaptive.extended.CancelOperation;
import org.ldaptive.extended.CancelRequest;
import org.ldaptive.handler.HandlerResult;
import org.ldaptive.handler.IntermediateResponseHandler;
import org.ldaptive.handler.OperationResponseHandler;
import org.ldaptive.handler.SearchEntryHandler;
import org.ldaptive.intermediate.IntermediateResponse;
import org.ldaptive.intermediate.SyncInfoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client that simplifies using the sync repl control.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class SyncReplClient
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** Connection to invoke the search operation on. */
  private final Connection connection;

  /** Controls which mode the sync repl control should use. */
  private final boolean refreshAndPersist;

  /** Cookie manager used by the deprecated constructor. */
  private final CookieManager cookieManager;


  /**
   * Creates a new sync repl client.
   *
   * @param  conn  to execute the async search operation on
   * @param  persist  whether to refresh and persist or just refresh
   */
  public SyncReplClient(final Connection conn, final boolean persist)
  {
    connection = conn;
    refreshAndPersist = persist;
    cookieManager = null;
  }


  /**
   * Creates a new sync repl client.
   *
   * @param  conn  to execute the async search operation on
   * @param  persist  whether to refresh and persist or just refresh
   * @param  cookie  optional sync repl cookie
   *
   * @deprecated  use {@link #SyncReplClient(Connection, boolean)} instead
   */
  @Deprecated
  public SyncReplClient(
    final Connection conn,
    final boolean persist,
    final byte[] cookie)
  {
    connection = conn;
    refreshAndPersist = persist;
    cookieManager = new DefaultCookieManager(cookie);
  }


  /**
   * Invokes {@link #execute(SearchRequest, CookieManager)} with a {@link
   * DefaultCookieManager}.
   *
   * @param  request  search request to execute
   *
   * @return  blocking queue to wait for sync repl items
   *
   * @throws  LdapException  if the search fails
   */
  @SuppressWarnings("unchecked")
  public BlockingQueue<SyncReplItem> execute(final SearchRequest request)
    throws LdapException
  {
    final CookieManager manager =
      cookieManager != null ? cookieManager : new DefaultCookieManager();
    return execute(request, manager);
  }


  /**
   * Performs an async search operation with the {@link SyncRequestControl}. The
   * supplied request is modified in the following way:
   *
   * <ul>
   *   <li>{@link SearchRequest#setControls(
   *     org.ldaptive.control.RequestControl...)} is invoked with {@link
   *     SyncRequestControl}</li>
   *   <li>{@link SearchRequest#setSearchEntryHandlers(SearchEntryHandler...)}
   *     is invoked with a custom handler that places sync repl data in a
   *     blocking queue.</li>
   *   <li>{@link SearchRequest#setIntermediateResponseHandlers(
   *     IntermediateResponseHandler...)} is invoked with a custom handler that
   *     places sync repl data in a blocking queue.</li>
   *   <li>{@link AsyncSearchOperation#setOperationResponseHandlers(
   *     OperationResponseHandler[])} is invoked with a custom handler that
   *     places the sync repl response in a blocking queue.</li>
   *   <li>{@link AsyncSearchOperation#setExceptionHandler(ExceptionHandler)} is
   *     invoked with a custom handler that places the exception in a blocking
   *     queue.</li>
   * </ul>
   *
   * <p>The search request object should not be reused for any other search
   * operations.</p>
   *
   * @param  request  search request to execute
   * @param  manager  for reading and writing cookies
   *
   * @return  blocking queue to wait for sync repl items
   *
   * @throws  LdapException  if the search fails
   */
  @SuppressWarnings("unchecked")
  public BlockingQueue<SyncReplItem> execute(
    final SearchRequest request,
    final CookieManager manager)
    throws LdapException
  {
    final BlockingQueue<SyncReplItem> queue =
      new LinkedBlockingQueue<SyncReplItem>();

    final AsyncSearchOperation search = new AsyncSearchOperation(connection);
    search.setOperationResponseHandlers(
      new OperationResponseHandler<SearchRequest, SearchResult>() {
        @Override
        public HandlerResult<Response<SearchResult>> handle(
          final Connection conn,
          final SearchRequest request,
          final Response<SearchResult> response)
          throws LdapException
        {
          try {
            logger.debug("received {}", response);
            search.shutdown();
            final SyncReplItem item = new SyncReplItem(
              new SyncReplItem.Response(response));
            if (item.getResponse().getSyncDoneControl() != null) {
              final byte[] cookie =
                item.getResponse().getSyncDoneControl().getCookie();
              if (cookie != null) {
                manager.writeCookie(cookie);
              }
            }
            queue.put(item);
          } catch (Exception e) {
            logger.warn("Unable to enqueue response {}", response);
          }
          return new HandlerResult<Response<SearchResult>>(response);
        }
      });
    search.setAsyncRequestHandlers(
      new AsyncRequestHandler() {
        @Override
        public HandlerResult<AsyncRequest> handle(
          final Connection conn,
          final Request request,
          final AsyncRequest asyncRequest)
          throws LdapException
        {
          try {
            logger.debug("received {}", asyncRequest);
            queue.put(new SyncReplItem(asyncRequest));
          } catch (Exception e) {
            logger.warn("Unable to enqueue async request {}", asyncRequest);
          }
          return new HandlerResult<AsyncRequest>(null);
        }
      });
    search.setExceptionHandler(
      new ExceptionHandler() {
        @Override
        public HandlerResult<Exception> handle(
          final Connection conn,
          final Request request,
          final Exception exception)
        {
          try {
            logger.debug("received exception:", exception);
            search.shutdown();
            queue.put(new SyncReplItem(exception));
          } catch (Exception e) {
            logger.warn("Unable to enqueue exception:", exception);
          }
          return new HandlerResult<Exception>(null);
        }
      });

    request.setControls(
      new SyncRequestControl(
        refreshAndPersist ? SyncRequestControl.Mode.REFRESH_AND_PERSIST
                          : SyncRequestControl.Mode.REFRESH_ONLY,
        manager.readCookie(),
        true));
    request.setSearchEntryHandlers(
      new SearchEntryHandler() {
        @Override
        public HandlerResult<SearchEntry> handle(
          final Connection conn,
          final SearchRequest request,
          final SearchEntry entry)
          throws LdapException
        {
          try {
            logger.debug("received {}", entry);
            final SyncReplItem item = new SyncReplItem(
              new SyncReplItem.Entry(entry));
            if (item.getEntry().getSyncStateControl() != null) {
              final byte[] cookie =
                item.getEntry().getSyncStateControl().getCookie();
              if (cookie != null) {
                manager.writeCookie(cookie);
              }
            }
            queue.put(item);
          } catch (Exception e) {
            logger.warn("Unable to enqueue entry {}", entry);
          }
          return new HandlerResult<SearchEntry>(null);
        }

        @Override
        public void initializeRequest(final SearchRequest request) {}
      });
    request.setIntermediateResponseHandlers(
      new IntermediateResponseHandler() {
        @Override
        public HandlerResult<IntermediateResponse> handle(
          final Connection conn,
          final Request request,
          final IntermediateResponse response)
          throws LdapException
        {
          if (SyncInfoMessage.OID.equals(response.getOID())) {
            try {
              logger.debug("received {}", response);
              final SyncInfoMessage message = (SyncInfoMessage) response;
              if (message.getCookie() != null) {
                manager.writeCookie(message.getCookie());
              }
              queue.put(new SyncReplItem(message));
            } catch (Exception e) {
              logger.warn(
                "Unable to enqueue intermediate response {}",
                response);
            }
          }
          return new HandlerResult<IntermediateResponse>(null);
        }
      });

    search.execute(request);
    return queue;
  }


  /**
   * Invokes a cancel operation on the supplied ldap message id. Convenience
   * method supplied to cancel sync repl operations.
   *
   * @param  messageId  of the operation to cancel
   *
   * @return  cancel operation response
   *
   * @throws  LdapException  if the cancel operation fails
   */
  public Response<Void> cancel(final int messageId)
    throws LdapException
  {
    final CancelOperation cancel = new CancelOperation(connection);
    return cancel.execute(new CancelRequest(messageId));
  }
}
