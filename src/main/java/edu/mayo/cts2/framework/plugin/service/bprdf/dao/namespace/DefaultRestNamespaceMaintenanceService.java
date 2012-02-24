package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import org.springframework.web.client.RestTemplate;

import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.service.namespace.MultiNameNamespaceReference;
import edu.mayo.cts2.framework.service.namespace.NamespaceMaintenanceService;

public class DefaultRestNamespaceMaintenanceService implements NamespaceMaintenanceService {

	@SuppressWarnings("unused")
	private Cts2RestClient cts2RestClient;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	private String serviceUri = "http://informatics.mayo.edu/cts2/services/bioportal-rdf";

	public DefaultRestNamespaceMaintenanceService(Cts2Marshaller cts2Marshaller) throws Exception {
		this.cts2RestClient = new Cts2RestClient(cts2Marshaller);
	}

	@Override
	public void save(MultiNameNamespaceReference namespaceReference) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addLocalName(String uri, String localName, boolean setPreferred) {
		this.restTemplate.postForLocation(
				this.serviceUri + "/namespacebyuri?uri={uri}", localName, uri);
	}

	@Override
	public void delete(String uri) {
		throw new UnsupportedOperationException();
	}
}
