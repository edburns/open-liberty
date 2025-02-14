/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.sib.mfp.impl;

import java.util.List;

import com.ibm.ws.sib.mfp.*;
import com.ibm.ws.sib.mfp.control.*;
import com.ibm.ws.sib.mfp.schema.ControlAccess;
import com.ibm.ws.sib.utils.ras.SibTr;
import com.ibm.websphere.ras.TraceComponent;

/**
 *  ControlReject extends ControlMessageImpl and hence JsMessageImpl,
 *  and is the implementation class for the ControlReject interface.
 */
final class ControlRejectImpl extends ControlMessageImpl implements ControlReject {

  private final static long serialVersionUID = 1L;

  private static TraceComponent tc = SibTr.register(ControlRejectImpl.class, MfpConstants.MSG_GROUP, MfpConstants.MSG_BUNDLE);

  /* **************************************************************************/
  /* Constructors                                                             */
  /* **************************************************************************/

  /**
   *  Constructor for a new Control Reject Message.
   *
   *  This constructor should never be used except by JsMessageImpl.createNew().
   *  The method must not actually do anything.
   */
  ControlRejectImpl() {
  }

  /**
   *  Constructor for a new Control Reject Message.
   *  To be called only by the ControlMessageFactory.
   *
   *  @param flag No-op flag to distinguish different constructors.
   *
   *  @exception MessageDecodeFailedException Thrown if such a message can not be created
   */
  ControlRejectImpl(int flag) throws MessageDecodeFailedException {
    super(flag);
    if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) SibTr.debug(tc, "<init>");
    setControlMessageType(ControlMessageType.REJECT);
  }


  /**
   *  Constructor for an inbound message.
   *  (Only to be called by a superclass make method.)
   *
   *  @param inJmo The JsMsgObject representing the inbound message
   */
  ControlRejectImpl(JsMsgObject inJmo) {
    super(inJmo);
    if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) SibTr.debug(tc, "<init>, inbound jmo ");
  }


  /* **************************************************************************/
  /* Get Methods                                                              */
  /* **************************************************************************/

  /*
   *  Get the Start Tick value from the message.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public final long[]  getStartTick() {
    List list = (List)jmo.getField(ControlAccess.BODY_REJECT_STARTTICK);

    long lists[] = new long[list.size()];

    for (int i = 0; i < lists.length; i++)
      lists[i] = ((Long)list.get(i)).longValue();

    return lists;
  }

  /*
   *  Get the End Tick value from the message.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public final long[]  getEndTick() {
    List list = (List)jmo.getField(ControlAccess.BODY_REJECT_ENDTICK);

    long lists[] = new long[list.size()];

    for (int i = 0; i < lists.length; i++)
      lists[i] = ((Long)list.get(i)).longValue();

    return lists;
  }

  /*
   * Get the recovery value for this request.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public boolean getRecovery() {
    return jmo.getBooleanField(ControlAccess.BODY_REJECT_RECOVERY);
  }

  /*
   * Get the RMEUnlockCountValue for this request.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public long[] getRMEUnlockCount() {
    long[] result;
    // if the value is unset then it has come from an environment with back level schema
    if (jmo.getChoiceField(ControlAccess.BODY_REJECT_RMEUNLOCKCOUNT) != ControlAccess.IS_BODY_REJECT_RMEUNLOCKCOUNT_UNSET) {
      List list = (List)jmo.getField(ControlAccess.BODY_REJECT_RMEUNLOCKCOUNT_VALUE);

      result = new long[list.size()];

      for (int i = 0; i < result.length; i++)
        result[i] = ((Long)list.get(i)).longValue();
    }
    else {
      result = new long[0];
    }
    return result;
  }

  /*
   * Get summary trace line for this message 
   * 
   *  Javadoc description supplied by ControlMessage interface.
   */
  public void getTraceSummaryLine(StringBuilder buff) {
    
    // Get the common fields for control messages
    super.getTraceSummaryLine(buff);
    
    appendArray(buff, "startTick", getStartTick());

    appendArray(buff, "endTick", getEndTick());
    
    buff.append("],recovery=");
    buff.append(getRecovery());
    
    appendArray(buff, "rmeUnlockCount", getRMEUnlockCount());
    
  }
  
  /* **************************************************************************/
  /* Set Methods                                                              */
  /* **************************************************************************/

  /*
   *  Set the Start Tick values in the message.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public final void setStartTick(long[] values) {
    jmo.setField(ControlAccess.BODY_REJECT_STARTTICK, values);
  }

  /*
   *  Set the End Tick values in the message.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public final void setEndTick(long[] values) {
    jmo.setField(ControlAccess.BODY_REJECT_ENDTICK, values);
  }

  /*
   * Sets the Recovery value for this request
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public final void setRecovery(boolean value) {
    jmo.setBooleanField(ControlAccess.BODY_REJECT_RECOVERY, value);
  }

  /**
   * Set the RMEUnlockCountValue for this request.
   *
   *  Javadoc description supplied by ControlReject interface.
   */
  public void setRMEUnlockCount(long[] value) {
    jmo.setField(ControlAccess.BODY_REJECT_RMEUNLOCKCOUNT_VALUE, value);
  }
}
