/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.orchestration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class CONNECTOutboundOrchestrator extends CONNECTOrchestrationBase {

    private static final Log logger = LogFactory.getLog(CONNECTOutboundOrchestrator.class);

    @Override
    protected Log getLogger() {
        return logger;
    }

    @Override
    protected Orchestratable processIfPolicyIsOk(Orchestratable message) {
        return processOutboundIfPolicyIsOk(message);

    }
}