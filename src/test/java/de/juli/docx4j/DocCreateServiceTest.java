package de.juli.docx4j;

import static org.junit.Assert.fail;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.services.DocxCreateService;
import de.juli.docx4j.util.AppConfig;

public class DocCreateServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(DocCreateServiceTest.class);

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
		Path target = AppConfig.getRootAsPath().resolve("docs").resolve("test.docx");
		LOG.debug(""+target);
		DocxCreateService service = new  DocxCreateService(target);
		service.open(target);
		service.create();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		LOG.info("DONE!");
	}

}
