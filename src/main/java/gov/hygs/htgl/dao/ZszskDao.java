package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zszsk;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZszskDao {

	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param,
			CustomUserDetails userDetails);

	public void addYxzskToZszsk(Zszsk zszsk);

	public void addZszsk(ZskJl zszsk);

	public void addGrDeptGxJl(ZskJl zszsk);

	public void updateZszsk(ZskJl zszsk);

	public void deleteYxzskFormZszsk(Zszsk zszsk);

	public void deleteZszsk(ZskJl zszsk);

	public void deleteGrDeptGxJl(ZskJl zszsk);

	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param);
	
}
