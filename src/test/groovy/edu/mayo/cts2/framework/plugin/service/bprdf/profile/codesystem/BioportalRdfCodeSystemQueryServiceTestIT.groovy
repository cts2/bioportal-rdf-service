package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

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
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.service.profile.ResourceQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")

class BioportalRdfCodeSystemQueryServiceTestIT {
	
	@Resource
	Cts2Marshaller marshaller
	
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
	void TestGetResourceSummariesValidXml(){
		def dir = query.getResourceSummaries(
			null,null,new Page())
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResourceSummariesHasDescritpion(){
		def dir = query.getResourceSummaries(
			null,null,new Page())
		
		dir.entries.each {
			assertTrue StringUtils.isNotBlank(it.resourceSynopsis.value.content)
		}
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
	void TestGetResourceSummariesFilteredContains(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"CPT",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as ResourceQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each { 
			assertTrue it.codeSystemName, it.codeSystemName.contains("CPT")
		}
	}
	
	@Test
	void TestGetResourceSummariesFilteredContainsDescription(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"Mouse",
						modelAttributeReference: new ModelAttributeReference(content:"resourceSynopsis")
						)] as Set
				}
				
			] as ResourceQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertTrue it.resourceSynopsis.value.content,
				StringUtils.containsIgnoreCase(
					it.resourceSynopsis.value.content, "Mouse")
		}
	}

	@Test
	void TestGetResourceSummariesFilteredContainsNoCase(){
		def dir1 = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"CPT",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as ResourceQuery,null,new Page())
		
		assertNotNull dir1
		assertTrue dir1.getEntries().size() > 0
		
		def dir2 = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"cPt",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as ResourceQuery,null,new Page())
		
		assertEquals dir1.entries.size(), dir2.entries.size()
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(null,null,new Page())
		
		dir.entries.each {
			
			assertNotNull it.href 
			
		}
	}

}
