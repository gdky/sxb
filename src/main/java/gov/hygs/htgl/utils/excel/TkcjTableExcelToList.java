package gov.hygs.htgl.utils.excel;

import gov.hygs.htgl.utils.excel.entity.TkcjTable;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import com.bstek.dorado.uploader.UploadFile;

public class TkcjTableExcelToList {

	public static List<Map<String, Object>> explainExcel(UploadFile file,
			Map<String, Object> param) throws Exception {

		String filename = file.getFileName();
		String extname = filename.substring(filename.lastIndexOf('.') + 1);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if ("xls".equalsIgnoreCase(extname) || "xlsx".equalsIgnoreCase(extname)) {

			POIFSFileSystem pfs = new POIFSFileSystem(file.getInputStream());
			HSSFWorkbook wrok = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wrok.getSheetAt(0);

			String[] fields = ((String) param.get("fields")).split(",");
			List<Integer> num = new ArrayList<Integer>();
			List<String> xzx = new ArrayList<String>();

			for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
				HSSFRow row = sheet.getRow(rownum);
				// String xzx[] = {};
				// int num[] = {};

				if (row != null
						&& row.getLastCellNum() > 0
						&& row.getCell(row.getLastCellNum() - 1).getCellType() != Cell.CELL_TYPE_BLANK
						&& row.getCell(0) != null
						&& row.getCell(0).getCellType() != Cell.CELL_TYPE_BLANK) {
					Map<String, Object> rowParam = new HashMap<String, Object>();// 存放答案或其他信息
					TkcjTable tkcjTable = new TkcjTable();
					Class clazz = tkcjTable.getClass();
					int i = 0;
					for (int cellnum = 0; cellnum < row.getLastCellNum() - 1; cellnum++) {// 格
						HSSFCell cell = row.getCell(cellnum);

						if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {

							Method method = clazz.getMethod("set"
									+ fields[cellnum - i],
									new Class[] { String.class });

							if (cell == null
									|| cell.getCellType() == cell.CELL_TYPE_STRING
									|| cell.getCellType() == cell.CELL_TYPE_BLANK) {
								if (cellnum == num.get(i == num.size() ? i - 1
										: i)) {
									// 存储答案或选项
									if (cell != null)
										rowParam.put(
												xzx.get(i),
												"".equals(cell
														.getStringCellValue()
														.trim()) ? null : cell
														.getStringCellValue());
									else
										rowParam.put(xzx.get(i), cell);
									i++;
								} else {

									method.invoke(
											tkcjTable,
											new Object[] { "".equals(cell
													.getStringCellValue()
													.trim()) ? null : cell
													.getStringCellValue() });
								}

							} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
								if (cellnum == num.get(i)) {
									// 存储答案或选项
									rowParam.put(xzx.get(i), chance2String(cell
											.getNumericCellValue()));
									i++;
								} else {
									method.invoke(tkcjTable,
											new Object[] { chance2String(cell
													.getNumericCellValue()) });
								}

							}

						} else {
							if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING
									&& "".equals(row.getCell(0)
											.getStringCellValue().trim())) {
								break;
							}
							if (cell.getStringCellValue().matches(".*[A-Z].*")) {
								num.add(cellnum);
								xzx.add(cell.getStringCellValue());
								i++;
							}// =======================================================================================================================
						}

					}
					if (!rowParam.isEmpty()) {
						rowParam.put("tkcjTable", tkcjTable);
					} else {
						rowParam.put("fields", xzx);
					}
					list.add(rowParam);
				}
			}

		}

		return list;
	}

	public static List<TkcjTable> explainExcel4(UploadFile file,
			Map<String, Object> param) throws Exception {

		String filename = file.getFileName();
		String extname = filename.substring(filename.lastIndexOf('.') + 1);
		List<TkcjTable> list = new ArrayList<TkcjTable>();
		if ("xls".equalsIgnoreCase(extname) || "xlsx".equalsIgnoreCase(extname)) {

			POIFSFileSystem pfs = new POIFSFileSystem(file.getInputStream());
			HSSFWorkbook wrok = new HSSFWorkbook(pfs);
			HSSFSheet sheet = wrok.getSheetAt(0);

			String[] fields = ((String) param.get("fields")).split(",");
			TkcjTable tkcjTable = null;
			for (int rownum = 1; rownum < sheet.getLastRowNum(); rownum++) {
				HSSFRow row = sheet.getRow(rownum);

				if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC
						&& row.getCell(row.getLastCellNum() - 1).getCellType() != Cell.CELL_TYPE_BLANK) {
					tkcjTable = new TkcjTable();
					Class clazz = tkcjTable.getClass();

					for (int cellnum = 1; cellnum < row.getLastCellNum() - 1; cellnum++) {// 格
						HSSFCell cell = row.getCell(cellnum);
						Method method = clazz.getMethod("set"
								+ fields[cellnum - 1],
								new Class[] { String.class });

						if (cell.getCellType() == cell.CELL_TYPE_STRING) {
							method.invoke(
									tkcjTable,
									new Object[] { "".equals(cell
											.getStringCellValue().trim()) ? null
											: cell.getStringCellValue() });
						} else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
							method.invoke(tkcjTable,
									new Object[] { chance2String(cell
											.getNumericCellValue()) });
						}
					}
					list.add(tkcjTable);
				}
			}

		}

		return list;
	}

	private static String chance2String(Serializable num) {
		String str = num.toString();
		return str.substring(0, str.indexOf("."));
	}
}
