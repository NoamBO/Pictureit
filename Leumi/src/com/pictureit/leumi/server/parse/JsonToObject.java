package com.pictureit.leumi.server.parse;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pictureit.leumi.server.parse.Service.ContactInfo;
import com.pictureit.leumi.server.parse.Service.LikingData;
import com.pictureit.leumi.server.parse.Service.Register;

public class JsonToObject {
	
	public static final String CONTACT_INFO_CONTACT_VALUE_PHONE = "Phone";
	public static final String CONTACT_INFO_CONTACT_VALUE_E_MAIL = "Mail";
	public static final String CONTACT_INFO_CONTACT_VALUE_FAX = "Fax";

	private final static String INDEXSES = "indexses";

	private static final String SEARCH_STATUS = "SearchStatus";
	
	private static final String SEARCH_STATUS_OK = "Ok";

	private static final String NO_RESULT = "NotResult";
	
	private static final String RESULT = "result";
	
	private static final String BRANCHS = "branchs";

	public static boolean isStatusOk(String json) {
		try {
			JSONObject o = new JSONObject(json);
			String results = o.getString("SearchStatus");
			if (results.equalsIgnoreCase("ok"))
				return true;
		} catch (JSONException e) {
			return false;
		}
		return false;
	}
	
	public static Branch jsonToBranch(String json) {
		Branch b = null;
		JSONObject j = null;
		try {
			j = new JSONObject(json);
			if(!j.getString(SEARCH_STATUS).equals(SEARCH_STATUS_OK))
				return null;
			JSONObject o = j.getJSONArray(BRANCHS).getJSONObject(0);
			
			b = new Branch();
			b.BankName = jsonGetString(o, "BankName");
			b.BankNum = jsonGetString(o, "BankNum");
			b.BranchCity = jsonGetString(o, "BranchCity");
			b.BranchFax = jsonGetString(o, "BranchFax");
			b.BranchName = jsonGetString(o, "BranchName");
			b.BranchNum = jsonGetString(o, "BranchNum");
			b.BranchPhone = jsonGetString(o, "BranchPhone");
			b.BranchStreet = jsonGetString(o, "BranchStreet");
			b.BranchZipCode = jsonGetString(o, "BranchZipCode");
			b.DepartmentCode = jsonGetString(o, "DepartmentCode");
			
			Type typeOfHourOperatation = new TypeToken<ArrayList<HourOperatation>>(){}.getType();
			ArrayList<HourOperatation> BranchHourOperatation = new Gson().fromJson(jsonGetJsonArray(o, "BranchHourOperatation").toString(), typeOfHourOperatation);
			b.BranchHourOperatation = BranchHourOperatation;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}

	public static ArrayList<Emploee> jsonToAutoComplete(String json) {
		ArrayList<Emploee> emploees = new ArrayList<Emploee>();
		try {
			if (!new JSONObject(json).has("results"))
				return new ArrayList<Emploee>();
			JSONObject results = new JSONObject(json).getJSONObject("results");

			if (results.getString(SEARCH_STATUS).equalsIgnoreCase(NO_RESULT))
				return new ArrayList<Emploee>();
			JSONArray indexes = results.getJSONArray(INDEXSES);
			for (int i = 0; i < indexes.length(); i++) {
				emploees.add(new Gson().fromJson(indexes.get(i).toString(),
						Emploee.class));
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return emploees;
	}

	public static ArrayList<LeumiService> jsonToLastServicesList(String json) {
		ArrayList<LeumiService> services = new ArrayList<LeumiService>();
		try {
			JSONArray results = new JSONObject(json)
					.getJSONArray("servicelist");
			for (int i = 0; i < results.length(); i++) {
				services.add(new Gson().fromJson(results.get(i).toString(),
						LeumiService.class));
			}
		} catch (Exception e) {
			return new ArrayList<LeumiService>();
		}
		return services;
	}

	/**
	 * Use to parse json array received from http call
	 * 
	 * 
	 * @param json
	 * @return Profile object
	 */
	public static Profile jsonToUserProfile(String json) {
		Profile p = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			if(!isStatusOk(json))
				return null;
			
			JSONArray jsonArray = jsonObject.getJSONArray("persons");
			if (jsonArray.length() > 0) {
				p = new Gson().fromJson(jsonArray.get(0).toString(),
						Profile.class);
			}
		} catch (JSONException e) {
			p = jsonToSingleEmploeeProfile(json);
			e.printStackTrace();
		}
		return p;
	}
	
	
	/**
	 * Use to parse json object received from within the app
	 * 
	 * @param json
	 * @return Profile object
	 */
	public static Profile jsonToSingleEmploeeProfile(String s) {
		Profile p = null;
		try {
			JSONObject jsonObject = new JSONObject(s);
				p = new Gson().fromJson(jsonObject.toString(),
						Profile.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}

	public static ArrayList<Profile> jsonToSearchResults(String json) {
		ArrayList<Profile> array = new ArrayList<Profile>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("persons");
			for(int i = 0 ; jsonArray.length() > i ; i++ ) {
				Profile p = new Profile(); 
				p = new Gson().fromJson(jsonArray.get(i).toString(),
						Profile.class);
				array.add(p);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return array;
	}

	public static ArrayList<NameValue> parseHeirarchy(String code,
			String headers) {
		ArrayList<NameValue> returnArray = new ArrayList<NameValue>();
		String[] names = headers.split("/");
		String[] codes = code.split("/");
		for (int i = 1; i < codes.length || i < names.length; i++) {
			returnArray.add(new NameValue(names[i], codes[i]));
		}
		return returnArray;
	}
	
	@SuppressWarnings("unchecked")
	public static Service jsonGetService(String json) {
		//   jsonGetService   Values
		final String ApealerDetails = "ApealerDetails";
		final String BllBusinessService = "BllBusinessService";
		final String ContactInfo = "ContactInfo";
		final String LikingData = "LikingData";
		final String Register = "Register";
		final String ServiceHourOperatation = "ServiceHourOperatation";
		final String ServiceOwner = "ServiceOwner";
		final String ServiceUnitCode = "ServiceUnitCode";
		final String ServiceUrl = "ServiceUrl";
		
		Service s = new Service();
		try {
			JSONObject o = new JSONObject(json).getJSONObject(RESULT);
			if(!o.getString(SEARCH_STATUS).equals(SEARCH_STATUS_OK))
				return null;
			o = o.getJSONObject("servicesystem");
			
			s.ApealerDetails = jsonGetString(o, ApealerDetails);
			s.BllBusinessService = jsonGetString(o, BllBusinessService);
			s.ServiceOwner = jsonGetString(o, ServiceOwner);
			s.ServiceUnitCode = jsonGetString(o, ServiceUnitCode);
			s.ServiceUrl = jsonGetString(o, ServiceUrl);


			Type typeOfServiceHourOperatation = new TypeToken<ArrayList<HourOperatation>>(){}.getType();
			ArrayList<HourOperatation> ServiceHourOperatationList = new Gson().fromJson(jsonGetJsonArray(o, ServiceHourOperatation).toString(), typeOfServiceHourOperatation);
			
			Type typeOfContactInfo = new TypeToken<ArrayList<ContactInfo>>(){}.getType();
			ArrayList<ContactInfo> ContactInfoList = new Gson().fromJson(jsonGetJsonArray(o, ContactInfo).toString(), typeOfContactInfo);


			LikingData LikingDataList = jsonGetFromGson(jsonGetJsonArray(o, ContactInfo).toString(), LikingData.class);			
			Register RegisterList = jsonGetFromGson(jsonGetJsonArray(o, ContactInfo).toString(), Register.class);
			
			s.ContactInfo = ContactInfoList;
			s.LikingData = LikingDataList;
			s.Register = RegisterList;
			s.ServiceHourOperatation = ServiceHourOperatationList;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return s;
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	

	private static <T extends Object> T jsonGetFromGson(String json ,Class<T> classType) {
		try {
			return (T) new Gson().fromJson(json,  classType);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return (T) null;
	}
	
	private static JSONArray jsonGetJsonArray(JSONObject jsonObject, String key) {
		JSONArray value = null;
		try {
			value = jsonObject.getJSONArray(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	private static String jsonGetString(JSONObject jsonObject, String key) {
		String value = null;
		try {
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	private static int jsonGetInteger(JSONObject jsonObject, String key) {
		int value = -1;
		try {
			value = jsonObject.getInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
