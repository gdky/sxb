package gov.hygs.htgl.dao.impl;

import gov.hygs.htgl.dao.QdtjDao;
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
public class QdtjDaoImpl extends BaseJdbcDao implements QdtjDao {

	@Override
	public List<Map<String,Object>> getGroup() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.*,ug.read_mark,count(ug.USER_ID) as zrs,sum(case when ug.read_mark = 'N' then 1 when ug.read_mark = 'Y' then 0 end ) as wqdrs ");
		sb.append(" from grouptable t ");
		sb.append(" left join user_group ug ");
		sb.append(" on t.ID_ = ug.GROUP_ID ");
		sb.append(" where  t.effective_date is not null and now()<= t.effective_date ");
		sb.append(" group by t.ID_ ");
		sb.append(" order by t.ID_ ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		return ls;
	}

	@Override
	public List<Map<String, Object>> getWqd(int ID_) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.login_name as loginname, u.USER_NAME as xming, dp.DEPT_NAME as dept ");
		sb.append(" from (user u,user_group ur) ");
		sb.append(" left join dept dp ");
		sb.append(" on u.deptid = dp.ID_ ");
		sb.append(" where u.ID_ = ur.USER_ID ");
		sb.append(" and ur.GROUP_ID = ? ");
		sb.append(" and ur.read_mark = 'N' ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString(),ID_);
		return ls;
	}

}
