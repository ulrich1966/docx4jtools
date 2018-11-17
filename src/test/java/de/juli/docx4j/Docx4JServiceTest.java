package de.juli.docx4j;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.Map;

import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.converter.ConvertHTMLService;
import de.juli.docx4j.service.converter.ConvertPDFService;
import de.juli.docx4j.service.services.FieldPasteService;
import de.juli.docx4j.util.Executer;
import de.juli.docx4j.util.FileTools;
import de.juli.docx4j.util.TestDaten;

public class Docx4JServiceTest extends ServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(Docx4JServiceTest.class);
	public static String name;

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws InvalidFormatException {
		Path docxTemplate = DOC_ROOT.resolve("ansch.docx");
		LOG.debug("{}", DOC_ROOT);
		LOG.debug("{}", docxTemplate);
		try {
			
			Path docx = fillFields(docxTemplate);
			LOG.info("Erstellt: {}", docx);
			if(docxOut) openProcess(Executer.DOCX_EXECUTER, docx.toString());

			Path pdf = FileTools.targetFromSource(docx, "pdf");
			pdf = convertToPdf(docx, pdf);
			LOG.info("Erstellt: {}", pdf);
			if(pdfOut) openProcess(Executer.PDF_EXECUTER, pdf.toString());
			
			name = pdf.toString();
			
			Path html = FileTools.targetFromSource(docx, "html");
			html = convertToHtml(docx, html);
			LOG.info("Erstellt: {}", html);
			if(htmlOut) openProcess(Executer.HTML_EXECUTER, html.toString());
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		LOG.debug("DONE!");
	}

	private Path convertToHtml(Path source, Path target) throws Exception {
		ConvertHTMLService web = new ConvertHTMLService(source);
		web.open(target);
		Path html = web.create();
		return html;
	}

	private Path convertToPdf(Path source, Path target) throws Exception {
		ConvertPDFService converter = new ConvertPDFService(source);
		//Path pdf = converter.xdocConvert(target);
		//pdf = converter.xdocreportConvert(pdf, target);
		Path pdf = converter.docx4jConvert(target);
		return pdf;
	}

	private Path fillFields(Path source) throws Exception {
		Map<String, String> data = TestDaten.genareate();	
		Path target = DOC_ROOT.resolve(String.format("anschreiben_%s.docx", data.get("name")));
		FieldPasteService pasteService = new FieldPasteService(source);
		pasteService.paste(data, target);
		return target;
	}

	public String getName() {
		return name;
	}
}
