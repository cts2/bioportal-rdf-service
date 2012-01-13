package edu.mayo.cts2.framework.plugin.service.bprdf.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import edu.mayo.cts2.framework.core.config.option.Option;
import edu.mayo.cts2.framework.core.plugin.PluginConfigManager;
import edu.mayo.twinkql.template.TwinkqlTemplate;

@Component
public class RdfDao implements InitializingBean {
	
	private static Log log = LogFactory.getLog(RdfDao.class);
	
	public static final String BIOPORTAL_RDF_CONFIG_NAMESPACE = "bioportal-rdf-service";
	
	private static final String API_KEY_PROP = "apiKey";

	private String sparqlService = "http://sparql.bioontology.org/sparql";
	private String apiKey;
	
	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	@Resource
	private PluginConfigManager pluginConfigManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.setApiKey();
	}
	
	public ResultSet query(
			String queryNamespace, 
			String queryId, 
			Map<String,Object> parameters) throws Exception {
		String query = this.twinkqlTemplate.queryForString(queryNamespace, queryId, parameters);
		
		System.out.println(query);

		return this.executeQuery(query);
	}

	public ResultSet executeQuery(String queryString) throws Exception {	
		Query query = QueryFactory.create(queryString) ;
	
		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(
				this.sparqlService, query);
		qexec.addParam("apikey", this.apiKey);


		long time = System.currentTimeMillis();
		ResultSet results = qexec.execSelect();
		System.out.println("Bioportal Call Time: " + (System.currentTimeMillis() - time));

		return results;
	}
	
	protected void setApiKey(){
		//check for environment varialbe
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
}
