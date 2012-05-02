package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import java.util.ArrayList;
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
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.CodeSystemVersionName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder.VariableQuery;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableTiedPropertyReference;
import edu.mayo.cts2.framework.plugin.service.bprdf.util.EntityUriLookupService;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

@Component
public class BioportalRdfAssociationQueryService extends AbstractQueryService
		implements AssociationQueryService {
	private final static String ASSOCIATION_NAMESPACE = "association";
	private final static String GET_ASSOCIATION_SUMMARIES = "getAssociationDirectoryEntrySummaries";
	private final static String GET_CHILDREN_ASSOCIATION_OF_ENTITY = "getChildrenAssociationsOfEntity";
	private final static String GET_SOURCE_ENTITIES = "getSourceEntities";

	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";

	@Resource
	private RdfDao rdfDao;

	@Resource
	private IdService idService;
	@Resource
	private EntityUriLookupService entityUriLookupService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.QueryService#getResourceSummaries
	 * (edu.mayo.cts2.framework.service.profile.ResourceQuery,
	 * edu.mayo.cts2.framework.model.core.SortCriteria,
	 * edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			AssociationQuery query, SortCriteria sortCriteria, Page page) {
        String ontologyId= null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LIMIT, page.getMaxToReturn() + 1);
		parameters.put(OFFSET, page.getStart());

		VariableQueryBuilder builder = new VariableQueryBuilder();

		if (query != null) {
			for (ResolvedFilter filter : query.getFilterComponent()) {
				PropertyReference modelRef = filter.getPropertyReference();

				VariableTiedPropertyReference variableModelRef = this
						.findSupportedModelAttribute(modelRef);

				builder = builder.addQuery(variableModelRef.getVariable(),
						filter.getMatchValue());
			}
		}

		VariableQuery variableQuery = builder.build();

		parameters.put("filters", variableQuery);

		if (query != null && query.getRestrictions() != null) {
			parameterizeAssociationRestriction(query.getRestrictions(), parameters );
		}

		List<AssociationDirectoryEntry> results;

		results = rdfDao.selectForList(ASSOCIATION_NAMESPACE,
				GET_ASSOCIATION_SUMMARIES, parameters,
				AssociationDirectoryEntry.class);

		boolean moreResults = results.size() > page.getMaxToReturn();

		if (moreResults) {
			results.remove(results.size() - 1);
		}

		return new DirectoryResult<AssociationDirectoryEntry>(results,
				!moreResults);

	}

	private void addRestriction(String ontologyId, String restrictionType,
			EntityNameOrURI restrictEntity, Map<String, Object> parameters) {
		if (restrictEntity != null) {
			String restrictionUri= null;
			String restrictionName= null;
			if (StringUtils.isNotBlank(restrictEntity.getUri())) {
				restrictionUri = restrictEntity.getUri();
			} else if (restrictEntity.getEntityName() != null) {
				 restrictionUri= entityUriLookupService.getUriFromScopedEntityName(ontologyId, restrictEntity.getEntityName());			 
				 restrictionName = restrictEntity.getEntityName()
						.getName();
				 
			}
			if (StringUtils.isNotBlank(restrictionUri)) {
				parameters.put(restrictionType + "Uri", restrictionUri);
			}
			else if (StringUtils.isNotBlank(restrictionName)) {
				parameters.put(restrictionType + "Name", restrictionName);
							
			}
			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.QueryService#getResourceList(
	 * edu.mayo.cts2.framework.service.profile.ResourceQuery,
	 * edu.mayo.cts2.framework.model.core.SortCriteria,
	 * edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<Association> getResourceList(AssociationQuery query,
			SortCriteria sortCriteria, Page page) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.QueryService#count(edu.mayo.cts2
	 * .framework.service.profile.ResourceQuery)
	 */
	@Override
	public int count(AssociationQuery query) {
		throw new UnsupportedOperationException();
	}

	public DirectoryResult<EntityDirectoryEntry> getChildrenAssociationsOfEntity(
			EntityDescriptionReadId entity, EntityDescriptionQuery query,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LIMIT, page.getMaxToReturn() + 1);
		parameters.put(OFFSET, page.getStart());

		NameOrURI codeSystemVersion = entity.getCodeSystemVersion();

		if (codeSystemVersion != null) {
			if (StringUtils.isNotBlank(codeSystemVersion.getName())) {
				CodeSystemVersionName name = CodeSystemVersionName
						.parse(codeSystemVersion.getName());

				parameters.put("restrictToGraph", name.getAcronym());
				parameters.put("restrictToCodeSystemVersion",
						codeSystemVersion.getName());

			}
		}
		if (entity.getEntityName() != null) {
			String entityName = entity.getEntityName().getName();
			if (StringUtils.isNotBlank(entityName)) {
				parameters.put("restrictToEntityName", entityName);
			}
		}

		List<EntityDirectoryEntry> results;

		results = rdfDao.selectForList(ASSOCIATION_NAMESPACE,
				GET_CHILDREN_ASSOCIATION_OF_ENTITY, parameters,
				EntityDirectoryEntry.class);

		boolean moreResults = results.size() > page.getMaxToReturn();

		if (moreResults) {
			results.remove(results.size() - 1);
		}
		return new DirectoryResult<EntityDirectoryEntry>(results, !moreResults);

	}

