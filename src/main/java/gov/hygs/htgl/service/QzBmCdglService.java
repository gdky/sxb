package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Grouptable;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

public interface QzBmCdglService {

	public void deleteDeptNodeInfo(String id);

	public void saveDeptNodeInfo(List<Dept> depts);

	public List<Dept> getDeptRoot();

	public List<Dept> getCurrentDeptById(String id_);

	public void getCurrentDeptPageById(Page<Dept> page, String id_);

	public void updataNodeInfo(Record record);

	public String checkDeptName(String param);
	
	public Collection<Grouptable> getGrouptableInfo();

	public Collection<User> getUserByGroupInfo(Record record);

	public void updateGroupInfo(List<Grouptable> groups);

	public void updateUserInfo(List<User> users);

	public String checkGroupName(String param);
	
	public Collection<Menu> getMenuRoot();

	public Collection<Menu> getCurrentMenuById(String id_);

	public void getCurrentMenuPageById(Page<Menu> page, String id_);

	public void saveMenuNodeInfo(List<Menu> menus);

	public void deleteMenuNodeInfo(String id);

	public void updateNodeInfo(Record record);

	public String checkMenuName(String param);
}
