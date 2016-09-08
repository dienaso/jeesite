/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tm.TmResult;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.tm.entity.Registration;
import com.thinkgem.jeesite.modules.tm.service.RegistrationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 商标注册评估Controller
 * 
 * @author 吴小平
 * @version 2016-08-01
 */
@Controller
@RequestMapping(value = "${adminPath}/tm/registration")
public class RegistrationController extends BaseController {

	@Autowired
	private RegistrationService registrationService;

	@ModelAttribute
	public Registration get(@RequestParam(required = false) String id) {
		Registration entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = registrationService.get(id);
		}
		if (entity == null) {
			entity = new Registration();
		}
		return entity;
	}

	@RequiresPermissions("tm:registration:view")
	@RequestMapping(value = { "list", "" })
	public String list(Registration registration, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<Registration> page = registrationService.findPage(new Page<Registration>(request, response), registration);
		model.addAttribute("page", page);
		return "modules/tm/registrationList";
	}

	@RequiresPermissions("tm:registration:view")
	@RequestMapping(value = "form")
	public String form(Registration registration, Model model) {
		model.addAttribute("registration", registration);
		return "modules/tm/registrationForm";
	}

	@RequiresPermissions("tm:registration:edit")
	@RequestMapping(value = "save")
	public String save(Registration registration, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, registration)) {
			return form(registration, model);
		}
		registrationService.save(registration);
		addMessage(redirectAttributes, "保存商标注册评估成功");
		return "redirect:" + Global.getAdminPath() + "/tm/registration/?repage";
	}

	@RequiresPermissions("tm:registration:edit")
	@RequestMapping(value = "delete")
	public String delete(Registration registration, RedirectAttributes redirectAttributes) {
		registrationService.delete(registration);
		addMessage(redirectAttributes, "删除商标注册评估成功");
		return "redirect:" + Global.getAdminPath() + "/tm/registration/?repage";
	}

	@RequiresPermissions("tm:registration:searchTm")
	@ResponseBody
	@RequestMapping(value = "searchTm")
	public TmResult searchTm(String st, String sc, String intCls, String searchKey) throws Exception {
		TmResult result = registrationService.searchTm(st, sc, intCls, searchKey);
		return result;
	}

	@RequiresPermissions("tm:registration:generateReports")
	@RequestMapping(value = "generateReports")
	public String generateReports(Registration registration, RedirectAttributes redirectAttributes) {
		int result = 0;

		try {
			result = registrationService.generateReports(registration);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = 10;
		}

		if (result == 0) {
			addMessage(redirectAttributes, "生成商标评估报告成功");
		} else if (result == 2) {
			addMessage(redirectAttributes, "生成商标评估报告失败，请先上传个人头像和微信二维码");
		} else if (result == 3) {
			addMessage(redirectAttributes, "生成商标评估报告失败，请完善商标注册主体信息");
		} else if (result == 4) {
			addMessage(redirectAttributes, "生成商标评估报告失败，请填写近似度与注册建议");
		} else if (result == 7) {
			addMessage(redirectAttributes, "生成PDF商标评估报告失败，请联系开发人员");
		} else {
			addMessage(redirectAttributes, "生成商标评估报告失败，请联系开发人员");
		}

		return "redirect:" + Global.getAdminPath() + "/tm/registration/?repage";
	}

	/**
	 * 下载PDF
	 * 
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("tm:registration:edit")
	@RequestMapping(value = "download")
	public String download(Registration registration, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = registration.getReportNo() + ".pdf";
			String pdfPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + "/tmReport/pdf/";
			FileUtils.downFile(new File(pdfPath + fileName), request, response);
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "报告下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/tm/registration/?repage";
	}

}