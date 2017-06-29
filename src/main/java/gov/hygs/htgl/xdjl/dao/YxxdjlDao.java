package gov.hygs.htgl.xdjl.dao;

import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxxdjlDao {

	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param,CustomUserDetails userDetails);

	public List<Xdjlly> getZsklyInfoByZsklyId(Integer id);

	public void addYxzsk(XdjlJl yxzsk);

	public void addGrDeptGxJl(XdjlJl yxzsk);

	public void deleteYxzsk(XdjlJl yxzsk);

	public void deleteGrDeptGxJl(XdjlJl yxzsk);

	public void updateYxzsk(XdjlJl yxzsk);

}
