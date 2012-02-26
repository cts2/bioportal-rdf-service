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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

/**
 * The Class BioportalRdfCodeSystemReadService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfEntityDescriptionReadService extends AbstractService
		implements EntityDescriptionReadService {

	private final static String ENTITY_NAMESPACE = "entity";
	private final static String GET_ENTITY_BY_URI = "getEntityDescriptionByUri";

	@Resource
	private RdfDao rdfDao;

	@Resource
	private IdService idService;

	@Resource
	private BioportalRestClient bioportalRestClient;

	@Resource
	private BioportalRestEntityDescriptionTransform bioportalRestTransform;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService
	 * #read(edu.mayo.cts2.framework.model.service.core.NameOrURI,
	 * edu.mayo.cts2.framework.model.command.ResolvedReadContext)
	 */
	@Override
	public EntityDescription read(EntityDescriptionReadId identifier,
			ResolvedReadContext readContext) {
		
		String ontologyId = null;
		
		if(identifier.getCodeSystemVersion() != null){
			String csvName = identifier.getCodeSystemVersion().getName();
			
			String id = CodeSystemVersionName.parse(csvName).getId();
			
			ontologyId = this.idService.getOntologyIdForId(id);
		}

		String uri;

		if (identifier.getEntityName() != null) {
			String name = identifier.getEntityName().getName();

			Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
			ResolvedFilter filter = new ResolvedFilter();
			filter.setMatchAlgorithmReference(StandardMatchAlgorithmReference.CONTAINS
					.getMatchAlgorithmReference());
			filter.setMatchValue(name);
			filters.add(filter);

			SuccessBean success = bioportalRestClient.searchEntities(ontologyId,
					filters, new Page());

			uri = this.bioportalRestTransform.successBeanToEntityUri(success);
		} else {
			uri = identifier.getUri();
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uri", uri);
		
		if(StringUtils.isNotBlank(ontologyId)){
			parameters.put("ontologyId", ontologyId);
		}

		return this.rdfDao.selectForObject(ENTITY_NAMESPACE, GET_ENTITY_BY_URI,
				parameters, EntityDescription.class);

	}

	protected CodeSystemVersionName getCodeSystemVersionNameFromCodeSystemVersionNameOrUri(
			NameOrURI codeSystemVersion) {
		if (StringUtils.isNotBlank(codeSystemVersion.getName())) {
			String csvName = codeSystemVersion.getName();

			return this.idService.getCodeSystemVersionNameForName(csvName);
		} else {
			throw new UnsupportedOperationException(
					"CodeSytemVersion must be a Name, not a URI -- not implemented yet.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService
	 * #exists(edu.mayo.cts2.framework.model.service.core.NameOrURI,
	 * edu.mayo.cts2.framework.model.service.core.ReadContext)
	 */
	@Override
	public boolean exists(EntityDescriptionReadId identifier,
			ReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityDescription readByCodeSystem(EntityNameOrURI entityId,
			NameOrURI codeSystem, String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean existsInCodeSystem(EntityNameOrURI entityId,
			NameOrURI codeSystem, String tagName,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<EntityListEntry> readEntityDescriptions(
			EntityNameOrURI entityId, SortCriteria sortCriteria,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReference availableDescriptions(EntityNameOrURI entityId,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

}
