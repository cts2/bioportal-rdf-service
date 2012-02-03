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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystemversion;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem.CodeSystemName;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService;

/**
 * The Class BioportalRdfCodeSystemReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfCodeSystemVersionReadService implements CodeSystemVersionReadService {

	private final static String CODESYSTEMVERSION_NAMESPACE = "codeSystemVersion";
	private final static String GET_CODESYSTEMVERSION_BY_URI = "getCodeSystemVersionByUri";
	private final static String GET_CODESYSTEMVERSION_BY_NAME = "getCodeSystemVersionByName";
	private final static String GET_CODESYSTEMVERSION_BY_CODE_SYSTEM_NAME_AND_VERSION_ID = "getCodeSystemVersionByCodeSystemNameAndVersionId";

	@Resource
	private RdfDao rdfDao;
	
	@Resource
	private IdService idService;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#read(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public CodeSystemVersionCatalogEntry read(NameOrURI identifier,
			ResolvedReadContext readContext) {
		
		if(StringUtils.isNotBlank(identifier.getName())){
			CodeSystemVersionName name = CodeSystemVersionName.parse(identifier.getName());
			
			String ontologyId = this.idService.getOntologyIdForId(name.getId());
			
			if(StringUtils.isBlank(ontologyId)){
				//not found
				return null;
			}
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("id", name.getId());
			parameters.put("ontologyId", ontologyId);
			
			return this.rdfDao.selectForObject(
					CODESYSTEMVERSION_NAMESPACE, 
					GET_CODESYSTEMVERSION_BY_NAME, 
					parameters, 
					CodeSystemVersionCatalogEntry.class);
		} else {
			String id = StringUtils.substringAfterLast(identifier.getUri(), "/");
			String ontologyId = this.idService.getOntologyIdForId(id);
			
			if(StringUtils.isBlank(ontologyId)){
				//no ontologyId found -- means an invalid id
				return null;
			}
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			
			parameters.put("ontologyId", ontologyId);
			parameters.put("id", id);

			return this.rdfDao.selectForObject(
					CODESYSTEMVERSION_NAMESPACE, 
					GET_CODESYSTEMVERSION_BY_URI, 
					parameters, 
					CodeSystemVersionCatalogEntry.class);
		}
	}
	

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#exists(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.service.core.ReadContext)
	 */
	@Override
	public boolean exists(NameOrURI identifier, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}


	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService#existsCodeSystemVersionForCodeSystem(edu.mayo.cts2.framework.model.service.core.NameOrURI, java.lang.String)
	 */
	@Override
	public boolean existsCodeSystemVersionForCodeSystem(NameOrURI codeSystem,
			String tagName) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService#getCodeSystemVersionForCodeSystem(edu.mayo.cts2.framework.model.service.core.NameOrURI, java.lang.String)
	 */
	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, String tagName) {
		throw new UnsupportedOperationException();
	}


	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService#existsVersionId(edu.mayo.cts2.framework.model.service.core.NameOrURI, java.lang.String)
	 */
	@Override
	public boolean existsVersionId(NameOrURI codeSystem,
			String officialResourceVersionId) {
		throw new UnsupportedOperationException();
	}


	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService#getCodeSystemVersionForCodeSystem(edu.mayo.cts2.framework.model.service.core.NameOrURI, java.lang.String, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}


	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionReadService#getCodeSystemByVersionId(edu.mayo.cts2.framework.model.service.core.NameOrURI, java.lang.String, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
			NameOrURI codeSystem, 
			String officialResourceVersionId,
			ResolvedReadContext readContext) {
		
		if(StringUtils.isNotBlank(codeSystem.getName())){
			CodeSystemName name = CodeSystemName.parse(codeSystem.getName());
			
			String ontologyId = name.getOntologyId();
			
			if(StringUtils.isBlank(ontologyId)){
				//not found
				return null;
			}
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("versionId", officialResourceVersionId);
			parameters.put("ontologyId", ontologyId);
	

		return this.rdfDao.selectForObject(
				CODESYSTEMVERSION_NAMESPACE, 
				GET_CODESYSTEMVERSION_BY_CODE_SYSTEM_NAME_AND_VERSION_ID, 
				parameters, 
				CodeSystemVersionCatalogEntry.class);
		} else {
			throw new UnsupportedOperationException("Cannot lookup a CodeSystemVersion by URI AND VersionId.");
		}
	}

}
