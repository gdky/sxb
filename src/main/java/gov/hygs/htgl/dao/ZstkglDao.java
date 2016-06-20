package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Zstk;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZstkglDao {

	public void getZstkInfo(Page<Zstk> page, Map<String, Object> param, CustomUserDetails userDetails);

	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id);

	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id);

	public Collection<Tkxzx> getToFInfo();

	public void addZstk(Zstk zstk);

	public void addGrDeptGxJl(Zstk zstk);

	public void updateZstk(Zstk zstk);

	public void deleteZstk(Zstk zstk);

	public void deleteGrDeptGxJl(Zstk zstk);

	public void addTkxzx(Tkxzx xz);

	public void updateTkxzx(Tkxzx xz);

	public void deleteTkxzx(Tkxzx xz);

	public void addTkda(Tkxzx da);

	public void updateTkda(Tkxzx da);

	public void deleteTkda(Tkxzx da);

	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param);

	public void addYxtkToZstk(Zstk zstk);

	public void deleteYxtkFromZstk(Zstk zstk);

}
