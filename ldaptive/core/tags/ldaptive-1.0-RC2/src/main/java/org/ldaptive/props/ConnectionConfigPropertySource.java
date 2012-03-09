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
import org.ldaptive.ConnectionConfig;
import org.ldaptive.ssl.SslConfig;

/**
 * Reads properties specific to {@link ConnectionConfig} and returns an
 * initialized object of that type.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public final class ConnectionConfigPropertySource
  extends AbstractPropertySource<ConnectionConfig>
{

  /** Invoker for connection config. */
  private static final ConnectionConfigPropertyInvoker INVOKER =
    new ConnectionConfigPropertyInvoker(ConnectionConfig.class);


  /**
   * Creates a new connection config property source using the default
   * properties file.
   *
   * @param  cc  connection config to invoke properties on
   */
  public ConnectionConfigPropertySource(final ConnectionConfig cc)
  {
    this(cc, PROPERTIES_FILE);
  }


  /**
   * Creates a new connection config property source.
   *
   * @param  cc  connection config to invoke properties on
   * @param  paths  to read properties from
   */
  public ConnectionConfigPropertySource(
    final ConnectionConfig cc,
    final String... paths)
  {
    this(cc, loadProperties(paths));
  }


  /**
   * Creates a new connection config property source.
   *
   * @param  cc  connection config to invoke properties on
   * @param  readers  to read properties from
   */
  public ConnectionConfigPropertySource(
    final ConnectionConfig cc,
    final Reader... readers)
  {
    this(cc, loadProperties(readers));
  }


  /**
   * Creates a new connection config property source.
   *
   * @param  cc  connection config to invoke properties on
   * @param  props  to read properties from
   */
  public ConnectionConfigPropertySource(
    final ConnectionConfig cc,
    final Properties props)
  {
    this(cc, PropertyDomain.LDAP, props);
  }


  /**
   * Creates a new connection config property source.
   *
   * @param  cc  connection config to invoke properties on
   * @param  domain  that properties are in
   * @param  props  to read properties from
   */
  public ConnectionConfigPropertySource(
    final ConnectionConfig cc,
    final PropertyDomain domain,
    final Properties props)
  {
    super(cc, domain, props);
  }


  /** {@inheritDoc} */
  @Override
  public void initialize()
  {
    initializeObject(INVOKER);

    SslConfig sc = object.getSslConfig();
    if (sc == null) {
      sc = new SslConfig();

      final SslConfigPropertySource scSource = new SslConfigPropertySource(
        sc, propertiesDomain, properties);
      scSource.initialize();
      object.setSslConfig(sc);
    } else {
      final SimplePropertySource<SslConfig> sPropSource =
        new SimplePropertySource<SslConfig>(sc, propertiesDomain, properties);
      sPropSource.initialize();
    }
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
