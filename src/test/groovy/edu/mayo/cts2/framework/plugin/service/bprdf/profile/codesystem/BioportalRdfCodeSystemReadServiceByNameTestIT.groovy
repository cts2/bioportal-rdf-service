package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadServiceByNameTestIT extends BioportalRdfCodeSystemReadServiceTestITBase {

	@Override
	public Object doRead() {
		read.read(ModelUtils.nameOrUriFromName("MA"), null)
	}

}
