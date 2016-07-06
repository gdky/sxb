package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.ZstkglDao;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.ZstkglService;

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
public class ZstkglServiceImpl implements ZstkglService {
	@Resource
	ZstkglDao zstkglDao;
	String tkid;

	private List<Tkxzx> zstkNew = new ArrayList<Tkxzx>();

	@Override
	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zstkglDao.getZstkInfo(page, param, userDetails);
	}

	@Override
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		return zstkglDao.getTkzxzInfoByZstkId(id);
	}

	@Override
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		return zstkglDao.getDaXzxInfoByZstkId(id);
	}

	@Override
	public Collection<Tkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return zstkglDao.getToFInfo();
	}

	@Override
	public void updateZstk(List<Tktm> list) {
		// TODO Auto-generated method stub
		for (Tktm zstk : list) {
			if (EntityUtils.getState(zstk).equals(EntityState.NEW)) {
				if (zstk.getId() == null) {
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					zstk.setUserId(userDetails.getId());
					zstk.setDeptid(userDetails.getDeptid());
					tkid = getUUID();
					zstk.setId(tkid);
					zstk.setDrbz("N");
					zstkglDao.addZstk(zstk);
				} else {
					// zstkglDao.addYxtkToZstk(zstk);
					zstkglDao.updateZstk(zstk);
				}
				zstk.setContent(getUUID());
				zstkglDao.addGrDeptGxJl(zstk);
			}
			if (EntityUtils.getState(zstk).equals(EntityState.MODIFIED)) {
				zstkglDao.updateZstk(zstk); // 把yxbz和xybz都设置
			}
			if (EntityUtils.getState(zstk).equals(EntityState.DELETED)) {
				// zstkglDao.deleteYxtkFromZstk(zstk);
				// zstkglDao.updateZstk(zstk); 删除调用该方法，前台把xybz设置为n
				zstkglDao.deleteZstk(zstk);// 前台不需要这是xybz，该方法会把xybz设置为n
				zstkglDao.deleteGrDeptGxJl(zstk);
			}

			List<Tkxzx> das = (List<Tkxzx>) zstk.getDaxzx();
			List<Tkxzx> xzs = (List<Tkxzx>) zstk.getTkxzx();

			if (xzs != null) {
				for (Tkxzx xz : xzs) {
					if (EntityUtils.getState(xz).equals(EntityState.NEW)) {
						if(!"0".equals(xz.getTkId())){
							xz.setId(getUUID());
							if(xz.getTkId() == null){
								xz.setTkId(tkid);
							}
							zstkglDao.addTkxzx(xz);
						}
						zstkNew.add(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.MODIFIED)) {
						zstkglDao.updateTkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.DELETED)) {
						zstkglDao.deleteTkxzx(xz);
					}
				}
			}
			if (das != null) {
				for (Tkxzx da : das) {
					if (EntityUtils.getState(da).equals(EntityState.NEW)) {
						if (da.getId() == null) {
							for (Tkxzx xz : zstkNew) {
								if (da.getXzKey().equals(xz.getXzKey())) {
									da.setId(xz.getId());
									if(da.getTkId() == null){
										da.setTkId(tkid);
									}
								}
							}
						}
						da.setContent(getUUID());
						zstkglDao.addTkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.MODIFIED)) {
						zstkglDao.updateTkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.DELETED)) {
						zstkglDao.deleteTkda(da);
					}
				}
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.getYxtkInfo(page, param);
	}

	@Override
	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.getRandomTktmFilter(page, param);
	}

	@Override
	public void updateTkfxtsInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zstkglDao.updateTkfxtsInfo(param, userDetails);
	}

	@Override
	public void updateKstsjlInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zstkglDao.updateKstsjlInfo(param, userDetails);
	}

}
