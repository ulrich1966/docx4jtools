package de.juli.docx4j.service.services;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.JaxbXmlPartXPathAware;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.Document;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.SectPr.PgSz;
import org.docx4j.wml.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Docx4JService {
	private static final Logger LOG = LoggerFactory.getLogger(Docx4JService.class);
	private static Docx4JService instance;
	private WordprocessingMLPackage wmlPackage;
	private MainDocumentPart rootDocPart;
	private Document document;
	private Body body;
	private File currentFile;
	private Parts parts;

	private Docx4JService() throws InvalidFormatException {
		super();
		wmlPackage = WordprocessingMLPackage.createPackage();
		rootDocPart = wmlPackage.getMainDocumentPart();
	}

	private Docx4JService(Path source) throws Exception {
		this();
		open(source);
	}

	private void save() throws Docx4JException {
		wmlPackage.save(currentFile);
	}

	public static Docx4JService getInstance() {
		if (instance == null) {
			try {
				instance = new Docx4JService();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static Docx4JService getInstance(Path source) {
		if (instance == null) {
			try {
				instance = new Docx4JService(source);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public WordprocessingMLPackage open(Path source) throws Exception {
		this.wmlPackage = WordprocessingMLPackage.load(source.toFile());
		this.rootDocPart = wmlPackage.getMainDocumentPart();
		this.parts = this.wmlPackage.getParts();

		this.document = getJaxbElement();
		this.document = (Document) getDocument();
		this.body = document.getBody();

		this.currentFile = source.toFile();

		return wmlPackage;
	}

	public Path save(File file) throws Docx4JException {
		wmlPackage.save(file);
		return file.toPath();
	}

	public Path save(Path path) throws Docx4JException {
		return save(path.toFile());
	}

	public PgSz getPageSize() {
		// PageDimensions page = new PageDimensions();
		// ObjectFactory factory = Context.getWmlObjectFactory();

		SectPr sectPr = body.getSectPr();
		PgSz pgSz = sectPr.getPgSz();
		LOG.debug("{} x {}", pgSz.getH(), pgSz.getW());
		return pgSz;
	}

	public void setPageSize(Integer width, Integer hight) throws Docx4JException {
		BigInteger w = BigInteger.valueOf(width);
		BigInteger h = BigInteger.valueOf(hight);
		getPageSize().setH(h);
		getPageSize().setW(w);
		save();
	}

	public List<String> read(Document document) throws Exception {
		return null;
	}

	public Path create(List<String> list, Path path) throws Exception {
		return path;
	}

	public List<String> partNamesToList(HashMap<PartName, Part> map) {
		List<String> names = new ArrayList<>();
		for (Entry<PartName, Part> enty : map.entrySet()) {
			names.add(enty.getKey().toString());
		}
		return names;
	}

	public List<Part> partsToList(HashMap<PartName, Part> map) {
		List<Part> parts = new ArrayList<>();
		for (Entry<PartName, Part> enty : map.entrySet()) {
			parts.add(enty.getValue());
		}
		return parts;
	}

	public HashMap<PartName, Part> partMap() {
		return parts.getParts();
	}

	public HashMap<PartName, Part> findHeaderParts() {
		HashMap<PartName, Part> headers = new HashMap<>();
		for (Entry<PartName, Part> enty : parts.getParts().entrySet()) {
			if (enty.getValue() instanceof org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart) {
				headers.put(enty.getKey(), enty.getValue());
			}
		}
		return headers;
	}

	public HashMap<PartName, Part> findFooterParts() {
		HashMap<PartName, Part> headers = new HashMap<>();
		for (Entry<PartName, Part> enty : parts.getParts().entrySet()) {
			if (enty.getValue() instanceof org.docx4j.openpackaging.parts.WordprocessingML.FooterPart) {
				headers.put(enty.getKey(), enty.getValue());
			}
		}
		return headers;
	}

	@SuppressWarnings({ "rawtypes" })
	public StringBuilder parseBodyText() throws XPathBinderAssociationIsPartialException, JAXBException {
		StringBuilder sb = new StringBuilder();
		String textNodesXPath = "//w:t";
		List<Object> bodyTextNodes = rootDocPart.getJAXBNodesViaXPath(textNodesXPath, true);
		for (Object obj : bodyTextNodes) {
			Text text = (Text) ((JAXBElement) obj).getValue();
			sb.append(String.format("%s\n", text.getValue()));
		}
		return sb;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StringBuilder parseHeaderText() throws XPathBinderAssociationIsPartialException, JAXBException {
		StringBuilder sb = new StringBuilder();
		String textNodesXPath = "//w:t";
		List<Part> headerList = partsToList(findHeaderParts());
		if (headerList != null) {
			for (Part part : headerList) {
				List<Object> headerTextNodes = ((JaxbXmlPartXPathAware<Document>) part)
						.getJAXBNodesViaXPath(textNodesXPath, true);
				for (Object obj : headerTextNodes) {
					Text text = (Text) ((JAXBElement) obj).getValue();
					sb.append(String.format("%s\n", text.getValue()));
				}
			}
		}
		return sb;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StringBuilder parseFooterText() throws XPathBinderAssociationIsPartialException, JAXBException {
		StringBuilder sb = new StringBuilder();
		String textNodesXPath = "//w:t";
		List<Part> footerList = partsToList(findFooterParts());
		if (footerList != null) {
			for (Part part : footerList) {
				List<Object> footerTextNodes = ((JaxbXmlPartXPathAware<Document>) part).getJAXBNodesViaXPath(textNodesXPath, true);
				for (Object obj : footerTextNodes) {
					Text text = (Text) ((JAXBElement) obj).getValue();
					sb.append(String.format("%s\n", text.getValue()));
				}
			}
		}
		return sb;
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

	// Getter Setter

	public WordprocessingMLPackage getWmlPackage() {
		return wmlPackage;
	}

	public MainDocumentPart getRootDocPart() {
		return rootDocPart;
	}

	public Document getDocument() {
		return document;
	}

	public Document getJaxbElement() {
		return rootDocPart.getJaxbElement();
	}

	public Body getBody() {
		return body;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public Parts getParts() {
		return parts;
	}
}
