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
import java.util.Timer;
import java.util.TimerTask;

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
	private final static String VERSION_SUBRESOURCE = "version";
	
	private final static String BIOPORTAL_ONTOLOGIES_URI = "http://bioportal.bioontology.org/ontologies/";
	private final static String PURL_OBO_OWL_URI = "http://purl.org/obo/owl/";

	private final static String UTILITY_NAMESPACE = "util";
	private final static String GET_IDS = "getIds";

	@Resource
	private TwinkqlTemplate twinkqlTemplate;

	private Map<String, List<String>> ontologyIdToIds = new HashMap<String, List<String>>();
	private Map<String, Integer> ontologyIdToLatestId = new HashMap<String, Integer>();
	private Map<String, String> idToOntologyId = new HashMap<String, String>();
	private Map<String, CodeSystemVersionName> csvNameToCsv = new HashMap<String, CodeSystemVersionName>();
	private Map<String, CodeSystemVersionName> idToCsvName = new HashMap<String, CodeSystemVersionName>();
	private Map<String, String> acronymToOntologyId = new HashMap<String, String>();
	private Map<String, String> ontologyIdToAcronym = new HashMap<String, String>();
	private Map<String, String> acronymToUri = new HashMap<String, String>();
	private Map<String, String> uriToAcronym = new HashMap<String, String>();
	private Map<String, String> ontologyIdToUri = new HashMap<String, String>();
	private Map<String, String> idToDocumentUri = new HashMap<String, String>();

	private Timer cacheClearingTimer = new Timer();

	private static final int ONE_HOUR = 1000 * 60 * 60;
	
	private volatile boolean cacheBuilding = true;

	public void afterPropertiesSet() throws Exception {
		this.cacheClearingTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					cacheBuilding = true;
					buildCache();
				} finally {
					cacheBuilding = false;
				}
			}

		}, 0, ONE_HOUR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	// id = CodeSystemVersion
	// ontologyId = CodeSystem
	protected synchronized void buildCache() {

		List<IdResult> result = this.twinkqlTemplate.selectForList(
				UTILITY_NAMESPACE, GET_IDS, null, IdResult.class);

		for (IdResult idResult : result) {
			String id = idResult.getId();
			String ontologyId = idResult.getOntologyId();
			String acronym = idResult.getAcronym();
			String uri = BIOPORTAL_ONTOLOGIES_URI + acronym;
			String documentUri = BIOPORTAL_ONTOLOGIES_URI + acronym + "/"
					+ VERSION_SUBRESOURCE + "/" + id;
			
			if(acronym.equals("ontologia")){
				System.out.println("here");
			}

			if (!this.acronymToOntologyId.containsKey(acronym)) {
				this.acronymToOntologyId.put(acronym, ontologyId);
			} else {
				String foundOntologyId = this.acronymToOntologyId.get(acronym);

				if (!ontologyId.equals(foundOntologyId)) {
					log.warn("Found multiple OntologyIds (" + foundOntologyId
							+ "," + ontologyId + ") for the same acronym ("
							+ acronym + ").");
				}
			}

			if (!this.ontologyIdToAcronym.containsKey(ontologyId)) {
				this.ontologyIdToAcronym.put(ontologyId, acronym);
			} else {
				String foundAcronym = this.ontologyIdToAcronym.get(ontologyId);

				if (!acronym.equals(foundAcronym)) {
					log.warn("Found multiple Acronyms (" + foundAcronym + ","
							+ acronym + ") for the same ontologyId ("
							+ ontologyId + ").");
				}
			}

			if (!this.ontologyIdToIds.containsKey(ontologyId)) {
				this.ontologyIdToIds.put(ontologyId, new ArrayList<String>());
			}
			this.ontologyIdToIds.get(ontologyId).add(id);

			Integer latestOntologyId = this.ontologyIdToLatestId
					.get(ontologyId);
			if (latestOntologyId == null) {
				ontologyIdToLatestId.put(ontologyId, Integer.parseInt(id));
			} else {
				Integer foundOntologyId = Integer.parseInt(id);
				if (foundOntologyId > latestOntologyId) {
					ontologyIdToLatestId.put(ontologyId, foundOntologyId);
				}
			}

			this.idToOntologyId.put(id, ontologyId);

			CodeSystemVersionName csvName = new CodeSystemVersionName(acronym,
					id);
			this.csvNameToCsv.put(csvName.toString(), csvName);
			this.idToCsvName.put(id, csvName);

			this.acronymToUri.put(acronym, uri);
			this.uriToAcronym.put(uri, acronym);

			this.ontologyIdToUri.put(ontologyId, uri);
			this.idToDocumentUri.put(id, documentUri);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#
	 * getOntologyIdForId(java.lang.String)
	 */
	@Override
	public String getOntologyIdForId(String id) {
		return this.getFromCache(idToOntologyId, id);
	}

	@Override
	public String getOntologyIdForAcronym(String acronym) {
		return this.getFromCache(acronymToOntologyId, acronym);
	}

	@Override
	public String getAcronymForOntologyId(String ontologyId) {
		return this.getFromCache(ontologyIdToAcronym, ontologyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#
	 * getIdsForOntologyId(java.lang.String)
	 */
	@Override
	public Iterable<String> getIdsForOntologyId(String ontologyId) {
		return this.getFromCache(ontologyIdToIds, ontologyId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService#
	 * getCurrentIdForOntologyId(java.lang.String)
	 */
	@Override
	public String getCurrentIdForOntologyId(String ontologyId) {
		Integer intOntologyId = this.getFromCache(ontologyIdToLatestId, ontologyId);
		if (intOntologyId == null) {
			return null;
		} else {
			return Integer.toString(intOntologyId);
		}
	}

	@Override
	public CodeSystemVersionName getCodeSystemVersionNameForName(
			String codeSystemVersionName) {
		return this.getFromCache(csvNameToCsv, codeSystemVersionName);
	}

	@Override
	public Set<String> getAllOntologyIds() {
		return this.getKeySetFromCache(ontologyIdToLatestId);
	}

	@Override
	public CodeSystemVersionName getCodeSystemVersionNameForId(String id) {
		return this.getFromCache(idToCsvName, id);
	}

	@Override
	public String getUriForAcronym(String acronym) {
		return this.getFromCache(acronymToUri, acronym);
	}

	@Override
	public String getDocumentUriForId(String id) {
		return this.getFromCache(idToDocumentUri, id);
	}

	@Override
	public String getUriForOntologyId(String ontologyId) {
		return this.getFromCache(this.ontologyIdToUri, ontologyId);
	}
	
	public boolean isAcronym(String acronym) {
		return this.getFromCache(this.acronymToUri, acronym) != null;
	}

	@Override
	public String getAcronymForUri(String uri) {
		// try adding a '/' if we don't find it
		for (String addition : Arrays.asList("", "/")) {
			String acronym = this.getFromCache(this.uriToAcronym, uri + addition);
			if (acronym != null) {
				return acronym;
			}
		}

		if (uri.startsWith(BIOPORTAL_PURL_URI)) {
			uri = StringUtils.removeStart(uri, BIOPORTAL_PURL_URI);
			uri = StringUtils.removeEnd(uri, "/");
			uri = StringUtils.substringBefore(uri, "/");
			
			uri = StringUtils.removeEnd(uri, ":");
			uri = StringUtils.removeEnd(uri, "#");
			
			return uri;
		} if (uri.startsWith(PURL_OBO_OWL_URI)) {
			uri = StringUtils.removeStart(uri, PURL_OBO_OWL_URI);
			uri = StringUtils.removeEnd(uri, "/");
			uri = StringUtils.substringBefore(uri, "/");
			
			uri = StringUtils.removeEnd(uri, ":");
			uri = StringUtils.removeEnd(uri, "#");
			
			return uri;
		} else {
			return null;
		}
	}
	
	protected <T> T getFromCache(Map<?,T> map, Object key) {
		while(cacheBuilding) {
			//wait for cache rebuild...
		}
		return map.get(key);
	}
	
	protected <T> Set<T> getKeySetFromCache(Map<T,?> map) {
		while(cacheBuilding) {
			//wait for cache rebuild...
		}
		return map.keySet();
	}

}
