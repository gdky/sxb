package gov.hygs.htgl.tkgl.service.impl;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.tkgl.dao.TkglDao;
import gov.hygs.htgl.tkgl.service.TkglService;
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
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param,
			String xybz) {
		// TODO Auto-generated method stub
		tkglDao.getYxtkInfo(page, param, xybz);
	}

	@Override
	public Collection<User> getUser(String yhm) {
		// TODO Auto-generated method stub
		return tkglDao.getUser(yhm);
	}

	public String getUUID() {
		return tkglDao.getUUID();
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
		if (yxtk.getXybz().equals("Y")) {
			tkglDao.addYToZGxz(yxtk);
		}
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

	@Override
	public void yxtkToZstk(List<Tktm> tms, String lx) {
		// TODO Auto-generated method stub
		for (Tktm map : tms) {
			if (lx.equals("Y")) {
				tkglDao.yxtkToZstk(map, lx);
				tkglDao.addYToZGxz(map);
			}else if (lx.equals("L")) {
				tkglDao.yxtkToZstk(map, lx);
			}else if (lx.equals("Z")) {
				tkglDao.yxtkToZstk(map, "Y");
			}
		}
	}

	@Override
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		return tkglDao.getTkzxzInfoByZstkId(id);
	}

	@Override
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		return tkglDao.getDaXzxInfoByZstkId(id);
	}

	@Override
	public Collection<Tkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return tkglDao.getToFInfo();
	}

	@Override
	public List<TkcjTable> ImportTkcjTableExcel(List<Map<String, Object>> list) {
		List<TkcjTable> errMassage = new ArrayList<TkcjTable>();
		List<String> xzxFields = null;
		List<String> xzKeys = new ArrayList<String>();
		int tmlyId = 0;
		int flId = 0;
		Map<String, Integer> tmlyChack = new HashMap<String, Integer>();
		Map<String, Integer> flIdChack = new HashMap<String, Integer>();
		List<Tktm> tktms = new ArrayList<Tktm>();
		List<Tkxzx> tkxzxs = new ArrayList<Tkxzx>();
		List<Tkxzx> tkdas = new ArrayList<Tkxzx>();
		List<Map<String, Object>> deptUser = null;// new ArrayList<Map<String,
													// Object>>();
		Map<String, Map<String, List<Map<String, Object>>>> deptChack = new HashMap<String, Map<String, List<Map<String, Object>>>>();
		Map<String, List<Map<String, Object>>> userChack = new HashMap<String, List<Map<String, Object>>>();
		Map<String, Object> contentChack = new HashMap<String, Object>();
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
					if (tkglDao.chackTktmExistOrNot(tkcj.getTktmContent())
							&& contentChack.get(tkcj.getTktmContent()) == null) {
						String tktmContentId = tkglDao.chackIsImportOrNot(tkcj
								.getTktmContent());
						contentChack.put(tkcj.getTktmContent(),
								tkcj.getTktmContent());
						Tktm tktm = new Tktm();
						tktm.setDrbz("Y");
						userChack = deptChack.get(tkcj.getDeptName());
						if (userChack == null) {
							deptUser = tkglDao.getUserIdByDeptIdAndTheyName(
									tkcj.getUserName(), tkcj.getDeptName());
							userChack = new HashMap<String, List<Map<String, Object>>>();
							userChack.put(tkcj.getUserName(), deptUser);
							deptChack.put(tkcj.getDeptName(), userChack);

						} else {
							deptUser = userChack.get(tkcj.getUserName());
							if (deptUser == null) {
								deptUser = tkglDao
										.getUserIdByDeptIdAndTheyName(
												tkcj.getUserName(),
												tkcj.getDeptName());
								userChack.put(tkcj.getUserName(), deptUser);
							}
						}
						if (!deptUser.isEmpty()) {
							tktm.setDeptid(Integer.parseInt(String
									.valueOf(deptUser.get(0).get("deptid"))));
							tktm.setUserId(Integer.parseInt(String
									.valueOf(deptUser.get(0).get("userid"))));
						} else {
							tkcj.setErrMassage("出题者获出题科室不匹配");
							errMassage.add(tkcj);// 记录当前行数
							continue;
						}
						if (tkcj.getTmlyTitle() == null) {
							tmlyId = 0;
						} else {
							if (tmlyChack.get(tkcj.getTmlyTitle()) == null) {
								tmlyId = tkglDao.getTmlyInfoOrAddTmly(
										tkcj.getTmlyTitle(),
										tkcj.getTmlyContent());
								tmlyChack.put(tkcj.getTmlyTitle(), tmlyId);
							}
						}
						tktm.setTmlyId(tmlyChack.get(tkcj.getTmlyTitle()));

						if (tkcj.getTkflTkmc() == null) {
							flId = 0;
						} else {
							if (flIdChack.get(tkcj.getTkflTkmc()) == null) {
								flId = tkglDao.getTkflInfoOrAddTkfl(tkcj
										.getTkflTkmc());
								flIdChack.put(tkcj.getTkflTkmc(), flId);
							}
						}
						tktm.setFlId(flIdChack.get(tkcj.getTkflTkmc()));// 一个title对应一个content

						if ("基础题".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(0);
						} else if ("进阶题".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(1);
						} else if ("非税收业务类".equals(tkcj.getTktmTmnd())) {
							tktm.setTmnd(2);
						} else {
							tkcj.setErrMassage("没有匹配的题型");
							errMassage.add(tkcj);// 记录当前行数
							continue;
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
						String tktmid = null;
						if (!"0".equals(tktmContentId)) {
							tktmid = tktmContentId;
						} else {
							tktmid = this.getUUID();
						}
						tktm.setId(tktmid);
						tktms.add(tktm);

						if (!"0".equals(tktmContentId)) {
							tkglDao.deleteRecord(tktmContentId);
						}
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
									tkdas.add(da);
								}

								tkxzxs.add(xz);
							}
						}
					} else {
						tkcj.setErrMassage("题目内容已存在");
						errMassage.add(tkcj);
						continue;
					}
				} else {
					tkcj.setErrMassage("题目内容不能为空");
					errMassage.add(tkcj);
					continue;
				}
			}
		}
		tkglDao.batchInsertTk(tktms, tkxzxs, tkdas);
		return errMassage.isEmpty() ? null : errMassage;
	}

}
