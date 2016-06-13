package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkda;
import gov.hygs.htgl.entity.Yxtkxzx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

public interface YxtkglService {

	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param);

	public Collection<Dept> getDeptInfoByDeptId(String id);

	public Collection<User> getUserInfoByUserId(String id);

	public Collection<Yxtkxzx> getYxtkxzxInfoByYxtkId(String id);

	public Collection<Yxtkxzx> getYxtkdaInfoByYxtkId(String id);

	public void updateYxtk(List<Yxtk> list);

	public Collection<User> getUserByDeptId(String id);

	public Collection<Yxtkxzx> getToFInfo();

	public Collection<Tmly> getTmlyInfoByTmlyId(String id);

	public Collection<Tkfl> getTkflInfoByflId(String id);

	public String countGxjl(Record record);

}
