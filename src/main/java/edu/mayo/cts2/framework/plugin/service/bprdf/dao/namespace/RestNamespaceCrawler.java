package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ncbo.stanford.bean.NamespaceBean;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;

@Component
public class RestNamespaceCrawler implements NamespaceCrawler {
	
	protected final Log log = LogFactory.getLog(getClass().getName());
	
	@Resource
	private BioportalRestClient bioportalRestClient;
	
	@Resource
	private IdService idService;
	
	@Resource
	private NamespaceLookupService namespaceLookupService;
	
	@Override
	public void crawl(){
		for(String ontologyId : this.idService.getAllOntologyIds()){
			for(String id : this.idService.getIdsForOntologyId(ontologyId)){
				this.doCrawlId(id);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void doCrawlId(String id){
		log.info("Crawling ID:  " + id + " for namespaces...");
		
		SuccessBean bean;
		try {
			bean = bioportalRestClient.getNamespaces(id);
		} catch (RestClientException e) {
			log.warn("Namespace Crawl for ID: " + id + " failed.", e);
			return;
		}
		
		int count = 0;
		
		List<Object> beans =  bean.getData();
		
		for(Object obj : beans){
			List list = (List) obj;
			for(Object innerObj : list){
				NamespaceBean namespace = (NamespaceBean) innerObj;
				
				String prefix = namespace.getPrefix();
				String uri = namespace.getUri();
				
				if(StringUtils.isNotBlank(prefix) && StringUtils.isNotBlank(uri)){
					try {
						this.namespaceLookupService.addUriAndPrefix(prefix, uri);
					} catch (Exception e) {
						log.warn("Namespace Add for ID: " + id + ", URI: " + uri + ", Prefix: " + prefix + " failed.", e);
						continue;
					}
					count++;
				}
			}
		}	
		log.info(" - Done: Found " + count + " namespaces");
	}
}
