package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.TjglDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;
@Repository
public class TjglDaoImpl extends BaseJdbcDao implements TjglDao {

	private void rebuildSqlWhenDeptidIs1(StringBuffer sb){
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList("select id_ from dept where parent_id = 1");
		List id = new ArrayList();
		if(list != null){
			for(Map<String,Object> map : list){
				List<Map<String,Object>> ids = this.jdbcTemplate.queryForList("select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?))",new Object[]{map.get("id_")});
				for(Map<String,Object> maps : ids){
					id.add(maps.get("id_"));
				}
			}
		}
		for(int i = 0; i < id.size(); i++){
			sb.append(id.get(i));
			if(i<id.size() - 1){
				sb.append(",");
			}
		}
	}
	
	@Override
	public List countGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		StringBuffer sql = new StringBuffer("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as deptname, ");
		if (userId == null || userId != 0) {
			sql.append("u.user_name as username,");
		}
		sql.append("cou from ");
		sql.append("(select a.dept_id as did,a.user_id uid ,round(sum(a.gxz),1) as cou ");
		sql.append("from tk_gxjl a , tktm b ");
		sql.append("where a.tk_id=b.id_ ");
		if(deptid != null){
			sql.append(" and a.dept_id in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
				args.add(deptid);
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if(user != null){
			sql.append("and a.user_id in (select id_ from user where user_name like ?) ");
			args.add("%"+user+"%");
		}
		if (begin != null) {
			sql.append("and a.gx_date >= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("begin")));
		}
		if (end != null) {
			sql.append("and a.gx_date <= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("end")));
		}
		if (content != null) {
			sql.append("and b.tmly_id in ( select t.id_ from tmly t ");
			sql.append("where t.title like ? ");
			sql.append("or t.content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		sql.append(" group by a.dept_id");
		if (userId == null || userId != 0) {
			sql.append(",a.user_id ");
		}
		sql.append(")t, ");
		sql.append("user u,dept d where t.did=d.id_ and u.id_=t.uid");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countDeptGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		StringBuffer sql = new StringBuffer("select ifnull(");
			sql.append("(select round(sum(b.gxz),1) from dept a, tk_gxjl b ");
			sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) ");
			sql.append("and a.id_=b.dept_id),0)");
			sql.append("as cou, ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
			sql.append(" ,pt.dept_name) as deptname ");
			sql.append("from dept pt where pt.id_ in(");
			sql.append(" select d.id_");
			sql.append(" from dept d,tk_gxjl a , tktm b");
			sql.append(" where a.tk_id=b.id_ and d.id_ = a.dept_id");
		if(deptid != null){
			sql.append(" and a.dept_id in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
				args.add(deptid);
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if (begin != null) {
			sql.append(" and a.gx_date >= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("begin")));
		}
		if (end != null) {
			sql.append(" and a.gx_date <= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("end")));
		}
		if (content != null) {
			sql.append(" and b.tmly_id in ");
			sql.append(" (select t.id_ from tmly t where t.title like ?  or t.content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		sql.append(")");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());;
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List countZskgxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		
		StringBuffer sql = new StringBuffer("select ");
					sql.append(" concat( ");
					sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
					sql.append(" ,d.dept_name) ");
					sql.append("  as deptname, ");
					sql.append(" u.user_name as username,cou from ( ");
					sql.append("select a.dept_id as did,a.user_id uid ,round(sum(a.gxz),1) as cou from zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_");
					if(deptid != null){
						sql.append(" and a.dept_id in ( ");
						if(deptid != 1){
							
							sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
							args.add(deptid);
						}else if(deptid == 1){
							this.rebuildSqlWhenDeptidIs1(sql);
						}
						sql.append(" ) ");
					}
					if(userId != null || user != null){
						sql.append(" and a.user_id in (select id_ from user where user_name like ?) ");
						args.add("%"+user+"%");
					}
					if(begin != null){
						sql.append(" and a.gx_date >= date_format(?,'%Y%m%d') ");
						args.add(sdf.format(begin));
					}
					if(end != null){
						sql.append(" and a.gx_date <= date_format(?,'%Y%m%d') ");
						args.add(sdf.format(end));
					}
					if(content != null){
						sql.append(" and b.zskly_id in ( select t.id_ from zskly t ");
						sql.append(" where t.title like ? ");
						sql.append(" or t.content like ?)");
						args.add("%"+content+"%");
						args.add("%"+content+"%");
					}
					sql.append(" group by a.dept_id,a.user_id )t,user u,dept d ");
					sql.append(" where t.did=d.id_ and u.id_=t.uid");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countDeptZskgxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		
		StringBuffer sql = new StringBuffer("select ifnull((select round(sum(b.gxz),1) from dept a, zsk_gxjl b ");
				sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) and a.id_=b.dept_id),0)as cou,");
				sql.append(" concat( ");
				sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
				sql.append(" ,pt.dept_name) as deptname ");
				sql.append(" from dept pt where pt.id_ in( select d.id_ ");
				sql.append("from dept d,zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_ and d.id_ = a.dept_id ");
				if(deptid != null){
					sql.append(" and a.dept_id in ( ");
					if(deptid != 1){
						sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
						args.add(deptid);
					}else if(deptid == 1){
						this.rebuildSqlWhenDeptidIs1(sql);
					}
					sql.append(" ) ");
				}
				if (begin != null) {
					sql.append(" and a.gx_date >= date_format(?,'%Y%m%d') ");
					args.add(sdf.format(param.get("begin")));
				}
				if (end != null) {
					sql.append(" and a.gx_date <= date_format(?,'%Y%m%d') ");
					args.add(sdf.format(param.get("end")));
				}
				if (content != null) {
					sql.append("and b.zskly_id in (select t.id_  from zskly t ");
					sql.append("where t.title like ? ");
					sql.append("or t.content like ?) ");
					args.add("%"+content+"%");
					args.add("%"+content+"%");
				}
				sql.append(")");
			List list = null;
			if(args.isEmpty()){
				list = this.jdbcTemplate.queryForList(sql.toString());
			}else{
				list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
			}
			return list;
	}

	@Override
	public List countLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		
		StringBuffer sql = new StringBuffer("select ");
					sql.append(" concat( ");
					sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
					sql.append(" ,d.dept_name) ");
					sql.append("  as deptname, ");
					sql.append(" u.user_name as username, ");
					sql.append("cou from (select b.DEPTID as did,b.USER_ID uid ,count(b.id_) as cou ");
					sql.append("from laud_record a ,tktm b where a.zstk_id=b.id_ ");
					sql.append(" and a.`type` = 1 ");
					if(deptid != null){
						sql.append(" and b.deptid in ( ");
						if(deptid != 1){
							sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
							args.add(deptid);
						}else if(deptid == 1){
							this.rebuildSqlWhenDeptidIs1(sql);
						}
						sql.append(" ) ");
					}
					if(user != null){
						sql.append(" and b.user_id in (select id_ from user where user_name like ?) ");
						args.add("%"+user+"%");
					}
					if(begin != null){
						sql.append(" and a.dz_date >= date_format(?,'%Y%m%d') ");
						args.add(sdf.format(begin));
					}
					if(end != null){
						sql.append(" and a.dz_date <= date_format(?,'%Y%m%d') ");
						args.add(sdf.format(end));
					}
					if(content != null){
						sql.append(" and b.tmly_id in (select t.id_ from tmly t ");
						sql.append(" where t.title like ? ");
						sql.append(" or t.content like ?) ");
						args.add("%"+content+"%");
						args.add("%"+content+"%");
					}
					sql.append("group by b.DEPTID,b.USER_ID )t,user u,dept d where t.did=d.id_ and u.id_=t.uid");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countDeptLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		
		StringBuffer sql = new StringBuffer("select ifnull((select count(b.id_)from dept a, laud_record b ");
			sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) and a.id_=b.dept_id and b.`type` = 1),0 )as cou,");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
			sql.append(" ,pt.dept_name) as deptname ");
			sql.append("  from dept pt where pt.id_ in(  ");
			sql.append("select d.id_ from dept d,laud_record a ,tktm b where a.zstk_id=b.id_ and d.id_ = a.dept_id ");
			sql.append(" and a.`type` = 1 ");
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
					args.add(deptid);
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and a.dz_date >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and a.dz_date <= date_format(?,'%Y%m%d')   ");
				args.add(sdf.format(end));
			}
			if(content != null){
				sql.append(" and b.tmly_id in (select t.id_ from tmly t ");
				sql.append(" where t.title like ? ");
				sql.append(" or t.content like ? ");
				args.add("%"+content+"%");
				args.add("%"+content+"%");
			}
			sql.append(")");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countTktmLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		StringBuffer sql = new StringBuffer("select t.content as tmname,count(r.zstk_id) as laudCount ");
			sql.append(" from laud_record r,tktm t where t.id_ = r.zstk_id ");
			sql.append(" and r.`type` = 1 ");
			if(begin != null){
				sql.append(" and r.dz_date >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.dz_date <= date_format(?,'%Y%m%d')   ");
				args.add(sdf.format(end));
			}
			sql.append(" group by r.zstk_id ");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List countUserAnswerCount(Map<String, Object> param) {//answerCount
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		StringBuffer sql = new StringBuffer("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,count(r.user_id) as answerCount ");
			sql.append(" from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_ ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like ?) ");
				args.add("%"+user+"%");
			}
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
					args.add(deptid);
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(end));
			}
			if(tkfl != null){
				sql.append(" and r.TM_ID in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like ?) ");
				args.add("%"+tkfl+"%");
			}
			sql.append(" group by r.user_id ");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List countUserAnswerScore(Map<String, Object> param) {//answerScore
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		StringBuffer sql = new StringBuffer("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,round(sum(r.result_score),1) as answerScore ");
			sql.append(" from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_ ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like ?) ");
				args.add("%"+user+"%");
			}
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
					args.add(deptid);
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(end));
			}
			if(tkfl != null){
				sql.append(" and r.TM_ID in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like ?) ");
				args.add("%"+tkfl+"%");
			}
			sql.append(" group by r.user_id ");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countUserRushAnswerScore(Map<String, Object> param) {//rushAnswerScore
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		StringBuffer sql = new StringBuffer("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,round(sum(r.exam_score),1) as rushAnswerScore ");
			sql.append(" from exam_user_result r, user u, dept d, exam_detail ed, exam e ");
			sql.append(" where r.user_id = u.id_ and u.deptid=d.id_ and r.exam_detail_id = ed.id_ and ed.exam_id = e.id_ and e.exam_type='2' ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like ?) ");
				args.add("%"+user+"%");
			}
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
					args.add(deptid);
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(end));
			}
			if(tkfl != null){
				sql.append(" and ed.tm_id in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like ?) ");
				args.add("%"+tkfl+"%");
			}
			sql.append(" group by r.user_id");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List countUserExamScore(Map<String, Object> param) {//examScore
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String title = (String) param.get("title");
		String tkfl = (String) param.get("tkfl");
		StringBuffer sql = new StringBuffer("select t.title, ");
		sql.append(" t.dept, ");
		sql.append(" t.user,  ");
		sql.append(" round(if(t.examTime >= t.exam_time,t.userScore,0),1) as examScore, ");
		sql.append(" t.examTime ");
		sql.append(" from ( ");
			sql.append(" select e.title, ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");	
			//sql.append(" u.user_name as user,round(sum(if(r.exam_time >= e.exam_time,r.exam_score,0)),1) as examScore, sum(r.EXAM_TIME) as examTime ");
			sql.append(" u.user_name as user, ");
			sql.append(" e.exam_time, ");
			sql.append(" sum(r.exam_score) userScore, ");
			sql.append(" sum(r.EXAM_TIME) as examTime ");
			sql.append(" from exam_user_result r,user u,dept d,exam e,exam_detail ed where r.user_id = u.id_ and u.deptid=d.id_ ");
			sql.append(" and r.exam_detail_id = ed.id_ and ed.exam_id = e.id_ "); 
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like ?) ");
				args.add("%"+user+"%");
			}
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
					args.add(deptid);
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(end));
			}
			if(title != null){
				sql.append(" and e.TITLE like ? ");
				args.add("%"+title+"%");
			}
			if(tkfl != null){
				sql.append(" and ed.tm_id in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like ?) ");
				args.add("%"+tkfl+"%");
			}
			sql.append(" group by r.user_id,e.id_ order by e.title ");
			sql.append(" )t ");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List getExamInfo() {
		// TODO Auto-generated method stub
		String sql = "select id_ id,title,exam_time examTime from exam";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public List getCurrentDeptQjById(String id) {
		// TODO Auto-generated method stub
		String sql = "select id_,dept_name,PARENT_ID parentId,ms from dept t where t.parent_id=? and dept_name like '%局'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[]{ id });
		return list;
	}

	@Override
	public List countDeptCtslCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		//System.out.println("部门出题数量查询");
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		StringBuffer sql = new StringBuffer("select ifnull(");
			sql.append("(select count(b.USER_ID) from dept a, tktm b ");
			sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) ");
			sql.append("and a.id_=b.deptid),0)");
			sql.append("as ctslCount, ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
			sql.append(" ,pt.dept_name) as deptname ");
			sql.append("from dept pt where pt.id_ in(");
			sql.append(" select d.id_");
			sql.append(" from dept d,tktm b");
			sql.append(" where d.id_ = b.deptid");
		if(deptid != null){
			sql.append(" and b.deptid in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
				args.add(deptid);
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if (begin != null) {
			sql.append(" and b.create_date >= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("begin")));
		}
		if (end != null) {
			sql.append(" and b.create_date <= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("end")));
		}
		if (content != null) {
			sql.append(" and b.tmly_id in ");
			sql.append(" (select t.id_ from tmly t where t.title like ?  or t.content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		sql.append(")");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());;
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}

	@Override
	public List countUserCtslCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		//System.out.println("个人出题数量查询");
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		StringBuffer sql = new StringBuffer("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as deptname, ");
		if (userId == null || userId != 0) {
			sql.append("u.user_name as username,");
		}
		sql.append("ctslCount from ");
		sql.append("(select b.deptid as did,b.user_id uid ,count(b.user_id) as ctslCount ");//sum(a.gxz)
		sql.append("from tktm b ");//tk_gxjl a , 
		sql.append("where 1=1 ");
		if(deptid != null){
			sql.append(" and b.deptid in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
				args.add(deptid);
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if(user != null){
			sql.append("and b.user_id in (select id_ from user where user_name like ?) ");
			args.add("%"+user+"%");
		}
		if (begin != null) {
			sql.append("and b.create_date >= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("begin")));
		}
		if (end != null) {
			sql.append("and b.create_date <= date_format(?,'%Y%m%d') ");
			args.add(sdf.format(param.get("end")));
		}
		if (content != null) {
			sql.append("and b.tmly_id in ( select t.id_ from tmly t ");
			sql.append("where t.title like ? ");
			sql.append("or t.content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		sql.append(" group by b.deptid");
		if (userId == null || userId != 0) {
			sql.append(",b.user_id ");
		}
		sql.append(")t, ");
		sql.append("user u,dept d where t.did=d.id_ and u.id_=t.uid");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(), args.toArray());
		}
		return list;
	}

	@Override
	public List countTktmErrRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		StringBuffer sql = new StringBuffer("select t.content as tmname,count(r.zstk_id) as errCount ");
			sql.append(" from laud_record r,tktm t where t.id_ = r.zstk_id ");
			sql.append(" and r.`type` = 2 ");
			if(begin != null){
				sql.append(" and r.dz_date >= date_format(?,'%Y%m%d') ");
				args.add(sdf.format(begin));
			}
			if(end != null){
				sql.append(" and r.dz_date <= date_format(?,'%Y%m%d')   ");
				args.add(sdf.format(end));
			}
			sql.append(" group by r.zstk_id ");
		List list = null;
		if(args.isEmpty()){
			list = this.jdbcTemplate.queryForList(sql.toString());
		}else{
			list = this.jdbcTemplate.queryForList(sql.toString(),args.toArray());
		}
		return list;
	}
	
}
