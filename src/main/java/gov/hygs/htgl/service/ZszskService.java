package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zszsk;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZszskService {

	public void getZszskInfo(Page<Zszsk> page, Map<String, Object> param);

	public void updateZszsk(List<Zszsk> zszsk);

	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param);
	
}
