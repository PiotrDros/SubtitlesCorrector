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
		Optional<Path> first = Files.find(Paths.get("."), 1, SubtitlesCorrector::filter).findFirst();

		String message;
		if (first.isPresent()) {
			Path path = first.get();
			message = "Processing with file: " + path;

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
		} else {
			message = "Subtitles not found";
		}

		JOptionPane.showMessageDialog(null, message);
	}

}
