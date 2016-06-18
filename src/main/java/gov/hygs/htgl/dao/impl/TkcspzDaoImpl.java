package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.TkcspzDao;
import gov.hygs.htgl.entity.Tkfl;
import gov.hygs.htgl.entity.Tmly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class TkcspzDaoImpl extends BaseJdbcDao implements TkcspzDao {

	@Override
	public Collection<Tkfl> getTkflRoot() {
		// TODO Auto-generated method stub
		//String sql = "select * from tkfl where parent_id is null or parent_id = 0 order by id_ desc";
		String sql = "select * from tkfl where parent_id = 0 order by id_ desc";
		List<Tkfl> list = this.jdbcTemplate.query(sql, new RowMapper<Tkfl>() {

			@Override
			public Tkfl mapRow(ResultSet result, int i) throws SQLException {
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
	public Collection<Tkfl> getCurrentTkflById(String id) {
		// TODO Auto-generated method stub
		String sql = "select * from tkfl where parent_id=?";
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
	public void addTkfl(Tkfl tkfl) {
		// TODO Auto-generated method stub
		String sql = "insert into tkfl values(?,?,?,?)";
		this.jdbcTemplate.update(sql,
				new Object[] { tkfl.getId(), tkfl.getParentId(),
						tkfl.getTkmc(), tkfl.getMs() });
	}

	@Override
	public void updateTkfl(Tkfl tkfl) {
		// TODO Auto-generated method stub
		String sql = "update tkfl set parent_id=?,tkmc=?,ms=? where id_=?";
		this.jdbcTemplate.update(sql,
				new Object[] { tkfl.getParentId(), tkfl.getTkmc(),
						tkfl.getMs(), tkfl.getId() });
	}

	@Override
	public void deleteTkfl(Tkfl tkfl) {
		// TODO Auto-generated method stub
		List<Tkfl> tkfls = (List<Tkfl>) this.getCurrentTkflById(tkfl.getId()
				.toString());
		String sql = "delete from tkfl where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { tkfl.getId() });
		for (Tkfl child : tkfls) {
			this.deleteTkfl(child);
		}
	}

	@Override
	public Collection<Tmly> getTmlyInfo() {
		// TODO Auto-generated method stub
		String sql = "select * from tmly";
		List<Tmly> list = this.jdbcTemplate.query(sql, new RowMapper<Tmly>() {

			@Override
			public Tmly mapRow(ResultSet result, int i) throws SQLException {
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
	public void addTmly(Tmly tmly) {
		// TODO Auto-generated method stub
		String sql = "insert into tmly values(?,?,?)";
		this.jdbcTemplate
				.update(sql,
						new Object[] { tmly.getId(), tmly.getTitle(),
								tmly.getContent() });
	}

	@Override
	public void updateTmly(Tmly tmly) {
		// TODO Auto-generated method stub
		String sql = "update tmly set title=?,content=? where id_=?";
		this.jdbcTemplate
				.update(sql, new Object[] { tmly.getTitle(), tmly.getContent(),
						tmly.getId() });
	}

	@Override
	public void deleteTmly(Tmly tmly) {
		// TODO Auto-generated method stub
		String sql = "delete from tmly where id_=?";
		this.jdbcTemplate.update(sql, new Object[] { tmly.getId() });
	}

}
