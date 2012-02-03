package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry
import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadVersionServiceByVersionIdTestIT extends BioportalRdfCodeSystemVersionReadServiceTestITBase {

	@Resource
	Cts2Marshaller marshaller
	
	@Override
	public CodeSystemVersionCatalogEntry doRead() {
		def cs = read.getCodeSystemByVersionId(ModelUtils.nameOrUriFromName("FIX-1014"), "See Remote Site", null)
		
		cs
	}

	@Test
	void TestDoRead() {
		def cs = doRead()
		
		assertNotNull cs
	}
	
	@Test
	void TestValidXml() {
		def cs = doRead()
		
		assertNotNull cs

		marshaller.marshal(cs, new StreamResult(new StringWriter()))
	}

	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromName("TEST-INVALID"), null)
	}

}
