/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.client;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeFieldContainer;
import org.fudgemsg.FudgeMsgEnvelope;
import org.fudgemsg.mapping.FudgeDeserializationContext;
import org.fudgemsg.mapping.FudgeSerializationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.livedata.LiveDataSpecification;
import com.opengamma.livedata.ResolveRequest;
import com.opengamma.livedata.ResolveResponse;
import com.opengamma.transport.FudgeMessageReceiver;
import com.opengamma.transport.FudgeRequestSender;
import com.opengamma.util.ArgumentChecker;

/**
 * 
 *
 * @author pietari
 */
public class DistributedSpecificationResolver {
  
  public static final long TIMEOUT_MS = 5 * 60 * 100l;
  private static final Logger s_logger = LoggerFactory.getLogger(DistributedSpecificationResolver.class);
  private final FudgeRequestSender _requestSender;
  private final FudgeContext _fudgeContext;
  
  public DistributedSpecificationResolver(FudgeRequestSender requestSender) {
    this(requestSender, new FudgeContext());
  }
  
  public DistributedSpecificationResolver(FudgeRequestSender requestSender, FudgeContext fudgeContext) {
    ArgumentChecker.checkNotNull(requestSender, "Request Sender");
    ArgumentChecker.checkNotNull(fudgeContext, "Fudge Context");
    _requestSender = requestSender;
    _fudgeContext = fudgeContext;
  }
  
  public LiveDataSpecification resolve(
      LiveDataSpecification spec) {
    
    s_logger.info("Sending message to resolve ", spec);
    ResolveRequest resolveRequest = new ResolveRequest(spec);
    FudgeFieldContainer requestMessage = resolveRequest.toFudgeMsg(new FudgeSerializationContext(_fudgeContext));
    final AtomicBoolean responseReceived = new AtomicBoolean(false);
    final AtomicReference<LiveDataSpecification> resolved = new AtomicReference<LiveDataSpecification>();
    _requestSender.sendRequest(requestMessage, new FudgeMessageReceiver() {
      
      @Override
      public void messageReceived(FudgeContext fudgeContext,
          FudgeMsgEnvelope msgEnvelope) {
        
        FudgeFieldContainer msg = msgEnvelope.getMessage();
        ResolveResponse response = ResolveResponse.fromFudgeMsg(new FudgeDeserializationContext(_fudgeContext), msg);
        resolved.set(response.getResolvedSpecification());
        responseReceived.set(true);
        
      }
    });
    long start = System.currentTimeMillis();
    while(!responseReceived.get()) {
      try {
        Thread.sleep(100l);
      } catch (InterruptedException e) {
        Thread.interrupted();
      }
      if((System.currentTimeMillis() - start) >= TIMEOUT_MS) {
        throw new OpenGammaRuntimeException("Timeout. Waited for entitlement response for " + TIMEOUT_MS + " with no response.");
      }
    }
    
    return resolved.get();
  }

}
