package com.rhc.hadoop;


import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.map.MapperFact;
import com.rhc.hadoop.model.Transaction;
import com.rhc.hadoop.model.TransactionMapper;

public class TestMapperRules {

	@Test
	public void testSimpleMapping() {
		MapperFact<Transaction> entry = new TransactionMapper(new Transaction("1234,Charlotte,NC,43.24"));

		KieSession kieSession = DroolsHelper.createKieSession();
		kieSession.insert(entry);
		kieSession.getAgenda().getAgendaGroup("mapper-rules").setFocus();
		kieSession.fireAllRules();
		kieSession.dispose();

		Assert.assertEquals("1234", entry.getKey());
	}
}
