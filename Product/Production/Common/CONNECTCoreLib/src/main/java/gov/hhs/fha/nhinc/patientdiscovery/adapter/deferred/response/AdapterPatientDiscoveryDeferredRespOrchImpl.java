/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public class AdapterPatientDiscoveryDeferredRespOrchImpl {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion) {
        MCCIIN000002UV01 ack = HL7AckTransforms.createAckFrom201306(request, "Success");

        return ack;
    }

}
