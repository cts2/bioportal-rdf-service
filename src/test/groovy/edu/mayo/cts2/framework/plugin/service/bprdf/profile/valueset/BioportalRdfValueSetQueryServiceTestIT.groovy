package edu.mayo.cts2.framework.plugin.service.bprdf.profile.valueset

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
import edu.mayo.cts2.framework.service.profile.ResourceQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfValueSetQueryServiceTestIT {
	
	@Resource
	Cts2Marshaller marshaller
	
	@Resource
	BioportalRdfValueSetQueryService query
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(
			null as ValueSetQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesValidXml(){
		def dir = query.getResourceSummaries(
			null as ValueSetQuery,null,new Page())
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResourceSummariesHasDescritpion(){
		def dir = query.getResourceSummaries(
			null as ValueSetQuery,null,new Page())
		
		dir.entries.each {
			assertTrue StringUtils.isNotBlank(it.resourceSynopsis.value.content)
		}
	}
	
	
	@Test
	void TestGetResourceSummariesFilteredContains(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"BRO",
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceName"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each { 
			assertTrue it.valueSetName, it.valueSetName.contains("BRO")
		}
	}
	
	@Test
	void TestGetResourceSummariesFilteredContainsDescription(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"Economic",
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceSynopsis"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertTrue it.resourceSynopsis.value.content,
				StringUtils.containsIgnoreCase(
					it.resourceSynopsis.value.content, "Economic")
		}
	}

	@Test
	void TestGetResourceSummariesFilteredContainsNoCase(){
		def dir1 = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"BRO",
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceName"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		
		assertNotNull dir1
		assertTrue dir1.getEntries().size() > 0
		
		def dir2 = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"bRo",
						propertyReference: new PropertyReference(referenceTarget: new URIAndEntityName(name:"resourceName"))
						)] as Set
				}
				
			] as ValueSetQuery,null,new Page())
		
		assertEquals dir1.entries.size(), dir2.entries.size()
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(null as ValueSetQuery,null,new Page())
		
		dir.entries.each {
			
			assertNotNull it.href 
			
		}
	}

}
