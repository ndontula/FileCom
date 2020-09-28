package com.cigniti.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cigniti.compare.cli.CliArguments;
import com.cigniti.compare.cli.CliArgumentsImpl;
import com.cigniti.compare.cli.CliArgumentsParseException;
import com.cigniti.compare.cli.CliComparator;
import com.cigniti.compare.ui.Display;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            final CliArguments cliArguments = new CliArgumentsImpl(args);

            if (cliArguments.areAvailable()) {
                System.exit(startCLI(cliArguments));
            } else if (cliArguments.isHelp()) {
                cliArguments.printHelp();
            } else {
                startUI();
            }
        } catch (CliArgumentsParseException exception) {
            LOG.error(exception.getMessage());
        }
    }

    private static void startUI() {
        new Display().init();
    }

    private static int startCLI(CliArguments cliArguments) {
        return new CliComparator(cliArguments).getResult();
    }
}
