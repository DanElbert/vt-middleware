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
package org.ldaptive.pool;

import org.ldaptive.AbstractConfig;

/**
 * Contains all the configuration data that the pooling implementations need to
 * control the pool.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class PoolConfig extends AbstractConfig
{

  /** Default min pool size, value is {@value}. */
  public static final int DEFAULT_MIN_POOL_SIZE = 3;

  /** Default max pool size, value is {@value}. */
  public static final int DEFAULT_MAX_POOL_SIZE = 10;

  /** Default validate on check in, value is {@value}. */
  public static final boolean DEFAULT_VALIDATE_ON_CHECKIN = false;

  /** Default validate on check out, value is {@value}. */
  public static final boolean DEFAULT_VALIDATE_ON_CHECKOUT = false;

  /** Default validate periodically, value is {@value}. */
  public static final boolean DEFAULT_VALIDATE_PERIODICALLY = false;

  /** Default validate period, value is {@value}. */
  public static final long DEFAULT_VALIDATE_PERIOD = 1800;

  /** Default maximum average idle time, value is {@value}. */
  public static final long DEFAULT_AVERAGE_IDLE_TIME = 300;

  /** Min pool size. */
  private int minPoolSize = DEFAULT_MIN_POOL_SIZE;

  /** Max pool size. */
  private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;

  /** Whether the ldap object should be validated when returned to the pool. */
  private boolean validateOnCheckIn = DEFAULT_VALIDATE_ON_CHECKIN;

  /** Whether the ldap object should be validated when given from the pool. */
  private boolean validateOnCheckOut = DEFAULT_VALIDATE_ON_CHECKOUT;

  /** Whether the pool should be validated periodically. */
  private boolean validatePeriodically = DEFAULT_VALIDATE_PERIODICALLY;

  /** Time in seconds that the validate pool should repeat. */
  private long validatePeriod = DEFAULT_VALIDATE_PERIOD;

  /** Time in seconds that is the maximum average idle time of connections. */
  private long averageIdleTime = DEFAULT_AVERAGE_IDLE_TIME;


  /**
   * Returns the min pool size. Default value is {@link #DEFAULT_MIN_POOL_SIZE}.
   * This value represents the size of the pool after a prune has occurred.
   *
   * @return  min pool size
   */
  public int getMinPoolSize()
  {
    return minPoolSize;
  }


  /**
   * Sets the min pool size.
   *
   * @param  size  min pool size
   */
  public void setMinPoolSize(final int size)
  {
    checkImmutable();
    if (size >= 0) {
      logger.trace("setting minPoolSize: {}", size);
      minPoolSize = size;
    }
  }


  /**
   * Returns the max pool size. Default value is {@link #DEFAULT_MAX_POOL_SIZE}.
   * This value may or may not be strictly enforced depending on the pooling
   * implementation.
   *
   * @return  max pool size
   */
  public int getMaxPoolSize()
  {
    return maxPoolSize;
  }


  /**
   * Sets the max pool size.
   *
   * @param  size  max pool size
   */
  public void setMaxPoolSize(final int size)
  {
    checkImmutable();
    if (size >= 0) {
      logger.trace("setting maxPoolSize: {}", size);
      maxPoolSize = size;
    }
  }


  /**
   * Returns the validate on check in flag. Default value is {@link
   * #DEFAULT_VALIDATE_ON_CHECKIN}.
   *
   * @return  validate on check in
   */
  public boolean isValidateOnCheckIn()
  {
    return validateOnCheckIn;
  }


  /**
   * Sets the validate on check in flag.
   *
   * @param  b  validate on check in
   */
  public void setValidateOnCheckIn(final boolean b)
  {
    checkImmutable();
    logger.trace("setting validateOnCheckIn: {}", b);
    validateOnCheckIn = b;
  }


  /**
   * Returns the validate on check out flag. Default value is {@link
   * #DEFAULT_VALIDATE_ON_CHECKOUT}.
   *
   * @return  validate on check in
   */
  public boolean isValidateOnCheckOut()
  {
    return validateOnCheckOut;
  }


  /**
   * Sets the validate on check out flag.
   *
   * @param  b  validate on check out
   */
  public void setValidateOnCheckOut(final boolean b)
  {
    checkImmutable();
    logger.trace("setting validateOnCheckOut: {}", b);
    validateOnCheckOut = b;
  }


  /**
   * Returns the validate periodically flag. Default value is {@link
   * #DEFAULT_VALIDATE_PERIODICALLY}.
   *
   * @return  validate periodically
   */
  public boolean isValidatePeriodically()
  {
    return validatePeriodically;
  }


  /**
   * Sets the validate periodically flag.
   *
   * @param  b  validate periodically
   */
  public void setValidatePeriodically(final boolean b)
  {
    checkImmutable();
    logger.trace("setting validatePeriodically: {}", b);
    validatePeriodically = b;
  }


  /**
   * Returns the validate period. Default value is {@link
   * #DEFAULT_VALIDATE_PERIOD}.
   *
   * @return  validate period in seconds
   */
  public long getValidatePeriod()
  {
    return validatePeriod;
  }


  /**
   * Sets the period for which the pool will be validated.
   *
   * @param  time  in seconds
   */
  public void setValidatePeriod(final long time)
  {
    checkImmutable();
    if (time >= 0) {
      logger.trace("setting validatePeriod: {}", time);
      validatePeriod = time;
    }
  }


  /**
   * Returns the maximum average idle time for connections. Default value is
   * {@link #DEFAULT_AVERAGE_IDLE_TIME}. Pool size will be reduced by pruning if
   * the average of all idle times in the pool exceeds this value.. This value
   * does not apply to connections in the pool if the pool has only the minimum
   * number of connections available.
   *
   * @return  maximum average idle time in seconds
   */
  public long getAverageIdleTime()
  {
    return averageIdleTime;
  }


  /**
   * Sets the maximum average idle time for connections in the pool.
   *
   * @param  time  in seconds
   */
  public void setAverageIdleTime(final long time)
  {
    checkImmutable();
    if (time >= 0) {
      logger.trace("setting averageIdleTime: {}", time);
      averageIdleTime = time;
    }
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::minPoolSize=%s, maxPoolSize=%s, validateOnCheckIn=%s, " +
        "validateOnCheckOut=%s, validatePeriodically=%s, validatePeriod=%s, " +
        "averageIdleTime=%s]",
        getClass().getName(),
        hashCode(),
        minPoolSize,
        maxPoolSize,
        validateOnCheckIn,
        validateOnCheckOut,
        validatePeriodically,
        validatePeriod,
        averageIdleTime);
  }
}
