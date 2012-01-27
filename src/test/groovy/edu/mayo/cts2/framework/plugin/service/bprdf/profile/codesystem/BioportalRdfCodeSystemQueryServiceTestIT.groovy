package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.command.Page

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfCodeSystemQueryServiceTestIT {
	
	@Resource
	BioportalRdfCodeSystemQueryService query
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(null,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(null,null,new Page())
		
		dir.entries.each {
			
			assertNotNull it.href 
			
		}

	}

}
