/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.web.front;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.tm.entity.Registration;
import com.thinkgem.jeesite.modules.tm.service.RegistrationService;

/**
 * 网站Controller
 * 
 * @author 吴小平
 * @version 2016-8-2
 */
@Controller
@RequestMapping(value = "${frontPath}/tm/report")
public class ReportController extends BaseController {

	@Autowired
	private RegistrationService registrationService;
	@Autowired
	ServletContext context;

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "index-{id}")
	public String index(@PathVariable String id, Model model) {
		Registration registration = registrationService.get(id);
		if (registration == null)
			return null;
		String path = "/userfiles/tmReport/images/" + registration.getReportNo() + "/";
		path = FileUtils.path(path);
		model.addAttribute("registration", registration);
		model.addAttribute("path", path);
		return "modules/tm/front/report";
	}

}
