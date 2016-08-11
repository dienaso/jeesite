/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.service;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.ImagePdfUtils;
import com.thinkgem.jeesite.common.utils.ImageUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tm.TmKooUtils;
import com.thinkgem.jeesite.common.utils.tm.TmResult;
import com.thinkgem.jeesite.modules.tm.entity.Registration;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.tm.dao.RegistrationDao;
import com.thinkgem.jeesite.modules.tm.entity.Similar;
import com.thinkgem.jeesite.modules.tm.dao.SimilarDao;

/**
 * 商标注册评估Service
 * 
 * @author 吴小平
 * @version 2016-08-01
 */
@Service
@Transactional(readOnly = true)
public class RegistrationService extends CrudService<RegistrationDao, Registration> {

	@Autowired
	private SimilarDao similarDao;
	@Autowired
	private RegistrationDao registrationDao;

	public Registration get(String id) {
		Registration registration = super.get(id);
		registration.setSimilarList(similarDao.findList(new Similar(registration)));
		return registration;
	}

	public List<Registration> findList(Registration registration) {
		User user = UserUtils.getUser();
		registration.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
		return super.findList(registration);
	}

	public Page<Registration> findPage(Page<Registration> page, Registration registration) {
		User user = UserUtils.getUser();
		registration.getSqlMap().put("dsf", dataScopeFilter(user, "o", "u"));
		return super.findPage(page, registration);
	}

