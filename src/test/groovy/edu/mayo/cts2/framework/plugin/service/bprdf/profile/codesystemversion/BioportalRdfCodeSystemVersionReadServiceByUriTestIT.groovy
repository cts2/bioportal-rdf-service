package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.util.ModelUtils


class BioportalRdfCodeSystemVersionReadServiceByUriTestIT extends BioportalRdfCodeSystemVersionReadServiceTestITBase{
	
	@Resource
	BioportalRdfCodeSystemVersionReadService read
	
	def doRead() {
		read.read(ModelUtils.nameOrUriFromUri("http://purl.bioontology.org/ontology/MA.rdf/version/1.205"), null)
	}
	
	
	@Test
	void doReadByInvalid() {
		def csv = read.read(ModelUtils.nameOrUriFromUri("http://purl.bioontology.org/ontology/MA.rdf/version/__INVALID__"), null)
		assertNull csv
	}

}
