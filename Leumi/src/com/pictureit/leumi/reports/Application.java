package com.pictureit.leumi.reports;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(formKey = "", formUri = "http://www.yourselectedbackend.com/reportpath")
public class Application extends android.app.Application{
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
}
