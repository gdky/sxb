package gov.hygs.htgl.dao.impl;

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

import gov.hygs.htgl.dao.YxzskDao;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;

@Repository
public class YxzskDaoImpl extends BaseJdbcDao implements YxzskDao {

	@Override
	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder sqlCount = new StringBuilder("");

		if ("SuAdmin".equals(roleName)) {// 超级用户
			sqlCount.append("select count(*) from yxzsk where yxbz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sqlCount.append("select count(*) from yxzsk where yxbz='Y' and deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sqlCount.append("select count(*) from yxzsk where yxbz='Y' and deptid="
					+ userDetails.getDeptid()
					+ " and user_id="
					+ userDetails.getId());
		}

		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),
				Integer.class);
		List<Yxzsk> list = this.getYxzskInfo(pageSize * (pageNow - 1),
				pageSize, userDetails, roleName, param);
		page.setEntityCount(count);
		page.setEntities(list);
	}

	private List<Yxzsk> getYxzskInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		StringBuilder sql = new StringBuilder("");

		sql.append("select * from yxzsk where yxbz='Y' ");
		if ("SuAdmin".equals(roleName)) {// 超级管理员
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sql.append("and deptid=" + userDetails.getDeptid() + " ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sql.append("and deptid=" + userDetails.getDeptid()
					+ " and user_id=" + userDetails.getId() + " ");
		}
		sql.append(" order by create_date desc limit " + begin + "," + offest);
		// String sql = "select * from yxzsk order by create_date desc";
		List<Yxzsk> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Yxzsk>() {

					@Override
					public Yxzsk mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Yxzsk yxzsk = new Yxzsk();
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
		String content = (String) param.get("content");
		if (!sql.toString().contains("deptid")) {
			if (deptid != null) {
				sql.append(" and deptid=" + deptid);
			}
		}
		if (userId != null) {
			sql.append(" and user_id=" + userId);
		}
		if (begin != null) {
			sql.append(" and create_date >= date_format('" + sdf.format(begin)
					+ "','%Y%m%d')");
		}
		if (end != null) {
			sql.append(" and create_date <= date_format('" + sdf.format(end)
					+ "','%Y%m%d')");
		}
		if (content != null) {
			sql.append(" and zskly_id in "
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
	public void addYxzsk(Yxzsk yxzsk) {
		// TODO Auto-generated method stub
		String sql = "insert into yxzsk values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] objs = { yxzsk.getId(), yxzsk.getUserId(),
				yxzsk.getCreateDate(), yxzsk.getSpDate(), yxzsk.getSprId(),
				yxzsk.getDeptid(), yxzsk.getContent(), yxzsk.getZsklyId(),
				yxzsk.getTitle(), "Y", "N" };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void addGrDeptGxJl(Yxzsk yxzsk) {
		// TODO Auto-generated method stub
		String sql = "select value from system_props where id_=110";
		Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "insert into dept_zsk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxzsk.getId(), yxzsk.getDeptid(), yxzsk.getUserId(),
				yxzsk.getId(), value, 110, yxzsk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
		sql = "insert into gr_zsk_gxjl values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, objs);

	}

	@Override
	public void deleteYxzsk(Yxzsk yxzsk) {
		// TODO Auto-generated method stub
		String sql = "update yxzsk set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxzsk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(Yxzsk yxzsk) {
		// TODO Auto-generated method stub
		String sql = "delete from dept_zsk_gxjl where zsk_id=? and gxly=?";
		Object[] objs = { yxzsk.getId(), 110 };
		this.jdbcTemplate.update(sql, objs);
		sql = "delete from gr_zsk_gxjl where zsk_id=? and gxly=?";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateYxzsk(Yxzsk yxzsk) {
		// TODO Auto-generated method stub
		String sql = "update yxzsk set " + "user_id=?,create_date=?,sp_date=?,"
				+ "spr_id=?,deptid=?,content=?,zskly_id=?," + "title=? "
				+ "where id_=?";
		Object[] objs = { yxzsk.getUserId(), yxzsk.getCreateDate(),
				yxzsk.getSpDate(), yxzsk.getSprId(), yxzsk.getDeptid(),
				yxzsk.getContent(), yxzsk.getZsklyId(), yxzsk.getTitle(),
				yxzsk.getId() };
		this.jdbcTemplate.update(sql, objs);
	}

}
