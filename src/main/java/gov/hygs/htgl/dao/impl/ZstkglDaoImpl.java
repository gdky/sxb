package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.ZstkglDao;
import gov.hygs.htgl.entity.Exam;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class ZstkglDaoImpl extends BaseJdbcDao implements ZstkglDao {

	@Override
	public void getZstkInfo(Page<Tktm> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder count = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇鐢ㄦ埛
			count.append("select count(*) from tktm where xybz='Y'");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(count, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			count.append("select count(*) from tktm where xybz='Y' and deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(count, param);
			}
		} else if ("USER".equals(roleName)) {// 鏅�氱敤鎴�
			count.append("select count(*) from tktm where xybz='Y' and deptid="
					+ userDetails.getDeptid() + " and user_id="
					+ userDetails.getId());
		}
		int entityCount = this.jdbcTemplate.queryForObject(count.toString(),
				Integer.class);
		List<Tktm> list = this.getZstkInfo(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getZstkInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		StringBuilder sql = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇绠＄悊鍛�
			sql.append("select * from tktm where xybz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			sql.append("select * from tktm where xybz='Y' and deptid="
					+ userDetails.getDeptid() + " ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("USER".equals(roleName)) {// 鏅�氱敤鎴�
			sql.append("select * from tktm where xybz='Y' and deptid="
					+ userDetails.getDeptid() + " and user_id="
					+ userDetails.getId() + " ");
		}
		sql.append(" order by create_date desc limit " + begin + "," + offest);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm zstk = new Tktm();
						zstk.setId(result.getString("id_"));
						zstk.setFlId(result.getInt("fl_id"));
						zstk.setUserId(result.getInt("user_id"));
						zstk.setCreateDate(result.getDate("create_date"));
						zstk.setSpDate(result.getDate("sp_date"));
						zstk.setSprId(result.getInt("spr_id"));
						zstk.setDeptid(result.getInt("deptid"));
						zstk.setContent(result.getString("content"));
						zstk.setTmfz(result.getDouble("tmfz"));
						zstk.setTmnd(result.getInt("tmnd"));
						zstk.setTmlyId(result.getInt("tmly_id"));
						zstk.setMode(result.getString("mode"));
						zstk.setYxbz(result.getString("yxbz"));
						zstk.setXybz(result.getString("xybz"));
						return zstk;
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
				if ("SuAdmin".equals(role.getRole_Name())) {// 瓒呯骇绠＄悊鍛�
					aRole = role;
				}
			}
		}
		if (aRole == null) {
			for (Role role : roles) {
				if ("DeptAdmin".equals(role.getRole_Name())) {// 閮ㄩ棬绠＄悊鍛�
					aRole = role;
				}
			}
		}
		if (aRole == null) {
			for (Role role : roles) {
				if ("USER".equals(role.getRole_Name())) {// 鏅�氱敤鎴�
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
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
		String tkfl = (String) param.get("tkfl");
		String content = (String) param.get("content");
		String tktmcontent = (String) param.get("tktmcontent");
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
			sql.append(" and fl_id in (select id_ from tkfl where tkmc like '%"
					+ tkfl + "%') ");
		}
		if (content != null) {
			sql.append(" and tmly_id in "
					+ "(select id_ from tmly where title like '%" + content
					+ "%' or content like '%" + content + "%') ");
		}
		if (tktmcontent != null) {
			sql.append(" and content like '%" + tktmcontent + "%' ");
		}
		// return null;
	}

	@Override
	public Collection<Tkxzx> getTkzxzInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from tkxzx where tk_id=? order by xz_key";
		List<Tkxzx> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Tkxzx>() {

					@Override
					public Tkxzx mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tkxzx tkxzx = new Tkxzx();
						tkxzx.setId(result.getString("id_"));
						tkxzx.setTkId(result.getString("tk_id"));
						tkxzx.setXzKey(result.getString("xz_key"));
						tkxzx.setContent(result.getString("content"));
						return tkxzx;
					}

				});
		return list;
	}

	@Override
	public Collection<Tkxzx> getDaXzxInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		String sql = "select x.* from tkxzx x,tkda d where x.id_=d.tkxzxid and d.tk_id=? order by x.xz_key";
		List<Tkxzx> list = this.jdbcTemplate.query(sql, new Object[] { id },
				new RowMapper<Tkxzx>() {

					@Override
					public Tkxzx mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tkxzx tkxzx = new Tkxzx();
						tkxzx.setId(result.getString("id_"));
						tkxzx.setTkId(result.getString("tk_id"));
						tkxzx.setXzKey(result.getString("xz_key"));
						tkxzx.setContent(result.getString("content"));
						return tkxzx;
					}

				});
		return list;
	}

	@Override
	public Collection<Tkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return this.getTkzxzInfoByZstkId("0");
	}

	@Override
	public void addZstk(Tktm zstk) {
		// TODO Auto-generated method stub
		/*
		 * if (this.chackRecordExistOrNot(zstk) == 0) { List<Map<String,
		 * Object>> list = this.getSysPropValueByTmnd(zstk); SimpleDateFormat
		 * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String sql =
		 * "insert into zstk values(?,?,?,?,?,?,?,?,?,?,?,?,?)"; Object[] objs =
		 * { zstk.getId(), zstk.getFlId(), zstk.getUserId(),
		 * sdf.format(zstk.getCreateDate()), zstk.getSpDate(), zstk.getSprId(),
		 * zstk.getDeptid(), zstk.getContent(), list.get(0).get("value"),
		 * zstk.getTmnd(), zstk.getTmlyId(), zstk.getMode(), "Y" };
		 * this.jdbcTemplate.update(sql, objs); }
		 */
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(zstk);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into tktm values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] objs = { zstk.getId(), zstk.getFlId(), zstk.getUserId(),
				sdf.format(zstk.getCreateDate()), zstk.getSpDate(),
				zstk.getSprId(), zstk.getDeptid(), zstk.getContent(),
				list.get(0).get("value"), zstk.getTmnd(), zstk.getTmlyId(),
				zstk.getMode(), "Y", "Y", zstk.getDrbz() };
		this.jdbcTemplate.update(sql, objs);
	}

	private List<Map<String, Object>> getSysPropValueByTmnd(Tktm zstk) {
		String sql = "select value from system_props where id_=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { zstk.getTmnd() });
		return list;
	}

	@Override
	public void addGrDeptGxJl(Tktm zstk) {
		// TODO Auto-generated method stub
		// String sql = "select value from system_props where id_=105";
		// Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		// sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
		// Object[] objs = { zstk.getContent(), zstk.getDeptid(),
		// zstk.getUserId(), zstk.getId(), value, 105,
		// zstk.getCreateDate() };
		String sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { zstk.getContent(), zstk.getDeptid(),
				zstk.getUserId(), zstk.getId(), 3, 2, zstk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateZstk(Tktm zstk) {
		// TODO Auto-generated method stub
		this.chackYxtkModeChangeOrNot(zstk);
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(zstk);
		String sql = "update tktm set fl_id=?,user_id=?,create_date=?,"
				+ "sp_date=?,spr_id=?,deptid=?," + "content=?,tmfz=?,tmnd=?,"
				+ "tmly_id=?,mode=?,yxbz=?,xybz=? where id_=?";
		Object[] obj = { zstk.getFlId(), zstk.getUserId(),
				zstk.getCreateDate(), zstk.getSpDate(), zstk.getSprId(),
				zstk.getDeptid(), zstk.getContent(), list.get(0).get("value"),
				zstk.getTmnd(), zstk.getTmlyId(), zstk.getMode(),
				zstk.getYxbz(), zstk.getXybz(), zstk.getId() };
		this.jdbcTemplate.update(sql, obj);
	}

	private void chackYxtkModeChangeOrNot(Tktm zstk) {
		String sql = "select count(*) from tktm where id_='" + zstk.getId()
				+ "' and mode='" + zstk.getMode() + "'";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if (count == 0) {
			sql = "delete from tkda where tk_id=?";
			this.jdbcTemplate.update(sql, new Object[] { zstk.getId() });
		}
	}

	@Override
	public void deleteZstk(Tktm zstk) {
		// TODO Auto-generated method stub
		String sql = "update tktm set xybz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { zstk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(Tktm zstk) {
		// TODO Auto-generated method stub
		String sql = "delete from tk_gxjl where tk_id=? and gxly=?";
		Object[] objs = { zstk.getId(), 2 };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void addTkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "insert into tkxzx values(?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId(), xz.getTkId(),
				xz.getXzKey(), xz.getContent() });
	}

	@Override
	public void updateTkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "update tkxzx set tk_id=?,xz_key=?,content=? where id_=?";
		this.jdbcTemplate.update(
				sql,
				new Object[] { xz.getTkId(), xz.getXzKey(), xz.getContent(),
						xz.getId() });
	}

	@Override
	public void deleteTkxzx(Tkxzx xz) {
		// TODO Auto-generated method stub
		String sql = "delete from tkxzx where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { xz.getId() });
	}

	@Override
	public void addTkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "insert into tkda values(?,?,?)";
		this.jdbcTemplate.update(sql,
				new Object[] { da.getContent(), da.getTkId(), da.getId() });
	}

	@Override
	public void updateTkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "update tkda set tkxzxid=? where tk_id=?";
		this.jdbcTemplate.update(sql, new Object[] { da.getId(), da.getId() });
	}

	@Override
	public void deleteTkda(Tkxzx da) {
		// TODO Auto-generated method stub
		String sql = "delete from tkda where tkxzxid=? and tk_id=?";
		this.jdbcTemplate
				.update(sql, new Object[] { da.getId(), da.getTkId() });
	}

	@Override
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String count = "select count(*) from tktm where yxbz='Y' and xybz='N'";
		int entityCount = this.jdbcTemplate
				.queryForObject(count, Integer.class);
		List<Tktm> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getYxtkInfo(int begin, int offest) {
		StringBuilder sql = new StringBuilder(
				"select * from tktm where yxbz='Y'");
		if (offest == -1 && begin == -1) {
			sql.append(" and xybz='Y' ");
		} else {
			sql.append(" and xybz='N' limit " + begin + "," + offest);
		}
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm yxtk = new Tktm();
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

	@Override
	public void updateTkfxtsInfo(Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<String> ids = (List<String>) param.get("id");
		if (ids.size() > 0) {
			Integer groupId = (Integer) param.get("groupId");
			String groupname = (String) param.get("groupname");
			String ms = (String) param.get("ms");

			String sql = "insert into tkfxtsjl values(?,?,?,?)";
			int jlId = this.insertAndGetKeyByJdbc(sql,
					new Object[] { null, userDetails.getId(), new Date(), ms },
					new String[] { "id_" }).intValue();
			sql = "insert into tktsqz values(?,?,?)";
			this.jdbcTemplate.update(sql, new Object[] { null, groupId, jlId });
			for (String id : ids) {
				sql = "insert into tktsnr values(?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, jlId, id });
			}

		}
	}

	@Override
	public void updateKstsjlInfo(Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<String> ids = (List<String>) param.get("id");
		if (ids.size() > 0) {
			Integer groupId = (Integer) param.get("groupId");
			String ms = (String) param.get("ms");
			Date begin = (Date) param.get("begin");
			Date end = (Date) param.get("end");
			String title = (String) param.get("title");
			String tpye = (String) param.get("type");

			/*
			 * String sql = "insert into kstsjl values(?,?,?,?,?,?)"; int jlId =
			 * this.insertAndGetKeyByJdbc( sql, new Object[] { null,
			 * userDetails.getId(), new Date(), ms, begin, end }, new String[] {
			 * "id_" }).intValue(); sql = "insert into kstsqz value(?,?,?)";
			 * this.jdbcTemplate.update(sql, new Object[] { null, groupId, jlId
			 * }); for (String id : ids) { sql =
			 * "insert into kstsnr values(?,?,?)"; this.jdbcTemplate.update(sql,
			 * new Object[] { null, jlId, id }); }
			 */
			String sql = "insert into exam values(?,?,?,?,?,?)";
			int jlId = this.insertAndGetKeyByJdbc(
					sql,
					new Object[] { null, begin, end, title, tpye,
							userDetails.getId() }, new String[] { "id_" })
					.intValue();
			sql = "insert into exam_tsqz value(?,?,?)";
			this.jdbcTemplate.update(sql, new Object[] { null, groupId, jlId });
			for (int i = 0; i < ids.size(); i++) {
				sql = "insert into exam_detail values(?,?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, i + 1, jlId,
						ids.get(i) });
			}
		}
	}

	@Override
	public Integer getSomeInfoBySystemPropsKey(String systemPropsKey) {
		// TODO Auto-generated method stub
		String sql = "select value from system_props where key_='"+systemPropsKey+"'";
		Integer value = Integer.parseInt(this.jdbcTemplate.queryForObject(sql,
				String.class));
		return value;
	}

	List<Tktm> randomList = new ArrayList<Tktm>();

	@Override
	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param, String systemPropsKey) {
		// TODO Auto-generated method stub
		/*
		 * int pageSize = page.getPageSize(); Integer value =
		 * this.getKstsInfoFromSystemProps(); List<Tktm> list =
		 * this.getYxtkInfo(-1, -1); if (list.size() > 0) {
		 * Collections.shuffle(list); List<Tktm> randomList = new
		 * ArrayList<Tktm>(); //for (int i = 0; i < (pageSize > list.size() ?
		 * list.size() // : pageSize); i++) { for(int i = 0; i < (value >
		 * list.size() ? list.size():value); i++){ randomList.add(list.get(i));
		 * } page.setEntityCount(pageSize); page.setEntities(randomList); }
		 */
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		if (pageNow == 1) {
			if(param != null){
				if(param.get("table") != null){
					systemPropsKey = (String) param.get("table");
				}
			}
			Integer value = this.getSomeInfoBySystemPropsKey(systemPropsKey);
			List<Tktm> list = this.getYxtkInfoByKsts(-1, -1,param);
			if (list.size() > 0) {
				Collections.shuffle(list);
				randomList = new ArrayList<Tktm>();
				// for (int i = 0; i < (pageSize > list.size() ? list.size()
				// : pageSize); i++) {
				for (int i = 0; i < (value > list.size() ? list.size() : value); i++) {
					randomList.add(list.get(i));
				}
			}else{
				randomList = new ArrayList<Tktm>();
			}
		}
		List<Tktm> pageList = new ArrayList<Tktm>();
		for (int i = (pageNow - 1) * pageSize; i < (pageNow * pageSize > randomList
				.size() ? randomList.size() : pageNow * pageSize); i++) {
			pageList.add(randomList.get(i));
		}

		page.setEntityCount(randomList.size());
		page.setEntities(pageList);

	}

	private List<Tktm> getYxtkInfoByKsts(int begin, int offest, Map<String, Object> param) {
		// TODO Auto-generated method stub
		String flids = null;
		if(param!=null&&param.get("flids")!=null){
			flids  = ((String)param.get("flids"));
		}
		List<Object> args = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder("select  b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*   ");
		sql.append(" FROM tktm a,tkfl b, USER c,dept d,tmly e ");
		sql.append(" WHERE a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.TMLY_ID=e.ID_ AND a.FL_ID=b.ID_ AND a.yxbz='Y' ");
		if(flids!=null){
			sql.append(" and fl_id in ( ");
			sql.append(flids+"'' )");
			
		}
		if (offest == -1 && begin == -1) {
			sql.append(" and xybz='Y' ");
		} else {
			sql.append(" and xybz='N' limit " + begin + "," + offest);
		}
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm yxtk = new Tktm();
						yxtk.setId(result.getString("id_"));
						yxtk.setTkfl(result.getString("TKMC"));
						yxtk.setUser(result.getString("USER_NAME"));
						yxtk.setCreateDate(result.getDate("create_date"));
						yxtk.setSpDate(result.getDate("sp_date"));
						yxtk.setSprId(result.getInt("spr_id"));
						yxtk.setDept(result.getString("DEPT_NAME"));
						yxtk.setContent(result.getString("content"));
						yxtk.setTmfz(result.getDouble("tmfz"));
						yxtk.setTmnd(result.getInt("tmnd"));
						yxtk.setTmly(result.getString("TITLE"));
						yxtk.setMode(result.getString("mode"));
						yxtk.setYxbz(result.getString("yxbz"));
						yxtk.setXybz(result.getString("xybz"));
						return yxtk;
					}

				});
		return list;
	}

	@Override
	public void getExamInfo(Page<Exam> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String sql = "select count(*) from exam";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "select * from exam limit ?,?";
		List<Exam> list = this.jdbcTemplate.query(sql, new Object[]{pageSize * (pageNow - 1), pageSize}, new RowMapper<Exam>(){

			@Override
			public Exam mapRow(ResultSet result, int i) throws SQLException {
				// TODO Auto-generated method stub
				Exam exam = new Exam();
				exam.setId(result.getInt("id_"));
				exam.setStartTime(result.getDate("start_time"));
				exam.setEndTime(result.getDate("end_time"));
				exam.setTitle(result.getString("title"));
				exam.setExamType(result.getString("exam_type"));
				exam.setFqrId(result.getInt("fqr_id"));
				return exam;
			}
			
		});
		page.setEntityCount(count);
		page.setEntities(list);
	}

	@Override
	public void getExamDetailInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		int id = (int) param.get("id");
		StringBuffer sb=new StringBuffer("");
		sb.append(" FROM tktm a,tkfl b, USER c,dept d,tmly e ");
		sb.append(" WHERE a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.TMLY_ID=e.ID_ AND a.FL_ID=b.ID_ AND a.id_ IN ( ");
		sb.append(" SELECT DISTINCT tm_id ");
		sb.append(" FROM exam_detail ");
		sb.append(" WHERE exam_id= ?) ");

		//StringBuffer sb = new StringBuffer("from tktm where id_ in (select distinct tm_id from exam_detail where exam_id="+id+")");
		StringBuffer sqlCount = new StringBuffer("select count(*) ").append(sb);
		StringBuffer sql = new StringBuffer(" SELECT b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  ").append(sb).append(" limit "+(pageNow - 1) * pageSize+","+pageSize);
		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),new Object[]{id}, Integer.class);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),new Object[]{id}, new RowMapper<Tktm>(){

			@Override
			public Tktm mapRow(ResultSet result, int i) throws SQLException {
				// TODO Auto-generated method stub
				Tktm tktm = new Tktm();
				tktm.setId(result.getString("id_"));
				tktm.setTkfl(result.getString("TKMC"));
				tktm.setUser(result.getString("USER_NAME"));
				tktm.setCreateDate(result.getDate("create_date"));
				tktm.setSpDate(result.getDate("sp_date"));
				tktm.setSprId(result.getInt("spr_id"));
				tktm.setDept(result.getString("DEPT_NAME"));
				tktm.setContent(result.getString("content"));
				tktm.setTmfz(result.getDouble("tmfz"));
				tktm.setTmnd(result.getInt("tmnd"));
				tktm.setTmly(result.getString("TITLE"));
				tktm.setMode(result.getString("mode"));
				tktm.setYxbz(result.getString("yxbz"));
				tktm.setXybz(result.getString("xybz"));
				tktm.setDrbz(result.getString("drbz"));
				return tktm;
			}
			
		});
		page.setEntityCount(count);
		page.setEntities(list);
	}

	
	@Override
	public void getZstkInfoByKsts(Page<Tktm> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		Integer flid = (Integer)param.get("flid");
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder count = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇鐢ㄦ埛
			count.append("select count(*) from tktm where xybz='Y' and fl_id= ? ");
			
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			count.append("select count(*) from tktm where xybz='Y' and fl_id= ?  and deptid="
					+ userDetails.getDeptid());
			
		} else if ("USER".equals(roleName)) {// 鏅�氱敤鎴�
			count.append("select count(*) from tktm where xybz='Y' and fl_id= ?  and deptid="
					+ userDetails.getDeptid() + " and user_id="
					+ userDetails.getId());
		}
		int entityCount = this.jdbcTemplate.queryForObject(count.toString(),new Object[]{flid},
				Integer.class);
		List<Tktm> list = this.getZstkInfoByKsts(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getZstkInfoByKsts(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		Integer flid = (Integer)param.get("flid");
		StringBuilder sql = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇绠＄悊鍛�
			sql.append("select  b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*   ");
			sql.append(" FROM tktm a,tkfl b, USER c,dept d,tmly e ");
			sql.append(" WHERE a.xybz='Y' and a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.TMLY_ID=e.ID_ AND a.FL_ID=b.ID_ and a.fl_id = ?  ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			sql.append("select   b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  " );
			sql.append(" FROM tktm a,tkfl b, USER c,dept d,tmly e ");
			sql.append(" WHERE a.xybz='Y' and a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.TMLY_ID=e.ID_ AND a.FL_ID=b.ID_ and a.fl_id = ?  ");
			sql.append(" and a.xybz='Y' and a.deptid= ");
			sql.append(		 userDetails.getDeptid() + " ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sql, param);
			}
		} else if ("USER".equals(roleName)) {// 鏅�氱敤鎴�
			sql.append("select   b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  " );
			sql.append(" FROM tktm a,tkfl b, USER c,dept d,tmly e ");
			sql.append(" WHERE a.xybz='Y' and a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.TMLY_ID=e.ID_ AND a.FL_ID=b.ID_ and a.fl_id = ?  ");
			sql.append(" and a.xybz='Y' and a.deptid= ");
			sql.append(	userDetails.getDeptid() + " and a.user_id= ");
			sql.append(	userDetails.getId() + " ");
		}
		sql.append(" order by a.create_date desc limit " + begin + "," + offest);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),new Object[]{flid},
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm zstk = new Tktm();
						zstk.setId(result.getString("id_"));
						zstk.setTkfl(result.getString("TKMC"));
						zstk.setUser(result.getString("USER_NAME"));
						zstk.setCreateDate(result.getDate("create_date"));
						zstk.setSpDate(result.getDate("sp_date"));
						zstk.setSprId(result.getInt("spr_id"));
						zstk.setDept(result.getString("DEPT_NAME"));
						zstk.setContent(result.getString("content"));
						zstk.setTmfz(result.getDouble("tmfz"));
						zstk.setTmnd(result.getInt("tmnd"));
						zstk.setTmly(result.getString("TITLE"));
						zstk.setMode(result.getString("mode"));
						zstk.setYxbz(result.getString("yxbz"));
						zstk.setXybz(result.getString("xybz"));
						return zstk;
					}

				});
		return list;
	}
	
	
}
