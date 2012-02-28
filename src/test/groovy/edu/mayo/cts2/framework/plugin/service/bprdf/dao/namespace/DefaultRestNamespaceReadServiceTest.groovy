package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class DefaultRestNamespaceReadServiceTest {

	@Resource
	private Cts2Marshaller cts2Marshaller;
	
	@Test
	void testGetByUri(){
		def svc = new DefaultRestNamespaceReadService(cts2Marshaller)
		
		assertNotNull svc.readPreferredByUri("http://protege.stanford.edu/plugins/owl/protege#")
	}
}
