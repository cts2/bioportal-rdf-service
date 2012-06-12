package edu.mayo.cts2.framework.plugin.service.bprdf.integration;

import org.apache.commons.lang.StringUtils
import org.junit.Test;
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate

import static org.junit.Assert.*

class ValidateWikiPageUrlsTestIT  {

	@Test
	void TestWikiUrls(){
		def wikiUrl = "http://www.bioontology.org/wiki/index.php/CTS2_RDF_Plugin"
		
		def prefix = "http://informatics.mayo.edu/cts2/services/bioportal-rdf"
		
		def slurper = new XmlSlurper()
		def html = slurper.parse(wikiUrl)
		
		def template = new RestTemplate()
		
		html.depthFirst().collect { it }.findAll { it.name() == "a" }.each {
		    if(StringUtils.startsWith(it.@href.text(), prefix)){
				def url = it.@href.text()
							
				println "Checking: " + url
				
				def response = template.exchange( 
						  new URI(url),
						  HttpMethod.GET,
						  null,
						  String ).getBody();
				
				assertTrue StringUtils.isNotBlank(response)
			}
		}
	}
}
