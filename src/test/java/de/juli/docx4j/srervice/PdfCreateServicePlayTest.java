package de.juli.docx4j.srervice;

import static org.junit.Assert.fail;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.util.AppConfig;
import de.juli.docx4j.util.ExecProcess;
import de.juli.docx4j.util.Executer;

public class PdfCreateServicePlayTest {
	private static final Logger LOG = LoggerFactory.getLogger(PdfCreateServicePlayTest.class);
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			Path target = AppConfig.getRootAsPath().resolve("docs").resolve("test_pdf.pdf");
			Path source = AppConfig.getRootAsPath().resolve("docs").resolve("ansch.docx");
			LOG.debug("" + target);
			Pdf_PLAYService service = new Pdf_PLAYService(source);
			service.open(target);
			target = service.create();
			ExecProcess process = ExecProcess.getInstance(Executer.PDF_EXECUTER_LT, target.toString());
			process.start();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		LOG.info("DONE!");
	}
}
