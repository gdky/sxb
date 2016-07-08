package gov.hygs.htgl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.hygs.htgl.dao.TjglDao;
import gov.hygs.htgl.service.TjglService;

import org.springframework.stereotype.Service;
@Service
public class TjglServiceImpl implements TjglService {
	@Resource
	TjglDao tjglDao;

	@Override
	public List countGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Object type = param.get("type");
		if("0".equals(type) || type == null){
			return tjglDao.countGxjl(param);
		}else if("1".equals(type)){
			return tjglDao.countZskgxjl(param);
		}
		return null;
	}

	@Override
	public List countDeptGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Object type = (param==null)?param:param.get("type");
		if("0".equals(type) || type == null){
			return tjglDao.countDeptGxjl(param);
		}else if("1".equals(type)){
			return tjglDao.countDeptZskgxjl(param);
		}
		return null;
	}
	
	
}
