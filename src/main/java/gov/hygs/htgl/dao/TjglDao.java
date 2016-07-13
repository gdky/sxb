package gov.hygs.htgl.dao;

import java.util.List;
import java.util.Map;

public interface TjglDao {

	public List countGxjl(Map<String, Object> param);

	public List countDeptGxjl(Map<String, Object> param);

	public List countZskgxjl(Map<String, Object> param);

	public List countDeptZskgxjl(Map<String, Object> param);

	public List countLaudRecord(Map<String, Object> param);

	public List countDeptLaudRecord(Map<String, Object> param);

	public List countTktmLaudRecord(Map<String, Object> param);
	
}
