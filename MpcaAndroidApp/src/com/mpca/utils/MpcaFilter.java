package com.mpca.utils;

import java.util.SortedMap;
import java.util.TreeMap;

public class MpcaFilter<T extends Comparable> {
	private String name;
	private SortedMap<T,Boolean> values;
	
	public MpcaFilter(String name) {
		super();
		this.name = name;
		values = new TreeMap<T,Boolean>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SortedMap<T,Boolean> getValues() {
		return values;
	}
	public void addValue(T value) {
		this.values.put(value,true);
	}
}
