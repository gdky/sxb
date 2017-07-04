package gov.hygs.htgl.tkgl.dao;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Map;

import com.bstek.dorado.data.provider.Page;


public interface TkglDao {

	void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

}
