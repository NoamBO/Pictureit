package com.pictureit.leumi.main;

import java.util.ArrayList;

import com.pictureit.leumi.server.parse.LeumiService;
import com.pictureit.leumi.server.parse.Profile;
import com.pictureit.leumi.server.parse.SystemAddition;

public class LocalStorageManager {

	private static LocalStorageManager INSTANCE;

	public static LocalStorageManager getInstance() {
		if (INSTANCE != null)
			return INSTANCE;
		else {
			INSTANCE = new LocalStorageManager();
			return INSTANCE;
		}

	}

	public ArrayList<LeumiService> homeServicesList;

	public SystemAddition systemAddition;

	public Profile currentUserProfile;

}
