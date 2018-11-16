package de.juli.docx4j.service.services;

import java.io.File;
import java.nio.file.Path;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import de.juli.docx4j.service.CreateService;
import de.juli.docx4j.service.Service;

public class DocxCreateService extends Service implements CreateService{

	public DocxCreateService(Path source) throws Exception {
		super(source);
	}

	public Path create(Path target) throws Docx4JException {
		MainDocumentPart root = service.getRootDocPart();
		root.addStyledParagraphOfText("Title", "Hello World!");
		root.addParagraphOfText("Welcome To Baeldung");
		File exportFile = target.toFile();
		return service.save(exportFile);	
	}
}
