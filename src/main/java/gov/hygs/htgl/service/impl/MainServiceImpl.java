package gov.hygs.htgl.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import gov.hygs.htgl.dao.MainDao;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.service.MainService;
@Service
public class MainServiceImpl implements MainService {

	@Resource
	MainDao mainDao;
	
	@Override
	public List<Menu> getMenuByUser(String username) {
		// TODO Auto-generated method stub
		
		return getParentMenus(username);
	}

	public List<Menu> getParentMenus(String username) {
		List<Role> userRoles =mainDao.getUserRoles(username);
		List<Menu> allMenus = new ArrayList<Menu>();
		for(Role role:userRoles){
			allMenus.addAll(mainDao.getRoleMenus(role.getId_()));			
		}
		
		for(Menu menu:allMenus){
			if(menu.getParent_Id()!=0){
				for(Menu addmenu:allMenus){
					if(addmenu.getId_()==menu.getParent_Id()){
						List<Menu> childMenu = addmenu.getMenus();
						childMenu.add(menu);
						addmenu.setMenus(childMenu);
					}					
				}
			}
			
		}
		List<Menu> menus =  new ArrayList<Menu>();
		for(Menu menu:allMenus){
			if(menu.getParent_Id()==0){
				menus.add(menu);
			}
		}
		return menus;

	}

	@Override
	public List<Menu> getUserMenu(Map<String, Object> para) {
		// TODO Auto-generated method stub
		return mainDao.getUserMenu(para);
	}

	@Override
	public List<Menu> getChildMenus(int id) {
		// TODO Auto-generated method stub
		return mainDao.getChildMenus(id);
	}

	@Override
	public String UpdatePassword(Map<String, Object> para) {
		// TODO Auto-generated method stub
		return mainDao.UpdatePassword(para);
	}

	
}
