/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.tm.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.tm.entity.Similar;

/**
 * 商标注册评估DAO接口
 * @author 吴小平
 * @version 2016-08-01
 */
@MyBatisDao
public interface SimilarDao extends CrudDao<Similar> {
	
}