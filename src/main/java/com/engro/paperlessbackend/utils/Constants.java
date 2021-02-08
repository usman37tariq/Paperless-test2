package com.engro.paperlessbackend.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Constants {

	public static final String POSTGRES_TIMESTAMP_WITHOUT_TIMEZONE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String PATTERN_DATE_ONLY = "yyyy-MM-dd";
	public static final String PATTERN_DATE_UPTO_MINUTES = "yyyy-MM-dd HH:mm";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			POSTGRES_TIMESTAMP_WITHOUT_TIMEZONE_DATE_FORMAT);

	public static final String TAG_TYPE_NUMBER = "number";
	public static final String TAG_TYPE_TEXT = "text";
	public static final String TAG_TYPE_OK_NOT_OK = "oknotok";
	public static final String TAG_TYPE_YES_NO = "yesno";

	public static final String[] NUMBER_TYPES = { TAG_TYPE_NUMBER, TAG_TYPE_OK_NOT_OK, TAG_TYPE_YES_NO };

	public static final String USER_ID = "epcl_user";

	public static final String LOCAL_UI_PORT = "5005";

	public static final String BASE_URL_LOCAL = "http://localhost:" + LOCAL_UI_PORT + "/";
	public static final String SET_NEW_PASSWORD_URL_LOCAL = BASE_URL_LOCAL + "reset-password?hash=%s";

	public static final String BASE_URL_PRE_PROD = "https://epcl-sb.empiric.ai/";
	public static final String BASE_URL_PROD = "https://epcl.empiric.ai/";

	public static final String SET_NEW_PASSWORD_URL_PRE_PROD = BASE_URL_PRE_PROD + "reset-password?hash=%s";
	public static final String SET_NEW_PASSWORD_URL_PROD = BASE_URL_PROD + "reset-password?hash=%s";

	public static final String SENDER_EMAIL_ADDRESS = "no-reply@empiric.ai";
	public static final String EMAIL_SUBJECT_NEW_USER = "Welcome to Industrial Analytics";
	public static final String EMAIL_SUBJECT_PASSWORD_CHANGED = "Your Industrial Analytics Password has been Reset";

	public static final String DUMMY_RECEIVER_EMAIL_ADDRESS = "arslansaddique4137@gmail.com";

	public static final int ORG_ID = 1;
	public static final String ORGANIZATION_ID = "epcl";
	public static final String ORG_NAME = "epcl";

	public static final String TAG_DATA_SOURCE = "checklist";

	public static final String TAG_DATA_TYPE_NUMERIC = "NUMERIC";

	public static final String HIERARCHY_NODE_TYPE_LOCATION = "0";
	public static final String HIERARCHY_NODE_TYPE_ASSET = "1";
	public static final String HIERARCHY_NODE_TYPE_TAG = "2";

	public static final String HIERARCHY_NODE_PARENT_ID_FOR_UI = "#";
	public static final int HIERARCHY_NODE_PARENT_ID_IN_DB = -1;

	public static final String USER_STATUS_ACTIVE = "1";
	public static final String USER_STATUS_INACTIVE = "0";

	public static final int CHECKLIST_IS_NOT_DELETED = 0;
	public static final int CHECKLIST_IS_DELETED = 1;

	public static final int DEPARTMENT_IS_NOT_DELETED = 0;
	public static final int DEPARTMENT_IS_DELETED = 1;

	public static final int SECTION_IS_NOT_DELETED = 0;
	public static final int SECTION_IS_DELETED = 1;

	public static final String CHECKLIST_STATUS_PENDING = "PENDING";
	public static final String CHECKLIST_STATUS_ACTIVE = "ACTIVE";

	public static final int CHECKLIST_IS_REJECTED = 1;
	public static final int CHECKLIST_IS_APPROVED = 1;
	public static final int CHECKLIST_IS_SKIPPED = 1;

	public static final String CUSTOM_TAG_TABLE_PREFIX = "tbl_tag";

	public static final int TAG_QUALITY_BAD = 0;
	public static final int TAG_QUALITY_UNCERTAIN = 1;
	public static final int TAG_QUALITY_NOT_APPLICABLE = 2;
	public static final int TAG_QUALITY_GOOD = 3;

	public static final int WORKFLOW_LEVEL_ZERO_ID = 0;
	public static final String WORKFLOW_LEVEL_ZERO_NAME = "APPROVED";

	public static final int WORKFLOW_LEVEL_ONE_ID = 1;
	public static final String WORKFLOW_LEVEL_ONE_NAME = "DATA ENTRY PENDING";

	public static final int WORKFLOW_LEVEL_TWO_ID = 2;
	public static final String WORKFLOW_LEVEL_TWO_NAME = "FIRST APPROVAL PENDING";

	public static final int WORKFLOW_LEVEL_THREE_ID = 3;
	public static final String WORKFLOW_LEVEL_THREE_NAME = "SECOND APPROVAL PENDING";

	static {
		HashMap<Integer, String> WORKFLOW_LEVEL_MAP = new HashMap<>();
		WORKFLOW_LEVEL_MAP.put(WORKFLOW_LEVEL_ZERO_ID, WORKFLOW_LEVEL_ZERO_NAME);
		WORKFLOW_LEVEL_MAP.put(WORKFLOW_LEVEL_ONE_ID, WORKFLOW_LEVEL_ONE_NAME);
		WORKFLOW_LEVEL_MAP.put(WORKFLOW_LEVEL_TWO_ID, WORKFLOW_LEVEL_TWO_NAME);
		WORKFLOW_LEVEL_MAP.put(WORKFLOW_LEVEL_THREE_ID, WORKFLOW_LEVEL_THREE_NAME);
	}

	public static final String CLAIMED_STATUS_DUE = "due";
	public static final String CLAIMED_STATUS_OVER_DUE = "overdue";

	public static final Integer CHECKLIST_SEARCH_DIRECTION_FORWARD = 1;
	public static final Integer CHECKLIST_SEARCH_DIRECTION_BACKWARD = -1;

	public static final String RESOURCE_HIERARCHY_BUILDER = "Hierarchy Builder";
	public static final String RESOURCE_ASSET_BUILDER = "Asset Builder";
	public static final String RESOURCE_CHECKLIST_BUILDER = "Checklist Builder";
	public static final String RESOURCE_DATA_VISUALIZATION = "Data Visualization";
	public static final String RESOURCE_DATA_COLLECTOR = "Data Collector";
	public static final String RESOURCE_USER_MANAGEMENT = "User Management";

	public static final Long MANUAL_CHECKLIST_END_DATE_IN_MILLIS = 10413774000000L;
	public static final Long MANUAL_CHECKLIST_SCHEDULE_END_DATE_ZERO_IN_MILLIS = 0L;
	public static final Long MANUAL_CHECKLIST_SCHEDULE_END_DATE_NULL = null;
	public static final String MANUAL_CHECKLIST_UNIT = "MANUAL";
	public static final int CHECKLIST_IS_MANUAL = 1;

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static final String EMAIL_HTML_BODY_WITHOUT_PASSWORD = ""
			+"<p>Hi, %s</p>"
			+"<p>Welcome. You have just been added to Industrial Analytics.</p>"
			+"<p>Following are your login credentials, as set by your admin. Please log in using the link and your credentials.</p></br>"
			+"<table border=1 style='border-collapse: collapse'>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Log in Link</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Email</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"</table>";
			
			
