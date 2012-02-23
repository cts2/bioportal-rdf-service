package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.association.BioportalRdfAssociationQueryService
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfAssociationQueryServiceTestIT {
	
	@Resource
	BioportalRdfAssociationQueryService query
	
	@Resource
	Cts2Marshaller marshaller
	
	@Test
	void TestGetResourceSummaries(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{null},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void TestGetResourceSummariesWithCodeSystemVersionRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"), 
					                                                      sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("GO_0000001", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}
	
	@Test
	void TestGetResourceSummariesWithCodeSystemVersionAndSourceEntityUriRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromUri("http://purl.obolibrary.org/obo/GO_0000001") )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}
}
