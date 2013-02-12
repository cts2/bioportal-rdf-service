package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import edu.mayo.cts2.framework.core.client.Cts2RestClient;
import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.model.service.namespace.MultiNameNamespaceReference;
import edu.mayo.cts2.framework.service.namespace.NamespaceReadService;

public class DefaultRestNamespaceReadService implements NamespaceReadService {

	private Cts2RestClient cts2RestClient;

	private String serviceUri = "http://informatics.mayo.edu/cts2/services/bioportal-rdf";

	public DefaultRestNamespaceReadService(Cts2Marshaller cts2Marshaller)
			throws Exception {
		this.cts2RestClient = new Cts2RestClient(cts2Marshaller, true);
	}

	@Override
	public DocumentedNamespaceReference readPreferredByUri(final String uri) {
		return this
				.doInRestClient(new DoInRestClient<DocumentedNamespaceReference>() {

					@Override
					public DocumentedNamespaceReference function() {
						String url = serviceUri + "/namespacebyuri?uri={uri}";

						return cts2RestClient.getCts2Resource(url, null, null,
								DocumentedNamespaceReference.class, uri);
					}
				});
	}

	@Override
	public DocumentedNamespaceReference readPreferredByLocalName(
			final String localName) {

		return this
				.doInRestClient(new DoInRestClient<DocumentedNamespaceReference>() {

					@Override
					public DocumentedNamespaceReference function() {
						String url = serviceUri + "/namespace/" + localName;

						return cts2RestClient.getCts2Resource(url,
								DocumentedNamespaceReference.class);
					}
				});
	}

	@Override
	public MultiNameNamespaceReference readByUri(final String uri) {

		return this
				.doInRestClient(new DoInRestClient<MultiNameNamespaceReference>() {

					@Override
					public MultiNameNamespaceReference function() {
						String url = serviceUri
								+ "/namespacebyuri?all=true&uri=" + uri;
						return cts2RestClient.getCts2Resource(url,
								MultiNameNamespaceReference.class);
					}
				});
	}

	@Override
	public MultiNameNamespaceReference readByLocalName(final String localName) {

		return this
				.doInRestClient(new DoInRestClient<MultiNameNamespaceReference>() {

					@Override
					public MultiNameNamespaceReference function() {
						String url = serviceUri + "/namespace/" + localName
								+ "?all=true";

						return cts2RestClient.getCts2Resource(url,
								MultiNameNamespaceReference.class);
					}

				});

	}

	private <T> T doInRestClient(DoInRestClient<T> funtion) {
		try {
			return funtion.function();
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
				return null;
			} else {
				throw e;
			}
			// Need to do this because our testing OSGi war runs in the same
			// JVM, and
			// there end up being two 'HttpClientErrorException' classes loaded.
		} catch (RuntimeException e) {
			return null;
		}
	}

	private interface DoInRestClient<T> {
		public T function();
	}

	public String getServiceUri() {
		return serviceUri;
	}

	public void setServiceUri(String serviceUri) {
		this.serviceUri = serviceUri;
	}

}
