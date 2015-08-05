package com.rhc.hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.kie.api.runtime.KieSession;

import com.rhc.hadoop.drools.DroolsHelper;
import com.rhc.hadoop.drools.entry.Entry;
import com.rhc.hadoop.drools.entry.RunningReduce;

public class DroolsReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context output)
            throws IOException, InterruptedException {
    	Iterator<Text> iterator = values.iterator();
    	if (iterator.hasNext()) {
    		//No values!
    		return;
    	}
    	
    	Text initialValue = iterator.next();
    	RunningReduce runningReduce = new RunningReduce(key.toString(), initialValue.toString());
    	
    	while (iterator.hasNext()) {
    		Text nextValue = iterator.next();
    		KieSession kieSession = DroolsHelper.createKieSession();
            kieSession.insert(runningReduce);
            kieSession.insert(new Entry(key.toString(), nextValue.toString()));
            kieSession.getAgenda().getAgendaGroup("reducer-rules").setFocus();
            kieSession.fireAllRules();
            kieSession.dispose();
    	}

    	Text outKey = new Text(runningReduce.toString());
        Text outValue = new Text(runningReduce.toString());
        output.write(outKey , outValue);
    }
}