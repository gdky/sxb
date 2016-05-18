package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YhJsglDao {

	void getUserInfo(Map<String, Object> para, Page page);

	void insertUser(User user);

	void updateUser(User user);

	void deleteUser(User user);

	void getRoleInfo(Page page);

	void getUserInfoByRole(Page page, int id_);

	void getMenuInfoByRole(Page page, int id_);

	void insertRole(Role role);
	
	void updateRole(Role role);

	void deleteRole(Role role);
	
	void insertUserByRole(User user, int roleId);

	void updateUserByRole(User user, int roleId);

	void deleteUserByRole(User user, int roleId);

	void insertMenuByRole(Menu menu, int roleId);

	void updateMenuByRole(Menu menu, int roleId);

	void deleteMenuByRole(Menu menu, int roleId);

	void getMenuInfo(Page page);

}