//	public static final String EMAIL_HTML_BODY_WITHOUT_PASSWORD = ""
//			+ "Hi %s,<br/>Welcome. You have just been added to SSA.<br/>"
//			+ "Following are your login credentials, as set by your admin. "
//			+ "Please log in using the link and your credentials.<br/>" + "<b>Log in Link:</b> %s<br/>"
//			+ "<b>Email:</b> %s<br/>";

	public static final String EMAIL_HTML_BODY_WITH_PASSWORD = ""
			+"<p>Hi, %s</p>"
			+"<p>Welcome. You have just been added to Industrial Analytics.</p>"
			+"<p>Following are your login credentials, as set by your admin. Please log in using the link and your credentials.</p></br>"
			+"<table border=1 style='border-collapse: collapse'>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Log in Link</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Email</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Password</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"</table>";
	
//	public static final String EMAIL_HTML_BODY_WITH_PASSWORD = "Hi %s,<br/>Welcome. You have just been added to SSA.<br/>"
//			+ "Following are your login credentials, as set by your admin. "
//			+ "Please log in using the link and your credentials.<br/>" + "<b>Log in Link:</b> %s<br/>"
//			+ "<b>Email:</b> %s<br/>" + "<b>Password:</b> %s";
	
	public static final String EMAIL_HTML_BODY_PASSWORD_RESET = ""
			+"<p>Hi, %s</p>"
			+"<p>%s has just reset your password.</p></br>"
			+"<p>Following are your login credentials, as set by your admin. Please log in using the link and your credentials.</p></br>"
			+"<table border=1 style='border-collapse: collapse'>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Log in Link</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Email</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"<tr>"
			+"<th style='text-align:left; padding:5px'>Password</th>"
			+"<td style='padding:5px'>%s</td>"
			+"</tr>"
			+"</table>";
	
//	public static final String EMAIL_HTML_BODY_PASSWORD_RESET = "Hi %s,<br/>Admin has just reset your password.<br/>"
//			+ "Following are your login credentials, as set by your admin. "
//			+ "Please log in using the link and your credentials.<br/>" + "<b>Log in Link:</b> %s<br/>"
//			+ "<b>Email:</b> %s<br/>" + "<b>Password:</b> %s";

	

}
