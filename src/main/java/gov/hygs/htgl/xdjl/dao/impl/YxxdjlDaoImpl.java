package gov.hygs.htgl.xdjl.dao.impl;

import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.xdjl.dao.YxxdjlDao;
import gov.hygs.htgl.xdjl.entity.XdjlJl;
import gov.hygs.htgl.xdjl.entity.Xdjlly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxxdjlDaoImpl extends BaseJdbcDao implements YxxdjlDao {

	@Override
	public void getYxzskInfo(Page<XdjlJl> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuffer sqlCount = new StringBuffer("");

		if ("SuAdmin".equals(roleName)) {// 超级用户
			sqlCount.append("select count(*) from xdjl_jl a,user b,dept c,xdjlly d");
			sqlCount.append(" where a.USER_ID = b.ID_ and a.DEPTID = c.ID_ and a.xdjlLY_ID = d.ID_ and a.yxbz='Y' ");
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sqlCount, param);
				if(arg != null){
					args.addAll(arg);
				}
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sqlCount.append("select count(*) from xdjl_jl a,user b,dept c,xdjlly d");
			sqlCount.append(" where a.USER_ID = b.ID_ and a.DEPTID = c.ID_ and a.xdjlLY_ID = d.ID_ and a.yxbz='Y' and a.deptid=?");
			args.add(userDetails.getDeptid());
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sqlCount, param);
				if(arg != null){
					args.add(arg);
				}
			}
		} else if ("Other".equals(roleName)) {// 普通用户
			sqlCount.append("select count(*) from xdjl_jl a,user b,dept c,xdjlly d");
				sqlCount.append(" where a.USER_ID = b.ID_ and a.DEPTID = c.ID_ and a.xdjlLY_ID = d.ID_ and a.yxbz='Y' and a.deptid=?");
				sqlCount.append(" and a.user_id=?");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}

		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),args.toArray(),
				Integer.class);
		List<XdjlJl> list = this.getYxzskInfo(pageSize * (pageNow - 1),
				pageSize, userDetails, roleName, param);
		page.setEntityCount(count);
		page.setEntities(list);
	}

	private List<XdjlJl> getYxzskInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" select d.TITLE xdjltitle,b.USER_NAME,c.DEPT_NAME,a.*  ");
			sql.append(" from xdjl_jl a,user b,dept c,xdjlly d ");
			sql.append(" where a.USER_ID = b.ID_ and a.DEPTID = c.ID_ and a.xdjlly_id = d.ID_ ");
			sql.append(" and a.yxbz='Y' ");
		if ("SuAdmin".equals(roleName)) {// 超级管理员
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sql, param);
				if(arg != null){
					args.addAll(arg);
				}
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sql.append("and a.deptid=? ");
			args.add(userDetails.getDeptid());
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sql, param);
				if(arg != null){
					args.add(arg);
				}
			}
		} else if ("Other".equals(roleName)) {// 普通用户
			sql.append("and a.deptid=? and a.user_id=? ");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		sql.append(" order by a.create_date desc limit ?,?");
		args.add(begin);
		args.add(offest);
		List<XdjlJl> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
				new RowMapper<XdjlJl>() {

					@Override
					public XdjlJl mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						XdjlJl yxxdjl = new XdjlJl();
						yxxdjl.setXdjlly(result.getString("xdjltitle"));
						yxxdjl.setDept(result.getString("DEPT_NAME"));
						yxxdjl.setUser(result.getString("USER_NAME"));
						yxxdjl.setId(result.getString("id_"));
						yxxdjl.setUserId(result.getInt("user_id"));
						yxxdjl.setCreateDate(result.getDate("create_date"));
						yxxdjl.setSpDate(result.getDate("sp_date"));
						yxxdjl.setSprId(result.getInt("spr_id"));
						yxxdjl.setDeptid(result.getInt("deptid"));
						yxxdjl.setContent(result.getString("content"));
						yxxdjl.setXdjllyId(result.getInt("xdjlly_id"));
						yxxdjl.setTitle(result.getString("title"));
						yxxdjl.setYxbz(result.getString("yxbz"));
						yxxdjl.setXybz(result.getString("xybz"));
						return yxxdjl;
					}

				});
		return list;
	}

	private List<Object> rebuileSqlByConditionAndRole(StringBuffer sql,
			Map<String, Object> param) {
		List<Object> args = new ArrayList<Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		String content = (String) param.get("content");
		if(deptid != null){
			
			if(deptid != 1){
				sql.append(" and a.deptid in ( ");
				sql.append(" select a.id_ from dept a where find_in_set(a.id_,queryChildrenAreaInfo(?)) ");
				sql.append(" ) ");
				args.add(deptid);
			}else if(deptid == 1){
				sql.append(" and a.deptid != 2 ");
				sql.append(" and a.deptid != 307 ");
			}
			
		}
		if (userId != null || user != null) {
			sql.append(" and a.user_id in (select id_ from user where user_name like ?) ");
			args.add("%"+user+"%");
		}
		if (begin != null) {
			sql.append(" and a.create_date >= date_format(?,'%Y%m%d')");
			args.add(sdf.format(begin));
		}
		if (end != null) {
			sql.append(" and a.create_date <= date_format(?,'%Y%m%d')");
			args.add(sdf.format(end));
		}
		if (content != null) {
			sql.append(" and a.xdjlly_id in ");
			sql.append("(select id_ from xdjlly where title like ? or content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		return args;
	}

	private Role getRoleInfoByUserId(Integer id) {
		String sql = "select a.* from role a,user_role b where a.id_=b.role_id and user_id=? order by id_";
		List<Role> roles = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Role>() {

					@Override
					public Role mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Role role = new Role();
						role.setRole_Name(result.getString("role_name"));
						role.setMs(result.getString("ms"));
						return role;
					}

				});

		return this.chackRolePower(roles);
	}

	private Role chackRolePower(List<Role> roles) {
		Role aRole = null;
		if (aRole == null) {
			for (Role role : roles) {
				if ("SuAdmin".equals(role.getRole_Name())) {// 超级管理员
					aRole = role;
				}
			}
		}
		if (aRole == null) {
			for (Role role : roles) {
				if ("DeptAdmin".equals(role.getRole_Name())) {// 部门管理员
					aRole = role;
				}
			}
		}
		if (aRole == null) {
			aRole = roles.get(0);
			aRole.setRole_Name("Other");
		}
		return aRole;
	}

	@Override
	public List<Xdjlly> getZsklyInfoByZsklyId(Integer id) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from xdjlly ");
		if (id != null) {
			sql.append("where id_=?");
			args.add(id);
		}
		List<Xdjlly> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
				new RowMapper<Xdjlly>() {

					@Override
					public Xdjlly mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Xdjlly xdjlly = new Xdjlly();
						xdjlly.setId(result.getInt("id_"));
						xdjlly.setTitle(result.getString("title"));
						xdjlly.setContent(result.getString("content"));
						xdjlly.setAttachment(result.getString("attachment"));
						return xdjlly;
					}

				});
		return list;
	}

	@Override
	public void addYxzsk(XdjlJl yxxdjl) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into xdjl_jl values(?,?,?,?,?,?,?,?,?,?,?)";
		int xdjllyid = this.getZsklyInfoOrAddZskly(yxxdjl.getXdjlly(), yxxdjl.getXdjllyContent());
		Object[] objs = { yxxdjl.getId(), yxxdjl.getUserId(),
				sdf.format(yxxdjl.getCreateDate()), yxxdjl.getSpDate(), yxxdjl.getSprId(),
				yxxdjl.getDeptid(), yxxdjl.getContent(), xdjllyid,
				yxxdjl.getTitle(), "Y", "N" };
		this.jdbcTemplate.update(sql, objs);
	}
	
	private int getZsklyInfoOrAddZskly(String title, String content){
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select if(count(*),id_,0) from xdjlly where title=? ");
		args.add(title);
		if(content != null){
			sql.append(" and content=? ");
			args.add(content);
		}
		int xdjllyid = this.jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Integer.class);
		if(xdjllyid == 0){
			xdjllyid = this.addZskly(title, content);
		}
		return xdjllyid;
	}
	
	private int addZskly(String title, String content) {
		String sql = "insert into xdjlly values(?,?,?,?)";
		return this.insertAndGetKeyByJdbc(sql,
				new Object[] { null, title, content, null },
				new String[] { "id_" }).intValue();
	}

	@Override
	public void addGrDeptGxJl(XdjlJl yxxdjl) {
		// TODO Auto-generated method stub
		String sql = "insert into xdjl_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxxdjl.getId(), yxxdjl.getDeptid(), yxxdjl.getUserId(),
		yxxdjl.getId(), this.getGxjlValueByKey("GxzA2"), 1, yxxdjl.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);

	}
	
	private String getGxjlValueByKey(String key){
		String sql = "select value from system_props where key_=?";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{key}, String.class);
	}

	@Override
	public void deleteYxzsk(XdjlJl yxxdjl) {
		// TODO Auto-generated method stub
		String sql = "update xdjl_jl set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxxdjl.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(XdjlJl yxxdjl) {
		// TODO Auto-generated method stub
		String sql = "delete from xdjl_gxjl where xdjl_id=?";
		Object[] objs = { yxxdjl.getId() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateYxzsk(XdjlJl yxxdjl) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("update xdjl_jl set user_id=?,create_date=?,sp_date=?,");
				sql.append("spr_id=?,deptid=?,content=?,xdjlly_id=?,title=? ");
				sql.append("where id_=?");
		Object[] objs = { yxxdjl.getUserId(), yxxdjl.getCreateDate(),
				yxxdjl.getSpDate(), yxxdjl.getSprId(), yxxdjl.getDeptid(),
				yxxdjl.getContent(), yxxdjl.getXdjllyId(), yxxdjl.getTitle(),
				yxxdjl.getId() };
		this.jdbcTemplate.update(sql.toString(), objs);
	}

}
