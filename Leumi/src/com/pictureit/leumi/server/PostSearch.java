package com.pictureit.leumi.server;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utilities.server.BaseHttpPost;
import android.content.Context;

import com.pictureit.leumi.main.Const;

public class PostSearch extends BaseHttpPost {



	public PostSearch(Context ctx, HttpCallback callback) {
		super(ctx);
		this.callback = callback;
		mMainJson = new JSONObject();
		prepare(null);
	}

	@Override
	protected String doInBackground(String... params) {
		if (mMainJson == null)
			return null;
		return super.doInBackground(params);
	}


	@Override
	protected void prepare(String request) {
		StringBuilder sb = new StringBuilder();
		sb.append(ServerAddresses.HOST).append(ServerAddresses.BASE_PATH)
				.append(ServerAddresses.SEARCH_PERSON_METHOD_PATH);

		setUrl(sb.toString());
	}

	private void addObjectToMainJsonArray(JSONArray jsonArray)
			throws JSONException {
			mMainJson.put(Const.SEARCH_DATA_LIST, jsonArray);
		
	}

	// Brings Employees when tapped on name from AutoComplete
	public void getEmploeeForSearchID(String searchId) {
		JSONArray jsonArray = new JSONArray();
		JSONObject innerJson = new JSONObject();
		try {
			innerJson.put(Const.SEARCH_KEY, Const.FIRST_LAST_NAME);
			innerJson.put(Const.SEARCH_VALUE, searchId);
			addObjectToMainJsonArray(jsonArray.put(innerJson));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}

	// Brings array of Employees when search butten tapped in main screen
	public void getEmployeesForFreeText(String freeText) {
		JSONArray jsonArray = new JSONArray();
		JSONObject innerJson = new JSONObject();
		try {
			innerJson.put(Const.SEARCH_VALUE, freeText);
			innerJson.put(Const.SEARCH_KEY, Const.FREE_TEXT);
			addObjectToMainJsonArray(jsonArray.put(innerJson));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}

	// brings array of Employees from specific branch
	public void getBranchEmployeesForDepartmentCode(String departmantCode) {
		JSONArray jsonArray = new JSONArray();
		JSONObject innerJson = new JSONObject();
		try {
			innerJson.put(Const.SEARCH_KEY, Const.DEPARTMENT_CODE);
			innerJson.put(Const.SEARCH_VALUE, departmantCode);
			addObjectToMainJsonArray(jsonArray.put(innerJson));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
	
	public void getEmployeeForDepartment(String departmant) {
		JSONArray jsonArray = new JSONArray();
		JSONObject innerJson = new JSONObject();
		try {
			innerJson.put(Const.SEARCH_KEY, Const.DEPARTMENT);
			innerJson.put(Const.SEARCH_VALUE, departmant);
			addObjectToMainJsonArray(jsonArray.put(innerJson));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
	
	//Brings Employee for job from autocompletion click in home fragment
	public void getEmployeesForJob(String job) {
		JSONArray jsonArray = new JSONArray();
		try {

			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.JOB)
					.put(Const.SEARCH_VALUE, job)
					);
			
			addObjectToMainJsonArray(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
	
	//Brings Employee for advance searching
	public void getEmployeesForAdvanceSearch(String lastName ,String firstName ,String department ,String role ,String freeText) {
		JSONArray jsonArray = new JSONArray();
		try {
			if(lastName != null && !lastName.equalsIgnoreCase(""))
			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.LAST_NAME)
					.put(Const.SEARCH_VALUE, lastName)
					);
			
			if(firstName != null && !firstName.equalsIgnoreCase(""))
			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.FIRST_NAME)
					.put(Const.SEARCH_VALUE, firstName)
					);

			if(department != null && !department.equalsIgnoreCase(""))
			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.DEPARTMENT)
					.put(Const.SEARCH_VALUE, department)
					);

			if(role != null && !role.equalsIgnoreCase(""))
			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.JOB)
					.put(Const.SEARCH_VALUE, role)
					);

			if(freeText != null && !freeText.equalsIgnoreCase(""))
			jsonArray.put(
					new JSONObject()
					.put(Const.SEARCH_KEY, Const.FREE_TEXT)
					.put(Const.SEARCH_VALUE, freeText)
					);
			
			addObjectToMainJsonArray(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
	
	public void getManagersWithArray(ArrayList<String> serviceManagerIds) {
		JSONArray jsonArray = new JSONArray();
		try {
			for (int i = 0; i < serviceManagerIds.size(); i++) {
				jsonArray.put(
						new JSONObject()
						.put(Const.SEARCH_KEY, Const.ACCOUNT_NAME)
						.put(Const.SEARCH_VALUE, serviceManagerIds.get(i))
						);
			}
			addObjectToMainJsonArray(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.execute();
	}
}

