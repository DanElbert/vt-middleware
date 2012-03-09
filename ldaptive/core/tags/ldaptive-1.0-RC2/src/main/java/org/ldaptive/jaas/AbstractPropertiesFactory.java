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
package org.ldaptive.jaas;

import java.util.Map;
import java.util.Properties;
import org.ldaptive.props.PropertySource.PropertyDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides implementation common to properties based factories.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public abstract class AbstractPropertiesFactory
{

  /** Cache ID option used on the JAAS config. */
  public static final String CACHE_ID = "cacheId";

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * Returns context specific properties based on the supplied JAAS options.
   *
   * @param  options  to read properties from
   *
   * @return  properties
   */
  protected static Properties createProperties(final Map<String, ?> options)
  {
    final Properties p = new Properties();
    for (Map.Entry<String, ?> entry : options.entrySet()) {
      // if property name contains a dot, it isn't an ldaptive property
      // else add the domain to the ldaptive properties
      if (entry.getKey().indexOf(".") != -1) {
        p.setProperty(entry.getKey(), entry.getValue().toString());
      } else {
        p.setProperty(
          PropertyDomain.AUTH.value() + entry.getKey(),
          entry.getValue().toString());
      }
    }
    return p;
  }
}
