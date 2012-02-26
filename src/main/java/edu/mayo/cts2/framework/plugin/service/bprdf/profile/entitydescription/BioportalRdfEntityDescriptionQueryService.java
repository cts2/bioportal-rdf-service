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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.EntityReferenceList;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURIList;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;

/**
 * The Class BioportalRdfCodeSystemReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfEntityDescriptionQueryService extends AbstractService
	implements EntityDescriptionQueryService {
	
	@Resource
	private BioportalRestClient bioportalRestClient;
	
	@Resource
	private BioportalRestEntityDescriptionTransform bioportalRestTransform;
	
	@Resource
	private IdService idService;

	@Override
	public DirectoryResult<EntityDirectoryEntry> getResourceSummaries(
			EntityDescriptionQuery query, 
			SortCriteria sortCriteria, 
			Page page) {
		
		String ontologyId = null;
		
		if(query != null && 
				query.getRestrictions() != null && 
				query.getRestrictions().getCodeSystemVersion() != null){
			
			String id = 
					this.idService.getCodeSystemVersionNameForName(
							query.getRestrictions().getCodeSystemVersion().getName()).getId();
			
			ontologyId = this.idService.getOntologyIdForId(id);
			
		}

		return this.bioportalRestTransform.successBeanToEntitySummaries(
				this.bioportalRestClient.searchEntities(ontologyId, query.getFilterComponent(), page));
	}

	@Override
	public DirectoryResult<EntityDescription> getResourceList(
			EntityDescriptionQuery query, SortCriteria sortCriteria, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(EntityDescriptionQuery query) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		HashSet<MatchAlgorithmReference> returnSet = new HashSet<MatchAlgorithmReference>();
		
		returnSet.add(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference());
		returnSet.add(StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference());

		return returnSet;
	}

	@Override
	public Set<? extends ModelAttributeReference> getSupportedModelAttributes() {
		HashSet<ModelAttributeReference> returnSet = new HashSet<ModelAttributeReference>();

		returnSet.add(StandardModelAttributeReference.RESOURCE_SYNOPSIS.getModelAttributeReference());
		
		return returnSet;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEntityInSet(EntityNameOrURI entity, Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EntityReferenceList resolveAsEntityReferenceList(
			Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityNameOrURIList intersectEntityList(
			Set<EntityNameOrURI> entities,
			Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQueryServiceRestrictions restrictions,
			ResolvedReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends VersionTagReference> getSupportedTags() {
		// TODO Auto-generated method stub
		return null;
	}

}
