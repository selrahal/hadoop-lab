package com.rhc.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StateTotalMapper extends Mapper<Object, Text, Text, DoubleWritable>{

    @Override
    public void map(Object key, Text value, Context output) throws IOException, InterruptedException {
    	//Value is a line of transaction data of the format:
    	//credit card, city, state, amount
        String[] field = value.toString().split(",");
        
        //Get the state and amount of this transaction
        String state = field[2];
        double amount = Double.parseDouble(field[3]);
        
        //We are producing the key:value relationship state:amount
        output.write(new Text(state), new DoubleWritable(amount));
    }
}
