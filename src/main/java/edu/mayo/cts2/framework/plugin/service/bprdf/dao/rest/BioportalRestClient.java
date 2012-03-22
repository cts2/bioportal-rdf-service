package edu.mayo.cts2.framework.plugin.service.bprdf.dao.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ncbo.stanford.bean.NamespaceBean;
import org.ncbo.stanford.bean.UserBean;
import org.ncbo.stanford.bean.response.ErrorBean;
import org.ncbo.stanford.bean.response.SuccessBean;
import org.ncbo.stanford.bean.search.OntologyHitBean;
import org.ncbo.stanford.bean.search.SearchBean;
import org.ncbo.stanford.enumeration.ConceptTypeEnum;
import org.ncbo.stanford.enumeration.SearchRecordTypeEnum;
import org.ncbo.stanford.enumeration.ViewingRestrictionEnum;
import org.ncbo.stanford.service.xml.converters.ClassBeanListConverter;
import org.ncbo.stanford.service.xml.converters.ClassBeanResultListBeanConverter;
import org.ncbo.stanford.service.xml.converters.InstanceBeanResultListBeanConverter;
import org.ncbo.stanford.service.xml.converters.MappingResultListBeanConverter;
import org.ncbo.stanford.service.xml.converters.OntologyAclConverter;
import org.ncbo.stanford.service.xml.converters.OntologyHitMapConverter;
import org.ncbo.stanford.service.xml.converters.OntologyLicenseConverter;
import org.ncbo.stanford.service.xml.converters.SearchResultListBeanConverter;
import org.ncbo.stanford.service.xml.converters.UserAclConverter;
import org.ncbo.stanford.util.constants.ApplicationConstants;
import org.ncbo.stanford.util.paginator.impl.Page;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.enums.EnumSingleValueConverter;
import com.thoughtworks.xstream.mapper.Mapper;

import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.ModelAttributeReference;
import edu.mayo.cts2.framework.model.core.URIAndEntityName;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.ApiKeyProvider;
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;

@Component
public class BioportalRestClient implements InitializingBean {
	
	protected final Log log = LogFactory.getLog(getClass().getName());
	
	@Resource
	private ApiKeyProvider apiKeyProvider;
	
	private static final String API_KEY_PARAM = "apikey";
	
	private String apiKey;
	
	private XStream xstream = new XStream();
	
	public static final String PROPERTIES_NAME = "properties";
	public static final String PROPERTIES_URI = ExternalCts2Constants.buildModelAttributeUri(PROPERTIES_NAME);
	public static final ModelAttributeReference PROPERTIES = new ModelAttributeReference();
	static {
		PROPERTIES.setContent(PROPERTIES_NAME);
		PROPERTIES.setUri(PROPERTIES_URI);
	};
	
	public static final String DEFINITIONS_NAME = "definitions";
	public static final String DEFINITIONS_URI = ExternalCts2Constants.buildModelAttributeUri(DEFINITIONS_NAME);
	public static final ModelAttributeReference DEFINITIONS = new ModelAttributeReference();
	static {
		DEFINITIONS.setContent(DEFINITIONS_NAME);
		DEFINITIONS.setUri(DEFINITIONS_URI);
	};
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.apiKey = this.apiKeyProvider.getApiKey();
		
