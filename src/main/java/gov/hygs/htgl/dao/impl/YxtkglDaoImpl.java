package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YxtkglDao;
import gov.hygs.htgl.entity.Dept;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.Tmly;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.entity.Yxtk;
import gov.hygs.htgl.entity.Yxtkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.ListSelectionEvent;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class YxtkglDaoImpl extends BaseJdbcDao implements YxtkglDao {

	@Override
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuffer count = new StringBuffer("");
		if ("SuAdmin".equals(roleName)) {// 超级用户

			count.append("select count(*) from tktm a where a.yxbz='Y'");
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(count, param);
				if(arg != null)
					args.addAll(arg);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员

			count.append("select count(*) from tktm a where a.yxbz='Y' and a.deptid=?");
			args.add(userDetails.getDeptid());
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(count, param);
				if(arg != null){
					args.addAll(arg);
				}
			}
		} else if ("Other".equals(roleName)) {// 普通用户

			count.append("select count(*) from tktm a where a.yxbz='Y' and a.deptid=? and a.user_id=?");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		int entityCount = 0;
		if(args.isEmpty()){
			entityCount = this.jdbcTemplate.queryForObject(count.toString(),
					Integer.class);
		}else{
			entityCount = this.jdbcTemplate.queryForObject(count.toString(),args.toArray(),
					Integer.class);
		}
		List<Tktm> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
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
		String tkfl = (String) param.get("tkfl");
		String content = (String) param.get("content");
		String tktmcontent = (String) param.get("tktmcontent");
		String ksbz = (String) param.get("ksbz");
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
		if (tkfl != null) {
			sql.append(" and a.fl_id in (select id_ from tkfl where tkmc like ?) ");
			args.add("%"+tkfl+"%");
		}
		if (content != null) {
			sql.append(" and a.tmly_id in ");
			sql.append("(select id_ from tmly where title like ? or content like ?) ");
			args.add("%"+content+"%");
			args.add("%"+content+"%");
		}
		if(tktmcontent != null){
			sql.append(" and a.content like ? ");
			args.add("%"+tktmcontent+"%");
		}
		if(ksbz != null){
			if("是".equals(ksbz)){
				sql.append(" and a.KSBZ='Y' ");
			}else if("否".equals(ksbz)){
				sql.append(" and a.KSBZ='N' ");
			}
		}
		return args;
	}

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
	
	private List<Tktm> getYxtkInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*");
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" where a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ ");
		if ("SuAdmin".equals(roleName)) {// 超级管理员
			sql.append(" and a.yxbz='Y' ");
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sql, param);
				if(arg != null){
					args.addAll(arg);
				}
			}

		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sql.append(" and a.yxbz='Y' and a.deptid=? ");
			args.add(userDetails.getDeptid());
			if (param != null) {
				List<Object> arg = this.rebuileSqlByConditionAndRole(sql, param);
				if(arg != null){
					args.addAll(arg);
				}
			}

		} else if ("Other".equals(roleName)) {// 普通用户
			sql.append(" and a.yxbz='Y' and a.deptid=? and user_id=? ");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		sql.append(" order by a.create_date desc limit ?,? ");
		args.add(begin);
		args.add(offest);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(), args.toArray(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm yxtk = new Tktm();
						yxtk.setTkfl(result.getString("TKMC"));
						yxtk.setTmly(result.getString("TITLE"));
						yxtk.setDept(result.getString("DEPT_NAME"));
						yxtk.setUser(result.getString("USER_NAME"));
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
						yxtk.setKsbz(result.getString("ksbz"));
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
			aRole = roles.get(0);
			aRole.setRole_Name("Other");
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
						dept.setParentId(result.getInt("parent_id"));
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
	public void addYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "insert into tkxzx values(?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId(), xz.getTkId(),
				xz.getXzKey(), xz.getContent() });
	}

	@Override
	public void updateYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "update tkxzx set tk_id=?,xz_key=?,content=? where id_=?";
		this.jdbcTemplate.update(
				sql,
				new Object[] { xz.getTkId(), xz.getXzKey(), xz.getContent(),
						xz.getId() });
	}

	@Override
	public void deleteYxtkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "delete from tkxzx where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId() });
		// this.deleteYxtkda(xz);
	}

	@Override
	public void addYxtkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "insert into tkda values(?,?,?)";
		this.jdbcTemplate.update(sql,
				new Object[] { da.getContent(), da.getTkId(), da.getId() });
	}

	@Override
	public void updateYxtkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "update tkda set tkxzxid=? where tk_id=?";
		this.jdbcTemplate.update(sql, new Object[] { da.getId(), da.getId() });
	}

	@Override
	public void deleteYxtkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "delete from tkda where tkxzxid=? and tk_id=?";
		this.jdbcTemplate
				.update(sql, new Object[] { da.getId(), da.getTkId() });
	}

	@Override
	public Collection<User> getUserByDeptId(String id) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select * from user ");
		if(!"0".equals(id)){
			sql.append("where deptid=?");
			args.add(id);
		}
		List<User> users = this.jdbcTemplate.query(sql.toString(),args.toArray(),new RowMapper<User>() {

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
	public void addYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(yxtk);
		String sql = "insert into tktm values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int tmlyid = getTmlyInfoOrAddTmly(yxtk.getTmly(), yxtk.getTmlyContent());
		Object[] obj = { yxtk.getId(), yxtk.getFlId(), yxtk.getUserId(),
				sdf.format(yxtk.getCreateDate()), yxtk.getSpDate(),
				yxtk.getSprId(), yxtk.getDeptid(), yxtk.getContent(),
				list.get(0).get("value"), yxtk.getTmnd(), tmlyid,//yxtk.getTmlyId(),
				yxtk.getMode(), "Y", "N", yxtk.getDrbz(),yxtk.getKsbz() };
		this.jdbcTemplate.update(sql, obj);
	}

	private List<Map<String, Object>> getSysPropValueByTmnd(Tktm yxtk) {
		String sql = "select value from system_props where id_=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { yxtk.getTmnd() });
		return list;
	}

	@Override
	public void addGrDeptGxJl(Tktm yxtk) {
		String sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { yxtk.getId(), yxtk.getDeptid(), yxtk.getUserId(),
				yxtk.getId(), this.getGxjlValueByKey("GxzA"), 1, yxtk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
	}
	
	private String getGxjlValueByKey(String key){
		String sql = "select value from system_props where key_=?";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{key}, String.class);
	}

	@Override
	public void updateYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		this.chackYxtkModeChangeOrNot(yxtk);
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(yxtk);
		StringBuffer sql = new StringBuffer("update tktm set fl_id=?,user_id=?,create_date=?,");
			sql.append("sp_date=?,spr_id=?,deptid=?,content=?,tmfz=?,tmnd=?,");
			sql.append("tmly_id=?,mode=?,ksbz=? where id_=?");
		
		Object[] obj = { yxtk.getFlId(), yxtk.getUserId(),
				yxtk.getCreateDate(), yxtk.getSpDate(), yxtk.getSprId(),
				yxtk.getDeptid(), yxtk.getContent(), list.get(0).get("value"),
				yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), yxtk.getKsbz(), yxtk.getId() };
		this.jdbcTemplate.update(sql.toString(), obj);
	}

	private void chackYxtkModeChangeOrNot(Tktm yxtk) {
		String sql = "select count(*) from tktm where id_=? and mode=?";
		int count = this.jdbcTemplate.queryForObject(sql,new Object[]{yxtk.getId(),yxtk.getMode()}, Integer.class);
		if (count == 0) {
			sql = "delete from tkda where tk_id=?";
			this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
		}

	}

	@Override
	public void deleteYxtk(Tktm yxtk) {
		// TODO Auto-generated method stub
		String sql = "update tktm set yxbz='N',xybz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { yxtk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(Tktm yxtk) {
		String sql = "delete from tk_gxjl where tk_id=?";
		Object[] objs = { yxtk.getId() };
		this.jdbcTemplate.update(sql, objs);
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
	public List<Map<String, Object>> getLoginUserInfo(
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Role role = this.getRoleInfoByUserId(userDetails.getId());
		List<Dept> depts = (List<Dept>) this.getDeptInfoByDeptId(userDetails
				.getDeptid().toString());
		map.put("deptname", depts.get(0).getDept_name());
		map.put("parentid", depts.get(0).getParentId());
		map.put("username", userDetails.getLogin_Name());
		map.put("userid", userDetails.getId());
		map.put("deptid", userDetails.getDeptid());
		map.put("rolename", role.getRole_Name());
		map.put("ms", role.getMs());
		list.add(map);
		return list;
	}

	@Override
	public int getTmlyInfoOrAddTmly(String tmlyTitle, String tmlyContent) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select if(count(*),id_,0) from tmly where title=? ");
		args.add(tmlyTitle);
		if(tmlyContent != null){
			sql.append(" and content=? ");
			args.add(tmlyContent);
		}
		int tmlyid = this.jdbcTemplate.queryForObject(sql.toString(), args.toArray(), Integer.class);
		if (tmlyid == 0) {
			tmlyid = this.addTmly(tmlyTitle, tmlyContent);
		}
		return tmlyid;
	}

	private int addTmly(String tmlyTitle, String tmlyContent) {
		String sql = "insert into tmly values(?,?,?,?)";
		return this.insertAndGetKeyByJdbc(sql,
				new Object[] { null, tmlyTitle, tmlyContent, null },
				new String[] { "id_" }).intValue();
	}

	@Override
	public List<Map<String, Object>> getUserIdByDeptIdAndTheyName(
			String userName, String deptName) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("select if(count(*),id_,0) userid,deptid ");
				sql.append("from user where user_name like ? ");
				sql.append("and deptid in (select id_ from dept where dept_name like ?)");
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql.toString(), new Object[]{"%"+userName+"%","%"+deptName+"%"});
		String userid = String.valueOf(list.get(0).get("userid"));
		if("0".equals(userid)){
			return new ArrayList<Map<String, Object>>();
		}
		return list;
	}

	@Override
	public int getTkflInfoOrAddTkfl(String tkflTkmc) {
		// TODO Auto-generated method stub
		String sql = "select if(count(*),id_,0) from tkfl where tkmc=?";
		int flId = this.jdbcTemplate.queryForObject(sql,
				new Object[] { tkflTkmc }, Integer.class);
		if (flId == 0) {
			flId = this.addFlId(tkflTkmc);
		}
		return flId;
	}

	private int addFlId(String tkflTkmc) {
		String sql = "insert into tkfl values(?,?,?,?)";
		return this.insertAndGetKeyByJdbc(sql,
				new Object[] { null, 0, tkflTkmc, tkflTkmc },
				new String[] { "id_" }).intValue();
	}

	@Override
	public boolean chackTktmExistOrNot(String tktmContent) {
		// TODO Auto-generated method stub
		String sql = "select if(count(*),1,0) from tktm where content=?";
		int record = this.jdbcTemplate.queryForObject(sql,
				new Object[] { tktmContent }, Integer.class);
		return record == 0 ? true : false;
	}
	
	@Override
	public void batchInsertTk(List<Tktm> tktms, List<Tkxzx> tkxzxs,
			List<Tkxzx> tkdas) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Integer, Integer> chackTmnd = new HashMap<Integer, Integer>();
		List<Map<String, Object>> list = null;
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		List<Object[]> gxjlBatchArgs = new ArrayList<Object[]>();
		String gxz = this.getGxjlValueByKey("GxzA");
		for (Tktm yxtk : tktms) {
			if (chackTmnd.get(yxtk.getTmnd()) == null) {
				list = this.getSysPropValueByTmnd(yxtk);
			}
			batchArgs.add(new Object[] { yxtk.getId(), yxtk.getFlId(),
					yxtk.getUserId(), sdf.format(yxtk.getCreateDate()),
					yxtk.getSpDate(), yxtk.getSprId(), yxtk.getDeptid(),
					yxtk.getContent(), list.get(0).get("value"),
					yxtk.getTmnd(), yxtk.getTmlyId(), yxtk.getMode(), "Y", "N",
					yxtk.getDrbz(), "N" });
			gxjlBatchArgs.add(new Object[]{yxtk.getId(), yxtk.getDeptid(), yxtk.getUserId(),
					yxtk.getId(), gxz, 1, yxtk.getCreateDate()});	

		}
		
		if (batchArgs.size() > 0) {
			String sql = "insert into tktm values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.jdbcTemplate.batchUpdate(sql, batchArgs);
			sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
			this.jdbcTemplate.batchUpdate(sql, gxjlBatchArgs);

			List<Object[]> tkxzxBatchArgs = new ArrayList<Object[]>();
			List<Object[]> tkdaBatchArgs = new ArrayList<Object[]>();

			for (Tkxzx xz : tkxzxs) {
				tkxzxBatchArgs.add(new Object[] { xz.getId(), xz.getTkId(),
						xz.getXzKey(), xz.getContent() });
			}
			if (tkxzxBatchArgs.size() > 0) {
				sql = "insert into tkxzx values(?,?,?,?)";
				this.jdbcTemplate.batchUpdate(sql, tkxzxBatchArgs);
			}

			for (Tkxzx da : tkdas) {
				tkdaBatchArgs.add(new Object[] { da.getContent(), da.getTkId(),
						da.getId() });
			}
			if (tkdaBatchArgs.size() > 0) {
				sql = "insert into tkda values(?,?,?)";
				this.jdbcTemplate.batchUpdate(sql, tkdaBatchArgs);
			}
		}

	}

	
}
