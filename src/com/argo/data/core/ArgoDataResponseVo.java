package com.argo.data.core;

import java.util.ArrayList;
import java.util.List;

public class ArgoDataResponseVo {
	
	private String platformNumber;

	private List<String> dateList = new ArrayList<>();
	
	private List<String> avgPreesureList = new ArrayList<>();
	
	private List<String> minPreesureList = new ArrayList<>();
	
	private List<String> maxPreesureList = new ArrayList<>();

	
	private List<String> avgTemperatureList = new ArrayList<>();
	
	private List<String> minTemperatureList = new ArrayList<>();
	
	private List<String> maxTemperatureList = new ArrayList<>();
	
	private List<String> avgSalinityList = new ArrayList<>();
	
	private List<String> minSalinityList = new ArrayList<>();
	
	private List<String> maxSalinityList = new ArrayList<>();

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

	public List<String> getAvgPreesureList() {
		return avgPreesureList;
	}

	public void setAvgPreesureList(List<String> avgPreesureList) {
		this.avgPreesureList = avgPreesureList;
	}

	public List<String> getMinPreesureList() {
		return minPreesureList;
	}

	public void setMinPreesureList(List<String> minPreesureList) {
		this.minPreesureList = minPreesureList;
	}

	public List<String> getMaxPreesureList() {
		return maxPreesureList;
	}

	public void setMaxPreesureList(List<String> maxPreesureList) {
		this.maxPreesureList = maxPreesureList;
	}

	public List<String> getAvgTemperatureList() {
		return avgTemperatureList;
	}

	public void setAvgTemperatureList(List<String> avgTemperatureList) {
		this.avgTemperatureList = avgTemperatureList;
	}

	public List<String> getMinTemperatureList() {
		return minTemperatureList;
	}

	public void setMinTemperatureList(List<String> minTemperatureList) {
		this.minTemperatureList = minTemperatureList;
	}

	public List<String> getMaxTemperatureList() {
		return maxTemperatureList;
	}

	public void setMaxTemperatureList(List<String> maxTemperatureList) {
		this.maxTemperatureList = maxTemperatureList;
	}

	public List<String> getAvgSalinityList() {
		return avgSalinityList;
	}

	public void setAvgSalinityList(List<String> avgSalinityList) {
		this.avgSalinityList = avgSalinityList;
	}

	public List<String> getMinSalinityList() {
		return minSalinityList;
	}

	public void setMinSalinityList(List<String> minSalinityList) {
		this.minSalinityList = minSalinityList;
	}

	public List<String> getMaxSalinityList() {
		return maxSalinityList;
	}

	public void setMaxSalinityList(List<String> maxSalinityList) {
		this.maxSalinityList = maxSalinityList;
	}

}
