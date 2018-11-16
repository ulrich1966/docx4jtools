package de.juli.docx4j.service.services;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.wml.P;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.jvnet.jaxb2_commons.ppp.Child;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import de.juli.docx4j.service.CreateService;
import de.juli.docx4j.service.Service;
import de.juli.docx4j.util.TestDaten;

public class PdfCreateService extends Service implements CreateService {
	private static final Logger LOG = LoggerFactory.getLogger(PdfCreateService.class);
	private DocxReadService docxReader;
	List<Child> elementList = new ArrayList<>();

	public PdfCreateService(Path path) throws Exception {
		super(path);
		docxReader = new DocxReadService(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Path create(Path target) throws Exception {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(target.toFile()));
		document.open();

		addAttrib(document);
		
		Object read = docxReader.read();
		List<Object> list = (List<Object>) read;

		list.forEach(e -> docxContent(e));
		elementList.stream().forEach(e -> showCildInfo(e));
		
		Map<String, String> map = TestDaten.testFiedsAsString();
		Collection<String> values = map.values();	
		
		elementList.stream().forEach(e -> {
			try {
				append(document, e);
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		});
		
		
		if(document.isOpen()) {
			document.close();			
		}
		if(!writer.isCloseStream()) {
			writer.close();			
		}
		return target;
	}

	private Paragraph append(Document document, Child child) throws DocumentException {
		return  append(document, String.format("%s", child));
	}

	private Paragraph append(Document document, String value) throws DocumentException {
		Chunk chunk = new Chunk(value);
		Paragraph paragraph = new Paragraph(chunk);
		document.add(paragraph);
		return paragraph;
	}

	private void showCildInfo(Child child) {
		LOG.debug("{}", child.getClass());
		// if (cild instanceof org.docx4j.wml.P) {
		// org.docx4j.wml.P pCild = (P) cild;
		// LOG.debug("{}", pCild);
		// //pCild.getContent().forEach(e -> LOG.info("{}", e));
		// }
	}

	private void docxContent(Object value) {
		Child child = null;
		if (value.getClass().equals(javax.xml.bind.JAXBElement.class)) {
			javax.xml.bind.JAXBElement<?> jaxb = (JAXBElement<?>) value;
			if (jaxb.getValue() instanceof org.docx4j.wml.Tbl) {
				org.docx4j.wml.Tbl element = (Tbl) jaxb.getValue();
				//handleTableElement(element);
				child = element;
			}
		}
		if (value instanceof org.docx4j.wml.P) {
			org.docx4j.wml.P element = (P) value;
			child = element;
		}
		if (value instanceof org.docx4j.wml.Tbl) {
			org.docx4j.wml.Tbl element = (Tbl) value;
			child = element;
		}
		if (value instanceof org.docx4j.wml.Tr) {
			// org.docx4j.wml.Tr elm = (Tr) value;
			// elementList.add(elm);
		}
		elementList.add(child);
	}

	private void handleTableElement(Tbl element) {
		LOG.debug("{}", element);
		TblPr tblPr = element.getTblPr();
		docxContent(tblPr);
	}

	private void addAttrib(Document document) {
		document.addAuthor("Ulrich Klood");
		document.addCreationDate();
		document.addCreator("Ulrich Klood");
		document.addTitle("PDF");
		document.addSubject("PDF Dokumentenerstellung");
	}

	private void addChunk(Document document, Child child) throws DocumentException {
		//Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
		Chunk chunk = new Chunk(""+child);
		document.add(chunk);
	}
}
