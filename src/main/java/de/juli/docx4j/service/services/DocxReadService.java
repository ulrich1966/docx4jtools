package de.juli.docx4j.service.services;

import java.nio.file.Path;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.wml.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.ReadService;
import de.juli.docx4j.service.Service;

public class DocxReadService extends Service implements ReadService {
	private static final Logger LOG = LoggerFactory.getLogger(DocxReadService.class);
	private String marshalString;
	private Part header;
	private Part footer;

	public DocxReadService(Path path) throws Exception {
		super(path);
	}
	
	@Override
	public Object read() throws Exception {
		//MainDocumentPart root = service.getRootDocPart();
		List<Object> content = super.service.getJaxbElement().getContent();
		
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

	public String getMarshalString() {
		// marshalString kann sich waerend der Laufzeit aendern
		try {
			marschallDocx();
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return marshalString;
	}

	public Part getHeader() throws InvalidFormatException {
		if(header == null) {
			header = super.service.getParts().get(new PartName("/word/cover-header.xml"));
		}
		return header;
	}

	public Part getFooter() throws InvalidFormatException {
		if(footer == null) {
			footer = super.service.getParts().get(new PartName("/word/cover-footer.xml"));
		}
		return footer;
	}
}
