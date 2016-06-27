package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.YxtkglService;

import java.util.ArrayList;
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
public class YxtkglServiceImpl implements YxtkglService {
	@Resource
	YxtkglDao yxtkglDao;

	private List<Tkxzx> yxtkNew = new ArrayList<Tkxzx>();
	
	@Override
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		yxtkglDao.getYxtkInfo(page, param, userDetails);
	}

	@Override
	public Collection<Dept> getDeptInfoByDeptId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getDeptInfoByDeptId(id);
	}

	@Override
	public Collection<User> getUserInfoByUserId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getUserInfoByUserId(id);
	}

	@Override
	public void updateYxtk(List<Tktm> list) {
		// TODO Auto-generated method stub
		for (Tktm yxtk : list) {
			if (EntityUtils.getState(yxtk).equals(EntityState.NEW)) {
				CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
						.getContext().getAuthentication().getPrincipal();
				yxtk.setUserId(userDetails.getId());
				yxtk.setDeptid(userDetails.getDeptid());
				yxtk.setId(getUUID());
				yxtkglDao.addYxtk(yxtk);
				yxtkglDao.addGrDeptGxJl(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.MODIFIED)) {
				yxtkglDao.updateYxtk(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.DELETED)) {
				yxtkglDao.deleteYxtk(yxtk);
				yxtkglDao.deleteGrDeptGxJl(yxtk);
			}

			List<Tkxzx> das = (List<Tkxzx>) yxtk.getDaxzx();
			List<Tkxzx> xzs = (List<Tkxzx>) yxtk.getTkxzx();

			if (xzs != null) {
				for (Tkxzx xz : xzs) {
					if (EntityUtils.getState(xz).equals(EntityState.NEW)) {
						xz.setId(this.getUUID());
						yxtkNew.add(xz);
						yxtkglDao.addYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.MODIFIED)) {
						yxtkglDao.updateYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.DELETED)) {
						yxtkglDao.deleteYxtkxzx(xz);
					}
				}
			}
			if (das != null) {
				for (Tkxzx da : das) {
					if (EntityUtils.getState(da).equals(EntityState.NEW)) {
						if(da.getId() == null){
							for(Tkxzx xz : yxtkNew){
								if(da.getXzKey().equals(xz.getXzKey())){
									da.setId(xz.getId());
								}
							}
						}
						da.setContent(getUUID());
						yxtkglDao.addYxtkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.MODIFIED)) {
						yxtkglDao.updateYxtkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.DELETED)) {
						yxtkglDao.deleteYxtkda(da);
					}
				}
			}

		}
	}
	
	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public Collection<User> getUserByDeptId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getUserByDeptId(id);
	}

	@Override
	public Collection<Tmly> getTmlyInfoByTmlyId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getTmlyInfoByTmlyId(id);
	}

	@Override
	public Collection<Tkfl> getTkflInfoByflId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getTkflInfoByflId(id);
	}

	@Override
	public List countGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return yxtkglDao.countGxjl(param);
	}

	@Override
	public List<Map<String,Object>> getLoginUserInfo() {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return yxtkglDao.getLoginUserInfo(userDetails);
	}

}
