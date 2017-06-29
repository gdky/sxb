package gov.hygs.htgl.xdjl.service;

import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;

public interface ZsxdjlService {

	public void getZszskInfo(Page<XdjlJl> page, Map<String, Object> param);

	public void updateZszsk(List<XdjlJl> zszsk);

	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param);

	public void getRandomdsZszskFilter(Page<XdjlJl> page,
			Map<String, Object> param);

	public void updateZsdtsInfo(Map<String, Object> param);

	public Collection<Xdjlly> getZsklyInfo();

	public void updateZskly(List<Xdjlly> zskly);

	public String importAttachment(UploadFile file, Map<String, Object> param) throws IOException;

	public void cancelUploadAttachmentFile(String param);

	public String importAttachmentImmediately(UploadFile file, Map<String, Object> param) throws IOException;

	public Integer getZsdtsInfoFromSystemProps();

	public void getTsxxInfo(Page page, Map<String, Object> param);

	public void getZsdDetailInfo(Page page, Map<String, Object> param);

	public void updateZsdtsDetailInfo(Map<String, Object> param);

	public void deleteZsdtsInfo(String jlid);

	public void updateTsxxInfo(Map param);
	
}
