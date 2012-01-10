package edu.mayo.cts2.framework.plugin.service.rdf.dao;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class RdfDao {

	private String sparqlService = "http://alphasparql.bioontology.org/sparql";
	private String apiKey = "880e5e30-0fa9-4cba-b25f-3069b15577f9";
	
	public static void main(String[] args) throws Exception{
		RdfDao dao = new RdfDao();
		dao.getCodeSystems();
	}

	public void getCodeSystems() throws Exception {

		String query = "PREFIX omv: <http://omv.ontoware.org/2005/05/ontology#> "
				+ "SELECT ?ont ?name ?acr "
				+ "WHERE { ?ont a omv:Ontology; "
				+ "omv:acronym ?acr; " + "omv:name ?name . " + "}";

		ResultSet results = this.executeQuery(query);
		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();
			RDFNode ontUri = soln.get("ont");
			Literal name = soln.getLiteral("name");
			Literal acr = soln.getLiteral("acr");
			System.out.println(ontUri + " ---- " + name + " ---- " + acr);
		}

	}

	public ResultSet executeQuery(String queryString) throws Exception {
		Query query = QueryFactory.create(queryString) ;
		 
		QueryEngineHTTP qexec = QueryExecutionFactory.createServiceRequest(
				this.sparqlService, query);
		qexec.addParam("apikey", this.apiKey);
		ResultSet results = qexec.execSelect();
		return results;

	}
}
