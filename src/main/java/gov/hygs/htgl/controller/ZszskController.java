package gov.hygs.htgl.controller;

import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.service.ZszskService;
import gov.hygs.htgl.utils.AttachmentOpt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import com.bstek.dorado.uploader.DownloadFile;
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileProvider;
import com.bstek.dorado.uploader.annotation.FileResolver;

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
	
	@FileResolver
	public String importAttachment(UploadFile file, Map<String, Object> param) throws IOException{
		return zszskService.importAttachment(file,param);
	}
	
	@Transactional
	@FileResolver
	public String importAttachmentImmediately(UploadFile file, Map<String, Object> param) throws IOException{
		return zszskService.importAttachmentImmediately(file,param);
	}
	
	@Expose
	public void cancelUploadAttachmentFile(String param){
		//AttachmentOpt.deleteAttachmentFile(param);
		zszskService.cancelUploadAttachmentFile(param);
	}
	
	@DataProvider
	public Integer getZsdtsInfoFromSystemProps(){
		return zszskService.getZsdtsInfoFromSystemProps();
	}

	@DataProvider
	public void getTsxxInfo(Page page, Map<String,Object> param){
		zszskService.getTsxxInfo(page, param);
	}
	
	@DataProvider
	public void getZsdDetailInfo(Page page, Map<String, Object> param){
		if(param != null){
			zszskService.getZsdDetailInfo(page, param);
		}
	}
}
