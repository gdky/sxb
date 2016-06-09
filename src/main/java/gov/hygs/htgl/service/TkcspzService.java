package gov.hygs.htgl.service;

import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;

import java.util.Collection;
import java.util.List;

import com.bstek.dorado.data.provider.Page;

public interface TkcspzService {

	public Collection<Tkfl> getTkflRoot();

	public Collection<Tkfl> getCurrentTkflById(String id);

	public void updateTkfl(List<Tkfl> list);

	public Collection<Tmly> getTmlyInfo();

	public void updateTmly(List<Tmly> tmlys);

}
