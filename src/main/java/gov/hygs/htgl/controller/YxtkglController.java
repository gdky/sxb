package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.YxtkglService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;

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
	 * 统计贡献记录
	 * @param param
	 * @return
	 */
	@DataProvider
	public List countGxjl(Map<String,Object> param){
		if(param == null){
			return null;
		}
		return yxtkglService.countGxjl(param);
	}
	
	@DataProvider
	public List countDeptGxjl(Map<String,Object> param){
		return yxtkglService.countDeptGxjl(param);
	}
	
	/**
	 * 获取当前登录用户信息
	 * @return
	 */
	@DataProvider
	public List<Map<String,Object>> getLoginUserInfo(){
		return yxtkglService.getLoginUserInfo();
	}
	
}
