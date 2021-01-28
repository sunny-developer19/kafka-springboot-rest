package com.threerivers.bankingapi.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.log.LogAccessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AbstractConsumerSeekAware;
import org.springframework.kafka.listener.ListenerUtils;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import com.threerivers.bankingapi.models.Transaction;
import com.threerivers.bankingapi.services.TransactionService;



public class TransactionConsumer extends AbstractConsumerSeekAware {
	private KafkaConsumerErrorHandler errorHandler;
	private TransactionService service;
	private Long nackInterval;
	private String topic;

	@Autowired
	public TransactionConsumer(KafkaConsumerErrorHandler errorHandler,
							   TransactionService service,
							   @Value("${kafka.nackInterval}") Long nackInterval,
							   @Value("${kafka.transaction.topicName}") String topic) {
		this.errorHandler=errorHandler;
		this.service=service;
		this.nackInterval=nackInterval;
		this.topic=topic;
	}

	@KafkaListener(topics = "${kafka.transaction.topicName}", 
			groupId = "${kafka.transaction.groupId}",
			containerFactory = "kafkaListenerContainerFactory")
	public void listener(ConsumerRecords<String, Transaction> records, Acknowledgment acknowledgment) {
		boolean acked=false;
		try {
			List<ConsumerRecord<String, Transaction>> validRecords = new ArrayList<>();
			validRecords=filter(records);

			int index=0;
			for(ConsumerRecord<String, Transaction> record: validRecords) {
				try {
					service.save(record.value());

					index++;
				}catch(Exception e) {

					String errorMessage="Failed Record from partition-"+ record.partition()+", offset-"+record.offset();
					errorHandler.handleError(errorMessage, e, record.value());
					acknowledgment.nack(index, nackInterval);
					acked=true;
				}
			}

		} catch(Exception e) {

			acknowledgment.nack(nackInterval);
			acked=true;
		} finally {
			if(!acked) 
				acknowledgment.acknowledge();
		}
	}

	private List<ConsumerRecord<String,Transaction>> filter(ConsumerRecords<String,Transaction> records) {
		List<ConsumerRecord<String,Transaction>> validRecords=new ArrayList<>();
		records.forEach(record -> {
			if(!checkForDeserializationException(record)) {
				validRecords.add(record);
			}
		});
		return validRecords;
	}

	private boolean checkForDeserializationException(ConsumerRecord<String,Transaction> record) {
		LogAccessor logAccessor=new LogAccessor(Transaction.class);
		DeserializationException exception=ListenerUtils.getExceptionFromHeader(record, 
				ErrorHandlingDeserializer.VALUE_DESERIALIZER_EXCEPTION_HEADER, logAccessor);
		if(exception!=null) {
			String errorMessage="Error while deserializing the record" +record.value()+", offset:"+record.offset();
			errorHandler.handleError(errorMessage, exception, record.value());
			return true;
		}
		return false;
	}

	public List<Integer> getCurrentlyAssingedPartitions() {
		return super.getSeekCallbacks().keySet().stream().map(TopicPartition::partition).collect(Collectors.toList());
	}

	public void seekOffsetToBeginning(int partition) {
		super.getSeekCallbackFor(new TopicPartition(topic,partition)).seekToBeginning(topic, partition);
	}

	public void seekOffsetToEnd(int partition) {
		super.getSeekCallbackFor(new TopicPartition(topic,partition)).seekToEnd(topic, partition);
	}

	public void seekToAnOffset(int partition,long offset) {
		super.getSeekCallbackFor(new TopicPartition(topic,partition)).seek(topic, partition, offset);
	}

}
