package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion

import javax.annotation.Resource;
import static org.junit.Assert.*
import org.junit.Test;

import edu.mayo.cts2.framework.model.util.ModelUtils


class BioportalRdfCodeSystemVersionReadServiceByUriTestIT extends BioportalRdfCodeSystemVersionReadServiceTestITBase{
	
	@Resource
	BioportalRdfCodeSystemVersionReadService read
	
	def doRead() {
		read.read(ModelUtils.nameOrUriFromUri("http://bioportal.bioontology.org/ontologies/LNC/version/44256"), null)
	}
	
	@Test
	void TestDoRead() {
		def csv = doRead()
		
		assertNotNull csv
	}
	
	@Test
	void doReadByInvalid() {
		def csv = read.read(ModelUtils.nameOrUriFromUri("http://bioportal.bioontology.org/ontologies/4572023423423"), null)
		assertNull csv
	}

}
