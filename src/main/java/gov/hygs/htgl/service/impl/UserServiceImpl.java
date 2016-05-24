package gov.hygs.htgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import gov.hygs.htgl.dao.MainDao;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Resource
	MainDao mainDao;
	
	@Override
	public List<Role> getRolesByUser(String username) {
		// TODO Auto-generated method stub
		return mainDao.getUserRoles(username);
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return mainDao.getUser(username);
	}

}
