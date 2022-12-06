package de.hsfd.verca.flowmeter.jnetpcap.worker;

import de.hsfd.verca.flowmeter.jnetpcap.BasicFlow;

public interface FlowGeneratorListener {
    void onFlowGenerated(BasicFlow flow);
}
