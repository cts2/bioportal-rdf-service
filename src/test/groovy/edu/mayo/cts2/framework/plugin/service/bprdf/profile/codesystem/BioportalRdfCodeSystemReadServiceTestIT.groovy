package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.util.ModelUtils

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class BioportalRdfCodeSystemReadServiceTestIT {
	
	@Resource
	BioportalRdfCodeSystemReadService read
	
	void TestReadByName(){
		def cs = read.read(ModelUtils.nameOrUriFromName("LNC"), null);
		
		assertNotNull cs
	}

}
