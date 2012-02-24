package edu.mayo.cts2.framework.plugin.service.bprdf.integration;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntryMsg

class CodeSystemCatalogReadTestIT extends BaseServiceTestITBase {
	
	@Test void TestGetCodeSystemByName(){
		
		CodeSystemCatalogEntryMsg result = 
			client.getCts2Resource(server + "codesystem/GO-1070", CodeSystemCatalogEntryMsg.class);
			
		assertNotNull result
	}
	
	@Ignore
	@Test void TestGetCodeSystemByUri(){
		
		CodeSystemCatalogEntryMsg result = 
			client.getCts2Resource(server + "codesystembyuri?uri=http://bioportal.bioontology.org/ontologies/1000", CodeSystemCatalogEntryMsg.class);
			
		assertNotNull result
	}
}
