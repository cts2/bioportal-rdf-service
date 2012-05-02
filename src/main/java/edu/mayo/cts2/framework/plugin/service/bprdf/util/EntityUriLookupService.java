package edu.mayo.cts2.framework.plugin.service.bprdf.util;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest.BioportalRestClient;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.entitydescription.BioportalRestEntityDescriptionTransform;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
@Component("entityUriLookupService")
public class EntityUriLookupService {

	@Resource
	private BioportalRestClient bioportalRestClient;

	@Resource
	private BioportalRestEntityDescriptionTransform bioportalRestTransform;

	public String getUriFromCode(String ontologyId, String code) {
		Set<ResolvedFilter> filters = new HashSet<ResolvedFilter>();
		ResolvedFilter filter = new ResolvedFilter();
		filter.setMatchAlgorithmReference(StandardMatchAlgorithmReference.EXACT_MATCH
				.getMatchAlgorithmReference());
		filter.setMatchValue(code);
		filters.add(filter);

		long start = System.currentTimeMillis();
		SuccessBean success = bioportalRestClient.searchEntities(ontologyId,
				filters, new Page());
		System.out.println("Query REST time: "
				+ (System.currentTimeMillis() - start));

		return this.bioportalRestTransform.successBeanToEntityUri(success);
	}

	public String getUriFromScopedEntityName(String ontologyId,
			ScopedEntityName scopedEntity) {

		String uri = null;
		if (scopedEntity != null) {
			String name = scopedEntity.getName();
			String namespace = scopedEntity.getNamespace();

			uri = this.getUriFromCode(ontologyId, name);

			if (StringUtils.isBlank(uri)) {
				uri = this.getUriFromCode(ontologyId, namespace + ":" + name);
			}

			if (StringUtils.isBlank(uri)) {
				uri = this.getUriFromCode(ontologyId, namespace + "_" + name);
			}
		}

		return uri;
	}
}
