package com.rhc.hadoop;


import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.map.ReducerFact;
import com.rhc.hadoop.model.Transaction;
import com.rhc.hadoop.model.TransactionCreditCardReducer;

public class TestReducerRules {

	@Test
	public void testSimpleMapping() {
		ReducerFact reducee = new TransactionCreditCardReducer("1234");
		String[] values = {"1234,Durham,NC,20.0","1234,Portland,OR,40.0"};
    	
    	for (String value : values) {
    		KieSession kieSession = DroolsHelper.createKieSession();
            kieSession.insert(reducee);
            kieSession.insert(new Transaction(value.toString()));
            kieSession.getAgenda().getAgendaGroup("reducer-rules").setFocus();
            kieSession.fireAllRules();
            kieSession.dispose();
    	}

		Assert.assertEquals("60.0", reducee.getValue());
	}
}
