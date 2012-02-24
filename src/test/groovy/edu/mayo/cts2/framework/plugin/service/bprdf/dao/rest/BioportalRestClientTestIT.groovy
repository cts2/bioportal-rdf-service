package edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest;

import javax.annotation.Resource;
import static org.junit.Assert.*
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRestClientTestIT {

	@Resource
	BioportalRestClient client
	
	@Test
	void testNotNull(){
		assertNotNull client
	}
	
	@Test
	void TestSearchEntities(){
		def filter = new ResolvedFilter(
			matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			modelAttributeReference: StandardModelAttributeReference.RESOURCE_SYNOPSIS.modelAttributeReference,
			matchValue: "software"
		)
		def result = client.searchEntities("1104", [filter] as Set, new Page())
		
		assertNotNull result
		
	}
}
