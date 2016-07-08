package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZszskService {

	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param);

	public void updateZszsk(List<ZskJl> zszsk);

	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param);

	public void getRandomdsZszskFilter(Page<ZskJl> page,
			Map<String, Object> param);

	public void updateZsdtsInfo(Map<String, Object> param);

	public Collection<Zskly> getZsklyInfo();

	public void updateZskly(List<Zskly> zskly);
	
}
