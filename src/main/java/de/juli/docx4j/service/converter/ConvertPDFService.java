package de.juli.docx4j.service.converter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.Docx4J;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.ConvertService;
import de.juli.docx4j.service.Service;
import de.juli.docx4j.util.AppConfig;
import de.juli.docx4j.util.FileTools;

public class ConvertPDFService extends Service implements ConvertService {
	private static final Logger LOG = LoggerFactory.getLogger(ConvertPDFService.class);

	public ConvertPDFService(Path source) throws Exception {
		super(source);
	}

	public Path docx4jConvert(Path source, Path target) throws Exception {
		return docx4jConvert(source, FileTools.targetFromSource(source, "pdf"));
	}

	public Path docx4jConvert(Path target) throws Exception {
		logInfo(target);
		Docx4J.toPDF(service.getWmlPackage(), new FileOutputStream(target.toFile()));
		return target;
	}

	public Path xdocConvert(Path target) {
		try {
			XWPFDocument document = new XWPFDocument(new FileInputStream(source.toFile()));
			if (AppConfig.IS_TEST) {
				debugDoc(document);
			}
			PdfOptions options = PdfOptions.create().fontEncoding("UTF-8");
			PdfConverter.getInstance().convert(document, new FileOutputStream(target.toFile()), options);
			return target;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Path xdocConvert(Path source, Path target) throws Exception {
		return xdocConvert(source, FileTools.targetFromSource(source, "pdf"));
	}

	public Path xdocConvert() throws Exception {
		return xdocConvert(FileTools.targetFromSource(source, "pdf"));
	}

	public Path docx4jConvert() throws Exception {
		return docx4jConvert(FileTools.targetFromSource(source, "pdf"));
	}

	private void logInfo(Object t) {
		LOG.debug("Source {}", source);
		LOG.debug("Target {}", t);
	}

	private void debugDoc(XWPFDocument document) {
		@SuppressWarnings("resource")
		XWPFWordExtractor we = new XWPFWordExtractor(document);
		LOG.debug(we.getText());
	}

	@SuppressWarnings("unused")
	private Path cleanDoc(Path source, Path target) throws XWPFConverterException, IOException {
		InputStream is = new FileInputStream(source.toFile());
		XWPFDocument document = new XWPFDocument(is);
		PdfOptions options = PdfOptions.create();
		FileOutputStream pdfStream = new FileOutputStream(target.toFile());
		PdfConverter.getInstance().convert(document, pdfStream, options);
		pdfStream.close();
		return target;
	}

	@Override
	public Path create(Path target) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
