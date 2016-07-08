package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;

public interface YxtkglDao {

	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param, CustomUserDetails userDetails);

	public Collection<Dept> getDeptInfoByDeptId(String id);

	public Collection<User> getUserInfoByUserId(String id);

	public void addYxtkxzx(Tkxzx xz);

	public void updateYxtkxzx(Tkxzx xz);

	public void deleteYxtkxzx(Tkxzx xz);

	public void addYxtkda(Tkxzx da);

	public void updateYxtkda(Tkxzx da);

	public void deleteYxtkda(Tkxzx da);

	public Collection<User> getUserByDeptId(String id);

	public void addYxtk(Tktm yxtk);

	public void updateYxtk(Tktm yxtk);

	public void deleteYxtk(Tktm yxtk);

	public Collection<Tmly> getTmlyInfoByTmlyId(String id);

	public Collection<Tkfl> getTkflInfoByflId(String id);

	public void addGrDeptGxJl(Tktm yxtk);

	public void deleteGrDeptGxJl(Tktm yxtk);

	public List<Map<String,Object>> getLoginUserInfo(CustomUserDetails userDetails);

	public int getTmlyInfoOrAddTmly(String tmlyTitle, String tmlyContent);

	public int getUserIdByDeptIdAndUserName(int deptid, String userName);

	public int getDeptIdByDeptName(String deptName);

	public int getTkflInfoOrAddTkfl(String tkflTkmc);

	public boolean chackTktmExistOrNot(String tktmContent);

	public void batchInsertTk(List<Tktm> tktms, List<Tkxzx> tkxzxs,
			List<Tkxzx> tkdas);

}
