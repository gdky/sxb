package gov.hygs.htgl.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import gov.hygs.htgl.dao.ZszskDao;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zszsk;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.ZszskService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@Service
public class ZszskServiceImpl implements ZszskService {
	@Resource
	ZszskDao zszskDao;

	@Override
	public void getZszskInfo(Page<Zszsk> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zszskDao.getZszskInfo(page, param, userDetails);
	}

	@Override
	public void updateZszsk(List<Zszsk> list) {
		// TODO Auto-generated method stub
		for (Zszsk zszsk : list) {
			if (EntityUtils.getState(zszsk).equals(EntityState.NEW)) {
				if (zszsk.getId() == null) {
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					zszsk.setUserId(userDetails.getId());
					zszsk.setDeptid(userDetails.getDeptid());
					zszsk.setId(getUUID());
				} else {
					zszskDao.addYxzskToZszsk(zszsk);
				}
				zszskDao.addZszsk(zszsk);
				zszsk.setContent(getUUID());
				zszskDao.addGrDeptGxJl(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.MODIFIED)) {
				zszskDao.updateZszsk(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.DELETED)) {
				zszskDao.deleteYxzskFormZszsk(zszsk);
				zszskDao.deleteZszsk(zszsk);
				zszskDao.deleteGrDeptGxJl(zszsk);
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zszskDao.getYxzskInfo(page, param);
	}

}
