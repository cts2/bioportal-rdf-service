package edu.mayo.cts2.framework.plugin.service.bprdf.result;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.mayo.twinkql.result.callback.ConditionalTest;

@Component("isLiteral")
public class IsLiteralConditional implements ConditionalTest<RDFNode> {

	@Override
	public boolean test(RDFNode param) {
		return param.isLiteral();
	}

}
