package org.example;

public class Main {
    public static void main(String[] args) {


        KafkaConsument kafkaConsument = new KafkaConsument(2);
        kafkaConsument.initConsumers();
        kafkaConsument.consumeTopicByAllConsumers();
    }
}
