package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZstkglDao {

	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param,
			CustomUserDetails userDetails);

	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id);

	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id);

	public Collection<Tkxzx> getToFInfo();

	public void addZstk(Tktm zstk);

	public void addGrDeptGxJl(Tktm zstk);

	public void updateZstk(Tktm zstk);

	public void deleteZstk(Tktm zstk);

	public void deleteGrDeptGxJl(Tktm zstk);

	public void addTkxzx(Tkxzx xz);

	public void updateTkxzx(Tkxzx xz);

	public void deleteTkxzx(Tkxzx xz);

	public void addTkda(Tkxzx da);

	public void updateTkda(Tkxzx da);

	public void deleteTkda(Tkxzx da);

	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param);

	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param);

	public void updateTkfxtsInfo(Map<String, Object> param,
			CustomUserDetails userDetails);

	public void updateKstsjlInfo(Map<String, Object> param,
			CustomUserDetails userDetails);

}
