package edu.mayo.cts2.framework.plugin.service.bprdf.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg

class CodeSystemCatalogReadTestIT extends BaseServiceTestITBase {
	
	@Test void TestGetCodeSystemByName(){
		
		CodeSystemCatalogEntryMsg result = 
			client.getCts2Resource(server + "codesystem/LNC?", CodeSystemCatalogEntryMsg.class);
			
		assertNotNull result
	}
	
	@Test void TestGetCodeSystemByUri(){
		
		CodeSystemCatalogEntryMsg result = 
			client.getCts2Resource(server + "codesystembyuri?uri=http://purl.bioontology.org/ontology/CST.rdf", CodeSystemCatalogEntryMsg.class);
			
		assertNotNull result
	}
}
