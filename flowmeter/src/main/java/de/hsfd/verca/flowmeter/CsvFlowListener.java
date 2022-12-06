package de.hsfd.verca.flowmeter;


import de.hsfd.verca.flowmeter.jnetpcap.BasicFlow;
import de.hsfd.verca.flowmeter.jnetpcap.FlowFeature;
import de.hsfd.verca.flowmeter.jnetpcap.worker.FlowGeneratorListener;
import de.hsfd.verca.flowmeter.jnetpcap.worker.InsertCsvRow;

import java.util.ArrayList;
import java.util.List;

public class CsvFlowListener implements FlowGeneratorListener {

    private final String fileName;

    private final String outPath;

    private long cnt;

    public CsvFlowListener(String fileName, String outPath) {
        this.fileName = fileName;
        this.outPath = outPath;
    }

    /*
        public void dumbCsv(){

            flowGen.dumpLabeledCurrentFlow(saveFileFullPath.getPath(), FlowFeature.getHeader());

            long lines = SwingUtils.countLines(saveFileFullPath.getPath());

            System.out.println(String.format("%s is done. total %d flows ",fileName,lines));
        }
    */
    @Override
    public void onFlowGenerated(BasicFlow flow) {
        String flowDump = flow.dumpFlowBasedFeaturesEx();
        List<String> flowStringList = new ArrayList<>();
        flowStringList.add(flowDump);
        InsertCsvRow.insert(FlowFeature.getHeader(), flowStringList, outPath, fileName + FlowMgr.FLOW_SUFFIX);
        cnt++;

        String console = String.format("%s -> %d flows \r", fileName, cnt);

        System.out.print(console);
    }
}
