package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.util.ModelUtils


class BioportalRdfCodeSystemReadServiceByUriTestIT extends BioportalRdfCodeSystemReadServiceTestITBase{
	
	@Resource
	BioportalRdfCodeSystemReadService read
	
	def doRead() {
		read.read(ModelUtils.nameOrUriFromUri("http://purl.bioontology.org/ontology/MA.rdf"), null)
	}
	
	
	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromUri("http://__INVALID__"), null)
	}

}
