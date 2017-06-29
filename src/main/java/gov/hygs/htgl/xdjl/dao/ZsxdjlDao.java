package gov.hygs.htgl.xdjl.dao;

import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;
import gov.hygs.htgl.xdjl.entity.Zsxdjl;

import java.util.Collection;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZsxdjlDao {

	public void getZszskInfo(Page<XdjlJl> page, Map<String, Object> param,
			CustomUserDetails userDetails);

	public void addYxzskToZszsk(Zsxdjl zszsk);

	public void addZszsk(XdjlJl zszsk);

	public void addGrDeptGxJl(XdjlJl zszsk);

	public void updateZszsk(XdjlJl zszsk);

	public void deleteYxzskFormZszsk(Zsxdjl zszsk);

	public void deleteZszsk(XdjlJl zszsk);

	public void deleteGrDeptGxJl(XdjlJl zszsk);

	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param);

	public void getRandomdsZszskFilter(Page<XdjlJl> page,
			Map<String, Object> param);

	public void updateZsdtsInfo(Map<String, Object> param,
			CustomUserDetails userDetails);

	public Collection<Xdjlly> getZsklyInfo();

	public void addZskly(Xdjlly zskly);

	public void updateZskly(Xdjlly zskly);

	public void deleteZskly(Xdjlly zskly);

	public String importAttachment(Map<String, Object> param);

	public void cancelUploadAttachmentFile(String param);

	public Integer getZsdtsInfoFromSystemProps();

	public void getTsxxInfo(Page page, Map<String, Object> param);

	public void getZsdDetailInfo(Page page, Map<String, Object> param);

	public void addGxjl(XdjlJl zszsk);

	public void updateZsdtsDetailInfo(Map<String, Object> param);

	public void deleteZsdtsInfo(String jlid);

	public void updateTsxxInfo(Map param);

}
