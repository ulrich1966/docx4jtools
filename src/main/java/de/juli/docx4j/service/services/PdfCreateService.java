package de.juli.docx4j.service.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart;
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
import de.juli.docx4j.service.model.Attribut;

public class PdfCreateService extends CreateService {
	private static final Logger LOG = LoggerFactory.getLogger(PdfCreateService.class);
	private DocxReadService docxReader;
	List<Child> elementList = new ArrayList<>();
	private Document document;
	private PdfWriter writer;
	private Path target;

	public PdfCreateService(Path path) throws Exception {
		super(path);
		docxReader = new DocxReadService(path);
	}

	@Override
	public void open(Path target) throws FileNotFoundException {
		super.open(target);
		try {
			this.document = new Document();
			writer = PdfWriter.getInstance(document, new FileOutputStream(target.toFile()));
			document.open();
			this.target = target;
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Path create() throws Exception {
		if (document == null) {
			throw new IllegalStateException("Kein Dokument vorhanden");
		}

		List<Object> content = docxReader.read();
		List<List<Child>> collect = docxReader.getHeaders().stream().map(e -> handleParts(e)).collect(Collectors.toList());

		collect.forEach(h -> {
			h.forEach(c -> itteratePart(c));
		});
		
		System.out.println();
		
		for (Entry<String, StringBuilder> entry : docxReader.docxText().entrySet()) {
			String txt = entry.getValue().toString();
			LOG.info("{}", txt);			
		}

		System.out.println();
		
		docxReader.play();
		
		/*
		 * 
		 * List<Object> list = (List<Object>) read;
		 * 
		 * list.forEach(e -> docxContent(e)); elementList.stream().forEach(e ->
		 * showCildInfo(e));
		 * 
		 * Map<String, String> map = TestDaten.testFiedsAsString(); Collection<String>
		 * values = map.values();
		 * 
		 * elementList.stream().forEach(e -> { try { append(document, e); } catch
		 * (DocumentException e1) { e1.printStackTrace(); } });
		 */

		if (document.isOpen()) {
			try {
				document.close();
			} catch (Exception e) {
				LOG.error("{}", e.getMessage());
				return null;
			}
			if (!writer.isCloseStream()) {
				try {
					writer.close();
				} catch (Exception e) {
					LOG.error("{}", e.getMessage());
					return null;
				}
			}
		}
		return target;
	}

	private Object itteratePart(Child child) {
		LOG.debug("{}: {}", child.getClass(), child);
		if (child instanceof org.docx4j.wml.P) {
			org.docx4j.wml.P element = (P) child;
			List<Object> content = element.getContent();
			if(content != null && content.size() >=1) {
				content.forEach(c-> itteratePart((Child) c));
			} else {
				LOG.info("no more elements");				
			}
		}
		return null;
	}

	private Paragraph append(Document document, Child child) throws DocumentException {
		return append(document, String.format("%s", child));
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

	private void docxContent(Object value, List<Child> childs) {
		Child child = null;
		if (value.getClass().equals(javax.xml.bind.JAXBElement.class)) {
			javax.xml.bind.JAXBElement<?> jaxb = (JAXBElement<?>) value;
			if (jaxb.getValue() instanceof org.docx4j.wml.Tbl) {
				org.docx4j.wml.Tbl element = (Tbl) jaxb.getValue();
				// handleTableElement(element);
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
		childs.add(child);
	}

	private void handleTableElement(Tbl element, List<Child> childs) {
		LOG.debug("{}", element);
		TblPr tblPr = element.getTblPr();
		docxContent(tblPr, childs);
	}

	public void addAttrib(Document document, Attribut attibut) {
		document.addCreationDate();
		document.addAuthor(attibut.getAuthor());
		document.addCreator(attibut.getCreator());
		document.addTitle(attibut.getTitle());
		document.addSubject(attibut.getSubject());
	}

	private void addChunk(Document document, Child child) throws DocumentException {
		// Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
		Chunk chunk = new Chunk("" + child);
		document.add(chunk);
	}

	private List<Child> handleParts(Part part) {
		if (part instanceof org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart) {
			org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart header = (HeaderPart) part;
			List<Object> content = header.getContent();
			List<Child> childs = new ArrayList<>();
			content.forEach(e -> docxContent(e, childs));
			return childs;
		}

		if (part instanceof org.docx4j.openpackaging.parts.WordprocessingML.FooterPart) {

		}

		if (part instanceof org.docx4j.openpackaging.parts.WordprocessingML.FontTablePart) {

		}
		return null;
	}

	// Getter / Setter

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public void addAttrib(Attribut attibut) {
		// TODO Auto-generated method stub

	}

}
