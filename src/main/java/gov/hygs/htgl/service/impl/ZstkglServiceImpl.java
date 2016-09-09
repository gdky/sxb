package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.ZstkglDao;
import gov.hygs.htgl.entity.Exam;
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
	public void getZstkInfoByKsts(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zstkglDao.getZstkInfoByKsts(page, param, userDetails);
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
					zstk.setContent(getUUID());
					zstkglDao.addGxJl(zstk);
				} else {
					// zstkglDao.addYxtkToZstk(zstk);
					zstkglDao.updateZstk(zstk);
				}
				zstk.setContent(getUUID());
				zstkglDao.addGrDeptGxJl(zstk);
			}
			if (EntityUtils.getState(zstk).equals(EntityState.MODIFIED)) {
				zstkglDao.updateZstk(zstk); // 鎶妝xbz鍜寈ybz閮借缃�
			}
			if (EntityUtils.getState(zstk).equals(EntityState.DELETED)) {
				// zstkglDao.deleteYxtkFromZstk(zstk);
				// zstkglDao.updateZstk(zstk); 鍒犻櫎璋冪敤璇ユ柟娉曪紝鍓嶅彴鎶妜ybz璁剧疆涓簄
				zstkglDao.deleteZstk(zstk);// 鍓嶅彴涓嶉渶瑕佽繖鏄痻ybz锛岃鏂规硶浼氭妸xybz璁剧疆涓簄
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
										da.setTkId(tkid==null?xz.getTkId():tkid);
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
		zstkglDao.getRandomTktmFilter(page, param, "ksts");
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

	@Override
	public Integer getSomeInfoBySystemPropsKey(String param) {
		// TODO Auto-generated method stub
		return zstkglDao.getSomeInfoBySystemPropsKey(param);
	}

	@Override
	public void getRandomFxtsTktmFilter(Page<Tktm> page,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.getRandomTktmFilter(page, param, "fxts");
	}

	@Override
	public Integer getFxtsInfoFromSystemProps() {
		// TODO Auto-generated method stub
		return zstkglDao.getSomeInfoBySystemPropsKey("fxts");
	}
//ksts

	@Override
	public void getExamInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.getExamInfo(page,param);
	}

	@Override
	public void getExamDetailInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.getExamDetailInfo(page,param);
	}

	@Override
	public Map<String, Object> getGroupByExamId(String param) {
		// TODO Auto-generated method stub
		return zstkglDao.getGroupByExamId(param);
	}

	@Override
	public void updateKstsjlDetailInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		zstkglDao.updateKstsjlDetailInfo(param);
	}

	@Override
	public void deleteKstsjlInfo(String examid) {
		// TODO Auto-generated method stub
		zstkglDao.deleteKstsjlInfo(examid);
	}
}
