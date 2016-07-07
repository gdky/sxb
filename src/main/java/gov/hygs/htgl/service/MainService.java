package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Menu;

import java.util.List;
import java.util.Map;

public interface MainService {

	List<Menu> getMenuByUser(String username);

	List<Menu> getUserMenu(Map<String, Object> para);

	List<Menu> getChildMenus(int id);

	String UpdatePassword(Map<String, Object> para);

}
