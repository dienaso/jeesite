package com.thinkgem.jeesite.common.utils.tm;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.common.utils.HttpUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

public class TmKooUtils {

	/**
	 * 
	 * @param searchType
	 *            1: 商标名， 2：注册号， 3：申请人 4：商标名/注册号/申请人只要匹配到就算
	 * @param intCls
	 *            0：全部国际分类，非0：限定在指定类别，类别间用分号分割。如：4;12;34 表示在第4、12、34类内查询
	 * @param searchKey
	 *            关键词，中文未转码
	 * @return
	 * @throws Exception
	 */
	public static TmResult doSearch(String searchType, String intCls, String searchKey) throws Exception {
		Principal principal = (Principal) UserUtils.getPrincipal();

		TmResult rtn = new TmResult();
		String param = "apiKey=" + Global.getConfig("tm.tmkoo.apiKey") + "&apiPassword="
				+ Global.getConfig("tm.tmkoo.apiPassword") + "&keyword=" + searchKey + "&pageSize="
				+ Global.getConfig("tm.tmkoo.pageSize") + "&searchType=" + searchType + "&intCls=" + intCls;
		System.out.println("param:" + param);
		String jsonString = HttpUtils.doGet(Global.getConfig("tm.tmkoo.host") + "search.php", param, "UTF-8", false);

		JSONObject json = new JSONObject(jsonString);
		String ret = json.get("ret").toString();
		String msg = json.get("msg").toString();
		String remainCount = json.get("remainCount").toString();

		JSONArray jsonArray = json.getJSONArray("results");
		System.out.println("jsonArray:" + jsonArray);
		List<TmInfo> list = new ArrayList<TmInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tm = (JSONObject) jsonArray.get(i);
			TmInfo vo = new TmInfo();
			vo.setRegNo(tm.get("regNo").toString());
			vo.setIntCls(tm.get("intCls").toString());
			// 保存图片到本地
			String descFileName = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal
					+ "/images/tm/similar/";

			String tmImg = Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + principal
					+ "/images/tm/similar/" + tm.get("regNo").toString() + ".jpg";
			FileUtils.createDirectory(FileUtils.path(descFileName));
			FileUtils.copyUrlImg(Global.getConfig("tm.tmkoo.imgUrl") + tm.get("tmImg").toString(),
					descFileName + tm.get("regNo").toString() + ".jpg");

			vo.setTmImg(tmImg);
			vo.setTmName(tm.get("tmName").toString());
			vo.setApplicantCn(tm.get("applicantCn").toString());
			vo.setAppDate(tm.get("appDate").toString());
			vo.setCurrentStatus(tm.get("currentStatus").toString());
			vo.setAnnouncementIssue(tm.get("announcementIssue").toString());
			vo.setAnnouncementDate(tm.get("announcementDate").toString());
			vo.setRegIssue(tm.get("regIssue").toString());
			vo.setRegDate(tm.get("regDate").toString());
			vo.setAgent(tm.get("agent").toString());
			list.add(vo);
		}

		rtn.setAllRecords("" + list.size());
		rtn.setMsg(msg);
		rtn.setRemainCount(remainCount);
		rtn.setResults(list);
		rtn.setRet(ret);
		return rtn;
	}

	/**
	 * @param st
	 *            查询类型，1-中文 4-英文 【备注，目前只支持这2种类型】
	 * 
	 * @param sc
	 *            查询条件。是如下编号的逗号分隔，如： 1,2,3,4,5,6,7,8,9,10
	 * @param intCls
	 *            0：全部国际分类，非0：限定在指定类别，类别间用分号分割。如：4;12;34 表示在第4、12、34类内查询
	 * @param searchKey
	 *            关键词，中文未转码
	 * @return
	 * @throws Exception
	 */
	public static TmResult doSimilar(String st, String sc, String intCls, String searchKey) throws Exception {
		Principal principal = (Principal) UserUtils.getPrincipal();

		TmResult rtn = new TmResult();

		String[] cls = intCls.split(";");

		String ret = "";
		String msg = "";
		String remainCount = "";
		List<TmInfo> list = new ArrayList<TmInfo>();
		
		//相似商标图片路径
		String descFileName = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + principal
				+ "/images/tm/similar/";
		FileUtils.createDirectory(FileUtils.path(descFileName));

		for (int i = 0; i < cls.length; i++) {
			String param = "apiKey=" + Global.getConfig("tm.tmkoo.apiKey") + "&apiPassword="
					+ Global.getConfig("tm.tmkoo.apiPassword") + "&searchKey=" + searchKey + "&st=" + st + "&sc=" + sc
					+ "&intCls=" + cls[i] + "&rtnMoreProperty=true";
			System.out.println("param:" + param);
			String jsonString = HttpUtils.doGet(Global.getConfig("tm.tmkoo.host") + "similar.php", param, "UTF-8",
					false);

			JSONObject json = new JSONObject(jsonString);
			ret = json.get("ret").toString();
			msg = json.get("msg").toString();
			remainCount = json.get("remainCount").toString();

			JSONArray jsonArray = json.getJSONArray("results");
			System.out.println("jsonArray:" + jsonArray);
			
			for (int j = 0; j < jsonArray.length(); j++) {
				JSONObject tm = (JSONObject) jsonArray.get(j);
				TmInfo vo = new TmInfo();
				vo.setRegNo(tm.get("regNo").toString());
				vo.setIntCls(tm.get("intCls").toString());
				

				String tmImg = Servlets.getRequest().getContextPath() + Global.USERFILES_BASE_URL + principal
						+ "/images/tm/similar/" + tm.get("regNo").toString() + ".jpg";
				// 保存图片到本地
				FileUtils.copyUrlImg(Global.getConfig("tm.tmkoo.imgUrl") + tm.get("tmImg").toString(),
						descFileName + tm.get("regNo").toString() + ".jpg");

				vo.setTmImg(tmImg);
				vo.setTmName(tm.get("tmName").toString());
				vo.setApplicantCn(tm.get("applicantCn").toString());
				vo.setAppDate(tm.get("appDate").toString());
				vo.setCurrentStatus(tm.get("currentStatus").toString());
				vo.setAnnouncementIssue(tm.get("announcementIssue").toString());
				vo.setAnnouncementDate(tm.get("announcementDate").toString());
				vo.setRegIssue(tm.get("regIssue").toString());
				vo.setRegDate(tm.get("regDate").toString());
				vo.setAgent(tm.get("agent").toString());
				list.add(vo);
			}
		}

		rtn.setAllRecords("" + list.size());
		rtn.setMsg(msg);
		rtn.setRemainCount(remainCount);
		rtn.setResults(list);
		rtn.setRet(ret);
		return rtn;
	}

}
