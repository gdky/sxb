package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Zstk;
import gov.hygs.htgl.service.ZstkglService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;

@Component
public class ZstkglController {
	@Resource
	ZstkglService zstkglService;
	
	@DataProvider
	public void getZstkInfo(Page<Zstk> page, Map<String, Object> param){
		zstkglService.getZstkInfo(page,param);
	}
	
	@DataProvider
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id){
		return zstkglService.getTkzxzInfoByZstkId(id);
	}
	
	@DataProvider
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id){
		return zstkglService.getDaXzxInfoByZstkId(id);
	}
	
	@DataProvider
	public Collection<Tkxzx> getToFInfo(){
		return zstkglService.getToFInfo();
	}
	
	@Transactional
	@DataResolver
	public void updateZstk(List<Zstk> list){
		zstkglService.updateZstk(list);
	}
	
	@DataProvider
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param){
		zstkglService.getYxtkInfo(page, param);
	}
	
}
