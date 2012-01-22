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

@Component("httpQueryExecutionProvider")
public class HttpQueryExecutionProvider implements QueryExecutionProvider, InitializingBean {

	private Log log = LogFactory.getLog(this.getClass());

	public static final String BIOPORTAL_RDF_CONFIG_NAMESPACE = "bioportal-rdf-service";
	
	private static final String API_KEY_PROP = "apiKey";
	
	private String sparqlService = "http://sparql.bioontology.org/sparql";
	
	private String apiKey;
	
	@Resource
	private PluginConfigManager pluginConfigManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.apiKey == null){
			this.setUpApiKey();
		}
	}
	
	@Override
	public QueryExecution provideQueryExecution(Query query) {
		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(
				this.sparqlService, query);
		
		qexec.addParam("apikey", this.apiKey);
		
		return qexec;
	}
	
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
