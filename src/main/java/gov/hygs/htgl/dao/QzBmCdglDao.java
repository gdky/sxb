package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Grouptable;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.SystemProps;
import gov.hygs.htgl.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface QzBmCdglDao {

	public List<Dept> getDeptRoot();

	public List<Dept> getCurrentDeptById(String id_);

	public void getCurrentDeptPageById(Page<Dept> page, String id_);

	public void saveDeptNodeInfo(Dept dept);

	public void deleteDeptNodeInfo(String id);

	public void updateDeptNodeInfo(Dept dept);

	public String checkDeptName(String param);
	
	public Collection<Grouptable> getGrouptableInfo();

	public Collection<User> getUserByGroupId(Object object);

	public void addGroup(Grouptable group);

	public void updataGroup(Grouptable group);

	public void deleteGroup(Grouptable group);

	public void addUser(User user);

	public void updataUser(User user);

	public void deleteUser(User user);

	public String checkGroupName(String param);
	
	public Collection<Menu> getMenuRoot();

	public Collection<Menu> getCurrentMenuById(String id);

	public void getCurrentMenuPageById(Page<Menu> page, String id_);

	public String saveMenuNodeInfo(Menu menu);

	public String updateMenuNodeInfo(Menu menu);

	public void deleteMenuNodeInfo(String id);

	public String checkMenuName(String param);

	public void getUserInfo(Page page, Map<String, Object> param);

	public void getSystemPropsInfo(Page page, Map<String, Object> param);

	public void addSystemProps(SystemProps sp);

	public void updateSystemProps(SystemProps sp);

	public void deleteSystemProps(SystemProps sp);

	public void addUserInfoToGroup(Map<String, Object> param);

	public Collection<Grouptable> getCurrentGroupById(String id);

	public Collection<Grouptable> getflushGroupById(String id);

}
