package de.juli.docx4j.service.services;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.Parts;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.Document;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.SectPr.PgSz;
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

	public List<String> partNamesToList() {
		List<String> names = new ArrayList<>();
		for (Entry<PartName, Part> enty : partMap().entrySet()) {
			names.add(enty.getKey().toString());
		} 
		return names;
	}

	public HashMap<PartName, Part> partMap() {
		return parts.getParts();
	}

	public HashMap<PartName, Part> findHeaderParts() {
		HashMap<PartName, Part> headers = new HashMap<>(); 
		for (Entry<PartName, Part> enty : parts.getParts().entrySet()) {
			if(enty.getValue() instanceof org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart) {
				headers.put(enty.getKey(), enty.getValue());
			}
		}
		return headers;
	}

	public HashMap<PartName, Part> findFooterParts() {
		HashMap<PartName, Part> headers = new HashMap<>(); 
		for (Entry<PartName, Part> enty : parts.getParts().entrySet()) {
			if(enty.getValue() instanceof org.docx4j.openpackaging.parts.WordprocessingML.HeaderPart) {
				headers.put(enty.getKey(), enty.getValue());
			}
		}
		return headers;
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
