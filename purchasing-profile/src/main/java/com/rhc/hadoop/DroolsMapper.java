package com.rhc.hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.entry.Collector;
import com.rhc.hadoop.drools.entry.Entry;

public class DroolsMapper extends Mapper<Text, Text, Text, Text>{

    @Override
    public void map(Text key, Text value, Context output) throws IOException, InterruptedException {
        Collector collector = new Collector();
        KieSession kieSession = DroolsHelper.createKieSession();
        kieSession.setGlobal("collector", collector);
        kieSession.insert(new Entry(key.toString(), value.toString()));
        kieSession.getAgenda().getAgendaGroup("mapper-rules").setFocus();
        kieSession.fireAllRules();
        kieSession.dispose();
        
        Iterator<Entry> iterator = collector.iterator();
        while (iterator.hasNext()) {
        	Entry next = iterator.next();
        	output.write(new Text(next.key), new Text(next.value));
        }
    }
}
