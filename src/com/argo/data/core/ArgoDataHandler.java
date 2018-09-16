package com.argo.data.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

public class ArgoDataHandler {

	private static final String ARGO_COMPRESS_PATH = "C:/argo/file/temp";

	public boolean doUpload(MultipartFile file) throws IOException, ClassNotFoundException, SQLException {

		boolean depressSuccess = false;
		File directory = new File(ARGO_COMPRESS_PATH);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File destFile = new File(ARGO_COMPRESS_PATH + "/tempfile");
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		try {
			// 解压文件到临时目录
			file.transferTo(destFile);
			depressSuccess = ZipUtils.decompressZip(destFile, ARGO_COMPRESS_PATH);
		}  finally {
			if (destFile.exists()) destFile.delete();
		}

		if (!depressSuccess){
			return false;
		}

		File f = new File(ARGO_COMPRESS_PATH);
		if (f.exists() && f.isDirectory()) {
			try {
				// 提取临时目录下的文件进行处理
				File[] files = f.listFiles()[0].listFiles();
				if (files != null && files.length > 0) {
					deleteAll();
					for (File dataFile : files) {
						// 将文件提取成对象列表
						List<ArgoData> argoList = convertArgoData(dataFile);

						if (argoList != null && argoList.size() > 0) {
							// 插入到数据库
							doInsertArgoList(argoList);
						}
					}
				}
			} finally {
				f.delete();
			}

		}
		return true;
	}

	private List<ArgoData> convertArgoData(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;

		List<ArgoData> resultList = new ArrayList<>();		
		boolean readRealData = false;

		String platFormNumber = null;
		String longitude = null;
		String latitude = null;
		String date = null;

		try {
			while ((str = br.readLine()) != null && str != "") {

				if (!readRealData) {
					// 去掉所有空格
					str = str.replace(" ", "");
					if (str.contains("PLATFORMNUMBER")
							&& str.split(":")[0].equals("PLATFORMNUMBER")) {
						platFormNumber = str.split(":")[1];
					}

					if (str.contains("LATITUDE")
							&& str.split(":")[0].equals("LATITUDE")) {
						latitude = str.split(":")[1];
					}

					if (str.contains("LONGITUDE") 
							&& str.split(":")[0].equals("LONGITUDE")) {
						longitude = str.split(":")[1];
					}

					if (str.contains("DATE")
							&& str.split(":")[0].equals("DATE")) {
						date = str.split(":")[1];
					}
				}else {
					ArgoData argoData = new ArgoData();

					str = str.replaceAll("  ", " ");
					String[] datas = str.split(" ");
					// 去除空字符串
					List<String> dataList = new ArrayList<>();
					for (String s : datas) {
						if (!s.trim().equals("")) {
							dataList.add(s);
						}
					}


					argoData.setDate(date);
					argoData.setLatitude(latitude);
					argoData.setPlatformNumber(platFormNumber);
					argoData.setLongitude(longitude);
					argoData.setPreesure(dataList.get(1));
					argoData.setTemperature(dataList.get(4));
					argoData.setSalinity(dataList.get(7));

					resultList.add(argoData);
				}

				if (str.equals("==========================================================================")) {
					readRealData = true;
				}
			}
		} finally {
			br.close();
		}

		return resultList;
	}

