package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.ZszskDao;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.Yxzsk;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.entity.Zszsk;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.utils.AttachmentOpt;

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
public class ZszskDaoImpl extends BaseJdbcDao implements ZszskDao {

	@Override
	public void getZszskInfo(Page<ZskJl> page, Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String roleName = this.getRoleInfoByUserId(userDetails.getId())
				.getRole_Name();
		StringBuilder sqlCount = new StringBuilder("");
		if ("SuAdmin".equals(roleName)) {// 超级用户
			sqlCount.append("select count(*) from zsk_jl where xybz='Y' ");
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("DeptAdmin".equals(roleName)) {// 部门管理员
			sqlCount.append("select count(*) from zsk_jl where xybz='Y' and deptid="
					+ userDetails.getDeptid());
			if (param != null) {
				this.rebuileSqlByConditionAndRole(sqlCount, param);
			}
		} else if ("USER".equals(roleName)) {// 普通用户
			sqlCount.append("select count(*) from zsk_jl where xybz='Y' and deptid="
					+ userDetails.getDeptid()
					+ " and user_id="
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

		StringBuilder sql = new StringBuilder("");
		sql.append("select * from zsk_jl where xybz='Y' ");
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
		List<ZskJl> list = this.jdbcTemplate.query(sql.toString(),
				new RowMapper<ZskJl>() {

					@Override
					public ZskJl mapRow(ResultSet result, int i)
							throws SQLException {
						// TODO Auto-generated method stub
						ZskJl zszsk = new ZskJl();
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
						zszsk.setXybz(result.getString("xybz"));
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
		String dept = (String) param.get("dept");
		String user = (String) param.get("user");
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
	public void addZszsk(ZskJl zszsk) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into zsk_jl values(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] objs = { zszsk.getId(), zszsk.getUserId(),
				sdf.format(zszsk.getCreateDate()), zszsk.getSpDate(),
				zszsk.getSprId(), zszsk.getDeptid(), zszsk.getContent(),
				zszsk.getZsklyId(), zszsk.getTitle(), "Y", "Y" };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void addGrDeptGxJl(ZskJl zszsk) {
		// TODO Auto-generated method stub
		//String sql = "select value from system_props where id_=111";
		//Integer value = this.jdbcTemplate.queryForObject(sql, Integer.class);
		//sql = "insert into zsk_gxjl values(?,?,?,?,?,?,?)";
		//Object[] objs = { zszsk.getContent(), zszsk.getDeptid(),
				//zszsk.getUserId(), zszsk.getId(), value, 111,
				//zszsk.getCreateDate() };
		String sql = "insert into zsk_gxjl values(?,?,?,?,?,?,?)";
		Object[] objs = { zszsk.getContent(), zszsk.getDeptid(),
				zszsk.getUserId(), zszsk.getId(), 3, 2,
				zszsk.getCreateDate() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void updateZszsk(ZskJl zszsk) {
		// TODO Auto-generated method stub
		String sql = "update zsk_jl set "
				+ "user_id=?,create_date=?,sp_date=?,"
				+ "spr_id=?,deptid=?,content=?,zskly_id=?," + "title=?,xybz=? "
				+ "where id_=?";
		Object[] objs = { zszsk.getUserId(), zszsk.getCreateDate(),
				zszsk.getSpDate(), zszsk.getSprId(), zszsk.getDeptid(),
				zszsk.getContent(), zszsk.getZsklyId(), zszsk.getTitle(),
				zszsk.getXybz(), zszsk.getId() };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void deleteYxzskFormZszsk(Zszsk zszsk) {
		// TODO Auto-generated method stub
		String sql = "update yxzsk set xybz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
	}

	@Override
	public void deleteZszsk(ZskJl zszsk) {
		// TODO Auto-generated method stub
		String sql = "update zsk_jl set xybz='N' where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { zszsk.getId() });
	}

	@Override
	public void deleteGrDeptGxJl(ZskJl zszsk) {
		// TODO Auto-generated method stub
		String sql = "delete from zsk_gxjl where zsk_id=? and gxly=?";
		Object[] objs = { zszsk.getId(), 2 };
		this.jdbcTemplate.update(sql, objs);
	}

	@Override
	public void getYxzskInfo(Page<ZskJl> page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		String count = "select count(*) from zsk_jl where yxbz='Y' and xybz='N'";
		int entityCount = this.jdbcTemplate
				.queryForObject(count, Integer.class);
		List<ZskJl> list = this
				.getYxzskInfo(pageSize * (pageNow - 1), pageSize);
		page.setEntityCount(entityCount);
		page.setEntities(list);
	}

	private List<ZskJl> getYxzskInfo(int begin, int offest) {
		//String sql = "select * from zsk_jl where yxbz='Y' and xybz='N' limit ?,?";
		StringBuilder sql = new StringBuilder("select * from zsk_jl where yxbz='Y'");
		if (offest == -1 && begin == -1) {
			sql.append(" and xybz='Y' ");
		} else {
			sql.append(" and xybz='N' limit " + begin + "," + offest);
		}
		List<ZskJl> list = this.jdbcTemplate.query(sql.toString(), new RowMapper<ZskJl>() {

			@Override
			public ZskJl mapRow(ResultSet result, int i) throws SQLException {
				// TODO Auto-generated method stub
				ZskJl yxzsk = new ZskJl();
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

	List<ZskJl> randomList = new ArrayList<ZskJl>();
	@Override
	public void getRandomdsZszskFilter(Page<ZskJl> page,
			Map<String, Object> param) {
		// TODO Auto-generated method stub
		/*
		int pageSize = page.getPageSize();
		List<ZskJl> list = this.getYxzskInfo(-1, -1);
		if(list.size() > 0){
			Collections.shuffle(list);
			List<ZskJl> randomList = new ArrayList<ZskJl>();
			for (int i = 0; i < (pageSize > list.size() ? list.size()
					: pageSize); i++) {
				randomList.add(list.get(i));
			}
			page.setEntityCount(pageSize);
			page.setEntities(randomList);
		}
		*/
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		if(pageNow == 1){
			Integer value = this.getZsdtsInfoFromSystemProps();
			List<ZskJl> list = this.getYxzskInfo(-1, -1);
			if(list.size() > 0){
				Collections.shuffle(list);
				randomList = new ArrayList<ZskJl>();
				for (int i = 0; i < (value > list.size() ? list.size() : value); i++) {
					randomList.add(list.get(i));
				}
			}
		}
		List<ZskJl> pageList = new ArrayList<ZskJl>();
		for (int i = (pageNow - 1) * pageSize; i < (pageNow * pageSize > randomList
				.size() ? randomList.size() : pageNow * pageSize); i++) {
			pageList.add(randomList.get(i));
		}

		page.setEntityCount(randomList.size());
		page.setEntities(pageList);
	}

	@Override
	public void updateZsdtsInfo(Map<String, Object> param,
			CustomUserDetails userDetails) {
		// TODO Auto-generated method stub
		List<String> ids = (List<String>) param.get("id");
		if (ids.size() > 0) {
			Integer groupId = (Integer) param.get("groupId");
			String ms = (String) param.get("ms");
			Date begin = (Date) param.get("begin");
			Date end = (Date) param.get("end");
			
			String sql = "insert into zsdtsjl values(?,?,?,?)";
			int jlId = this.insertAndGetKeyByJdbc(sql,
					new Object[] { null, userDetails.getId(), new Date(), ms },
					new String[] { "id_" }).intValue();
			sql = "insert into zsktsqz values(?,?,?)";
			this.jdbcTemplate.update(sql, new Object[] { null, groupId, jlId });
			for (String id : ids) {
				sql = "insert into zsktsnr values(?,?,?)";
				this.jdbcTemplate.update(sql, new Object[] { null, jlId, id });
			}
		}
	}

	@Override
	public Collection<Zskly> getZsklyInfo() {
		// TODO Auto-generated method stub
		String sql = "select * from zskly order by id_ desc";
		List<Zskly> list = this.jdbcTemplate.query(sql, new RowMapper<Zskly>(){

			@Override
			public Zskly mapRow(ResultSet result, int i) throws SQLException {
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
	public void addZskly(Zskly zskly) {
		// TODO Auto-generated method stub
		String sql = "insert into zskly values(?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[]{null,zskly.getTitle(),zskly.getContent(),zskly.getAttachment()});
	}

	@Override
	public void updateZskly(Zskly zskly) {
		// TODO Auto-generated method stub
		String sql = "select attachment from zskly where id_=?";
		String attachment = this.jdbcTemplate.queryForObject(sql, new Object[]{zskly.getId()}, String.class);
		if(attachment != null && !attachment.equals(zskly.getAttachment())){
			AttachmentOpt.deleteAttachmentFile(attachment);
		}
		sql = "update zskly set title=?,content=?,attachment=? where id_=?";
		this.jdbcTemplate.update(sql, new Object[]{zskly.getTitle(),zskly.getContent(),zskly.getAttachment(),zskly.getId()});
	}

	@Override
	public void deleteZskly(Zskly zskly) {
		// TODO Auto-generated method stub
		String sql = "delete from zskly where id_=?";
		this.jdbcTemplate.update(sql, new Object[]{zskly.getId()});
	}

	@Override
	public String importAttachment(Map<String, Object> param) {
		// TODO Auto-generated method stub
		
		String path = (String) param.get("path");
		String id = (String) param.get("id");
		String sql = "select attachment from zskly where id_=?";
		String attachment = this.jdbcTemplate.queryForObject(sql, new Object[]{id}, String.class);
		if(attachment != null){
			AttachmentOpt.deleteAttachmentFile(attachment);
		}
		sql = "update zskly set attachment=? where id_=?";
		this.jdbcTemplate.update(sql, new Object[]{path,id});
		return null;
	}

	@Override
	public void cancelUploadAttachmentFile(String param) {
		// TODO Auto-generated method stub
		String sql = "select if(count(*),1,0) from zskly where attachment=?";
		int count = this.jdbcTemplate.queryForObject(sql, new Object[]{param}, Integer.class);
		if(count == 0){
			AttachmentOpt.deleteAttachmentFile(param);
		}
	}

	@Override
	public Integer getZsdtsInfoFromSystemProps() {
		// TODO Auto-generated method stub
		String sql = "select value from system_props where key_='zsdts'";
		Integer value = Integer.parseInt(this.jdbcTemplate.queryForObject(sql,
				String.class));
		return value;
	}

	@Override
	public void getTsxxInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageNow = page.getPageNo();
		int pageSize = page.getPageSize();
		StringBuffer sqlWhere = new StringBuffer(" from zsdtsjl jl,zsktsqz qz,grouptable g,user u "
				+ "where jl.id_ = qz.tsjlid and qz.group_id = g.id_ and jl.tsrid = u.id_ ");
		StringBuffer sql = new StringBuffer("select u.user_name tsr,jl.tsrq,g.group_name groupname,jl.ms,jl.id_ jlid ");
		StringBuffer sqlCount = new StringBuffer("select count(*)");
		int count = this.jdbcTemplate.queryForObject(sqlCount.append(sqlWhere).toString(), Integer.class);
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql.append(sqlWhere).append("limit "+pageSize * (pageNow - 1)+","+pageSize).toString());
		page.setEntityCount(count);
		page.setEntities(list);
	}

	@Override
	public void getZsdDetailInfo(Page page, Map<String, Object> param) {
		// TODO Auto-generated method stub
		int pageNow = page.getPageNo();
		int pageSize = page.getPageSize();
		int id = (int) param.get("id");
		StringBuffer sqlWhere = new StringBuffer(" from zsk_jl jl, zskly ly, dept d, user u ,zsktsnr nr, zsdtsjl z "
				+ "where jl.zskly_id = ly.id_ and jl.user_id = u.id_ and jl.deptid = d.id_ "
				+ "and nr.zskid = jl.id_ and z.id_ = nr.tsjlid and z.id_ = "+id+" ");
		StringBuffer sql = new StringBuffer("select jl.create_date,jl.content,ly.title zsklyname,u.user_name username,d.dept_name deptname,jl.title");
		StringBuffer sqlCount = new StringBuffer("select count(*)");
		int count = this.jdbcTemplate.queryForObject(sqlCount.append(sqlWhere).toString(), Integer.class);
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql.append(sqlWhere).append("limit "+pageSize * (pageNow - 1)+","+pageSize).toString());
		page.setEntityCount(count);
		page.setEntities(list);
	}

}
