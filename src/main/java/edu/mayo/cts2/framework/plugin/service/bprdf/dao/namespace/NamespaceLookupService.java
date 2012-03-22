package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.osgi.service.importer.OsgiServiceLifecycleListener;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.service.namespace.NamespaceMaintenanceService;
import edu.mayo.cts2.framework.service.namespace.NamespaceReadService;

@Component("namespaceLookupService")
public class NamespaceLookupService implements OsgiServiceLifecycleListener, InitializingBean {
	
	@Resource
	private Cts2Marshaller cts2Marshaller;

	private NamespaceReadService namespaceReadService;
	
	private NamespaceMaintenanceService namespaceMaintenanceService;
	
	private NamespaceReadService defaultNamespaceReadService;
	
	private NamespaceMaintenanceService defaultNamespaceMaintenanceService;

	@Override
	public void afterPropertiesSet() throws Exception {

		this.defaultNamespaceMaintenanceService = new DefaultRestNamespaceMaintenanceService(this.cts2Marshaller);
		this.defaultNamespaceReadService = new DefaultRestNamespaceReadService(this.cts2Marshaller);
		
		if(this.namespaceReadService == null){
			this.namespaceReadService = defaultNamespaceReadService;
		}
		
		if(this.namespaceMaintenanceService == null){
			this.namespaceMaintenanceService = defaultNamespaceMaintenanceService;
		}
	}

	public String getUriForNamespace(String prefix){
		DocumentedNamespaceReference namespace = this.namespaceReadService.readPreferredByLocalName(prefix);
		
		if(namespace != null){
			return namespace.getUri();
		} else {
			return null;
		}
	}
	
	public String getPreferredPrefixForUri(String uri){
		DocumentedNamespaceReference namespace = this.namespaceReadService.readPreferredByUri(uri);
		
		if(namespace != null){
			return namespace.getContent();
		} else {
			return null;
		}
	}
	
	public void addUriAndPrefix(String prefix, String uri){
		this.namespaceMaintenanceService.addLocalName(uri, prefix, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void bind(Object service, Map properties) throws Exception {
		if(service instanceof NamespaceReadService){
			this.namespaceReadService = (NamespaceReadService) service;
		}
		
		if(service instanceof NamespaceMaintenanceService){
			this.namespaceMaintenanceService = (NamespaceMaintenanceService) service;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void unbind(Object service, Map properties) throws Exception {
		if(service instanceof NamespaceReadService){
			this.namespaceReadService = this.defaultNamespaceReadService;
		}
		
		if(service instanceof NamespaceMaintenanceService){
			this.namespaceMaintenanceService = this.defaultNamespaceMaintenanceService;
		}
	}
}
