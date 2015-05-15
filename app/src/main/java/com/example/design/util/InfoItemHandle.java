package com.example.design.util;

import android.util.Log;

import com.example.design.control.Constant;
import com.example.design.model.InfoItem;
import com.example.design.model.Infos;
import com.example.design.model.InfosDto;
import com.example.design.tool.LogTool;
import com.example.design.tool.StringTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理InfoItem的业务类
 */
public class InfoItemHandle {

	public List<InfoItem> getInfosItems(int infosType, int currentPage) throws Exception {
		String urlStr = URLUtil.getUrl(infosType, currentPage);// 获取地址
//		String htmlStr = DataUtil.doGet(urlStr);// 获取数据

		List<InfoItem> infosItems = new ArrayList<InfoItem>();
		InfoItem infosItem = null;

//		Document doc = Jsoup.parse(urlStr);// 解析html数据
		Document doc = null;
		Long start = System.currentTimeMillis();
		try {
			doc = Jsoup.connect(urlStr).timeout(5000).get();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			LogTool.e("Time is:", (System.currentTimeMillis()-start) + "ms");
		}
		Document content1 = Jsoup.parse(doc.toString());
		Elements units = content1.getElementsByClass("pic_list");
		Document divcontions = Jsoup.parse(units.toString());
		Elements element = divcontions.getElementsByTag("li");
		for (int i = 0; i < element.size(); i++) {
			infosItem = new InfoItem();
			infosItem.setInfoType(infosType);

			Element unit_ele = element.get(i);

			String title = StringTool.ToDBC(unit_ele.select("a").attr("title"));
			String link = Constant.URL_HOST +unit_ele.select("a").attr("href").trim();
			String img = unit_ele.select("img").attr("src").trim();
			String imgUrl;
			if (img.contains("http://")) {
				imgUrl = img;
			} else
				imgUrl = Constant.URL_HOST + img;

			String info = unit_ele.getElementsByTag("p").get(1).text();

			infosItem.setLink(link);
			infosItem.setTitle(title);
			infosItem.setDate(info);
			try {
				infosItem.setImgLink(imgUrl);
			} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {

			}
			infosItem.setContent(info);
			infosItems.add(infosItem);
		}
		return infosItems;
	}

	public InfosDto getInfos(String urlStr,int page) throws Exception {
		InfosDto infosDto = new InfosDto();
		List<Infos> infosList = new ArrayList<Infos>();
//		String htmlStr = DataUtil.doGet(urlStr);
		long start = System.currentTimeMillis();
		Document doc = null;
		try {
			doc = Jsoup.connect(urlStr).timeout(5000).get();
		} catch (Exception e) {
			e.getStackTrace();
		} finally{
			LogTool.e("Time=====", (System.currentTimeMillis() - start) + "ms");
		}

		Element detailEle = doc.select(".mainleft").get(0);

		Element titleEle = detailEle.select("h1").get(0);
		Infos infos = new Infos();
		infos.setTitle(titleEle.text());
		infos.setType(1);
//		if (page == 1)
//			infosList.add(infos);

		Element summaryEle = detailEle.select("div.article_info").get(0);
		infos = new Infos();
		infos.setSummary(summaryEle.select("span").get(0).text() + "   " + summaryEle.select("span").get(1).text());
		if (page == 1)
			infosList.add(infos);

		Element contentEle = detailEle.select("div.pic_bd").get(0);
		Elements childrenEle = contentEle.children();

		for (Element child : childrenEle) {
			Elements imgEles = child.getElementsByTag("img");

			if (imgEles.size() > 0) {
				for (Element imgEle : imgEles) {
					if (imgEle.attr("src").equals(""))
						continue;
					infos = new Infos();
					String img = imgEle.attr("src").trim();
					String imgUrl;
					if (img.contains("http://")) {
						imgUrl = img;
					} else
						imgUrl = Constant.URL_HOST + img;
					infos.setImageLink(imgUrl);
					infosList.add(infos);
				}
			}

			imgEles.remove();
		}
		infosDto.setInfoList(infosList);
		return infosDto;
	}
}
