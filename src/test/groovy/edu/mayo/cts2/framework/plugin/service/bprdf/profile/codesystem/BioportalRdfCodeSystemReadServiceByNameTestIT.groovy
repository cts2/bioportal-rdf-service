package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult
import static org.junit.Assert.*
import org.junit.Test;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadServiceByNameTestIT extends BioportalRdfCodeSystemReadServiceTestITBase {

	@Resource
	Cts2Marshaller marshaller
	
	@Override
	public CodeSystemCatalogEntry doRead() {
		def cs = read.read(ModelUtils.nameOrUriFromName("LNC"), null)
		
		cs
	}
	
	@Test
	void TestCodeSystemWithHyhpenName() {
		def cs = read.read(ModelUtils.nameOrUriFromName("FDA-MedDevice"), null)
		
		assertNotNull cs
		
		assertEquals cs.codeSystemName, "FDA-MedDevice"
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
