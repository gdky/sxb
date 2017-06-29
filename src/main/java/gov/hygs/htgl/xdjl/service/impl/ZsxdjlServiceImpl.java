package gov.hygs.htgl.xdjl.service.impl;

import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.utils.AttachmentOpt;
import gov.hygs.htgl.xdjl.dao.ZsxdjlDao;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;
import gov.hygs.htgl.xdjl.service.ZsxdjlService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;

@Service
public class ZsxdjlServiceImpl implements ZsxdjlService {
	@Resource
	ZsxdjlDao zsxdjlDao;

	@Override
	public void getZszskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zsxdjlDao.getZszskInfo(page, param, userDetails);
	}

	@Override
	public void updateZszsk(List<XdjlJl> list) {
		// TODO Auto-generated method stub
		for (XdjlJl zszsk : list) {
			if (EntityUtils.getState(zszsk).equals(EntityState.NEW)) {
				if (zszsk.getId() == null) {
					CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					zszsk.setUserId(userDetails.getId());
					zszsk.setDeptid(userDetails.getDeptid());
					zszsk.setId(getUUID());
					zsxdjlDao.addZszsk(zszsk);
					zszsk.setContent(getUUID());
					zsxdjlDao.addGxjl(zszsk);
				} else {
					//zsxdjlDao.addYxzskToZszsk(zszsk);
					zsxdjlDao.updateZszsk(zszsk);
				}
				zszsk.setContent(getUUID());
				zsxdjlDao.addGrDeptGxJl(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.MODIFIED)) {
				zsxdjlDao.updateZszsk(zszsk);
			}
			if (EntityUtils.getState(zszsk).equals(EntityState.DELETED)) {
				//zsxdjlDao.deleteYxzskFormZszsk(zszsk);
				zsxdjlDao.deleteZszsk(zszsk);
				zsxdjlDao.deleteGrDeptGxJl(zszsk);
			}
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}

	@Override
	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zsxdjlDao.getYxzskInfo(page, param);
	}

	@Override
	public void getRandomdsZszskFilter(Page<XdjlJl> page,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		zsxdjlDao.getRandomdsZszskFilter(page, param);
	}

	@Override
	public void updateZsdtsInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		zsxdjlDao.updateZsdtsInfo(param, userDetails);
	}

	@Override
	public Collection<Xdjlly> getZsklyInfo() {
		// TODO Auto-generated method stub
		return zsxdjlDao.getZsklyInfo();
	}

	@Override
	public void updateZskly(List<Xdjlly> zsklys) {
		// TODO Auto-generated method stub
		for(Xdjlly zskly : zsklys){
			if(EntityUtils.getState(zskly).equals(EntityState.NEW)){
				zsxdjlDao.addZskly(zskly);
			}
			if(EntityUtils.getState(zskly).equals(EntityState.MODIFIED)){
				zsxdjlDao.updateZskly(zskly);
			}
			if(EntityUtils.getState(zskly).equals(EntityState.DELETED)){
				zsxdjlDao.deleteZskly(zskly);
				AttachmentOpt.deleteAttachmentFile(zskly.getAttachment());
			}
		}
	}

	@Override
	public String importAttachment(UploadFile file, Map<String, Object> param) throws IOException {
		// TODO Auto-generated method stub
		MultipartFile mufile = file.getMultipartFile();
		String path = AttachmentOpt.getAttachmentPath();
		path+="attachments"; 
		//FileOutputStream out=new FileOutputStream(path+"/"+file.getFileName());
		FileOutputStream out=new FileOutputStream(path+"/"+this.rebulidFileName(file.getFileName()));
		out.write(mufile.getBytes());
		out.close();
		//param.put("path","attachments/"+file.getFileName() );
		//return "attachments/"+file.getFileName();
		//return path;
		return ""+this.rebulidFileName(file.getFileName());
	}

	@Override
	public void cancelUploadAttachmentFile(String param) {
		// TODO Auto-generated method stub
		zsxdjlDao.cancelUploadAttachmentFile(param);
	}

	@Override
	public String importAttachmentImmediately(UploadFile file,
			Map<String, Object> param) throws IOException {
		// TODO Auto-generated method stub
		MultipartFile mufile = file.getMultipartFile();
		String path = AttachmentOpt.getAttachmentPath();
		path+="attachments"; 
		//FileOutputStream out=new FileOutputStream(path+"/"+file.getFileName());
		FileOutputStream out=new FileOutputStream(path+"/"+this.rebulidFileName(file.getFileName()));
		out.write(mufile.getBytes());
		out.close();
		//param.put("path","attachments/"+file.getFileName());
		param.put("path",""+this.rebulidFileName(file.getFileName()));
		zsxdjlDao.importAttachment(param);
		return null;
	}
	
	private String rebulidFileName(String fileName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String filePostfix = fileName.substring(fileName.lastIndexOf("."));
		String newFileName = fileName.substring(0, fileName.lastIndexOf(filePostfix))+"_"+sdf.format(new Date())+filePostfix;
		return newFileName;
	}

	@Override
	public Integer getZsdtsInfoFromSystemProps() {
		// TODO Auto-generated method stub
		return zsxdjlDao.getZsdtsInfoFromSystemProps();
	}

	@Override
	public void getTsxxInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zsxdjlDao.getTsxxInfo(page, param);
	}

	@Override
	public void getZsdDetailInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		zsxdjlDao.getZsdDetailInfo(page, param);
	}

	@Override
	public void updateZsdtsDetailInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		zsxdjlDao.updateZsdtsDetailInfo(param);
	}

	@Override
	public void deleteZsdtsInfo(String jlid) {
		// TODO Auto-generated method stub
		zsxdjlDao.deleteZsdtsInfo(jlid);
	}

	@Override
	public void updateTsxxInfo(Map param) {
		// TODO Auto-generated method stub
		zsxdjlDao.updateTsxxInfo(param);
	}

}
