/*
  $Id$

  Copyright (C) 2003-2013 Virginia Tech.
  All rights reserved.

  SEE LICENSE FOR MORE INFORMATION

  Author:  Middleware Services
  Email:   middleware@vt.edu
  Version: $Revision$
  Updated: $Date$
*/
package org.ldaptive.auth;

import java.util.Arrays;
import org.ldaptive.Credential;
import org.ldaptive.LdapEntry;
import org.ldaptive.LdapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality to authenticate users against an ldap directory.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class Authenticator
{

  /** NoOp entry resolver. */
  private static final EntryResolver NOOP_RESOLVER = new NoOpEntryResolver();

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** For finding user DNs. */
  private DnResolver dnResolver;

  /** Handler to process authentication. */
  private AuthenticationHandler authenticationHandler;

  /** For finding user entries. */
  private EntryResolver entryResolver;

  /** Handlers to process authentication responses. */
  private AuthenticationResponseHandler[] authenticationResponseHandlers;

  /** Whether to execute the entry resolver on authentication failure. */
  private boolean resolveEntryOnFailure;


  /** Default constructor. */
  public Authenticator() {}


  /**
   * Creates a new authenticator.
   *
   * @param  resolver  dn resolver
   * @param  handler  authentication handler
   */
  public Authenticator(
    final DnResolver resolver,
    final AuthenticationHandler handler)
  {
    setDnResolver(resolver);
    setAuthenticationHandler(handler);
  }


  /**
   * Returns the DN resolver.
   *
   * @return  DN resolver
   */
  public DnResolver getDnResolver()
  {
    return dnResolver;
  }


  /**
   * Sets the DN resolver.
   *
   * @param  resolver  for finding DNs
   */
  public void setDnResolver(final DnResolver resolver)
  {
    dnResolver = resolver;
  }


  /**
   * Returns the authentication handler.
   *
   * @return  authentication handler
   */
  public AuthenticationHandler getAuthenticationHandler()
  {
    return authenticationHandler;
  }


  /**
   * Sets the authentication handler.
   *
   * @param  handler  for performing authentication
   */
  public void setAuthenticationHandler(final AuthenticationHandler handler)
  {
    authenticationHandler = handler;
  }


  /**
   * Returns the entry resolver.
   *
   * @return  entry resolver
   */
  public EntryResolver getEntryResolver()
  {
    return entryResolver;
  }


  /**
   * Sets the entry resolver.
   *
   * @param  resolver  for finding entries
   */
  public void setEntryResolver(final EntryResolver resolver)
  {
    entryResolver = resolver;
  }


  /**
   * Returns whether to execute the entry resolver on authentication failure.
   *
   * @return  whether to execute the entry resolver on authentication failure
   */
  public boolean getResolveEntryOnFailure()
  {
    return resolveEntryOnFailure;
  }


  /**
   * Sets whether to execute the entry resolver on authentication failure.
   *
   * @param  b  whether to execute the entry resolver
   */
  public void setResolveEntryOnFailure(final boolean b)
  {
    resolveEntryOnFailure = b;
  }


  /**
   * Returns the authentication response handlers.
   *
   * @return  authentication response handlers
   */
  public AuthenticationResponseHandler[] getAuthenticationResponseHandlers()
  {
    return authenticationResponseHandlers;
  }


  /**
   * Sets the authentication response handlers.
   *
   * @param  handlers  authentication response handlers
   */
  public void setAuthenticationResponseHandlers(
    final AuthenticationResponseHandler... handlers)
  {
    authenticationResponseHandlers = handlers;
  }


  /**
   * This will attempt to find the DN for the supplied user. {@link
   * DnResolver#resolve(String)} is invoked to perform this operation.
   *
   * @param  user  to find DN for
   *
   * @return  user DN
   *
   * @throws  LdapException  if an LDAP error occurs during resolution
   */
  public String resolveDn(final String user)
    throws LdapException
  {
    return dnResolver.resolve(user);
  }


  /**
   * Authenticate the user in the supplied request.
   *
   * @param  request  authentication request
   *
   * @return  response containing the ldap entry of the user authenticated
   *
   * @throws  LdapException  if an LDAP error occurs
   */
  public AuthenticationResponse authenticate(
    final AuthenticationRequest request)
    throws LdapException
  {
    return authenticate(resolveDn(request.getUser()), request);
  }


  /**
   * Performs authentication by opening a new connection to the LDAP and binding
   * as the supplied DN. If return attributes have been request, the user entry
   * will be searched on the same connection.
   *
   * @param  dn  to authenticate as
   * @param  request  containing authentication parameters
   *
   * @return  ldap entry for the supplied DN
   *
   * @throws  LdapException  if an LDAP error occurs
   */
  protected AuthenticationResponse authenticate(
    final String dn,
    final AuthenticationRequest request)
    throws LdapException
  {
    logger.debug("authenticate dn={} with request={}", dn, request);

    final AuthenticationResponse invalidInput = validateInput(dn, request);
    if (invalidInput != null) {
      return invalidInput;
    }

    LdapEntry entry = null;

    AuthenticationHandlerResponse response = null;
    try {
      final AuthenticationCriteria ac = new AuthenticationCriteria(dn);
      ac.setCredential(request.getCredential());

      // attempt to authenticate as this dn
      response = getAuthenticationHandler().authenticate(ac);
      // resolve the entry
      entry = resolveEntry(request, response, ac);
    } finally {
      if (response != null && response.getConnection() != null) {
        response.getConnection().close();
      }
    }

    logger.info(
      "Authentication {} for dn: {}",
      response.getResult() ? "succeeded" : "failed",
      dn);

    final AuthenticationResponse authResponse = new AuthenticationResponse(
      response.getResult() ?
        AuthenticationResultCode.AUTHENTICATION_HANDLER_SUCCESS :
        AuthenticationResultCode.AUTHENTICATION_HANDLER_FAILURE,
      response.getResultCode(),
      entry,
      response.getMessage(),
      response.getControls(),
      response.getMessageId());

    // execute authentication response handlers
    if (
      getAuthenticationResponseHandlers() != null &&
        getAuthenticationResponseHandlers().length > 0) {
      for (AuthenticationResponseHandler ah :
           getAuthenticationResponseHandlers()) {
        ah.process(authResponse);
      }
    }

    logger.debug(
      "authenticate response={} for dn={} with request={}",
      response,
      dn,
      request);
    return authResponse;
  }


  /**
   * Validates the authentication request and resolved DN. Returns an
   * authentication response if validation failed.
   *
   * @param  dn  to validate
   * @param  request  to validate
   *
   * @return  authentication response if validation failed, otherwise null
   */
  protected AuthenticationResponse validateInput(
    final String dn,
    final AuthenticationRequest request)
  {
    AuthenticationResponse response = null;
    final Credential credential = request.getCredential();
    if (credential == null || credential.getBytes() == null) {
      response = new AuthenticationResponse(
        AuthenticationResultCode.INVALID_CREDENTIAL,
        null,
        null,
        "Credential cannot be null",
        null,
        -1);
    } else if (credential.getBytes().length == 0) {
      response = new AuthenticationResponse(
        AuthenticationResultCode.INVALID_CREDENTIAL,
        null,
        null,
        "Credential cannot be empty",
        null,
        -1);
    } else if (dn == null) {
      response = new AuthenticationResponse(
        AuthenticationResultCode.DN_RESOLUTION_FAILURE,
        null,
        null,
        "DN cannot be null",
        null,
        -1);
    } else if (dn.isEmpty()) {
      response = new AuthenticationResponse(
        AuthenticationResultCode.DN_RESOLUTION_FAILURE,
        null,
        null,
        "DN cannot be empty",
        null,
        -1);
    }
    return response;
  }


  /**
   * Attempts to find the ldap entry for the supplied DN. If an entry resolver
   * has been configured it is used. A {@link SearchEntryResolver} is used if
   * return attributes have been requested. If none of these criteria is met, a
   * {@link NoOpDnResolver} is used.
   *
   * @param  request  authentication request
   * @param  response  from the authentication handler
   * @param  criteria  needed by the entry resolver
   *
   * @return  ldap entry
   *
   * @throws  LdapException  if an error occurs resolving the entry
   */
  protected LdapEntry resolveEntry(
    final AuthenticationRequest request,
    final AuthenticationHandlerResponse response,
    final AuthenticationCriteria criteria)
    throws LdapException
  {
    LdapEntry entry = null;
    EntryResolver er;
    if (resolveEntryOnFailure || response.getResult()) {
      if (entryResolver != null) {
        er = entryResolver;
      } else if (request.getReturnAttributes() == null ||
                 request.getReturnAttributes().length > 0) {
        er = new SearchEntryResolver(request.getReturnAttributes());
      } else {
        er = NOOP_RESOLVER;
      }
      try {
        entry = er.resolve(response.getConnection(), criteria);
        logger.trace("resolved entry={} with resolver={}", entry, er);
      } catch (LdapException e) {
        logger.debug("entry resolution failed for resolver={}", er, e);
      }
    }
    if (entry == null) {
      entry = NOOP_RESOLVER.resolve(response.getConnection(), criteria);
      logger.trace("resolved entry={} with resolver={}", entry, NOOP_RESOLVER);
    }
    return entry;
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return
      String.format(
        "[%s@%d::dnResolver=%s, authenticationHandler=%s, " +
        "entryResolver=%s, authenticationResponseHandlers=%s]",
        getClass().getName(),
        hashCode(),
        getDnResolver(),
        getAuthenticationHandler(),
        getEntryResolver(),
        Arrays.toString(getAuthenticationResponseHandlers()));
  }
}
