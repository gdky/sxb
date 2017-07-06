package gov.hygs.htgl.tkgl.controller;

import gov.hygs.htgl.entity.Tktm;
import gov.hygs.htgl.entity.Tkxzx;
import gov.hygs.htgl.entity.User;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.tkgl.service.TkglService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.dorado.annotation.DataProvider;
import com.bstek.dorado.annotation.DataResolver;
import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;

@Component
public class TkglController {
	@Resource
	TkglService tkglService;

	/**
	 * 获取预选题库信息
	 * 
	 * @param page
	 * @param param
	 */
	@DataProvider
	public void getYxtkInfo(Page<Tktm> page, Map<String, Object> param) {
		tkglService.getYxtkInfo(page, param);
	}

	/**
	 * 获取用户最高权限及其部门
	 * @return
	 */
	@DataProvider
	public Map<String, Object> getRolePower() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		String roleName = userDetails.getRolePower();
		Integer deptid = userDetails.getDeptid();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rolename", roleName);
		map.put("deptid", deptid);
		return map;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param id
	 * @return
	 */
	@DataProvider
	public Collection<User> getUser(String yhm) {
		return tkglService.getUser(yhm);
	}

	/**
	 * 更新预选题库信息
	 * 
	 * @param list
	 */
	@Transactional
	@DataResolver
	public void updateYxtk(List<Tktm> list) {
		//题目
		for (Tktm yxtk : list) {
			if (EntityUtils.getState(yxtk).equals(EntityState.NEW)) {
				yxtk.setId(tkglService.getUUID());
				tkglService.insertYxtk(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.MODIFIED)) {
				tkglService.updateYxtk(yxtk);
			}
			if (EntityUtils.getState(yxtk).equals(EntityState.DELETED)) {
				tkglService.deleteYxtk(yxtk);
			}
			//选择项
			List<Tkxzx> xzs = (List<Tkxzx>) yxtk.getTkxzx();
			if (xzs != null) {
				for (Tkxzx xz : xzs) {
					if (EntityUtils.getState(xz).equals(EntityState.NEW)) {
						xz.setId(tkglService.getUUID());
						xz.setTkId(yxtk.getId());
						tkglService.insertYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.MODIFIED)) {
						tkglService.updateYxtkxzx(xz);
					}
					if (EntityUtils.getState(xz).equals(EntityState.DELETED)) {
						tkglService.deleteYxtkxzx(xz);
					}
				}
			}
			//答案项
			List<Tkxzx> das = (List<Tkxzx>) yxtk.getDaxzx();
			if (das != null) {
				for (Tkxzx da : das) {
					if (EntityUtils.getState(da).equals(EntityState.NEW)) {
						tkglService.insertYxtkda(da,xzs);
					}
					if (EntityUtils.getState(da).equals(EntityState.MODIFIED)) {
						tkglService.updateYxtkda(da);
					}
					if (EntityUtils.getState(da).equals(EntityState.DELETED)) {
						tkglService.deleteYxtkda(da);
					}
				}
			}

		}
	}
}
