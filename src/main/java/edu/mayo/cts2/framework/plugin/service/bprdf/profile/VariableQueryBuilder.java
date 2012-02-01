package edu.mayo.cts2.framework.plugin.service.bprdf.profile;

import java.util.ArrayList;
import java.util.List;

public class VariableQueryBuilder {
	
	private VariableQuery variableQuery = new VariableQuery();
	
	public VariableQueryBuilder(){
		super();
	}
	
	public VariableQueryBuilder addQuery(String variable, String text){
		this.variableQuery.getQueries().add(new Query(variable, text));
		
		return this;
	}
	
	public VariableQuery build(){
		return this.variableQuery;
	}
	
	public static class VariableQuery {
		
		private List<Query> queries = new ArrayList<Query>();

		public List<Query> getQueries() {
			return queries;
		}

		public void setQueries(List<Query> queries) {
			this.queries = queries;
		}
	}
	
	public static class Query {
		
		private String var;
		private String text;

		public Query(String var, String text) {
			super();
			this.var = var;
			this.text = text;
		}
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
		
	}
}
