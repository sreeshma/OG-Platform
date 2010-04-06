/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.server;

import org.junit.Test;

import com.opengamma.id.DomainSpecificIdentifier;
import com.opengamma.id.DomainSpecificIdentifiers;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.normalization.StandardRules;
import com.opengamma.livedata.resolver.CachingDistributionSpecificationResolver;
import com.opengamma.livedata.resolver.DistributionSpecificationResolver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 
 *
 * @author pietari
 */
public class CachingDistributionSpecificationResolverTest {
  
  @Test
  public void testCaching() {
    
    DomainSpecificIdentifier id = new DomainSpecificIdentifier("foo", "bar");
    
    LiveDataSpecification request = new LiveDataSpecification(
        "TestNormalization",
        new DomainSpecificIdentifier("foo", "bar"));
    
    DistributionSpecification returnValue = new DistributionSpecification(
        new DomainSpecificIdentifiers(id),
        StandardRules.getNoNormalization(),
        "testtopic");
    
    DistributionSpecificationResolver underlying = mock(DistributionSpecificationResolver.class);
    when(underlying.getDistributionSpecification(request)).thenReturn(returnValue);
    
    CachingDistributionSpecificationResolver resolver = new CachingDistributionSpecificationResolver(underlying);
    assertEquals(returnValue, resolver.getDistributionSpecification(request));
    assertEquals(returnValue, resolver.getDistributionSpecification(request));
    
    verify(underlying, times(1)).getDistributionSpecification(request);
  }

}