	@Transactional(readOnly = false)
	public void save(Registration registration) {
		super.save(registration);
		for (Similar similar : registration.getSimilarList()) {
			if (similar.getId() == null) {
				continue;
			}
			if (Similar.DEL_FLAG_NORMAL.equals(similar.getDelFlag())) {
				if (StringUtils.isBlank(similar.getId())) {
					similar.setTm_registration(registration);
					similar.preInsert();
					similarDao.insert(similar);
				} else {
					similar.preUpdate();
					similarDao.update(similar);
				}
			} else {
				similarDao.delete(similar);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(Registration registration) {
		super.delete(registration);
		similarDao.delete(new Similar(registration));
	}

	public TmResult searchTm(String intCls, String searchKey) throws Exception {
		TmResult result = TmKooUtils.doSearch("1", intCls, searchKey);
		return result;
	}

	@Transactional(readOnly = false)
	public int generateReports(Registration registration) {

		String reportNo = registration.getReportNo();
		String savePath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + "/tmReport/images/" + reportNo
				+ "/";
		String pdfPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + "/tmReport/pdf/";
		String templatePath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + "/tmReport/template/";
		templatePath = FileUtils.path(templatePath);
		savePath = FileUtils.path(savePath);
		pdfPath = FileUtils.path(pdfPath);
		FileUtils.createDirectory(savePath);
		FileUtils.createDirectory(pdfPath);
		
		System.out.println("templatePath:"+templatePath);
		System.out.println("savePath:"+savePath);
		System.out.println("pdfPath:"+pdfPath);

		Color black = Color.decode("#000000");
		// 1、生成首页
		ImageUtils.pressTextWithLine("报告编号：" + reportNo, templatePath + "1.jpg", savePath + "1-large.jpg", "微软雅黑",
				Font.PLAIN, black, 40, 0, 300, 1.0f);// 添加文字水印
		//ImageUtils.scale(savePath + "1-large.jpg", savePath + "1.jpg", 2, false);// 缩略图

		// 2、生成销售专员信息图
		String name = registration.getCurrentUser().getName();// 姓名
		String no = registration.getCurrentUser().getNo();// 工号
		String photo = registration.getCurrentUser().getPhoto();// 头像

		// 校验是否上传头像
		if (photo == null || photo.equals("")) {
			return 2;
		}

		photo = FileUtils.path(Global.getUserfilesBaseDir() + photo);
		String mobile = registration.getCurrentUser().getMobile();// 手机

		ImageUtils.pressSellInfo(photo, name, no, mobile, templatePath + "2.jpg", savePath + "2-large.jpg", "微软雅黑",
				Font.PLAIN, black, 40);// 添加文字和图片水印
		//ImageUtils.scale(savePath + "2-large.jpg", savePath + "2.jpg", 2, false);// 缩略图

		// 3、生成注册主体信息图
		String applicantCn = registration.getApplicantCn();
		String businessType = DictUtils.getDictLabels(registration.getBusinessType(), "tm_business", "");
		String tmName = registration.getTmName();

		// 校验注册主体信息
		if (applicantCn == null || applicantCn.equals("") || businessType == null || businessType.equals("")
				|| tmName == null || tmName.equals("")) {
			return 3;
		}

		String tmImg = registration.getTmImg();
		if (tmImg != null && !tmImg.equals("")) {
			tmImg = FileUtils.path(Global.getUserfilesBaseDir() + tmImg);
		}

		ImageUtils.pressMainInfo(tmImg, applicantCn, businessType, tmName, templatePath + "3.jpg",
				savePath + "3-large.jpg", "微软雅黑", Font.PLAIN, black, 40);// 添加文字和图片水印
		//ImageUtils.scale(savePath + "3-large.jpg", savePath + "3.jpg", 2, false);// 缩略图

		// 4、生成相似商标区分左页图
		int i = 4;
		List<Similar> similarList = registration.getSimilarList();
		if (similarList != null && similarList.size() > 0) {
			String sTmName = "";
			String sCls = "";
			String sRegNoAndStatus = "";
			String sApplicantCn = "";
			String sSimilarity = "";
			String sTmImg = "";
			String sAdvise = "";

			for (Similar similar : similarList) {
				sTmName = similar.getTmName();
				sCls = DictUtils.getDictLabels(similar.getIntCls(), "tm_category", "");
				sRegNoAndStatus = similar.getRegNo() + " / " + similar.getCurrentStatus();
				sApplicantCn = similar.getApplicantCn();
				sSimilarity = similar.getSimilarity();
				if (sTmImg != null || !"".equals(sTmImg)) {
					sTmImg = Global.getUserfilesBaseDir() + similar.getTmImg();
					sTmImg = FileUtils.path(sTmImg);
				}
				sAdvise = similar.getAdvise();

				// 校验相似商标信息
				if (sSimilarity == null || sSimilarity.equals("") || sAdvise == null || sAdvise.equals("")) {
					return 4;
				}

				String tempImg = "4-left.jpg";
				if (i % 2 == 1) {
					tempImg = "4-right.jpg";
				}
				ImageUtils.pressSimilarInfo(sTmImg, sTmName, sCls, sRegNoAndStatus, sApplicantCn, sSimilarity, sAdvise,
						templatePath + tempImg, savePath + i + "-large.jpg");// 添加文字和图片水印
				//ImageUtils.scale(savePath + i + "-large.jpg", savePath + i + ".jpg", 2, false);// 缩略图
				i++;
			}

		}

		// 5、生成商标注册建议方案图
		String safe = registration.getSafe();
		String balance = registration.getBalance();
		String radical = registration.getRadical();

		ImageUtils.pressAdviseInfo(safe, balance, radical, templatePath + "5.jpg", savePath + i + "-large.jpg", 25);// 添加文字水印
		//ImageUtils.scale(savePath + i + "-large.jpg", savePath + i + ".jpg", 2, false);// 缩略图
		i++;

		// 6、尾页
		//FileUtils.copyFile(templatePath + "6.jpg", savePath + i + ".jpg");
		FileUtils.copyFile(templatePath + "6-large.jpg", savePath + i + "-large.jpg");

		// 7、生成pdf
		try {
			ImagePdfUtils.imgMerageToPdf(new File(savePath).listFiles(), new File(pdfPath + reportNo + ".pdf"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 7;
		}

		// 更新报告生成次数
		registrationDao.updateGenTimes(registration);
		// 更新报告页数
		registration.setPages(i);
		registrationDao.updatePages(registration);

		return 0;
	}

}