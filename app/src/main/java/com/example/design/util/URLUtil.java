package com.example.design.util;


import com.example.design.control.Constant;

public class URLUtil {

	public static String getUrl(int newsType, int currentPage) {
		currentPage = currentPage > 0 ? currentPage : 1;
		String urlStr = "";
		switch (newsType) {
		case Constant.TYPE_NEWS:
			urlStr = Constant.URL_NEWS;
			break;
		case Constant.TYPE_MOBILE:
			urlStr = Constant.URL_MOBILE;
			break;
		case Constant.TYPE_SD:
			urlStr = Constant.URL_SD;
			break;
		case Constant.TYPE_PROGRAMMER:
			urlStr = Constant.URL_PROGRAMMER;
			break;
		case Constant.TYPE_CLOUD:
			urlStr = Constant.URL_CLOUD;
			break;
		}

		urlStr += "/" + "list_" + currentPage + ".html";

		return urlStr;

	}

	public static String getDetailUrl(String url, int currentPage) {
		currentPage = currentPage > 0 ? currentPage : 1;
		String urlStr = "";

		StringBuffer buffer = new StringBuffer();
		buffer.append(url);
		if (currentPage > 1) {
			buffer.delete(buffer.length() - 5,buffer.length());
			urlStr = buffer + "_" + currentPage + ".html";
		} else
			urlStr = url;


		return urlStr;

	}
}
