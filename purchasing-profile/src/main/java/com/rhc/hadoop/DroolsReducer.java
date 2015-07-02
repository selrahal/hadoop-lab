package com.rhc.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.map.ReducerFact;
import com.rhc.hadoop.model.Transaction;
import com.rhc.hadoop.model.TransactionCreditCardReducer;

public class DroolsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context output)
            throws IOException, InterruptedException {
    	ReducerFact reducee = new TransactionCreditCardReducer(key.toString());
    	
    	for (Text value : values) {
    		KieSession kieSession = DroolsHelper.createKieSession();
            kieSession.insert(reducee);
            kieSession.insert(new Transaction(value.toString()));
            kieSession.getAgenda().getAgendaGroup("reducer-rules").setFocus();
            kieSession.fireAllRules();
            kieSession.dispose();
    	}

    	Text outKey = new Text(reducee.getKey());
        Text outValue = new Text(reducee.getValue());
        output.write(outKey , outValue);
    }
}