		this.setAliases(this.xstream);
		this.registerConverters(this.xstream);
	}

	protected String callBioportal(String url){
		
		String fullUrl = url + ( url.contains("?") ? "&" : "?") + API_KEY_PARAM + "=" + this.apiKey;
		
		String xml = this.doCallBioportal(fullUrl);
		
		return xml;
	}
	
	public SuccessBean getNamespaces(String id){
		
		String url = "http://rest.bioontology.org/bioportal/ontologies/namespaces/" + id;
		
		String xml = this.callBioportal(url);
		
		SuccessBean successBean = (SuccessBean) this.xstream.fromXML(xml);
		
		return successBean;
	}
	
	public SuccessBean searchEntities(String ontologyId, Set<ResolvedFilter> filters, edu.mayo.cts2.framework.model.command.Page page){
		if(CollectionUtils.isEmpty(filters) || filters.size() > 1){
			throw new RuntimeException("Search Filter Set must have ONE Filter.");
		}
		
		String xml = this.doSearchEntities(Arrays.asList(ontologyId), filters.iterator().next(), page);
		
		SuccessBean successBean = (SuccessBean) this.xstream.fromXML(xml);
		
		return successBean;
	}
	
	protected String doSearchEntities(Collection<String> ontologyIds, ResolvedFilter filter, edu.mayo.cts2.framework.model.command.Page page){
		String url = "http://rest.bioontology.org/bioportal/search/" + filter.getMatchValue() +
			"?pagenum=" + (page.getPage() + 1) +
			"&pagesize=" + page.getMaxToReturn();
		
		StringBuffer sb = new StringBuffer();
		sb.append(url);
	
		if(CollectionUtils.isNotEmpty(ontologyIds)){
			Iterator<String> itr = ontologyIds.iterator();
			sb.append("&ontologyids=" + itr.next());
			
			while(itr.hasNext()){
				sb.append("," + itr.next());
			}
		}
		
		String algorithm = filter.getMatchAlgorithmReference().getContent();
		
		if(algorithm.equals(StandardMatchAlgorithmReference.EXACT_MATCH.
			getMatchAlgorithmReference().getContent())){
			sb.append("&isexactmatch=1");
		}

		sb.append(
				this.getBioportalQueryStringForFilter(filter));
		
		String xml = this.callBioportal(sb.toString());

		return xml;
	}
	
	protected String getBioportalQueryStringForFilter(ResolvedFilter filter) {
		if(filter == null || filter.getPropertyReference() == null){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();

		URIAndEntityName target = filter.getPropertyReference().getReferenceTarget();
		
		if(StringUtils.equals(target.getName(),DEFINITIONS_NAME)){
			sb.append("&includedefinitions=true");
		} else if(StringUtils.equals(target.getName(),PROPERTIES_NAME)){
			sb.append("&includeproperties=true");
		}
			
		return sb.toString();
	}
	
	protected String doCallBioportal(String url){
	
		HttpHeaders headers = new HttpHeaders();
		headers.set( "Accept", "application/xml" );
		
		if(log.isDebugEnabled()){
			log.debug(url);
		}
		
		ResponseEntity<String> response = this.restTemplate.exchange(
				url, 
				HttpMethod.GET, 
				new HttpEntity<Void>(headers), 
				String.class);
		
		return response.getBody();
	}
	
	private void setAliases(XStream xmlSerializer) {
		xmlSerializer.setMode(XStream.NO_REFERENCES);
		
		xmlSerializer.alias("page", Page.class);
		xmlSerializer.alias("searchResultList", List.class);
		xmlSerializer.alias("ontologyHitList", List.class);
		xmlSerializer.alias("ontologyHitBean", OntologyHitBean.class);
		xmlSerializer.alias("searchBean", SearchBean.class);
		xmlSerializer.alias("namespace", NamespaceBean.class);

		xmlSerializer.alias(ApplicationConstants.RESPONSE_XML_TAG_NAME,
				SuccessBean.class);
		xmlSerializer.alias(ApplicationConstants.ERROR_STATUS_XML_TAG_NAME,
				ErrorBean.class);
		xmlSerializer.alias(ApplicationConstants.SUCCESS_XML_TAG_NAME,
				SuccessBean.class);
		
		xmlSerializer.addDefaultImplementation(URIImpl.class, URI.class);


		xmlSerializer.omitField(SearchBean.class, "recordType");
		xmlSerializer.omitField(SearchBean.class, "objectType");
		xmlSerializer.omitField(UserBean.class, "password");
		xmlSerializer.omitField(UserBean.class, "apiKey");

		xmlSerializer.addDefaultImplementation(ArrayList.class, List.class);
	}
	
	private void registerConverters(XStream xmlSerializer) {
		Mapper mapper = xmlSerializer.getMapper();
		xmlSerializer.registerConverter(new OntologyHitMapConverter(mapper));
		xmlSerializer.registerConverter(new SearchResultListBeanConverter(
				mapper));
		xmlSerializer.registerConverter(new EnumSingleValueConverter(
				SearchRecordTypeEnum.class));
		xmlSerializer.registerConverter(new EnumSingleValueConverter(
				ConceptTypeEnum.class));
		xmlSerializer.registerConverter(new ClassBeanResultListBeanConverter(
				mapper));
		xmlSerializer.registerConverter(new ClassBeanListConverter(mapper));
		xmlSerializer
				.registerConverter(new InstanceBeanResultListBeanConverter(
						mapper));
		xmlSerializer.registerConverter(new MappingResultListBeanConverter(
				mapper));
		xmlSerializer.registerConverter(new UserAclConverter(mapper));
		xmlSerializer.registerConverter(new OntologyAclConverter(mapper));
		xmlSerializer.registerConverter(new OntologyLicenseConverter(mapper));
		xmlSerializer.registerConverter(new EnumSingleValueConverter(
				ViewingRestrictionEnum.class));
	}

}
