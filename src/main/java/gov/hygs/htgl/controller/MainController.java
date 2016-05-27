package gov.hygs.htgl.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.MainService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.common.event.DefaultClientEvent;
import com.bstek.dorado.view.widget.base.accordion.Accordion;
import com.bstek.dorado.view.widget.base.accordion.Section;
import com.bstek.dorado.view.widget.tree.Tree;

@Component
public class MainController {

	@Resource
	MainService mainService;
	

	
	public void init(Accordion control) throws Exception{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		String username = userDetails.getUsername();
		List<Menu> menus =mainService.getMenuByUser(username);
			
		
		for(Menu menu:menus){
			Section section = new Section();
			
			Tree tree = new Tree();
			tree.setTags(String.valueOf(menu.getId_()));
			tree.setId("treeMk"+menu.getId_());
			tree.addClientEventListener("onRenderNode", new DefaultClientEvent("view.$renderNode(self,arg);"));

			section.setCaption(menu.getMenu_Name());
			section.setControl(tree);
			control.addSection(section);
		}
		
	}
	
	@DataProvider
	public List<Menu> getUserMenu(Map<String,Object> para){
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		int username = userDetails.getId();
		if(para==null){
			para= new HashMap<String,Object>();
		}
		para.put("user_id", username);
		return mainService.getUserMenu(para);
		
	}
	
	@DataProvider
	public List<Menu> getChildMenus(int id){
		return mainService.getChildMenus(id);
	}
	
}
