package de.hsfd.verca.flowmeter;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Properties;

public class KafkaAvroProducer {

    public static class Builder<K, V> {

        private String kafkaBroker;

        private String clientId;

        private Serializer<K> keySerializer;

        private AvroSerializer valueSerializer;

        public Builder<K, V> setKafkaBroker(String kafkaBroker) {
            this.kafkaBroker = kafkaBroker;
            return this;
        }

        public Builder<K, V> setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder<K, V> setKeySerializer(Serializer<K> keySerializer) {
            this.keySerializer = keySerializer;
            return this;
        }

        public Builder<K, V> setValueSerializer(AvroSerializer valueSerializer) {
            this.valueSerializer = valueSerializer;
            return this;
        }

        private Properties getProperties() {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaBroker);
            //properties.put(ProducerConfig.CLIENT_ID_CONFIG, this.clientId);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, this.keySerializer.getClass().getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, this.valueSerializer.getClass().getName());
            properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, KafkaConfig.AVRO_SCHEMA_URL);
            return properties;
        }

        public KafkaProducer<K, V> build() {
            return new KafkaProducer<>(getProperties());
        }

    }

}
