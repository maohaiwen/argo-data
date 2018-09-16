package com.argo.data.core;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/argo/data")
public class ArgoDataController {
	
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
    public String home(@RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
		
		boolean success = false;
        try {
        	success = new ArgoDataHandler().doUpload(file);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} 
        
        return success ? "上传成功" : "上传失败";
    }
	
	@RequestMapping(value="/getData", method = RequestMethod.GET)
	@ResponseBody
	public List<ArgoDataResponseVo> getData(ArgoDataRequestVo request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		try {
			return new ArgoDataHandler().getData(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	@RequestMapping(value="/getAllPlatformNumbers", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getAllPlatformNumbers(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*"); 
		try {
			return new ArgoDataHandler().getAllPlatformNumbers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
}
