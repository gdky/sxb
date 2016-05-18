package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Dept;

import java.util.List;

import com.bstek.dorado.data.provider.Page;

public interface QXBmCdglDao {

	public List<Dept> getDeptRoot();

	public List<Dept> getCurrentDeptById(String id_);

	public void getCurrentDeptPageById(Page<Dept> page, String id_);

	public void saveDeptNodeInfo(List<Dept> depts);
	
}
