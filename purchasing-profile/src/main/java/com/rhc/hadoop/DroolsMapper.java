package com.rhc.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.map.MapperFact;
import com.rhc.hadoop.model.Transaction;
import com.rhc.hadoop.model.TransactionMapper;

public class DroolsMapper extends Mapper<Object, Text, Text, Text>{

    @Override
    public void map(Object key, Text value, Context output) throws IOException, InterruptedException {
        MapperFact<Transaction> entry = new TransactionMapper(new Transaction(value.toString()));
        
        KieSession kieSession = DroolsHelper.createKieSession();
        kieSession.insert(entry);
        kieSession.getAgenda().getAgendaGroup("mapper-rules").setFocus();
        kieSession.fireAllRules();
        kieSession.dispose();
        
        
        Text outKey = new Text(entry.getKey());
        Text outValue = new Text(entry.getValue());
        output.write(outKey, outValue);
    }
}
