package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import static org.junit.Assert.*

import org.apache.commons.lang.StringUtils
import org.junit.Test

import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadServiceByNameTestIT extends BioportalRdfCodeSystemReadServiceTestITBase {

	@Override
	public Object doRead() {
		def cs = read.read(ModelUtils.nameOrUriFromName("CARO-1063"), null)
		
		cs
	}
	
	@Test
	void TestDoRead() {
		def cs = doRead()
		
		assertNotNull cs
		
		cs.property.each {
			println it.predicate.name
		}
	}

	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromName("__INVALID__"), null)
	}

}
