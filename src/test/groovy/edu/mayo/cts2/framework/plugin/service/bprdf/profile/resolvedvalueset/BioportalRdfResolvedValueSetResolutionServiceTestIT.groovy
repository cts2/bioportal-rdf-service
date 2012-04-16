package edu.mayo.cts2.framework.plugin.service.bprdf.profile.resolvedvalueset

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
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfResolvedValueSetResolutionServiceTestIT {
	
	@Resource
	Cts2Marshaller marshaller
	
	@Resource
	BioportalRdfResolvedValueSetResolutionService resolution
	
	@Test
	void TestGetResolution(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),null),null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResolutionWrongValueSetName(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO-WRONG"),null),null,new Page())
		
		assertNull dir

	}
	
	
	@Test
	void TestGetResolutionHasHrefs(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),null),null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			
			assertNotNull it.href
			assertFalse it.href.contains('null')
			
		}
	}
	
	@Test
	void TestGetResolutionWithFilter(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),null),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResolutionWithFilterHasHrefs(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),null),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

		dir.entries.each {
			
			assertNotNull it.href
			assertFalse it.href.contains('null')
			
		}
	}
	
	@Test
	void TestGetResolutionValidXML(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),null),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		marshaller.marshal(dir, new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestGetResolvedValueSetHeader(){
		def header = resolution.getResolvedValueSetHeader("41011")
		
		assertNotNull header
	}
	
}
