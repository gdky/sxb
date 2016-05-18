package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.YhJsglService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;

@Component
public class YhJsglController {
	
	@Resource
	private YhJsglService yhglService;
	
	@DataProvider
	public void getUserInfo(Page page,Map<String,Object> para){
		 yhglService.getUserInfo(para,page);
		
	}
	
	@DataResolver
	public void saveUserInfo(List<User> users){
		yhglService.saveUserInfo(users);
	}
	
	@DataProvider
	public void getRoleInfo(Page page){
		yhglService.getRoleInfo(page);
	}
	
	@DataProvider
	public void getUserInfoByRole(Page page,int id_){
		yhglService.getUserInfoByRole(page,id_);
	}
	
	@DataProvider
	public void  getMenuInfoByRole(Page page,int id_){
		yhglService.getMenuInfoByRole(page,id_);
	}
	
	@DataResolver
	public void saveRole(List<Role> roles){
		yhglService.saveRole(roles);
	}
	@DataProvider
	public void getMenuInfo(Page page){
		yhglService.getMenuInfo(page);
	}
}
