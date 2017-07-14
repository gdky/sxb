package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.ZstkglDao;
import gov.hygs.htgl.entity.Exam;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.security.CustomUserDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
		List<Object> args = new ArrayList<Object>();
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuffer count = new StringBuffer("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇鐢ㄦ埛
			count.append("select count(*) from tktm a where a.xybz='Y'");
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(count, param));
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			count.append("select count(*) from tktm a where a.xybz='Y' and a.deptid=?");
			args.add(userDetails.getDeptid());
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(count, param));
			}
		} else if ("Other".equals(roleName)) {// 鏅�氱敤鎴�
			count.append("select count(*) from tktm a where a.xybz='Y' and a.deptid=? and a.user_id=?");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		int entityCount = this.jdbcTemplate.queryForObject(count.toString(),args.toArray(),
				Integer.class);
		List<Tktm> list = this.getZstkInfo(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getZstkInfo(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("  select b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  ");
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" where a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ ");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇绠＄悊鍛�
			sql.append(" and a.xybz='Y' ");
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(sql, param));
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			sql.append(" and a.xybz='Y' and a.deptid=? ");
			args.add(userDetails.getDeptid());
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(sql, param));
			}
		} else if ("Other".equals(roleName)) {// 鏅�氱敤鎴�
			sql.append(" and a.xybz='Y' and a.deptid=? and a.user_id=? ");
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		sql.append(" order by a.create_date desc limit ?,?");
		args.add(begin);
		args.add(offest);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm zstk = new Tktm();
						zstk.setUser(result.getString("USER_NAME"));
						zstk.setDept(result.getString("DEPT_NAME"));
						zstk.setTmly(result.getString("TITLE"));
						zstk.setTkfl(result.getString("TKMC"));
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
						zstk.setKsbz(result.getString("ksbz"));
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
			aRole = roles.get(0);
			aRole.setRole_Name("Other");
		}
		return aRole;
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
		if (tktmcontent != null) {
			sql.append(" and a.content like ? ");
			args.add("%"+tktmcontent+"%");
		}
		if(ksbz != null){
			if("是".equals(ksbz)){
				sql.append(" and a.KSBZ='Y' ");
			}else if("否".equals(ksbz)){
				sql.append(" and a.KSBZ='N'  ");
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
	public Map<String, Object> getDaMapInfoByZstkId(String id) {
		// TODO Auto-generated method stub
		String sql = "select x.* from tkxzx x,tkda d where x.id_=d.tkxzxid and d.tk_id=? order by x.xz_key";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, new Object[]{ id });
		Map<String,Object> da = null;
		if(list != null){
			da = new HashMap<String, Object>();
			String content = "";
			for(Map<String,Object> das : list){
				if("0".equals(das.get("tk_id"))){
					content += das.get("content")+";";
				}else{
					content += das.get("xz_key")+"、"+das.get("content")+";";
				}
			}
			if(content.length() > 0){
				da.put("content", content.substring(0, content.length()-1));
			}
		}
		return da;
	}
	
	@Override
	public Collection<Tkxzx> getToFInfo() {
		// TODO Auto-generated method stub
		return this.getTkzxzInfoByZstkId("0");
	}

	@Override
	public void addZstk(Tktm zstk) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(zstk);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into tktm values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int tmlyid = this.getTmlyInfoOrAddTmly(zstk.getTmly(), zstk.getTmlyContent());
		Object[] objs = { zstk.getId(), zstk.getFlId(), zstk.getUserId(),
				sdf.format(zstk.getCreateDate()), zstk.getSpDate(),
				zstk.getSprId(), zstk.getDeptid(), zstk.getContent(),
				list.get(0).get("value"), zstk.getTmnd(), tmlyid,//zstk.getTmlyId(),
				zstk.getMode(), "Y", "Y", zstk.getDrbz(),"N" };
		this.jdbcTemplate.update(sql, objs);
	}

	private int getTmlyInfoOrAddTmly(String tmlyTitle, String tmlyContent) {
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

	private List<Map<String, Object>> getSysPropValueByTmnd(Tktm zstk) {
		String sql = "select value from system_props where id_=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { zstk.getTmnd() });
		return list;
	}

	@Override
	public void addGxJl(Tktm zstk) {
		// TODO Auto-generated method stub
		String sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { zstk.getContent(), zstk.getDeptid(),
				zstk.getUserId(), zstk.getId(), this.getGxjlValueByKey("GxzA"), 1, zstk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void addGrDeptGxJl(Tktm zstk) {
		// TODO Auto-generated method stub
		String sql = "insert into tk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { zstk.getContent(), zstk.getDeptid(),
				zstk.getUserId(), zstk.getId(), this.getGxjlValueByKey("GxzB"), 2, zstk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
	}
	
	private String getGxjlValueByKey(String key){
		String sql = "select value from system_props where key_=?";
		return this.jdbcTemplate.queryForObject(sql, new Object[]{key}, String.class);
	}

	@Override
	public void updateZstk(Tktm zstk) {
		// TODO Auto-generated method stub
		this.chackYxtkModeChangeOrNot(zstk);
		List<Map<String, Object>> list = this.getSysPropValueByTmnd(zstk);
		StringBuffer sql = new StringBuffer("update tktm set fl_id=?,user_id=?,create_date=?,");
			sql.append("sp_date=?,spr_id=?,deptid=?,content=?,tmfz=?,tmnd=?,");
			sql.append("tmly_id=?,mode=?,yxbz=?,xybz=?,ksbz=? where id_=?");
		Object[] obj = { zstk.getFlId(), zstk.getUserId(),
				zstk.getCreateDate(), zstk.getSpDate(), zstk.getSprId(),
				zstk.getDeptid(), zstk.getContent(), list.get(0).get("value"),
				zstk.getTmnd(), zstk.getTmlyId(), zstk.getMode(),
				zstk.getYxbz(), zstk.getXybz(), zstk.getKsbz(), zstk.getId() };
		this.jdbcTemplate.update(sql.toString(), obj);
	}

	private void chackYxtkModeChangeOrNot(Tktm zstk) {
		String sql = "select count(*) from tktm where id_=? and mode=? ";
		int count = this.jdbcTemplate.queryForObject(sql, new Object[]{zstk.getId(),zstk.getMode()}, Integer.class);
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
		List<Object> args = new ArrayList<Object>();
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		StringBuffer count = new StringBuffer("select count(*) from tktm a where yxbz='Y' and xybz='N'");
		if (param != null) {
			args.addAll(this.rebuileSqlByConditionAndRole(count, param));
		}
		int entityCount = this.jdbcTemplate
				.queryForObject(count.toString(), args.toArray(), Integer.class);
		List<Tktm> list = this.getYxtkInfo(pageSize * (pageNow - 1), pageSize, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getYxtkInfo(int begin, int offest, Map<String, Object> param) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(" select b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.* ");
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" where a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ ");
			sql.append(" and a.yxbz='Y' ");
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(sql, param));
			}
		if (offest == -1 && begin == -1) {
			sql.append(" and a.xybz='Y' ");
		} else {
			sql.append(" and a.xybz='N' limit ?,?" );
			args.add(begin);
			args.add(offest);
		}
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
				new RowMapper<Tktm>() {

					@Override
					public Tktm mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						Tktm yxtk = new Tktm();
						yxtk.setTkfl(result.getString("TKMC"));
						yxtk.setTmly(result.getString("TITLE"));
						yxtk.setUser(result.getString("USER_NAME"));
						yxtk.setDept(result.getString("DEPT_NAME"));
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
			List<String> groupId = (List<String>) param.get("groupId");
			String ms = (String) param.get("ms");
			Date begin = (Date) param.get("begin");
			Date end = (Date) param.get("end");
			String title = (String) param.get("title");
			String type = (String) param.get("type");
			Integer examtime = (Integer) param.get("examtime");
			String jct = (String) param.get("jct");
			String jjt = (String) param.get("jjt");
			
			String sql = "insert into exam values(?,?,?,?,?,?,?,?,?,?)";
			int jlId = this.insertAndGetKeyByJdbc(
					sql,
					new Object[] { null, begin, end, title, type,
							userDetails.getId(),ms,examtime,Double.parseDouble(jct),Double.parseDouble(jjt) }, new String[] { "id_" })
					.intValue();
			for(int j = 0; j < groupId.size(); j++){
				sql = "insert into exam_tsqz value(?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, groupId.get(j), jlId });
			}
			for (int i = 0; i < ids.size(); i++) {
				sql = "insert into exam_detail values(?,?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, i + 1, jlId,
						ids.get(i) });
			}
		}
	}
	

	@Override
	public void updateKstsjlDetailInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<String> ids = (List<String>) param.get("id");
		if (ids.size() > 0) {
			List<String> groupId = (List<String>) param.get("groupId");
			Integer examid = (Integer) param.get("examid");
			Date begin = (Date) param.get("begin");
			Date end = (Date) param.get("end");
			String title = (String) param.get("title");
			String tpye = (String) param.get("type");
			String ms = (String) param.get("ms");
			Integer examtime = (Integer) param.get("examtime");
			
			String sql = "update exam set start_time=?,end_time=?,exam_type=?,title=?,remark=?,exam_time=? where id_=?";
			this.jdbcTemplate.update(sql, new Object[]{ begin, end, tpye, title, ms, examtime, examid });
			if(groupId != null){
				sql = "delete from exam_tsqz where exam_id = ?";
				this.jdbcTemplate.update(sql, new Object[]{ examid });
				for(int j = 0; j < groupId.size(); j++){
					sql = "insert into exam_tsqz value(?,?,?)";
					this.jdbcTemplate.update(sql, new Object[] { null, groupId.get(j), examid });
				}
			}
			sql = "delete from exam_detail where exam_id=?";
			this.jdbcTemplate.update(sql, new Object[]{ examid });
			for (int i = 0; i < ids.size(); i++) {
				sql = "insert into exam_detail values(?,?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, i + 1, examid,
						ids.get(i) });
			}
		}
	}

	@Override
	public Integer getSomeInfoBySystemPropsKey(String systemPropsKey) {
		// TODO Auto-generated method stub
		String sql = "select value from system_props where key_=?";
		Integer value = Integer.parseInt(this.jdbcTemplate.queryForObject(sql,new Object[]{systemPropsKey},
				String.class));
		return value;
	}

	List<Tktm> randomList = new ArrayList<Tktm>();

	@Override
	public void getRandomTktmFilter(Page<Tktm> page, Map<String, Object> param, String systemPropsKey) {
		// TODO Auto-generated method stub
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
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select  b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*   ");
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" where a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ ");
		if(flids!=null){
			sql.append(" and fl_id in ( ");
			if(flids.contains(",")){
				flids = flids.substring(0, flids.length() - 1);
				String[] flid = flids.split(",");
				for(int i = 0; i < flid.length; i++){
					sql.append("?");
					if(i != flid.length - 1)
						sql.append(",");
					args.add(flid[i]);
				}
			}else{
				sql.append(" ? ");
				args.add(flids);
			}
			sql.append(" ) ");	
		}
		if (offest == -1 && begin == -1) {
			sql.append(" and (xybz='Y' or ksbz='Y') ");
		} else {
			sql.append(" AND a.yxbz='Y' and xybz='N' limit ?,?" );
			args.add(begin);
			args.add(offest);
		}
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
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
						yxtk.setKsbz(result.getString("ksbz"));
						return yxtk;
					}

				});
		return list;
	}

	@Override
	public void getExamInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH24:mm:ss");
		String sql = "select count(*) from exam";
		int count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		sql = "select id_,date_format(start_time,'%Y-%m-%d %T') start_time,date_format(end_time,'%Y-%m-%d %T') end_time,title,exam_type,fqr_id,remark,exam_time,jct,jjt from exam order by start_time desc limit ?,?";
		List<Exam> list = this.jdbcTemplate.query(sql, new Object[]{pageSize * (pageNow - 1), pageSize}, new RowMapper<Exam>(){

			@Override
			public Exam mapRow(ResultSet result, int i) throws SQLException {
				// TODO Auto-generated method stub
				//Date date = result.getDate("start_time");
				Exam exam = new Exam();
				String date = result.getString("start_time");
				if(date != null){
					String[] dates = date.split(" ");
					String[] ymd = dates[0].split("-");
					String[] hms = dates[1].split(":");
					exam.setStartTime(new Date(Integer.parseInt(ymd[0])-1900,Integer.parseInt(ymd[1])-1,Integer.parseInt(ymd[2]),
							Integer.parseInt(hms[0]),Integer.parseInt(hms[1]),Integer.parseInt(hms[2])));
				}
				date = result.getString("end_time");
				if(date != null){
					String[] dates = date.split(" ");
					String[] ymd = dates[0].split("-");
					String[] hms = dates[1].split(":");
					exam.setEndTime(new Date(Integer.parseInt(ymd[0])-1900,Integer.parseInt(ymd[1])-1,Integer.parseInt(ymd[2]),
							Integer.parseInt(hms[0]),Integer.parseInt(hms[1]),Integer.parseInt(hms[2])));
				}
				exam.setId(result.getInt("id_"));
				//exam.setStartTime(result.getDate("start_time"));
				//exam.setEndTime(result.getDate("end_time"));
				exam.setTitle(result.getString("title"));
				exam.setExamType(result.getString("exam_type"));
				exam.setFqrId(result.getInt("fqr_id"));
				exam.setRemark(result.getString("remark"));
				exam.setExamTime(result.getInt("exam_time"));
				exam.setJct(result.getDouble("jct"));
				exam.setJjt(result.getDouble("jjt"));
				return exam;
			}
			
		});
		page.setEntityCount(count);
		page.setEntities(list);
	}

	@Override
	public void getExamDetailInfo(Page<Tktm> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<Object> args = new ArrayList<Object>();
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		int id = (int) param.get("id");
		StringBuffer sb=new StringBuffer("");
		sb.append(" from tktm a ");
		sb.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
		sb.append(" left join tkfl b on a.FL_ID=b.ID_, ");
		sb.append(" USER c,dept d ");
		sb.append(" WHERE a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ AND a.id_ IN ( ");
		sb.append(" SELECT DISTINCT tm_id ");
		sb.append(" FROM exam_detail ");
		sb.append(" WHERE exam_id= ?) ");
		args.add(id);
		StringBuffer sqlCount = new StringBuffer("select count(*) ").append(sb);
		int count = this.jdbcTemplate.queryForObject(sqlCount.toString(),new Object[]{id}, Integer.class);
		StringBuffer sql = new StringBuffer(" SELECT b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  ").append(sb).append(" limit ?,?");
		args.add((pageNow - 1) * pageSize);
		args.add(pageSize);
		
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),args.toArray(), new RowMapper<Tktm>(){

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
				tktm.setKsbz(result.getString("ksbz"));
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
		List<Object> args = new ArrayList<Object>();
		Integer flid = (Integer)param.get("flId");
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuffer count = new StringBuffer("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇鐢ㄦ埛
			count.append("select count(*) from tktm a where (a.xybz='Y' or ksbz='Y') and a.fl_id= ? ");
			args.add(flid);
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(count, param));
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			count.append("select count(*) from tktm a where (a.xybz='Y' or ksbz='Y') and a.fl_id= ?  and a.deptid=?");
			args.add(flid);
			args.add(userDetails.getDeptid());
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(count, param));
			}
		} else if ("Other".equals(roleName)) {// 鏅�氱敤鎴�
			count.append("select count(*) from tktm a where (a.xybz='Y' or ksbz='Y') and a.fl_id= ?  and a.deptid=? and user_id=?");
			args.add(flid);
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		int entityCount = this.jdbcTemplate.queryForObject(count.toString(),args.toArray(),
				Integer.class);
		List<Tktm> list = this.getZstkInfoByKsts(pageSize * (pageNow - 1), pageSize,
				userDetails, roleName, param);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<Tktm> getZstkInfoByKsts(int begin, int offest,
			CustomUserDetails userDetails, String roleName,
			Map<String, Object> param) {
		List<Object> args = new ArrayList<Object>();
		Integer flid = (Integer)param.get("flId");
		StringBuffer sql = new StringBuffer("");
		if ("SuAdmin".equals(roleName)) {// 瓒呯骇绠＄悊鍛�
			sql.append("select  b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*   ");
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" WHERE (a.xybz='Y' or ksbz='Y') and a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ and a.fl_id = ?  ");
			args.add(flid);
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(sql, param));
			}
		} else if ("DeptAdmin".equals(roleName)) {// 閮ㄩ棬绠＄悊鍛�
			sql.append("select   b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  " );
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" WHERE a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ and a.fl_id = ? ");
			sql.append(" and (a.xybz='Y' or ksbz='Y') and a.deptid=? ");
			args.add(flid);
			args.add(userDetails.getDeptid());
			if (param != null) {
				args.addAll(this.rebuileSqlByConditionAndRole(sql, param));
			}
		} else if ("Other".equals(roleName)) {// 鏅�氱敤鎴�
			sql.append("select   b.TKMC,c.USER_NAME,d.DEPT_NAME,e.TITLE,a.*  " );
			sql.append(" from tktm a ");
			sql.append(" left join tmly e on a.TMLY_ID=e.ID_ ");
			sql.append(" left join tkfl b on a.FL_ID=b.ID_, ");
			sql.append(" USER c,dept d ");
			sql.append(" WHERE a.USER_ID=c.ID_ AND a.DEPTID=d.ID_ and a.fl_id = ? ");
			sql.append(" and (a.xybz='Y' or ksbz='Y') and a.deptid=? ");
			sql.append(" and a.user_id=? ");
			args.add(flid);
			args.add(userDetails.getDeptid());
			args.add(userDetails.getId());
		}
		sql.append(" order by a.create_date desc limit ?,?");
		args.add(begin);
		args.add(offest);
		List<Tktm> list = this.jdbcTemplate.query(sql.toString(),args.toArray(),
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
						zstk.setKsbz(result.getString("ksbz"));
						return zstk;
					}

				});
		return list;
	}

	@Override
	public Map<String, Object> getGroupByExamId(String param) {
		// TODO Auto-generated method stub
		Map<String, Object> map = null;
		if(param != null){
			String sql = "select g.group_name from exam_tsqz qz, exam e, grouptable g where e.id_=qz.exam_id and qz.group_id=g.id_ and e.id_=?";
			List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, new Object[]{ param });
			if(list != null){
				map = new HashMap<String, Object>();
				String groupnames = "";
				for(Map<String,Object> groupname : list){
					groupnames += (String)groupname.get("group_name")+",";
				}
				if(groupnames.length() > 0){
					map.put("groupName", groupnames.substring(0, groupnames.length()-1));
				}
			}
		}
		return map;
	}

	@Override
	public void deleteKstsjlInfo(String examid) {
		// TODO Auto-generated method stub
		String sql = "delete from exam where id_=?";
		this.jdbcTemplate.update(sql, new Object[]{ examid });
		sql = "delete from exam_detail where exam_id=?";
		this.jdbcTemplate.update(sql, new Object[]{ examid });
		sql = "delete from exam_tsqz where exam_id=?";
		this.jdbcTemplate.update(sql, new Object[]{ examid });
	}

	@Override
	public void updateExamInfo(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<String> groupId = (List<String>) param.get("groupId");
		Integer examid = (Integer) param.get("examid");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String title = (String) param.get("title");
		String tpye = (String) param.get("type");
		String ms = (String) param.get("ms");
		Integer examtime = (Integer) param.get("examtime");
		String jct = (String) param.get("jct");
		String jjt = (String) param.get("jjt");
		
		String sql = "update exam set start_time=?,end_time=?,exam_type=?,title=?,remark=?,exam_time=?,jct=?,jjt=? where id_=?";
		this.jdbcTemplate.update(sql, new Object[]{ begin, end, tpye, title, ms, examtime,Double.parseDouble(jct),Double.parseDouble(jjt), examid });
		if(groupId != null){
			sql = "delete from exam_tsqz where exam_id = ?";
			this.jdbcTemplate.update(sql, new Object[]{ examid });
			for(int j = 0; j < groupId.size(); j++){
				sql = "insert into exam_tsqz value(?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, groupId.get(j), examid });
			}
		}
	}

}
