/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 商标注册评估Entity
 * @author 吴小平
 * @version 2016-08-01
 */
public class Similar extends DataEntity<Similar> {
	
	private static final long serialVersionUID = 1L;
	private Registration tm_registration;		// 关联商标注册主体表 父类
	private String tmName;		// 相似商标名
	private String tmImg;		// 相似商标图形
	private String currentStatus;		// 商标状态
	private String intCls;		// 所属类别
	private String applicantCn;		// 申请人
	private String regNo;		// 申请号/注册号
	private String agent;		// 代理公司
	private String similarity;		// 相似度
	private String advise;		// 顾问建议
	
	public Similar() {
		super();
	}

	public Similar(String id){
		super(id);
	}

	public Similar(Registration tm_registration){
		this.tm_registration = tm_registration;
	}

	@Length(min=0, max=64, message="关联商标注册主体表长度必须介于 0 和 64 之间")
	public Registration getTm_registration() {
		return tm_registration;
	}

	public void setTm_registration(Registration tm_registration) {
		this.tm_registration = tm_registration;
	}
	
	@Length(min=0, max=100, message="相似商标名长度必须介于 0 和 100 之间")
	public String getTmName() {
		return tmName;
	}

	public void setTmName(String tmName) {
		this.tmName = tmName;
	}
	
	@Length(min=0, max=300, message="相似商标图形长度必须介于 0 和 300 之间")
	public String getTmImg() {
		return tmImg;
	}

	public void setTmImg(String tmImg) {
		this.tmImg = tmImg;
	}
	
	@Length(min=0, max=100, message="商标状态长度必须介于 0 和 100 之间")
	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	
	@Length(min=0, max=5, message="所属类别长度必须介于 0 和 5 之间")
	public String getIntCls() {
		return intCls;
	}

	public void setIntCls(String intCls) {
		this.intCls = intCls;
	}
	
	@Length(min=0, max=100, message="申请人长度必须介于 0 和 100 之间")
	public String getApplicantCn() {
		return applicantCn;
	}

	public void setApplicantCn(String applicantCn) {
		this.applicantCn = applicantCn;
	}
	
	@Length(min=0, max=64, message="申请号/注册号长度必须介于 0 和 64 之间")
	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}
	
	@Length(min=0, max=100, message="代理公司长度必须介于 0 和 100 之间")
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	
	@Length(min=0, max=5, message="相似度长度必须介于 0 和 5 之间")
	public String getSimilarity() {
		return similarity;
	}

	public void setSimilarity(String similarity) {
		this.similarity = similarity;
	}
	
	@Length(min=0, max=300, message="顾问建议长度必须介于 0 和 300 之间")
	public String getAdvise() {
		return advise;
	}

	public void setAdvise(String advise) {
		this.advise = advise;
	}
	
}