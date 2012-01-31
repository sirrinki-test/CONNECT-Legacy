/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrievePolicyTransformer_a0 implements PolicyTransformer {

	@Override
	public CheckPolicyRequestType transform(Orchestratable message,
			Direction direction) {
		CheckPolicyRequestType policyReq = null;
		if (message instanceof OutboundDocRetrieveOrchestratable) {
			policyReq = tranform((OutboundDocRetrieveOrchestratable) message, direction);
		}
		return policyReq;
	}

	public CheckPolicyRequestType tranform(
			OutboundDocRetrieveOrchestratable EntityDROrchMessage,
			Direction direction) {
		CheckPolicyRequestType policyReq = null;
		DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
		if (Direction.INBOUND == direction)
			policyCheckReq
					.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
		else
			policyCheckReq
					.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);

		gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType();
		request.setAssertion(EntityDROrchMessage.getAssertion());
		request.setRetrieveDocumentSetRequest(EntityDROrchMessage.getRequest());
		policyCheckReq.setMessage(request);
		PolicyEngineChecker policyChecker = new PolicyEngineChecker();
		policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
		return policyReq;
	}

	
		
}