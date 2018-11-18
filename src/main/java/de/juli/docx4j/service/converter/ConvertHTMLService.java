package de.juli.docx4j.service.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.Create;
import de.juli.docx4j.service.Service;
import de.juli.docx4j.service.model.Attribut;

public class ConvertHTMLService extends Service implements Create {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ConvertHTMLService.class);
	private Path target;
	
	public ConvertHTMLService(Path source) throws Exception {
		super(source);
	}
	
	
	@Override
	public Path create() throws Exception {
		XWPFDocument document = new XWPFDocument(new FileInputStream(source.toFile()));
		XHTMLOptions options = XHTMLOptions.create();
		
		File imageFolder = new File(target.getName(target.getNameCount() - 1) + "/images/");
		options.setExtractor(new FileImageExtractor(imageFolder));
		options.URIResolver(new FileURIResolver(imageFolder));
		XHTMLConverter.getInstance().convert(document, new FileOutputStream(target.toFile()), options);
		return target;
	}


	@Override
	public void addAttrib(Attribut attibut) {
	}

	@Override
	public void open(Path target) {
		this.target = target;
	}
}
