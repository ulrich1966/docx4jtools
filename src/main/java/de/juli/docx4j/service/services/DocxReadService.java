package de.juli.docx4j.service.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.wml.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.ReadService;
import de.juli.docx4j.service.Service;
import javafx.beans.binding.StringBinding;

public class DocxReadService extends Service implements ReadService {
	private static final Logger LOG = LoggerFactory.getLogger(DocxReadService.class);
	private String marshalString;
	private Part header;
	private Part footer;

	public DocxReadService(Path path) throws Exception {
		super(path);
	}
	
	@Override
	public List<Object> read() throws Exception {
		//MainDocumentPart root = service.getRootDocPart();
		List<Object> content = super.service.getJaxbElement().getContent();
		//super.service.partNamesToList().forEach(e -> LOG.info("{}", e));
		return content;	
	}


	public String marschallDocx() throws Docx4JException {
		return marshalString = XmlUtils.marshaltoString(service.getJaxbElement(), true, true);
	}

	public Document unmarschallDocx(String value) throws Docx4JException {
		Document doc = new Document();
		try {
			Object obj = XmlUtils.unmarshalString(value, Context.jc, Document.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public void play() throws Docx4JException {
		Object target = XmlUtils.unwrap(source);
		LOG.info("{}", target);
	}

	public Map<String, StringBuilder> docxText() throws XPathBinderAssociationIsPartialException, JAXBException {
		Map<String, StringBuilder> map = new HashMap<>();
		map.put("head", super.service.parseHeaderText());
		map.put("body", super.service.parseBodyText());
		map.put("footer", super.service.parseFooterText());
		return map;
	}

	public String getMarshalString() {
		// marshalString kann sich waerend der Laufzeit aendern
		try {
			marschallDocx();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return marshalString;
	}

	public List<Part> getHeaders() {
		List<Part> parts = new ArrayList<>();
		service.findHeaderParts().forEach((k,v) -> parts.add(v));
		return parts;
	}

	public List<Part> getFooters() {
		List<Part> parts = new ArrayList<>();
		service.findFooterParts().forEach((k,v) -> parts.add(v));
		return parts;
	}
}
