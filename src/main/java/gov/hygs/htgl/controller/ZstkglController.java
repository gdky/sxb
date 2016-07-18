package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.service.ZstkglService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.annotation.Expose;
import com.bstek.dorado.data.provider.Page;

@Component
public class ZstkglController {
	@Resource
	ZstkglService zstkglService;

	@DataProvider
	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param) {
		zstkglService.getZstkInfo(page, param);
	}

	@DataProvider
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id) {
		return zstkglService.getTkzxzInfoByZstkId(id);
	}

	@DataProvider
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id) {
		return zstkglService.getDaXzxInfoByZstkId(id);
	}

	@DataProvider
	public Collection<Tkxzx> getToFInfo() {
		return zstkglService.getToFInfo();
	}

	@Transactional
	@DataResolver
	public void updateZstk(List<Tktm> list) {
		zstkglService.updateZstk(list);
	}

	@DataProvider
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		zstkglService.getYxtkInfo(page, param);
	}

	@DataProvider
	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param) {
		zstkglService.getRandomTktmFilter(page, param);
	}
	
	@DataProvider
	public void getRandomFxtsTktmFilter(Page<Tktm> page, Map<String, Object> param) {
		zstkglService.getRandomFxtsTktmFilter(page, param);
	}

	@Transactional
	@Expose
	public void updateTkfxtsInfo(Map<String,Object> param){
		zstkglService.updateTkfxtsInfo(param);
	}
	
	@Transactional
	@Expose
	public void updateKstsjlInfo(Map<String,Object> param){
		zstkglService.updateKstsjlInfo(param);
	}
	
	@DataProvider
	public Integer getSomeInfoBySystemPropsKey(String param){
		return zstkglService.getSomeInfoBySystemPropsKey(param);
	}
	
	@DataProvider
	public Integer getFxtsInfoFromSystemProps(){
		return zstkglService.getFxtsInfoFromSystemProps();
	}
	
}
