package com.argo.data.core;

import java.util.ArrayList;
import java.util.List;

public class ArgoDataResponseVo {
	
	private String platformNumber;

	private List<String> dateList = new ArrayList<>();
	
	private List<Double> avgPreesureList = new ArrayList<>();
	
	private List<Double> minPreesureList = new ArrayList<>();
	
	private List<Double> maxPreesureList = new ArrayList<>();

	
	private List<Double> avgTemperatureList = new ArrayList<>();
	
	private List<Double> minTemperatureList = new ArrayList<>();
	
	private List<Double> maxTemperatureList = new ArrayList<>();
	
	private List<Double> avgSalinityList = new ArrayList<>();
	
	private List<Double> minSalinityList = new ArrayList<>();
	
	private List<Double> maxSalinityList = new ArrayList<>();

	public String getPlatformNumber() {
		return platformNumber;
	}

	public void setPlatformNumber(String platformNumber) {
		this.platformNumber = platformNumber;
	}

	public List<String> getDateList() {
		return dateList;
	}

	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}

	public List<Double> getAvgPreesureList() {
		return avgPreesureList;
	}

	public void setAvgPreesureList(List<Double> avgPreesureList) {
		this.avgPreesureList = avgPreesureList;
	}

	public List<Double> getMinPreesureList() {
		return minPreesureList;
	}

	public void setMinPreesureList(List<Double> minPreesureList) {
		this.minPreesureList = minPreesureList;
	}

	public List<Double> getMaxPreesureList() {
		return maxPreesureList;
	}

	public void setMaxPreesureList(List<Double> maxPreesureList) {
		this.maxPreesureList = maxPreesureList;
	}

	public List<Double> getAvgTemperatureList() {
		return avgTemperatureList;
	}

	public void setAvgTemperatureList(List<Double> avgTemperatureList) {
		this.avgTemperatureList = avgTemperatureList;
	}

	public List<Double> getMinTemperatureList() {
		return minTemperatureList;
	}

	public void setMinTemperatureList(List<Double> minTemperatureList) {
		this.minTemperatureList = minTemperatureList;
	}

	public List<Double> getMaxTemperatureList() {
		return maxTemperatureList;
	}

	public void setMaxTemperatureList(List<Double> maxTemperatureList) {
		this.maxTemperatureList = maxTemperatureList;
	}

	public List<Double> getAvgSalinityList() {
		return avgSalinityList;
	}

	public void setAvgSalinityList(List<Double> avgSalinityList) {
		this.avgSalinityList = avgSalinityList;
	}

	public List<Double> getMinSalinityList() {
		return minSalinityList;
	}

	public void setMinSalinityList(List<Double> minSalinityList) {
		this.minSalinityList = minSalinityList;
	}

	public List<Double> getMaxSalinityList() {
		return maxSalinityList;
	}

	public void setMaxSalinityList(List<Double> maxSalinityList) {
		this.maxSalinityList = maxSalinityList;
	}

	
	
}
