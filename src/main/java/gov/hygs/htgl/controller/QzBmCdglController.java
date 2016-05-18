package gov.hygs.htgl.controller;

import gov.hygs.htgl.dao.QXBmCdglDao;
import gov.hygs.htgl.entity.Dept;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;

@Component
public class QzBmCdglController {
	@Resource
	QXBmCdglDao bmglDao;
	/**
	 * 获取dept的根节点
	 * @return
	 */
	@DataProvider
	public List<Dept> getDeptRoot(){
		return bmglDao.getDeptRoot();
	}
	
	/**
	 * 获取dept当前节点信息
	 * @param id_ 当前dept节点的id
	 * @return
	 */
	@DataProvider
	public List<Dept> getCurrentDeptById(String id_){
		return bmglDao.getCurrentDeptById(id_);
	}
	
	/**
	 * 获取当前节点后，用分页显示
	 * @param page
	 * @param id_
	 */
	@DataProvider
	public void getCurrentDeptPageById(Page<Dept> page,String id_){
		if(id_ != null){
			bmglDao.getCurrentDeptPageById(page, id_);
		}
	}
	
	@DataResolver
	@Transactional
	public void saveDeptNodeInfo(List<Dept> depts){
		bmglDao.saveDeptNodeInfo(depts);
	}
	
	@DataResolver
	@Transactional
	public void deleteDeptNodeInfo(List<Dept> depts){
		for(Dept dept : depts){
			System.out.println(dept.getId_());
		}
	}
}