	private void deleteAll() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC.DRIVERNAME);
		Connection conn = null;
		String sql = "delete from argo_data ";

		PreparedStatement ps = null;

		try {

			conn = DriverManager.getConnection(
					JDBC.URL,
					JDBC.USERNAME,
					JDBC.PASSWORD);

			ps = conn.prepareStatement(sql);
			ps.execute();
		} finally {
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}
	}

	private void doInsertArgoList(List<ArgoData> argoList) throws SQLException, ClassNotFoundException {

		if (argoList == null || argoList.size() == 0) {
			return;
		}

		Class.forName(JDBC.DRIVERNAME);
		Connection conn = null;
		String sql = "insert into argo_data (platformNumber, date, latitude, longitude, preesure, temperature, salinity) values(?,?,?,?,?,?,?)";

		PreparedStatement ps = null;

		try {

			conn = DriverManager.getConnection(
					JDBC.URL,
					JDBC.USERNAME,
					JDBC.PASSWORD);

			ps = conn.prepareStatement(sql);

			for (ArgoData argo : argoList) {
				ps.setString(1, argo.getPlatformNumber());
				ps.setString(2, argo.getDate());
				ps.setString(3, argo.getLatitude());
				ps.setString(4, argo.getLongitude());
				ps.setString(5, argo.getPreesure());
				ps.setString(6, argo.getTemperature());
				ps.setString(7, argo.getSalinity());
				ps.addBatch();
			}

			ps.executeBatch();
		} finally {
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}

	}


	public List<ArgoDataResponseVo> getData(ArgoDataRequestVo request) throws SQLException, ClassNotFoundException {
		StringBuffer sb = new StringBuffer("select * from argo_data where 1=1 ");
		if (request.getPlatformNumber() != null && request.getPlatformNumber() != "") {
			sb.append(" and platformNumber='" + request.getPlatformNumber() + "'");
		}
		if (request.getDateFrom() != null && request.getDateFrom() != "") {
			sb.append(" and date >= '" + request.getDateFrom() + "'");
		}
		if (request.getDateTo() != null && request.getDateTo() != "") {
			sb.append(" and date <= '" + request.getDateTo() + "'");
		}
		if (request.getMinLat() != null && request.getMinLat() != "") {
			sb.append(" and latitude >= '" + request.getMinLat() + "'");
		}
		if (request.getMaxLat() != null && request.getMaxLat() != "") {
			sb.append(" and latitude <= '" + request.getMaxLat() + "'");
		}
		if (request.getMinLong() != null && request.getMinLong() != "") {
			sb.append(" and longitude >= '" + request.getMinLong() + "'");
		}
		if (request.getMaxLong() != null && request.getMaxLong() != "") {
			sb.append(" and longitude <= '" + request.getMaxLong() + "'");
		}
		sb.append(" order by date");

		TreeMap<String, List<ArgoData>> treeMap = new TreeMap<>();

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Class.forName(JDBC.DRIVERNAME);
		try {
			conn = DriverManager.getConnection(
					JDBC.URL,
					JDBC.USERNAME,
					JDBC.PASSWORD);

			ps = conn.prepareStatement(sb.toString());

			rs = ps.executeQuery();
			while (rs.next()) {
				String platformNumber = rs.getString("platformNumber");
				String date = rs.getString("date");
				String preesure = rs.getString("preesure");
				String temperature = rs.getString("temperature");
				String salinity = rs.getString("salinity");

				ArgoData argoData = new ArgoData();
				argoData.setDate(date);
				argoData.setPreesure(preesure);
				argoData.setTemperature(temperature);
				argoData.setSalinity(salinity);
				argoData.setPlatformNumber(platformNumber);

				if (treeMap.get(platformNumber) == null) {
					treeMap.put(platformNumber, new ArrayList<ArgoData>(){{add(argoData);}});
				}else {
					treeMap.get(platformNumber).add(argoData);
				}
			}

		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}

		return reduceMap(treeMap);
	}

	private List<ArgoDataResponseVo> reduceMap(TreeMap<String, List<ArgoData>> treeMap) {

		if (treeMap == null || treeMap.size() == 0) return new ArrayList<ArgoDataResponseVo>(){{add(new ArgoDataResponseVo());}};

		List<ArgoDataResponseVo> resultList = new ArrayList<>();

		for (Entry<String, List<ArgoData>> entry : treeMap.entrySet()) {

			List<ArgoData> list = entry.getValue();

			if (list == null || list.size() == 0) {
				continue;
			}

			ArgoDataResponseVo vo = new ArgoDataResponseVo();

			vo.setPlatformNumber(entry.getKey());

			List<String> dateList = new ArrayList<>();

			Map<String, List<ArgoData>> groupMap = list.stream().collect(Collectors.groupingBy(ArgoData::getDate, TreeMap::new, Collectors.toList()));
			DecimalFormat df = new DecimalFormat("#.000");
			try {
				groupMap.forEach((date, argoList)->{
					dateList.add(date);
				
					
					vo.getAvgPreesureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getPreesure())).average().getAsDouble()));
					vo.getMinPreesureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getPreesure())).min().getAsDouble()));
					vo.getMaxPreesureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getPreesure())).max().getAsDouble()));

					vo.getAvgTemperatureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getTemperature())).average().getAsDouble()));
					vo.getMinTemperatureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getTemperature())).min().getAsDouble()));
					vo.getMaxTemperatureList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getTemperature())).max().getAsDouble()));

					vo.getAvgSalinityList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getSalinity())).average().getAsDouble()));
					vo.getMinSalinityList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getSalinity())).min().getAsDouble()));
					vo.getMaxSalinityList().add(df.format(argoList.stream().mapToDouble(item->Double.valueOf(item.getSalinity())).max().getAsDouble()));
				});
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			vo.setDateList(dateList);
			resultList.add(vo);
		}
		return resultList;
	}

	public List<String> getAllPlatformNumbers() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Class.forName(JDBC.DRIVERNAME);
		List<String> resultList = new ArrayList<>();

		try {
			conn = DriverManager.getConnection(
					JDBC.URL,
					JDBC.USERNAME,
					JDBC.PASSWORD);

			ps = conn.prepareStatement("select distinct platformNumber as platformNumber from argo_data");

			rs = ps.executeQuery();
			while (rs.next()) {
				resultList.add(rs.getString("platformNumber"));
			}
			
		} finally {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		}
		return resultList;
	}
	
}