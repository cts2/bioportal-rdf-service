package edu.mayo.cts2.framework.plugin.service.bprdf.dao.namespace;

import javax.annotation.Resource;
import static org.junit.Assert.*

import org.junit.Ignore
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value="classpath:/bioportal-rdf-service-test-context.xml")
class RestNamespaceCrawlerTestIT {

	@Resource
	RestNamespaceCrawler crawler
	
	@Test
	void testNotNull(){
		assertNotNull crawler
	}
	
	@Test
	@Ignore
	void TestDoGatherNamespaces(){
		crawler.crawl();
		
	}
}
