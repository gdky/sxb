package gov.hygs.htgl.tkgl.controller;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.tkgl.service.TkglService;
import gov.hygs.htgl.utils.JsonUtils;
import gov.hygs.htgl.utils.excel.TkcjTableExcelToList;
import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

@Component
public class TkglController {
	@Resource
	TkglService tkglService;

	/**
	 * 获取预选题库信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		tkglService.getYxtkInfo(page, param,"N");
	}
	/**
	 * 获取正式题库信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param) {
		tkglService.getYxtkInfo(page, param,"Y");
	}
	/**
	 * 获取正式题库信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getLstkInfo(Page<Tktm> page, Map<String, Object> param) {
		tkglService.getYxtkInfo(page, param,"L");
	}
	/**
	 * 获取用户最高权限及其部门
	 * 
	 * @return
	 */
	@DataProvider
	public Map<String, Object> getRolePower() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		String roleName = userDetails.getRolePower();
		Integer deptid = userDetails.getDeptid();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rolename", roleName);
		map.put("deptid", deptid);
		return map;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<User> getUser(String yhm) {
		return tkglService.getUser(yhm);
	}

	/**
	 * 更新预选题库信息
	 * 
	 * @param list
	 */
	@Transactional
	@DataResolver
	public void updateYxtk(List<Tktm> list) {
		// 题目
		for (Tktm yxtk : list) {
			if (EntityUtils.getState(yxtk).equals(EntityState.NEW)) {
				yxtk.setId(tkglService.getUUID());
				tkglService.insertYxtk(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.MODIFIED)) {
				tkglService.updateYxtk(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.DELETED)) {
				tkglService.deleteYxtk(yxtk);
			}
			// 选择项
			List<Tkxzx> xzs = (List<Tkxzx>) yxtk.getTkxzx();
			if (xzs != null) {
				for (Tkxzx xz : xzs) {
					if (EntityUtils.getState(xz).equals(EntityState.NEW)) {
						xz.setId(tkglService.getUUID());
						xz.setTkId(yxtk.getId());
						tkglService.insertYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.MODIFIED)) {
						tkglService.updateYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.DELETED)) {
						tkglService.deleteYxtkxzx(xz);
					}
				}
			}
			// 答案项
			List<Tkxzx> das = (List<Tkxzx>) yxtk.getDaxzx();
			if (das != null) {
				for (Tkxzx da : das) {
					if (EntityUtils.getState(da).equals(EntityState.NEW)) {
						tkglService.insertYxtkda(da, xzs);
					}
					if (EntityUtils.getState(da).equals(EntityState.MODIFIED)) {
						tkglService.updateYxtkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.DELETED)) {
						tkglService.deleteYxtkda(da);
					}
				}
			}
		}
	}
	
	@Expose
	public void yxtkToZstk(List<Tktm> tms,String lx){
		tkglService.yxtkToZstk(tms,lx);
	}
	
	@DataProvider
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id) {
		return tkglService.getTkzxzInfoByZstkId(id);
	}
	
	@DataProvider
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id) {
		return tkglService.getDaXzxInfoByZstkId(id);
	}

	@DataProvider
	public Collection<Tkxzx> getToFInfo() {
		return tkglService.getToFInfo();
	}
	
	/**
	 * 导入题库采集表excel
	 * @param file
	 * @param param
	 * @return
	 */
	@Transactional
	@FileResolver
	public String ImportTkcjTableExcel(UploadFile file, Map<String, Object> param){
		List<TkcjTable> tkcj = new ArrayList<TkcjTable>();
		try {
			List<Map<String,Object>> list = TkcjTableExcelToList.explainExcel(file, param);
			tkcj = tkglService.ImportTkcjTableExcel(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		return (tkcj != null && tkcj.size()>0)?JsonUtils.list2json(tkcj):null;
	}
	
}
