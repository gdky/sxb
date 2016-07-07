package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.QzBmCdglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Grouptable;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.SystemProps;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.QzBmCdglService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

@Service
public class QzBmCdglServiceImpl implements QzBmCdglService {
	@Resource
	QzBmCdglDao qzBmCdglDao;

	@Override
	public void deleteDeptNodeInfo(String id) {
		// TODO Auto-generated method stub
		if (id != null) {
			qzBmCdglDao.deleteDeptNodeInfo(id);
			List<Dept> depts = qzBmCdglDao.getCurrentDeptById(id);
			for (Dept dept : depts) {
				deleteDeptNodeInfo(dept.getId_().toString());
			}
		}
	}

	@Override
	public void saveDeptNodeInfo(List<Dept> depts) {
		// TODO Auto-generated method stub
		for (Dept dept : depts) {
			if (EntityUtils.getState(dept).equals(EntityState.NEW)) {
				qzBmCdglDao.saveDeptNodeInfo(dept);
			}
			/*
			 * if (EntityUtils.getState(dept).equals(EntityState.MODIFIED)) {
			 * qzBmCdglDao.updateDeptNodeInfo(dept); }
			 */
		}
	}

	@Override
	public List<Dept> getDeptRoot() {
		// TODO Auto-generated method stub
		return qzBmCdglDao.getDeptRoot();
	}

	@Override
	public List<Dept> getCurrentDeptById(String id_) {
		// TODO Auto-generated method stub
		return qzBmCdglDao.getCurrentDeptById(id_);
	}

	@Override
	public void getCurrentDeptPageById(Page<Dept> page, String id_) {
		// TODO Auto-generated method stub
		qzBmCdglDao.getCurrentDeptPageById(page, id_);
	}

	@Override
	public void updataNodeInfo(Record record) {
		// TODO Auto-generated method stub
		if (record != null) {
			Dept dept = new Dept();
			Object obj = record.get("depts");
			if (obj instanceof Record) {
				record = (Record) obj;
				dept.setId_((Integer) record.get("id_"));
				dept.setDept_name((String) record.get("dept_name"));
				dept.setParentId((Integer) record.get("parentId"));
				dept.setMs((String) record.get("ms"));
				qzBmCdglDao.updateDeptNodeInfo(dept);
			} else if (obj instanceof List) {
				List list = (List) obj;
				for (int i = 0; i < list.size(); i++) {
					record = (Record) list.get(i);
					dept.setId_((Integer) record.get("id_"));
					dept.setDept_name((String) record.get("dept_name"));
					dept.setParentId((Integer) record.get("parentId"));
					dept.setMs((String) record.get("ms"));
					qzBmCdglDao.updateDeptNodeInfo(dept);
				}
			}
		}
	}

	@Override
	public String checkDeptName(String param) {
		// TODO Auto-generated method stub
		return qzBmCdglDao.checkDeptName(param);
	}

	@Override
	public Collection<Grouptable> getGrouptableInfo() {
		// TODO Auto-generated method stub
		return qzBmCdglDao.getGrouptableInfo();
	}

	@Override
	public Collection<User> getUserByGroupInfo(Record record) {
		// TODO Auto-generated method stub
		if (record.get("id") != null) {
			return qzBmCdglDao.getUserByGroupId(record.get("id"));
		}
		return null;
	}

