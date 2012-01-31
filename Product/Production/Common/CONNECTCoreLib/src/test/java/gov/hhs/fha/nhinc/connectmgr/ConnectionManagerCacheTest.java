package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.common.nhinccommon.EPRType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.uddi.api_v3.BusinessEntity;
import org.xmlsoap.schemas.ws._2004._08.addressing.AttributedURI;
import org.xmlsoap.schemas.ws._2004._08.addressing.EndpointReferenceType;
import static org.junit.Assert.*;

/**
 *
 * @author Arthur Kong
 */
public class ConnectionManagerCacheTest {

    private static String HCID_1 = "1.1";
    private static String HCID_2 = "2.2";
    private static String QUERY_FOR_DOCUMENTS_NAME = "QueryForDocuments";
    private static String RETRIEVE_DOCUMENTS_NAME = "RetrieveDocuments";
    private static String NHIN_TARGET_ENDPOINT_URL_VALUE = "http://localhost:8080/";
    private static String QUERY_FOR_DOCUMENTS_URL = "https://localhost:8181/QueryForDocuments";
    private static String QUERY_FOR_DOCUMENTS_DEFERRED_NAME = "QueryForDocumentsDeferredRequest";
    private static String QUERY_FOR_DOCUMENTS_DEFERRED_URL = "https://localhost:8181/QueryForDocumentsDeferredRequest";
    private static String QUERY_FOR_DOCUMENTS_URL_22 = "https://server2:8181/QueryForDocuments";
    private static String QUERY_FOR_DOCUMENTS_DEFERRED_URL_22 = "https://server2:8181/QueryForDocumentsDeferredRequest";


    private static String FL_REGION_VALUE = "US-FL";

    private UddiConnectionInfoDAOFileImpl createUddiConnectionInfoDAO(String filename) {
        URL url = this.getClass().getResource(filename);
        File uddiConnectionFile = new File(url.getFile());
        UddiConnectionInfoDAOFileImpl uddiDAO = UddiConnectionInfoDAOFileImpl.getInstance();
        uddiDAO.setFileName(uddiConnectionFile.getAbsolutePath());

        return uddiDAO;
    }

    private InternalConnectionInfoDAOFileImpl createInternalConnectionInfoDAO(String filename) {
        URL url = this.getClass().getResource(filename);
        File internalConnectionFile = new File(url.getFile());
        InternalConnectionInfoDAOFileImpl internalDAO = InternalConnectionInfoDAOFileImpl.getInstance();
        internalDAO.setFileName(internalConnectionFile.getAbsolutePath());

        return internalDAO;
    }

