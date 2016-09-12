package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.entity.Zszsk;
import gov.hygs.htgl.security.CustomUserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bstek.dorado.data.provider.Page;

public interface ZszskDao {

	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param,
			CustomUserDetails userDetails);

	public void addYxzskToZszsk(Zszsk zszsk);

	public void addZszsk(ZskJl zszsk);

	public void addGrDeptGxJl(ZskJl zszsk);

	public void updateZszsk(ZskJl zszsk);

	public void deleteYxzskFormZszsk(Zszsk zszsk);

	public void deleteZszsk(ZskJl zszsk);

	public void deleteGrDeptGxJl(ZskJl zszsk);

	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param);

	public void getRandomdsZszskFilter(Page<ZskJl> page,
			Map<String, Object> param);

	public void updateZsdtsInfo(Map<String, Object> param,
			CustomUserDetails userDetails);

	public Collection<Zskly> getZsklyInfo();

	public void addZskly(Zskly zskly);

	public void updateZskly(Zskly zskly);

	public void deleteZskly(Zskly zskly);

	public String importAttachment(Map<String, Object> param);

	public void cancelUploadAttachmentFile(String param);

	public Integer getZsdtsInfoFromSystemProps();

	public void getTsxxInfo(Page page, Map<String, Object> param);

	public void getZsdDetailInfo(Page page, Map<String, Object> param);

	public void addGxjl(ZskJl zszsk);

	public void updateZsdtsDetailInfo(Map<String, Object> param);

	public void deleteZsdtsInfo(String jlid);

}
