package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.namespace.MultiNameNamespaceReference;
import edu.mayo.cts2.framework.service.namespace.NamespaceReadService;

public class DefaultRestNamespaceReadService implements NamespaceReadService {
	
	private Cts2RestClient cts2RestClient;

	private String serviceUri = "http://informatics.mayo.edu/cts2/services/bioportal-rdf";

	public DefaultRestNamespaceReadService(Cts2Marshaller cts2Marshaller) throws Exception {
		this.cts2RestClient = new Cts2RestClient(cts2Marshaller);
	}
	
	@Override
	public DocumentedNamespaceReference readPreferredByUri(String uri) {
		String url = this.serviceUri + "/namespacebyuri?uri={uri}";
		
		return this.cts2RestClient.getCts2Resource(url, DocumentedNamespaceReference.class, uri);
	}

	@Override
	public DocumentedNamespaceReference readPreferredByLocalName(
			String localName) {
		String url = this.serviceUri + "/namespace/"+localName;
		
		return this.cts2RestClient.getCts2Resource(url, DocumentedNamespaceReference.class);
	}

	@Override
	public MultiNameNamespaceReference readByUri(String uri) {
		String url = this.serviceUri + "/namespacebyuri?all=true&uri="+uri;
		
		return this.cts2RestClient.getCts2Resource(url, MultiNameNamespaceReference.class);
	}

	@Override
	public MultiNameNamespaceReference readByLocalName(String localName) {
		String url = this.serviceUri + "/namespace/"+localName+"?all=true";
		
		return this.cts2RestClient.getCts2Resource(url, MultiNameNamespaceReference.class);
	}

	public String getServiceUri() {
		return serviceUri;
	}

	public void setServiceUri(String serviceUri) {
		this.serviceUri = serviceUri;
	}

}
