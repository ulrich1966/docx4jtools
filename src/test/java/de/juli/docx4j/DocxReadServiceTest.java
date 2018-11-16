package de.juli.docx4j;

import static org.junit.Assert.fail;

import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.ReadService;
import de.juli.docx4j.service.services.DocxReadService;
import de.juli.docx4j.util.ExecProcess;
import de.juli.docx4j.util.Executer;

public class DocxReadServiceTest extends ServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(DocxReadServiceTest.class);

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			Path docx = DOC_ROOT.resolve("converttxt.docx");
			
			setDocxOut(false);
			if (docxOut)
				openProcess(Executer.DOCX_EXECUTER, docx.toString());

			ReadService service = new DocxReadService(docx);
			service.read();
			
	
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		LOG.info("DONE!");
	}
	
	public void openProcess(Path pdf) {
		ExecProcess process = ExecProcess.getInstance(Executer.PDF_EXECUTER, pdf);
		process.start();
		
	}
}
