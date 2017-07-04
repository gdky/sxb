package gov.hygs.htgl.tkgl.service.impl;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.tkgl.dao.TkglDao;
import gov.hygs.htgl.tkgl.service.TkglService;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.provider.Page;

@Service
public class TkglServiceImpl implements TkglService {
	@Resource
	TkglDao tkglDao;


	@Override
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		tkglDao.getYxtkInfo(page, param);
	}
}
