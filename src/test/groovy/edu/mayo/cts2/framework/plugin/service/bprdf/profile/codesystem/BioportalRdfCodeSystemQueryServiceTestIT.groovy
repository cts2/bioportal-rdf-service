package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery

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
	void TestGetResourceSummariesFilteredRegex(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"MA",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each { 
			assertTrue it.codeSystemName, it.codeSystemName.contains("MA")
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
