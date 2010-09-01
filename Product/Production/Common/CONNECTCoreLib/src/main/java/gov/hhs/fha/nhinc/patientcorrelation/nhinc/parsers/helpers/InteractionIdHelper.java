package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class InteractionIdHelper {


    public static II createInteractionId(String extension) {
        return IIHelper.IIFactory( Constants.HL7_OID, extension);
    }
}
