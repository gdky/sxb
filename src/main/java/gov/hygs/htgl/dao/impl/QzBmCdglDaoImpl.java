package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.QXBmCdglDao;
import gov.hygs.htgl.entity.Dept;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bstek.dorado.data.provider.Page;
import com.gdky.restfull.dao.BaseJdbcDao;

@Repository
public class QzBmCdglDaoImpl extends BaseJdbcDao implements QXBmCdglDao {

	@Override
	public List<Dept> getDeptRoot() {
		// TODO Auto-generated method stub
		String sql = "select * from dept t where t.parent_id is null";

		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return this.mapToObject(list);
	}

	@Override
	public List<Dept> getCurrentDeptById(String id_) {
		String sql = "select * from dept t where t.parent_id=?";
		Object[] objs = { id_ };
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				objs);
		return this.mapToObject(list);
	}

	@Override
	public void getCurrentDeptPageById(Page<Dept> page, String id_) {
		// TODO Auto-generated method stub
		int pageSize = page.getPageSize();
		int pageNow = page.getPageNo();
		page.setEntityCount(this.getCount(id_));
		page.setEntities(this.getCurrentDeptPageById(id_, (pageNow - 1)
				* pageSize, pageSize));
	}

	@Override
	public void saveDeptNodeInfo(List<Dept> depts) {
		// TODO Auto-generated method stub
		String sql = "insert into dept values(?,?,?,?)";
		for (Dept dept : depts) {
			Object[] objs = { dept.getId_(), dept.getDept_name(),
					dept.getParentId(), dept.getMs() };
			this.jdbcTemplate.update(sql, objs);
		}

	}

	private int getCount(String id_) {
		String sql = "select count(*) as count from dept t where t.parent_id=?";
		Object[] objs = { id_ };
		Map<String, Object> map = this.jdbcTemplate.queryForMap(sql, objs);
		long countLong = (long) map.get("count");
		int count = (int) countLong;
		return count;
	}

	private List<Dept> getCurrentDeptPageById(String id_, int begin, int offest) {
		String sql = "select * from dept t where t.parent_id=? limit ?,?";
		Object[] objs = { id_, begin, offest };
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,
				objs);
		return this.mapToObject(list);
	}

	private List<Dept> mapToObject(List<Map<String, Object>> list) {
		List<Dept> depts = new ArrayList<Dept>();
		Map<String, Object> map = null;
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Dept dept = new Dept();
				map = list.get(i);
				if (map.get("id_") != null) {
					dept.setId_((Integer) map.get("id_"));
				}

				if (map.get("dept_name") != null) {
					dept.setDept_name((String) map.get("dept_name"));
				}

				if (map.get("parent_id") != null) {
					dept.setParentId((Integer) map.get("parent_id"));
				}

				if (map.get("ms") != null) {
					dept.setMs((String) map.get("ms"));
				}
				depts.add(dept);
			}
		}
		return depts;
	}

}
