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
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.EntityReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDescription;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.entity.EntityListEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.bprdf.callback.common.Result;
import edu.mayo.cts2.framework.plugin.service.bprdf.common.CodeSystemVersionReferenceFactory;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier.NamespaceModifier;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractService;
import edu.mayo.cts2.framework.plugin.service.bprdf.util.EntityUriLookupService;
import edu.mayo.cts2.framework.plugin.service.bprdf.util.UriUtils;
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
	private final static String GET_AVAILABLE_DESCRIPTIONS = "getAvailableDescriptions";
	private final static String GET_DESIGNATION = "getDesignation";
	
	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Resource
	private NamespaceModifier namespaceModifier;
	
	@Resource
	private CodeSystemVersionReferenceFactory codeSystemVersionReferenceFactory;
	
	@Resource
	private RdfDao rdfDao;
	
	@Resource
	private UrlConstructor urlConstructor;

	@Resource
	private IdService idService;


	@Resource
	private EntityUriLookupService entityUriLookupService;

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
			uri= entityUriLookupService.getUriFromScopedEntityName(ontologyId, identifier.getEntityName());
		} else {
			uri = identifier.getUri();
		}
		
		if(StringUtils.isBlank(uri)){
			return null;
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uri", uri);
		
		if(StringUtils.isNotBlank(ontologyId)){
			parameters.put("ontologyId", ontologyId);
			
			String codeSystemName = this.idService.getAcronymForOntologyId(ontologyId);
			parameters.put("acronym", codeSystemName);
		}

		long start = System.currentTimeMillis();
		EntityDescription entity = this.rdfDao.selectForObject(ENTITY_NAMESPACE, GET_ENTITY_BY_URI,
				parameters, EntityDescription.class);
		log.info("Query TripleStore time: " + ( System.currentTimeMillis() - start ));

		return entity;
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

	@Override
	public DirectoryResult<EntityListEntry> readEntityDescriptions(
			EntityNameOrURI entityId, SortCriteria sortCriteria,
			ResolvedReadContext readContext, Page page) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityReference availableDescriptions(
			EntityNameOrURI entityId,
			ResolvedReadContext readContext) {
		final String entityUri;
		
		if(StringUtils.isNotBlank(entityId.getUri())){
			entityUri = entityId.getUri();
		} else {
			entityUri = entityUriLookupService.getUriFromScopedEntityName(null, entityId.getEntityName());
		}
		
		if(StringUtils.isBlank(entityUri)){
			return null;
		}
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uri", entityUri);

		long start = System.currentTimeMillis();
		
		@SuppressWarnings("rawtypes")
		List<Result> ids = this.rdfDao.selectForList(
				ENTITY_NAMESPACE, 
				GET_AVAILABLE_DESCRIPTIONS,
				parameters, Result.class);
		
		log.info("Query TripleStore time: " + ( System.currentTimeMillis() - start ));
	
		if(CollectionUtils.isNotEmpty(ids)){
			final EntityReference ref = new EntityReference();

			String[] names = UriUtils.getNamespaceNameTuple(entityUri);
			
			final String name = names[0];
			String namespaceUri = names[1];
			String namespaceName = this.namespaceModifier.getNamespace(namespaceUri);
		
			ref.setName(
					ModelUtils.createScopedEntityName(name, namespaceName));
			ref.setAbout(entityUri);
			
			List<Callable<Void>> callables = new ArrayList<Callable<Void>>();
			
			for(final Result<String> id : ids){
				final DescriptionInCodeSystem description = new DescriptionInCodeSystem();
				
				description.setDescribingCodeSystemVersion(
					codeSystemVersionReferenceFactory.getCodeSystemVersionReferenceFor(id.getResult()));
				
				ref.addKnownEntityDescription(description);
				
				final String acronym =
					description.getDescribingCodeSystemVersion().
						getCodeSystem().
							getContent();
				
				//Resolve all the descriptions at once in separate threads.
				//... may need to throttle this back if the server doesn't
				//handle it well.
				Callable<Void> callable = new Callable<Void>(){

					@Override
					public Void call() throws Exception {
						Map<String, Object> designationParams = new HashMap<String, Object>();
						designationParams.put("uri", entityUri);
						designationParams.put("acronym", acronym);
						
						DesignationResult designation = 
							rdfDao.selectForObject(
									ENTITY_NAMESPACE, 
									GET_DESIGNATION, 
									designationParams, 
									DesignationResult.class);
						
						if(designation == null){
							//if it doesn't assert a preferred designation, set it blank
							description.setDesignation("");
						} else {
							description.setDesignation(designation.getDesignation());
						}
						
						CodeSystemVersionReference csvRef = description.getDescribingCodeSystemVersion();
						description.setHref(
							urlConstructor.createEntityUrl(
								csvRef.getCodeSystem().getContent(), 
								csvRef.getVersion().getContent(), 
								ref.getName()));
							
						return null;
					}
					
				};
				
				callables.add(callable);
			}
			
			try {
				this.executorService.invokeAll(callables);
				
				return ref;
			} catch (InterruptedException e) {
				throw new IllegalStateException();
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean exists(EntityDescriptionReadId identifier,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityList readEntityDescriptions(
			EntityNameOrURI entityId,
			ResolvedReadContext readContext) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<CodeSystemReference> getKnownCodeSystems() {
		return null;
	}

	@Override
	public List<CodeSystemVersionReference> getKnownCodeSystemVersions() {
		return null;
	}

	@Override
	public List<VersionTagReference> getSupportedVersionTags() {
		return null;
	}

}
