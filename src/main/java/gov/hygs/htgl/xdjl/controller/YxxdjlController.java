package gov.hygs.htgl.xdjl.controller;

import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;
import gov.hygs.htgl.xdjl.service.YxxdjlService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.provider.Page;

@Component
public class YxxdjlController {
	@Resource
	YxxdjlService yxxdjlService;

	@DataProvider
	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		yxxdjlService.getYxzskInfo(page, param);
	}
	
	@DataProvider
	public List<Xdjlly> getZsklyInfoByZsklyId(Integer id){
		return yxxdjlService.getZsklyInfoByZsklyId(id);
	}

	@Transactional
	@DataResolver
	public void updateYxzsk(List<XdjlJl> yxzsks){
		yxxdjlService.updateYxzsk(yxzsks);
	}
}
