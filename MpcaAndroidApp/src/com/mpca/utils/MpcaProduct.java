package com.mpca.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MpcaProduct implements Comparable<MpcaProduct>{
	
	private int id;
	private String model;
	private String brand;
	private int ram; // Valores en GBs
	private int hardDisk; // Valores en GBs
	private String recommendation;
	private Integer priority;
	private String imageName;
	
	
	public MpcaProduct(int id, String model, String brand, int ram, int hardDisk,
			String recommendation, int priority, String imageName) {
		this.id = id;
		this.model = model;
		this.brand = brand;
		this.ram = ram;
		this.hardDisk = hardDisk;
		this.recommendation = recommendation;
		this.priority = priority;
		this.imageName = imageName;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getHardDisk() {
		return hardDisk;
	}
	public void setHardDisk(int hardDisk) {
		this.hardDisk = hardDisk;
	}
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public int compareTo(MpcaProduct that) {
		if(this.priority.compareTo(that.priority) == 0) {
			return this.model.compareTo(that.model);
		}
		return this.priority.compareTo(that.priority);
	}

	public Comparable getProperty(String name) {
		
		if(name.equals("Brand")) {
			return this.brand;
		}
		if(name.equals("RAM")) {
			return this.ram;
		}
		if(name.equals("Hard Drive")) {
			return this.hardDisk;
		}
		
		return null;
	}
}
