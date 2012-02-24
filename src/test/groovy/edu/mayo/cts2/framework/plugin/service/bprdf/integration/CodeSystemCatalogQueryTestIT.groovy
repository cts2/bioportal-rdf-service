package edu.mayo.cts2.framework.plugin.service.bprdf.integration;

import org.junit.Test;
import static org.junit.Assert.*
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
	
	@Test void TestGetCodeSystemsWithDefaultFilter(){
		
		CodeSystemCatalogEntryDirectory result =
			client.getCts2Resource(server + "codesystems?matchvalue=GO", CodeSystemCatalogEntryDirectory.class);
			
		assertNotNull result

		assertTrue result.entry.length > 0
		
		result.entry.each { 
			assertTrue it.codeSystemName, it.resourceSynopsis.value.content.toLowerCase().contains("GO".toLowerCase())
		}
	}
}
