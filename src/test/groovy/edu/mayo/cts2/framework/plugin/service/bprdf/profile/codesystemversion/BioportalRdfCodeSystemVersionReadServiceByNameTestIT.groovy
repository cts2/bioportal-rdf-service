package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils
import org.junit.Test

import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemVersionReadServiceByNameTestIT extends BioportalRdfCodeSystemVersionReadServiceTestITBase {

	@Override
	public Object doRead() {
		read.read(ModelUtils.nameOrUriFromName("MA_1.205"), null)
	}

	@Test
	void TestDoRead() {
		def csv = doRead()
		
		assertNotNull csv
	}
	
	@Test
	void doReadByInvalid() {
		def csv = read.read(ModelUtils.nameOrUriFromName("INVALID_VERSION"), null)
		println csv
	}

}
