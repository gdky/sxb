package gov.hygs.htgl.xdjl.controller;

import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;
import gov.hygs.htgl.xdjl.service.ZsxdjlService;

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
import com.bstek.dorado.uploader.UploadFile;
import com.bstek.dorado.uploader.annotation.FileResolver;

@Component
public class ZsxdjlController {
	@Resource
	ZsxdjlService zsxdjlService;

	@DataProvider
	public void getZszskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		zsxdjlService.getZszskInfo(page, param);
	}

	@DataProvider
	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		zsxdjlService.getYxzskInfo(page, param);
	}

	@Transactional
	@DataResolver
	public void updateZszsk(List<XdjlJl> zszsk) {
		zsxdjlService.updateZszsk(zszsk);
	}

	@DataProvider
	public void getRandomdsZszskFilter(Page<XdjlJl> page, Map<String, Object> param) {
		zsxdjlService.getRandomdsZszskFilter(page, param);
	}
	
	@Transactional
	@Expose
	public void updateZsdtsInfo(Map<String,Object> param){
		zsxdjlService.updateZsdtsInfo(param);
	}
	
	@DataProvider
	public Collection<Xdjlly> getZsklyInfo(){
		return zsxdjlService.getZsklyInfo();
	}
	
	@Transactional
	@DataResolver
	public void updateZskly(List<Xdjlly> zskly){
		zsxdjlService.updateZskly(zskly);
	}
	
	@FileResolver
	public String importAttachment(UploadFile file, Map<String, Object> param) throws IOException{
		return zsxdjlService.importAttachment(file,param);
	}
	
	@Transactional
	@FileResolver
	public String importAttachmentImmediately(UploadFile file, Map<String, Object> param) throws IOException{
		return zsxdjlService.importAttachmentImmediately(file,param);
	}
	
	@Expose
	public void cancelUploadAttachmentFile(String param){
		//AttachmentOpt.deleteAttachmentFile(param);
		zsxdjlService.cancelUploadAttachmentFile(param);
	}
	
	@DataProvider
	public Integer getZsdtsInfoFromSystemProps(){
		return zsxdjlService.getZsdtsInfoFromSystemProps();
	}

	@DataProvider
	public void getTsxxInfo(Page page, Map<String,Object> param){
		zsxdjlService.getTsxxInfo(page, param);
	}
	
	@DataProvider
	public void getZsdDetailInfo(Page page, Map<String, Object> param){
		if(param != null){
			zsxdjlService.getZsdDetailInfo(page, param);
		}
	}
	
	@Transactional
	@Expose
	public void updateZsdtsDetailInfo(Map<String,Object> param){
		zsxdjlService.updateZsdtsDetailInfo(param);
	}
	
	@Transactional
	@Expose
	public void deleteZsdtsInfo(String jlid){
		zsxdjlService.deleteZsdtsInfo(jlid);
	}
	
	@Transactional
	@Expose
	public void updateTsxxInfo(Map param){
		zsxdjlService.updateTsxxInfo(param);
	}
}
