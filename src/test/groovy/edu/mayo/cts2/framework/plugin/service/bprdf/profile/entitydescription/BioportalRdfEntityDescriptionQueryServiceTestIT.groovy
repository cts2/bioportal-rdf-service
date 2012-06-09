package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription

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
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")

class BioportalRdfEntityDescriptionQueryServiceTestIT {
	
	@Resource
	Cts2Marshaller marshaller

	@Resource
	BioportalRdfEntityDescriptionQueryService query
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
				}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertTrue it.knownEntityDescription[0].designation, it.knownEntityDescription[0].designation.toLowerCase().contains("software")
		}
	}
	
	@Test
	void TestGetResourceSummariesWithFilterNoCSV(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0	
	}
	
	@Test
	void TestGetResourceSummariesNoFilterNoCSV(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
			getFilterComponent:{null}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesNoFilterNoCSVIsValid(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
			getFilterComponent:{null}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResourceSummariesWithCSVNoFilter(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
	}
	
	@Test
	void TestGetResourceSummariesWithCSVNoFilterLowerCase(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("OBOE-SBC-44257")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
	}
	
	@Test
	void TestGetResourceSummariesWithCSV_ONTOLOGIA(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("ONTOLOGIA-47267")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
	}
	
	@Test
	void TestGetResourceSummariesWithCSVNoFilterInvalidCSV(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("__INVALID__")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNull dir
	}
	
	@Test
	void TestGetResourceSummariesWithCSVNoFilterAndNoLabels(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("POL-40653")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
	}
	
	@Test
	void TestGetResourceSummariesWithCSVNoFilterAndNoLabelsValidXML(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("POL-40653")
				)
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResourceSummariesWithCSVAndFilter(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"role",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue  dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesWithCSVAndFilterPagingUnder(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"role",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertEquals 10, dir.getEntries().size()
		assertFalse dir.atEnd
	}
	
	@Test
	void TestGetResourceSummariesWithCSVAndFilterPagingNoResultsAtEnd(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"INVALIDQUERYSTRING",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:100))
		
		assertNotNull dir
		assertEquals 0, dir.getEntries().size()
		assertTrue dir.atEnd
	}
	
	@Test
	void TestGetResourceSummariesWithCSVAndFilterPagingAtEnd(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"boxing",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:100))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 1
		assertTrue dir.getEntries().size() < 100
		assertTrue dir.atEnd
	}

	
	@Test
	void TestGetResourceSummariesWithCSVAndFilterForNonCurrentCSV(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-40400")
				)
			},
			getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"disease",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
					}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue  dir.getEntries().size() == 0
		
	}

	@Test
	void TestGetResourceSummariesHaveNameAndNamespace(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
				}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertNotNull it.name
			assertNotNull it.name.name
			assertNotNull it.name.namespace
		}
	}
	
	
	@Test
	void TestGetResourceSummariesValid(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
				}
			] as EntityDescriptionQuery,
		null,
		new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestGetResourceSummariesHaveHrefs(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{null},
				getFilterComponent:{
					[new ResolvedFilter(
						matchValue:"software",
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference,
						)] as Set
				}
			] as EntityDescriptionQuery,
		null,
		new Page())
		
		assertNotNull dir.entries.each {
			assertNotNull it.href
		}
	}
	
	@Test
	void TestGetResourceSummariesOfCodeSystemVersionNoFilterHaveHrefs(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getFilterComponent:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion:ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			}
			
			] as EntityDescriptionQuery,
		null,
		new Page())
		
		assertNotNull dir.entries.each {
			assertNotNull it.href
			
			assertTrue it.knownEntityDescription.size() > 0
			
			it.knownEntityDescription.each {inner->
				assertNotNull inner.href
			}
		}
	}
	
	@Test
	void TestGetResourceSummariesOfCodeSystemVersionNoFilterHaveDescribingCodeSystemVersion(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getFilterComponent:{null},
			getRestrictions:{
				new EntityDescriptionQueryServiceRestrictions(
					codeSystemVersion:ModelUtils.nameOrUriFromName("SNOMEDCT-46896")
				)
			}
			
			] as EntityDescriptionQuery,
		null,
		new Page())
		
		assertNotNull dir.entries.each {
			
			assertTrue it.knownEntityDescription.size() > 0
			
			it.knownEntityDescription.each {inner->
				assertNotNull inner.describingCodeSystemVersion
			}
		}
	}
}
