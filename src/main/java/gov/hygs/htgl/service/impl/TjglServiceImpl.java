package gov.hygs.htgl.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.hygs.htgl.dao.TjglDao;
import gov.hygs.htgl.service.TjglService;
import gov.hygs.htgl.utils.excel.ImportExcel;
import net.sf.jxls.exception.ParsePropertyException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
		}else if("11".equals(type)){//部门出题数量查询
			return tjglDao.countDeptCtslCount(param);
		}else if("12".equals(type)){//个人出题数量查询
			return tjglDao.countUserCtslCount(param);
		}else if("13".equals(type)){//题目纠错查询
			return tjglDao.countTktmErrRecord(param);
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

	@Override
	public String getGxtj(Map param) throws Exception {
		// TODO Auto-generated method stub
		String fileName = null;
		if(param != null){
			String type=(String) param.get("type");
			ImportExcel importExcel = new ImportExcel();
			Map beans=new HashMap();
			List gxtj=new ArrayList();
			String templateFile = null;
			if("0".equals(type)){
				templateFile = "BmCtGxz";
			}else if("1".equals(type)){
				templateFile = "GrCtGxz";
			}else if("2".equals(type)){
				templateFile = "BmZsdGxz";
			}else if("3".equals(type)){
				templateFile = "GrZsydGxz";
			}else if("4".equals(type)){
				templateFile = "BmBdzs";
			}else if("5".equals(type)){
				templateFile = "GrBdzs";
			}else if("6".equals(type)){
				templateFile = "TmBdzs";
			}else if("7".equals(type)){
				templateFile = "YhDtsl";
			}else if("8".equals(type)){
				templateFile = "YhDtdf";
			}else if("9".equals(type)){
				templateFile = "YhQdDtdf";
			}else if("10".equals(type)){
				templateFile = "YhKsfs";
			}else if("11".equals(type)){
				templateFile = "BmCtSlCx";
			}else if("12".equals(type)){
				templateFile = "GrCtSlCx";
			}else if("13".equals(type)){
				templateFile = "TmJcCx";
			}
			gxtj = this.countGxjl(param);
			beans.put("gxtj", gxtj);
			fileName=importExcel.importExcel(beans, templateFile);
		}
		return fileName;
	}

	@Override
	public List getExamInfo() {
		// TODO Auto-generated method stub
		return tjglDao.getExamInfo();
	}

	@Override
	public List getCurrentDeptQjById(String id) {
		// TODO Auto-generated method stub
		return tjglDao.getCurrentDeptQjById(id);
	}
	
}
