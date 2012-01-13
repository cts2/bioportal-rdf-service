package edu.mayo.cts2.framework.plugin.service.bprdf.integration;

import static org.junit.Assert.*

import org.junit.Test

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryDirectory

class CodeSystemCatalogQueryTestIT extends BaseServiceTestITBase {
	
	@Test void TestGetCodeSystemsSmallMax(){
		
		CodeSystemCatalogEntryDirectory result = 
			client.getCts2Resource(server + "codesystems?maxtoreturn=5", CodeSystemCatalogEntryDirectory.class);
			
		assertNotNull result
		
		assertEquals 5, result.getEntryCount()
	}
	
	@Test void TestGetCodeSystemsLargeMax(){
		
		CodeSystemCatalogEntryDirectory result =
			client.getCts2Resource(server + "codesystems?maxtoreturn=500", CodeSystemCatalogEntryDirectory.class);
			
		assertNotNull result
		
		//not sure how many there will actually be...
		assertTrue result.getEntryCount() > 10
	}
}