private void parameterizeAssociationRestriction(AssociationQueryServiceRestrictions restrictions, Map<String, Object> parameters) {
	NameOrURI codeSystemVersion = restrictions.getCodeSystemVersion();
    String ontologyId=null;
	if (codeSystemVersion != null) {
		if (StringUtils.isNotBlank(codeSystemVersion.getName())) {
			CodeSystemVersionName csvname = CodeSystemVersionName
					.parse(codeSystemVersion.getName());
			parameters.put("restrictToGraph", csvname.getAcronym());
			parameters.put("restrictToCodeSystemVersion",
					codeSystemVersion.getName());
			String id = csvname.getId();
			ontologyId = this.idService.getOntologyIdForId(id);

		}
	}

	addRestriction(ontologyId,"restrictToSourceEntity",
			restrictions.getSourceEntity(), parameters);
	addRestriction(ontologyId, "restrictToTargetEntity",
			restrictions.getTargetEntity(), parameters);
	addRestriction(ontologyId, "restrictToPredicate", restrictions.getPredicate(),
			parameters);
	addRestriction(ontologyId, "restrictToSourceOrTarget",
			restrictions.getSourceOrTargetEntity(), parameters);

}


	public DirectoryResult<EntityDirectoryEntry> getSourceEntities(
			AssociationQueryServiceRestrictions associationRestrictions,
			EntityDescriptionQueryServiceRestrictions entityRestrictions,
			ResolvedReadContext readContext, Page page) {
		// TODO Auto-generated method stub
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(LIMIT, page.getMaxToReturn() + 1);
		parameters.put(OFFSET, page.getStart());

		parameterizeAssociationRestriction(associationRestrictions, parameters );
		List<EntityDirectoryEntry> results;

		results = rdfDao.selectForList(ASSOCIATION_NAMESPACE,
				GET_SOURCE_ENTITIES, parameters, EntityDirectoryEntry.class);

		if (results == null) {
			results = new ArrayList<EntityDirectoryEntry>();
		}
		boolean moreResults = results.size() > page.getMaxToReturn();

		if (moreResults) {
			results.remove(results.size() - 1);
		}
		return new DirectoryResult<EntityDirectoryEntry>(results, !moreResults);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.service.profile.association.AssociationQueryService
	 * #getAssociationGraph(edu.mayo.cts2.framework.model.association.types.
	 * GraphFocus,
	 * edu.mayo.cts2.framework.service.profile.entitydescription.name
	 * .EntityDescriptionReadId,
	 * edu.mayo.cts2.framework.model.association.types.GraphDirection, long)
	 */
	@Override
	public DirectoryResult<GraphNode> getAssociationGraph(GraphFocus focusType,
			EntityDescriptionReadId focusEntity, GraphDirection direction,
			long depth) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService
	 * #doAddSupportedModelAttributes(java.util.Set)
	 */
	@Override
	public void doAddSupportedModelAttributes(
			Set<VariableTiedPropertyReference> set) {

		VariableTiedPropertyReference about = new VariableTiedPropertyReference(
				StandardModelAttributeReference.ABOUT, "ontologyId");

		set.add(about);
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
