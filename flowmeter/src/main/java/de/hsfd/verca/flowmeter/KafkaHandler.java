package de.hsfd.verca.flowmeter;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.io.File;
import java.io.IOException;

public class KafkaHandler {

    private String[] kafkaOptionValues;

    KafkaHandler(String[] kafkaOptionValues) {
        this.kafkaOptionValues = kafkaOptionValues;
    }

    private String getKafkaBroker() {
        return kafkaOptionValues[0];
    }

    private String getKafkaTopic() {
        return kafkaOptionValues[1];
    }

    private String getKafkaGroupId() {
        return kafkaOptionValues[2];
    }

    private KafkaProducer<Long, GenericRecord> getKafkaProducer() {
        return new KafkaAvroProducer.Builder<Long, GenericRecord>()
                .setKafkaBroker(getKafkaBroker())
                .setKeySerializer(new LongSerializer())
                .setValueSerializer(new AvroSerializer())
                .build();
    }


    private Schema getAvroSchema() {
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File(KafkaConfig.AVRO_SCHEMA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schema;
    }

    public KafkaFlowListener getKafkaFlowListener() {
        return new KafkaFlowListener(getKafkaProducer(), getAvroSchema(), getKafkaTopic());
    }

}
