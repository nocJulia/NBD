package org.example;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class KafkaConsument {
    private final List<KafkaConsumer<UUID, String>> kafkaConsumers = new ArrayList<>();
    private final String RENT_TOPIC = "reservations";
    int numCunsumers;
    private final MessageSaver messageSaver;

    public KafkaConsument(int numConsumers) {
        this.numCunsumers = numConsumers;
        messageSaver = new MessageSaver();
    }

    public void initConsumers() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "group-reservations");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192,kafka2:9292,kafka3:9392");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        if(kafkaConsumers.isEmpty()) {
            for (int i = 0; i < numCunsumers; i++) {
                KafkaConsumer<UUID, String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
                kafkaConsumer.subscribe(Collections.singleton(RENT_TOPIC));
                kafkaConsumers.add(kafkaConsumer);
                System.out.println("creating consumers");
            }
        }
    }

    public void consume(KafkaConsumer<UUID, String> consumer) {
        boolean saved = false;

        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssignment = consumer.assignment();
            consumer.seekToBeginning(consumerAssignment);
            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat formatter = new MessageFormat("Konsument {5}, Temat {0}, partycja {1}, offset {2, number, integer}, klucz {3}, wartość {4}");

            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);

                for (ConsumerRecord<UUID, String> record : records) {
                    String result = formatter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });

                    if (!saved) {
                        messageSaver.saveToMongo(record.value());
                    }
                    saved = true;
                    System.out.println(result);
                    consumer.commitAsync(); //zapewnienie, że komunikaty będą zawsze dostarczone tylko raz
                }
            }
        } catch (WakeupException we) {
            System.out.println("Job Finished");
        }
    }
    public void consumeTopicByAllConsumers() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(KafkaConsumer<UUID,String>consumer:kafkaConsumers){
            executorService.execute( () -> consume(consumer) );
        }
        /*Thread.sleep(100000);
        for(KafkaConsumer<UUID,String>consumer:kafkaConsumers){
            consumer.wakeup();
        }
        executorService.shutdown(); */
    }

}