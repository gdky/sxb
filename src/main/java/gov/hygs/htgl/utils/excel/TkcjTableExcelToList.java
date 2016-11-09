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
				
				if (row != null && row.getLastCellNum()>0 && row.getCell(row.getLastCellNum() - 1).getCellType() != Cell.CELL_TYPE_BLANK && row.getCell(0) != null) {
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
							
							if (cell == null || cell.getCellType() == cell.CELL_TYPE_STRING
									|| cell.getCellType() == cell.CELL_TYPE_BLANK) {
								if (cellnum == num.get(i == num.size() ? i - 1
										: i)) {
									// 存储答案或选项
									if(cell != null)
										rowParam.put(
											xzx.get(i),
											"".equals(cell.getStringCellValue()
													.trim()) ? null : cell
													.getStringCellValue());
									else
										rowParam.put(xzx.get(i),cell);
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
							/*
							 * Pattern p = Pattern.compile("[A-Z]"); Matcher m =
							 * p.matcher(cell.getStringCellValue());
							 * if(m.find()){ num.add(cellnum);
							 * xzx.add(m.group()); i++; }
							 */
							if (cell.getStringCellValue().matches(".*[A-Z].*")) {
								num.add(cellnum);
								xzx.add(cell.getStringCellValue());
								i++;
							}//=======================================================================================================================
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

	/*
	 * public static List explainExcel0(UploadFile file, Map<String, Object>
	 * param) throws Exception {
	 * 
	 * Class<TkcjTable> clazz = TkcjTable.class; TkcjTable tk =
	 * clazz.newInstance(); Method method = clazz.getMethod("set"+"Mode",
	 * String.class); method.invoke(tk, 1); tk = tk;
	 * 
	 * TkcjTable tk = new TkcjTable(); Class clazz = tk.getClass(); Method
	 * method = clazz.getMethod("set" + "Mode", new Class[] { String.class });
	 * method.invoke(tk, new Object[] { "1" }); tk = tk; return null; }
	 * 
	 * public static List explainExcel3(UploadFile file, String param) {
	 * String[] fields = param.split(","); List<Map<String, Object>> list =
	 * null; Map<String, Object> mapEntity = null;
	 * 
	 * try {
	 * 
	 * list = new ArrayList<Map<String, Object>>(); POIFSFileSystem pfs = new
	 * POIFSFileSystem(file.getInputStream()); HSSFWorkbook wrok = new
	 * HSSFWorkbook(pfs); HSSFSheet sheet = wrok.getSheetAt(0);
	 * 
	 * for (int rownum = 1; rownum < sheet.getLastRowNum(); rownum++) { HSSFRow
	 * row = sheet.getRow(rownum); mapEntity = new HashMap<String, Object>();
	 * 
	 * for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
	 * HSSFCell cell = row.getCell(cellnum);
	 * 
	 * if (cell.getCellType() == cell.CELL_TYPE_STRING) {
	 * mapEntity.put(fields[cellnum], cell.getStringCellValue()); } else if
	 * (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
	 * mapEntity.put(fields[cellnum], cell.getNumericCellValue()); } else if
	 * (cell.getCellType() == cell.CELL_TYPE_BLANK) {
	 * mapEntity.put(fields[cellnum], ""); } } list.add(mapEntity); }
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return list; }
	 * 
	 * public static List explainExcel2(UploadFile file, Map<String, Object>
	 * param) throws Exception { String filename = file.getFileName(); String
	 * extname = filename.substring(filename.lastIndexOf('.') + 1);
	 * List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); if
	 * ("xls".equalsIgnoreCase(extname) || "xlsx".equalsIgnoreCase(extname)) {//
	 * 表
	 * 
	 * POIFSFileSystem pfs = new POIFSFileSystem(file.getInputStream());
	 * HSSFWorkbook wrok = new HSSFWorkbook(pfs); HSSFSheet sheet =
	 * wrok.getSheetAt(0);
	 * 
	 * Map<String, Object> mapEntity = null; for (int rownum = 1; rownum <
	 * sheet.getLastRowNum(); rownum++) {// 行 HSSFRow row =
	 * sheet.getRow(rownum); if (row.getCell(0).getCellType() ==
	 * Cell.CELL_TYPE_NUMERIC && row.getCell(row.getLastCellNum() -
	 * 1).getCellType() != Cell.CELL_TYPE_BLANK) {
	 * 
	 * List<Tkxzx> tkxzxs = new ArrayList<Tkxzx>(); List<Tkxzx> tkdas = new
	 * ArrayList<Tkxzx>();// tkdas不为空则tkda为空，反之一样 mapEntity = new
	 * HashMap<String, Object>(); Tktm tktm = new Tktm(); Tkxzx tkxzx = null;
	 * Tkda tkda = new Tkda();// tkdas不为空则tkda为空，反之一样 Tmly tmly = new Tmly();
	 * Tkfl tkfl = new Tkfl(); User user = new User(); Dept dept = new Dept();
	 * 
	 * for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {// 格
	 * HSSFCell cell = row.getCell(cellnum);
	 * 
	 * switch (cellnum) {
	 * 
	 * case 1: if ("判断".equals(cell.getStringCellValue())) { tktm.setMode("0");
	 * } else if ("单选".equals(cell.getStringCellValue())) { tktm.setMode("1"); }
	 * else if ("多选".equals(cell.getStringCellValue())) { tktm.setMode("2"); }
	 * break; case 2: tkfl.setTkmc(cell.getStringCellValue()); break; case 3: if
	 * ("基础题".equals(cell.getStringCellValue())) { tktm.setTmnd(0); } else if
	 * ("进阶题".equals(cell.getStringCellValue())) { tktm.setTmnd(1); } else if
	 * ("非税收业务类".equals(cell .getStringCellValue())) { tktm.setTmnd(2); } break;
	 * case 4: tktm.setContent(cell.getStringCellValue()); break; case 5: if
	 * (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) { tkxzx = new Tkxzx();
	 * tkxzx.setXzKey("A"); tkxzx.setContent(String.valueOf(cell
	 * .getNumericCellValue())); } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { // if(cell.getStringCellValue() != null){ tkxzx
	 * = new Tkxzx(); tkxzx.setXzKey("A");
	 * tkxzx.setContent(cell.getStringCellValue()); tkxzxs.add(tkxzx); // } }
	 * 
	 * break; case 6: if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) { tkxzx
	 * = new Tkxzx(); tkxzx.setXzKey("B"); tkxzx.setContent(String.valueOf(cell
	 * .getNumericCellValue())); } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { tkxzx = new Tkxzx(); tkxzx.setXzKey("B");
	 * tkxzx.setContent(cell.getStringCellValue()); tkxzxs.add(tkxzx); } break;
	 * case 7: if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) { tkxzx = new
	 * Tkxzx(); tkxzx.setXzKey("C"); tkxzx.setContent(String.valueOf(cell
	 * .getNumericCellValue())); } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { tkxzx = new Tkxzx(); tkxzx.setXzKey("C");
	 * tkxzx.setContent(cell.getStringCellValue()); tkxzxs.add(tkxzx); } break;
	 * case 8: if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) { tkxzx = new
	 * Tkxzx(); tkxzx.setXzKey("D"); tkxzx.setContent(String.valueOf(cell
	 * .getNumericCellValue())); } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { tkxzx = new Tkxzx(); tkxzx.setXzKey("D");
	 * tkxzx.setContent(cell.getStringCellValue()); tkxzxs.add(tkxzx); } break;
	 * case 9: if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) { tkxzx = new
	 * Tkxzx(); tkxzx.setXzKey("E"); tkxzx.setContent(String.valueOf(cell
	 * .getNumericCellValue())); } else if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { tkxzx = new Tkxzx(); tkxzx.setXzKey("E");
	 * tkxzx.setContent(cell.getStringCellValue()); tkxzxs.add(tkxzx); } break;
	 * case 10: if ("正确".equals(cell.getStringCellValue())) { tkxzx = new
	 * Tkxzx(); tkxzx.setXzKey("A"); tkdas.add(tkxzx); } break; case 11: if
	 * ("正确".equals(cell.getStringCellValue())) { tkxzx = new Tkxzx();
	 * tkxzx.setXzKey("B"); tkdas.add(tkxzx); } break; case 12: if
	 * ("正确".equals(cell.getStringCellValue())) { tkxzx = new Tkxzx();
	 * tkxzx.setXzKey("C"); tkdas.add(tkxzx); } break; case 13: if
	 * ("正确".equals(cell.getStringCellValue())) { tkxzx = new Tkxzx();
	 * tkxzx.setXzKey("D"); tkdas.add(tkxzx); } break; case 14: if
	 * ("正确".equals(cell.getStringCellValue())) { tkxzx = new Tkxzx();
	 * tkxzx.setXzKey("E"); tkdas.add(tkxzx); } break; case 15: if
	 * ("正确".equals(cell.getStringCellValue())) { tkda = new Tkda();
	 * tkda.setTkxzxid("1"); } else if ("错误".equals(cell.getStringCellValue()))
	 * { tkda = new Tkda(); tkda.setTkxzxid("0"); } break; case 16:
	 * tmly.setTitle(cell.getStringCellValue()); break; case 17:
	 * tmly.setContent(cell.getStringCellValue()); break; case 18:
	 * user.setUser_Name(cell.getStringCellValue()); break;
	 * 
	 * case 19: dept.setDept_name(cell.getStringCellValue()); break;
	 * 
	 * }
	 * 
	 * } mapEntity.put("dept", dept); mapEntity.put("user", user);
	 * mapEntity.put("tkfl", tkfl); mapEntity.put("tmly", tmly);
	 * mapEntity.put("tktm", tktm); mapEntity.put("tkxzxs", tkxzxs);
	 * mapEntity.put("tkdas", tkdas); mapEntity.put("tkda", tkda);
	 * list.add(mapEntity); }
	 * 
	 * }
	 * 
	 * } else { throw new Exception("不支持该类型导入"); } return list; }
	 */

}
