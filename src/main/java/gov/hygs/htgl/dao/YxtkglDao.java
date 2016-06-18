package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkda;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

public interface YxtkglDao {

	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param, CustomUserDetails userDetails);

	public Collection<Dept> getDeptInfoByDeptId(String id);

	public Collection<User> getUserInfoByUserId(String id);

	public Collection<Yxtkxzx> getYxtkxzxInfoByYxtkId(String id);

	public Collection<Yxtkxzx> getYxtkdaInfoByYxtkId(String id);

	public void addYxtkxzx(Yxtkxzx xz);

	public void updateYxtkxzx(Yxtkxzx xz);

	public void deleteYxtkxzx(Yxtkxzx xz);

	public void addYxtkda(Yxtkxzx da);

	public void updateYxtkda(Yxtkxzx da);

	public void deleteYxtkda(Yxtkxzx da);

	public Collection<User> getUserByDeptId(String id);

	public void addYxtk(Yxtk yxtk);

	public void updateYxtk(Yxtk yxtk);

	public void deleteYxtk(Yxtk yxtk);

	public Collection<Yxtkxzx> getToFInfo();

	public Collection<Tmly> getTmlyInfoByTmlyId(String id);

	public Collection<Tkfl> getTkflInfoByflId(String id);

	public void addGrDeptGxJl(Yxtk yxtk);

	public void deleteGrDeptGxJl(Yxtk yxtk);

	public List countGxjl(Map<String, Object> param);

	public List<Map<String,Object>> getLoginUserInfo(CustomUserDetails userDetails);

}
