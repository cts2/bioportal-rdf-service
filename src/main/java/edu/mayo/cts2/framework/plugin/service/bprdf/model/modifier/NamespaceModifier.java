/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace.NamespaceLookupService;
import edu.mayo.twinkql.context.TwinkqlContext;
import edu.mayo.twinkql.model.NamespaceDefinition;
import edu.mayo.twinkql.model.TwinkqlConfig;
import edu.mayo.twinkql.model.TwinkqlConfigItem;
import edu.mayo.twinkql.result.callback.Modifier;

/**
 * The Class NamespaceModifier.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("namespaceModifier")
public class NamespaceModifier implements Modifier<String>, InitializingBean {
	
	protected final Log log = LogFactory.getLog(getClass().getName());
	
	private Map<String,String> knownNamespaces;
	
	@Resource
	private TwinkqlContext twinkqlContext;
	
	@Resource
	private IdService idService;
	
	@Resource
	private NamespaceLookupService namespaceLookupService;

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.knownNamespaces == null){
			this.knownNamespaces = this.setUpDefaultKnownNamespaces();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.Modifier#beforeSetting(java.lang.Object)
	 */
	@Override
	public String beforeSetting(String object) {
		return this.getNamespace(object);
	}
	
	/**
	 * Gets the namespace.
	 *
	 * @param uri the uri
	 * @return the namespace
	 */
	public String getNamespace(String uri){
		
		String acronym = this.idService.getAcronymForUri(uri);
		if(acronym != null){
			return acronym;
		}

		if(! this.knownNamespaces.containsKey(uri)){

			String prefix = 
					this.namespaceLookupService.getPreferredPrefixForUri(uri);
			
			if(StringUtils.isNotBlank(prefix)){
				this.knownNamespaces.put(uri, prefix);

			} else {
				String hashString = Integer.toString(uri.hashCode());
				
				hashString = "ns"+hashString;
				
				this.knownNamespaces.put(uri, hashString);
				
				log.warn("Generating prefix for Namespace URI: " + uri);
			}
		}
		
		return this.knownNamespaces.get(uri);
	}

	/**
	 * Sets the up default known namespaces.
	 *
	 * @return the map
	 */
	protected Map<String,String> setUpDefaultKnownNamespaces(){
		Map<String,String> namespaces = new HashMap<String,String>();
		
		namespaces.put("http://purl.bioontology.org/ontology/bioportal/", "bioportal");
		namespaces.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdfs");
		namespaces.put("http://www.w3.org/2002/07/owl#", "owl");
		
		TwinkqlConfig twinkqlConfig = this.twinkqlContext.getTwinkqlConfig();
		if(twinkqlConfig != null && twinkqlConfig.getTwinkqlConfigItem() != null){
			for(TwinkqlConfigItem item : twinkqlConfig.getTwinkqlConfigItem()){
				if(item.getNamespace() != null){
					NamespaceDefinition def = item.getNamespace();
					namespaces.put(def.getUri(), def.getPrefix());
				}
			}
		}
		
		return namespaces;
		
	}
}
