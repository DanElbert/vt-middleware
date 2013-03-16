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
package org.ldaptive.handler;

/**
 * Handler result data.
 *
 * @param  <T>  type of result
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class HandlerResult<T>
{

  /** Result produced by a handler. */
  private final T result;

  /** Whether the operation should be aborted. */
  private final boolean abort;


  /**
   * Creates a new handler result.
   *
   * @param  t  produced by a handler
   */
  public HandlerResult(final T t)
  {
    result = t;
    abort = false;
  }


  /**
   * Creates a new handler result.
   *
   * @param  t  produced by a handler
   * @param  b  whether the operation should be aborted
   */
  public HandlerResult(final T t, final boolean b)
  {
    result = t;
    abort = b;
  }


  /**
   * Returns the result produced by a handler.
   *
   * @return  result
   */
  public T getResult()
  {
    return result;
  }


  /**
   * Returns whether the operation should be aborted.
   *
   * @return  whether the operation should be aborted
   */
  public boolean getAbort()
  {
    return abort;
  }
}
