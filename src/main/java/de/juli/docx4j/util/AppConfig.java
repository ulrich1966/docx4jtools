package de.juli.docx4j.util;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
	public static final String DOC_ROOT;
	public static final boolean IS_TEST = true;
	public static final String LO_PATH = "C:/Program Files (x86)/LibreOffice 5";

	static {
		DOC_ROOT = AppConfig.class.getResource("/").toString();
	}

	public static Path getRootAsPath() {
		return Paths.get(URI.create(DOC_ROOT));
	}
}
