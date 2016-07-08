package gov.hygs.htgl.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.hygs.htgl.service.TjglService;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;

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
		if(param == null){
			return null;
		}
		return tjglService.countGxjl(param);
	}
	
	@DataProvider
	public List countDeptGxjl(Map<String,Object> param){
		return tjglService.countDeptGxjl(param);
	}
}
