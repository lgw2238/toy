package com.project.lgw.comm;

public class CommonConstant {
public static final String CLASSPATH_RESOURCE_LOCATIONS = "classpath:/static/";
	
	public static final String LOGIN_FAIL_MESSAGE = "로그인에 실패 하였습니다.";
	public static final String DB_CONNECTION_FAIL = "DB 연결실패";
	
	public static final String PASSWORD_CHANGE_FAIL_MESSAGE = "비밀번호가 일치하지 않습니다.";
	public static final String PASSWORD_CHANGE_INFO_FAIL_MESSAGE = "입력하신 정보를 다시 확인해주세요.";
	public static final String BAD_CREDENTIALS_EXCEPTION_MESSAGE_PRFIX = "계정 명(ID) 또는 비밀번호(PW)를\\n";
	public static final String BAD_CREDENTIALS_EXCEPTION_MESSAGE_SUFFIX = "회 잘못 입력하셨습니다.\\n";
	public static final String CHECK_INFO_MESSAGE = "입력하신 정보를 확인하십시오.";
	
	public static final String INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION = "아이디가 없습니다.";	
	public static final String LOGIN_ACCESSDENY_EXCEPTION = "접속 권한이 없습니다.";
	public static final String ANONYMOUS_USER = "유효하지 않는 사용자 입니다.";	
	public static final String LOGIN_TRY_COUNT_EXCEEDED = "로그인을 5회 이상 실패하여\\n더 이상 로그인할 수 없습니다.\\n관리자에게 문의 하십시오.";
	public static final String DUPLICATIONEXCEPTION_MESSAGE = "해당 계정은 현재\\n다른 단말기에서 접속 중입니다.\\n기존 연결을 해지하고\\n새로 접속하시겠습니까?";
	public static final String LOGIN_EXPIRE_DATE_MESSAGE = "해당 계정은 비밀번호가 만료되어 로그인 할 수 없습니다.\\n 관리자에게 문의 하십시오.";
	
	public static final String ADMIN_LOGIN_DORMANCY_MESSAGE_PREFIX = "해당 계정은 30일 이상\\n접속을 하지 않아 현재 휴면상태로\\n로그인 할 수 없습니다."; 
	public static final String CONTACT_SYSTEM_ADMIN = "관리자에게 문의하십시오";
	
	public static final String ADMIN_NOT_LOGIN_STATUS_PREFIX = "해당 계정은 현재 ";
	public static final String ADMIN_NOT_LOGIN_STATUS_SUFFIX = " 상태 입니다.\\n";
	
	public static final String RESERVATION_MMS_PATH = "/api/mms/insertReservationMms";
	public static final String ERROR_PATH = "/error";
	public static final String DUPLICATION_LOGIN_ERROR_PATH = "/duplicationLogin";
	public static final String SESSION_EXPIRED = "/sessionExpired";
	
	public static final String KEY_SESSIONUSER = "adminInfo"; 	
	
	public static final String KEY_USERAGENT = "userAgent";
		
	/* 게시글 타입(BBS_TYPE) */
	// 일반게시물
	public static final String BBS_TYPE_NORMAL = "B";
	// FAQ	
	public static final String BBS_TYPE_FAQ = "F";
	// 질의
	public static final String BBS_TYPE_CONTACT_US = "Q";
	// 응답
	public static final String BBS_TYPE_ANSWER = "A";
	// 공지사항
	public static final String BBS_TYPE_NOTICE = "N";
	// EVENT
	public static final String BBS_TYPE_EVENT = "E";
	
	// 게시글 부모 ID
	public static final String UPPER_BBS_ID_PARENT = "-";
	
	// ckeditor upload Path
	public static final String CKEDITOR_UPLOAD_PATH = "D:/study/fileupload";

	
	// Excel extension
	public static final String XLS = "xls";	
	public static final String XLSX = "xlsx";
}
