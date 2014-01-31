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
package org.ldaptive.ad.io;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DeltaTimeValueTranscoder}.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class DeltaTimeValueTranscoderTest
{

  /** Transcoder to test. */
  private final DeltaTimeValueTranscoder transcoder =
    new DeltaTimeValueTranscoder();


  /**
   * Time test data.
   *
   * @return  time test data
   *
   * @throws  Exception  if test data cannot be generated
   */
  @DataProvider(name = "times")
  public Object[][] createDates()
    throws Exception
  {
    return new Object[][] {
      new Object[] {
        2592000000L,
        "-25920000000000", },
    };
  }


  /**
   * @param  millis  to compare
   * @param  deltaTime  ldap attribute string value
   *
   * @throws  Exception  On test failure.
   */
  @Test(groups = {"io"}, dataProvider = "times")
  public void testTranscode(final Long millis, final String deltaTime)
    throws Exception
  {
    Assert.assertEquals(transcoder.decodeStringValue(deltaTime), millis);
    Assert.assertEquals(transcoder.encodeStringValue(millis), deltaTime);
  }
}
