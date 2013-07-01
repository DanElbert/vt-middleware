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
package org.ldaptive.extended;

import org.ldaptive.Connection;

/**
 * Adds and removes unsolicited notification listeners to provider connections.
 *
 * @author  Middleware Services
 * @version  $Revision$ $Date$
 */
public class UnsolicitedNotifications
{

  /** Connection to configure unsolicited notifications on. */
  private final Connection connection;


  /**
   * Creates a new unsolicited notifications.
   *
   * @param  conn  connection
   */
  public UnsolicitedNotifications(final Connection conn)
  {
    connection = conn;
  }


  /**
   * Adds a listener to receive unsolicited notifications.
   *
   * @param  listener  to receive unsolicited notifications
   */
  public void addListener(final UnsolicitedNotificationListener listener)
  {
    connection.getProviderConnection().addUnsolicitedNotificationListener(
      listener);
  }


  /**
   * Removes a listener from receiving unsolicited notifications.
   *
   * @param  listener  to no longer receive unsolicited notifications
   */
  public void removeListener(final UnsolicitedNotificationListener listener)
  {
    connection.getProviderConnection().removeUnsolicitedNotificationListener(
      listener);
  }
}
