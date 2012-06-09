package edu.mayo.cts2.framework.plugin.service.bprdf.profile.resolvedvalueset

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils
import org.junit.Ignore
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
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery

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
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResolutionWrongValueSetName(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO-WRONG"),ModelUtils.nameOrUriFromName("BRO-41011")),null,new Page())
		
		assertNull dir

	}
	
	
	@Test
	@Ignore("Known Bioportal bug -- planned fix JUN 2012")
	void TestGetResolutionICECINSTRUMENT_OBJECT_SUBSTANCE(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("43008",ModelUtils.nameOrUriFromName("ICECI-INSTRUMENT_OBJECT_SUBSTANCE"),ModelUtils.nameOrUriFromName("ICECI-INSTRUMENT_OBJECT_SUBSTANCE-43008")),null,new Page())
		
		assertNull dir
		
		assertTrue dir.getEntries().size() > 1
	}
	
	@Test
	void TestGetResolutionSNMD_BDY(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("48006",ModelUtils.nameOrUriFromName("SNMD_BDY"),ModelUtils.nameOrUriFromName("SNMD_BDY-48006")),null,new Page())
		
		assertNotNull dir
		
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertTrue StringUtils.endsWith(it.href, it.name)
		}
	}
	
	@Test
	void TestGetResolutionSnomedEthnicGroup(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("43057",ModelUtils.nameOrUriFromName("SNOMED-ETHNIC-GROUP"),
				ModelUtils.nameOrUriFromName("SNOMED-ETHNIC-GROUP-43057")),null,new Page())
		
		assertNotNull dir
		
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	
	@Test
	void TestGetResolutionSnomedEthnicGroupWithFilter(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("43057",ModelUtils.nameOrUriFromName("SNOMED-ETHNIC-GROUP"),
				ModelUtils.nameOrUriFromName("SNOMED-ETHNIC-GROUP-43057")),
				[new ResolvedFilter(
					matchValue:"north",
					matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
				)] as Set
			,new Page())
		
		assertNotNull dir
		
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertTrue ! it.name.contains("http")
		}
	}
	
	@Test
	void TestGetResolutionNoFilterAreFromCorrectCSVSnomedCT(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("43049",ModelUtils.nameOrUriFromName("SNOMEDCT-TF"),ModelUtils.nameOrUriFromName("SNOMEDCT-TF-43049")),null,new Page())
		
		assertNotNull dir
		assertTrue dir.entries.size() > 0
		
				assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			
			assertNotNull it.href
			assertTrue it.href.contains('SNOMEDCT')
			
		}

	}

	@Test
	void TestGetResolutionHasHrefs(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),null,new Page())
		
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
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResolutionWithNoFilter(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[] as Set
			,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 4

	}
	
	@Test
	void TestGetResolutionWithNoFilterHasEntities(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41016",ModelUtils.nameOrUriFromName("NIF-RTH"),ModelUtils.nameOrUriFromName("NIF-RTH-41016")),
			[] as Set
			,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 4

	}
	
	@Test
	void TestGetResolutionWithNoFilterLowerCase(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("43061",ModelUtils.nameOrUriFromName("HOM-UCSF_UCAREDISPOSTION"),ModelUtils.nameOrUriFromName("HOM-UCSF_UCAREDISPOSTION-43061")),
			[] as Set
			,new Page())
		
		assertNotNull dir
		assertEquals 1, dir.getEntries().size()

	}
	
	@Test
	void TestGetResolutionWithFilterHasHrefs(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
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
	void TestGetResolutionWithFilterHasCorrectNamespaces(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		assertNotNull dir
		println dir.getEntries().size()
		assertTrue dir.getEntries().size() > 0

		dir.entries.each {
			
			assertNotNull it.namespace
			assertFalse it.namespace.equals("ns")
			assertFalse StringUtils.startsWith(it.namespace, "ns")
			
		}
	}
	
	@Test
	void TestGetResolutionWithFilterIsCompleteFalse(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		assertNotNull dir
		
		assertTrue dir.atEnd
		assertEquals 10, dir.entries.size

	}
	
	@Test
	void TestGetResolutionWithFilterIsCompleteTrueEqualsSize(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page(maxToReturn:10))
		
		assertNotNull dir
		
		assertTrue dir.atEnd
		assertEquals 10, dir.entries.size

	}
	
	@Test
	void TestGetResolutionWithFilterIsCompleteFalseSize(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page(maxToReturn:5))
		
		assertNotNull dir
		
		assertFalse dir.atEnd
		assertEquals 5, dir.entries.size

	}
	
	@Test
	void TestGetResolutionWithFilterIsCompleteTrueInvalidSearch(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"THISISNOTGOINGTOMATCHANYTHING",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page(maxToReturn:50))
		
		assertNotNull dir
		
		assertTrue dir.atEnd
		assertEquals 0, dir.entries.size

	}
	
	@Test
	void TestGetResolutionValidXML(){

		def dir = resolution.getResolution(
			new ResolvedValueSetReadId("41011",ModelUtils.nameOrUriFromName("BRO"),ModelUtils.nameOrUriFromName("BRO-41011")),
			[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
			)] as Set
			,new Page())
		
		dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResolvedValueSetHeader(){
		def header = resolution.getResolvedValueSetHeader("41011")
		
		assertNotNull header
	}
	
	@Test
	void TestGetResolutionEntitiesNoFilter(){

		def dir = resolution.getEntities(
			new ResolvedValueSetReadId("43099",ModelUtils.nameOrUriFromName("NDF-RT"),ModelUtils.nameOrUriFromName("NDF-RT-43099")),null,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0

	}
	
	@Test
	void TestGetResolutionEntitiesNoFilterAreFromCorrectCSV(){

		def dir = resolution.getEntities(
			new ResolvedValueSetReadId("43099",ModelUtils.nameOrUriFromName("NDF-RT"),ModelUtils.nameOrUriFromName("NDF-RT-43099")),null,null,new Page())
		
		assertNotNull dir
		assertTrue dir.entries.size() > 0
		
		dir.entries.each {
			assertNotNull dir.entries.each {
			
			assertTrue it.knownEntityDescription.size() > 0
			
			it.knownEntityDescription.each {inner->
				assertNotNull inner.describingCodeSystemVersion
				assertNotNull inner.describingCodeSystemVersion.version
				assertNotNull inner.describingCodeSystemVersion.codeSystem
				
				assertEquals "NDFRT", inner.describingCodeSystemVersion.codeSystem.content
				assertEquals "NDFRT-40402", inner.describingCodeSystemVersion.version.content
				
				}
			}
		}

	}
	
	@Test
	void TestGetResolutionEntitiesNoFilterAreFromCorrectCSVSnomedCT(){

		def dir = resolution.getEntities(
			new ResolvedValueSetReadId("43049",ModelUtils.nameOrUriFromName("SNOMEDCT-TF"),ModelUtils.nameOrUriFromName("SNOMEDCT-TF-43049")),null,null,new Page())
		
		assertNotNull dir
		assertTrue dir.entries.size() > 0
		
		dir.entries.each {
			assertNotNull dir.entries.each {
			
			assertTrue it.knownEntityDescription.size() > 0
			
			it.knownEntityDescription.each {inner->
				assertNotNull inner.describingCodeSystemVersion
				assertNotNull inner.describingCodeSystemVersion.version
				assertNotNull inner.describingCodeSystemVersion.codeSystem
				
				assertEquals "SNOMEDCT", inner.describingCodeSystemVersion.codeSystem.content
				assertEquals "SNOMEDCT-40403", inner.describingCodeSystemVersion.version.content
				
				}
			}
		}

	}
	
	@Test
	void TestGetResolutionEntitiesWithFilterAreFromCorrectCSVSnomedCT(){

		def dir = resolution.getEntities(
			new ResolvedValueSetReadId("43049",ModelUtils.nameOrUriFromName("SNOMEDCT-TF"),
				ModelUtils.nameOrUriFromName("SNOMEDCT-TF-43049")),
			[
				getResolvedValueSetResolutionEntityRestrictions:{null},
				getFilterComponent:{[new ResolvedFilter(	
						matchValue:"test",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
					)] as Set}
			] as ResolvedValueSetResolutionEntityQuery,
			null,
			new Page())
		
		assertNotNull dir
		assertTrue dir.entries.size() > 0
		
		dir.entries.each {
			assertNotNull dir.entries.each {
			
			assertTrue it.knownEntityDescription.size() > 0
			
			it.knownEntityDescription.each {inner->
				assertNotNull inner.describingCodeSystemVersion
				assertNotNull inner.describingCodeSystemVersion.version
				assertNotNull inner.describingCodeSystemVersion.codeSystem
				
				assertEquals "SNOMEDCT", inner.describingCodeSystemVersion.codeSystem.content
				assertEquals "SNOMEDCT-40403", inner.describingCodeSystemVersion.version.content
				
				}
			}
		}

	}
}
