package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.association.BioportalRdfAssociationQueryService
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
//@Ignore("This seems to be killing the SPARQL endpoint... need to check with NCBO.")
class BioportalRdfAssociationQueryServiceTestIT {
	
	@Resource
	BioportalRdfAssociationQueryService query
	
	@Resource
	Cts2Marshaller marshaller
	
	@Test
	void testGetResourceSummaries(){
		def dir = query.getResourceSummaries(
		   [
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928") )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,
		null,
		new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
	}
	
	@Test
	void testGetResourceSummariesValid(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-44774"), 
					                                                      sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("18265-9", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void testGetResourceSummariesValidForLNC(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-44774"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("18265-9", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceNameRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-44774"), 
					                                                      sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("LP7119-3", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceNameRestrictionLNC(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-44774"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("MTHU000999", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}

	
	
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceEntityUriRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromUri("http://purl.obolibrary.org/obo/GO_0000001") )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceEntityNameRestrictionCheckNamespaces(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("ICD9CM-47178"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromName(new ScopedEntityName(name:"E008.0")) )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		
		dir.entries.each {
			assertNotNull it.subject.namespace
			assertFalse "ICD9CM-47178".equals( it.subject.namespace )
			assertFalse it.subject.namespace.startsWith("ns")
			
			assertNotNull it.predicate.namespace
			assertFalse "ICD9CM-47178".equals( it.predicate.namespace )
			assertFalse it.predicate.namespace.startsWith("ns")
			
			assertNotNull it.target.entity.namespace
			assertFalse "ICD9CM-47178".equals( it.target.entity.namespace )
			assertFalse it.target.entity.namespace.startsWith("ns")
		}
	}
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceEntityNameInvalidCSV(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("__INVALID__"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromName(new ScopedEntityName(name:"E008.0")) )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNull dir
	}
	
	@Test
	void testGetResourceSummariesWithCodeSystemVersionAndSourceEntityUriRestrictionForLNC(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("LNC-44774"),
																		  sourceEntity: ModelUtils.entityNameOrUriFromName(new ScopedEntityName(name:"LP7119-3")) )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}

	void testGetResourceSummariesWithCodeSystemVersionAndSourceOrTargetEntityNameRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"),
																		  sourceOrTargetEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("GO_0000001", "") ))},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}

	void testGetResourceSummariesWithCodeSystemVersionAndSourceOrTargetEntityUriRestriction(){
		def dir = query.getResourceSummaries(
			[
				getRestrictions:{ new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"),
																		  sourceOrTargetEntity: ModelUtils.entityNameOrUriFromUri("http://purl.obolibrary.org/obo/GO_0000001") )},
				getFilterComponent:{[] as Set}
			] as AssociationQuery,null,new Page())
		
		assertNotNull dir
		assertTrue dir.getEntries().size() > 0
		

	}
	
	
	@Test
	void testGetChildrenAssociationOfEntityValidate(){
		def dir = query.getChildrenAssociationsOfEntity(
			new EntityDescriptionReadId(ModelUtils.createScopedEntityName("GO_0008150","GO"), ModelUtils.nameOrUriFromName("GO-46928"))
				, null, null,new Page())
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void testGetChildrenAssociationOfEntityValidateSNOMED(){
		def dir = query.getChildrenAssociationsOfEntity(
			new EntityDescriptionReadId(ModelUtils.createScopedEntityName("363662004","SNOMEDCT"),
				 ModelUtils.nameOrUriFromName("SNOMEDCT-46896 "))
				, null, null,new Page())
		
		assertNotNull dir
		assertTrue dir.entries.size() > 0
		
		dir.entries.each {
		
			marshaller.marshal(it, new StreamResult(new StringWriter()))
			
		}
	}
	
	
	
	@Test
	void testGetSourceEntitiesValidate(){
		def dir = query.getSourceEntities(
			new  AssociationQueryServiceRestrictions(codeSystemVersion: ModelUtils.nameOrUriFromName("GO-46928"), 
					                                 sourceEntity: ModelUtils.entityNameOrUriFromName(ModelUtils.createScopedEntityName("GO_0000001", "") ))
			, null, null,new Page())
		
		assertNotNull dir.entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}

	
}
