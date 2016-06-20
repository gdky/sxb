package gov.hygs.htgl.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

import gov.hygs.htgl.dao.YxzskDao;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.YxzskService;

@Service
public class YxzskServiceImpl implements YxzskService {
	@Resource
	YxzskDao yxzskDao;

	@Override
	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		yxzskDao.getYxzskInfo(page, param);
	}

	@Override
	public List<Zskly> getZsklyInfoByZsklyId(Integer id) {
		// TODO Auto-generated method stub
		return yxzskDao.getZsklyInfoByZsklyId(id);
	}

	@Override
	public void updateYxzsk(List<Yxzsk> yxzsks) {
		// TODO Auto-generated method stub
		if(yxzsks != null){
			for(Yxzsk yxzsk : yxzsks){
				if(EntityUtils.getState(yxzsk).equals(EntityState.NEW)){
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					yxzsk.setId(getUUID());
					yxzsk.setUserId(userDetails.getId());
					yxzsk.setDeptid(userDetails.getDeptid());
					//yxzskDao.addYxzsk(yxzsk);
					//yxzskDao.addGrDeptGxJl(yxzsk);
				}if(EntityUtils.getState(yxzsk).equals(EntityState.MODIFIED)){
					//yxzskDao.updateYxzsk(yxzsk);
				}if(EntityUtils.getState(yxzsk).equals(EntityState.DELETED)){
					//yxzskDao.deleteYxzsk(yxzsk);
					//yxzskDao.deleteGrDeptGxJl(yxzsk);
				}
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}
	
}
