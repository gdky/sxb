package gov.hygs.htgl.service;

import java.util.List;
import java.util.Map;

public interface TjglService {

	public List countGxjl(Map<String, Object> param);

	public List countDeptGxjl(Map<String, Object> param);

	public String getGxtj(Map param) throws Exception;

	public List getExamInfo();

	public List getCurrentDeptQjById(String id);
	
}
