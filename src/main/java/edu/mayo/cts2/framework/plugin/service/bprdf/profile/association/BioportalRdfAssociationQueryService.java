package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemName;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder.VariableQuery;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableTiedModelAttributeReference;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;
@Component
public class BioportalRdfAssociationQueryService extends AbstractQueryService implements  AssociationQueryService{
	private final static String ASSOCIATION_NAMESPACE = "association";
	private final static String GET_ASSOCIATION_SUMMARIES = "getAssociationDirectoryEntrySummaries";
	
	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";

	@Resource
	private RdfDao rdfDao;




	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#getResourceSummaries(edu.mayo.cts2.framework.service.profile.ResourceQuery, edu.mayo.cts2.framework.model.core.SortCriteria, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			AssociationQuery query, SortCriteria sortCriteria, Page page) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put(LIMIT, page.getMaxToReturn()+1);
		parameters.put(OFFSET, page.getStart());

		String codeSystemVersionRestriction = "?codeSystemVersionRestriction";
		
		VariableQueryBuilder builder = new VariableQueryBuilder();
		
		if(query != null){
			for(ResolvedFilter filter : query.getFilterComponent()){
				ModelAttributeReference modelRef = filter.getModelAttributeReference();
				
				VariableTiedModelAttributeReference variableModelRef = this.findSupportedModelAttribute(modelRef);
				
				builder = builder.addQuery(variableModelRef.getVariable(), filter.getMatchValue());
			}
		}
		
		VariableQuery variableQuery = builder.build();
		
		parameters.put("filters", variableQuery);
		
		if(query != null && query.getRestrictions() != null) {
			NameOrURI codeSystem = query.getRestrictions().getCodeSystemVersion();
		
			if(codeSystem != null){
				if(StringUtils.isNotBlank(codeSystem.getName())){
					CodeSystemName name = CodeSystemName.parse(codeSystem.getName());
					
					codeSystemVersionRestriction = name.getOntologyId();
				}
			}
		}
		
		parameters.put("codeSystemVersionRestriction", codeSystemVersionRestriction);
		
		List<AssociationDirectoryEntry> results;
		
			results = rdfDao.selectForList(
					ASSOCIATION_NAMESPACE, 
					GET_ASSOCIATION_SUMMARIES,
					parameters,
					AssociationDirectoryEntry.class);
		
		boolean moreResults = results.size() > page.getMaxToReturn();
		
		if(moreResults){
			results.remove(results.size() - 1);
		}
		
		return new DirectoryResult<AssociationDirectoryEntry>(results,!moreResults);

	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#getResourceList(edu.mayo.cts2.framework.service.profile.ResourceQuery, edu.mayo.cts2.framework.model.core.SortCriteria, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<Association> getResourceList(AssociationQuery query,
			SortCriteria sortCriteria, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#count(edu.mayo.cts2.framework.service.profile.ResourceQuery)
	 */
	@Override
	public int count(AssociationQuery query) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getChildrenAssociationsOfEntity(edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			EntityDescriptionReadId entity, EntityDescriptionQuery query,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getChildrenAssociationsOfEntityList(edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntityList(
			EntityDescriptionReadId entity, EntityDescriptionQuery query,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getSourceEntities(edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions, edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getSourceEntities(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getSourceEntitiesList(edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions, edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityList> getSourceEntitiesList(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getTargetEntities(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getTargetEntitiesList(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityList> getTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getAllSourceAndTargetEntities(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getAllSourceAndTargetEntities(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getAllSourceAndTargetEntitiesList(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityList> getAllSourceAndTargetEntitiesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getPredicates(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityDirectoryEntry> getPredicates(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getPredicatesList(edu.mayo.cts2.framework.service.profile.association.AssociationQuery, edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery, edu.mayo.cts2.framework.model.command.ResolvedReadContext, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<EntityList> getPredicatesList(
			AssociationQuery associationQuery,
			EntityDescriptionQuery entityDescriptionQuery,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.association.AssociationQueryService#getAssociationGraph(edu.mayo.cts2.framework.model.association.types.GraphFocus, edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId, edu.mayo.cts2.framework.model.association.types.GraphDirection, long)
	 */
	@Override
	public DirectoryResult<GraphNode> getAssociationGraph(GraphFocus focusType,
			EntityDescriptionReadId focusEntity, GraphDirection direction,
			long depth) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService#doAddSupportedModelAttributes(java.util.Set)
	 */
	@Override
	public void doAddSupportedModelAttributes(
			Set<edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableTiedModelAttributeReference> set) {
		VariableTiedModelAttributeReference name = 
				new VariableTiedModelAttributeReference(
						StandardModelAttributeReference.RESOURCE_NAME, "acronym");
		
		VariableTiedModelAttributeReference description = 
				new VariableTiedModelAttributeReference(
						StandardModelAttributeReference.RESOURCE_SYNOPSIS, "description");
		
		VariableTiedModelAttributeReference about = 
				new VariableTiedModelAttributeReference(
						StandardModelAttributeReference.ABOUT, "ontologyId");

		set.add(name);
		set.add(description);
		set.add(about);
	}
}
