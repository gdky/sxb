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

}
