package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.ResultSet;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.service.core.ReadContext;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.transform.CodeSystemTransform;
import edu.mayo.cts2.framework.service.profile.codesystem.CodeSystemReadService;

@Component
public class BioportalRdfCodeSystemReadService implements CodeSystemReadService {

	private final static String CODESYSTEM_NAMESPACE = "codeSystem";
	private final static String GET_CODESYSTEM_BY_URI = "getCodeSystemByUri";
	private final static String GET_CODESYSTEM_BY_NAME = "getCodeSystemByName";

	@Resource
	private CodeSystemTransform codeSystemTransform;

	@Resource
	private RdfDao rdfDao;

	@Override
	public CodeSystemCatalogEntry read(NameOrURI identifier,
			ResolvedReadContext readContext) {
		/*
		ResultSet resultSet;
		
		if (StringUtils.isNotBlank(identifier.getName())) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("name", identifier.getName());

			resultSet = this.rdfDao.query(CODESYSTEM_NAMESPACE,
					GET_CODESYSTEM_BY_NAME, parameters);
		} else {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("uri", identifier.getUri());

			resultSet = this.rdfDao.query(CODESYSTEM_NAMESPACE,
					GET_CODESYSTEM_BY_URI, parameters);
		}
		
		if (!resultSet.hasNext()) {
			return null;
		} else {
			CodeSystemCatalogEntry result = this.codeSystemTransform
					.transform(resultSet);

			return result;
		}
		*/
		return null;
	}

	@Override
	public boolean exists(NameOrURI identifier, ReadContext readContext) {
		// TODO Auto-generated method stub
		return false;
	}

}
