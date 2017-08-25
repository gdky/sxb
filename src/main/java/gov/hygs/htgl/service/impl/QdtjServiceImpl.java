package gov.hygs.htgl.service.impl;

import gov.hygs.htgl.dao.QdtjDao;
import gov.hygs.htgl.dao.ZszskDao;
import gov.hygs.htgl.entity.ZskJl;
import gov.hygs.htgl.entity.Zskly;
import gov.hygs.htgl.security.CustomUserDetails;
import gov.hygs.htgl.service.QdtjService;
import gov.hygs.htgl.service.ZszskService;
import gov.hygs.htgl.utils.AttachmentOpt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.uploader.UploadFile;

@Transactional
@Service
public class QdtjServiceImpl implements QdtjService {
	@Resource
	private QdtjDao qdtjDao;

	@Override
	public List<Map<String,Object>> getGroup() {
		return qdtjDao.getGroup();
	}

	@Override
	public List<Map<String, Object>> getWqd(int ID_) {
		return qdtjDao.getWqd(ID_);
	}

	

}
