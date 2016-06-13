package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.DeptTkGxjl;
import gov.hygs.htgl.entity.GrTkGxjl;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxtkglDaoImpl extends BaseJdbcDao implements YxtkglDao {

	@Override
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId());
		String sqlCount = "";
		if ("SuAdmin".equals(roleName)) {// 超级用户

			sqlCount = "select count(*) from yxtk where yxbz='Y'";

		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员

			sqlCount = "select count(*) from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid();

		} else if ("USER".equals(roleName)) {// 普通用户

			sqlCount = sqlCount = "select count(*) from yxtk where yxbz='Y' and deptid="
					+ userDetails.getDeptid()
					+ " and user_id="
					+ userDetails.getId();

		}
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				Integer.class);
		List<Yxtk> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Yxtk> getYxtkInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName) {
		// TODO Auto-generated method stub
		String sql = "";
		Object[] objs = {};
		if ("SuAdmin".equals(roleName)) {// 超级管理员

			sql = "select * from yxtk where yxbz='Y' "
					+ "order by create_date desc limit ?,?";
			objs = new Object[] { begin, offest };

		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员

			sql = "select * from yxtk where yxbz='Y' and deptid=? "
					+ "order by create_date desc limit ?,?";
			objs = new Object[] { userDetails.getDeptid(), begin, offest };

		} else if ("USER".equals(roleName)) {// 普通用户

			sql = "select * from yxtk where yxbz='Y' and deptid=? and user_id=? "
					+ "order by create_date desc limit ?,?";
			objs = new Object[] { userDetails.getDeptid(), userDetails.getId(),
					begin, offest };

		}
		List<Yxtk> list = this.jdbcTemplate.query(sql, objs,
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
						return yxtk;
					}

				});
		return list;
	}

	private String getRoleInfoByUserId(Integer id) {
		String sql = "select a.* from role a,user_role b where a.id_=b.role_id and user_id=? order by id_";
		List<Role> roles = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Role>() {

					@Override
					public Role mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Role role = new Role();
						role.setRole_Name(result.getString("role_name"));
						return role;
					}

				});
		return roles.get(0).getRole_Name();
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
		String sql = "insert into yxtk values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] obj = { yxtk.getId(), yxtk.getFlId(), yxtk.getUserId(),
				yxtk.getCreateDate(), yxtk.getSpDate(), yxtk.getSprId(),
				yxtk.getDeptid(), yxtk.getContent(), yxtk.getTmfz(),
				yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), "Y", "N" };
		this.jdbcTemplate.update(sql, obj);
	}

	@Override
	public void addGrDeptGxJl(Yxtk yxtk) {
		String sql = "insert into dept_tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxtk.getId(), yxtk.getDeptid(), yxtk.getUserId(),
				yxtk.getId(), 1, 1, yxtk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
		sql = "insert into gr_tk_gxjl values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		this.chackYxtkModeChangeOrNot(yxtk);
		String sql = "update yxtk set fl_id=?,user_id=?,create_date=?,"
				+ "sp_date=?,spr_id=?,deptid=?," + "content=?,tmfz=?,tmnd=?,"
				+ "tmly_id=?,mode=? where id_=?";
		Object[] obj = { yxtk.getFlId(), yxtk.getUserId(),
				yxtk.getCreateDate(), yxtk.getSpDate(), yxtk.getSprId(),
				yxtk.getDeptid(), yxtk.getContent(), yxtk.getTmfz(),
				yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), yxtk.getId() };
		this.jdbcTemplate.update(sql, obj);
	}

	private void chackYxtkModeChangeOrNot(Yxtk yxtk) {
		/*String sql = "select count(*) from yxtk where id_='" + yxtk.getId()
				+ "' and mode='" + yxtk.getMode()+"'";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if (count == 0) {
			// sql =
			// "select if((select mode from yxtk where id_='"+yxtk.getId()+"'),1,0) from dual";
			sql = "select if((select mode from yxtk where id_='"+yxtk.getId()+"'),1,0) from dual";
			count = this.jdbcTemplate.queryForObject(sql, Integer.class);
			if((count == 0 && !yxtk.getMode().equals("0")) || (count == 1 && yxtk.getMode().equals("0"))){//mode从0变成1或2   //mode从1或2变成0
				sql = "delete from yxtkxzx where tk_id=? and tk_id != '0'";
				this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
				
			}
			sql = "delete from yxtkda where tk_id=?";
			this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
		}*/
		String sql = "select count(*) from yxtk where id_='" + yxtk.getId()
				+ "' and mode='" + yxtk.getMode()+"'";
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
		String sql = "delete from dept_tk_gxjl where tk_id=?";
		Object[] objs = { yxtk.getId() };
		this.jdbcTemplate.update(sql, objs);
		sql = "delete from gr_tk_gxjl where tk_id=?";
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
	public String countGxjl(Record record) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from dept_tk_gxjl d where dept_id=? and gx_date >= date_format(?,'%Y%m%d') and gx_date <= date_format(?,'%Y%m%d')";
		Object[] objs = { record.getInt("deptId"), record.getDate("begin"),
				record.getDate("end") };
		Integer deptCount = this.jdbcTemplate.queryForObject(sql, objs,
				Integer.class);
		sql = "select count(*) from gr_tk_gxjl g where user_id=? and gx_date >= date_format(?,'%Y%m%d') and gx_date <= date_format(?,'%Y%m%d')";
		objs = new Object[] { record.getInt("userId"), record.getDate("begin"),
				record.getDate("end") };
		Integer userCount = this.jdbcTemplate.queryForObject(sql, objs,
				Integer.class);
		String str = "{deptCount:" + deptCount + ",userCount:" + userCount
				+ "}";
		return String.valueOf(str);
	}

}
