package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.template.TwinkqlTemplate;

@Component
public class TripleStoreNamespaceCrawler implements NamespaceCrawler {
	
	protected final Log log = LogFactory.getLog(getClass().getName());

	private final static String UTILITY_NAMESPACE = "util";
	private final static String GET_DISTINCT_NAMESPACES = "getDistinctNamespaces";
	
	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	@Resource
	private NamespaceLookupService namespaceLookupService;
	
	@Resource
	private IdService idService;
	
	public void crawl(){
		for(String ontologyId : this.idService.getAllOntologyIds()){
			Set<String> foundNamespaces = this.doGatherNamespaces(ontologyId);
			
			for(String uri : foundNamespaces){
				String prefix = null;
				try {
					prefix = this.namespaceLookupService.getPreferredPrefixForUri(uri);
				} catch (Exception e) {
					log.warn("Error looking up namespace URI: " + uri);
					continue;
				}
				
				if(StringUtils.isBlank(prefix)){
					try {
						this.namespaceLookupService.addUriAndPrefix(prefix, uri);
					} catch (Exception e) {
						log.warn("Namespace Add for URI: " + uri + ", Prefix: " + prefix + " failed.", e);
						continue;
					}
				}
			}
		}
	}
	
	protected Set<String> doGatherNamespaces(String ontologyId){
		Set<String> namespaces = new HashSet<String>();
		
		String[] vars = new String[]{"s","p","o"};
		
		for(String var : vars){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("var", var);
			params.put("ontologyId", ontologyId);
			
			List<DistinctNamespaceResult> results = this.twinkqlTemplate.selectForList(
					UTILITY_NAMESPACE, 
					GET_DISTINCT_NAMESPACES, 
					params, 
					DistinctNamespaceResult.class);
			
			for(DistinctNamespaceResult ns : results){
				String namespace = ns.getNamespace();
				if(StringUtils.isNotBlank(namespace)){
					namespaces.add(namespace);
				}
			}
		}
		
		return namespaces;
	}
}