	@Override
	public void getUserInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		qzBmCdglDao.getUserInfo(page, param);
	}

	@Override
	public void updateUserInfo(List<User> users) {
		// TODO Auto-generated method stub
		for (User user : users) {
			if (EntityUtils.getState(user).equals(EntityState.NEW)) {
				qzBmCdglDao.addUser(user);
			}
			if (EntityUtils.getState(user).equals(EntityState.MODIFIED)) {
				qzBmCdglDao.updataUser(user);
			}
			if (EntityUtils.getState(user).equals(EntityState.DELETED)) {
				qzBmCdglDao.deleteUser(user);
			}
		}
	}

	@Override
	public void updateGroupInfo(List<Grouptable> groups) {
		// TODO Auto-generated method stub
		for (Grouptable group : groups) {
			if (EntityUtils.getState(group).equals(EntityState.NEW)) {
				qzBmCdglDao.addGroup(group);
			}
			if (EntityUtils.getState(group).equals(EntityState.MODIFIED)) {
				qzBmCdglDao.updataGroup(group);
			}
			if (EntityUtils.getState(group).equals(EntityState.DELETED)) {
				qzBmCdglDao.deleteGroup(group);
			}
		}

	}

	@Override
	public String checkGroupName(String param) {
		// TODO Auto-generated method stub
		return qzBmCdglDao.checkGroupName(param);
	}

	@Override
	public Collection<Menu> getMenuRoot() {
		// TODO Auto-generated method stub
		return qzBmCdglDao.getMenuRoot();
	}

	@Override
	public Collection<Menu> getCurrentMenuById(String id_) {
		// TODO Auto-generated method stub
		return qzBmCdglDao.getCurrentMenuById(id_);
	}

	@Override
	public void getCurrentMenuPageById(Page<Menu> page, String id_) {
		// TODO Auto-generated method stub
		qzBmCdglDao.getCurrentMenuPageById(page, id_);
	}

	@Override
	public void saveMenuNodeInfo(List<Menu> menus) {
		// TODO Auto-generated method stub
		for (Menu menu : menus) {
			if (EntityUtils.getState(menu).equals(EntityState.NEW)) {
				qzBmCdglDao.saveMenuNodeInfo(menu);
			}/*
			 * if (EntityUtils.getState(menu).equals(EntityState.MODIFIED)) {
			 * qzBmCdglDao.updateMenuNodeInfo(menu); }
			 */
		}
	}

	@Override
	public void deleteMenuNodeInfo(String id) {
		// TODO Auto-generated method stub
		if (id != null) {
			qzBmCdglDao.deleteMenuNodeInfo(id);
			List<Menu> menus = (List<Menu>) qzBmCdglDao.getCurrentMenuById(id);
			for (Menu menu : menus) {
				deleteMenuNodeInfo(menu.getId_().toString());
			}
		}
	}

	@Override
	public void updateNodeInfo(Record record) {
		// TODO Auto-generated method stub
		if (record != null) {
			Menu menu = new Menu();
			Object obj = record.get("menus");
			if (obj instanceof Record) {
				record = (Record) obj;
				menu.setId_((Integer) record.get("id_"));
				menu.setMenu_Name((String) record.get("menu_Name"));
				menu.setParent_Id((Integer) record.get("parent_id"));
				menu.setUrl((String) record.get("url"));
				menu.setYxbz((String) record.get("yxbz"));
				qzBmCdglDao.updateMenuNodeInfo(menu);
			} else if (obj instanceof List) {
				List list = (List) obj;
				for (int i = 0; i < list.size(); i++) {
					record = (Record) list.get(i);
					menu.setId_((Integer) record.get("id_"));
					menu.setMenu_Name((String) record.get("menu_name"));
					menu.setParent_Id((Integer) record.get("parent_id"));
					menu.setUrl((String) record.get("url"));
					menu.setYxbz((String) record.get("yxbz"));
					qzBmCdglDao.updateMenuNodeInfo(menu);
				}
			}
		}
	}

	@Override
	public String checkMenuName(String param) {
		// TODO Auto-generated method stub
		return qzBmCdglDao.checkMenuName(param);
	}

	@Override
	public void getSystemPropsInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		qzBmCdglDao.getSystemPropsInfo(page, param);
	}

	@Override
	public void updateSystemPropsInfo(List<SystemProps> list) {
		// TODO Auto-generated method stub
		for(SystemProps sp : list){
			if (EntityUtils.getState(sp).equals(EntityState.NEW)) {
				qzBmCdglDao.addSystemProps(sp);
			}
			if(EntityUtils.getState(sp).equals(EntityState.MODIFIED)){
				qzBmCdglDao.updateSystemProps(sp);
			}
			if(EntityUtils.getState(sp).equals(EntityState.DELETED)){
				qzBmCdglDao.deleteSystemProps(sp);
			}
		}
	}

}
