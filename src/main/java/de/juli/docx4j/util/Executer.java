package de.juli.docx4j.util;

public enum Executer {
	PDF_EXECUTER("C:/Program Files (x86)/Foxit Software/Foxit PhantomPDF/FoxitPhantomPDF.exe"), 
	PDF_EXECUTER_LT("C:/Program Files (x86)/Foxit Software/Foxit Reader/FoxitReader.exe"), 
	DOCX_EXECUTER("C:/Program Files (x86)/LibreOffice 5/program/sweb.exe"), 
	HTML_EXECUTER("C:/Program Files/Mozilla Firefox/firefox.exe");

	private String executer = "";

	Executer(String executer) {
		this.executer = executer;
	}

	public String getExecuter() {
		return executer;
	}
}
