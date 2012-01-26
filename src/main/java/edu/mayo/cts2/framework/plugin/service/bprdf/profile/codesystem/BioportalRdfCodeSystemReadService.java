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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

/**
 * The Class BioportalRdfCodeSystemReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfCodeSystemReadService implements CodeSystemReadService {

	private final static String CODESYSTEM_NAMESPACE = "codeSystem";
	private final static String GET_CODESYSTEM_BY_URI = "getCodeSystemByUri";
	private final static String GET_CODESYSTEM_BY_NAME = "getCodeSystemByName";

	@Resource
	private RdfDao rdfDao;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#read(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public CodeSystemCatalogEntry read(NameOrURI identifier,
			ResolvedReadContext readContext) {
		
		if(StringUtils.isNotBlank(identifier.getName())){
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("name", identifier.getName());

			return this.rdfDao.selectForObject(
					CODESYSTEM_NAMESPACE, 
					GET_CODESYSTEM_BY_NAME, 
					parameters, 
					CodeSystemCatalogEntry.class);
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService#exists(edu.mayo.cts2.framework.model.service.core.NameOrURI, edu.mayo.cts2.framework.model.service.core.ReadContext)
	 */
	@Override
	public boolean exists(NameOrURI identifier, ReadContext readContext) {
		throw new UnsupportedOperationException();
	}

}
