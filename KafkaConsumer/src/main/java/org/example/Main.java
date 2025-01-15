package org.example;

public class Main {
    public static void main(String[] args) {
        KafkaConsumerManager kafkaConsumerManager = new KafkaConsumerManager(2); // 2 consumers
        kafkaConsumerManager.initConsumers();
        kafkaConsumerManager.consumeTopicByAllConsumers();
    }
}
