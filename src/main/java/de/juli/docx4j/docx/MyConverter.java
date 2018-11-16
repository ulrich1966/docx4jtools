package de.juli.docx4j.docx;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.docx4j.services.client.ConversionException;
import org.docx4j.services.client.Converter;
import org.docx4j.services.client.Format;
import org.docx4j.services.client.MyRetryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConverter implements Converter {
	private static final Logger LOG = LoggerFactory.getLogger(MyConverter.class);
	private String URL = null;

	public MyConverter() {
		super();
	}

	public MyConverter(String endpointURL) {
		LOG.debug("starting, with endpointURL: " + endpointURL);
		if (endpointURL != null) {
			this.URL = endpointURL;
		}
	}
	public void convert(File f, Format fromFormat, Format toFormat, OutputStream os) throws IOException, ConversionException {
		checkParameters(fromFormat, toFormat);
		CloseableHttpClient httpclient = HttpClients.custom().setRetryHandler(new MyRetryHandler()).build();
		try {
			HttpPost httppost = getUrlForFormat(toFormat);
			HttpEntity reqEntity = new FileEntity(f, map(fromFormat));
			httppost.setEntity(reqEntity);
			execute(httpclient, httppost, os);
			LOG.debug("..done");
		} finally {
			httpclient.close();
		}
	}

	private HttpPost getUrlForFormat(Format toFormat) {
		if (Format.TOC.equals(toFormat)) {
			// httppost = new HttpPost(URL+"/?bookmarks");
			// System.out.println(URL+"?format=application/json");
			return new HttpPost(URL + "?format=application/json");
		} else if (Format.DOCX.equals(toFormat)) {
			return new HttpPost(URL + "?format=application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		} else {
			return new HttpPost(URL);
		}
	}

	public void convert(InputStream instream, Format fromFormat, Format toFormat, OutputStream os) throws IOException, ConversionException {
		checkParameters(fromFormat, toFormat);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = getUrlForFormat(toFormat);
			BasicHttpEntity reqEntity = new BasicHttpEntity();
			reqEntity.setContentType(map(fromFormat).getMimeType());
			reqEntity.setContent(instream);
			httppost.setEntity(reqEntity);
			execute(httpclient, httppost, os);
		} finally {
			httpclient.close();
		}
	}

	public void convert(byte[] bytesIn, Format fromFormat, Format toFormat, OutputStream os) throws IOException, ConversionException {
		checkParameters(fromFormat, toFormat);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = getUrlForFormat(toFormat);
			HttpEntity reqEntity = new ByteArrayEntity(bytesIn, map(fromFormat));
			reqEntity.getContentType();
		/*
		 * 
			InputStream content = reqEntity.getContent();
			XWPFDocument document = new XWPFDocument(content);
			Path target = AppConfig.getRootAsPath().resolve("docs").resolve("content.pdf");
			document.write(new FileOutputStream(target.toFile()));
		 */
			httppost.setEntity(reqEntity);
			execute(httpclient, httppost, os);
		} finally {
			httpclient.close();
		}
	}

	protected void execute(CloseableHttpClient httpclient, HttpPost httppost, OutputStream os) throws ClientProtocolException, ConversionException {
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			// System.out.println(""+response.getStatusLine());
			HttpEntity resEntity = response.getEntity();
			resEntity.writeTo(os);
			if (response.getStatusLine().getStatusCode() == 403) {
				throw new ConversionException("403 license expired?", response);
			} else if (response.getStatusLine().getStatusCode() != 200) {
				throw new ConversionException(response);
			}
		} catch (java.net.UnknownHostException uhe) {
			LOG.error("\nLooks like you have the wrong host in your endpoint URL '" + URL + "'\n");
			throw new ConversionException(uhe.getMessage(), uhe);
		} catch (java.net.SocketException se) {
			LOG.error(se.getMessage());
			if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") > -1) {
				LOG.error("This behaviour may be Windows client OS specific; please look in the server logs or try a Linux client");
				System.err.println("This behaviour may be Windows client OS specific; please look in the server logs or try a Linux client");
			}
			throw new ConversionException(se.getMessage(), se);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new ConversionException(ioe.getMessage(), ioe);
		} finally {
			try {
				if (response == null) {
					LOG.error("\nLooks like your endpoint URL '" + URL + "' is wrong\n");
				} else {
					response.close();
				}
			} catch (IOException e) {
			}
		}
	}

	private ContentType map(Format f) {
		if (Format.DOCX.equals(f)) {
			return ContentType.create("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		} else if (Format.DOC.equals(f)) {
			return ContentType.create("application/msword");
		}
		return null;
	}

	private void checkParameters(Format fromFormat, Format toFormat) throws ConversionException {
		if (URL == null) {
			throw new ConversionException("Endpoint URL not configured.");
		}

		if (Format.DOCX.equals(fromFormat) || Format.DOC.equals(fromFormat)) {
			// OK
		} else {
			throw new ConversionException("Conversion from format " + fromFormat + " not supported");
		}

		if (Format.PDF.equals(toFormat) || Format.TOC.equals(toFormat) || Format.DOCX.equals(toFormat)) {
			// OK
		} else {
			throw new ConversionException("Conversion to format " + toFormat + " not supported");
		}
	}
}
