package edu.mayo.cts2.framework.plugin.service.bprdf.profile.resolvedvalueset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.ncbo.stanford.bean.response.SuccessBean;
import org.ncbo.stanford.bean.search.OntologyHitBean;
import org.ncbo.stanford.bean.search.SearchBean;
import org.ncbo.stanford.bean.search.SearchResultListBean;
import org.ncbo.stanford.util.paginator.impl.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.bprdf.model.modifier.NamespaceModifier;
import edu.mayo.twinkql.result.UriParser;

@Component
public class BioportalRestResolvedValueSetTransform {
	
	@Resource
	private NamespaceModifier namespaceModifier;
	
	private UriParser uriParser = new UriParser();

	@SuppressWarnings({"unchecked","rawtypes"})
	public DirectoryResult<EntitySynopsis> successBeanToEntityEntitySynopsis(SuccessBean successBean, int expectedSize){

		List<EntitySynopsis> returnList = new ArrayList<EntitySynopsis>();
		
		List<Page> pages = (List) successBean.getData();
		for(Page p : pages){
			SearchResultListBean searchResults = (SearchResultListBean) p.getContents();
			
			List<Object> contents = (List<Object>) searchResults.get(0);
			
			Iterator<Object> itr = contents.iterator();
			
			while(itr.hasNext()){
				SearchBean searchBean = (SearchBean) itr.next();

				EntitySynopsis entry = new EntitySynopsis();

				if(searchBean == null || searchBean instanceof OntologyHitBean){
					continue;
				}

				String about = searchBean.getConceptId();
				
				Assert.hasText(about, "Entity:" + searchBean.getConceptIdShort() + " has no URI.");

				entry.setUri(about);
				entry.setName(this.uriParser.getLocalPart(about));
				entry.setNamespace(this.namespaceModifier.getNamespace(this.uriParser.getNamespace(about)));
				
				entry.setDesignation(searchBean.getPreferredName());
				
				returnList.add(entry);
			}
		}
		
		boolean isComplete = returnList.size() < expectedSize;
		
		if(!isComplete){
			returnList.remove(returnList.size() - 1);
		}
		
		DirectoryResult<EntitySynopsis> result = new DirectoryResult<EntitySynopsis>(returnList, isComplete);
		
		return result;
	}
}
