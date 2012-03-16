package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import static org.junit.Assert.*

import org.junit.Test;

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller


class DefaultRestNamespaceReadServiceTestIT {

	@Test
	void testReadByURIBad(){
		def svc = new DefaultRestNamespaceReadService( new DelegatingMarshaller() )
		
		def result = svc.readPreferredByUri("http://something/that/isnt/there")
		
		assertNull result
	}
	
	
}
