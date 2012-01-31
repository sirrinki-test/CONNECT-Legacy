/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyPatientDiscoveryAsyncResp", portName = "NhincProxyPatientDiscoveryAsyncRespPortType", endpointInterface = "gov.hhs.fha.nhinc.nhincproxypatientdiscoveryasyncresp.NhincProxyPatientDiscoveryAsyncRespPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxypatientdiscoveryasyncresp", wsdlLocation = "WEB-INF/wsdl/NhincProxyPatientDiscoveryAsyncResp/NhincProxyPatientDiscoveryAsyncResp.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class NhincProxyPatientDiscoveryAsyncResp extends PatientDiscoveryBase {
	@Resource
	private WebServiceContext context;

	public NhincProxyPatientDiscoveryAsyncResp() {
		super();
	}

	public NhincProxyPatientDiscoveryAsyncResp(
			PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}
	
	public org.hl7.v3.MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(
			org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType proxyProcessPatientDiscoveryAsyncRespRequest) {
		return getServiceFactory().getNhincProxyPatientDiscoveryAsyncRespImpl()
				.proxyProcessPatientDiscoveryAsyncResp(
						proxyProcessPatientDiscoveryAsyncRespRequest,
						getWebServiceContext());
		}

	protected WebServiceContext getWebServiceContext() {
		return context;
	}

}