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
package org.ldaptive.props;

import java.io.Reader;
import java.util.Properties;
import java.util.Set;
import org.ldaptive.BindConnectionInitializer;

/**
 * Reads properties specific to {@link BindConnectionInitializer} and returns an
 * initialized object of that type.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public final class BindConnectionInitializerPropertySource
  extends AbstractPropertySource<BindConnectionInitializer>
{

  /** Invoker for bind connection initializer. */
  private static final BindConnectionInitializerPropertyInvoker INVOKER =
    new BindConnectionInitializerPropertyInvoker(
      BindConnectionInitializer.class);


  /**
   * Creates a new bind connection initializer property source using the default
   * properties file.
   *
   * @param  initializer  bind connection initializer to invoke properties on
   */
  public BindConnectionInitializerPropertySource(
    final BindConnectionInitializer initializer)
  {
    this(initializer, PROPERTIES_FILE);
  }


  /**
   * Creates a new bind connection initializer property source.
   *
   * @param  initializer  bind connection initializer to invoke properties on
   * @param  paths  to read properties from
   */
  public BindConnectionInitializerPropertySource(
    final BindConnectionInitializer initializer,
    final String... paths)
  {
    this(initializer, loadProperties(paths));
  }


  /**
   * Creates a new bind connection initializer property source.
   *
   * @param  initializer  bind connection initializer to invoke properties on
   * @param  readers  to read properties from
   */
  public BindConnectionInitializerPropertySource(
    final BindConnectionInitializer initializer,
    final Reader... readers)
  {
    this(initializer, loadProperties(readers));
  }


  /**
   * Creates a new bind connection initializer property source.
   *
   * @param  initializer  bind connection initializer to invoke properties on
   * @param  props  to read properties from
   */
  public BindConnectionInitializerPropertySource(
    final BindConnectionInitializer initializer,
    final Properties props)
  {
    this(initializer, PropertyDomain.LDAP, props);
  }


  /**
   * Creates a new bind connection initializer property source.
   *
   * @param  initializer  bind connection initializer to invoke properties on
   * @param  domain  that properties are in
   * @param  props  to read properties from
   */
  public BindConnectionInitializerPropertySource(
    final BindConnectionInitializer initializer,
    final PropertyDomain domain,
    final Properties props)
  {
    super(initializer, domain, props);
  }


  /** {@inheritDoc} */
  @Override
  public void initialize()
  {
    initializeObject(INVOKER);
  }


  /**
   * Returns the property names for this property source.
   *
   * @return  all property names
   */
  public static Set<String> getProperties()
  {
    return INVOKER.getProperties();
  }
}
