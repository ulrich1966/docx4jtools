package de.juli.docx4j.service;

import java.nio.file.Path;

import de.juli.docx4j.service.services.Docx4JService;

public abstract class Service {

	protected Path source;
	protected Docx4JService service;

	public Service(Path path) throws Exception{
		super();
		if(null != path){
			this.source = path;
			this.service =  Docx4JService.getInstance(path);			
		}
	}

	public Path getSource() {
		return source;
	}

	public void setSource(Path source) {
		this.source = source;
	}

	public Docx4JService getService() {
		return service;
	}

	public void setService(Docx4JService service) {
		this.service = service;
	}
	
}
