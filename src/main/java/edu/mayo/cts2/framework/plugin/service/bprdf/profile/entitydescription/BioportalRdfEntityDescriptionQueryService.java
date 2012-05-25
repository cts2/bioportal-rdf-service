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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.EntityReferenceList;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURIList;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.plugin.service.bprdf.common.CodeSystemVersionReferenceFactory;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.association.BioportalRdfAssociationQueryService;
import edu.mayo.cts2.framework.plugin.service.bprdf.util.SparqlUtils;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions.HierarchyRestriction.HierarchyType;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntitiesFromAssociationsQuery.EntitiesFromAssociations;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

/**
 * The Class BioportalRdfCodeSystemReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfEntityDescriptionQueryService extends AbstractService
	implements EntityDescriptionQueryService {
	
	private final static String ENTITY_NAMESPACE = "entity";
	private final static String GET_ALL_ENTITY_DESCRIPTIONS = "getAllEntityDescriptions";

	@Resource
	private BioportalRestClient bioportalRestClient;
	
	@Resource
	private CodeSystemVersionReferenceFactory codeSystemVersionReferenceFactory;
	
	@Resource
	private BioportalRestEntityDescriptionTransform bioportalRestTransform;
	
	@Resource
	private BioportalRdfAssociationQueryService bioportalRdfAssociationQueryService;
	
	@Resource
	private RdfDao rdfDao;
	
	@Resource
	private IdService idService;

	@Override
	public DirectoryResult<EntityDirectoryEntry> getResourceSummaries(
			EntityDescriptionQuery query, 
			SortCriteria sortCriteria, 
			Page page) {
		
		if(query.getEntitiesFromAssociationsQuery() != null){
			return this.handleAssociationsQuery(query, sortCriteria, page);
		}

		if(this.isHierarchyQuery(query)){
			return handleHierarchyQuery(query, sortCriteria, page);
		}
		
		String ontologyId = null;
		String id = null;
		String acronym = null;
		
		if(query != null && 
				query.getRestrictions() != null && 
				query.getRestrictions().getCodeSystemVersion() != null){
			
			CodeSystemVersionName csvName = 
					this.idService.getCodeSystemVersionNameForName(
							query.getRestrictions().getCodeSystemVersion().getName());
			
			id = csvName.getId();
			acronym = csvName.getAcronym();
			
			
			ontologyId = this.idService.getOntologyIdForId(id);
			String currentId= idService.getCurrentIdForOntologyId(ontologyId);
			//The BioPortal rdf sparql end point doesn't contain non current csv data. So
			// we return an empty list 
			if (id != null && ! id.equals(currentId)) {
				List<EntityDirectoryEntry> returnList = new ArrayList<EntityDirectoryEntry>();
				DirectoryResult<EntityDirectoryEntry> result = new DirectoryResult<EntityDirectoryEntry>(returnList, true);
                return result;
			}
		} 
		
		if(CollectionUtils.isEmpty(query.getFilterComponent())){
			return this.handleAllEntitiesOfCodeSystemVersion(id, acronym, page);
		} else {
			return this.bioportalRestTransform.successBeanToEntitySummaries(
					this.bioportalRestClient.searchEntities(ontologyId, query.getFilterComponent(), page));
		}
	}
	
	private DirectoryResult<EntityDirectoryEntry> handleAllEntitiesOfCodeSystemVersion(
			String id,
			String acronym, 
			Page page){	
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("acronym", acronym);
		SparqlUtils.setLimitOffsetParams(parameters, page);
		
		List<EntityDirectoryEntry> results = rdfDao.selectForList(
				ENTITY_NAMESPACE, 
				GET_ALL_ENTITY_DESCRIPTIONS,
				parameters,
				EntityDirectoryEntry.class);
		
		this.setCodesSystemVersionRefs(id, results);
	
		boolean moreResults = results.size() > page.getMaxToReturn();
		
		if(moreResults){
			results.remove(results.size() - 1);
		}
		
		return new DirectoryResult<EntityDirectoryEntry>(results,!moreResults);
	}
	
	private void setCodesSystemVersionRefs(String id, Iterable<EntityDirectoryEntry> entries){
		for(EntityDirectoryEntry entry : entries){
			
			CodeSystemVersionReference ref =
				this.codeSystemVersionReferenceFactory.getCodeSystemVersionReferenceFor(id);
			
			entry.getKnownEntityDescription(0).setDescribingCodeSystemVersion(ref);
		}
	}

	private DirectoryResult<EntityDirectoryEntry> handleHierarchyQuery(
			EntityDescriptionQuery query, 
			SortCriteria sortCriteria, 
			Page page) {

		HierarchyRestriction hierarchy = query.getRestrictions().getHierarchyRestriction();
		
		if(hierarchy.getHierarchyType() != HierarchyType.CHILDREN){
			throw new UnsupportedOperationException("Only CHILDREN may currently be resolved.");
		}
	
		EntityNameOrURI entity = hierarchy.getEntity();
		
		Assert.notNull(query.getRestrictions().getCodeSystemVersion(), "CodeSystemVersion restriction should be set.");
		
		EntityDescriptionReadId id = new EntityDescriptionReadId(
				entity, 
				query.getRestrictions().getCodeSystemVersion());
		
		return this.bioportalRdfAssociationQueryService.getChildrenAssociationsOfEntity(
				id, 
				query, 
				query.getReadContext(), 
				page);
	}

	private boolean isHierarchyQuery(EntityDescriptionQuery query) {
		return 
				query != null && 
				query.getRestrictions() != null && 
				query.getRestrictions().getHierarchyRestriction() != null;
	}
	

	private DirectoryResult<EntityDirectoryEntry> handleAssociationsQuery(
			EntityDescriptionQuery query, 
			SortCriteria sortCriteria, 
			Page page) {
		
		EntitiesFromAssociations type = 
			query.getEntitiesFromAssociationsQuery().getEntitiesFromAssociationsType();
		
		
		if(type != EntitiesFromAssociations.SOURCES){
			throw new UnsupportedOperationException("Only SOURCES can be resolved as Entities.");
		}
		
		return this.bioportalRdfAssociationQueryService.getSourceEntities(
				query.getEntitiesFromAssociationsQuery().getAssociationQuery().getRestrictions(), 
				query.getRestrictions(), 
				query.getReadContext(), 
				page);
	}

	@Override
	public DirectoryResult<EntityDescription> getResourceList(
			EntityDescriptionQuery query, SortCriteria sortCriteria, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int count(EntityDescriptionQuery query) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		HashSet<MatchAlgorithmReference> returnSet = new HashSet<MatchAlgorithmReference>();
		
		returnSet.add(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference());
		returnSet.add(StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference());

		return returnSet;
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSearchReferences() {
		HashSet<PropertyReference> returnSet = new HashSet<PropertyReference>();

		returnSet.add(StandardModelAttributeReference.RESOURCE_SYNOPSIS.getPropertyReference());
		
		return returnSet;
	}

	@Override
	public boolean isEntityInSet(EntityNameOrURI entity, Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQuery restrictions, 
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReferenceList resolveAsEntityReferenceList(Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQuery restrictions, 
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityNameOrURIList intersectEntityList(
			Set<EntityNameOrURI> entities, Query query,
			Set<ResolvedFilter> filterComponent,
			EntityDescriptionQuery restrictions, 
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<? extends VersionTagReference> getSupportedTags() {
		return null;
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSortReferences() {
		return null;
	}

	@Override
	public Set<PredicateReference> getKnownProperties() {
		return null;
	}

}
