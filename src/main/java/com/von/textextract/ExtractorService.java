package com.von.textextract;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.config.loader.TikaLoader;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;

@Slf4j
public class ExtractorService {
    private final TikaLoader tikaLoader;

    public ExtractorService() {
        this.tikaLoader = TikaLoader.loadDefault(getClass().getClassLoader());
        log.debug("TikaLoader initialized with default configuration");
    }

    public void extractTextStreaming(String filePath, Writer writer, boolean ocrEnabled,
            String lang) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {

            log.info("Starting text extraction from the document");

            try (TikaInputStream tikaInputStream = TikaInputStream.get(fileInputStream)) {

                Parser parser = tikaLoader.loadAutoDetectParser();
                Metadata metadata = new Metadata();
                ContentHandler handler = new StreamingBodyContentHandler(writer);
                ParseContext parseContext = tikaLoader.loadParseContext();

                TesseractOCRConfig tesseractOCRConfig = new TesseractOCRConfig();
                tesseractOCRConfig.setLanguage(lang);
                tesseractOCRConfig.setSkipOcr(!ocrEnabled);

                parseContext.set(TesseractOCRConfig.class, tesseractOCRConfig);
                parseContext.set(Parser.class, parser);

                parser.parse(tikaInputStream, handler, metadata, parseContext);

                writer.flush();

                log.info("Text extraction completed for (pages/slides: {}, content-type: {})",
                        metadata.get("xmpTPg:NPages"),
                        metadata.get(Metadata.CONTENT_TYPE));

            } catch (Exception e) {
                log.error("Error extracting text from document!", e);
                throw new IOException("Failed to extract text from document: " + e.getMessage(), e);
            }
        }
    }

    private static class StreamingBodyContentHandler extends BodyContentHandler {
        public StreamingBodyContentHandler(Writer writer) {
            super(writer);
        }
    }
}
