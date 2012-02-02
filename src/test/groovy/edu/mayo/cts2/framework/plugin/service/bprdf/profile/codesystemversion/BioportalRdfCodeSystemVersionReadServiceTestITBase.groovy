package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion

import static org.junit.Assert.*

import javax.annotation.Resource

import org.apache.commons.lang.StringUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")

abstract class BioportalRdfCodeSystemVersionReadServiceTestITBase {
	
	@Resource
	BioportalRdfCodeSystemVersionReadService read
	
	abstract doRead()

	@Test
	void TestReadByNameNotNull(){
		def cs = doRead()
		
		assertNotNull cs
	}
	
	@Test
	void TestReadByNameHasDescription(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertNotNull cs.resourceSynopsis.value.content
	}
	
	@Test
	void TestReadByNameHasFormalName(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertNotNull cs.formalName
	}
	
	@Test
	void TestReadByNameHasDocumentUri(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertNotNull cs.documentURI
	}
	
	@Test
	void TestReadByNameHasProperties(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertTrue cs.getProperty().length > 0
	}
	
	@Test
	void TestReadByNameHasEntitiesUrl(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertNotNull cs.entityDescriptions
	}
	
	@Test
	void TestReadByNameHasName(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertEquals "FIX-45720", cs.codeSystemVersionName
	}
	
	@Test
	void TestReadByNameHasVersion(){
		CodeSystemVersionCatalogEntry cs = doRead()
		
		assertEquals "See Remote Site", cs.officialResourceVersionId
	}
	
	@Test
	void CheckNamespaces() {
		def cs = doRead()
			
		cs.property.each {
			assertFalse StringUtils.startsWith(it.predicate.namespace, "ns-")
		}
	}
}
