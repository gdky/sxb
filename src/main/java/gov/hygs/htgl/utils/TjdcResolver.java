package gov.hygs.htgl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.ModelAndView;

import com.bstek.dorado.web.resolver.AbstractResolver;

public class TjdcResolver extends AbstractResolver {

	@Override
	protected ModelAndView doHandleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String lx= request.getParameter("lx");
		String swjgJc =request.getParameter("swjgJc");
		if(swjgJc!=null){
			swjgJc =new String(swjgJc.getBytes("iso-8859-1"),"UTF-8");
		}
		String fileName = request.getParameter("fileName");
		//System.out.println(fileName);//GrCtGxz8934724564588283275.xls
		File f = File.createTempFile("assa", ".xls");
		String path = f.getParentFile().getPath();
		f.delete();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			response.setHeader("expires", "0");
			response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("贡献统计_"+sdf.format(new Date())+".xls", "UTF-8"));
			IOUtils.copyLarge(new FileInputStream(path+File.separator+fileName), response.getOutputStream());
			response.flushBuffer();
			return null;
	}
	
}
