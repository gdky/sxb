package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxtkglDaoImpl extends BaseJdbcDao implements YxtkglDao {

	@Override
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder count = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 超级用户

			count.append("select count(*) from yxtk where yxbz='Y'");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(count, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员

			count.append("select count(*) from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(count, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户

			count.append("select count(*) from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid() + " and user_id="
					+ userDetails.getId());

		}
		int entityCount = this.jdbcTemplate.queryForObject(count.toString(),
				Integer.class);
		List<Yxtk> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
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
		String tkfl = (String) param.get("tkfl");
		String content = (String) param.get("content");
		if (!sql.toString().contains("deptid")) {
			if (deptid != null || dept != null) {
				// sql.append(" and deptid=" + deptid);
				sql.append(" and deptid in (select id_ from dept where dept_name like '%"
						+ dept + "%') ");
			}
		}
		if (userId != null || user != null) {
			// sql.append(" and user_id=" + userId);
			sql.append(" and user_id in (select id_ from user where user_name like '%"
					+ user + "%') ");
		}
		if (begin != null) {
			sql.append(" and create_date >= date_format('" + sdf.format(begin)
					+ "','%Y%m%d')");
		}
		if (end != null) {
			sql.append(" and create_date <= date_format('" + sdf.format(end)
					+ "','%Y%m%d')");
		}
		if (tkfl != null) {
			sql.append(" and fl_id in (select id_ from tkfl where tkmc like '%"+tkfl+"%') ");
		}
		if (content != null) {
			sql.append(" and tmly_id in "
					+ "(select id_ from tmly where title like '%" + content
					+ "%' or content like '%" + content + "%') ");
		}
		// return null;
	}

	private List<Yxtk> getYxtkInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder("");
		Object[] objs = {};
		if ("SuAdmin".equals(roleName)) {// 超级管理员
			sql.append("select * from yxtk where yxbz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
			sql.append(" order by create_date desc limit " + begin + ","
					+ offest);

		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sql.append("select * from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid() + " ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
			sql.append(" order by create_date desc limit " + begin + ","
					+ offest);

		} else if ("USER".equals(roleName)) {// 普通用户
			sql.append("select * from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid() + " and user_id="
					+ userDetails.getId() + " ");
			sql.append(" order by create_date desc limit " + begin + ","
					+ offest);

		}
		List<Yxtk> list = this.jdbcTemplate.query(sql.toString(), objs,
				new RowMapper<Yxtk>() {

					@Override
					public Yxtk mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Yxtk yxtk = new Yxtk();
						yxtk.setId(result.getString("id_"));
						yxtk.setFlId(result.getInt("fl_id"));
						yxtk.setUserId(result.getInt("user_id"));
						yxtk.setCreateDate(result.getDate("create_date"));
						yxtk.setSpDate(result.getDate("sp_date"));
						yxtk.setSprId(result.getInt("spr_id"));
						yxtk.setDeptid(result.getInt("deptid"));
						yxtk.setContent(result.getString("content"));
						yxtk.setTmfz(result.getDouble("tmfz"));
						yxtk.setTmnd(result.getInt("tmnd"));
						yxtk.setTmlyId(result.getInt("tmly_id"));
						yxtk.setMode(result.getString("mode"));
						yxtk.setYxbz(result.getString("yxbz"));
						yxtk.setXybz(result.getString("xybz"));
						return yxtk;
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

	@Override
	public Collection<Dept> getDeptInfoByDeptId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from dept where id_=?";
		List<Dept> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Dept>() {

					@Override
					public Dept mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Dept dept = new Dept();
						dept.setDept_name(result.getString("dept_name"));
						return dept;
					}

				});
		return list;
	}

	@Override
	public Collection<User> getUserInfoByUserId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from user where id_=?";
		List<User> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<User>() {

					@Override
					public User mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						User user = new User();
						user.setUser_Name(result.getString("user_name"));
						return user;
					}

				});
		return list;
	}

	@Override
	public Collection<Yxtkxzx> getYxtkxzxInfoByYxtkId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from yxtkxzx where tk_id=? order by xz_key";
		List<Yxtkxzx> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Yxtkxzx>() {

					@Override
					public Yxtkxzx mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Yxtkxzx yxtkxzx = new Yxtkxzx();
						yxtkxzx.setId(result.getString("id_"));
						yxtkxzx.setTkId(result.getString("tk_id"));
						yxtkxzx.setXzKey(result.getString("xz_key"));
						yxtkxzx.setContent(result.getString("content"));
						return yxtkxzx;
					}

				});
		return list;
	}

	@Override
	public Collection<Yxtkxzx> getYxtkdaInfoByYxtkId(String id) {
		// TODO Auto-generated method stub
		// id是Yxtk的id，根据yxtk.id与yxtkxzx.tkid关联出yxtkxzx
		// 再把结果与yxtkda.tkxzxid关联把答案关联出来在前台显示
		String sql = "select x.* from yxtkxzx x,yxtkda d where x.id_=d.tkxzxid and d.tk_id=? order by x.xz_key";
		List<Yxtkxzx> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Yxtkxzx>() {

					@Override
					public Yxtkxzx mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Yxtkxzx yxtkxzx = new Yxtkxzx();
						yxtkxzx.setId(result.getString("id_"));
						yxtkxzx.setTkId(result.getString("tk_id"));
						yxtkxzx.setXzKey(result.getString("xz_key"));
						yxtkxzx.setContent(result.getString("content"));
						return yxtkxzx;
					}

				});
		return list;
	}

	@Override
	public void addYxtkxzx(Yxtkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "insert into yxtkxzx values(?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId(), xz.getTkId(),
				xz.getXzKey(), xz.getContent() });
	}

	@Override
	public void updateYxtkxzx(Yxtkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "update yxtkxzx set tk_id=?,xz_key=?,content=? where id_=?";
		this.jdbcTemplate.update(
				sql,
				new Object[] { xz.getTkId(), xz.getXzKey(), xz.getContent(),
						xz.getId() });
	}

	@Override
	public void deleteYxtkxzx(Yxtkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "delete from yxtkxzx where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId() });
		// this.deleteYxtkda(xz);
	}

	@Override
	public void addYxtkda(Yxtkxzx da) {
		// TODO Auto-generated method stub
		String sql = "insert into yxtkda values(?,?,?)";
		this.jdbcTemplate.update(sql,
				new Object[] { da.getContent(), da.getTkId(), da.getId() });
	}

	@Override
	public void updateYxtkda(Yxtkxzx da) {
		// TODO Auto-generated method stub
		String sql = "update yxtkda set tkxzxid=? where tk_id=?";
		this.jdbcTemplate.update(sql, new Object[] { da.getId(), da.getId() });
	}

	@Override
	public void deleteYxtkda(Yxtkxzx da) {
		// TODO Auto-generated method stub
		String sql = "delete from yxtkda where tkxzxid=? and tk_id=?";
		this.jdbcTemplate
				.update(sql, new Object[] { da.getId(), da.getTkId() });
	}

	@Override
	public Collection<User> getUserByDeptId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from user where deptid=?";
		List<User> users = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<User>() {

					@Override
					public User mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						User user = new User();
						user.setId_(result.getInt("id_"));
						user.setUser_Name(result.getString("user_name"));
						return user;
					}

				});
		return users;
	}

	@Override
	public void addYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(yxtk);
		String sql = "insert into yxtk values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] obj = { yxtk.getId(), yxtk.getFlId(), yxtk.getUserId(),
				sdf.format(yxtk.getCreateDate()), yxtk.getSpDate(),
				yxtk.getSprId(), yxtk.getDeptid(), yxtk.getContent(),
				list.get(0).get("value"), yxtk.getTmnd(), yxtk.getTmlyId(),
				yxtk.getMode(), "Y", "N" };
		this.jdbcTemplate.update(sql, obj);
	}

	private List<Map<String, Object>> getSysPropValueByTmnd(Yxtk yxtk) {
		String sql = "select value from system_props where id_=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { yxtk.getTmnd() });
		return list;
	}

	@Override
	public void addGrDeptGxJl(Yxtk yxtk) {
		String sql = "select value from system_props where id_=104";
		Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "insert into dept_tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxtk.getId(), yxtk.getDeptid(), yxtk.getUserId(),
				yxtk.getId(), value, 104, yxtk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
		sql = "insert into gr_tk_gxjl values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		this.chackYxtkModeChangeOrNot(yxtk);
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(yxtk);
		String sql = "update yxtk set fl_id=?,user_id=?,create_date=?,"
				+ "sp_date=?,spr_id=?,deptid=?," + "content=?,tmfz=?,tmnd=?,"
				+ "tmly_id=?,mode=? where id_=?";
		Object[] obj = { yxtk.getFlId(), yxtk.getUserId(),
				yxtk.getCreateDate(), yxtk.getSpDate(), yxtk.getSprId(),
				yxtk.getDeptid(), yxtk.getContent(), list.get(0).get("value"),
				yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), yxtk.getId() };
		this.jdbcTemplate.update(sql, obj);
	}

	private void chackYxtkModeChangeOrNot(Yxtk yxtk) {
		/*
		 * String sql = "select count(*) from yxtk where id_='" + yxtk.getId() +
		 * "' and mode='" + yxtk.getMode()+"'"; int count =
		 * this.jdbcTemplate.queryForObject(sql, Integer.class); if (count == 0)
		 * { // sql = //
		 * "select if((select mode from yxtk where id_='"+yxtk.getId
		 * ()+"'),1,0) from dual"; sql =
		 * "select if((select mode from yxtk where id_='"
		 * +yxtk.getId()+"'),1,0) from dual"; count =
		 * this.jdbcTemplate.queryForObject(sql, Integer.class); if((count == 0
		 * && !yxtk.getMode().equals("0")) || (count == 1 &&
		 * yxtk.getMode().equals("0"))){//mode从0变成1或2 //mode从1或2变成0 sql =
		 * "delete from yxtkxzx where tk_id=? and tk_id != '0'";
		 * this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
		 * 
		 * } sql = "delete from yxtkda where tk_id=?";
		 * this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() }); }
		 */
		String sql = "select count(*) from yxtk where id_='" + yxtk.getId()
				+ "' and mode='" + yxtk.getMode() + "'";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if (count == 0) {
			sql = "delete from yxtkda where tk_id=?";
			this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
		}

	}

	@Override
	public void deleteYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		String sql = "update yxtk set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(Yxtk yxtk) {
		String sql = "delete from dept_tk_gxjl where tk_id=? and gxly=?";
		Object[] objs = { yxtk.getId(), 104 };
		this.jdbcTemplate.update(sql, objs);
		sql = "delete from gr_tk_gxjl where tk_id=? and gxly=?";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public Collection<Yxtkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return this.getYxtkxzxInfoByYxtkId("0");
	}

	@Override
	public Collection<Tmly> getTmlyInfoByTmlyId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from tmly where id_=?";
		List<Tmly> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Tmly>() {

					@Override
					public Tmly mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tmly tmly = new Tmly();
						tmly.setId(result.getInt("id_"));
						tmly.setTitle(result.getString("title"));
						tmly.setContent(result.getString("content"));
						return tmly;
					}

				});
		return list;
	}

	@Override
	public Collection<Tkfl> getTkflInfoByflId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from tkfl where id_=?";
		List<Tkfl> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Tkfl>() {

					@Override
					public Tkfl mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tkfl tkfl = new Tkfl();
						tkfl.setId(result.getInt("id_"));
						tkfl.setParentId(result.getInt("parent_id"));
						tkfl.setTkmc(result.getString("tkmc"));
						tkfl.setMs(result.getString("ms"));
						return tkfl;
					}

				});
		return list;
	}

	@Override
	public List countGxjl(Map<String, Object> param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Integer deptid = (Integer) param.get("deptid");
		Integer userId = (Integer) param.get("userid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String content = (String) param.get("content");
		StringBuffer sql = new StringBuffer("select d.dept_name as deptname,");
		if (userId == null || userId != 0) {
			sql.append("u.user_name as username,");
		}
		sql.append("cou from ");
		sql.append("(select a.dept_id as did,a.user_id uid ,sum(a.gxz) as cou ");
		sql.append("from gr_tk_gxjl a , yxtk b ");
		sql.append("where a.tk_id=b.id_ ");
		if (deptid != null) {
			sql.append("and a.dept_id=" + deptid + " ");
			if (userId != null) {
				if (userId != 0) {
					sql.append("and a.user_id=" + userId + " ");
				}

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
	public List<Map<String, Object>> getLoginUserInfo(
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Role role = this.getRoleInfoByUserId(userDetails.getId());
		List<Dept> depts = (List<Dept>) this.getDeptInfoByDeptId(userDetails
				.getDeptid().toString());
		map.put("deptname", depts.get(0).getDept_name());
		map.put("username", userDetails.getLogin_Name());
		map.put("deptid", userDetails.getDeptid());
		map.put("rolename", role.getRole_Name());
		map.put("ms", role.getMs());
		list.add(map);
		return list;
	}

}
