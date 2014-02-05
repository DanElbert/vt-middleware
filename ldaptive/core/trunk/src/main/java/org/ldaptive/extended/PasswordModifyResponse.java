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
package org.ldaptive.extended;

import java.nio.ByteBuffer;
import org.ldaptive.Credential;
import org.ldaptive.asn1.AbstractParseHandler;
import org.ldaptive.asn1.DERParser;
import org.ldaptive.asn1.DERPath;
import org.ldaptive.asn1.OctetStringType;

/**
 * Contains the response from an ldap password modify operation. See RFC 3062.
 * Response is defined as:
 *
 * <pre>
   PasswdModifyResponseValue ::= SEQUENCE {
     genPasswd       [0]     OCTET STRING OPTIONAL }
 * </pre>
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class PasswordModifyResponse extends AbstractExtendedResponse<Credential>
{


  /** {@inheritDoc} */
  @Override
  public String getOID()
  {
    // RFC defines the response name as absent
    return null;
  }


  /** {@inheritDoc} */
  @Override
  public void decode(final byte[] encoded)
  {
    final DERParser parser = new DERParser();
    parser.registerHandler(GenPasswdHandler.PATH, new GenPasswdHandler(this));
    parser.parse(ByteBuffer.wrap(encoded));
  }


  /** {@inheritDoc} */
  @Override
  public String toString()
  {
    return String.format("[%s@%d]", getClass().getName(), hashCode());
  }


  /** Parse handler implementation for the genPasswd. */
  private static class GenPasswdHandler
    extends AbstractParseHandler<PasswordModifyResponse>
  {

    /** DER path to generated password. */
    public static final DERPath PATH = new DERPath("/SEQ/CTX(0)");


    /**
     * Creates a new gen passwd handler.
     *
     * @param  response  to configure
     */
    public GenPasswdHandler(final PasswordModifyResponse response)
    {
      super(response);
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final DERParser parser, final ByteBuffer encoded)
    {
      getObject().setValue(new Credential(OctetStringType.decode(encoded)));
    }
  }
}
