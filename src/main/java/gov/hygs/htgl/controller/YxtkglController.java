package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.service.YxtkglService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

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
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param){
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
	 * 根据预选题库id获取预选题库选择项信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Yxtkxzx> getYxtkxzxInfoByYxtkId(String id){
		return yxtkglService.getYxtkxzxInfoByYxtkId(id);
	}
	/**
	 * 根据预选题库id获取预选题库答案信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Yxtkxzx> getDaXzxInfoByYxtkId(String id){
		return yxtkglService.getYxtkdaInfoByYxtkId(id);
	}
	/**
	 * 更新预选题库信息
	 * @param list
	 */
	@Transactional
	@DataResolver
	public void updateYxtk(List<Yxtk> list){
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
	 * 获取判断题选项信息
	 * @return
	 */
	@DataProvider
	public Collection<Yxtkxzx> getToFInfo(){
		return yxtkglService.getToFInfo();
	}
	
	@DataProvider
	public Collection<Tmly> getTmlyInfoByTmlyId(String id){
			return yxtkglService.getTmlyInfoByTmlyId(id);
	}
	
	@DataProvider
	public Collection<Tkfl> getTkflInfoByflId(String id){
		return yxtkglService.getTkflInfoByflId(id);
	}
	
	@Expose
	public String countGxjl(Record record){
		return yxtkglService.countGxjl(record);
	}
}
