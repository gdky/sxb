package gov.hygs.htgl.service.impl;

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
import com.bstek.dorado.data.variant.Record;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkda;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.YxtkglService;

@Service
public class YxtkglServiceImpl implements YxtkglService {
	@Resource
	YxtkglDao yxtkglDao;

	private List<Yxtkxzx> yxtkNew = new ArrayList<Yxtkxzx>();
	
	@Override
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param) {
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
	public Collection<Yxtkxzx> getYxtkxzxInfoByYxtkId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getYxtkxzxInfoByYxtkId(id);
	}

	@Override
	public Collection<Yxtkxzx> getYxtkdaInfoByYxtkId(String id) {
		// TODO Auto-generated method stub
		return yxtkglDao.getYxtkdaInfoByYxtkId(id);
	}

	@Override
	public void updateYxtk(List<Yxtk> list) {
		// TODO Auto-generated method stub
		for (Yxtk yxtk : list) {
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

			List<Yxtkxzx> das = (List<Yxtkxzx>) yxtk.getDaxzx();
			List<Yxtkxzx> xzs = (List<Yxtkxzx>) yxtk.getYxtkxzx();

			if (xzs != null) {
				for (Yxtkxzx xz : xzs) {
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
				for (Yxtkxzx da : das) {
					if (EntityUtils.getState(da).equals(EntityState.NEW)) {
						if(da.getId() == null){
							for(Yxtkxzx xz : yxtkNew){
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
	public Collection<Yxtkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return yxtkglDao.getToFInfo();
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
	public String countGxjl(Record record) {
		// TODO Auto-generated method stub
		return yxtkglDao.countGxjl(record);
	}

}
