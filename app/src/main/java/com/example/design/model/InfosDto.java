package com.example.design.model;

import java.util.List;

public class InfosDto {
	private List<Infos> infosList;
	private String nextPageUrl;
	private int pageInfo;// 页数

	public int getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(int pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<Infos> getInfoList() {
		return this.infosList;
	}

	public void setInfoList(List<Infos> infosList) {
		this.infosList = infosList;
	}

	public String getNextPageUrl() {
		return this.nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}
}