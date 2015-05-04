package com.example.design;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
/**
 * @author Zack White
 */
public class CSDN extends Application {
	private static CSDN instance;

	/**
	 *
	 * @return CSDN
	 */
	public static CSDN getInstance() {
		if (instance == null) {
			instance = new CSDN();
		}
		return instance;
	}

	/**
	 *
	 * @return getInstance()
	 */
	public static CSDN getContext() {
		return getInstance();
	}

	@Override
	public void onCreate() {
		synchronized (CSDN.class) {
			instance = this;
		}
		super.onCreate();
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
	}
}
