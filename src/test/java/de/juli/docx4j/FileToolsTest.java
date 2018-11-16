package de.juli.docx4j;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.juli.docx4j.util.FileTools;

public class FileToolsTest {
	private static final Logger LOG = LoggerFactory.getLogger(FileToolsTest.class);
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Path source = Paths.get("J:/workspace/docx4jtools/target/test-classes/docs/anschreiben_Ich AG.docx");
		Path target= FileTools.targetFromSource(source, "pdf");
		LOG.debug("{}", source);
		LOG.debug("{}", target);
	}

}
