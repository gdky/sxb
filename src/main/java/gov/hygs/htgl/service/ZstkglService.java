package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Zstk;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZstkglService {

	public void getZstkInfo(Page<Zstk> page, Map<String, Object> param);

	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id);

	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id);

	public Collection<Tkxzx> getToFInfo();

	public void updateZstk(List<Zstk> list);

	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param);

}
