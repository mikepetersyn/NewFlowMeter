package de.hsfd.verca.flowmeter;


import java.io.File;

import static de.hsfd.verca.flowmeter.jnetpcap.Utils.FILE_SEP;


public class CsvHandler {

    private String[] csvOptionValues;

    private String csvPath;
    private String fileName;

    CsvHandler(String[] csvOptionValues) {
        this.csvOptionValues = csvOptionValues;
        this.csvPath = getCsvPath();
        this.fileName = getCsvFilename();
    }

    private String getCsvPath() {
        return csvOptionValues[0];
    }

    private String getCsvFilename() {
        return csvOptionValues[1];
    }

    public void handleCsv() {
        if (!csvPath.endsWith(FILE_SEP)) {
            csvPath += FILE_SEP;
        }

        File saveFileFullPath = new File(csvPath + fileName + FlowMgr.FLOW_SUFFIX);

        if (saveFileFullPath.exists()) {
            if (!saveFileFullPath.delete()) {
                System.out.println("Save file can not be deleted");
            }
        }
    }


    public CsvFlowListener getCsvFlowListener() {
        return new CsvFlowListener(fileName, csvPath);
    }


}
