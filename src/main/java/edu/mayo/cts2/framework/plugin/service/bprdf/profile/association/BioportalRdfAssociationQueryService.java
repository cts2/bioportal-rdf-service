package edu.mayo.cts2.framework.plugin.service.bprdf.profile.association;

import java.util.List;
import java.util.Set;

import edu.mayo.cts2.framework.model.association.Association;
import edu.mayo.cts2.framework.model.association.AssociationDirectoryEntry;
import edu.mayo.cts2.framework.model.association.GraphNode;
import edu.mayo.cts2.framework.model.association.types.GraphDirection;
import edu.mayo.cts2.framework.model.association.types.GraphFocus;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.FormatReference;
import edu.mayo.cts2.framework.model.core.NamespaceReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.entity.EntityList;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableTiedModelAttributeReference;
import edu.mayo.cts2.framework.service.command.restriction.AssociationQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.command.restriction.EntityDescriptionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.association.AssociationQuery;
import edu.mayo.cts2.framework.service.profile.association.AssociationQueryService;
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionQuery;
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId;

public class BioportalRdfAssociationQueryService extends AbstractQueryService implements  AssociationQueryService{

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getServiceName()
	 */
	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getServiceDescription()
	 */
	@Override
	public OpaqueData getServiceDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getServiceVersion()
	 */
	@Override
	public String getServiceVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getServiceProvider()
	 */
	@Override
	public SourceReference getServiceProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getSupportedFormatList()
	 */
	@Override
	public List<FormatReference> getSupportedFormatList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getDefaultFormat()
	 */
	@Override
	public FormatReference getDefaultFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.BaseService#getKnownNamespaceList()
	 */
	@Override
	public List<NamespaceReference> getKnownNamespaceList() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#getResourceSummaries(edu.mayo.cts2.framework.service.profile.ResourceQuery, edu.mayo.cts2.framework.model.core.SortCriteria, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<AssociationDirectoryEntry> getResourceSummaries(
			AssociationQuery query, SortCriteria sortCriteria, Page page) {
		// TODO Auto-generated method stub
		return null;
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
	 * @see edu.mayo.cts2.framework.service.profile.BaseQueryService#getSupportedProperties()
	 */
	@Override
	public Set<? extends PredicateReference> getSupportedProperties() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
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
