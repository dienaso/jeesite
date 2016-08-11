/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import com.google.common.collect.Lists;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 商标注册评估Entity
 * 
 * @author 吴小平
 * @version 2016-08-01
 */
public class Registration extends DataEntity<Registration> {

	private static final long serialVersionUID = 1L;
	private String reportNo; // 报告编号
	private String applicantCn; // 注册人
	private String businessType; // 行业类型
	private String intCls; // 注册类别

	private String tmName; // 期望商标名称
	private String genTimes; // 报告生成次数
	private String tmImg; // 期望商标图形
	private String reportImg;// 报告图片
	
	private String safe; // 稳健型注册
	private String balance; // 平衡型注册
	private String radical; // 激进型注册
	private int pages;

	private List<Similar> similarList = Lists.newArrayList(); // 子表列表

	public List<String> getSafeList() {
		List<String> list = Lists.newArrayList();
		if (safe != null) {
			for (String s : StringUtils.split(safe, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setSafeList(List<String> safeList) {
		if (safeList != null && safeList.size() > 0) {
			this.safe = safeList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.safe = "";
		}
	}
	
	public List<String> getBalanceList() {
		List<String> list = Lists.newArrayList();
		if (balance != null) {
			for (String s : StringUtils.split(balance, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setBalanceList(List<String> balanceList) {
		if (balanceList != null && balanceList.size() > 0) {
			this.balance = balanceList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.balance = "";
		}
	}
	
	public List<String> getRadicalList() {
		List<String> list = Lists.newArrayList();
		if (radical != null) {
			for (String s : StringUtils.split(radical, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setRadicalList(List<String> radicalList) {
		if (radicalList != null && radicalList.size() > 0) {
			this.radical = radicalList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.radical = "";
		}
	}
	
	public List<String> getBusinessTypeList() {
		List<String> list = Lists.newArrayList();
		if (businessType != null) {
			for (String s : StringUtils.split(businessType, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setBusinessTypeList(List<String> businessTypeList) {
		if (businessTypeList != null && businessTypeList.size() > 0) {
			this.businessType = businessTypeList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.businessType = "";
		}
	}

	public List<String> getIntClsList() {
		List<String> list = Lists.newArrayList();
		if (intCls != null) {
			for (String s : StringUtils.split(intCls, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setIntClsList(List<String> intClsList) {
		if (intClsList != null && intClsList.size() > 0) {
			this.intCls = intClsList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.intCls = "";
		}

	}

	public Registration() {
		super();
	}

	public Registration(String id) {
		super(id);
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	@Length(min = 1, max = 200, message = "注册人长度必须介于 1 和 200 之间")
	public String getApplicantCn() {
		return applicantCn;
	}

	public void setApplicantCn(String applicantCn) {
		this.applicantCn = applicantCn;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getIntCls() {
		return intCls;
	}

	public void setIntCls(String intCls) {
		this.intCls = intCls;
	}

	@Length(min = 1, max = 100, message = "期望商标名称长度必须介于 1 和 100 之间")
	public String getTmName() {
		return tmName;
	}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}

	public String getGenTimes() {
		return genTimes;
	}

	public void setGenTimes(String genTimes) {
		this.genTimes = genTimes;
	}

	public String getTmImg() {
		return tmImg;
	}

	public void setTmImg(String tmImg) {
		this.tmImg = tmImg;
	}

	public List<Similar> getSimilarList() {
		return similarList;
	}

	public void setSimilarList(List<Similar> similarList) {
		this.similarList = similarList;
	}

	public String getReportImg() {
		return reportImg;
	}

	public void setReportImg(String reportImg) {
		this.reportImg = reportImg;
	}
	
	@Length(min=1, max=64, message="请选择稳健型注册类别")
	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

	@Length(min=1, max=64, message="请选择平衡型注册类别")
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	@Length(min=1, max=64, message="请选择冲击型注册类别")
	public String getRadical() {
		return radical;
	}

	public void setRadical(String radical) {
		this.radical = radical;
	}
	
	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
}