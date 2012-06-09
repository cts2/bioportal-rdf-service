package edu.mayo.cts2.framework.plugin.service.bprdf.profile.valueset

import javax.annotation.Resource;
import static org.junit.Assert.*
import org.junit.Test;

import edu.mayo.cts2.framework.model.util.ModelUtils


class BioportalRdfValueSetReadServiceByUriTestIT extends BioportalRdfValueSetReadServiceTestITBase{
	
	@Resource
	BioportalRdfValueSetReadService read
	
	def doRead() {
		read.read(ModelUtils.nameOrUriFromUri("http://bioportal.bioontology.org/ontologies/BRO-ACTIVITY"), null)
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
