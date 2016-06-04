package gov.hygs.htgl.dao;

import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;

import java.util.Collection;

public interface TkcspzDao {

	public Collection<Tkfl> getTkflRoot();

	public Collection<Tkfl> getCurrentTkflById(String id);

	public void addTkfl(Tkfl tkfl);

	public void updateTkfl(Tkfl tkfl);

	public void deleteTkfl(Tkfl tkfl);

	public Collection<Tmly> getTmlyInfo();

	public void addTmly(Tmly tmly);

	public void updateTmly(Tmly tmly);

	public void deleteTmly(Tmly tmly);
	
}
