package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkda;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.YxtkglService;
import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	String tkid;

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
				tkid = getUUID();
				yxtk.setId(tkid);
				yxtk.setDrbz("N");
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
						if(!"0".equals(xz.getTkId())){
							xz.setId(this.getUUID());
							if (xz.getTkId() == null) {
								xz.setTkId(tkid);
							}
							yxtkglDao.addYxtkxzx(xz);
						}
						yxtkNew.add(xz);
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
						if (da.getId() == null) {
							for (Tkxzx xz : yxtkNew) {
								if (da.getXzKey().equals(xz.getXzKey())) {
									da.setId(xz.getId());
									if (da.getTkId() == null) {
										da.setTkId(tkid);
									}
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
	public List<Map<String, Object>> getLoginUserInfo() {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return yxtkglDao.getLoginUserInfo(userDetails);
	}

	@Override
	public List countDeptGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return yxtkglDao.countDeptGxjl(param);
	}

	@Override
	public List<TkcjTable> ImportTkcjTableExcel(List<Map<String, Object>> list) {
		List<TkcjTable> errMassage = new ArrayList<TkcjTable>();
		List<String> xzxFields = null;
		List<String> xzKeys = new ArrayList<String>();
		int tmlyId = 0;
		int flId = 0;
		int deptid = 0;
		int userid = 0;
		Map<String, String> tmlyChack = new HashMap<String, String>();
		Map<String, String> flIdChack = new HashMap<String, String>();
		Map<String, String> deptChack = new HashMap<String, String>();
		Map<String, String> userChack = new HashMap<String, String>();

		List<Tktm> tktms = new ArrayList<Tktm>();
		List<Tkxzx> tkxzxs = new ArrayList<Tkxzx>();
		List<Tkxzx> tkdas = new ArrayList<Tkxzx>();

		Pattern p = Pattern.compile("[A-Z]");
		for (Map<String, Object> rowInfo : list) {
			TkcjTable tkcj = (TkcjTable) rowInfo.get("tkcjTable");
			if (tkcj == null) {

				xzxFields = (List<String>) rowInfo.get("fields");
				for (int i = 0; i < xzxFields.size(); i++) {
					Matcher m = p.matcher((String) xzxFields.get(i));
					if (m.find()) {
						xzKeys.add(m.group());
					}
				}

			} else {
				if (tkcj.getTktmContent() != null) {
					if (yxtkglDao.chackTktmExistOrNot(tkcj.getTktmContent())) {
						Tktm tktm = new Tktm();
						tktm.setDrbz("Y");

						if (deptChack.get(tkcj.getDeptName()) == null
								|| deptid == 0) {
							deptid = yxtkglDao.getDeptIdByDeptName(tkcj
									.getDeptName());
							deptChack.put(tkcj.getDeptName(),
									tkcj.getDeptName());
						}
						if (deptid == 0) {
							errMassage.add(tkcj);// 记录当前行数
							deptChack.put(tkcj.getDeptName(), null);
							continue;
						}
						tktm.setDeptid(deptid);

						if (userChack.get(tkcj.getUserName()) == null
								|| userid == 0) {
							userid = yxtkglDao.getUserIdByDeptIdAndUserName(
									deptid, tkcj.getUserName());
							userChack.put(tkcj.getUserName(),
									tkcj.getUserName());
						}
						if (userid == 0) {
							errMassage.add(tkcj);// 记录当前行数
							userChack.put(tkcj.getUserName(), null);
							continue;
						}
						tktm.setUserId(userid);

						if (tmlyChack.get(tkcj.getTmlyTitle()) == null) {
							tmlyId = yxtkglDao.getTmlyInfoOrAddTmly(
									tkcj.getTmlyTitle(), tkcj.getTmlyContent());
							tmlyChack.put(tkcj.getTmlyTitle(),
									tkcj.getTmlyContent());
						}
						tktm.setTmlyId(tmlyId);

						if (flIdChack.get(tkcj.getTkflTkmc()) == null) {
							flId = yxtkglDao.getTkflInfoOrAddTkfl(tkcj
									.getTkflTkmc());
							flIdChack.put(tkcj.getTkflTkmc(),
									tkcj.getTkflTkmc());
						}
						tktm.setFlId(flId);

						if ("基础题".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(0);
						} else if ("进阶题".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(1);
						} else if ("非税收业务类".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(2);
						}

						tktm.setContent(tkcj.getTktmContent());
						String mode = null;
						if ("判断".equals(tkcj.getMode())) {
							mode = "0";
						} else if ("单选".equals(tkcj.getMode())) {
							mode = "1";
						} else if ("多选".equals(tkcj.getMode())) {
							mode = "2";
						}
						tktm.setMode(mode);
						tktm.setCreateDate(new Date());
						String tktmid = this.getUUID();
						tktm.setId(tktmid);

						// yxtkglDao.addYxtk(tktm);
						// yxtkglDao.addGrDeptGxJl(tktm);
						tktms.add(tktm);

						if ("0".equals(mode)) {
							// 判断题
							Tkxzx da = new Tkxzx();
							da.setContent(getUUID());
							da.setTkId(tktmid);
							if ("错误".equals(tkcj.getDaToF())) {
								da.setId("0");
							} else if ("正确".equals(tkcj.getDaToF())) {
								da.setId("1");
							}

							// yxtkglDao.addYxtkda(da);
							tkdas.add(da);

						} else if ("1".equals(mode) || "2".equals(mode)) {
							// 单选题 或 多选题
							int len = xzxFields.size();
							for (int i = 0; i < len / 2; i++) {
								Tkxzx xz = new Tkxzx();
								Tkxzx da = new Tkxzx();
								String tkxzxid = this.getUUID();
								xz.setId(tkxzxid);
								xz.setTkId(tktmid);

								xz.setXzKey(xzKeys.get(i));

								xz.setContent((String) rowInfo.get(xzxFields
										.get(i)));
								if ("正确".equals(rowInfo.get(xzxFields.get(len
										/ 2 + i)))) {
									da.setContent(getUUID());
									da.setTkId(tktmid);
									da.setId(tkxzxid);

									// yxtkglDao.addYxtkda(da);
									tkdas.add(da);

								}

								// yxtkglDao.addYxtkxzx(xz);
								tkxzxs.add(xz);

							}
						}
					}
				}
			}
		}
		yxtkglDao.batchInsertTk(tktms, tkxzxs, tkdas);
		return errMassage == null ? null : errMassage;
	}

}
