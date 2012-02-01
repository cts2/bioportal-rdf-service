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
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
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

	@Resource
	private RdfDao rdfDao;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#read(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public CodeSystemVersionCatalogEntry read(NameOrURI identifier,
			ResolvedReadContext readContext) {
		
		if(StringUtils.isNotBlank(identifier.getName())){
			String[] splitName = StringUtils.split(identifier.getName(), "_");
			Assert.noNullElements(splitName);
			Assert.isTrue(splitName.length == 2);
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("abbr", splitName[0]);
			parameters.put("version", splitName[1]);

			return this.rdfDao.selectForObject(
					CODESYSTEMVERSION_NAMESPACE, 
					GET_CODESYSTEMVERSION_BY_NAME, 
					parameters, 
					CodeSystemVersionCatalogEntry.class);
		} else {
			String[] splitDocumentUri = this.splitUri(identifier.getUri());
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			
			parameters.put("about", splitDocumentUri[0]);
			parameters.put("version", splitDocumentUri[1]);

			return this.rdfDao.selectForObject(
					CODESYSTEMVERSION_NAMESPACE, 
					GET_CODESYSTEMVERSION_BY_URI, 
					parameters, 
					CodeSystemVersionCatalogEntry.class);
		}
	}
	
	
	/**
	 * We will form document URIs like this:
	 * {codeSystemAbout}/version/{versionNumber}
	 * Example: http://purl.bioontology.org/ontology/MA.rdf/version/1.205
	 *
	 * @param uri the uri
	 * @return the string[]
	 */
	protected String[] splitUri(String uri){
		String about = StringUtils.substringBeforeLast(uri, "/version/");
		String version = StringUtils.substringAfterLast(uri, "/version/");
		
		return new String[]{ about,version };
	}


	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#exists(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.service.core.ReadContext)
	 */
	@Override
	public boolean exists(NameOrURI identifier, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean existsCodeSystemVersionForCodeSystem(NameOrURI codeSystem,
			String tagName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, String tagName) {
		throw new UnsupportedOperationException();
	}


	@Override
	public boolean existsVersionId(NameOrURI codeSystem,
			String officialResourceVersionId) {
		throw new UnsupportedOperationException();
	}


	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem, String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}


	@Override
	public CodeSystemVersionCatalogEntry getCodeSystemByVersionId(
			NameOrURI codeSystem, String officialResourceVersionId,
			ResolvedReadContext readContext) {
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		parameters.put("name", codeSystem.getName());
		parameters.put("version", officialResourceVersionId);

		return this.rdfDao.selectForObject(
				CODESYSTEMVERSION_NAMESPACE, 
				GET_CODESYSTEMVERSION_BY_NAME, 
				parameters, 
				CodeSystemVersionCatalogEntry.class);
	}

}