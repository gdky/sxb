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

	public List countUserAnswerCount(Map<String, Object> param);

	public List countUserAnswerScore(Map<String, Object> param);

	public List countUserRushAnswerScore(Map<String, Object> param);

	public List countUserExamScore(Map<String, Object> param);

	public List getExamInfo();

	public List getCurrentDeptQjById(String id);
	
}
