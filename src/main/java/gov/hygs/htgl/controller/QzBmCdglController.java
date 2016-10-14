package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Grouptable;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.SystemProps;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.QzBmCdglService;

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
public class QzBmCdglController {
	@Resource
	QzBmCdglService qzBmCdglService;

	/**
	 * 获取dept的根节点
	 * @return
	 */
	@DataProvider
	public List<Dept> getDeptRoot(Map<String,Object> param) {
		return qzBmCdglService.getDeptRoot(param);
	}

	/**
	 * 获取dept树当前节点信息
	 * @param id_ 当前dept节点的id
	 * @return
	 */
	@DataProvider
	public List<Dept> getCurrentDeptById(String id_) {
		return qzBmCdglService.getCurrentDeptById(id_);
	}

	/**
	 * 获取当前节点后，用分页显示
	 * @param page
	 * @param id_
	 */
	@DataProvider
	public void getCurrentDeptPageById(Page<Dept> page, String id_) {
		if (id_ != null) {
			qzBmCdglService.getCurrentDeptPageById(page, id_);
		}
	}

	/**
	 * 保存同级或子级节点信息
	 * @param depts
	 */
	@DataResolver
	@Transactional
	public void saveDeptNodeInfo(List<Dept> depts) {
		// bmglDao.saveDeptNodeInfo(depts);
		qzBmCdglService.saveDeptNodeInfo(depts);
	}

	/**
	 * 删除该节点和该节点下所有节点
	 * @param id
	 */
	@Expose
	@Transactional
	public void deleteDeptNodeInfo(String id) {
		qzBmCdglService.deleteDeptNodeInfo(id);
	}

	/**
	 * 保存修改后的节点信息
	 * @param record 接收修改后的数据
	 */
	@DataResolver
	@Transactional
	public void updataNodeInfo(Record record) {
		qzBmCdglService.updataNodeInfo(record);
	}

	/**
	 * 用于校验部门字段是否重名
	 * @param param
	 * @return
	 */
	@Expose
	public String checkDeptName(String param) {
		return qzBmCdglService.checkDeptName(param);
	}

	/**
	 * 获取menu的根节点
	 * @return
	 */
	@DataProvider
	public Collection<Menu> getMenuRoot() {
		return qzBmCdglService.getMenuRoot();
	}

	/**
	 * 获取menu树当前节点信息
	 * @param id_
	 * @return
	 */
	@DataProvider
	public Collection<Menu> getCurrentMenuById(String id_) {
		return qzBmCdglService.getCurrentMenuById(id_);
	}

	/**
	 * 获取当前节点后，用分页显示
	 * @param page
	 * @param id_
	 */
	@DataProvider
	public void getCurrentMenuPageById(Page<Menu> page, String id_) {
		if (id_ != null) {
			qzBmCdglService.getCurrentMenuPageById(page, id_);
		}
	}

	/**
	 * 保存同级或子级节点
	 * @param menus
	 */
	@DataResolver
	@Transactional
	public String saveMenuNodeInfo(List<Menu> menus) {
		return qzBmCdglService.saveMenuNodeInfo(menus);
	}

	/**
	 * 删除该节点和该节点下所有节点
	 * @param id
	 */
	@Expose
	@Transactional
	public void deleteMenuNodeInfo(String id) {
		qzBmCdglService.deleteMenuNodeInfo(id);
	}

	/**
	 * 保存修改后的节点信息
	 * @param record 接收修改后的数据
	 */
	@DataResolver
	@Transactional
	public String updateNodeInfo(Record record) {
		return qzBmCdglService.updateNodeInfo(record);
	}

	/**
	 * 用于校验部门字段是否重名
	 * 
	 * @param param
	 * @return
	 */
	@Expose
	public String checkMenuName(String param) {
		return qzBmCdglService.checkMenuName(param);
	}

	/**
	 * 获取群主名称信息
	 * 
	 * @return
	 */
	@DataProvider
	public Collection<Grouptable> getGrouptableInfo() {
		return qzBmCdglService.getGrouptableInfo();
	}
	
	/**
	 * 获取群组当前节点信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Grouptable> getCurrentGroupById(String id){
		return qzBmCdglService.getCurrentGroupById(id);
	}
	
	/**
	 * 获取节点刷新信息
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<Grouptable> getflushGroupById(String id){
		return qzBmCdglService.getflushGroupById(id);
	}
	
	/**
	 * 获取所有用户信息
	 * 
	 * @return
	 */
	/*
	 * @DataProvider public Collection<User> getUserInfo(){ return
	 * qzBmCdglService.getAllUserInfo(); }
	 */
	@DataProvider
	public void getUserInfo(Page page, Map<String, Object> param) {
		qzBmCdglService.getUserInfo(page, param);
	}

	/**
	 * 通过群信息获取相关的用户信息
	 * 
	 * @param record
	 *            群信息
	 * @return
	 */
	@DataProvider
	public Collection<User> getUserByGroupInfo(Record record) {
		return qzBmCdglService.getUserByGroupInfo(record);
	}

	/**
	 * 更新群信息
	 * 
	 * @param groups
	 */
	@DataResolver
	@Transactional
	public void updateGroup(List<Grouptable> groups){
		qzBmCdglService.updateGroup(groups);
	}
	
	/**
	 * 添加用户信息到群组中
	 * @param param
	 */
	@Transactional
	@Expose
	public void addUserInfoToGroup(Map<String,Object> param){
		qzBmCdglService.addUserInfoToGroup(param);
	}

	/**
	 * 更新用户信息
	 * 
	 * @param users
	 */
	@DataResolver
	@Transactional
	public void updateUserInfo(List<User> users) {
		qzBmCdglService.updateUserInfo(users);
	}

	/**
	 * 用于校验群字段是否重名
	 * 
	 * @param param
	 * @return
	 */
	@Expose
	public String checkGroupName(String param) {
		return qzBmCdglService.checkGroupName(param);
	}

	/**
	 * 获取系统参数表信息
	 * 
	 * @return
	 */
	@DataProvider
	public void getSystemPropsInfo(Page page, Map<String, Object> param) {
		qzBmCdglService.getSystemPropsInfo(page, param);
	}
	
	/**
	 * 更新系统参数表信息
	 * @param list
	 */
	@DataResolver
	public void updateSystemPropsInfo(List<SystemProps> list){
		qzBmCdglService.updateSystemPropsInfo(list);
	}
	
	/**
	 * 修改子节点信息
	 * @param record
	 */
	@Transactional
	@Expose
	public String updateChildNode(Record record){
		return qzBmCdglService.updateNodeInfo(record);
	}
	
}
