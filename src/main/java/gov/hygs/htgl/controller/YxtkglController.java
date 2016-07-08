package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.YxtkglService;
import gov.hygs.htgl.utils.JsonUtils;
import gov.hygs.htgl.utils.excel.TkcjTableExcelToList;
import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

@Component
public class YxtkglController {
	@Resource
	YxtkglService yxtkglService;
	/**
	 * 获取预选题库信息
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param){
		yxtkglService.getYxtkInfo(page, param);
	}
	/**
	 * 根据部门id获取部门信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Dept> getDeptInfoByDeptId(String id){
		return yxtkglService.getDeptInfoByDeptId(id);
	}
	/**
	 * 根据用户id获取用户信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<User> getUserInfoByUserId(String id){
		return yxtkglService.getUserInfoByUserId(id);
	}
	/**
	 * 更新预选题库信息
	 * @param list
	 */
	@Transactional
	@DataResolver
	public void updateYxtk(List<Tktm> list){
		yxtkglService.updateYxtk(list);
	}
	/**
	 * 根据部门id获取用户信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<User> getUserByDeptId(String id){
		return yxtkglService.getUserByDeptId(id);
	}
	
	/**
	 * 根据题目来源id获取题目来源信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Tmly> getTmlyInfoByTmlyId(String id){
			return yxtkglService.getTmlyInfoByTmlyId(id);
	}
	
	/**
	 * 根据分类id获取题库分类信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Tkfl> getTkflInfoByflId(String id){
		return yxtkglService.getTkflInfoByflId(id);
	}
	
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	@DataProvider
	public List<Map<String,Object>> getLoginUserInfo(){
		return yxtkglService.getLoginUserInfo();
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
			tkcj = yxtkglService.ImportTkcjTableExcel(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}
		return tkcj.size()>0?JsonUtils.list2json(tkcj):null;
	}
	
}
