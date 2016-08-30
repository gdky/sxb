package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YxzskDao;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxzskDaoImpl extends BaseJdbcDao implements YxzskDao {

	@Override
	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder sqlCount = new StringBuilder("");

		if ("SuAdmin".equals(roleName)) {// 超级用户
			sqlCount.append("select count(*) from zsk_jl a where a.yxbz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sqlCount.append("select count(*) from zsk_jl a where a.yxbz='Y' and a.deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sqlCount.append("select count(*) from zsk_jl a where a.yxbz='Y' and a.deptid="
					+ userDetails.getDeptid()
					+ " and a.user_id="
					+ userDetails.getId());
		}

		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),
				Integer.class);
		List<ZskJl> list = this.getYxzskInfo(pageSize * (pageNow - 1),
				pageSize, userDetails, roleName, param);
		page.setEntityCount(count);
		page.setEntities(list);
	}

	private List<ZskJl> getYxzskInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		StringBuilder sql = new StringBuilder(" select d.TITLE zsktitle,b.USER_NAME,c.DEPT_NAME,a.*  ");
			sql.append(" from zsk_jl a,user b,dept c,zskly d ");
			sql.append(" where a.USER_ID = b.ID_ and a.DEPTID = c.ID_ and a.ZSKLY_ID = d.ID_ ");
			sql.append(" and a.yxbz='Y' ");
		if ("SuAdmin".equals(roleName)) {// 超级管理员
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sql.append("and a.deptid=" + userDetails.getDeptid() + " ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sql.append("and a.deptid=" + userDetails.getDeptid()
					+ " and a.user_id=" + userDetails.getId() + " ");
		}
		sql.append(" order by a.create_date desc limit " + begin + "," + offest);
		// String sql = "select * from yxzsk order by create_date desc";
		List<ZskJl> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<ZskJl>() {

					@Override
					public ZskJl mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						ZskJl yxzsk = new ZskJl();
						yxzsk.setZskly(result.getString("zsktitle"));
						yxzsk.setDept(result.getString("DEPT_NAME"));
						yxzsk.setUser(result.getString("USER_NAME"));
						yxzsk.setId(result.getString("id_"));
						yxzsk.setUserId(result.getInt("user_id"));
						yxzsk.setCreateDate(result.getDate("create_date"));
						yxzsk.setSpDate(result.getDate("sp_date"));
						yxzsk.setSprId(result.getInt("spr_id"));
						yxzsk.setDeptid(result.getInt("deptid"));
						yxzsk.setContent(result.getString("content"));
						yxzsk.setZsklyId(result.getInt("zskly_id"));
						yxzsk.setTitle(result.getString("title"));
						yxzsk.setYxbz(result.getString("yxbz"));
						yxzsk.setXybz(result.getString("xybz"));
						return yxzsk;
					}

				});
		return list;
	}

	private void rebuileSqlByConditionAndRole(StringBuilder sql,
			Map<String, Object> param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		String content = (String) param.get("content");
		if (!sql.toString().contains("deptid")) {
			if (deptid != null || dept != null) {
				//sql.append(" and deptid=" + deptid);
				sql.append(" and a.deptid in (select id_ from dept where dept_name like '%"+dept+"%') ");
			}
		}
		if (userId != null || user != null) {
			//sql.append(" and user_id=" + userId);
			sql.append(" and a.user_id in (select id_ from user where user_name like '%"+user+"%') ");
		}
		if (begin != null) {
			sql.append(" and a.create_date >= date_format('" + sdf.format(begin)
					+ "','%Y%m%d')");
		}
		if (end != null) {
			sql.append(" and a.create_date <= date_format('" + sdf.format(end)
					+ "','%Y%m%d')");
		}
		if (content != null) {
			sql.append(" and a.zskly_id in "
					+ "(select id_ from zskly where title like '%" + content
					+ "%' or content like '%" + content + "%') ");
		}
		// return null;
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
			for (Role role : roles) {
				if ("USER".equals(role.getRole_Name())) {// 普通用户
					aRole = role;
				}
			}
		}
		return aRole;
	}

	@Override
	public List<Zskly> getZsklyInfoByZsklyId(Integer id) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("select * from zskly ");
		if (id != null) {
			sql.append("where id_=" + id);
		}
		List<Zskly> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Zskly>() {

					@Override
					public Zskly mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Zskly zskly = new Zskly();
						zskly.setId(result.getInt("id_"));
						zskly.setTitle(result.getString("title"));
						zskly.setContent(result.getString("content"));
						zskly.setAttachment(result.getString("attachment"));
						return zskly;
					}

				});
		return list;
	}

	@Override
	public void addYxzsk(ZskJl yxzsk) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into zsk_jl values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] objs = { yxzsk.getId(), yxzsk.getUserId(),
				sdf.format(yxzsk.getCreateDate()), yxzsk.getSpDate(), yxzsk.getSprId(),
				yxzsk.getDeptid(), yxzsk.getContent(), yxzsk.getZsklyId(),
				yxzsk.getTitle(), "Y", "N" };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void addGrDeptGxJl(ZskJl yxzsk) {
		// TODO Auto-generated method stub
		//String sql = "select value from system_props where id_=110";
		//Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		//sql = "insert into zsk_gxjl values(?,?,?,?,?,?,?)";
		//Object[] objs = { yxzsk.getId(), yxzsk.getDeptid(), yxzsk.getUserId(),
				//yxzsk.getId(), value, 110, yxzsk.getCreateDate() };
		String sql = "insert into zsk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxzsk.getId(), yxzsk.getDeptid(), yxzsk.getUserId(),
		yxzsk.getId(), 1, 1, yxzsk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);

	}

	@Override
	public void deleteYxzsk(ZskJl yxzsk) {
		// TODO Auto-generated method stub
		String sql = "update zsk_jl set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxzsk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(ZskJl yxzsk) {
		// TODO Auto-generated method stub
		String sql = "delete from zsk_gxjl where zsk_id=? and gxly=?";
		Object[] objs = { yxzsk.getId(), 1 };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateYxzsk(ZskJl yxzsk) {
		// TODO Auto-generated method stub
		String sql = "update zsk_jl set " + "user_id=?,create_date=?,sp_date=?,"
				+ "spr_id=?,deptid=?,content=?,zskly_id=?," + "title=? "
				+ "where id_=?";
		Object[] objs = { yxzsk.getUserId(), yxzsk.getCreateDate(),
				yxzsk.getSpDate(), yxzsk.getSprId(), yxzsk.getDeptid(),
				yxzsk.getContent(), yxzsk.getZsklyId(), yxzsk.getTitle(),
				yxzsk.getId() };
		this.jdbcTemplate.update(sql, objs);
	}

}
