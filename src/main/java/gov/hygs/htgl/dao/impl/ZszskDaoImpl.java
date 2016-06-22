package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.ZszskDao;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.Zszsk;
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
public class ZszskDaoImpl extends BaseJdbcDao implements ZszskDao {

	@Override
	public void getZszskInfo(Page<Zszsk> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder sqlCount = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 超级用户
			sqlCount.append("select count(*) from zszsk where yxbz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sqlCount.append("select count(*) from zszsk where yxbz='Y' and deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sqlCount.append("select count(*) from zszsk where yxbz='Y' and deptid="
					+ userDetails.getDeptid()
					+ " and user_id="
					+ userDetails.getId());
		}
		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),
				Integer.class);
		List<Zszsk> list = this.getYxzskInfo(pageSize * (pageNow - 1),
				pageSize, userDetails, roleName, param);
		page.setEntityCount(count);
		page.setEntities(list);
	}

	private List<Zszsk> getYxzskInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {

		StringBuilder sql = new StringBuilder("");
		sql.append("select * from zszsk where yxbz='Y' ");
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
		List<Zszsk> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Zszsk>() {

					@Override
					public Zszsk mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Zszsk zszsk = new Zszsk();
						zszsk.setId(result.getString("id_"));
						zszsk.setUserId(result.getInt("user_id"));
						zszsk.setCreateDate(result.getDate("create_date"));
						zszsk.setSpDate(result.getDate("sp_date"));
						zszsk.setSprId(result.getInt("spr_id"));
						zszsk.setDeptid(result.getInt("deptid"));
						zszsk.setContent(result.getString("content"));
						zszsk.setZsklyId(result.getInt("zskly_id"));
						zszsk.setTitle(result.getString("title"));
						zszsk.setYxbz(result.getString("yxbz"));
						return zszsk;
					}

				});
		return list;
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

	@Override
	public void addYxzskToZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		int count = this.chackRecordExistOrNot(zszsk);
		if (count != 0) {
			String sql = "update zszsk set yxbz='Y' where id_=?";
			this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
			sql = "update yxzsk set xybz='Y' where id_=?";
			this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
		} else {
			String sql = "update yxzsk set xybz='Y' where id_=?";
			this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
		}
	}

	private Integer chackRecordExistOrNot(Zszsk zszsk) {
		String sql = "select count(*) from zszsk where id_=?";
		int count = this.jdbcTemplate.queryForObject(sql,
				new Object[] { zszsk.getId() }, Integer.class);
		return count;
	}

	@Override
	public void addZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		if (this.chackRecordExistOrNot(zszsk) == 0) {
			String sql = "insert into zszsk values(?,?,?,?,?,?,?,?,?,?)";
			Object[] objs = { zszsk.getId(), zszsk.getUserId(),
					zszsk.getCreateDate(), zszsk.getSpDate(), zszsk.getSprId(),
					zszsk.getDeptid(), zszsk.getContent(), zszsk.getZsklyId(),
					zszsk.getTitle(), "Y" };
			this.jdbcTemplate.update(sql, objs);
		}
	}

	@Override
	public void addGrDeptGxJl(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "select value from system_props where id_=111";
		Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "insert into dept_zsk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { zszsk.getContent(), zszsk.getDeptid(),
				zszsk.getUserId(), zszsk.getId(), value, 111,
				zszsk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
		sql = "insert into gr_zsk_gxjl values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "update zszsk set " + "user_id=?,create_date=?,sp_date=?,"
				+ "spr_id=?,deptid=?,content=?,zskly_id=?," + "title=? "
				+ "where id_=?";
		Object[] objs = { zszsk.getUserId(), zszsk.getCreateDate(),
				zszsk.getSpDate(), zszsk.getSprId(), zszsk.getDeptid(),
				zszsk.getContent(), zszsk.getZsklyId(), zszsk.getTitle(),
				zszsk.getId() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void deleteYxzskFormZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "update yxzsk set xybz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
	}

	@Override
	public void deleteZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "update zszsk set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "delete from dept_zsk_gxjl where zsk_id=? and gxly=?";
		Object[] objs = { zszsk.getId(), 111 };
		this.jdbcTemplate.update(sql, objs);
		sql = "delete from gr_zsk_gxjl where zsk_id=? and gxly=?";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void getYxzskInfo(Page<Yxzsk> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String count = "select count(*) from yxzsk where yxbz='Y' and xybz='N'";
		int entityCount = this.jdbcTemplate
				.queryForObject(count, Integer.class);
		List<Yxzsk> list = this
				.getYxzskInfo(pageSize * (pageNow - 1), pageSize);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Yxzsk> getYxzskInfo(int begin, int offest) {
		String sql = "select * from yxzsk where yxbz='Y' and xybz='N' limit ?,?";
		List<Yxzsk> list = this.jdbcTemplate.query(sql, new Object[] { begin,
				offest }, new RowMapper<Yxzsk>() {

			@Override
			public Yxzsk mapRow(ResultSet result, int i) throws SQLException {
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

}
