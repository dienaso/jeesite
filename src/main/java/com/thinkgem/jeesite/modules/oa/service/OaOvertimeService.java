/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.oa.entity.OaOvertime;
import com.thinkgem.jeesite.modules.oa.dao.OaOvertimeDao;

/**
 * 加班申请Service
 * @author 吴小平
 * @version 2016-05-06
 */
@Service
@Transactional(readOnly = true)
public class OaOvertimeService extends CrudService<OaOvertimeDao, OaOvertime> {

	public OaOvertime get(String id) {
		return super.get(id);
	}
	
	public List<OaOvertime> findList(OaOvertime oaOvertime) {
		return super.findList(oaOvertime);
	}
	
	public Page<OaOvertime> findPage(Page<OaOvertime> page, OaOvertime oaOvertime) {
		return super.findPage(page, oaOvertime);
	}
	
	@Transactional(readOnly = false)
	public void save(OaOvertime oaOvertime) {
		super.save(oaOvertime);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaOvertime oaOvertime) {
		super.delete(oaOvertime);
	}
	
}