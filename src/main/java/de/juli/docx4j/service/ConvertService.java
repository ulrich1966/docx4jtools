package de.juli.docx4j.service;

import java.nio.file.Path;

public interface ConvertService {
	public Path create(Path target) throws Exception;
}
