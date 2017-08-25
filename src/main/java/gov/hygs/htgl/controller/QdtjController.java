package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.service.QdtjService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.data.provider.Page;

@Component
public class QdtjController {
	@Resource
	private QdtjService qdtjService;

	@DataProvider
	public List<Map<String,Object>> getGroup() {
		return qdtjService.getGroup();
	}
	
	 @DataProvider
	    public List<Map<String,Object>> getWqd(int ID_){
	        return qdtjService.getWqd(ID_);
	    }
}
