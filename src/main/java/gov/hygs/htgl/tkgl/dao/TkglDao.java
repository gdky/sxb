package gov.hygs.htgl.tkgl.dao;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;


public interface TkglDao {

	void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

	Collection<User> getUser(String yhm);

	void deleteYxtkda(Tkxzx da);

	void updateYxtkda(Tkxzx da);

	void addYxtkda(Tkxzx da);

	void addYxtk(Tktm yxtk);

	void addGrDeptGxJl(Tktm yxtk);

	void updateYxtk(Tktm yxtk);

	void deleteGrDeptGxJl(Tktm yxtk);

	void deleteYxtk(Tktm yxtk);

	void addYxtkxzx(Tkxzx xz);

	void updateYxtkxzx(Tkxzx xz);

	void deleteYxtkxzx(Tkxzx xz);

}
