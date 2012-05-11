package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult
import static org.junit.Assert.*
import org.junit.Test;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemVersionReadServiceByNameTestIT extends BioportalRdfCodeSystemVersionReadServiceTestITBase {

	@Resource
	Cts2Marshaller marshaller
	
	@Override
	public Object doRead() {
		read.read(ModelUtils.nameOrUriFromName("LNC-44256"), null)
	}

	@Test
	void TestDoRead() {
		def csv = doRead()
		
		assertNotNull csv
	}

	@Test
	void TestReadWithNoFileNameProperty() {
		def csv = read.read(ModelUtils.nameOrUriFromName("CBO-39336"), null)
		
		assertNotNull csv
		
		marshaller.marshal(csv, new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestReadByNameWithHyphen() {
		def csv = read.read(ModelUtils.nameOrUriFromName("GFO-Bio-42453"), null)
		
		assertNotNull csv
	}
	
	
	@Test
	void TestValidXml() {
		def csv = doRead()
		
		assertNotNull csv
		
		marshaller.marshal(csv, new StreamResult(new StringWriter()))
	}
	
	@Test
	void doReadByInvalid() {
		def csv = read.read(ModelUtils.nameOrUriFromName("INVALID-VERSION"), null)
		assertNull csv
	}

}
