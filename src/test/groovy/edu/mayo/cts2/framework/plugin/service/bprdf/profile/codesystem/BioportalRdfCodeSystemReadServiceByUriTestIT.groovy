package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import javax.annotation.Resource;
import static org.junit.Assert.*
import org.junit.Test;

import edu.mayo.cts2.framework.model.util.ModelUtils


class BioportalRdfCodeSystemReadServiceByUriTestIT extends BioportalRdfCodeSystemReadServiceTestITBase{
	
	@Resource
	BioportalRdfCodeSystemReadService read
	
	def doRead() {
		read.read(ModelUtils.nameOrUriFromUri("http://bioportal.bioontology.org/ontologies/1070"), null)
	}
	
	@Test
	void TestDoRead() {
		def cs = doRead()
		
		assertNotNull cs

	}
	
	
	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromUri("http://__INVALID__/9999999"), null)
	}

}
