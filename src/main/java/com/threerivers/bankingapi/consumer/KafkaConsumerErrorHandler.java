package com.threerivers.bankingapi.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.threerivers.bankingapi.models.Account;
import com.threerivers.bankingapi.models.Transaction;



@Component

public class KafkaConsumerErrorHandler {
	
	@Autowired
	@Qualifier("transObjectMapper")
	private ObjectMapper mapper;
	
	
	
	public void handleError(String customErrorMessage, Exception e, Transaction failedRecord) {
		String message=null;
		try {
			message = customErrorMessage+ "\n Record:"+mapper.writeValueAsString(failedRecord) +
					"\n Stacktrace:" +getExceptionStacktrace(e);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void handleError(String customErrorMessage, Exception e, Account failedRecord) {
		String message=null;
		try {
			message = customErrorMessage+ "\n Record:"+mapper.writeValueAsString(failedRecord) +
					"\n Stacktrace:" +getExceptionStacktrace(e);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
	}

	private String getExceptionStacktrace(Exception ex) {
	    StringBuffer sb = new StringBuffer(500);
	    StackTraceElement[] st = ex.getStackTrace();
	    sb.append(ex.getClass().getName() + ": " + ex.getMessage() + "\n");
	    for (int i = 0; i < st.length; i++) {
	      sb.append("\t at " + st[i].toString() + "\n");
	    }
	    return sb.toString();
	}
}
