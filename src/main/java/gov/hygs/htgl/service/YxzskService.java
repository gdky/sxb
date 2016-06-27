package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxzskService {

	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param);

	public List<Zskly> getZsklyInfoByZsklyId(Integer id);

	public void updateYxzsk(List<ZskJl> yxzsks);
	
}
