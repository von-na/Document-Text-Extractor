package com.von.textextract;

import com.von.textextract.config.LoggingConfig;
import picocli.CommandLine;

class Application {
    static void main(String[] args) {
        LoggingConfig.init();
        int exitCode = new CommandLine(new CLIHelper()).execute(args);
        System.exit(exitCode);
    }
}
