package com.example.design.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;

/**
 * 保存图片
 * 
 */
public class SaveImagesUtil {
public static final int SaveSucced=6;
public static final int SaveFailure=5;
	public static Bitmap getBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public static void saveImages(Context context, Bitmap bmp, Handler handler) {
	    // 首先保存图片
	    File appDir = new File(Environment.getExternalStorageDirectory(), "DesignImages");
	    if (!appDir.exists()) {
	        appDir.mkdirs();
	    }
	    String fileName = System.currentTimeMillis() + ".jpg";
	    File file = new File(appDir, fileName);
	    try {
	        FileOutputStream fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.PNG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
			handler.sendMessage(handler.obtainMessage(SaveFailure, fileName));
		} catch (IOException e) {
			e.printStackTrace();
			handler.sendMessage(handler.obtainMessage(SaveFailure, fileName));
		}
		handler.sendMessage(handler.obtainMessage(SaveSucced, fileName));
	}
}
