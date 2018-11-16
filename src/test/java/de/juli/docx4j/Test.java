package de.juli.docx4j;

import org.junit.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	private static final Logger LOG = LoggerFactory.getLogger(Test.class);

	@After
	public void tearDown() throws Exception {
	}

	@org.junit.Test
	public void test() {
		LOG.info("{}", "test");
	}

}
