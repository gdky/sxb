package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MainDao {

	List<Role> getUserRoles(String username);

	Collection<? extends Menu> getRoleMenus(Integer id_);

	List<Menu> getUserMenu(Map<String, Object> para);

	List<Menu> getChildMenus(int id);

	User getUser(String username);

	

}
