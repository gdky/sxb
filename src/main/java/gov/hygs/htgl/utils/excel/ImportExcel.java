package gov.hygs.htgl.utils.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.bstek.dorado.core.io.ResourceUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Component
public class ImportExcel {
	public  String  importExcel(Map beans, String templateFile) throws IOException, ParsePropertyException, InvalidFormatException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {        
    	XLSTransformer transformer = new XLSTransformer();
        com.bstek.dorado.core.io.Resource resource = null;
        //String templateName="Ysykffqktjbg";
        String templateName = templateFile;
        templateFile = "gov/hygs/htgl/utils/excel/templates/"+templateFile+".xls";
        resource = ResourceUtils.getResource(templateFile);
		Workbook workbook =transformer.transformXLS(resource.getInputStream(), beans);
		File outTemp = null;
		try{
			outTemp=File.createTempFile(templateName, ".xls");
			saveWorkbook(workbook,outTemp.getPath());
			String excelName = outTemp.getName();
			return excelName;
			
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			outTemp.deleteOnExit();
		}
		return null;
    }
	public static void saveWorkbook(Workbook resultWorkbook, String fileName)
			throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(
				fileName));
		resultWorkbook.write(os);
		os.flush();
		os.close();
	}
}
