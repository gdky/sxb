package gov.hygs.htgl.xdjl.service;

import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface YxxdjlService {

	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param);

	public List<Xdjlly> getZsklyInfoByZsklyId(Integer id);

	public void updateYxzsk(List<XdjlJl> yxzsks);
	
}
