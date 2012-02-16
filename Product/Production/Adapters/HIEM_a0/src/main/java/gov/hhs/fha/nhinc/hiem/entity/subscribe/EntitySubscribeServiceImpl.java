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
package gov.hhs.fha.nhinc.hiem.entity.subscribe;

import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecured;
import gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.EntityNotificationProducerSecuredPortType;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.entitysubscriptionmanagement.UnsupportedPolicyRequestFault;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestSecuredType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * 
 * @author dunnek
 */
public class EntitySubscribeServiceImpl {
    private static Log log = LogFactory.getLog(EntitySubscribeServiceImpl.class);

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitysubscriptionmanagementsecured";
    private static final String SERVICE_LOCAL_PART = "EntityNotificationProducerSecured";
    private static final String PORT_LOCAL_PART = "EntityNotificationProducerSecuredPortSoap";
    private static final String WSDL_FILE = "EntitySubscriptionManagementSecured.wsdl";

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentResponseType subscribeDocument(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeDocumentRequestType subscribeDocumentRequest) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageResponseType subscribeCdcBioPackage(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeCdcBioPackageRequestType subscribeCdcBioPackageRequest) {
        // TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public SubscribeResponse subscribe(
            gov.hhs.fha.nhinc.common.nhinccommonentity.SubscribeRequestType subscribeRequest, WebServiceContext context)
            throws InvalidFilterFault, InvalidMessageContentExpressionFault, InvalidProducerPropertiesExpressionFault,
            InvalidTopicExpressionFault, NotifyMessageNotSupportedFault, ResourceUnknownFault,
            SubscribeCreationFailedFault, TopicExpressionDialectUnknownFault, TopicNotSupportedFault,
            UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault {
        log.debug("begin subscribe");
        SubscribeResponse result = null;

        try {
            String url = getURL();
            AssertionType assertIn = subscribeRequest.getAssertion();

            EntityNotificationProducerSecuredPortType port = getPort(url, assertIn);

            SubscribeRequestSecuredType securedRequest = new SubscribeRequestSecuredType();
            securedRequest.setSubscribe(subscribeRequest.getSubscribe());
            securedRequest.setNhinTargetCommunities(subscribeRequest.getNhinTargetCommunities());

            // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default method
            // invocation
            result = port.subscribe(securedRequest);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidFilterFault ex) {
            throw new InvalidFilterFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidMessageContentExpressionFault ex) {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidProducerPropertiesExpressionFault ex) {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.NotifyMessageNotSupportedFault ex) {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicExpressionDialectUnknownFault ex) {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnacceptableInitialTerminationTimeFault ex) {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnrecognizedPolicyRequestFault ex) {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), ex.getFaultInfo(), ex);
        } catch (gov.hhs.fha.nhinc.entitysubscriptionmanagementsecured.UnsupportedPolicyRequestFault ex) {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), ex.getFaultInfo(), ex);
        }

        return result;
    }

    private String getURL() {
        String url = "";

        try {
            url = ConnectionManagerCache.getInstance().getLocalEndpointURLByServiceName(
                    NhincConstants.HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME_SECURED);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected EntityNotificationProducerSecuredPortType getPort(String url, AssertionType assertIn) {
        EntityNotificationProducerSecuredPortType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        EntityNotificationProducerSecuredPortType.class);

                log.info("Setting endpoint address to Entity Notification Producer Secured Service to " + url);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME_SECURED, null, assertIn);
            } else {
                log.error("Unable to obtain service - no port created.");
            }
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    private Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}