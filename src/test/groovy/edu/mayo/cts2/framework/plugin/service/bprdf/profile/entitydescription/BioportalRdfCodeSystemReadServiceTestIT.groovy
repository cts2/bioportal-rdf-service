package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfCodeSystemReadServiceTestIT {

	@Resource
	Cts2Marshaller marshaller
	
	@Resource
	BioportalRdfEntityDescriptionReadService read
	
	@Test
	@Ignore
	void TestReadByUri() {
	
		//TODO:
	}
	
	@Test
	void TestReadByName() {
	
		def ed = read.read(new EntityDescriptionReadId("http://purl.bioontology.org/ontology/WHO/1313"), null)
	
		println ed.namedEntity.property.size()

		assertNotNull ed
		
	}
	

}
