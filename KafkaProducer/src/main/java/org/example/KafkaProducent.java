package org.example;

import lombok.Getter;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.example.model.Transaction;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Getter
public class KafkaProducent {
    static KafkaProducer<UUID, String> kafkaProducer;
    private final String RENT_TOPIC = "reservations";

    public KafkaProducent() throws ExecutionException, InterruptedException {
        initProducer();
        createTopic();
    }

    private static void initProducer() {
        if (kafkaProducer == null) {
            Properties producerConfig = new Properties();
            producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
            producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
            producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
            producerConfig.put(ProducerConfig.ACKS_CONFIG, "all");
            producerConfig.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // zapewnienie, że dane będą dostarczone tylko 1 raz
            kafkaProducer = new KafkaProducer<>(producerConfig);
        }
    }

    public KafkaProducer<UUID, String> getKafkaProducer() {
        return kafkaProducer;
    }

    public void sendTransactionAsync(Transaction transaction) throws InterruptedException {
        Jsonb jsonb = JsonbBuilder.create();
        Callback callback = this::onCompletion;

        // Serializacja obiektu Transaction na JSON
        String jsonTransaction = jsonb.toJson(transaction +" nazwa - budynki i lokale");
        System.out.println(jsonTransaction);

        // Wysyłamy wiadomość do Kafka z obiektem Transaction
        ProducerRecord<UUID, String> record = new ProducerRecord<>(RENT_TOPIC, transaction.getEntityId(), jsonTransaction);
        kafkaProducer.send(record, callback);
    }

    private void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            System.out.printf("Message sent to topic=%s, partition=%d, offset=%d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } else {
            System.err.println("Error sending message: " + exception.getMessage());
        }
    }

    public void createTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        int partitionsNumber = 5;
        short replicationFactor = 3;

        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(RENT_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(RENT_TOPIC);
            futureResult.get();
        } catch (ExecutionException e) {
            System.out.println("Topic already exists" + e.getCause());
        }
    }

    public void close() {
        if (kafkaProducer != null) {
            kafkaProducer.close();
        }
    }
}



