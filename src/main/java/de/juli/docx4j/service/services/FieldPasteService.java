package de.juli.docx4j.service.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger.OutputField;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.service.Service;
import de.juli.docx4j.util.FileTools;

public class FieldPasteService extends Service {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FieldPasteService.class);
	
	public FieldPasteService(Path source) throws Exception {
		super(source);
	}

	public WordprocessingMLPackage paste(Map<DataFieldName, String> data, WordprocessingMLPackage pack) throws Docx4JException {
		List<Map<DataFieldName, String>> list = new ArrayList<Map<DataFieldName, String>>();
		list.add(data);
		org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(OutputField.REMOVED);
		org.docx4j.model.fields.merge.MailMergerWithNext.performLabelMerge(pack, list);
		return pack;
	}
	
	public Path paste(Map<String, String> data) throws Exception {
		String name = FileTools.name(source);
		String ext = FileTools.extension(name);
		Path target = source.getParent().resolve(String.format("gen%s.%s", name, ext));
		return paste(data, target);
	}

	public Path paste(Map<String, String> data, Path target) throws Exception {
		service.getRootDocPart().variableReplace(data);
		return service.save(target);
	}
}
