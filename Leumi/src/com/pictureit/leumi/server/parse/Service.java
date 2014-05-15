package com.pictureit.leumi.server.parse;

import java.util.ArrayList;

public class Service {
	
	public static final String COMMUNICATION_TYPE_PHONE = "Phone";
	public static final String COMMUNICATION_TYPE_FAX = "Fax";
	public static final String COMMUNICATION_TYPE_MAIL = "Mail";
	public static final String REGISTRATION_STATUS_ALLREADY_REGISTER = "1";
	public static final String USER_NOT_LIKE_YET = "0";

	public String ApealerDetails;
	public String BllBusinessService;
	public ArrayList<ContactInfo> ContactInfo;
	public LikingData LikingData;
	public Register Register;
	public ArrayList<HourOperatation> ServiceHourOperatation;
	public String ServiceOwner;
	public String ServiceUnitCode;
	public String ServiceUrl;
	public String ServiceID;

	public static class ContactInfo {
		
		public ContactInfo(String value, String contact) {
			this.contact = contact;
			this.Value = value;
		}
		
		public String Value;
		public String contact;
	}

	public static class LikingData {
		public String Liking;
		public String LikingCount;
	}

	public class Register {
		public String Status;
	}

}
