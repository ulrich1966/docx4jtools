package de.juli.docx4j;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.srervice.Pdf_PLAYService;
import de.juli.docx4j.util.ExecProcess;
import de.juli.docx4j.util.Executer;

public class PdfReadServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(PdfReadServiceTest.class);
	private boolean execPdf = true;

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			Docx4JServiceTest st = new Docx4JServiceTest();
			st.test();
			
			String pdfLoc = st.getName();
			LOG.debug("{}", pdfLoc);

			Path pdf = Paths.get(pdfLoc);
			Pdf_PLAYService service = new Pdf_PLAYService(pdf);
			pdf = service.read();
			if(execPdf) openProcess(pdf);
			
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
