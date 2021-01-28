package com.threerivers.bankingapi;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.threerivers.bankingapi.models.Account;


@Configuration
@EnableKafka
@ComponentScan({"com.threerivers.bankingapi"})
public class KafkaAccountConsumerConfig {

	@Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;
 
    @Value(value = "${kafka.account.groupId}")
    private String consumerGroupId;
    
    @Value(value = "${kafka.concurrency}")
    private Integer concurrency;
    
    @Value(value = "${kafka.enableAutoCommit}")
    private Boolean consumerEnableAutoCommit;
    
    @Value(value = "${kafka.consumerAutoOffsetReset}")
    private String consumerAutoOffsetReset;
    
    @Value(value = "${kafka.fetchMaxBytesConfig}")
    private Integer fetchMaxBytesConfig;
    
    @Value(value = "${kafka.consumerSessionTimeoutMs}")
    private String consumerSessionTimeoutMs;
    
    @Value(value = "${kafka.consumerMaxPollRecords}")
    private String consumerMaxPollRecords;
    
    @Value(value = "${kafka.consumerPartitionAssignmentStrategy}")
    private String partitionAssignmentStrategy;
    
    @Autowired
    private ObjectMapper om;
    
    @Bean
    public ConsumerFactory<String, Account> kafkaAccountConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Account.class);
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, partitionAssignmentStrategy);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords);
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, fetchMaxBytesConfig);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props
        			,new ErrorHandlingDeserializer<String>(new StringDeserializer())
        			,new ErrorHandlingDeserializer<>(new JsonDeserializer<Account>(Account.class,om,false)));
    }
 
    @Bean("kafkaAccountListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Account> kafkaAccountListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Account> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaAccountConsumerFactory());
        factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
        factory.setConcurrency(concurrency);
        factory.setBatchListener(true);
        return factory;
    }
    

 

}
