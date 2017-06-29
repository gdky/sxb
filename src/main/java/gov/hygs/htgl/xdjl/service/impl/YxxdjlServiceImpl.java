package gov.hygs.htgl.xdjl.service.impl;

import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.xdjl.dao.YxxdjlDao;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;
import gov.hygs.htgl.xdjl.service.YxxdjlService;

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
public class YxxdjlServiceImpl implements YxxdjlService {
	@Resource
	YxxdjlDao yxxdjlDao;

	@Override
	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		yxxdjlDao.getYxzskInfo(page, param, userDetails);
	}

	@Override
	public List<Xdjlly> getZsklyInfoByZsklyId(Integer id) {
		// TODO Auto-generated method stub
		return yxxdjlDao.getZsklyInfoByZsklyId(id);
	}

	@Override
	public void updateYxzsk(List<XdjlJl> yxzsks) {
		// TODO Auto-generated method stub
		if (yxzsks != null) {
			for (XdjlJl yxzsk : yxzsks) {
				if (EntityUtils.getState(yxzsk).equals(EntityState.NEW)) {
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					yxzsk.setId(getUUID());
					yxzsk.setUserId(userDetails.getId());
					yxzsk.setDeptid(userDetails.getDeptid());
					yxxdjlDao.addYxzsk(yxzsk);
					yxxdjlDao.addGrDeptGxJl(yxzsk);
				}
				if (EntityUtils.getState(yxzsk).equals(EntityState.MODIFIED)) {
					yxxdjlDao.updateYxzsk(yxzsk);
				}
				if (EntityUtils.getState(yxzsk).equals(EntityState.DELETED)) {
					yxxdjlDao.deleteYxzsk(yxzsk);
					yxxdjlDao.deleteGrDeptGxJl(yxzsk);
				}
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

}
