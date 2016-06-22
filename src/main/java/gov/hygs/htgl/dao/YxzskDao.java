package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxzskDao {

	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param,CustomUserDetails userDetails);

	public List<Zskly> getZsklyInfoByZsklyId(Integer id);

	public void addYxzsk(Yxzsk yxzsk);

	public void addGrDeptGxJl(Yxzsk yxzsk);

	public void deleteYxzsk(Yxzsk yxzsk);

	public void deleteGrDeptGxJl(Yxzsk yxzsk);

	public void updateYxzsk(Yxzsk yxzsk);

}
