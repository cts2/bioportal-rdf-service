package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.ResultSet;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.transform.CodeSystemTransform;
import edu.mayo.cts2.framework.service.profile.ResourceQuery;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemQueryService;

@Component
public class BioportalRdfCodeSystemQueryService implements
		CodeSystemQueryService {
	
	private final static String CODESYSTEM_NAMESPACE = "codeSystem";
	private final static String GET_CODESYSTEM_SUMMARIES = "getCodeSystemCatalogSummaries";
	
	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";
	
	@Resource
	private CodeSystemTransform codeSystemTransform;
	
	@Resource
	private RdfDao rdfDao;

	@Override
	public DirectoryResult<CodeSystemCatalogEntrySummary> getResourceSummaries(
			ResourceQuery query, 
			SortCriteria sortCriteria, 
			Page page) {
		List<CodeSystemCatalogEntrySummary> resultList = new ArrayList<CodeSystemCatalogEntrySummary>();
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put(LIMIT, page.getMaxToReturn());
		parameters.put(OFFSET, page.getStart());
		
		ResultSet results;
		try {
			results = rdfDao.query(
					CODESYSTEM_NAMESPACE, 
					GET_CODESYSTEM_SUMMARIES,
					parameters);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		while(results.hasNext()){
			resultList.add(this.codeSystemTransform.transform(results.next()));
		}
		
		return new DirectoryResult<CodeSystemCatalogEntrySummary>(resultList,true,true);
	}

	@Override
	public DirectoryResult<CodeSystemCatalogEntry> getResourceList(
			ResourceQuery query, SortCriteria sortCriteria, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count(ResourceQuery query) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends ModelAttributeReference> getSupportedModelAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
