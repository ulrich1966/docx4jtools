package de.juli.docx4j.srervice;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import de.juli.docx4j.service.Create;
import de.juli.docx4j.service.Service;
import de.juli.docx4j.service.model.Attribut;

public class Pdf_PLAYService extends Service implements Create {
	private static final Logger LOG = LoggerFactory.getLogger(Pdf_PLAYService.class);
	private Path source;
	private Path target;

	public Pdf_PLAYService(Path path) throws Exception {
		super(null);
		source = path;
	}
	
	@Override
	public void addAttrib(Attribut attibut) {
		
	}
	
	@Override
	public void open(Path target) {
		this.target = target;
	}

	@Override
	public Path create() throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(target.toFile()));

		document.open();

		Font font = FontFactory.getFont(FontFactory.COURIER, 16, Color.BLACK);
		Chunk chunk = new Chunk("Hello World", font);

		document.add(new Paragraph("A Hello World PDF document."));

		addAttrib(document);
		addImge(document, "wolpi.jpg");

		document.add(chunk);
		document.close();
		document.close();
		writer.close();

		return target;
	}

	public Path read() throws DocumentException, IOException {
		PdfReader pdfReader = new PdfReader(source.toString());
		Path pdf2 = source.getParent().resolve("pdf2.pdf");
		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(pdf2.toFile()));
		
		Path imgs = source.getParent().getParent().resolve("img");
		Path img = imgs.resolve("wolpi.jpg");

		Image image = Image.getInstance(img.toString());
		image.scaleAbsolute(100, 100);
		image.setAbsolutePosition(350f, 700f);

		for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
			PdfContentByte content = pdfStamper.getUnderContent(i);
			content.addImage(image);
		}

		pdfStamper.close();
		return pdf2;

	}

	private void addImge(Document document, String imgName) {
		Path imgs = source.getParent().getParent().resolve("img");
		Path img = imgs.resolve(imgName);
		Image image2 = null;
		Image image1 = null;
		try {
			image1 = Image.getInstance(img.toString());
			image1.setAbsolutePosition(100f, 550f);
			image1.scaleAbsolute(200, 200);

			String imageUrl = "http://www.eclipse.org/xtend/images/java8_logo.png";
			image2 = Image.getInstance(new URL(imageUrl));
			image2.setAlt("logo notfound");
			image2.setAbsolutePosition(350f, 550f);
			image2.scaleAbsolute(200, 200);

			document.add(image1);
			document.add(image2);
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
		} catch (IOException | DocumentException e) {
		}

	}

	private void addAttrib(Document document) {
		document.addAuthor("Ulrich Klood");
		document.addCreationDate();
		document.addCreator("Ulrich Klood");
		document.addTitle("Test für PDF");
		document.addSubject("Ein Test der PDF Dokumentenerstellung");
	}


}
