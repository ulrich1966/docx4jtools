package de.juli.docx4j.service;

import java.nio.file.Path;

public interface CreateService {
	public Path create(Path target) throws Exception;
}
