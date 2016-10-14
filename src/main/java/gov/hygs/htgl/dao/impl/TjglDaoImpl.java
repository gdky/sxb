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

	private void rebuildSqlWhenDeptidIs1(StringBuilder sb){
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList("select id_ from dept where parent_id = 1");
		List id = new ArrayList();
		if(list != null){
			for(Map<String,Object> map : list){
				List<Map<String,Object>> ids = this.jdbcTemplate.queryForList("select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+map.get("id_")+"))");
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		//StringBuffer sql = new StringBuffer("select d.dept_name");
		StringBuilder sql = new StringBuilder("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as deptname, ");
		if (userId == null || userId != 0) {
			sql.append("u.user_name as username,");
		}
		sql.append("cou from ");
		sql.append("(select a.dept_id as did,a.user_id uid ,sum(a.gxz) as cou ");
		sql.append("from tk_gxjl a , tktm b ");
		sql.append("where a.tk_id=b.id_ ");
		/*if (deptid != null) {
			sql.append("and a.dept_id=" + deptid + " ");
			if (userId != null) {
				if (userId != 0) {
					sql.append("and a.user_id=" + userId + " ");
				}

			}
		}
		if(dept != null){
			sql.append("and a.dept_id in (select id_ from dept where dept_name like '%" + dept + "%') ");
		}
		*/
		if(deptid != null){
			sql.append(" and a.dept_id in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if(user != null){
			sql.append("and a.user_id in (select id_ from user where user_name like '%" + user + "%') ");
		}
		if (begin != null) {
			sql.append("and a.gx_date >= date_format('"
					+ sdf.format(param.get("begin")) + "','%Y%m%d') ");
		}
		if (end != null) {
			sql.append("and a.gx_date <= date_format('"
					+ sdf.format(param.get("end")) + "','%Y%m%d') ");
		}
		sql.append("and b.tmly_id in ( select t.id_ from tmly t ");
		if (content != null) {
			sql.append("where t.title like '%" + content + "%' ");
			sql.append("or t.content like '%" + content + "%' ");
		}
		sql.append(") group by a.dept_id");
		if (userId == null || userId != 0) {
			sql.append(",a.user_id ");
		}
		sql.append(")t, ");
		sql.append("user u,dept d where t.did=d.id_ and u.id_=t.uid");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countDeptGxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		StringBuilder sql = new StringBuilder("select ifnull(");
			sql.append("(select sum(b.gxz) " + "from dept a, tk_gxjl b ");
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
		/*if (deptid == null || param == null) {
			// sql.append(" and d.parent_id is null");
		} else {
			sql.append(" and a.dept_id =" + deptid);
		}*/
		/*
		if(dept != null){
			sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%" + dept+"%') ");
		}
		*/
		if(deptid != null){
			sql.append(" and a.dept_id in ( ");
			if(deptid != 1){
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
			}else if(deptid == 1){
				this.rebuildSqlWhenDeptidIs1(sql);
			}
			sql.append(" ) ");
		}
		if (begin != null) {
			sql.append(" and a.gx_date >= date_format('"
					+ sdf.format(param.get("begin")) + "','%Y%m%d') ");
		}
		if (end != null) {
			sql.append(" and a.gx_date <= date_format('"
					+ sdf.format(param.get("end")) + "','%Y%m%d') ");
		}
		if (content != null) {
			sql.append(" and b.tmly_id in (" + " select t.id_ "
					+ " from tmly t " + " where t.title like '%" + content
					+ "%' " + " or t.content like '%" + content + "%') ");
		}
		sql.append(")");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countZskgxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		
		//StringBuilder sql = new StringBuilder("select d.dept_name as deptname,");
		StringBuilder sql = new StringBuilder("select ");
					sql.append(" concat( ");
					sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
					sql.append(" ,d.dept_name) ");
					sql.append("  as deptname, ");
					sql.append(" u.user_name as username,cou from ( ");
					sql.append("select a.dept_id as did,a.user_id uid ,sum(a.gxz) as cou from zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_");
					/*if(deptid != null || dept != null){
						sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%"+dept+"%') ");
					}*/
					if(deptid != null){
						sql.append(" and a.dept_id in ( ");
						if(deptid != 1){
							
							sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
						}else if(deptid == 1){
							this.rebuildSqlWhenDeptidIs1(sql);
						}
						sql.append(" ) ");
					}
					if(userId != null || user != null){
						sql.append(" and a.user_id in (select id_ from user where user_name like '%"+user+"%') ");
					}
					if(begin != null){
						sql.append(" and a.gx_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
					}
					if(end != null){
						sql.append(" and a.gx_date <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
					}
					if(content != null){
						sql.append(" and b.zskly_id in ( select t.id_ from zskly t ");
						sql.append(" where t.title like '%"+content+"%' ");
						sql.append(" or t.content like '%"+content+"%')");
					}
					sql.append(" group by a.dept_id,a.user_id )t,user u,dept d ");
					sql.append(" where t.did=d.id_ and u.id_=t.uid");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countDeptZskgxjl(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		
		StringBuilder sql = new StringBuilder("select ifnull((select sum(b.gxz) from dept a, zsk_gxjl b ");
				sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) and a.id_=b.dept_id),0)as cou,");
				sql.append(" concat( ");
				sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
				sql.append(" ,pt.dept_name) as deptname ");
				//sql.append("pt.dept_name as deptname  ");
				sql.append(" from dept pt where pt.id_ in( select d.id_ ");
				sql.append("from dept d,zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_ and d.id_ = a.dept_id ");
				//if(dept != null && param != null){
				/*
				if(dept != null){
					sql.append("and b.deptid in (select id_ from dept where dept_name like '%"+dept+"%') ");
				}
				*/
				if(deptid != null){
					sql.append(" and a.dept_id in ( ");
					if(deptid != 1){
						sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
					}else if(deptid == 1){
						this.rebuildSqlWhenDeptidIs1(sql);
					}
					sql.append(" ) ");
				}
				if (begin != null) {
					sql.append(" and a.gx_date >= date_format('"
							+ sdf.format(param.get("begin")) + "','%Y%m%d') ");
				}
				if (end != null) {
					sql.append(" and a.gx_date <= date_format('"
							+ sdf.format(param.get("end")) + "','%Y%m%d') ");
				}
				if (content != null) {
					sql.append("and b.zskly_id in (select t.id_  from zskly t ");
					sql.append("where t.title like '%"+content+"%' ");
					sql.append("or t.content like '%"+content+"%') ");
				}
				sql.append(")");
			List list = this.jdbcTemplate.queryForList(sql.toString());
			return list;
	}

	@Override
	public List countLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		
		//StringBuilder sql = new StringBuilder("select d.dept_name as deptname,");
		StringBuilder sql = new StringBuilder("select ");
					sql.append(" concat( ");
					sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
					sql.append(" ,d.dept_name) ");
					sql.append("  as deptname, ");
					sql.append(" u.user_name as username, ");
					sql.append("cou from (select b.DEPTID as did,b.USER_ID uid ,count(b.id_) as cou ");
					sql.append("from laud_record a ,tktm b where a.zstk_id=b.id_ ");
					/*if(dept != null){
						sql.append(" and b.DEPTID in (select id_ from dept where dept_name like '%"+dept+"%') ");
					}*/
					if(deptid != null){
						sql.append(" and b.deptid in ( ");
						if(deptid != 1){
							sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
						}else if(deptid == 1){
							this.rebuildSqlWhenDeptidIs1(sql);
						}
						sql.append(" ) ");
					}
					if(user != null){
						sql.append(" and b.user_id in (select id_ from user where user_name like '%"+user+"%') ");
					}
					if(begin != null){
						//sql.append(" and b.create_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
						sql.append(" and a.dz_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
					}
					if(end != null){
						//sql.append(" and b.create_date <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
						sql.append(" and a.dz_date <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
					}
					if(content != null){
						sql.append(" and b.tmly_id in (select t.id_ from tmly t ");
						sql.append(" where t.title like '%"+content+"%' ");
						sql.append(" or t.content like '%"+content+"%') ");
					}
					sql.append("group by b.DEPTID,b.USER_ID )t,user u,dept d where t.did=d.id_ and u.id_=t.uid");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countDeptLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = param == null ? null : (Integer) param.get("deptid");
		String dept =  param == null?null : (String)param.get("dept");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		String content = param == null ? null : (String) param.get("content");
		
		StringBuilder sql = new StringBuilder("select ifnull((select count(b.id_)from dept a, laud_record b ");
			sql.append("where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) and a.id_=b.dept_id),0 )as cou,");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=pt.PARENT_ID),'') ");
			sql.append(" ,pt.dept_name) as deptname ");
			//sql.append(" pt.dept_name as deptname ");
			sql.append("  from dept pt where pt.id_ in(  ");
			sql.append("select d.id_ from dept d,laud_record a ,tktm b where a.zstk_id=b.id_ and d.id_ = a.dept_id ");//dept_id
			/*if(dept != null){
				sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}*/
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				//sql.append(" and b.create_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
				sql.append(" and a.dz_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				//sql.append(" and b.create_date <= date_format('"+end+"','%Y%m%d')   ");
				sql.append(" and a.dz_date <= date_format('"+sdf.format(end)+"','%Y%m%d')   ");
			}
			if(content != null){
				sql.append(" and b.tmly_id in (select t.id_ from tmly t ");
				sql.append(" where t.title like '%"+content+"%' ");
				sql.append(" or t.content like '%"+content+"%') ");
			}
			sql.append(")");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countTktmLaudRecord(Map<String, Object> param) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = param == null ? null : (Date) param.get("begin");
		Date end = param == null ? null : (Date) param.get("end");
		//String sql = "select t.content as tmname,count(r.zstk_id) as laudCount "
				//+ "from laud_record r,tktm t where t.id_ = r.zstk_id group by r.zstk_id";
		StringBuilder sql = new StringBuilder("select t.content as tmname,count(r.zstk_id) as laudCount ");
			sql.append(" from laud_record r,tktm t where t.id_ = r.zstk_id ");
			if(begin != null){
				//sql.append(" and b.create_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
				sql.append(" and r.dz_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				//sql.append(" and b.create_date <= date_format('"+end+"','%Y%m%d')   ");
				sql.append(" and r.dz_date <= date_format('"+sdf.format(end)+"','%Y%m%d')   ");
			}
			sql.append(" group by r.zstk_id ");
		List list =  this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserAnswerCount(Map<String, Object> param) {//answerCount
		// TODO Auto-generated method stub
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		//Date begintime = (Date) param.get("begintime");
		//Date endtime = (Date) param.get("endtime");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		//StringBuilder sql = new StringBuilder("select d.dept_name as dept, ");
		StringBuilder sql = new StringBuilder("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,count(r.user_id) as answerCount ");
			sql.append(" from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_ ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			/*if(dept != null){
				sql.append(" and d.id_ in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
			if(begintime != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begintime)+"','%Y%m%d%H%i%s') ");
			}
			if(endtime != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(endtime)+"','%Y%m%d%H%i%s')   ");
			}*/
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
			}
			if(tkfl != null){
				sql.append(" and r.TM_ID in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like '%"+tkfl+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserAnswerScore(Map<String, Object> param) {//answerScore
		// TODO Auto-generated method stub
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		//Date begintime = (Date) param.get("begintime");
		//Date endtime = (Date) param.get("endtime");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		//StringBuilder sql = new StringBuilder("select d.dept_name as dept,");
		StringBuilder sql = new StringBuilder("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,sum(r.result_score) as answerScore ");
			sql.append(" from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_ ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			/*if(dept != null){
				sql.append(" and d.id_ in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
			if(begintime != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begintime)+"','%Y%m%d%H%i%s') ");
			}
			if(endtime != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(endtime)+"','%Y%m%d%H%i%s')   ");
			}*/
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
			}
			if(tkfl != null){
				sql.append(" and r.TM_ID in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like '%"+tkfl+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserRushAnswerScore(Map<String, Object> param) {//rushAnswerScore
		// TODO Auto-generated method stub
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		//Date begintime = (Date) param.get("begintime");
		//Date endtime = (Date) param.get("endtime");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String tkfl = (String) param.get("tkfl");
		StringBuilder sql = new StringBuilder("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");
			sql.append(" u.user_name as user,sum(r.exam_score) as rushAnswerScore ");
			sql.append(" from exam_user_result r, user u, dept d, exam_detail ed, exam e ");
			sql.append(" where r.user_id = u.id_ and u.deptid=d.id_ and r.exam_detail_id = ed.id_ and ed.exam_id = e.id_ and e.exam_type='2' ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			/*if(dept != null){
				sql.append(" and d.id_ in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
			if(begintime != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begintime)+"','%Y%m%d%H%i%s') ");
			}
			if(endtime != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(endtime)+"','%Y%m%d%H%i%s')   ");
			}*/
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
			}
			if(tkfl != null){
				sql.append(" and ed.tm_id in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like '%"+tkfl+"%') ");
			}
			sql.append(" group by r.user_id");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserExamScore(Map<String, Object> param) {//examScore
		// TODO Auto-generated method stub
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String user = (String) param.get("user");
		String dept = (String) param.get("dept");
		Integer deptid = (Integer) param.get("deptid");
		//Date begintime = (Date) param.get("begintime");
		//Date endtime = (Date) param.get("endtime");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String title = (String) param.get("title");
		String tkfl = (String) param.get("tkfl");
		StringBuilder sql = new StringBuilder("select ");
			sql.append(" concat( ");
			sql.append(" ifnull((select dept_name from dept where id_=d.PARENT_ID),'') ");
			sql.append(" ,d.dept_name) ");
			sql.append("  as dept, ");	
			sql.append(" u.user_name as user,sum(r.exam_score) as examScore ");
			sql.append(" from exam_user_result r,user u,dept d,exam e,exam_detail ed where r.user_id = u.id_ and u.deptid=d.id_ ");
			sql.append(" and r.exam_detail_id = ed.id_ and ed.exam_id = e.id_ ");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			/*if(dept != null){
				sql.append(" and d.id_ in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
			if(begintime != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begintime)+"','%Y%m%d%H%i%s') ");
			}
			if(endtime != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(endtime)+"','%Y%m%d%H%i%s')   ");
			}*/
			if(deptid != null){
				sql.append(" and d.id_ in ( ");
				if(deptid != 1){
					sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo("+deptid+")) ");
				}else if(deptid == 1){
					this.rebuildSqlWhenDeptidIs1(sql);
				}
				sql.append(" ) ");
			}
			if(begin != null){
				sql.append(" and r.start_time >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				sql.append(" and r.end_time <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
			}
			if(title != null){
				//sql.append(" and r.EXAM_DETAIL_ID in (select d.ID_ from exam e, exam_detail d where e.ID_ = d.EXAM_ID and e.TITLE like '%"+title+"%') ");
				sql.append(" and e.TITLE like '%"+title+"%' ");
			}
			if(tkfl != null){
				sql.append(" and ed.tm_id in (select t.ID_ from tktm t, tkfl l where t.FL_ID = l.ID_ and l.TKMC like '%"+tkfl+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List getExamInfo() {
		// TODO Auto-generated method stub
		String sql = "select id_ id,title from exam";
		return this.jdbcTemplate.queryForList(sql);
	}

	@Override
	public List getCurrentDeptQjById(String id) {
		// TODO Auto-generated method stub
		//String sql = "select id_,dept_name,PARENT_ID parentId,ms from dept t where t.parent_id=? and dept_name like '%局%'";
		//String sql = "select id_,dept_name,PARENT_ID parentId,ms from dept t where t.parent_id=? and exists(select * from dept where parent_id = t.ID_)";
		String sql = "select id_,dept_name,PARENT_ID parentId,ms from dept t where t.parent_id=? and dept_name like '%局'";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, new Object[]{ id });
		return list;
	}
	
}
