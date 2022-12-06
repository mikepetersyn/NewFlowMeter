package de.hsfd.verca.flowmeter;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws ParseException {

        CommandLineOptions commandLineOptions = new CommandLineOptions();
        Options options = commandLineOptions.getOptions();
        CommandLineHandler handler = new CommandLineHandler(args, options);
        handler.handleCommandLineOptions();


    }

}
