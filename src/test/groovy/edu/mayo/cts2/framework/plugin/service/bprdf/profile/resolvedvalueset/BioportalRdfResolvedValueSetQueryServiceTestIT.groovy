package edu.mayo.cts2.framework.plugin.service.bprdf.profile.resolvedvalueset

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult
import static org.junit.Assert.*
import org.apache.commons.lang.StringUtils
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfResolvedValueSetQueryServiceTestIT {
	
	@Resource
	Cts2Marshaller marshaller
	
	@Resource
	BioportalRdfResolvedValueSetQueryService query
	
	@Test
	void TestGetResourceSummaries(){

		def dir = query.getResourceSummaries(
			null as ResolvedValueSetQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(null,null,new Page())
		
		dir.entries.each {
			
			assertNotNull it.href
			
			assertTrue it.href, !it.href.contains("null")
			
		}
	}
	
	@Test
	void TestGetResourceSummariesValidXml(){
		def dir = query.getResourceSummaries(
			null as ResolvedValueSetQuery,null,new Page())
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}

}
