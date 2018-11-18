package de.juli.docx4j.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public abstract class CreateService extends Service implements Create {
	
	public CreateService(Path target) throws Exception{
		super(target);
	}
	
	@Override
	public void open(Path target) throws FileNotFoundException {
		if(!target.toFile().exists()) {
			throw new FileNotFoundException(String.format("Die angegebene Datei: [%s] konnte nicht gefunden werden!", target.toString()));
		}
	}
}
