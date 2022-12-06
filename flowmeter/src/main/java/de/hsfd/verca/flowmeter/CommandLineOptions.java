package de.hsfd.verca.flowmeter;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandLineOptions {

    private final Options options;
    private final Option[] optionList;

    public CommandLineOptions() {
        options = new Options();
        optionList = initializeOptions();
        addOptions();
    }

    private Option[] initializeOptions() {
        Option[] optionList = new Option[4];
        optionList[0] = getOptionPcap();
        optionList[1] = getOptionInterface();
        optionList[2] = getOptionKafka();
        optionList[3] = getOptionCsv();
        return optionList;
    }

    private void addOptions() {
        for (Option o : optionList) {
            options.addOption(o);
        }
    }

    private Option getOptionPcap() {
        return Option.builder("p")
                .longOpt("pcap")
                .desc("reads pcap files")
                .hasArg(true)
                .argName("pcap-path")
                .build();
    }

    private Option getOptionInterface() {
        return Option.builder("i")
                .longOpt("interface")
                .desc("listens on interface")
                .hasArg(true)
                .argName("interface-name")
                .build();
    }

    private Option getOptionKafka() {
        return Option.builder("k")
                .longOpt("kafka")
                .desc("produces flows into a specified kafka topic")
                .hasArgs()
                .numberOfArgs(3)
                .argName("broker> <topic> <group-id")
                .build();
    }

    private Option getOptionCsv() {
        return Option.builder("f")
                .longOpt("csv-file")
                .desc("writes flows into a csv file")
                .hasArg()
                .numberOfArgs(2)
                .argName("output-path> <filename")
                .build();
    }

    public Options getOptions() {
        return this.options;
    }
}
