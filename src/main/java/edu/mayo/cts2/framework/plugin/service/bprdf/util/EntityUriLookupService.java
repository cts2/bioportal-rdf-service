package edu.mayo.cts2.framework.plugin.service.bprdf.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace.NamespaceLookupService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription.BioportalRestEntityDescriptionTransform;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
@Component("entityUriLookupService")
public class EntityUriLookupService {

	protected final Log log = LogFactory.getLog(getClass().getName());
	
	@Resource
	private BioportalRestClient bioportalRestClient;
	
	@Resource
	private NamespaceLookupService namespaceLookupService;
	
	private Map<String,String> knownNamespaces = new HashMap<String,String>();

	@Resource
	private BioportalRestEntityDescriptionTransform bioportalRestTransform;

	public String getUriFromCode(String ontologyId, String code) {
		Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
		ResolvedFilter filter = new ResolvedFilter();
		filter.setMatchAlgorithmReference(StandardMatchAlgorithmReference.EXACT_MATCH
				.getMatchAlgorithmReference());
		filter.setMatchValue(code);
		filters.add(filter);

		long start = System.currentTimeMillis();
		SuccessBean success = bioportalRestClient.searchEntities(ontologyId,
				filters, new Page());
		log.info("Query REST time: "
				+ (System.currentTimeMillis() - start));

		return this.bioportalRestTransform.successBeanToEntityUri(success);
	}

	public String getUriFromScopedEntityName(String ontologyId,
			ScopedEntityName scopedEntity) {

		String uri = null;
		if (scopedEntity != null) {
			String name = scopedEntity.getName();
			String namespace = scopedEntity.getNamespace();
			
			uri = this.getUriFromNamespace(scopedEntity);
			
			if (StringUtils.isBlank(uri)) {
				uri = this.getUriFromCode(ontologyId, name);
			}

			if (StringUtils.isBlank(uri)) {
				uri = this.getUriFromCode(ontologyId, namespace + ":" + name);
			}
	
			//For the underscore case "GO_12345" - Lucene will split these on index
			if (StringUtils.isBlank(uri)) {
				String[] parts = StringUtils.split(name, '_');
				if(parts.length == 2){
					uri = this.getUriFromCode(ontologyId, StringUtils.join(parts, ':'));
				}
			}

			if (StringUtils.isBlank(uri)) {
				uri = this.getUriFromCode(ontologyId, namespace + "_" + name);
			}
		}

		return uri;
	}
	
	private String getUriFromNamespace(ScopedEntityName name){
		String uri = null;
		String namespace = name.getNamespace();
		if(StringUtils.isNotBlank(namespace)){
			
			if(! this.knownNamespaces.containsKey(namespace)){
				uri = this.namespaceLookupService.getUriForNamespace(namespace);
				
				if(StringUtils.isNotBlank(uri)){
					this.knownNamespaces.put(namespace, uri);
				}
			} 
			
			uri = this.knownNamespaces.get(namespace);
		}
		
		if(StringUtils.isNotBlank(uri)){
			uri = uri + name.getName();
		}
		
		return uri;
	}
}
