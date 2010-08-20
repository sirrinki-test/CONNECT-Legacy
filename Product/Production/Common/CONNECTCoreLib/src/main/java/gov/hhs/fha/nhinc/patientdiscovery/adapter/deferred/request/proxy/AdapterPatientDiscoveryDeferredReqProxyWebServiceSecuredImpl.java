/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscoverysecuredasyncreq.AdapterPatientDiscoverySecuredAsyncReqPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredReqProxyWebServiceSecuredImpl implements AdapterPatientDiscoveryDeferredReqProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncreq";
    private static final String SERVICE_LOCAL_PART = "AdapterPatientDiscoverySecuredAsyncReq";
    private static final String PORT_LOCAL_PART = "AdapterPatientDiscoverySecuredAsyncReqPortSoap";
    private static final String WSDL_FILE = "AdapterPatientDiscoverySecuredAsyncReq.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoverysecuredasyncreq:ProcessPatientDiscoveryAsyncReqAsyncRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterPatientDiscoveryDeferredReqProxyWebServiceSecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterPatientDiscoverySecuredAsyncReqPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        AdapterPatientDiscoverySecuredAsyncReqPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPatientDiscoverySecuredAsyncReqPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion) {
        log.debug("Begin processPatientDiscoveryAsyncReqError");
        MCCIIN000002UV01 ack = null;

        try {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_DISCOVERY_ADAPTER_SECURED_ASYNC_REQ_SERVICE_NAME);
            AdapterPatientDiscoverySecuredAsyncReqPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if (request == null) {
                log.error("Request was null");
            }  else if (port == null) {
                log.error("port was null");
            } else {
                RespondingGatewayPRPAIN201305UV02SecuredRequestType securedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
                securedRequest.setPRPAIN201305UV02(request);

                ack = (MCCIIN000002UV01) oProxyHelper.invokePort(port, AdapterPatientDiscoverySecuredAsyncReqPortType.class, "processPatientDiscoveryAsyncReq", securedRequest);
            }
        } catch (Exception ex) {
            log.error("Error calling processPatientDiscoveryAsyncReq: " + ex.getMessage(), ex);
            ack = HL7AckTransforms.createAckFrom201305(request, NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
        }

        log.debug("End processPatientDiscoveryAsyncReqError");
        return ack;
    }
}
