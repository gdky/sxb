package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.DeptTkGxjl;
import gov.hygs.htgl.entity.GrTkGxjl;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkxzx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxtkglDaoImpl extends BaseJdbcDao implements YxtkglDao {

	@Override
	public void getYxtkInfo(Page<Yxtk> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String sqlCount = "select count(*) from yxtk where yxbz='Y'";
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				Integer.class);
		List<Yxtk> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Yxtk> getYxtkInfo(int begin, int offest) {
		// TODO Auto-generated method stub
		String sql = "select * from yxtk where yxbz='Y'limit ?,?";
		List<Yxtk> list = this.jdbcTemplate.query(sql, new Object[] { begin,
				offest }, new RowMapper<Yxtk>() {

			@Override
			public Yxtk mapRow(ResultSet result, int i) throws SQLException {
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
		String sql = "select * from yxtkxzx where tk_id=?";
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
		// 上面函数getYxtkdaInfoByYxtkId可不要
		String sql = "select x.* from yxtkxzx x,yxtkda d where x.id_=d.tkxzxid and d.tk_id=?";
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
		this.jdbcTemplate.update(sql, new Object[] { da.getId(), da.getTkId(),
				da.getId() });
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
		this.addGrTkGxJl(yxtk);
		this.addDeptTkGxJl(yxtk);
	}
	
	private void addDeptTkGxJl(Yxtk yxtk){
		List<DeptTkGxjl> deptTkGxjls = this.getDeptTkGxjlInfoById(yxtk);
		String sqlDept = null;
		Object[] objs = null;
		if(deptTkGxjls.size() > 0){
			DeptTkGxjl deptTkGxjl = deptTkGxjls.get(0);
			sqlDept = "update dept_tk_gxjl set gxz=gxz+? where id_=?";
			objs = new Object[]{deptTkGxjl.getGxly(),deptTkGxjl.getId()};
		}else{
			sqlDept = "insert into dept_tk_gxjl values(?,?,?,?,?,?)";
			objs = new Object[]{yxtk.getDeptid(),yxtk.getDeptid(),yxtk.getId(),1,1,yxtk.getCreateDate()};
		}
		this.jdbcTemplate.update(sqlDept, objs);
	}
	
	private List<DeptTkGxjl> getDeptTkGxjlInfoById(Yxtk yxtk){
		String sql = "select * from dept_tk_gxjl where id_=?";
		List<DeptTkGxjl> list = this.jdbcTemplate.query(sql, new Object[]{yxtk.getDeptid()}, new RowMapper<DeptTkGxjl>(){

			@Override
			public DeptTkGxjl mapRow(ResultSet result, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				DeptTkGxjl deptTkGxjl = new DeptTkGxjl();
				deptTkGxjl.setId(result.getString("id_"));
				deptTkGxjl.setDeptId(result.getInt("dept_id"));
				deptTkGxjl.setTkId(result.getString("tk_id"));
				deptTkGxjl.setGxz(result.getDouble("gxz"));
				deptTkGxjl.setGxly(result.getInt("gxly"));
				deptTkGxjl.setGxDate(result.getDate("gx_date"));
				return deptTkGxjl;
			}
			
		});
		return list;
	}

	private void addGrTkGxJl(Yxtk yxtk) {
		List<GrTkGxjl> grTkGxjls = this.getGrTkGxjlInfoById(yxtk);
		String sqlUser = null;
		Object[] objs = null;
		if (grTkGxjls.size() > 0) {
			GrTkGxjl grTkGxjl = grTkGxjls.get(0);
			sqlUser = "update gr_tk_gxjl set gxz=gxz+? where id_=?";
			objs = new Object[] { grTkGxjl.getGxly(), grTkGxjl.getId() };
		}else{
			sqlUser = "insert into gr_tk_gxjl values(?,?,?,?,?,?)";
			objs = new Object[]{yxtk.getUserId(),yxtk.getDeptid(),yxtk.getId(),1,1,yxtk.getCreateDate()};
		}
		this.jdbcTemplate.update(sqlUser, objs);
	}

	private List<GrTkGxjl> getGrTkGxjlInfoById(Yxtk yxtk) {
		String sql = "select * from gr_tk_gxjl where id_=?";
		List<GrTkGxjl> list = this.jdbcTemplate.query(sql,
				new Object[] { yxtk.getUserId() }, new RowMapper<GrTkGxjl>() {

					@Override
					public GrTkGxjl mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						GrTkGxjl grTkGxjl = new GrTkGxjl();
						grTkGxjl.setId(result.getString("id_"));
						grTkGxjl.setDeptId(result.getInt("dept_id"));
						grTkGxjl.setTkId(result.getString("tk_id"));
						grTkGxjl.setGxz(result.getDouble("gxz"));
						grTkGxjl.setGxly(result.getInt("gxly"));
						grTkGxjl.setGxDate(result.getDate("gx_date"));
						return grTkGxjl;
					}

				});
		return list;
	}

	@Override
	public void updateYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		String sql = "update yxtk set fl_id=?,user_id=?,create_date=?,"
				+ "sp_date=?,spr_id=?,deptid=?," + "content=?,tmfz=?,tmnd=?,"
				+ "tmly_id=?,mode=? where id_=?";
		Object[] obj = { yxtk.getFlId(), yxtk.getUserId(),
				yxtk.getCreateDate(), yxtk.getSpDate(), yxtk.getSprId(),
				yxtk.getDeptid(), yxtk.getContent(), yxtk.getTmfz(),
				yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), yxtk.getId() };
		this.jdbcTemplate.update(sql, obj);
	}

	@Override
	public void deleteYxtk(Yxtk yxtk) {
		// TODO Auto-generated method stub
		// String sql = "delete from yxtk where id_=?";
		String sql = "update yxtk set yxbz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
		this.deleteGrDeptGxJl(yxtk);
	}
	
	private void deleteGrDeptGxJl(Yxtk yxtk) {
		String sqlUser = "update gr_tk_gxjl set gxz=gxz-1 where id_=?";
		this.jdbcTemplate.update(sqlUser, new Object[]{yxtk.getUserId()});
		String sqlDept = "update dept_tk_gxjl set gxz=gxz-1 where id_=?";
		this.jdbcTemplate.update(sqlDept, new Object[]{yxtk.getDeptid()});
	}
	
	@Override
	public Collection<Yxtkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return this.getYxtkxzxInfoByYxtkId("0");
	}

}
