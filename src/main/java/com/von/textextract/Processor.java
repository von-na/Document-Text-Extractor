package com.von.textextract;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Processor {
    private final ExtractorService extractorService;

    public Processor(ExtractorService extractorService) {
        this.extractorService = extractorService;
    }

    public void process(String input, String output, boolean ocrEnabled, String lang) {
        log.info("Input: {}", Path.of(input).toAbsolutePath());
        Path outputPath = Path.of(output);
        log.info("Output: {}", outputPath.toAbsolutePath());

        try (BufferedWriter writer =
                Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {

            extractorService.extractTextStreaming(input, writer, ocrEnabled, lang);

        } catch (IOException e) {
            log.error("Failed to process file", e);
        }
    }
}
