package com.rhc.hadoop;


import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.entry.Collector;
import com.rhc.hadoop.drools.entry.Entry;

public class TestMapperRules {

	@Test
	public void testSimpleMapping() {
		Collector collector = new Collector();
        
		Entry entry = new Entry("1","1234,Charlotte,NC,43.24");

		KieSession kieSession = DroolsHelper.createKieSession();
		kieSession.setGlobal("collector", collector);
		kieSession.insert(entry);
		kieSession.getAgenda().getAgendaGroup("mapper-rules").setFocus();
		kieSession.fireAllRules();
		kieSession.dispose();

		Entry out = collector.iterator().next();
		Assert.assertEquals("1234", out.key);
	}
}
