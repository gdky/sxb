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
		//Object type = param.get("type");
		/*if("0".equals(type) || type == null){
			return tjglDao.countGxjl(param);
		}else if("1".equals(type)){
			return tjglDao.countZskgxjl(param);
		}else if("2".equals(type)){
			return tjglDao.countLaudRecord(param);
		}*/
		Object type = (param==null)?param:param.get("type");
		if("0".equals(type) || type == null){//部门题库贡献记录
			return tjglDao.countDeptGxjl(param);
		}else if("1".equals(type)){//个人题库贡献记录
			return tjglDao.countGxjl(param);
		}else if("2".equals(type)){//部门知识库贡献记录
			return tjglDao.countDeptZskgxjl(param);
		}else if("3".equals(type)){//个人知识库贡献记录
			return tjglDao.countZskgxjl(param);
		}else if("4".equals(type)){//部门点赞数
			return tjglDao.countDeptLaudRecord(param);
		}else if("5".equals(type)){//个人点赞数
			return tjglDao.countLaudRecord(param);
		}else if("6".equals(type)){//题目点赞数
			return tjglDao.countTktmLaudRecord(param);
		}else if("7".equals(type)){//用户答题数量
			return tjglDao.countUserAnswerCount(param);
		}else if("8".equals(type)){//用户答题得分
			return tjglDao.countUserAnswerScore(param);
		}else if("9".equals(type)){//用户抢答答题得分
			return tjglDao.countUserRushAnswerScore(param);
		}else if("10".equals(type)){//用户考试分数
			return tjglDao.countUserExamScore(param);
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
		}else if("2".equals(type)){
			return tjglDao.countDeptLaudRecord(param);
		}
		return null;
	}
	
	
}
