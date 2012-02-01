package edu.mayo.cts2.framework.plugin.service.bprdf.result;

import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.RDFNode;

import edu.mayo.twinkql.result.callback.ConditionalTest;

@Component("isNotLiteral")
public class IsNotLiteralConditional implements ConditionalTest<RDFNode> {

	@Override
	public boolean test(RDFNode param) {
		return !param.isLiteral();
	}

}
