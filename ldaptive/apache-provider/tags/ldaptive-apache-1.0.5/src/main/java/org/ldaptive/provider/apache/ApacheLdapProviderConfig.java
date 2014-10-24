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
package org.ldaptive.provider.apache;

import java.util.Arrays;
import org.apache.directory.api.ldap.model.message.Control;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.ldaptive.ResultCode;
import org.ldaptive.provider.ControlProcessor;
import org.ldaptive.provider.ProviderConfig;

/**
 * Contains configuration data for the Apache Ldap provider.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class ApacheLdapProviderConfig extends ProviderConfig<Control>
{

  /** Connection configuration. */
  private LdapConnectionConfig connectionConfig;


  /** Default constructor. */
  public ApacheLdapProviderConfig()
  {
    setOperationExceptionResultCodes(ResultCode.SERVER_DOWN);
    setControlProcessor(
      new ControlProcessor<Control>(new ApacheLdapControlHandler()));
  }


  /**
   * Returns the connection configuration.
   *
   * @return  connection configuration
   */
  public LdapConnectionConfig getLdapConnectionConfig()
  {
    return connectionConfig;
  }


  /**
   * Sets the connection configuration.
   *
   * @param  config  connection configuration
   */
  public void setLdapConnectionConfig(final LdapConnectionConfig config)
  {
    checkImmutable();
    logger.trace("setting ldapConnectionConfig: {}", config);
    connectionConfig = config;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::operationExceptionResultCodes=%s, properties=%s, " +
        "connectionStrategy=%s, controlProcessor=%s, ldapConnectionConfig=%s]",
        getClass().getName(),
        hashCode(),
        Arrays.toString(getOperationExceptionResultCodes()),
        getProperties(),
        getConnectionStrategy(),
        getControlProcessor(),
        connectionConfig);
  }
}
