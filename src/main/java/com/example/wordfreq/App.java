package com.example.wordfreq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * CLI entry point — reads a bundled sample text and prints word frequencies.
 * Supports JSON (default) and CSV output formats.
 *
 * Usage:
 * java -jar app.jar [threshold] [--csv]
 */
public class App {

  public static void main(String[] args) throws IOException {
    String text = loadResource("/sample.txt");

    long threshold = 1;
    boolean csvOutput = false;

    for (String arg : args) {
      if ("--csv".equals(arg)) {
        csvOutput = true;
      } else {
        threshold = Long.parseLong(arg);
      }
    }

    WordFrequencyAnalyzer analyzer = new WordFrequencyAnalyzer();
    Map<String, Long> frequencies = analyzer.analyzeWithThreshold(text, threshold);

    System.out.println("=== Word Frequency Analysis ===");
    System.out.println("Total unique words: " + frequencies.size());
    System.out.println("Minimum frequency threshold: " + threshold);
    System.out.println("Output format: " + (csvOutput ? "CSV" : "JSON"));
    System.out.println();

    if (csvOutput) {
      CsvExporter exporter = new CsvExporter();
      System.out.println(exporter.export(frequencies));
    } else {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      System.out.println(gson.toJson(frequencies));
    }
  }

  private static String loadResource(String path) throws IOException {
    try (InputStream is = App.class.getResourceAsStream(path)) {
      if (is == null) {
        throw new IOException("Resource not found: " + path);
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
