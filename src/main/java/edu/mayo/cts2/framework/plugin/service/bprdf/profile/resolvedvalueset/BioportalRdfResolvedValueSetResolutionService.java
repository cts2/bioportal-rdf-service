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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.resolvedvalueset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet;
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.plugin.service.bprdf.util.SparqlUtils;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetResolutionService;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;

/**
 * The Class BioportalRdfResolvedValueSetResolutionService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfResolvedValueSetResolutionService extends AbstractService 
	implements ResolvedValueSetResolutionService {
	
	private final static String RESOLVEDVALUESET_NAMESPACE = "resolvedValueSet";
	private final static String GET_RESOLVEDVALUESET_ENTITYSYNOPSIS = "getAllEntitySynonpsisOfValueSet";
	private final static String GET_RESOLVEDVALUESET_HEADER = "getResolvedValueSetHeader";
	@Resource
	private RdfDao rdfDao;
	
	@Resource 
	private BioportalRestClient bioportalRestClient;
	
	@Resource
	private IdService idService;
	
	@Resource
	private BioportalRestResolvedValueSetTransform bioportalRestResolvedValueSetTransform;

	@Override
	public ResolvedValueSetResult getResolution(
			ResolvedValueSetReadId identifier,
			Set<ResolvedFilter> filterComponent, 
			Page page) {
		
		String id = identifier.getLocalName();
		String ontologyId = this.idService.getOntologyIdForId(identifier.getLocalName());
		String acronym = identifier.getValueSet().getName();
		String expectedAcronym = this.idService.getAcronymForOntologyId(ontologyId);
		
		//acronym mismatch - this is caused if the 'valueSet' restriction is wrong.
		if(! acronym.equals(expectedAcronym)){
			return null;
		}
		
		if(CollectionUtils.isEmpty(filterComponent)){
			
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("ontologyId", ontologyId);
			parameters.put("id", id);
			parameters.put("acronym", acronym);
			
			SparqlUtils.setLimitOffsetParams(parameters, page);
			
			List<EntitySynopsis> results = 
				this.rdfDao.selectForList(
						RESOLVEDVALUESET_NAMESPACE, 
						GET_RESOLVEDVALUESET_ENTITYSYNOPSIS, 
						parameters, 
						EntitySynopsis.class);
			
			if(results == null){
				return null;
			}
			
			boolean moreResults = results.size() > page.getMaxToReturn();
			
			if(moreResults){
				results.remove(results.size() - 1);
			}
					
			return new ResolvedValueSetResult(
					this.getResolvedValueSetHeader(id), 
					results, 
					!moreResults);
		} else {
			SuccessBean successBean = 
				this.bioportalRestClient.searchEntities(ontologyId, filterComponent, page);
			
			DirectoryResult<EntitySynopsis> result = 
				this.bioportalRestResolvedValueSetTransform.successBeanToEntityEntitySynopsis(successBean);
			
			return new ResolvedValueSetResult(
					this.getResolvedValueSetHeader(id), 
					result.getEntries(), 
					result.isAtEnd());
		}
	}
	
	protected ResolvedValueSetHeader getResolvedValueSetHeader(String id){
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", id);
		
		List<ResolvedValueSetHeader> list = this.rdfDao.selectForList(
					RESOLVEDVALUESET_NAMESPACE, 
					GET_RESOLVEDVALUESET_HEADER, 
					parameters, 
					ResolvedValueSetHeader.class);
		
		Assert.isTrue(list != null && list.size() == 1, 
					"Error finding ResolvedValueSetHeader for: " +
						id);
		
		return list.get(0);
	}

	@Override
	public ResolvedValueSet getResolution(
			ResolvedValueSetReadId identifier) {
		throw new UnsupportedOperationException("Cannot resolve the complete ResolvedValueSet yet...");
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSortReferences() {
		return null;
	}

	@Override
	public Set<PredicateReference> getKnownProperties() {
		return null;
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

	
}
