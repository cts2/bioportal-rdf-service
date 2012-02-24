package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult

import static org.junit.Assert.*
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.core.ScopedEntityName
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
		def ed = read.read(new EntityDescriptionReadId("http://purl.bioontology.org/ontology/WHO/1313"), null)

		assertNotNull ed
	}
	
	@Test
	void TestReadByUriValid() {
		def ed = read.read(new EntityDescriptionReadId("http://purl.bioontology.org/ontology/WHO/1313"), null)

		marshaller.marshal(read, new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestReadByName() {
	
		def name = new ScopedEntityName(name:"D001914")
		def csv = ModelUtils.nameOrUriFromName("MSHFRE-42580")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		assertNotNull ed
		
	}
	
	@Test
	void TestReadByNameValid() {
	
		def name = new ScopedEntityName(name:"D001914")
		def csv = ModelUtils.nameOrUriFromName("MSHFRE-42580")
		def ed = read.read(new EntityDescriptionReadId(name, csv), null)
	
		marshaller.marshal(read, new StreamResult(new StringWriter()))
		
	}
	

}
