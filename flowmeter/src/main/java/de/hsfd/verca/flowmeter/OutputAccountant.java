package de.hsfd.verca.flowmeter;

public class OutputAccountant {

    private int numberValidPackets;
    private int numberDiscardedPackets;
    private int numberTotalPackets;

    OutputAccountant() {
        numberValidPackets = 0;
        numberDiscardedPackets = 0;
        numberTotalPackets = 0;
    }

    public void increaseValid() {
        numberValidPackets++;
    }

    public void increaseDiscarded() {
        numberValidPackets++;
    }

    public void increaseTotal() {
        numberTotalPackets++;
    }

    public void printProcessingStats() {
        System.out.println(String.format(
                "Packet Output: Total=%d,Valid=%d,Discarded=%d",
                numberTotalPackets,
                numberValidPackets,
                numberDiscardedPackets));
    }

}
