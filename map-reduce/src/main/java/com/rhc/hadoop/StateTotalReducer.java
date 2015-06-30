package com.rhc.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StateTotalReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    @Override
    public void reduce(Text key, Iterable<DoubleWritable> values, Context output)
            throws IOException, InterruptedException {
        double totalAmount = 0.0;
        for(DoubleWritable amount: values){
            totalAmount += amount.get();
        }
        output.write(key, new DoubleWritable(totalAmount));
    }
}