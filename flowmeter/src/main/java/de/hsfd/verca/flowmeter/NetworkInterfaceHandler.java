package de.hsfd.verca.flowmeter;

import de.hsfd.verca.flowmeter.jnetpcap.FlowGenerator;
import de.hsfd.verca.flowmeter.jnetpcap.PacketReader;
import de.hsfd.verca.flowmeter.jnetpcap.worker.FlowGeneratorListener;
import org.jnetpcap.Pcap;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class NetworkInterfaceHandler {

    public static final Logger log = LoggerFactory.getLogger(NetworkInterfaceHandler.class);

    private final String networkInterface;
    private final FlowGenerator flowGenerator;
    private StringBuilder messageBuffer;

    public NetworkInterfaceHandler(String networkInterface, FlowGeneratorListener flowGeneratorListener) {
        this.networkInterface = networkInterface;
        flowGenerator = getFlowGenerator(flowGeneratorListener);
        messageBuffer = new StringBuilder();
        handleNetworkInterface();
    }

    public FlowGenerator getFlowGenerator(FlowGeneratorListener flowGeneratorListener) {
        FlowGenerator flowGenerator = new FlowGenerator(true, FlowConfig.flowTimeout, FlowConfig.activityTimeout);
        flowGenerator.addFlowListener(flowGeneratorListener);
        return flowGenerator;
    }

    public void handleNetworkInterface() {
        Optional<Pcap> pcap = openNetworkInterface();
        if (pcap.isPresent())
            listenOnInterface(pcap.get());
        else
            handleEmptyPcapBuffer();
    }

    public void handleEmptyPcapBuffer() {
        log.info("open {} fail -> {}", networkInterface, messageBuffer.toString());
    }

    public Optional<Pcap> openNetworkInterface() {
        return Optional.ofNullable(getPcapBuffer());
    }

    public Pcap getPcapBuffer() {
        return Pcap.openLive(
                networkInterface,
                InterfaceConfig.snaplen,
                Pcap.MODE_PROMISCUOUS,
                InterfaceConfig.timeout,
                messageBuffer);
    }

    public void listenOnInterface(Pcap pcap) {
        int returnCode = pcap.loop(
                Pcap.DISPATCH_BUFFER_FULL,
                getPacketHandler(),
                networkInterface);
        stopListening(returnCode);
    }

    public PcapPacketHandler<String> getPacketHandler() {
        return (packet, user) -> flowGenerator.addPacket(PacketReader.getBasicPacketInfo(
                getPacketCopy(packet), true, false));
    }

    public PcapPacket getPacketCopy(PcapPacket packetToCopy) {
        PcapPacket newPacket = new PcapPacket(JMemory.Type.POINTER);
        packetToCopy.transferStateAndDataTo(newPacket);
        return newPacket;
    }

    public void stopListening(int returnCode) {
        switch (returnCode) {
            case 0:
                messageBuffer.append("listening finished.");
            case -1:
                messageBuffer.append("listening error.");
            case -2:
                messageBuffer.append("stop listening.");
        }
    }

}
