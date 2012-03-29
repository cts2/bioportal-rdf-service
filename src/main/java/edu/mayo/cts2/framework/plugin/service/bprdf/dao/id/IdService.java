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

import java.util.Set;

/**
 * The Interface IdService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface IdService {

	public Set<String> getAllOntologyIds();
	/**
	 * Gets the ontology id for id.
	 *
	 * @param id the id
	 * @return the ontology id for id
	 */
	public String getOntologyIdForId(String id);
	
	public CodeSystemVersionName getCodeSystemVersionNameForName(String codeSystemVersionName);
	
	public CodeSystemVersionName getCodeSystemVersionNameForId(String id);

	/**
	 * Gets the ids for ontology id.
	 *
	 * @param OntologyId the ontology id
	 * @return the ids for ontology id
	 */
	public Iterable<String> getIdsForOntologyId(String ontologyId);
	
	/**
	 * Gets the current id for ontology id.
	 *
	 * @param OntologyId the ontology id
	 * @return the current id for ontology id
	 */
	public String getCurrentIdForOntologyId(String ontologyId);
	
	public String getOntologyIdForAcronym(String acronym);
	
	public String getAcronymForOntologyId(String ontologyId);
}
