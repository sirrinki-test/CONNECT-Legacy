/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author mweaver
 */
public abstract class CONNECTOrchestrationBase {

    private static final Log logger = LogFactory.getLog(CONNECTOrchestrationBase.class);

    protected Log getLogger() {
        return logger;
    }

    /*
     * Begin Audit Methods
     */
    protected AcknowledgementType auditRequest(Orchestratable message) {
        AcknowledgementType resp = null;

        if (message != null && message.getAuditTransformer()!= null) {
            AuditTransformer transformer = message.getAuditTransformer();
            LogEventRequestType auditLogMsg = transformer.transformRequest(message);
            resp = audit(auditLogMsg, message.getAssertion());
        }
        return resp;
    }

    protected AcknowledgementType auditResponse(Orchestratable message) {
        AcknowledgementType resp = null;

        if (message != null && message.getAuditTransformer() != null) {
            AuditTransformer transformer = message.getAuditTransformer();
            LogEventRequestType auditLogMsg = transformer.transformResponse(message);
            resp = audit(auditLogMsg, message.getAssertion());
        }
        return resp;
    }

    private AcknowledgementType audit(LogEventRequestType message, AssertionType assertion) {
        getLogger().debug("Entering CONNECTNhinOrchestrator.audit(...)");
        AcknowledgementType ack = null;
        try {
            if (isAuditServiceEnabled()) {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

                ack = proxy.auditLog(message, assertion);
            }
        } catch (Exception exc) {
            getLogger().error("Error: Failed to Audit message.", exc);
        }
        getLogger().debug("Exiting AuditRCONNECTNhinOrchestratorepositoryLogger.audit(...)");
        return ack;
    }

    protected boolean isAuditServiceEnabled() {
        getLogger().debug("Entering CONNECTNhinOrchestrator.isServiceEnabled(...)");
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);
        } catch (PropertyAccessException ex) {
            getLogger().error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            getLogger().error(ex.getMessage(), ex);
        }
        getLogger().debug("Exiting CONNECTNhinOrchestrator.isServiceEnabled(...) with value of: " + serviceEnabled);
        return serviceEnabled;
    }
    /*
     * End Audit Methods
     */

    /*
     * Begin Policy Methods
     */
    protected boolean isPolicyOk(Orchestratable message, PolicyTransformer.Direction direction) {
        getLogger().debug("Entering CONNECTNhinOrchestrator.isPolicyOk(...)");
        boolean policyIsValid = false;

        try {
            PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
            PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();

            PolicyTransformer transformer = message.getPolicyTransformer();
            CheckPolicyRequestType policyReq = transformer.tranform(message, direction);

            if (policyReq != null && message.getAssertion() != null) {
                CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, message.getAssertion());

                if (policyResp.getResponse() != null && policyResp.getResponse().getResult() != null) {
                    // we are expecting only 1 result, if we get more we are only paying attention to the first result
                    for (ResultType r : policyResp.getResponse().getResult()) {
                        if (r.getDecision() == DecisionType.PERMIT) {
                            policyIsValid = true;
                        }
                        break;
                    }
                }
            }
        } catch (Exception exc) {
            getLogger().error("Error: Failed to check policy.", exc);
        }
        getLogger().debug("Exiting CONNECTNhinOrchestrator.isPolicyOk(...) with a value of :" + policyIsValid);
        return policyIsValid;
    }
    /*
     * End Policy Methods
     */
}