package gov.hygs.htgl.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

import gov.hygs.htgl.dao.YhJsglDao;
import gov.hygs.htgl.entity.Menu;
import gov.hygs.htgl.entity.Role;
import gov.hygs.htgl.entity.User;
@Repository
public class YhJsglDaoImpl extends BaseJdbcDao implements YhJsglDao {

	@Override
	public void getUserInfo(Map<String, Object> para,Page page) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from user";
		int entityCount =this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		
		String sql="select * from user limit ?,? ";
		
		List<Map<String, Object>>  ls= this.jdbcTemplate.queryForList(sql,
				new Object[]{page.getPageSize()*(page.getPageNo()-1),page.getPageSize()});
		page.setEntityCount(entityCount);
		page.setEntities(ls);

	}

	@Override
	public void insertUser(User user) {
		// TODO Auto-generated method stub
		String sql ="insert into USER (login_Name,user_Name, phone,rzsj,zw,pwd,photo,deptid,birthday) values(?,?,?,?,?,?,?,?,?) ";
		this.jdbcTemplate.update(sql,new Object[]{
				user.getLogin_Name(),user.getLogin_Name(),user.getPhone(),user.getRzsj(),user.getZw(),user.getPwd(),user.getPhoto(),user.getDeptid(),user.getBirthday()

		});
		
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		String sql ="update USER set login_Name=?,user_Name=?,phone=?,rzsj=?,zw=?,pwd=?,photo=?,deptid=?,birthday=? where id_=? ";
		this.jdbcTemplate.update(sql,new Object[]{
				user.getLogin_Name(),user.getLogin_Name(),user.getPhone(),user.getRzsj(),user.getZw(),user.getPwd(),user.getPhoto(),user.getDeptid(),user.getBirthday(),user.getId_()

		});
		
	}

	@Override
	public void deleteUser(User user) {
		// TODO Auto-generated method stub
		String sql ="delete from USER WHERE id_ = ?";
		this.jdbcTemplate.update(sql,new Object[]{user.getId_()});
				
	}

	@Override
	public void getRoleInfo(Page page) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from Role";
		int entityCount =this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		
		String sql="select * from Role limit ?,? ";
		
		List<Map<String, Object>>  ls= this.jdbcTemplate.queryForList(sql,
				new Object[]{page.getPageSize()*(page.getPageNo()-1),page.getPageSize()});
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void getUserInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from user a,user_role b,role c where a.id_=b.user_id and b.role_id =c.id_ and c.id_ =? ";
		int entityCount =this.jdbcTemplate.queryForObject(sqlCount,new Object[]{id_}, Integer.class);
		
		String sql="select a.* from  user a,user_role b,role c where a.id_=b.user_id and b.role_id =c.id_ and c.id_ =? limit ?,? ";
		
		List<Map<String, Object>>  ls= this.jdbcTemplate.queryForList(sql,
				new Object[]{id_,page.getPageSize()*(page.getPageNo()-1),page.getPageSize()});
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void getMenuInfoByRole(Page page, int id_) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from menu a,role_menu b,role c where a.id_=b.menu_id and b.role_id =c.id_ and c.id_ =? ";
		int entityCount =this.jdbcTemplate.queryForObject(sqlCount,new Object[]{id_}, Integer.class);
		
		String sql="select a.* from   menu a,role_menu b,role c where a.id_=b.menu_id and b.role_id =c.id_ and c.id_ =?  limit ?,? ";
		
		List<Map<String, Object>>  ls= this.jdbcTemplate.queryForList(sql,
				new Object[]{id_,page.getPageSize()*(page.getPageNo()-1),page.getPageSize()});
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}

	@Override
	public void insertRole(Role role) {
		// TODO Auto-generated method stub
		String sql="insert into ROLE (ROLE_NAME,MS) values(?,?) ";
		this.jdbcTemplate.update(sql,new Object[]{
			role.getRole_Name(),role.getMs()	
		});
	}

	@Override
	public void updateRole(Role role) {
		// TODO Auto-generated method stub
		String sql ="update ROLE set ROLE_NAME = ? ,MS = ? where ID_ =?  ";
		this.jdbcTemplate.update(sql,new Object[]{
				role.getRole_Name(),role.getMs(),role.getId_()
			});
	}

	@Override
	public void deleteRole(Role role) {
		// TODO Auto-generated method stub
		String sql =" delete from ROLE where ID_ =? ";
		this.jdbcTemplate.update(sql,new Object[]{
				role.getId_()
			});
	}

	@Override
	public void insertUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		String sql =" insert into user_role (User_id,role_id) values (?,?) ";
		this.jdbcTemplate.update(sql,new Object[]{
				user.getId_(),roleId
			});
	}

	@Override
	public void updateUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		//String sql =" update  ";
	}

	@Override
	public void deleteUserByRole(User user, int roleId) {
		// TODO Auto-generated method stub
		String sql =" delete from user_role where user_id=? and role_id=? ";
		this.jdbcTemplate.update(sql,new Object[]{
				user.getId_(),roleId
			});
	}

	@Override
	public void insertMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql ="  insert into role_menu (menu_id,role_id) values (?,?) ";
		this.jdbcTemplate.update(sql,new Object[]{
				menu.getId_(),roleId
			});
	}

	@Override
	public void updateMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql ="";
	}

	@Override
	public void deleteMenuByRole(Menu menu, int roleId) {
		// TODO Auto-generated method stub
		String sql =" delete from role_menu where menu_id=? and role_id=?  ";
		this.jdbcTemplate.update(sql,new Object[]{
				menu.getId_(),roleId
			});
	}

	@Override
	public void getMenuInfo(Page page) {
		// TODO Auto-generated method stub
		String sqlCount = "select count(*) from menu";
		int entityCount =this.jdbcTemplate.queryForObject(sqlCount, Integer.class);
		
		String sql="select * from menu limit ?,? ";
		
		List<Map<String, Object>>  ls= this.jdbcTemplate.queryForList(sql,
				new Object[]{page.getPageSize()*(page.getPageNo()-1),page.getPageSize()});
		page.setEntityCount(entityCount);
		page.setEntities(ls);
	}


}
