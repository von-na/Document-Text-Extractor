# text-extract

A command-line tool for extracting text from documents. Built on top of Apache Tika, it handles a wide range of file formats — PDFs, Word documents, spreadsheets, presentations, and more — and can optionally run OCR on image-based content via Tesseract.

---

## Requirements

**Java 17+** must be available on your system.

For OCR support, **Tesseract** must be installed system-wide along with any language data packs you intend to use. Tika invokes the `tesseract` binary directly, so it needs to be on your `PATH`.

Installing Tesseract on common systems:

```sh
# Debian / Ubuntu
sudo apt install tesseract-ocr

# Fedora / RHEL
sudo dnf install tesseract

# Arch
sudo pacman -S tesseract

# macOS
brew install tesseract
```

Language packs follow the pattern `tesseract-ocr-<lang>`. For example, to add German and French support:

```sh
sudo apt install tesseract-ocr-deu tesseract-ocr-fra
```

You can list installed languages with:

```sh
tesseract --list-langs
```

---

## Building

Clone the repository and build the fat JAR with the Gradle wrapper:

```sh
./gradlew shadowJar
```

The output will be at `build/libs/text-extract.jar`.

---

## Usage

```
java -jar text-extract.jar -i <input-file> [options]
```

**Options**

| Flag                | Default              | Description                                                                     |
|---------------------|----------------------|---------------------------------------------------------------------------------|
| `-i <file>`         | *(required)*         | Path to the input document                                                      |
| `-o <file>`         | `extracted-text.txt` | Path to write the extracted text                                                |
| `-ocr`              | `false`              | Enable OCR for image-based content                                              |
| `-lang <code>`      | `eng`                | Tesseract language code(s) for OCR — combine multiple with `+` (e.g. `eng+deu`) |
| `-h, --help`        |                      | Print help and exit                                                             |

---

## Examples

Extract text from a PDF:

```sh
java -jar text-extract.jar -i report.pdf -o report.txt
```

Extract text from a scanned document using OCR:

```sh
java -jar text-extract.jar -i scan.pdf -ocr -o scan.txt
```

OCR with a specific language (German):

```sh
java -jar text-extract.jar -i document.pdf -ocr -lang deu -o document.txt
```

Multiple languages can be combined with a `+` separator:

```sh
java -jar text-extract.jar -i document.pdf -ocr -lang eng+deu -o document.txt
```

If the output file already exists, the tool will prompt you to overwrite it, rename it, or cancel.

---

## Supported Formats

Anything Apache Tika can parse — including but not limited to PDF, DOCX, XLSX, PPTX, ODT, HTML, plain text, and most image formats (when OCR is enabled).

---

## License

See [license](license).
