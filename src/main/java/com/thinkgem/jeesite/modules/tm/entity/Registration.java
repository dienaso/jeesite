/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.util.List;

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

	private String recNormal; // 推荐普通注册
	private String recUrgent; // 推荐加急注册
	private int recNormalTotalPrice;// 推荐普通注册总价
	private String recNormalDiscountPrice;// 推荐普通注册优惠价
	private int recUrgentTotalPrice;// 推荐加急注册总价
	private String recUrgentDiscountPrice;// 推荐加急注册优惠价
	private String extNormal; // 扩展普通注册
	private String extUrgent; // 扩展加急注册
	private int extNormalTotalPrice;// 拓展普通注册总价
	private String extNormalDiscountPrice;// 拓展普通注册优惠价
	private int extUrgentTotalPrice;// 拓展加急注册总价
	private String extUrgentDiscountPrice;// 拓展加急注册优惠价

	private String tmSt = "1";// 查询类型
	private String tmSc = "1,2,3,4,5,10";// 查询条件
	private int pages;

	private List<Similar> similarList = Lists.newArrayList(); // 子表列表

	public List<String> getTmScList() {
		List<String> list = Lists.newArrayList();
		if (tmSc != null) {
			for (String s : StringUtils.split(tmSc, ",")) {
				list.add(s);
			}
		}
		return list;
	}
	
	public void setTmScList(List<String> tmScList) {
		if (tmScList != null && tmScList.size() > 0) {
			this.tmSc = tmScList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.tmSc = "";
		}
	}

	public List<String> getRecNormalList() {
		List<String> list = Lists.newArrayList();
		if (recNormal != null) {
			for (String s : StringUtils.split(recNormal, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setRecNormalList(List<String> recNormalList) {
		if (recNormalList != null && recNormalList.size() > 0) {
			this.recNormal = recNormalList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.recNormal = "";
		}
	}

	public List<String> getRecUrgentList() {
		List<String> list = Lists.newArrayList();
		if (recUrgent != null) {
			for (String s : StringUtils.split(recUrgent, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setRecUrgentList(List<String> recUrgentList) {
		if (recUrgentList != null && recUrgentList.size() > 0) {
			this.recUrgent = recUrgentList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.recUrgent = "";
		}
	}

	public List<String> getExtNormalList() {
		List<String> list = Lists.newArrayList();
		if (extNormal != null) {
			for (String s : StringUtils.split(extNormal, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setExtNormalList(List<String> extNormalList) {
		if (extNormalList != null && extNormalList.size() > 0) {
			this.extNormal = extNormalList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.extNormal = "";
		}
	}

	public List<String> getExtUrgentList() {
		List<String> list = Lists.newArrayList();
		if (extUrgent != null) {
			for (String s : StringUtils.split(extUrgent, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public void setExtUrgentList(List<String> extUrgentList) {
		if (extUrgentList != null && extUrgentList.size() > 0) {
			this.extUrgent = extUrgentList.toString().replace(" ", "").replace("[", "").replace("]", "");
		} else {
			this.extUrgent = "";
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

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public String getTmSt() {
		return tmSt;
	}

	public void setTmSt(String tmSt) {
		this.tmSt = tmSt;
	}

	public String getTmSc() {
		return tmSc;
	}

	public void setTmSc(String tmSc) {
		this.tmSc = tmSc;
	}

	public String getRecNormal() {
		return recNormal;
	}

	public void setRecNormal(String recNormal) {
		this.recNormal = recNormal;
	}

	public String getRecUrgent() {
		return recUrgent;
	}

	public void setRecUrgent(String recUrgent) {
		this.recUrgent = recUrgent;
	}

	public Integer getRecNormalTotalPrice() {
		return recNormalTotalPrice;
	}

	public void setRecNormalTotalPrice(int recNormalTotalPrice) {
		this.recNormalTotalPrice = recNormalTotalPrice;
	}

	public String getRecNormalDiscountPrice() {
		return recNormalDiscountPrice;
	}

	public void setRecNormalDiscountPrice(String recNormalDiscountPrice) {
		this.recNormalDiscountPrice = recNormalDiscountPrice;
	}

	public Integer getRecUrgentTotalPrice() {
		return recUrgentTotalPrice;
	}

	public void setRecUrgentTotalPrice(int recUrgentTotalPrice) {
		this.recUrgentTotalPrice = recUrgentTotalPrice;
	}

	public String getRecUrgentDiscountPrice() {
		return recUrgentDiscountPrice;
	}

	public void setRecUrgentDiscountPrice(String recUrgentDiscountPrice) {
		this.recUrgentDiscountPrice = recUrgentDiscountPrice;
	}

	public String getExtNormal() {
		return extNormal;
	}

	public void setExtNormal(String extNormal) {
		this.extNormal = extNormal;
	}

	public String getExtUrgent() {
		return extUrgent;
	}

	public void setExtUrgent(String extUrgent) {
		this.extUrgent = extUrgent;
	}

	public Integer getExtNormalTotalPrice() {
		return extNormalTotalPrice;
	}

	public void setExtNormalTotalPrice(int extNormalTotalPrice) {
		this.extNormalTotalPrice = extNormalTotalPrice;
	}

	public String getExtNormalDiscountPrice() {
		return extNormalDiscountPrice;
	}

	public void setExtNormalDiscountPrice(String extNormalDiscountPrice) {
		this.extNormalDiscountPrice = extNormalDiscountPrice;
	}

	public Integer getExtUrgentTotalPrice() {
		return extUrgentTotalPrice;
	}

	public void setExtUrgentTotalPrice(int extUrgentTotalPrice) {
		this.extUrgentTotalPrice = extUrgentTotalPrice;
	}

	public String getExtUrgentDiscountPrice() {
		return extUrgentDiscountPrice;
	}

	public void setExtUrgentDiscountPrice(String extUrgentDiscountPrice) {
		this.extUrgentDiscountPrice = extUrgentDiscountPrice;
	}

	
}