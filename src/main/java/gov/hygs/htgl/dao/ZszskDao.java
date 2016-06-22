package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zszsk;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZszskDao {

	public void getZszskInfo(Page<Zszsk> page, Map<String, Object> param,
			CustomUserDetails userDetails);

	public void addYxzskToZszsk(Zszsk zszsk);

	public void addZszsk(Zszsk zszsk);

	public void addGrDeptGxJl(Zszsk zszsk);

	public void updateZszsk(Zszsk zszsk);

	public void deleteYxzskFormZszsk(Zszsk zszsk);

	public void deleteZszsk(Zszsk zszsk);

	public void deleteGrDeptGxJl(Zszsk zszsk);

	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param);
	
}
