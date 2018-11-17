package de.juli.docx4j;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.CreateService;
import de.juli.docx4j.service.model.Attribut;
import de.juli.docx4j.service.services.FieldPasteService;
import de.juli.docx4j.service.services.PdfCreateService;
import de.juli.docx4j.util.Executer;
import de.juli.docx4j.util.TestDaten;

public class PdfCreateServiceTest extends ServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(PdfCreateServiceTest.class);

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			Path target = DOC_ROOT.resolve("test_pdf.pdf");
			Path docxTemplate = DOC_ROOT.resolve("ansch.docx");
			//Path docx = fillFields(docxTemplate);
			Path docx = DOC_ROOT.resolve("converttxt.docx");
			LOG.info("Erstellt: {}", docx);
			
			setDocxOut(false);
			if (docxOut)
				openProcess(Executer.DOCX_EXECUTER, docx.toString());

			CreateService service = new PdfCreateService(docx);
			service.open(target);
			Attribut attribut = new Attribut();
			attribut.setAuthor("Uli");
			attribut.setCreator("Uli");;
			attribut.setSubject("Pdf creation");
			attribut.setTitle("Pdf creation");
			service.addAttrib(attribut);
			Path pdf = service.create();
			
			setPdfOut(true);
			if (pdfOut && pdf != null)
				openProcess(Executer.PDF_EXECUTER, pdf.toString());
				
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		LOG.info("DONE!");
	}

	private Path fillFields(Path source) throws Exception {
		Map<String, String> data = TestDaten.genareate();
		Path target = DOC_ROOT.resolve(String.format("anschreiben_%s.docx", data.get("name")));
		FieldPasteService pasteService = new FieldPasteService(source);
		pasteService.paste(data, target);
		return target;
	}
}
