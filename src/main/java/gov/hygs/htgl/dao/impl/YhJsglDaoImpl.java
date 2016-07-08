package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.YhJsglDao;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.security.Md5Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;


@Repository
public class YhJsglDaoImpl extends BaseJdbcDao implements YhJsglDao {

	@Override
	public void getUserInfo(Map<String, Object> para, Page page) {
		// TODO Auto-generated method stub
		StringBuilder sqlCount = new StringBuilder(
				"select count(*) from user a where 1=1 ");
		if (para != null) {
			this.rebuildSqlByCondition(sqlCount, para);
		}
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount.toString(),
				Integer.class);

		StringBuilder sql = new StringBuilder(
				"select a.*,b.dept_name deptMc from user a,dept b where a.deptid=b.id_ ");
		if (para != null) {
			this.rebuildSqlByCondition(sql, para);
		}
		sql.append(" limit ?,? ");

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql.toString(),
				new Object[] { page.getPageSize() * (page.getPageNo() - 1),
						page.getPageSize() });
		page.setEntityCount(entityCount);
		page.setEntities(ls);

	}

	private void rebuildSqlByCondition(StringBuilder sql,
			Map<String, Object> param) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = (Date) param.get("begin");
		Date end = (Date) param.get("end");
		String loginName = (String) param.get("loginName");
		String userName = (String) param.get("userName");
		String phone = (String) param.get("phone");
		String zw = (String) param.get("zw");
		String dept = (String) param.get("dept");
		if (begin != null) {
			sql.append(" and a.rzsj >= date_format('" + sdf.format(begin) + "','%Y%m%d') ");
		}
		if (end != null) {
			sql.append(" and a.rzsj <= date_format('" + sdf.format(end) + "','%Y%m%d') ");
		}
		if (loginName != null) {
			sql.append(" and a.login_name like '%" + loginName + "%' ");
		}
		if (userName != null) {
			sql.append(" and a.user_name like '%" + userName + "%' ");
		}
		if (phone != null && !"".equals(phone)) {
			sql.append(" and a.phone like '%" + phone + "%' ");
		}
		if (zw != null && !"".equals(zw)) {
			sql.append(" and a.zw like '%" + zw + "%' ");
		}
		if (dept != null) {
			sql.append(" and a.deptid in (select id_ from dept where dept_name like '%"
					+ dept + "%') ");
		}
	}

	@Override
	public Integer insertUser(User user) {
		// TODO Auto-generated method stub
		String imagePath ="images/mr.jpg";
		String sql = "insert into USER (login_Name,user_Name, phone,rzsj,zw,pwd,photo,deptid,birthday) values(?,?,?,?,?,?,?,?,?) ";
		return this.insertAndGetKeyByJdbc(
				sql,
				new Object[] { user.getLogin_Name(), user.getUser_Name(),
						user.getPhone(), user.getRzsj(), user.getZw(),
						Md5Utils.encodeMd5(user.getPwd()), imagePath, user.getDeptid(),
						user.getBirthday()

				}, new String[] { "id_" }).intValue();
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		String sql = "update USER set login_Name=?,user_Name=?,phone=?,rzsj=?,zw=?,photo=?,deptid=?,birthday=? where id_=? ";
		this.jdbcTemplate.update(
				sql,
				new Object[] { user.getLogin_Name(), user.getLogin_Name(),
						user.getPhone(), user.getRzsj(), user.getZw(),
						 user.getPhoto(), user.getDeptid(),
						user.getBirthday(), user.getId_()

				});

	}
	
	@Override
	public void updateUserPwd(User user) {
		// TODO Auto-generated method stub
		String sql = "update USER set pwd=? where id_=? ";
		this.jdbcTemplate.update(
				sql,
				new Object[] {Md5Utils.encodeMd5(user.getPwd()), user.getId_()

				});

	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		String sql = "delete from USER WHERE id_ = ?";
		this.jdbcTemplate.update(sql, new Object[] { user.getId_() });

	}

	@Override
	public void getRoleInfo(Page page) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from Role";
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				Integer.class);

		String sql = "select * from Role limit ?,? ";

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { page.getPageSize() * (page.getPageNo() - 1),
						page.getPageSize() });
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void getUserInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from user a,user_role b,role c where a.id_=b.user_id and b.role_id =c.id_ and c.id_ =? ";
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				new Object[] { id_ }, Integer.class);

		String sql = "select a.* from  user a,user_role b,role c where a.id_=b.user_id and b.role_id =c.id_ and c.id_ =? limit ?,? ";

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql,
				new Object[] { id_,
						page.getPageSize() * (page.getPageNo() - 1),
						page.getPageSize() });
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void getMenuInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from menu a,role_menu b,role c where a.id_=b.menu_id and b.role_id =c.id_ and c.id_ =? ";
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				new Object[] { id_ }, Integer.class);

		String sql = "select a.* from   menu a,role_menu b,role c where a.id_=b.menu_id and b.role_id =c.id_ and c.id_ =?  limit ?,? ";

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sql,
				new Object[] { id_,
						page.getPageSize() * (page.getPageNo() - 1),
						page.getPageSize() });
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void insertRole(Role role) {
		// TODO Auto-generated method stub
		String sql = "insert into ROLE (ROLE_NAME,MS) values(?,?) ";
		this.jdbcTemplate.update(sql,
				new Object[] { role.getRole_Name(), role.getMs() });
	}

	@Override
	public void updateRole(Role role) {
		// TODO Auto-generated method stub
		String sql = "update ROLE set ROLE_NAME = ? ,MS = ? where ID_ =?  ";
		this.jdbcTemplate
				.update(sql, new Object[] { role.getRole_Name(), role.getMs(),
						role.getId_() });
	}

	@Override
	public void deleteRole(Role role) {
		// TODO Auto-generated method stub
		String sql = " delete from ROLE where ID_ =? ";
		this.jdbcTemplate.update(sql, new Object[] { role.getId_() });
	}

	@Override
	public void insertUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		String sql = " insert into user_role (User_id,role_id) values (?,?) ";
		this.jdbcTemplate.update(sql, new Object[] { user.getId_(), roleId });
	}

	@Override
	public void updateUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		// String sql =" update  ";
	}

	@Override
	public void deleteUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		String sql = " delete from user_role where user_id=? and role_id=? ";
		this.jdbcTemplate.update(sql, new Object[] { user.getId_(), roleId });
	}

	@Override
	public void insertMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql = "  insert into role_menu (menu_id,role_id) values (?,?) ";
		this.jdbcTemplate.update(sql, new Object[] { menu.getId_(), roleId });
	}

	@Override
	public void updateMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql = "";
	}

	@Override
	public void deleteMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql = " delete from role_menu where menu_id=? and role_id=?  ";
		this.jdbcTemplate.update(sql, new Object[] { menu.getId_(), roleId });
	}

	@Override
	public void getMenuInfo(Page page) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from menu";
		int entityCount = this.jdbcTemplate.queryForObject(sqlCount,
				Integer.class);

		String sql = "select * from menu limit ?,? ";

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { page.getPageSize() * (page.getPageNo() - 1),
						page.getPageSize() });
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public List<Map<String, Object>> getMenuRoot(int id_) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer("select a.*,b.role_id ");
		sql.append(" from   menu a,role_menu b,role c where a.id_=b.menu_id and b.role_id =c.id_ and c.id_ =? and a.parent_id is  null ");
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				sql.toString(), new Object[] { id_ });
		return list;
	}

	@Override
	public List<Map<String, Object>> getCurrentMenuById(int id_, int role_id) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer(
				"select a.*,b.role_id from menu a,role_menu b where a.parent_id=? and a.id_=b.menu_id and b.role_id= ? ");

		Object[] obs = { id_, role_id };
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
				sql.toString(), obs);
		return list;
	}

	@Override
	public Boolean saveRoleMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		String sql = "select * from role_menu a ,menu b where a.menu_id=b.parent_id and a.role_id=? and b.id_ = ?";
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { role_id, menu_id });
		if (ls.size() == 0) {
			sql = " select case when parent_id is null then 1 else 0 end parent from menu where id_ = ? ";
			int parent = this.jdbcTemplate.queryForObject(sql,
					new Object[] { menu_id }, Integer.class);
			if (parent == 0) {
				return false;
			} else {
				sql = "insert into role_menu(role_id,menu_id) values (?,?) ";
				this.jdbcTemplate
						.update(sql, new Object[] { role_id, menu_id });
				return true;
			}
		} else {
			sql = "insert into role_menu(role_id,menu_id) values (?,?) ";
			this.jdbcTemplate.update(sql, new Object[] { role_id, menu_id });
			return true;
		}

	}

	@Override
	public Boolean validateMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		String sql = "select * from role_menu a,menu b where a.menu_id = b.id_ and a.role_id = ? and a.menu_id = ? ";
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { role_id, menu_id });
		if (ls.size() > 0) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public Boolean deleteMenu(int role_id, int menu_id) {
		// TODO Auto-generated method stub
		String sql = "select * from role_menu a,menu b where a.menu_id = b.id_ and a.role_id = ? and b.parent_id = ?";
		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(sql,
				new Object[] { role_id, menu_id });
		if (ls.size() > 0) {
			return false;
		} else {
			sql = "delete from role_menu where role_id= ? and menu_id = ?";
			this.jdbcTemplate.update(sql, new Object[] { role_id, menu_id });
			return true;
		}

	}

	@Override
	public String checkLoginName(String param) {
		// TODO Auto-generated method stub
		String sql = "select * from user where login_name=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { param });
		return list.size() > 0 ? "登陆账号'" + param + "'被注册" : null;
	}

	@Override
	public String checkUserName(String param) {
		// TODO Auto-generated method stub
		String sql = "select * from user where user_name=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { param });
		return list.size() > 0 ? "用户名'" + param + "'已存在" : null;
	}

	@Override
	public String checkRoleName(String param) {
		// TODO Auto-generated method stub
		String sql = "select * from Role where role_name=?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				new Object[] { param });
		return list.size() > 0 ? "角色名'" + param + "'已存在" : null;
	}

	@Override
	public String getCurrentUserName() {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		return userDetails.getUser_Name();
	}

	@Override
	public int getRoleIdByRoleName(String roleName) {
		// TODO Auto-generated method stub
		String sql = "select id_ from Role where role_name=?";
		return this.jdbcTemplate.queryForObject(sql,new Object[]{roleName}, Integer.class);
	}

	@Override
	public Map<String, Object> getCurrentUserInfo() {
		// TODO Auto-generated method stub
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		
		String sql = "select * from user where id_ = ? ";
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql,new Object[]{userDetails.getId()});
		return ls.get(0);
	}

	@Override
	public String importImage(Map<String, Object> para) {
		// TODO Auto-generated method stub
		String path = (String)para.get("path");
		String id = (String)para.get("id");
		String sql="update user set photo = ? where id_ = ? ";
		this.jdbcTemplate.update(sql,new Object[]{path,id});
		return null;
	}

}
