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
	public ArrayList<ServiceHourOperatation> ServiceHourOperatation;
	public String ServiceOwner;
	public String ServiceUnitCode;
	public String ServiceUrl;

	public class ContactInfo {
		public String Value;
		public String contact;
	}

	public class LikingData {
		public String Liking;
		public String LikingCount;
	}

	public class Register {
		public String Status;
	}

	public class ServiceHourOperatation {
		public String CloseOpen;
		public String DayInWeek;
		public String EFrom;
		public String ETo;
		public String HourBeforeShabat;
		public String MFrom;
		public String MTo;
	}
}
