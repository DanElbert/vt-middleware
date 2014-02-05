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
package org.ldaptive.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Contains data associated with a query request.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class Query
{

  /** Used for setting empty terms. */
  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  /** Query separated into terms. */
  private final String[] terms;

  /** Attributes to return with the ldap query. */
  private String[] returnAttributes;

  /** Additional restrictions to place on every query. */
  private String searchRestrictions;

  /** Start index of search results to return. */
  private Integer fromResult;

  /** End index of search results to return. */
  private Integer toResult;


  /**
   * Parses the query from a string into query terms.
   *
   * @param  query  to parse
   */
  public Query(final String query)
  {
    if (query != null) {
      final List<String> l = new ArrayList<String>();
      final StringTokenizer queryTokens = new StringTokenizer(
        query.toLowerCase().trim());
      while (queryTokens.hasMoreTokens()) {
        l.add(queryTokens.nextToken());
      }
      terms = l.toArray(new String[l.size()]);
    } else {
      terms = EMPTY_STRING_ARRAY;
    }
  }


  /**
   * Returns the terms.
   *
   * @return  query terms
   */
  public String[] getTerms()
  {
    return terms;
  }


  /**
   * Sets the return attributes.
   *
   * @param  attrs  return attributes
   */
  public void setReturnAttributes(final String[] attrs)
  {
    returnAttributes = attrs;
  }


  /**
   * Returns the return attributes.
   *
   * @return  return attributes
   */
  public String[] getReturnAttributes()
  {
    return returnAttributes;
  }


  /**
   * Sets the search restrictions.
   *
   * @param  restrictions  search restrictions
   */
  public void setSearchRestrictions(final String restrictions)
  {
    searchRestrictions = restrictions;
  }


  /**
   * Returns the search restrictions.
   *
   * @return  search restrictions
   */
  public String getSearchRestrictions()
  {
    return searchRestrictions;
  }


  /**
   * Sets the index of the result to begin searching.
   *
   * @param  i  from index
   */
  public void setFromResult(final Integer i)
  {
    fromResult = i;
  }


  /**
   * Returns the from result.
   *
   * @return  from result
   */
  public Integer getFromResult()
  {
    return fromResult;
  }


  /**
   * Sets the index of the result to stop searching.
   *
   * @param  i  to result
   */
  public void setToResult(final Integer i)
  {
    toResult = i;
  }


  /**
   * Returns the to result.
   *
   * @return  to result
   */
  public Integer getToResult()
  {
    return toResult;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::terms=%s, returnAttributes=%s, searchRestrictions=%s, " +
        "fromResult=%d, toResult=%d]",
        getClass().getName(),
        hashCode(),
        Arrays.toString(terms),
        Arrays.toString(returnAttributes),
        searchRestrictions,
        fromResult,
        toResult);
  }
}
