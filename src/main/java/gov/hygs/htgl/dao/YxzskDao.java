package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zskly;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxzskDao {

	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param);

	public List<Zskly> getZsklyInfoByZsklyId(Integer id);

	public void addYxzsk(Yxzsk yxzsk);

	public void addGrDeptGxJl(Yxzsk yxzsk);

}
