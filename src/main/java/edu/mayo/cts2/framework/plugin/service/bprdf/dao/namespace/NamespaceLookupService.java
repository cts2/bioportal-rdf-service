package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.service.namespace.NamespaceMaintenanceService;
import edu.mayo.cts2.framework.service.namespace.NamespaceReadService;

@Component
public class NamespaceLookupService implements InitializingBean {
	
	@Resource
	private Cts2Marshaller cts2Marshaller;

	@Autowired(required=false)
	private NamespaceReadService namespaceReadService;
	
	@Autowired(required=false)
	private NamespaceMaintenanceService namespaceMaintenanceService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.namespaceReadService == null){
			this.namespaceReadService = new DefaultRestNamespaceReadService(this.cts2Marshaller);
		}
		
		if(this.namespaceMaintenanceService == null){
			this.namespaceMaintenanceService = new DefaultRestNamespaceMaintenanceService(this.cts2Marshaller);
		}
	}
	
	public String getUriForNamespace(String prefix){
		return this.namespaceReadService.readPreferredByLocalName(prefix).getContent();
	}
	
	public String getPreferredPrefixForUri(String uri){
		return this.namespaceReadService.readPreferredByUri(uri).getContent();
	}
	
	public void addUriAndPrefix(String prefix, String uri){
		this.namespaceMaintenanceService.addLocalName(uri, prefix, true);
	}
	
}
