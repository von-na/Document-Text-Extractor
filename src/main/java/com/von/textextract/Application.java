package com.von.textextract;

import picocli.CommandLine;

class Application {
    static void main(String[] args) {
        int exitCode = new CommandLine(new CLIHelper()).execute(args);
        System.exit(exitCode);
    }
}
