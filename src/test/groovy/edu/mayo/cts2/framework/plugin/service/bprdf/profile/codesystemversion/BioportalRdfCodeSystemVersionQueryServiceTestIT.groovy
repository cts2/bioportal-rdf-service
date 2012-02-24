package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult
import static org.junit.Assert.*
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ModelAttributeReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.service.command.restriction.CodeSystemVersionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfCodeSystemVersionQueryServiceTestIT {
	
	@Resource
	BioportalRdfCodeSystemVersionQueryService query
	
	@Resource
	Cts2Marshaller marshaller
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesValidXml(){
		def dir = query.getResourceSummaries(
			[	
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as CodeSystemVersionQuery,null,new Page())
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
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
				
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir1
		assertTrue dir1.getEntries().size() > 0
		
		dir1.entries.each {
			assertTrue it.codeSystemVersionName.toLowerCase().contains("cpt")	
		}
		
		def dir2 = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"cPt",
						modelAttributeReference: new ModelAttributeReference(content:"resourceName")
						)] as Set
				}
				
			] as CodeSystemVersionQuery,null,new Page())
		
		dir1.entries.each {
			assertTrue it.codeSystemVersionName.toLowerCase().contains("cpt")
		}
		
		assertEquals dir1.entries.size(), dir2.entries.size()
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			
			assertNotNull it.href 
			
		}

	}
	
	@Test
	void TestGetResourceSummariesWithCodeSystemRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  CodeSystemVersionQueryServiceRestrictions(codeSystem: ModelUtils.nameOrUriFromName("GO-1070"))},
				getFilterComponent:{[] as Set}			
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		Set nameSet = new HashSet()
		
		dir.entries.each {
			
			def name = it.codeSystemVersionName
			
			if(nameSet.contains(name)){
				fail("duplicates found")
			}
			
			nameSet.add(name)
			
			assertTrue name.startsWith("GO")
		}

	}
	
	@Test
	void TestGetResourceSummariesVersionOf(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			
			assertNotNull it.versionOf.content
			assertNotNull it.versionOf.uri
			assertNotNull it.versionOf.href	
		}
	}
	
	@Test
	void TestGetResourceSummariesHaveNames(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as CodeSystemVersionQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			
			assertNotNull "1", it.codeSystemVersionName
			assertNotNull "2", it.resourceName
			assertEquals "3", it.codeSystemVersionName, it.resourceName
			
		}

	}

}
