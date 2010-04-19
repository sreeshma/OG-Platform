/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.opengamma.id.Identifier;
import com.opengamma.livedata.CollectingLiveDataListener;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.msg.SubscriptionType;

/**
 * 
 *
 * @author kirk
 */
public class SubscriptionHandleTest {

  @Test
  public void equalsDifferentRequestedSpecification() {
    LiveDataSpecification requestedSpecification1 =
      new LiveDataSpecification(
          "NormalizationId1",
          new Identifier("Domain1", "Value1"));
    CollectingLiveDataListener listener = new CollectingLiveDataListener();
    SubscriptionHandle handle1 = new SubscriptionHandle("kirk", SubscriptionType.NON_PERSISTENT, requestedSpecification1, listener);
    SubscriptionHandle handle2 = new SubscriptionHandle("kirk", SubscriptionType.NON_PERSISTENT, requestedSpecification1,  listener);
    assertTrue(handle1.equals(handle2));
  }

  @Test
  public void hashCodeDifferentRequestedSpecification() {
    LiveDataSpecification requestedSpecification1 =
      new LiveDataSpecification(
          "NormalizationId1",
          new Identifier("Domain1", "Value1"));
    CollectingLiveDataListener listener = new CollectingLiveDataListener();
    SubscriptionHandle handle1 = new SubscriptionHandle("kirk", SubscriptionType.NON_PERSISTENT, requestedSpecification1, listener);
    SubscriptionHandle handle2 = new SubscriptionHandle("kirk", SubscriptionType.NON_PERSISTENT, requestedSpecification1, listener);
    assertEquals(handle1.hashCode(), handle2.hashCode());
  }
}
