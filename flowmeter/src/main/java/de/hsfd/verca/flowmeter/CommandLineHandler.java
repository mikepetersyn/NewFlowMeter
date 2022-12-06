package de.hsfd.verca.flowmeter;


import de.hsfd.verca.flowmeter.jnetpcap.worker.FlowGeneratorListener;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandLineHandler {

    public static final Logger log = LoggerFactory.getLogger(CommandLineHandler.class);
    private CommandLine commandLine;
    private String pcapPath;
    private String csvPath;
    private PcapHandler pcapHandler;
    private NetworkInterfaceHandler interfaceHandler;

    CommandLineHandler(String[] args, Options options) throws ParseException {
        commandLine = getDefaultParser().parse(options, args);
    }

    private static CommandLineParser getDefaultParser() {
        return new DefaultParser();
    }

    public void handleCommandLineOptions() {
        FlowGeneratorListener listener = handleSinkOptions();
        if (listener != null)
            handleSourceOptions(listener);
        else
            log.info("No sink option selected.");
    }

    private void handleSourceOptions(FlowGeneratorListener listener) {
        if (commandLine.hasOption("p"))
            handlePcapOption(listener);
        else if (commandLine.hasOption("i"))
            handleInterfaceOption(listener);
        else
            log.info("No source option selected.");
    }

    private FlowGeneratorListener handleSinkOptions() {
        if (commandLine.hasOption("k"))
            return handleKafkaOption();
        else if (commandLine.hasOption("f"))
            return handleCsvOption();
        return null;
    }

    private void handlePcapOption(FlowGeneratorListener listener) {
        pcapPath = commandLine.getOptionValue("pcap");
        if (pcapPath == null)
            log.info("Please select pcap!");
        else
            pcapHandler = new PcapHandler(pcapPath, listener);
    }

    private void handleInterfaceOption(FlowGeneratorListener listener) {
        String networkInterface = commandLine.getOptionValue("i");
        if (networkInterface == null)
            log.info("Please select network interface!");
        else
            interfaceHandler = new NetworkInterfaceHandler(networkInterface, listener);
    }

    private FlowGeneratorListener handleKafkaOption() {
        String[] kafkaOptionValues = commandLine.getOptionValues("k");
        if (kafkaOptionValues == null)
            log.info("Please select kafka parameters!");
        else
            return new KafkaHandler(kafkaOptionValues).getKafkaFlowListener();
        return null; //avoid this
    }

    private FlowGeneratorListener handleCsvOption() {
        String[] csvOptionValues = commandLine.getOptionValues("f");
        return new CsvHandler(csvOptionValues).getCsvFlowListener();

    }


}
