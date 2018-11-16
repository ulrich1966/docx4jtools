package de.juli.docx4j;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.juli.docx4j.util.AppConfig;
import de.juli.docx4j.util.ExecProcess;
import de.juli.docx4j.util.Executer;

public abstract class ServiceTest {
	protected static final Path DOC_ROOT = Paths.get(URI.create(AppConfig.DOC_ROOT.toString())).resolve("docs");
	protected boolean docxOut = false;
	protected boolean pdfOut = false;
	protected boolean htmlOut = false;
	
	public void openProcess(Executer exec, String param) {
		ExecProcess process = ExecProcess.getInstance(exec, param);
		process.start();	
	}

	public boolean isDocxOut() {
		return docxOut;
	}

	public void setDocxOut(boolean docxOut) {
		this.docxOut = docxOut;
	}

	public boolean isPdfOut() {
		return pdfOut;
	}

	public void setPdfOut(boolean pdfOut) {
		this.pdfOut = pdfOut;
	}

	public boolean isHtmlOut() {
		return htmlOut;
	}

	public void setHtmlOut(boolean htmlOut) {
		this.htmlOut = htmlOut;
	}
}
