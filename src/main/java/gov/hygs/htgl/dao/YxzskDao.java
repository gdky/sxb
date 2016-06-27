package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxzskDao {

	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param,CustomUserDetails userDetails);

	public List<Zskly> getZsklyInfoByZsklyId(Integer id);

	public void addYxzsk(ZskJl yxzsk);

	public void addGrDeptGxJl(ZskJl yxzsk);

	public void deleteYxzsk(ZskJl yxzsk);

	public void deleteGrDeptGxJl(ZskJl yxzsk);

	public void updateYxzsk(ZskJl yxzsk);

}
