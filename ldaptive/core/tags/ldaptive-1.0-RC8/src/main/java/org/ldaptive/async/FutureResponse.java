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
package org.ldaptive.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.ldaptive.Response;
import org.ldaptive.ResultCode;
import org.ldaptive.control.ResponseControl;

/**
 * Response that blocks on each property until it is available.
 *
 * @param  <T>  type of ldap result contained in this response
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class FutureResponse<T> extends Response<T>
{

  /** Future that will contain the response. */
  private final Future<Response<T>> future;


  /**
   * Creates a new future ldap response.
   *
   * @param  f  future response
   */
  public FutureResponse(final Future<Response<T>> f)
  {
    super(null, null);
    future = f;
  }


  /**
   * Invokes {@link Future#get()}, waiting until the response is returned.
   *
   * @return  response from the future
   *
   * @throws  IllegalStateException  if the future is interrupted or throws an
   * execution exception
   */
  protected Response<T> getResponse()
  {
    try {
      return future.get();
    } catch (InterruptedException e) {
      throw new IllegalStateException("Future interrupted", e);
    } catch (ExecutionException e) {
      throw new IllegalStateException("Future execution error", e);
    }
  }


  /** {@inheritDoc} */
  @Override
  public T getResult()
  {
    return getResponse().getResult();
  }


  /** {@inheritDoc} */
  @Override
  public ResultCode getResultCode()
  {
    return getResponse().getResultCode();
  }


  /** {@inheritDoc} */
  @Override
  public String getMessage()
  {
    return getResponse().getMessage();
  }


  /** {@inheritDoc} */
  @Override
  public String getMatchedDn()
  {
    return getResponse().getMatchedDn();
  }


  /** {@inheritDoc} */
  @Override
  public ResponseControl[] getControls()
  {
    return getResponse().getControls();
  }


  /** {@inheritDoc} */
  @Override
  public String[] getReferralURLs()
  {
    return getResponse().getReferralURLs();
  }


  /** {@inheritDoc} */
  @Override
  public int getMessageId()
  {
    return getResponse().getMessageId();
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return getResponse().toString();
  }
}
