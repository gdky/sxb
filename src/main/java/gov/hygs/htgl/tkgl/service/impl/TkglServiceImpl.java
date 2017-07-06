package gov.hygs.htgl.tkgl.service.impl;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.tkgl.dao.TkglDao;
import gov.hygs.htgl.tkgl.service.TkglService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
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

	@Override
	public Collection<User> getUser(String yhm) {
		// TODO Auto-generated method stub
		return tkglDao.getUser(yhm);
	}

	public String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public void insertYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		yxtk.setUserId(userDetails.getId());
		yxtk.setDeptid(userDetails.getDeptid());
		yxtk.setDrbz("N");
		tkglDao.addYxtk(yxtk);
		tkglDao.addGrDeptGxJl(yxtk);
	}

	@Override
	public void updateYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		tkglDao.updateYxtk(yxtk);
	}

	@Override
	public void deleteYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		tkglDao.deleteYxtk(yxtk);
		tkglDao.deleteGrDeptGxJl(yxtk);
	}

	@Override
	public void insertYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		if (!"0".equals(xz.getTkId())) {
			tkglDao.addYxtkxzx(xz);
		}
	}

	@Override
	public void updateYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		tkglDao.updateYxtkxzx(xz);
	}

	@Override
	public void deleteYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		tkglDao.deleteYxtkxzx(xz);
	}

	@Override
	public void insertYxtkda(Tkxzx da, List<Tkxzx> xzx) {
		// TODO Auto-generated method stub
		if (da.getId() == null) {
			for (Tkxzx xz : xzx) {
				if (da.getXzKey().equals(xz.getXzKey())) {
					da.setId(xz.getId());
					if (da.getTkId() == null) {
						da.setTkId(xz.getTkId());
					}
				}
			}
		}
		da.setContent(getUUID());
		tkglDao.addYxtkda(da);
	}

	@Override
	public void updateYxtkda(Tkxzx da) {
		// TODO Auto-generated method stub
		tkglDao.updateYxtkda(da);
	}

	@Override
	public void deleteYxtkda(Tkxzx da) {
		// TODO Auto-generated method stub
		tkglDao.deleteYxtkda(da);
	}
	
	
}
