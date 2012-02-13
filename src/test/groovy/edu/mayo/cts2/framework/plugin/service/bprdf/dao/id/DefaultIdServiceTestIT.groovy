package edu.mayo.cts2.framework.plugin.service.bprdf.dao.id;

import static org.junit.Assert.*;

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class DefaultIdServiceTestIT {
	
	@Resource
	DefaultIdService id
	
	@Test
	void TestServiceNotNull(){
		assertNotNull id
	}

}
