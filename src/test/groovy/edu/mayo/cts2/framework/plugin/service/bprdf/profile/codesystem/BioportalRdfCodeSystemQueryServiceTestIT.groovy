package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.service.profile.ResourceQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")

class BioportalRdfCodeSystemQueryServiceTestIT {
	
	@Resource
	BioportalRdfCodeSystemQueryService query
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(
			null,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestCurrentVersion(){
		def dir = query.getResourceSummaries(
			null,null,new Page())
		
		assertNotNull dir
		dir.entries.each {
			assertNotNull "1",it.currentVersion
			
			assertNotNull "2",it.currentVersion.codeSystem
			assertNotNull "3",it.currentVersion.codeSystem.content
			assertNotNull "4",it.currentVersion.codeSystem.uri
			assertNotNull "5",it.currentVersion.codeSystem.href
			
			assertNotNull "6",it.currentVersion.version
			assertNotNull "7",it.currentVersion.version.content
			assertNotNull "8",it.currentVersion.version.uri
			assertNotNull "9",it.currentVersion.version.href
		}
	}
	
	@Test
	void TestGetResourceSummariesFilteredRegex(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"CPTAC",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as ResourceQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each { 
			assertTrue it.codeSystemName, it.codeSystemName.contains("CPTAC")
		}
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(null,null,new Page())
		
		dir.entries.each {
			
			assertNotNull it.href 
			
		}
	}

}
