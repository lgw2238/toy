package com.project.lgw.user.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class AuthVo {
	private String authGroupId;
	private String authGroupNm;
	private String authGroupDesc;
	private String authGroupUseYn;
	private String authGroupUseYnNm;

	private String authTypeSearch;
	private String authTypeControl;

	private String searchAuthGroupUseYn;
	
	private List<Map<String, Object> > mappingDataList = new ArrayList<Map<String,Object>>();
	private List<String> authGroupIdList = new ArrayList<String>();
}
