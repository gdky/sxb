package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZstkglService {

	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param);

	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id);

	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id);

	public Collection<Tkxzx> getToFInfo();

	public void updateZstk(List<Tktm> list);

	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param);

	public void updateTkfxtsInfo(Map<String, Object> param);

}
