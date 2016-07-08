package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.service.ZszskService;

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
public class ZszskController {
	@Resource
	ZszskService zszskService;

	@DataProvider
	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param) {
		zszskService.getZszskInfo(page, param);
	}

	@DataProvider
	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param) {
		zszskService.getYxzskInfo(page, param);
	}

	@Transactional
	@DataResolver
	public void updateZszsk(List<ZskJl> zszsk) {
		zszskService.updateZszsk(zszsk);
	}

	@DataProvider
	public void getRandomdsZszskFilter(Page<ZskJl> page, Map<String, Object> param) {
		zszskService.getRandomdsZszskFilter(page, param);
	}
	
	@Transactional
	@Expose
	public void updateZsdtsInfo(Map<String,Object> param){
		zszskService.updateZsdtsInfo(param);
	}
	
	@DataProvider
	public Collection<Zskly> getZsklyInfo(){
		return zszskService.getZsklyInfo();
	}
	
	@Transactional
	@DataResolver
	public void updateZskly(List<Zskly> zskly){
		zszskService.updateZskly(zskly);
	}
}
