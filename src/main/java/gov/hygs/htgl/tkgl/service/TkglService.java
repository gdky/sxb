package gov.hygs.htgl.tkgl.service;

import gov.hygs.htgl.entity.Tktm;

import java.util.Map;

import com.bstek.dorado.data.provider.Page;


public interface TkglService {

	void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

}
