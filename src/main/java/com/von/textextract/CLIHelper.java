package com.von.textextract;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static java.lang.String.format;

@CommandLine.Command(name = "processor", mixinStandardHelpOptions = true)
public class CLIHelper implements Runnable {

    @CommandLine.Spec
    private CommandSpec spec;

    @CommandLine.Option(names = "-i", required = true)
    private String input;

    @CommandLine.Option(names = "-o")
    private String output = "extracted-text.txt";

    @CommandLine.Option(names = "-ocr")
    private boolean ocrEnabled = false;

    @CommandLine.Option(names = "-lang")
    private String lang = "eng";

    private static final String FILE_CONFLICT_MSG = """
            The file '%s' already exists.
            Choose an action: [O]verwrite, [R]ename, or [C]ancel:
            """;

    @Override
    public void run() {
        PrintWriter out = spec.commandLine().getOut();
        String resolvedOutput = resolveOutputPath(output, out);
        if (resolvedOutput == null) {
            out.println("Operation cancelled.");
            return;
        }

        ExtractorService extractorService = new ExtractorService();
        Processor processor = new Processor(extractorService);
        processor.process(input, resolvedOutput, ocrEnabled, lang);
    }

    private String resolveOutputPath(String outputFile, PrintWriter out) {
        Scanner scanner = new Scanner(System.in);
        String current = outputFile;

        while (Files.exists(Path.of(current))) {
            out.printf(format(FILE_CONFLICT_MSG, current));
            out.flush();
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("O")) {
                return current;
            } else if (answer.equalsIgnoreCase("C")) {
                return null;
            } else if (answer.equalsIgnoreCase("R")) {
                out.print("Enter new file name: ");
                out.flush();
                current = scanner.nextLine().trim();
            } else {
                out.println("Invalid input.");
            }
        }
        scanner.close();
        return current;
    }
}
