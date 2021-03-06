package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import javax.annotation.Resource;
import static org.junit.Assert.*
import org.apache.commons.lang.StringUtils
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")

abstract class BioportalRdfCodeSystemReadServiceTestITBase {
	
	@Resource
	BioportalRdfCodeSystemReadService read
	
	abstract doRead()

	@Test
	void TestReadByNameNotNull(){
		def cs = doRead()
		
		assertNotNull cs
	}
	
	@Test
	void TestReadByNameHasDescription(){
		CodeSystemCatalogEntry cs = doRead()
		
		assertNotNull cs.resourceSynopsis.value.content
	}
	
	@Test
	void TestReadByNameHasFormalName(){
		CodeSystemCatalogEntry cs = doRead()
		
		assertNotNull cs.formalName
	}
	
	@Test
	void TestReadByNameHasProperties(){
		CodeSystemCatalogEntry cs = doRead()
		
		assertTrue cs.getProperty().length > 0
	}
	
	@Test
	void TestReadByNameHasVersionsUrl(){
		CodeSystemCatalogEntry cs = doRead()
		
		assertNotNull cs.versions
	}
	
	@Test
	void TestReadByNameHasName(){
		CodeSystemCatalogEntry cs = doRead()
		
		assertEquals "LNC", cs.codeSystemName
	}
	
	@Test
	void CheckNamespaces() {
		def cs = doRead()
			
		cs.property.each {
			assertFalse StringUtils.startsWith(it.predicate.namespace, "ns-")
		}
	}
}
