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
	void TestGetResourceSummariesWithCSVNoFilter(){
		def dir = query.getResourceSummaries(
			[
			getEntitiesFromAssociationsQuery:{null},
			getRestrictions:{
				getCodeSystemVersion: {
					ModelUtils.nameOrUriFromName("GO")
				}
			},
			getFilterComponent:{}
			] as EntityDescriptionQuery,
		null,
		new Page(maxToReturn:10))
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
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
}
