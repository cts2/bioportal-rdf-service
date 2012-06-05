package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.core.EntityReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.entity.types.DesignationRole
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfEntityDescriptionReadServiceTestIT {

	@Resource
	Cts2Marshaller marshaller
	
	@Resource
	BioportalRdfEntityDescriptionReadService read
	
	@Test
	void TestReadByUri() {
		def ed = read.read(new EntityDescriptionReadId(
			"http://www.icn.ch/icnp#Hospital",
			ModelUtils.nameOrUriFromName("ICNP-45766")), null)

		assertNotNull ed
	}
	
	@Test
	void TestReadByUriValid() {
		def ed = read.read(new EntityDescriptionReadId(
			"http://www.icn.ch/icnp#Hospital",
			ModelUtils.nameOrUriFromName("ICNP-45766")), null)

		marshaller.marshal(read, new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestReadByName() {
	
		def name = new ScopedEntityName(name:"Hospital")
		def csv = ModelUtils.nameOrUriFromName("ICNP-45766")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
		
	}
	
	@Test
	void TestReadByNameLNC() {
	
		def name = new ScopedEntityName(name:"LP97322-9", namespace:"LNC")
		def csv = ModelUtils.nameOrUriFromName("LNC-44774")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
		
	}
	
	@Test
	void TestReadByNameBAO() {
	
		def name = new ScopedEntityName(name:"IAO_0000030", namespace:"obo")
		def csv = ModelUtils.nameOrUriFromName("BAO-47219")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
		
		ed.choiceValue.property.each {
			assertTrue ! it.predicate.name.equals("label")
		}
	}
	
	@Test
	void TestReadByNameWBPHENOTYPE() {
	
		def name = new ScopedEntityName(name:"WBPhenotype_0000349", namespace:"obo")
		def csv = ModelUtils.nameOrUriFromName("WBPHENOTYPE-47253")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
	}

	@Test
	void TestReadByNameNotDuplicatePresentations() {
	
		def name = new ScopedEntityName(name:"D001914")
		def csv = ModelUtils.nameOrUriFromName("MESH-OWL-47112")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
		
		def pref = 0;
		ed.choiceValue.designation.each {
			if(it.designationRole == DesignationRole.PREFERRED){
				pref++
			}
		}
		
		assertEquals 1, pref
	}
	
	@Test
	void TestReadByNameValid() {
	
		def name = new ScopedEntityName(name:"Hospital")
		def csv = ModelUtils.nameOrUriFromName("ICNP-45766")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertNotNull ed
	
		marshaller.marshal(ed, new StreamResult(new StringWriter()))
		
	}
	
	@Test
	void TestReadByNameSnomedCTValid() {
	
		def name = new ScopedEntityName(name:"167902000")
		def csv = ModelUtils.nameOrUriFromName("SNOMEDCT-44777")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertNotNull ed
	
		marshaller.marshal(ed, new StreamResult(new StringWriter()))
		
	}
	
	@Test
	void TestReadByNameValidDescribingCodeSystemVersion() {
	
		def name = new ScopedEntityName(name:"Hospital")
		def csv = ModelUtils.nameOrUriFromName("ICNP-45766")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertNotNull ed
	
		assertNotNull ed.namedEntity.describingCodeSystemVersion
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.codeSystem
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.version
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.codeSystem.content
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.version.content
		
	}
	
	@Test
	void TestReadByUriValidDescribingCodeSystemVersion() {
	
		def ed = read.read(new EntityDescriptionReadId(
			"http://www.icn.ch/icnp#Hospital",
			ModelUtils.nameOrUriFromName("ICNP-45766")), null)
		
		assertNotNull ed
	
		assertNotNull ed.namedEntity.describingCodeSystemVersion
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.codeSystem
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.version
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.codeSystem.content
		
		assertNotNull ed.namedEntity.describingCodeSystemVersion.version.content
		
	}
	
	@Test
	void TestReadByNameHasParent() {
	
		def name = new ScopedEntityName(name:"Hospital")
		def csv = ModelUtils.nameOrUriFromName("ICNP-45766")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assert 1 <= ed.namedEntity.parent.length
	}
	
	@Test
	void TestReadByNameHasChildrenHref() {
	
		def name = new ScopedEntityName(name:"LP7119-3")
		def csv = ModelUtils.nameOrUriFromName("LNC-44774")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertNotNull ed.choiceValue.children
	}
	
	@Test
	void TestReadByNameHasChildrenHrefCorrect() {
	
		def name = new ScopedEntityName(name:"E008.2")
		def csv = ModelUtils.nameOrUriFromName("ICD9CM-47178")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertFalse StringUtils.endsWith(
			ed.choiceValue.children, "ICD9CM:Class/children")
	}
	
	@Test
	void TestReadByNameHasCorrectNameAndNamespace() {
	
		def name = new ScopedEntityName(name:"E008.2")
		def csv = ModelUtils.nameOrUriFromName("ICD9CM-47178")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertEquals "E008.2", ed.choiceValue.entityID.name
		assertEquals "ICD9CM", ed.choiceValue.entityID.namespace
	}
	
	@Test
	void TestReadByNameHasSubjectOfHref() {
	
		def name = new ScopedEntityName(name:"LP7119-3")
		def csv = ModelUtils.nameOrUriFromName("LNC-44774")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assertNotNull ed.choiceValue.subjectOf
	}
	
	@Test
	void TestReadByNameParentComplete() {
	
		def name = new ScopedEntityName(name:"Hospital")
		def csv = ModelUtils.nameOrUriFromName("ICNP-45766")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
		
		assert 1 <= ed.namedEntity.parent.length
		
		ed.namedEntity.parent.each {
			assertNotNull it.name
			assertNotNull it.namespace
			assertNotNull it.href	
		}
	}
	
	@Test
	void TestAvailableDescriptionsNotFound() {
		def name = new ScopedEntityName(name:"___INVALID___", namespace:"___INVALID___")
		def ed = read.availableDescriptions( new EntityNameOrURI(entityName:name), null)
		
		assertNull ed
	}
	
	@Test
	void TestAvailableDescriptionsHaveNameAndUri() {
		def name = new ScopedEntityName(name:"285487006", namespace:"SNOMEDCT")
		def ed = read.availableDescriptions( new EntityNameOrURI(entityName:name), null)
		
		assertNotNull ed
		
		assertNotNull ed.about
		assertNotNull ed.name.name;
		assertNotNull ed.name.namespace;
	}
	
	@Test
	void TestAvailableDescriptions() {
		def name = new ScopedEntityName(name:"285487006", namespace:"SNOMEDCT")
		def ed = read.availableDescriptions( new EntityNameOrURI(entityName:name), null)
		
		assertNotNull ed
		
		assert ed instanceof EntityReference
	}
	

	@Test
	void TestAvailableDescriptionsHaveDescriptions() {
		def name = new ScopedEntityName(name:"285487006", namespace:"SNOMEDCT")
		def ed = read.availableDescriptions( new EntityNameOrURI(entityName:name), null)
		
		assertEquals 1, ed.knownEntityDescriptionCount
	}
	
	@Test
	void TestAvailableDescriptionsHaveDescriptionsNone() {
		def name = new ScopedEntityName(name:"___INVALID___", namespace:"SNOMEDCT")
		def ed = read.availableDescriptions( new EntityNameOrURI(entityName:name), null)
		
		assertNull ed
	}
	
	
	@Test
	void TestAvailableDescriptionsHaveDescriptionsMoreThanOne() {
		def ed = read.availableDescriptions( 
			new EntityNameOrURI(uri:"http://www.ifomis.org/bfo/1.1/span#Occurrent"), null)
		
		assertTrue ed.knownEntityDescriptionCount > 1
	}
	
	@Test
	void TestAvailableDescriptionsHaveDescriptionsMoreThanOneAllDifferent() {
		def ed = read.availableDescriptions(
			new EntityNameOrURI(uri:"http://www.ifomis.org/bfo/1.1/span#Occurrent"), null)
		
		def set = new HashSet()
		ed.knownEntityDescription.each {
			def name = it.describingCodeSystemVersion.codeSystem.content
			if(set.contains(name)){
				fail(name)
			} else {
				set.add(name)
			}
		}
	}
	
	@Test
	void TestAvailableDescriptionsHaveDesignations() {
		def ed = read.availableDescriptions(
			new EntityNameOrURI(uri:"http://www.ifomis.org/bfo/1.1/span#Occurrent"), null)

		ed.knownEntityDescription.each {
			assertNotNull it.designation
		}
	}
	
	@Test
	void TestAvailableDescriptionsHaveHrefs() {
		def ed = read.availableDescriptions(
			new EntityNameOrURI(uri:"http://www.ifomis.org/bfo/1.1/span#Occurrent"), null)

		ed.knownEntityDescription.each {
			assertNotNull it.href
		}
	}
	
}
