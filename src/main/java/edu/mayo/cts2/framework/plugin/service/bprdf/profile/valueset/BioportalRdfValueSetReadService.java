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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.valueset;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService;

/**
 * The Class BioportalRdfCodeSystemReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfValueSetReadService extends AbstractService 
	implements ValueSetReadService {

	private final static String VALUESET_NAMESPACE = "valueSet";

	private final static String GET_VALUESET_BY_NAME = "getValueSetByName";

	@Resource
	private RdfDao rdfDao;
	
	@Resource
	private IdService idService;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#read(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public ValueSetCatalogEntry read(NameOrURI identifier,
			ResolvedReadContext readContext) {
		
		String ontologyId;
		
		if(StringUtils.isNotBlank(identifier.getName())){
			String name = identifier.getName();
			
			ontologyId = this.idService.getOntologyIdForAcronym(name);

		} else {
			String uri = identifier.getUri();
			
			ontologyId = this.getOntologyIdFromUri(uri);
		}
		
		String id = this.idService.getCurrentIdForOntologyId(ontologyId);
		if(StringUtils.isBlank(id)){
			//not found
			return null;
		}
			
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", id);
		parameters.put("ontologyId", ontologyId);

		return this.rdfDao.selectForObject(
				VALUESET_NAMESPACE, 
				GET_VALUESET_BY_NAME, 
				parameters, 
				ValueSetCatalogEntry.class);
	}
	
	/**
	 * Gets the ontology id from uri.
	 *
	 * @param uri the uri
	 * @return the ontology id from uri
	 */
	private String getOntologyIdFromUri(String uri){
		String acronym = StringUtils.substringAfterLast(uri, "/");
		
		return this.idService.getOntologyIdForAcronym(acronym);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#exists(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.service.core.ReadContext)
	 */
	@Override
	public boolean exists(NameOrURI identifier, ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

}
