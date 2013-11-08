package com.mpca.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MpcaProduct {
	
	private int id;
	private String model;
	private String brand;
	private int ram; // Valores en GBs
	private int hardDisk; // Valores en GBs
	private String recommendation;
	private int priority;
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
}
