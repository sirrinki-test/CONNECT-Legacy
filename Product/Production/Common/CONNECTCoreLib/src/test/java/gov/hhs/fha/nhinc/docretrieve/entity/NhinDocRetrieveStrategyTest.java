/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.EntityOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mweaver
 */
public class NhinDocRetrieveStrategyTest {

    public NhinDocRetrieveStrategyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class NhinDocRetrieveStrategy.
     */
    @Test
    public void testExecute() {
        System.out.println("execute");
        EntityDocRetrieveOrchestratable message = null;
        OrchestrationStrategy instance = new NhinDocRetrieveStrategyImpl();
        instance.execute(message);
    }

    public class NhinDocRetrieveStrategyImpl implements OrchestrationStrategy {

        public void execute(EntityDocRetrieveOrchestratable message) {
        }

		@Override
		public void execute(EntityOrchestratable message) {
			// TODO Auto-generated method stub
			
		}
    }

}