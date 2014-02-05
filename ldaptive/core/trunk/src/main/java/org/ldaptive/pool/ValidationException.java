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
package org.ldaptive.pool;

/**
 * Thrown when an attempt to validate a pooled object fails.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ValidationException extends PoolException
{

  /** serialVersionUID. */
  private static final long serialVersionUID = -5043560632396467010L;


  /**
   * Creates a new validation exception.
   *
   * @param  msg  describing this exception
   */
  public ValidationException(final String msg)
  {
    super(msg);
  }


  /**
   * Creates a new validation exception.
   *
   * @param  e  pooling specific exception
   */
  public ValidationException(final Exception e)
  {
    super(e);
  }


  /**
   * Creates a new validation exception.
   *
   * @param  msg  describing this exception
   * @param  e  pooling specific exception
   */
  public ValidationException(final String msg, final Exception e)
  {
    super(msg, e);
  }
}
