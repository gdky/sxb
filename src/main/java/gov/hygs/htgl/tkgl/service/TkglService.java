package gov.hygs.htgl.tkgl.service;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;


public interface TkglService {

	void getYxtkInfo(Page<Tktm> page, Map<String, Object> param, String xybz);

	Collection<User> getUser(String yhm);

	String getUUID();

	void insertYxtk(Tktm yxtk);

	void updateYxtk(Tktm yxtk);

	void deleteYxtk(Tktm yxtk);

	void insertYxtkxzx(Tkxzx xz);

	void updateYxtkxzx(Tkxzx xz);

	void deleteYxtkxzx(Tkxzx xz);

	void insertYxtkda(Tkxzx da, List<Tkxzx> xzx);

	void updateYxtkda(Tkxzx da);

	void deleteYxtkda(Tkxzx da);

	void yxtkToZstk(List<Tktm> tms, String lx);

	Collection<Tkxzx> getTkzxzInfoByZstkId(String id);

	Collection<Tkxzx> getDaXzxInfoByZstkId(String id);

	Collection<Tkxzx> getToFInfo();

	List<TkcjTable> ImportTkcjTableExcel(List<Map<String, Object>> list);



}
