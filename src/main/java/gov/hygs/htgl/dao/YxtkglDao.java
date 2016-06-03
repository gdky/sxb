package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkda;
import gov.hygs.htgl.entity.Yxtkxzx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxtkglDao {

	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param);

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

}
