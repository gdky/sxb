package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.YhJsglService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

@Component
public class YhJsglController {
	
	@Resource
	private YhJsglService yhglService;
	
	@DataProvider
	public String getCurrentUserName(){
		return yhglService.getCurrentUserName();
	}
	@DataProvider
	public void getUserInfo(Page page,Map<String,Object> para){
		 yhglService.getUserInfo(para,page);
		
	}
	
	@DataProvider
	public Map<String,Object> getCurrentUserInfo(){
		return yhglService.getCurrentUserInfo();
	}
	
	
	@DataResolver
	public void saveUserInfo(List<User> users){
		yhglService.saveUserInfo(users);
	}
	
	@DataResolver
	public void saveUserPwd(List<User> users){
		yhglService.saveUserPwd(users);
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
	
	@DataProvider
	public List<Map<String,Object>> getMenuRoot(int id_){
		return yhglService.getMenuRoot(id_);
	}
	
	@DataProvider
	public List<Map<String,Object>> getCurrentMenuById(int id_,int role_id){
		return yhglService.getCurrentMenuById(id_,role_id);
	}
	
	@Expose
	public Boolean saveRoleMenu(int role_id,int menu_id){
		return yhglService.saveRoleMenu(role_id,menu_id);
	}
	
	@Expose 
	public Boolean validateMenu(int role_id,int menu_id){
		return yhglService.validateMenu(role_id,menu_id);	
	}
	@Expose
	public Boolean deleteMenu(int role_id,int menu_id){
		 return yhglService.deleteMenu(role_id,menu_id);
	}
	
	@Expose
	public String checkLoginName(String param){
		return yhglService.checkLoginName(param);
	}
	
	@Expose
	public String checkUserName(String param){
		return yhglService.checkUserName(param);
	}
	
	@Expose
	public String checkRoleName(String param){
		return yhglService.checkRoleName(param);
	}
	
	@FileResolver
	public String importImage(UploadFile file, Map<String, Object> para) throws IOException{
		return yhglService.importImage(file,para);
	}
}
