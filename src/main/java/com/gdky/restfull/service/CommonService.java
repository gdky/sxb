package com.gdky.restfull.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bstek.dorado.annotation.Expose;
import com.gdky.restfull.dao.BaseJdbcDao;

@Service
public class CommonService extends BaseJdbcDao{
 
 

	@Expose
	public String getTest(){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		String sql ="select * from user ";
		List<Map<String,Object>> ls =this.jdbcTemplate.queryForList(sql);
		
		return ls.get(0).toString();
	}

	 
	

}
