/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

/**
 *
 * @author mweaver
 */
public interface OutboundOrchestratable extends Orchestratable {
    public OutboundDelegate getDelegate();
    public NhinAggregator getAggregator();
}