package com.rhc.hadoop.drools.entry;

public class RunningReduce {
	public String key;
	public String value;
	
	public RunningReduce(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
