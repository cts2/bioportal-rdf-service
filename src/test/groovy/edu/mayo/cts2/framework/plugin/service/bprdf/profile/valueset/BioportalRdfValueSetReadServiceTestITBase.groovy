package edu.mayo.cts2.framework.plugin.service.bprdf.profile.valueset

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

abstract class BioportalRdfValueSetReadServiceTestITBase {
	
	@Resource
	BioportalRdfValueSetReadService read
	
	abstract doRead()

	@Test
	void TestReadByNameNotNull(){
		def cs = doRead()
		
		assertNotNull cs
	}
	
	@Test
	void TestReadByNameHasDescription(){
		def resource = doRead()
		
		assertNotNull resource.resourceSynopsis.value.content
	}
	
	@Test
	void TestReadByNameHasFormalName(){
		def resource = doRead()
		
		assertNotNull resource.formalName
	}
	
	@Test
	void TestReadByNameHasProperties(){
		def resource = doRead()
		
		assertTrue resource.getProperty().length > 0
	}
	
	
	@Test
	void CheckNamespaces() {
		def cs = doRead()
			
		cs.property.each {
			assertFalse StringUtils.startsWith(it.predicate.namespace, "ns-")
		}
	}
}
