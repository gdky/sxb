package gov.hygs.htgl.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import gov.hygs.htgl.dao.YhJsglDao;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.YhJsglService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@Service
public class YhJsglServiceImpl implements YhJsglService {

	@Resource
	private YhJsglDao yhglDao;

	@Override
	public void getUserInfo(Map<String, Object> para, Page page) {
		// TODO Auto-generated method stub
		yhglDao.getUserInfo(para, page);
	}

	@Override
	public void saveUserInfo(List<User> users) {
		// TODO Auto-generated method stub
		for (User user : users) {
			if (EntityUtils.getState(user).equals(EntityState.NEW)) {
				user.setId_(yhglDao.insertUser(user));
				int roleId = yhglDao.getRoleIdByRoleName("USER");
				yhglDao.insertUserByRole(user,roleId);
			}
			if (EntityUtils.getState(user).equals(EntityState.MODIFIED)) {
				yhglDao.updateUser(user);
			}
			if (EntityUtils.getState(user).equals(EntityState.DELETED)) {
				yhglDao.deleteUser(user);
			}
		}
	}

	
	@Override
	public void saveUserPwd(List<User> users) {
		// TODO Auto-generated method stub
		for (User user : users) {
			
			if (EntityUtils.getState(user).equals(EntityState.MODIFIED)) {
				yhglDao.updateUserPwd(user);
			}
			
		}
	}
	
	
	@Override
	public void getRoleInfo(Page page) {
		// TODO Auto-generated method stub
		yhglDao.getRoleInfo(page);
	}

	@Override
	public void getUserInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		yhglDao.getUserInfoByRole(page, id_);
	}

	@Override
	public void getMenuInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		yhglDao.getMenuInfoByRole(page, id_);
	}

	@Override
	public void saveRole(List<Role> roles) {
		// TODO Auto-generated method stub
		for (Role role : roles) {
			if (EntityUtils.getState(role).equals(EntityState.NEW)) {
				yhglDao.insertRole(role);
			}
			if (EntityUtils.getState(role).equals(EntityState.MODIFIED)) {
				yhglDao.updateRole(role);
			}
			if (EntityUtils.getState(role).equals(EntityState.DELETED)) {
				yhglDao.deleteRole(role);
			}
			List<User> users = (List<User>) role.getUsers();
			List<Menu> menus = (List<Menu>) role.getMenus();
			if (users != null) {
				for (User user : users) {
					int roleId = role.getId_();
					if (EntityUtils.getState(user).equals(EntityState.NEW)) {
						yhglDao.insertUserByRole(user, roleId);
					}
					if (EntityUtils.getState(user).equals(EntityState.MODIFIED)) {
						// yhglDao.updateUserByRole(user,roleId);
					}
					if (EntityUtils.getState(user).equals(EntityState.DELETED)) {
						yhglDao.deleteUserByRole(user, roleId);
					}
				}
			}
			if (menus != null) {
				for (Menu menu : menus) {
					int roleId = role.getId_();
					if (EntityUtils.getState(menu).equals(EntityState.NEW)) {
						yhglDao.insertMenuByRole(menu, roleId);
					}
					if (EntityUtils.getState(menu).equals(EntityState.MODIFIED)) {
						// yhglDao.updateMenuByRole(menu,roleId);
					}
					if (EntityUtils.getState(menu).equals(EntityState.DELETED)) {
						yhglDao.deleteMenuByRole(menu, roleId);
					}
				}
			}

		}
	}

	@Override
	public void getMenuInfo(Page page) {
		// TODO Auto-generated method stub
		yhglDao.getMenuInfo(page);
	}

	@Override
	public List<Map<String, Object>> getMenuRoot(int id_) {
		// TODO Auto-generated method stub
		return yhglDao.getMenuRoot(id_);
	}

	@Override
	public List<Map<String, Object>> getCurrentMenuById(int id_, int role_id) {
		// TODO Auto-generated method stub
		return yhglDao.getCurrentMenuById(id_, role_id);
	}

	@Override
	public Boolean saveRoleMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		return yhglDao.saveRoleMenu(role_id, menu_id);
	}

	@Override
	public Boolean validateMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		return yhglDao.validateMenu(role_id, menu_id);
	}

	@Override
	public Boolean deleteMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		return yhglDao.deleteMenu(role_id, menu_id);
	}

	@Override
	public String checkLoginName(String param) {
		// TODO Auto-generated method stub
		return yhglDao.checkLoginName(param);
	}

	@Override
	public String checkUserName(String param) {
		// TODO Auto-generated method stub
		return yhglDao.checkUserName(param);
	}

	@Override
	public String checkRoleName(String param) {
		// TODO Auto-generated method stub
		return yhglDao.checkRoleName(param);
	}

	@Override
	public String getCurrentUserName() {
		// TODO Auto-generated method stub
		return yhglDao.getCurrentUserName();
	}

	@Override
	public Map<String, Object> getCurrentUserInfo() {
		// TODO Auto-generated method stub
		return yhglDao.getCurrentUserInfo();
	}

}
