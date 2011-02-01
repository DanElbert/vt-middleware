/*
  $Id: AbstractLdapBean.java 1330 2010-05-23 22:10:53Z dfisher $

  Copyright (C) 2003-2010 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision: 1330 $
  Updated: $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
*/
package edu.vt.middleware.ldap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides common implementations to other ldap beans.
 *
 * @author  Middleware Services
 * @version  $Revision: 1330 $ $Date: 2010-05-23 18:10:53 -0400 (Sun, 23 May 2010) $
 */
public abstract class AbstractLdapBean
{

  /** Log for this class. */
  protected final Log logger = LogFactory.getLog(getClass());

  /** Sort behavior. */
  protected SortBehavior sortBehavior;


  /**
   * Creates a new abstract ldap bean.
   *
   * @param  sb  sort behavior
   */
  public AbstractLdapBean(final SortBehavior sb)
  {
    if (sb == null) {
      throw new IllegalArgumentException("Sort behavior cannot be null");
    }
    this.sortBehavior = sb;
  }


  /**
   * Returns the sort behavior for this ldap bean.
   *
   * @return  sort behavior
   */
  public SortBehavior getSortBehavior()
  {
    return this.sortBehavior;
  }


  /**
   * Returns whether the supplied object contains the same data as this bean.
   * Delegates to {@link #hashCode()} implementation.
   *
   * @param  o  to compare for equality
   *
   * @return  equality result
   */
  public boolean equals(final Object o)
  {
    if (o == null) {
      return false;
    }
    return
      o == this ||
        (this.getClass() == o.getClass() && o.hashCode() == this.hashCode());
  }


  /**
   * Returns the hash code for this object.
   *
   * @return  hash code
   */
  public abstract int hashCode();
}