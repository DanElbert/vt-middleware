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
package org.ldaptive.schema;

/**
 * Interface for schema elements.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public interface SchemaElement
{


  /**
   * Returns this schema element as formatted string per RFC 4512.
   *
   * @return  formatted string
   */
  String format();
}
