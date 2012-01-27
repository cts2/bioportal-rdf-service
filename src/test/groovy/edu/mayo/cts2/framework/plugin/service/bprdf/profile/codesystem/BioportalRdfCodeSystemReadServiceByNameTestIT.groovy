package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadServiceByNameTestIT extends BioportalRdfCodeSystemReadServiceTestITBase {

	@Override
	public Object doRead() {
		read.read(ModelUtils.nameOrUriFromName("MA"), null)
	}

	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromName("__INVALID__"), null)
	}
}