    protected ConnectionManagerCache createConnectionManager_Empty() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/emptyBusinessDetailConnectionInfo.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO("/config/ConnectionManagerCacheTest/emptyBusinessDetailConnectionInfo.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager_EmptyBusinessEntity() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/emptyBusinessEntityConnectionInfo.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO("/config/ConnectionManagerCacheTest/emptyBusinessEntityConnectionInfo.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/uddiConnectionInfoTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO("/config/ConnectionManagerCacheTest/internalConnectionInfoTest.xml");
            }
        };
    }

    protected ConnectionManagerCache createConnectionManager_Override() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/uddiConnectionInfoMergeTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO("/config/ConnectionManagerCacheTest/internalConnectionInfoMergeTest.xml");
            }
        };
    }

    @Test
    public void testGetInstance() {
        ConnectionManagerCache connectionManager = ConnectionManagerCache.getInstance();
        assertNotNull(connectionManager);

        ConnectionManagerCache connectionManager2 = ConnectionManagerCache.getInstance();
        assertEquals(connectionManager, connectionManager2);

        UddiConnectionInfoDAOFileImpl uddiDAO = connectionManager.getUddiConnectionManagerDAO();
        assertNotNull(uddiDAO);

        InternalConnectionInfoDAOFileImpl internalDAO = connectionManager.getInternalConnectionManagerDAO();
        assertNotNull(internalDAO);
    }

    @Test
    public void testGetAllCommunities_EmptyBusinessDetail() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_Empty();           
            assertTrue(connectionManager.getAllCommunities().isEmpty());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessDetail test: " + t.getMessage());
        }
    }


    @Test
    public void testGetAllCommunities_EmptyBusinessEntity() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_EmptyBusinessEntity();
            assertTrue(connectionManager.getAllCommunities().isEmpty());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities_EmptyBusinessEntity test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllCommunities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            assertEquals(2, connectionManager.getAllCommunities().size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testSetCommunityId() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();
            BusinessEntity businessEntity = connectionManager.getBusinessEntity(HCID_1);

            String HCID_3 = "3.3";
            connectionManager.setCommunityId(businessEntity, HCID_3);
            String newHCID = connectionManager.getCommunityId(businessEntity);
            assertTrue(newHCID.equals(HCID_3));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testSetCommunityId test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAllBusinessEntities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();           
            List<BusinessEntity> entities = connectionManager.getAllBusinessEntities();
            assertEquals(2, entities.size());

            connectionManager = createConnectionManager_Override();
            entities = connectionManager.getAllBusinessEntities();
            assertEquals(3, entities.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAllBusinessEntities test: " + t.getMessage());
        }
    }

    @Test
    public void testMerging() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager_Override();

            String url = connectionManager.getEndpointURLByServiceName(HCID_2, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL_22, url);

            url = connectionManager.getEndpointURLByServiceName(HCID_2, "QueryForDocument");
            assertEquals(QUERY_FOR_DOCUMENTS_URL_22, url);

            url = connectionManager.getEndpointURLByServiceName(HCID_1, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(QUERY_FOR_DOCUMENTS_URL, url);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testMerging test: " + t.getMessage());
        }
    }

    @Test
    public void testGetBusinessEntitySet() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<String> hcidList = new ArrayList<String>();
            hcidList.add(HCID_1);
            hcidList.add(HCID_2);
            Set<BusinessEntity> entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertEquals(2, entitySet.size());

            entitySet = connectionManager.getBusinessEntitySet(null);
            assertNull(entitySet);

            hcidList = new ArrayList<String>();
            hcidList.add("hcidValue1123");
            hcidList.add("hcidValue2123");
            entitySet = connectionManager.getBusinessEntitySet(hcidList);
            assertNull(entitySet);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetBusinessEntitySet test: " + t.getMessage());
        }
    }

    @Test
    public void testGetEndpointURLByServiceName() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getEndpointURLByServiceName(HCID_1, QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_URL));

            url = connectionManager.getEndpointURLByServiceName("hcidValue123", QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(""));

            url = connectionManager.getEndpointURLByServiceName(HCID_2, QUERY_FOR_DOCUMENTS_DEFERRED_NAME);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_DEFERRED_URL_22));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpointURLByServiceName test: " + t.getMessage());
        }
    }

    @Test
    public void testGetLocalEndpointURLByServiceName() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getLocalEndpointURLByServiceName(QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_URL));

            url = connectionManager.getLocalEndpointURLByServiceName("serviceNameValue123");
            assertTrue(url.equals(""));

            url = connectionManager.getLocalEndpointURLByServiceName(RETRIEVE_DOCUMENTS_NAME);
            assertTrue(url.equals(""));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpointURLByServiceName test: " + t.getMessage());
        }
    }

    protected NhinTargetSystemType createNhinTargetSystem() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        EPRType eprType = new EPRType();
        EndpointReferenceType endpointReference = new EndpointReferenceType();
        AttributedURI address = new AttributedURI();
        address.setValue(NHIN_TARGET_ENDPOINT_URL_VALUE);
        endpointReference.setAddress(address);
        eprType.setEndpointReference(endpointReference);
        targetSystem.setEpr(eprType);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_UrlOnly() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setUrl(NHIN_TARGET_ENDPOINT_URL_VALUE);

        return targetSystem;
    }

    protected NhinTargetSystemType createNhinTargetSystem_HCIDOnly() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetSystem.setHomeCommunity(homeCommunity);

        return targetSystem;
    }

    @Test
    public void testGetEndpontURLFromNhinTarget() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(NHIN_TARGET_ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem_UrlOnly(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(NHIN_TARGET_ENDPOINT_URL_VALUE));

            url = connectionManager.getEndpontURLFromNhinTarget(createNhinTargetSystem_HCIDOnly(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_URL));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpontURLFromNhinTarget test: " + t.getMessage());
        }
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunites() {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(FL_REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);

        return targetCommunities;
    }

    @Test
    public void testGetEndpontURLFromNhinTargetCommunities() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            List<UrlInfo> endpointUrlList = connectionManager.getEndpontURLFromNhinTargetCommunities(createNhinTargetCommunites(), QUERY_FOR_DOCUMENTS_NAME);
            assertTrue(endpointUrlList.get(0).getUrl().equals(QUERY_FOR_DOCUMENTS_URL));

            endpointUrlList = connectionManager.getEndpontURLFromNhinTargetCommunities(null, QUERY_FOR_DOCUMENTS_NAME);
            assertEquals(2, endpointUrlList.size());
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetEndpontURLFromNhinTargetCommunities test: " + t.getMessage());
        }
    }

    @Test
    public void testGetAdapterEndpontURL() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            String url = connectionManager.getAdapterEndpontURL(QUERY_FOR_DOCUMENTS_NAME, ADAPTER_API_LEVEL.LEVEL_a0);
            assertNull(url);

            url = connectionManager.getAdapterEndpontURL(QUERY_FOR_DOCUMENTS_DEFERRED_NAME, ADAPTER_API_LEVEL.LEVEL_a0);
            assertTrue(url.equals(QUERY_FOR_DOCUMENTS_DEFERRED_URL));
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            fail("Error running testGetAdapterEndpontURL test: " + t.getMessage());
        }

    }

}