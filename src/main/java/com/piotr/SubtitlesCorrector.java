package com.piotr;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

public class SubtitlesCorrector {

	private static final List<String> ENDINGS = new ArrayList<>();
	static {
		ENDINGS.add(".srt");
		ENDINGS.add(".txt");
	}

	private static boolean filter(Path path, BasicFileAttributes attribute) {
		String fileName = path.getFileName().toString();

		for (String ending : ENDINGS) {
			if (fileName.endsWith(ending)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		List<Path> paths = Files.find(Paths.get("."), 1, SubtitlesCorrector::filter).collect(Collectors.toList());

		String message;
		if (paths.isEmpty()) {
			message = "Subtitles not found";
		} else {
			message = "Processing files: ";
			for (Path path : paths) {
				process(path);
				message = message + path.getFileName() + ";";
			}
		}

		JOptionPane.showMessageDialog(null, message);
	}

	private static void process(Path path) throws IOException {

		List<String> lines = Files.readAllLines(path);

		try (PrintWriter writer = new PrintWriter(
				Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING))) {
			for (String line : lines) {
				// @formatter:off
				writer.println(line
						.replace('¹', 'ą')
						.replace('æ', 'ć')
						.replace('ê', 'ę')
						.replace('³', 'ł')
						.replace('£', 'Ł')
						.replace('ñ', 'ń')
						.replace('œ', 'ś')
						.replace('Œ', 'Ś')
						.replace('Ÿ', 'ź')
						.replace('¿', 'ż')
						.replace('¯', 'Ż')
					    .replace('„', '\"')
					);
				// @formatter:on
			}

		}
	}

}
