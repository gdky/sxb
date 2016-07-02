package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxtkglService {

	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

	public Collection<Dept> getDeptInfoByDeptId(String id);

	public Collection<User> getUserInfoByUserId(String id);

	public void updateYxtk(List<Tktm> list);

	public Collection<User> getUserByDeptId(String id);

	public Collection<Tmly> getTmlyInfoByTmlyId(String id);

	public Collection<Tkfl> getTkflInfoByflId(String id);

	public List countGxjl(Map<String, Object> param);

	public List<Map<String,Object>> getLoginUserInfo();

	public List countDeptGxjl(Map<String, Object> param);

	public void ImportTkcjTableExcel(List<Map<String, Object>> list);

}
