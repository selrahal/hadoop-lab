package com.rhc.hadoop.drools.entry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Collector {
	private List<Entry> entries = new ArrayList<Entry>();
	
	public void collect(Entry entry) {
		entries.add(entry);
	}
	
	public void collect(String key, String value) {
		this.collect(new Entry(key, value));
	}
	
	public Iterator<Entry> iterator() {
		return entries.iterator();
	}
}
