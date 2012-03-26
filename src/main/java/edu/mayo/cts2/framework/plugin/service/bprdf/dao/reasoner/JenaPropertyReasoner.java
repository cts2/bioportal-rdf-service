package edu.mayo.cts2.framework.plugin.service.bprdf.dao.reasoner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.transitiveReasoner.TransitiveReasoner;

import edu.mayo.twinkql.context.QueryExecutionProvider;
import edu.mayo.twinkql.result.beans.reasoning.PropertyReasoner;
import edu.mayo.twinkql.template.TwinkqlTemplate;

@Component("jenaPropertyReasoner")
public class JenaPropertyReasoner implements InitializingBean, PropertyReasoner {

	private static final String REASON_NS = "reasoning";
	private static final String REASON_SPARQL = "reason";

	private Map<String, Set<String>> cache = new HashMap<String, Set<String>>();

	@Autowired
	private TwinkqlTemplate twinkqlTemplate;

	@Autowired
	private QueryExecutionProvider queryExecutionProvider;

	private InfModel infModel;

	@Override
	public void afterPropertiesSet() throws Exception {
		String sparql = this.twinkqlTemplate.getSelectQueryString(REASON_NS,
				REASON_SPARQL, null);
		Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();

		QueryExecution queryExecution = this.queryExecutionProvider
				.provideQueryExecution(sparql);

		Model model = queryExecution.execConstruct();

		infModel = ModelFactory.createInfModel(reasoner, model);
	}

	public Set<String> reason(String uri) {
		if(! this.cache.containsKey(uri)){
			Set<String> returnSet = new HashSet<String>();
	
			Property subPropertyOf = ResourceFactory
					.createProperty(TransitiveReasoner.subPropertyOf.getURI());
	
			Property object = ResourceFactory.createProperty(uri);
	
			StmtIterator i = this.infModel.listStatements(null, subPropertyOf, object);
			while (i.hasNext()) {
				Statement s = i.nextStatement();
	
				returnSet.add(s.getSubject().getURI());
			}
			
			this.cache.put(uri, returnSet);
		}

		return this.cache.get(uri);
	}

	public TwinkqlTemplate getTwinkqlTemplate() {
		return twinkqlTemplate;
	}

	public void setTwinkqlTemplate(TwinkqlTemplate twinkqlTemplate) {
		this.twinkqlTemplate = twinkqlTemplate;
	}
}
