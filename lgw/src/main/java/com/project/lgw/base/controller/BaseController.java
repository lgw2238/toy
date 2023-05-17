package com.project.lgw.base.controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseController {
	
	public String getResultModel(String code, String msg, String paginationTag, Object data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<String, Object>();
				
    	modelMap.put("resultCode", code);
    	modelMap.put("resultMsg", msg);
    	modelMap.put("paginationTag", paginationTag);
    	modelMap.put("resultData", data);
    	
    	return mapper.writeValueAsString(modelMap);
	}

	public String getResultModel(String code, String msg, String paginationTag, Object data, int totalCount) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
    	modelMap.put("resultCode", code);
    	modelMap.put("resultMsg", msg);
    	modelMap.put("paginationTag", paginationTag);
    	modelMap.put("resultData", data);
    	modelMap.put("totalCount", totalCount);
    	
    	return mapper.writeValueAsString(modelMap);
	}
	
}