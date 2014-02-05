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
package org.ldaptive.sasl;

/**
 * Contains all the configuration data for SASL GSSAPI authentication.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class GssApiConfig extends SaslConfig
{

  /** sasl realm. */
  private String saslRealm;


  /** Default constructor. */
  public GssApiConfig()
  {
    setMechanism(Mechanism.GSSAPI);
  }


  /**
   * Returns the sasl realm.
   *
   * @return  realm
   */
  public String getRealm()
  {
    return saslRealm;
  }


  /**
   * Sets the sasl realm.
   *
   * @param  realm  realm
   */
  public void setRealm(final String realm)
  {
    checkImmutable();
    logger.trace("setting realm: {}", realm);
    saslRealm = realm;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::mechanism=%s, authorizationId=%s, mutualAuthentication=%s, " +
        "qualityOfProtection=%s, securityStrength=%s, realm=%s]",
        getClass().getName(),
        hashCode(),
        getMechanism(),
        getAuthorizationId(),
        getMutualAuthentication(),
        getQualityOfProtection(),
        getSecurityStrength(),
        saslRealm);
  }
}
