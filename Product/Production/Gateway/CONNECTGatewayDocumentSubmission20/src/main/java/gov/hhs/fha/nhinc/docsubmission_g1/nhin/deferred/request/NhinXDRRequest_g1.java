/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission_g1.nhin.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "XDRDeferredRequest_Service", portName = "XDRDeferredRequest_Port_Soap", endpointInterface = "nhin.deferred.XDRDeferredRequestPortType", targetNamespace = "urn:nhin:Deferred", wsdlLocation = "WEB-INF/wsdl/NhinXDRRequest/NhinXDRDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled=true)
public class NhinXDRRequest_g1 {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.XDRAcknowledgementType provideAndRegisterDocumentSetBDeferredRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        return new NhinDocSubmissionDeferredRequestImpl_g1().provideAndRegisterDocumentSetBRequest(body, context);
    }

}