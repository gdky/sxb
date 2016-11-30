package gov.hygs.htgl.controller;

import gov.hygs.htgl.service.TjglService;
import gov.hygs.htgl.utils.excel.ImportExcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.Expose;

@Component
public class TjglController {
	@Resource
	TjglService tjglService;
	
	/**
	 * 统计贡献记录
	 * @param param
	 * @return
	 */
	@DataProvider
	public List countGxjl(Map<String,Object> param){
		/*if(param == null){
			return null;
		}*/
		return tjglService.countGxjl(param);
	}
	
	@DataProvider
	public List countDeptGxjl(Map<String,Object> param){
		return tjglService.countDeptGxjl(param);
	}
	
	@Expose
	public String exportExcel(Map param) throws Exception {
		return tjglService.getGxtj(param);
	}
	
	@DataProvider
	public List getExamInfo(){
		return tjglService.getExamInfo();
	}
	
	@DataProvider
	public List getCurrentDeptQjById(String id){
		return tjglService.getCurrentDeptQjById(id);
	}
}
