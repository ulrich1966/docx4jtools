package de.juli.docx4j.docx;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.events.EventFinished;
import org.docx4j.events.StartEvent;
import org.docx4j.events.WellKnownProcessSteps;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.services.client.ConversionException;
import org.docx4j.services.client.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDocx extends Docx4J {
	private static final Logger LOG = LoggerFactory.getLogger(MyDocx.class);

	public static void toPDF(WordprocessingMLPackage wmlPackage, OutputStream outputStream) throws Docx4JException {
		StartEvent startEvent = new StartEvent(wmlPackage, WellKnownProcessSteps.PDF);
		startEvent.publish();

		if (pdfViaFO()) {
			FOSettings settings = createFOSettings();
			settings.setWmlPackage(wmlPackage);
			settings.setApacheFopMime("application/pdf");
			toFO(settings, outputStream, FLAG_NONE);
		} else {

			// Configure this property to point to your own Converter instance.
			String URL = Docx4jProperties.getProperty("com.plutext.converter.URL", "http://converter-eval.plutext.com:80/v1/00000000-0000-0000-0000-000000000000/convert");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			save(wmlPackage, baos);

			MyConverter converter = new MyConverter(URL);

			try {
				converter.convert(baos.toByteArray(), Format.DOCX, Format.PDF, outputStream);
				baos.close();
			} catch (ConversionException e) {
				new EventFinished(startEvent).publish();
				if (e.getResponse() != null) {
					if (e.getResponse().getStatusLine().getStatusCode() == 403) {
						throw new Docx4JException("Problem converting to PDF; license expired?", e);
					} else {
						LOG.error(e.getResponse().getStatusLine().getStatusCode() + " " + e.getResponse().getStatusLine().getReasonPhrase());
					}
				}
				// the content is in the outputstream, we can't inspect that
				// here.
				throw new Docx4JException("Problem converting to PDF; \nusing URL " + URL + "\n" + e.getMessage(), e);
			} catch (Exception e) {
				new EventFinished(startEvent).publish();
				throw new Docx4JException("Problem converting to PDF; \nusing URL " + URL + "\n" + e.getMessage(), e);
			}

		}

		new EventFinished(startEvent).publish();
	}
}
