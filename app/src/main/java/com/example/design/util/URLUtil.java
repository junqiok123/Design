package com.example.design.util;


import com.example.design.control.Constant;

public class URLUtil {

    public static String getUrl(int newsType, int currentPage) {
        currentPage = currentPage > 0 ? currentPage : 1;
        String urlStr = Constant.URL_HOST;
        switch (newsType) {
            case Constant.TYPE_GALLERY:
                urlStr += Constant.URL_GALLERY;
                break;
            case Constant.TYPE_GRAPHIC:
                urlStr += Constant.URL_GRAPHIC;
                break;
            case Constant.TYPE_VI:
                urlStr += Constant.URL_VI;
                break;
            case Constant.TYPE_LOGO:
                urlStr += Constant.URL_LOGO;
                break;
            case Constant.TYPE_PICTURE:
                urlStr += Constant.URL_PICTURE;
                break;
            case Constant.TYPE_POSTER:
                urlStr += Constant.URL_POSTER;
                break;
            case Constant.TYPE_PACKAGE:
                urlStr += Constant.URL_PACKAGE;
                break;
            case Constant.TYPE_BOOK:
                urlStr += Constant.URL_BOOK;
                break;
            case Constant.TYPE_CARD:
                urlStr += Constant.URL_CARD;
                break;
            case Constant.TYPE_ESTATE:
                urlStr += Constant.URL_ESTATE;
                break;
            case Constant.TYPE_FORMAT:
                urlStr += Constant.URL_FORMAT;
                break;
            case Constant.TYPE_COVER:
                urlStr += Constant.URL_COVER;
                break;
            case Constant.TYPE_ADVERTISING:
                urlStr += Constant.URL_ADVERTISING;
                break;
            case Constant.TYPE_FONT:
                urlStr += Constant.URL_FONT;
                break;
            case Constant.TYPE_SINGLE:
                urlStr += Constant.URL_SINGLE;
                break;
            case Constant.TYPE_UI:
                urlStr += Constant.URL_UI;
                break;
            case Constant.TYPE_WEB:
                urlStr += Constant.URL_WEB;
                break;
            case Constant.TYPE_INDUSTRY:
                urlStr += Constant.URL_INDUSTRY;
                break;
            case Constant.TYPE_ENVIRONMENT:
                urlStr += Constant.URL_ENVIRONMENT;
                break;
            case Constant.TYPE_INTERIOR:
                urlStr += Constant.URL_INTERIOR;
                break;
            case Constant.TYPE_CLOTHING:
                urlStr += Constant.URL_CLOTHING;
                break;
            case Constant.TYPE_PHOTOGRAPHY:
                urlStr += Constant.URL_PHOTOGRAPHY;
                break;
            case Constant.TYPE_ILLUSTRATION:
                urlStr += Constant.URL_ILLUSTRATION;
                break;
            case Constant.TYPE_CHINA_PAINTING:
                urlStr += Constant.URL_CHINA_PAINTING;
                break;
            case Constant.TYPE_OIL_PAINTING:
                urlStr += Constant.URL_OIL_PAINTING;
                break;
            case Constant.TYPE_OTHER:
                urlStr += Constant.URL_OTHER;
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
            buffer.delete(buffer.length() - 5, buffer.length());
            urlStr = buffer + "_" + currentPage + ".html";
        } else
            urlStr = url;


        return urlStr;

    }
}
