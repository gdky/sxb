package gov.hygs.htgl.tkgl.controller;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.tkgl.service.TkglService;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;

@Component
public class TkglController {
	@Resource
	TkglService tkglService;
	/**
	 * 获取预选题库信息
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param){
		tkglService.getYxtkInfo(page, param);
	}
}
