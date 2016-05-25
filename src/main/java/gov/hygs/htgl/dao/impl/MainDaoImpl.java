package gov.hygs.htgl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.dao.BaseJdbcDao;

import gov.hygs.htgl.dao.MainDao;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
@Repository
public class MainDaoImpl extends BaseJdbcDao  implements MainDao {

	@Override
	public List<Role> getUserRoles(String username) {
		// TODO Auto-generated method stub
		String sql ="select c.* from user a,user_role b,role c where a.id_=b.user_id and b.role_id=c.id_ and a.user_name=?";
		return this.jdbcTemplate.query(sql, new Object[] { username },
				new RoleRowMapper());
	}


	@Override
	public List<Menu> getRoleMenus(Integer id_) {
		// TODO Auto-generated method stub
		String sql ="select a.* from menu a ,role_menu b where a.id_ =b.menu_id and b.role_id = ?";
		return this.jdbcTemplate.query(sql, new Object[] { id_ },
				new MenuRowMapper());
	}
	
	
	private class RoleRowMapper implements RowMapper<Role> {
		public Role mapRow(final ResultSet rs, final int arg1) throws SQLException {
			Role role = new Role();
			role.setId_(rs.getInt("id_"));
			role.setMs(rs.getString("ms"));
			role.setRole_Name(rs.getString("role_Name"));
			return role;
		}
	}
	
	
	private class MenuRowMapper implements RowMapper<Menu> {
		public Menu mapRow(final ResultSet rs, final int arg1) throws SQLException {
			Menu menu = new Menu();
			menu.setId_(rs.getInt("id_"));
			menu.setMenu_Name(rs.getString("menu_Name"));
			menu.setUrl(rs.getString("url"));
			menu.setParent_Id(rs.getInt("parent_Id"));
			return menu;
		}
	}

	private class UserRowMapper implements RowMapper<User> {
		public User mapRow(final ResultSet rs, final int arg1) throws SQLException {
			User user = new User();
			user.setId_(rs.getInt("id_"));
			user.setLogin_Name(rs.getString("login_Name"));
			user.setDeptid(rs.getInt("deptid"));
			user.setPhone(rs.getString("phone"));
			user.setRzsj(rs.getDate("rzsj"));
			user.setPwd(rs.getString("pwd"));
			user.setUser_Name(rs.getString("user_Name"));
			user.setPhoto(rs.getString("photo"));
			return user;
		}
	}

	@Override
	public List<Menu> getUserMenu(Map<String, Object> para) {
		// TODO Auto-generated method stub
		String username =(String)para.get("username");
		StringBuffer sql = new StringBuffer();
		sql.append(" select d.* from user a,user_role b,role_menu c,menu d");
		sql.append(" where a.id_=b.user_id and b.role_id=c.role_id and c.menu_id = d.id_ and d.parent_id is null and a.user_name=? ");
		return this.jdbcTemplate.query(sql.toString(), new Object[] {username  },
				new MenuRowMapper());
	}


	@Override
	public List<Menu> getChildMenus(int id) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from menu where parent_id = ? ");
		return this.jdbcTemplate.query(sql.toString(), new Object[] {id  },
				new MenuRowMapper());
	}


	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		String sql = " select * from user where user_name= ?";
		List<User> users= this.jdbcTemplate.query(sql.toString(), new Object[] {username  },
				new UserRowMapper());
		return users.get(0);
	}


}
