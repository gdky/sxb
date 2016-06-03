package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.TkcspzDao;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.service.TkcspzService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
@Service
public class TkcspzServiceImpl implements TkcspzService {
	@Resource
	TkcspzDao tkcspzDao;

	@Override
	public Collection<Tkfl> getTkflRoot() {
		// TODO Auto-generated method stub
		return tkcspzDao.getTkflRoot();
	}

	@Override
	public Collection<Tkfl> getCurrentTkflById(String id) {
		// TODO Auto-generated method stub
		return tkcspzDao.getCurrentTkflById(id);
	}

	@Override
	public void updateTkfl(List<Tkfl> list) {
		// TODO Auto-generated method stub
		if(list != null){
			for(Tkfl tkfl : list){
				if(EntityUtils.getState(tkfl).equals(EntityState.NEW)){
					tkcspzDao.addTkfl(tkfl);
				}
				if(EntityUtils.getState(tkfl).equals(EntityState.MODIFIED)){
					tkcspzDao.updateTkfl(tkfl);
				}
				if(EntityUtils.getState(tkfl).equals(EntityState.DELETED)){
					tkcspzDao.deleteTkfl(tkfl);
				}
				this.updateTkfl((List<Tkfl>) tkfl.getChild());
			}
		}
	}

	@Override
	public Collection<Tmly> getTmlyInfo() {
		// TODO Auto-generated method stub
		return tkcspzDao.getTmlyInfo();
	}

	@Override
	public void updateTmly(List<Tmly> tmlys) {
		// TODO Auto-generated method stub
		for(Tmly tmly : tmlys){
			if(EntityUtils.getState(tmly).equals(EntityState.NEW)){
				tkcspzDao.addTmly(tmly);
			}
			if(EntityUtils.getState(tmly).equals(EntityState.MODIFIED)){
				tkcspzDao.updateTmly(tmly);
			}
			if(EntityUtils.getState(tmly).equals(EntityState.DELETED)){
				tkcspzDao.deleteTmly(tmly);
			}			
		}
	}
	
}
