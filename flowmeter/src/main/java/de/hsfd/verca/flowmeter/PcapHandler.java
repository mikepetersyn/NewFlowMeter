package de.hsfd.verca.flowmeter;


import de.hsfd.verca.flowmeter.jnetpcap.BasicPacketInfo;
import de.hsfd.verca.flowmeter.jnetpcap.FlowGenerator;
import de.hsfd.verca.flowmeter.jnetpcap.PacketReader;
import de.hsfd.verca.flowmeter.jnetpcap.worker.FlowGeneratorListener;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.jnetpcap.PcapClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class PcapHandler {

    public static final Logger log = LoggerFactory.getLogger(PcapHandler.class);

    private File pcapFile;
    private FlowGenerator flowGenerator;
    private OutputAccountant outputAccountant;
    private PacketReader packetReader;
    private final static String PCAP = "application/vnd.tcpdump.pcap";

    PcapHandler(String pcapPath, FlowGeneratorListener flowGeneratorListener) {
        flowGenerator = getFlowGenerator(flowGeneratorListener);
        pcapFile = new File(pcapPath);
        outputAccountant = new OutputAccountant();
        handlePcap();
    }

    public FlowGenerator getFlowGenerator(FlowGeneratorListener flowGeneratorListener) {
        FlowGenerator flowGenerator = new FlowGenerator(true, FlowConfig.flowTimeout, FlowConfig.activityTimeout);
        flowGenerator.addFlowListener(flowGeneratorListener);
        return flowGenerator;
    }

    private void handlePcap() {
        if (!pcapFile.exists())
            log.info("The pcap file or folder does not exist! -> {}", pcapFile.getPath());
        else if (pcapFile.isDirectory())
            getPcapFilesFromDirectory(pcapFile);
        else
            readPcapFile(pcapFile.getPath());
    }

    private void getPcapFilesFromDirectory(File inputPath) {
        File[] pcapFiles = inputPath.listFiles(PcapHandler::isPcapFile);
        if (pcapFiles == null)
            log.info("The given directory does not exist.");
        else
            readPcapFiles(pcapFiles);
    }

    private void readPcapFiles(File[] pcapFiles) {
        for (File pcap : pcapFiles) {
            if (!pcap.isDirectory())
                readPcapFile(pcap.getPath());
        }
    }

    private void readPcapFile(String inputFile) {
        boolean readIP6 = false;
        boolean readIP4 = true;
        packetReader = new PacketReader(inputFile, readIP4, readIP6);
        boolean pcapFinished = false;
        while (!pcapFinished) {
            pcapFinished = readPacket();
        }
    }

    public boolean readPacket() {
        try {
            BasicPacketInfo packet = getNextPacket();
            if (packet != null)
                process(packet);
            else
                outputAccountant.increaseDiscarded();

        } catch (PcapClosedException e) {
            return true;
        }
        return false;
    }

    public BasicPacketInfo getNextPacket() throws PcapClosedException {
        BasicPacketInfo basicPacket = packetReader.nextPacket();
        outputAccountant.increaseTotal();
        return basicPacket;
    }

    public void process(BasicPacketInfo packet) {
        flowGenerator.addPacket(packet);
        outputAccountant.increaseValid();
    }


    private static void readPcapFile(String inputFile, String outPath) {
        String fileName = FilenameUtils.getName(inputFile);
    }

    public static boolean isPcapFile(File file) {

        if (file == null) {
            return false;
        }

        try {

            String contentType;

            contentType = new Tika().detect(file);

            if (PCAP.equalsIgnoreCase(contentType)) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        return false;
    }


}
