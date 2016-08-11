package com.thinkgem.jeesite.common.utils.tm;

import java.util.List;

public class TmResult {
	private String ret;
	private String msg;
	private String remainCount;
	private String allRecords;
	private List<TmInfo> results;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(String remainCount) {
		this.remainCount = remainCount;
	}

	public String getAllRecords() {
		return allRecords;
	}

	public void setAllRecords(String allRecords) {
		this.allRecords = allRecords;
	}

	public List<TmInfo> getResults() {
		return results;
	}

	public void setResults(List<TmInfo> results) {
		this.results = results;
	}

}
