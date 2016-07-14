package gov.hygs.htgl.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

import gov.hygs.htgl.dao.TjglDao;
@Repository
public class TjglDaoImpl extends BaseJdbcDao implements TjglDao {

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
		StringBuffer sql = new StringBuffer("select d.dept_name as deptname,");
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
		}*/
		if(dept != null){
			sql.append("and a.dept_id in (select id_ from dept where dept_name like '%" + dept + "%') ");
			if(user != null){
				sql.append("and a.user_id in (select id_ from user where user_name like '%" + user + "%') ");
			}
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
		StringBuffer sql = new StringBuffer("select ifnull("
				+ "(select sum(b.gxz) " + "from dept a, tk_gxjl b "
				+ "where find_in_set(a.id_,queryChildrenAreaInfo(pt.id_)) "
				+ "and a.id_=b.dept_id),0)"
				+ "as cou,pt.dept_name as deptname "
				+ "from dept pt where pt.id_ in(" + " select d.id_"
				+ " from dept d,tk_gxjl a ," + " tktm b"
				+ " where a.tk_id=b.id_" + " and d.id_ = a.dept_id");
		/*if (deptid == null || param == null) {
			// sql.append(" and d.parent_id is null");
		} else {
			sql.append(" and a.dept_id =" + deptid);
		}*/
		if(dept != null){
			sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%" + dept+"%') ");
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
		
		StringBuilder sql = new StringBuilder("select d.dept_name as deptname,u.user_name as username,cou from (");
					sql.append("select a.dept_id as did,a.user_id uid ,sum(a.gxz) as cou from zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_");
					if(deptid != null || dept != null){
						sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%"+dept+"%') ");
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
				sql.append("pt.dept_name as deptname from dept pt where pt.id_ in( select d.id_ ");
				sql.append("from dept d,zsk_gxjl a ,zsk_jl b where a.zsk_id=b.id_ and d.id_ = a.dept_id ");
				//if(dept != null && param != null){
				if(dept != null){
					sql.append("and b.deptid in (select id_ from dept where dept_name like '%"+dept+"%') ");
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
		
		StringBuilder sql = new StringBuilder("select d.dept_name as deptname,u.user_name as username,");
					sql.append("cou from (select a.dept_id as did,a.user_id uid ,count(a.id_) as cou ");
					sql.append("from laud_record a ,tktm b where a.zstk_id=b.id_ ");
					if(dept != null){
						sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%"+dept+"%') ");
					}
					if(user != null){
						sql.append(" and a.user_id in (select id_ from user where user_name like '%"+user+"%') ");
					}
					if(begin != null){
						sql.append(" and b.create_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
					}
					if(end != null){
						sql.append(" and b.create_date <= date_format('"+sdf.format(end)+"','%Y%m%d') ");
					}
					if(content != null){
						sql.append(" and b.tmly_id in (select t.id_ from tmly t ");
						sql.append(" where t.title like '%"+content+"%' ");
						sql.append(" or t.content like '%"+content+"%') ");
					}
					sql.append("group by a.dept_id,a.user_id )t,user u,dept d where t.did=d.id_ and u.id_=t.uid");
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
			sql.append("pt.dept_name as deptname from dept pt where pt.id_ in( ");
			sql.append("select d.id_ from dept d,laud_record  a ,tktm b where a.zstk_id=b.id_ and d.id_ = a.dept_id ");
			if(dept != null){
				sql.append(" and a.dept_id in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
			if(begin != null){
				sql.append(" and b.create_date >= date_format('"+sdf.format(begin)+"','%Y%m%d') ");
			}
			if(end != null){
				sql.append(" and b.create_date <= date_format('"+end+"','%Y%m%d')   ");
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
		String sql = "select t.content as tmname,count(r.zstk_id) as laudCount "
				+ "from laud_record r,tktm t where t.id_ = r.zstk_id group by r.zstk_id";
		List list =  this.jdbcTemplate.queryForList(sql);
		return list;
	}

	@Override
	public List countUserAnswerCount(Map<String, Object> param) {//answerCount
		// TODO Auto-generated method stub
		String user = (String) param.get("user");
		
		StringBuilder sql = new StringBuilder("select d.dept_name as dept,u.user_name as user,count(r.user_id) as answerCount "
				+ "from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserAnswerScore(Map<String, Object> param) {//answerScore
		// TODO Auto-generated method stub
		String user = (String) param.get("user");
		
		StringBuilder sql = new StringBuilder("select d.dept_name as dept,u.user_name as user,sum(r.result_score) as answerScore "
				+ "from user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserRushAnswerScore(Map<String, Object> param) {//rushAnswerScore
		// TODO Auto-generated method stub
		//user_result 通过tm_id关联exam_detail,在通过tm_id关联exam,筛选出考试类型为抢答的结果
		//问题：假如user_result的一条应该是"正式"的题目刚好在exam表中存在对应该题目且考试类型为"正式"和"抢答"两条记录，查询是否会出错？
		String user = (String) param.get("user");
		
		StringBuilder sql = new StringBuilder("select d.dept_name as dept,u.user_name as user,sum(r.exam_score) as rushAnswerScore "+
					"from exam_user_result r, user u, dept d, exam_detail ed, exam e "+
					"where r.user_id = u.id_ and u.deptid=d.id_ and r.exam_detail_id = ed.id_ and ed.exam_id = e.id_ and e.exam_type='2'");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			sql.append(" group by r.user_id");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	@Override
	public List countUserExamScore(Map<String, Object> param) {//examScore
		// TODO Auto-generated method stub
		String user = (String) param.get("user");
		
		StringBuilder sql = new StringBuilder("select d.dept_name as dept,u.user_name as user,sum(r.exam_score) as examScore "
				+ "from exam_user_result r,user u,dept d where r.user_id = u.id_ and u.deptid=d.id_");
			if(user != null){
				sql.append(" and r.user_id in (select id_ from user where user_name like '%"+user+"%') ");
			}
			sql.append(" group by r.user_id ");
		List list = this.jdbcTemplate.queryForList(sql.toString());
		return list;
	}
	
}
