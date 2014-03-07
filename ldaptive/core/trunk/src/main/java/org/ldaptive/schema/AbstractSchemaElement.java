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

import org.ldaptive.LdapUtils;

/**
 * Base class for schema elements.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class AbstractSchemaElement implements SchemaElement
{

  /** Regex to match zero or more spaces. */
  protected static final String WSP_REGEX = "[ ]*";

  /** Regex to match one or more non spaces. */
  protected static final String NO_WSP_REGEX = "[^ ]+";

  /** Description. */
  private String description;

  /** Extensions. */
  private Extensions extensions;


  /**
   * Returns the description.
   *
   * @return  description
   */
  public String getDescription()
  {
    return description;
  }


  /**
   * Sets the description
   *
   * @param  s  description
   */
  public void setDescription(final String s)
  {
    description = s;
  }


  /**
   * Returns the extensions.
   *
   * @return  extensions
   */
  public Extensions getExtensions()
  {
    return extensions;
  }


  /**
   * Sets the extensions.
   *
   * @param  e  extensions
   */
  public void setExtensions(final Extensions e)
  {
    extensions = e;
  }


  /**
   * Returns whether the supplied schema element has an extension name with a
   * value of 'true'.
   *
   * @param  <T>  type of schema element
   * @param  schemaElement  to inspect
   * @param  extensionName  to read boolean from
   *
   * @return  whether syntax has this boolean extension
   */
  public static <T extends AbstractSchemaElement> boolean
  containsBooleanExtension(
    final T schemaElement,
    final String extensionName)
  {
    if (schemaElement != null) {
      final Extensions exts = schemaElement.getExtensions();
      if (exts != null &&
        Boolean.parseBoolean(exts.getValue(extensionName))) {
        return true;
      }
    }
    return false;
  }


  /** {@inheritDoc} */
  @Override
  public abstract int hashCode();


  /** {@inheritDoc} */
  @Override
  public boolean equals(final Object o)
  {
    return LdapUtils.areEqual(this, o);
  }
}
