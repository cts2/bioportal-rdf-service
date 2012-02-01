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
package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import edu.mayo.cts2.framework.core.config.option.Option;
import edu.mayo.cts2.framework.core.plugin.PluginConfigManager;
import edu.mayo.twinkql.context.QueryExecutionProvider;

/**
 * The Class HttpQueryExecutionProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("httpQueryExecutionProvider")
public class HttpQueryExecutionProvider implements QueryExecutionProvider, InitializingBean {

	private Log log = LogFactory.getLog(this.getClass());

	public static final String BIOPORTAL_RDF_CONFIG_NAMESPACE = "bioportal-rdf-service";
	
	private static final String API_KEY_PROP = "apiKey";
	
	private String sparqlService = "http://alphasparql.bioontology.org/sparql";
	
	private String apiKey;
	
	@Resource
	private PluginConfigManager pluginConfigManager;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.apiKey == null){
			this.setUpApiKey();
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.context.QueryExecutionProvider#provideQueryExecution(com.hp.hpl.jena.query.Query)
	 */
	@Override
	public QueryExecution provideQueryExecution(Query query) {
		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(
				this.sparqlService, query);
		
		qexec.addParam("apikey", this.apiKey);
		
		return qexec;
	}
	
	/**
	 * Sets the up api key.
	 */
	protected void setUpApiKey(){
		//check for environment variable
		String apiKeyEnvVar = System.getProperty(API_KEY_PROP);
		if(StringUtils.isNotBlank(apiKeyEnvVar)){
			log.info("Using APIKEY from System Property.");
			this.apiKey = apiKeyEnvVar;
			
			return;
		}
		
		Option apiKey = this.pluginConfigManager.
    			getPluginConfigProperties(BIOPORTAL_RDF_CONFIG_NAMESPACE).
    				getStringOption(API_KEY_PROP);
		
		if(apiKey != null && StringUtils.isNotBlank(apiKey.getOptionValueAsString())){
			log.info("Using APIKEY from Configuration File.");
			this.apiKey = apiKey.getOptionValueAsString();
			
			return;
		}
		
		log.warn("No Bioportal API Key Set! Please sent one via the System Variable: " 
				+ API_KEY_PROP + " or in the Bioportal config file.");
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
