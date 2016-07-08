package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.ZszskDao;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.ZszskService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

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
	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zszskDao.getZszskInfo(page, param, userDetails);
	}

	@Override
	public void updateZszsk(List<ZskJl> list) {
		// TODO Auto-generated method stub
		for (ZskJl zszsk : list) {
			if (EntityUtils.getState(zszsk).equals(EntityState.NEW)) {
				if (zszsk.getId() == null) {
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					zszsk.setUserId(userDetails.getId());
					zszsk.setDeptid(userDetails.getDeptid());
					zszsk.setId(getUUID());
					zszskDao.addZszsk(zszsk);
				} else {
					//zszskDao.addYxzskToZszsk(zszsk);
					zszskDao.updateZszsk(zszsk);
				}
				zszsk.setContent(getUUID());
				zszskDao.addGrDeptGxJl(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.MODIFIED)) {
				zszskDao.updateZszsk(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.DELETED)) {
				//zszskDao.deleteYxzskFormZszsk(zszsk);
				zszskDao.deleteZszsk(zszsk);
				zszskDao.deleteGrDeptGxJl(zszsk);
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zszskDao.getYxzskInfo(page, param);
	}

	@Override
	public void getRandomdsZszskFilter(Page<ZskJl> page,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		zszskDao.getRandomdsZszskFilter(page, param);
	}

	@Override
	public void updateZsdtsInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zszskDao.updateZsdtsInfo(param, userDetails);
	}

	@Override
	public Collection<Zskly> getZsklyInfo() {
		// TODO Auto-generated method stub
		return zszskDao.getZsklyInfo();
	}

	@Override
	public void updateZskly(List<Zskly> zsklys) {
		// TODO Auto-generated method stub
		for(Zskly zskly : zsklys){
			if(EntityUtils.getState(zskly).equals(EntityState.NEW)){
				zszskDao.addZskly(zskly);
			}
			if(EntityUtils.getState(zskly).equals(EntityState.MODIFIED)){
				zszskDao.updateZskly(zskly);
			}
			if(EntityUtils.getState(zskly).equals(EntityState.DELETED)){
				zszskDao.deleteZskly(zskly);
			}
		}
	}

}
