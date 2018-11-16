package de.juli.docx4j.util;

import java.nio.file.Path;

import com.google.common.io.Files;

public class FileTools {

	public static Path targetFromSource(Path source, String newExention) {
		String name = name(source);
		name = name.replaceAll(extension(name), newExention);
		Path target = source.getParent().resolve(name.toString());
		return target;
	}

	public static String name(Path source) {
		String name = source.getName(source.getNameCount() - 1).toString();
		return name;
	}

	public static String extension(String name) {
		String extension = Files.getFileExtension(name);
		return extension;
	}

	public static Path temp(Path source) {
		return source.getParent().resolve(String.format("temp", name(source)));
	}

	public static Path temp(Path source, String name) {
		return source.getParent().resolve(name);
	}
}

