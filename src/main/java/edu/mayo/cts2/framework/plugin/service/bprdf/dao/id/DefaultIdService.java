/*
 * Copyright: (c) 2004-2012 Mayo Foundation for Medical Education and 
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
package edu.mayo.cts2.framework.plugin.service.bprdf.dao.id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import edu.mayo.twinkql.template.TwinkqlTemplate;

/**
 * The Class DefaultIdService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class DefaultIdService implements IdService, InitializingBean {
	
	protected final Log log = LogFactory.getLog(getClass().getName());
	
	private final static String BIOPORTAL_PURL_URI = "http://purl.bioontology.org/ontology/";
	private final static String BIOPORTAL_ONTOLOGIES_URI = "http://bioportal.bioontology.org/ontologies/";
	
	private final static String UTILITY_NAMESPACE = "util";
	private final static String GET_IDS = "getIds";
	
	@Resource
	private TwinkqlTemplate twinkqlTemplate;
	
	private Map<String,List<String>> ontologyIdToIds = new HashMap<String,List<String>>();
	private Map<String,Integer> ontologyIdToLatestId = new HashMap<String,Integer>();
	private Map<String,String> idToOntologyId = new HashMap<String,String>();
	private Map<String,CodeSystemVersionName> csvNameToCsv = new HashMap<String,CodeSystemVersionName>();
	private Map<String,CodeSystemVersionName> idToCsvName = new HashMap<String,CodeSystemVersionName>();
	private Map<String,String> acronymToOntologyId = new HashMap<String,String>();
	private Map<String,String> ontologyIdToAcronym = new HashMap<String,String>();
	private Map<String,String> acronymToUri = new HashMap<String,String>();
	private Map<String,String> uriToAcronym = new HashMap<String,String>();
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	//id = CodeSystemVersion
	//ontologyId = CodeSystem
	public void afterPropertiesSet() throws Exception {
		List<IdResult> result = 
			this.twinkqlTemplate.selectForList(UTILITY_NAMESPACE, GET_IDS, null, IdResult.class);
		
		for(IdResult idResult : result){
			String id = idResult.getId();
			String ontologyId = idResult.getOntologyId();
			String acronym = idResult.getAcronym();
			String uri = BIOPORTAL_ONTOLOGIES_URI + acronym;

			if(! this.acronymToOntologyId.containsKey(acronym)){
				this.acronymToOntologyId.put(acronym, ontologyId);
			} else {
				String foundOntologyId = 
					this.acronymToOntologyId.get(acronym);
				
				if(! ontologyId.equals(foundOntologyId)){
						log.warn("Found multiple OntologyIds ("+foundOntologyId+","+ontologyId+") for the same acronym ("+acronym+").");
				}
			}
			
			if(! this.ontologyIdToAcronym.containsKey(ontologyId)){
				this.ontologyIdToAcronym.put(ontologyId, acronym);
			} else {
				String foundAcronym = 
					this.ontologyIdToAcronym.get(ontologyId);
				
				if(! acronym.equals(foundAcronym)){
						log.warn("Found multiple Acronyms ("+foundAcronym+","+acronym+") for the same ontologyId ("+ontologyId+").");
				}
			}
			
			if(! this.ontologyIdToIds.containsKey(ontologyId)){
				this.ontologyIdToIds.put(ontologyId, new ArrayList<String>());
			}
			this.ontologyIdToIds.get(ontologyId).add(id);
			
			Integer latestOntologyId = this.ontologyIdToLatestId.get(ontologyId);
			if(latestOntologyId == null){
				ontologyIdToLatestId.put(ontologyId, Integer.parseInt(id));
			} else {
				Integer foundOntologyId = Integer.parseInt(id);
				if(foundOntologyId > latestOntologyId){
					ontologyIdToLatestId.put(ontologyId, foundOntologyId);
				}
			}
			
			this.idToOntologyId.put(id, ontologyId);
			
			CodeSystemVersionName csvName = new CodeSystemVersionName(acronym, id);
			this.csvNameToCsv.put(csvName.toString(), csvName);
			this.idToCsvName.put(id, csvName);
			
			this.acronymToUri.put(acronym, uri);
			this.uriToAcronym.put(uri, acronym);
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#getOntologyIdForId(java.lang.String)
	 */
	@Override
	public String getOntologyIdForId(String id) {
		return this.idToOntologyId.get(id);
	}

	@Override
	public String getOntologyIdForAcronym(String acronym) {
		return this.acronymToOntologyId.get(acronym);
	}
	
	@Override
	public String getAcronymForOntologyId(String ontologyId) {
		return this.ontologyIdToAcronym.get(ontologyId);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#getIdsForOntologyId(java.lang.String)
	 */
	@Override
	public Iterable<String> getIdsForOntologyId(String ontologyId) {
		return this.ontologyIdToIds.get(ontologyId);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#getCurrentIdForOntologyId(java.lang.String)
	 */
	@Override
	public String getCurrentIdForOntologyId(String ontologyId) {
		Integer intOntologyId = this.ontologyIdToLatestId.get(ontologyId);
		if(intOntologyId == null){
			return null;
		} else {
			return Integer.toString(intOntologyId);
		}
	}

	@Override
	public CodeSystemVersionName getCodeSystemVersionNameForName(
			String codeSystemVersionName) {
		return this.csvNameToCsv.get(codeSystemVersionName);
	}

	@Override
	public Set<String> getAllOntologyIds() {
		return this.ontologyIdToLatestId.keySet();
	}

	@Override
	public CodeSystemVersionName getCodeSystemVersionNameForId(String id) {
		return this.idToCsvName.get(id);
	}

	@Override
	public String getUriForAcronym(String acronym) {
		return this.acronymToUri.get(acronym);
	}
	
	@Override
	public String getAcronymForUri(String uri) {
		//try adding a '/' if we don't find it
		for(String addition : Arrays.asList("", "/")){
			String acronym = this.uriToAcronym.get(uri+addition);
			if(acronym != null){
				return acronym;
			}
		}
		
		if(uri.startsWith(BIOPORTAL_PURL_URI)){
			uri = StringUtils.removeEnd(uri, "/");
			return StringUtils.substringAfterLast(uri, "/");
		} else {
			return null;	
		}
	}
	
}
