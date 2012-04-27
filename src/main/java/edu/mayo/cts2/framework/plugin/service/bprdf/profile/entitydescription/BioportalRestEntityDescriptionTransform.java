package edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.xerces.util.XMLChar;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.ncbo.stanford.bean.search.OntologyHitBean;
import org.ncbo.stanford.bean.search.SearchBean;
import org.ncbo.stanford.bean.search.SearchResultListBean;
import org.ncbo.stanford.util.paginator.impl.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.core.url.UrlConstructor;
import edu.mayo.cts2.framework.model.core.CodeSystemReference;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.model.core.DescriptionInCodeSystem;
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;


@Component
public class BioportalRestEntityDescriptionTransform {
	
	@Resource
	private IdService idService;
	
	@Resource
	private UrlConstructor urlConstructor;

	@SuppressWarnings({"unchecked","rawtypes"})
	public DirectoryResult<EntityDirectoryEntry> successBeanToEntitySummaries(SuccessBean successBean){

		List<EntityDirectoryEntry> returnList = new ArrayList<EntityDirectoryEntry>();
		
		List<Page> pages = (List) successBean.getData();
		for(Page p : pages){
			SearchResultListBean searchResults = (SearchResultListBean) p.getContents();
			
			List<Object> contents = (List<Object>) searchResults.get(0);
			
			Iterator<Object> itr = contents.iterator();
			
			while(itr.hasNext()){
				SearchBean searchBean = (SearchBean) itr.next();
				
				EntityDirectoryEntry entry = new EntityDirectoryEntry();

				if(searchBean == null || searchBean instanceof OntologyHitBean){
					continue;
				}
				
				String ontologyId = Integer.toString(searchBean.getOntologyId());
				String id = Integer.toString(searchBean.getOntologyVersionId());
				String shortId = searchBean.getConceptIdShort();
				
				String about = searchBean.getConceptId();
				
				Assert.hasText(about, "Entity:" + searchBean.getConceptIdShort() + " has no URI.");
				
				entry.setAbout(about);
				
				DescriptionInCodeSystem description = new DescriptionInCodeSystem();
				
				CodeSystemVersionReference ref = this.getCodeSystemVersionReference(ontologyId, id);
				
				//If this is null, no info was found about that id in the triple store.
				//Probably a REST/Triplestore content mismatch.
				if(ref == null){
					continue;
				}
				
				description.setDescribingCodeSystemVersion(ref);
				entry.addKnownEntityDescription(description);
				entry.getKnownEntityDescription(0).setDesignation(searchBean.getPreferredName());
				
				String codeSystemName = description.getDescribingCodeSystemVersion().getCodeSystem().getContent();
				
				ScopedEntityName scopedEntityName = this.buildScopedEntityName(shortId, codeSystemName);
				
				entry.setHref(this.getEntityHref(ontologyId, id, scopedEntityName.getName()));
				
				entry.setName(scopedEntityName);
				
				returnList.add(entry);
			}
		}
		
		DirectoryResult<EntityDirectoryEntry> result = new DirectoryResult<EntityDirectoryEntry>(returnList, true);
		
		return result;
	}
	
	protected ScopedEntityName buildScopedEntityName(String name, String codeSystemName){
		ScopedEntityName scopedName = new ScopedEntityName();

		String[] namePartsColon = StringUtils.split(name, ':');
		String[] namePartsHash = StringUtils.split(name, '#');

		String[] nameParts;
		if(namePartsColon.length > namePartsHash.length){
			nameParts = namePartsColon;
		} else {
			nameParts = namePartsHash;
		}

		if(nameParts.length == 1){
			scopedName.setName(nameParts[0]);
			scopedName.setNamespace(codeSystemName);
		} else {
			boolean isNamespaceValidNCName = 
					XMLChar.isValidNCName(nameParts[0]);
			if(isNamespaceValidNCName){
				scopedName.setNamespace(nameParts[0]);
			} else {
				scopedName.setNamespace(codeSystemName);
			}
			scopedName.setName(nameParts[1]);	
		}

		return this.sanitizeNcNameNamespace(scopedName);
	}

	private ScopedEntityName sanitizeNcNameNamespace(ScopedEntityName scopedName) {
		if(! XMLChar.isValidNCName(scopedName.getNamespace())){
			scopedName.setNamespace("ns" + Integer.toString(
				scopedName.getNamespace().hashCode()));
		}
		
		return scopedName;
	}
	
	protected String getEntityHref(String ontologyId, String id, String entityName){
		CodeSystemVersionName codeSystemVersionName = this.idService.getCodeSystemVersionNameForId(id);
		
		//the triple store doesn't have all of these..
		if(codeSystemVersionName == null){
			return null;
		}
		
		String versionName = codeSystemVersionName.getName();
		String codeSystemName = codeSystemVersionName.getAcronym();
		
		return this.urlConstructor.createEntityUrl(codeSystemName, versionName, entityName);
	}
	
	protected CodeSystemVersionReference getCodeSystemVersionReference(String ontologyId, String id){
		CodeSystemVersionReference versionRef = new CodeSystemVersionReference();
		
		CodeSystemVersionName codeSystemVersionName = this.idService.getCodeSystemVersionNameForId(id);
		
		//the triple store doesn't have all of these..
		if(codeSystemVersionName == null){
			return null;
		}
		
		String versionName = codeSystemVersionName.getName();
		String codeSystemName = codeSystemVersionName.getAcronym();
		
		NameAndMeaningReference ref = new NameAndMeaningReference();
		ref.setContent(versionName);
		ref.setHref(this.urlConstructor.createCodeSystemVersionUrl(codeSystemName, versionName));
		
		versionRef.setVersion(ref);
		
		versionRef.setCodeSystem(this.getCodeSystemReference(codeSystemName));
		
		return versionRef;
	}
	
	private CodeSystemReference getCodeSystemReference(String codeSystemName){
		CodeSystemReference codeSystemReference = new CodeSystemReference();
		codeSystemReference.setContent(codeSystemName);
		codeSystemReference.setHref(this.urlConstructor.createCodeSystemUrl(codeSystemName));
		
		return codeSystemReference;
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public String successBeanToEntityUri(SuccessBean successBean){

		List<Page> pages = (List) successBean.getData();
		for(Page p : pages){
			if(p.getNumResultsTotal() == 0){
				return null;
			}
			Iterator<List> itr = p.getContents().iterator();
			while(itr.hasNext()){
				List bean = itr.next();
				
				for(Object hit : bean){

					SearchBean searchBean = (SearchBean)hit;
					
					return searchBean.getConceptId();
				}
			}
		}

		return null;
	}
}
