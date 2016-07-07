package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YhJsglService {

	void getUserInfo(Map<String, Object> para, Page page);

	void saveUserInfo(List<User> users);

	void getRoleInfo(Page page);

	void getUserInfoByRole(Page page, int id_);

	void getMenuInfoByRole(Page page, int id_);

	void saveRole(List<Role> roles);

	void getMenuInfo(Page page);

	List<Map<String, Object>> getMenuRoot(int id_);

	List<Map<String, Object>> getCurrentMenuById(int id_, int role_id);

	Boolean saveRoleMenu(int role_id, int menu_id);

	Boolean validateMenu(int role_id, int menu_id);

	Boolean deleteMenu(int role_id, int menu_id);

	String checkLoginName(String param);

	String checkUserName(String param);

	String checkRoleName(String param);

	String getCurrentUserName();

	void saveUserPwd(List<User> users);

	Map<String, Object> getCurrentUserInfo();

}
