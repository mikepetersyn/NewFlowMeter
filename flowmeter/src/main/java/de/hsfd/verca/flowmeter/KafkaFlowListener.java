package de.hsfd.verca.flowmeter;

import de.hsfd.verca.flowmeter.jnetpcap.BasicFlow;
import de.hsfd.verca.flowmeter.jnetpcap.worker.FlowGeneratorListener;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;

public class KafkaFlowListener implements FlowGeneratorListener {

    private final Producer<Long, GenericRecord> producer;
    private final Schema schema;
    private final String topic;

    public KafkaFlowListener(Producer<Long, GenericRecord> producer, Schema schema, String topic) {
        this.producer = producer;
        this.topic = topic;
        this.schema = schema;
    }

    @Override
    public void onFlowGenerated(BasicFlow flow) {
        producer.send(createRecord(flow));
    }

    public ProducerRecord<Long, GenericRecord> createRecord(BasicFlow flow) {
        ArrayList<Double> flowDump = flow.dumpFlowBasedFeaturesTransformed();
        GenericData.Record record = new GenericData.Record(schema);
        record.put("flow_record", flowDump);
        System.out.println(record);
        return new ProducerRecord<>(topic, record);
    }
}
