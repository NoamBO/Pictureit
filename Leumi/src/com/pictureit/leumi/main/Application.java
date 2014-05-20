package com.pictureit.leumi.main;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

//@ReportsCrashes(formKey = "", formUri = "http://www.yourselectedbackend.com/reportpath")
public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ACRA.init(this);

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())

		.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory().discCacheFileCount(100)
				.build();

		ImageLoader.getInstance().init(config);
	}
}
