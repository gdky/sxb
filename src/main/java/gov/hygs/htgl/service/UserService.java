package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.User;

import java.util.List;

import gov.hygs.htgl.entity.Role;

public interface UserService {

	List<Role> getRolesByUser(String username);

	User getUser(String username);

}
