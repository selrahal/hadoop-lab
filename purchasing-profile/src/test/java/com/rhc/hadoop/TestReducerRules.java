package com.rhc.hadoop;


import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.entry.Entry;
import com.rhc.hadoop.drools.entry.RunningReduce;

public class TestReducerRules {

	@Test
	public void testSimpleMapping() {
		Entry one = new Entry("1234", "20.0");
		Entry two = new Entry("1234", "40.0");
		
		RunningReduce reducee = new RunningReduce(one.key, one.value);
    	
    		KieSession kieSession = DroolsHelper.createKieSession();
            kieSession.insert(reducee);
            kieSession.insert(two);
            kieSession.getAgenda().getAgendaGroup("reducer-rules").setFocus();
            kieSession.fireAllRules();
            kieSession.dispose();

		Assert.assertEquals("60.0", reducee.value);
	}
}
