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
		return null;
	}

}
