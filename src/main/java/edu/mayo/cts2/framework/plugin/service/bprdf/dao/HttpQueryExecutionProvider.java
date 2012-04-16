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

import org.springframework.beans.factory.InitializingBean;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import edu.mayo.twinkql.context.QueryExecutionProvider;

/**
 * The Class HttpQueryExecutionProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HttpQueryExecutionProvider implements QueryExecutionProvider, InitializingBean {

	private String sparqlService = "http://sparql.bioontology.org/sparql/";
	
	private String apiKey;

	@Resource
	private ApiKeyProvider apiKeyProvider;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.apiKey = this.apiKeyProvider.getApiKey();
	}
	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.context.QueryExecutionProvider#provideQueryExecution(com.hp.hpl.jena.query.Query)
	 */
	@Override
	public QueryExecution provideQueryExecution(String query) {
		QueryEngineHTTP qexec = new QueryEngineHTTP(
				this.sparqlService, query);

		qexec.addParam("apikey", this.apiKey);
		
		return qexec;
	}

}
