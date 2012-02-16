/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 * 
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredResponseImpl {

    /**
     * 
     * @param crossGatewayRetrieveResponse
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(
            RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse, WebServiceContext context) {

        AssertionType assertion = extractAssertionFromContext(context, crossGatewayRetrieveResponse.getAssertion());
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = crossGatewayRetrieveResponse
                .getRetrieveDocumentSetResponse();
        NhinTargetCommunitiesType nhinTarget = crossGatewayRetrieveResponse.getNhinTargetCommunities();

        return new EntityDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse,
                assertion, nhinTarget);
    }

    /**
     * 
     * @param body
     * @return DocRetrieveAcknowledgementType
     */
    protected DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(
            RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, WebServiceContext context) {

        AssertionType assertion = extractAssertionFromContext(context, null);
        RetrieveDocumentSetResponseType retrieveDocumentSetResponse = body.getRetrieveDocumentSetResponse();
        NhinTargetCommunitiesType nhinTarget = body.getNhinTargetCommunities();

        return new EntityDocRetrieveDeferredRespOrchImpl().crossGatewayRetrieveResponse(retrieveDocumentSetResponse,
                assertion, nhinTarget);
    }

    /**
     * 
     * @param context
     * @param oAssertionIn
     * @return AssertionType
     */
    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;

        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList)) {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }
}