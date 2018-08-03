package com.rongji.dfish.misc.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rongji.dfish.base.Utils;

/**
 * Excel工具类(POI实现方式),用于Excel数据的导入导出,使用此工具类,需要导入的包有:
 * (poi-3.x-yyyyMMdd.jar,poi-ooxml-3.x-yyyyMMdd.jar,poi-ooxml-schemas-3.x-yyyyMMdd.jar,xmlbeans-2.x.jar)
 * 其中方法名中带有HSSF是代表03版Excel的方法,带有XSSF是代表07版Excel的方法
 * 
 * @author DFish Team
 * 
 */
public class ExcelUtil {
    // 03版Excel
	public static final String TYPE_HSSF = "xls";
	// 07版Excel及以上
	public static final String TYPE_XSSF = "xlsx";
	
	// 日期格式类型;poi中存的类型是0-5;我们这里使用9,以防版本更新添加了类型导致冲突
	private static final int CELL_TYPE_DATE = 9;

	/**
     * 新建Excel工作簿实例
     * 默认创建03版工作簿
     * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版; 
     * @return 工作簿
     */
	public static Workbook newWorkbook(String type) {
		Workbook workbook = null;
		if (isXssf(type)) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new HSSFWorkbook();
		}
		return workbook;
	}
	
	private static boolean isXssf(String type) {
		return TYPE_XSSF.equals(type) || "1".equals(type); // "1"是兼容旧版的命名
	}
	
	/**
	 * 新建Excel工作簿实例
	 * 默认创建03版工作簿
	 * @param is 输入流
	 * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版; 
	 * @return 工作簿
	 */
	public static Workbook newWorkbook(InputStream is, String type) throws Exception {
		Workbook workbook = null;
		if (isXssf(type)) {
			workbook = new XSSFWorkbook(is);
		} else {
			workbook = new HSSFWorkbook(is);
		}
		return workbook;
	}
	
	/**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     * @param dataList 数据列表
     * @return 工作簿
     */
	public static Workbook createWorkbook(List<Object[]> dataList) {
		return createWorkbook(dataList, TYPE_HSSF);
	}
	
	/**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     * @param dataList 数据列表
     * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版; 
     * @return 工作簿
     */
	public static Workbook createWorkbook(List<Object[]> dataList, String type) {
		return createWorkbook(0, "Sheet", dataList, type);
	}
	
	/**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     * @param sheetIndex 工作表的序号,0代表第1张表
     * @param sheetName 工作表的名称
     * @param dataList 数据列表
     * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版; 
     * @return 工作簿
     */
	public static Workbook createWorkbook(int sheetIndex, String sheetName, List<Object[]> dataList, String type) {
		Workbook workbook = newWorkbook(type);
		fillWorkbook(workbook, sheetIndex, sheetName, dataList);
		return workbook;
	}
	
	/**
     * 填充工作簿中工作表的内容
     * @param workbook 工作簿
     * @param sheetIndex 工作表的序号,0代表第1张表
     * @param sheetName 工作表的名称
     * @param dataList 数据列表
     * @return 工作簿
     */
	public static void fillWorkbook(Workbook workbook, int sheetIndex, String sheetName, List<Object[]> dataList) {
		if (workbook == null) {
			return;
		}
		Sheet sheet = null;
		while (workbook.getNumberOfSheets() <= sheetIndex) {
			workbook.createSheet();
		}
		sheet = workbook.getSheetAt(sheetIndex);
		workbook.setSheetName(sheetIndex, sheetName);
        int fromRow = 0;
        for (Object[] data : dataList) {
        	fillRow(workbook, sheet.createRow(fromRow++), data);
        }
	}
	
	/**
	 * 创建Excel工作簿
	 * 默认创建07版工作簿
	 * @param dataMap 数据集合
	 * @return 工作薄
	 */
	public static Workbook createWorkbook(Map<String, List<Object[]>> dataMap) {
		return createWorkbook(dataMap, TYPE_XSSF);
	}
	
	/**
	 * 创建Excel工作簿
	 * 默认创建03版工作簿
	 * @param dataMap 数据集合
	 * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版;
	 * @return 工作薄
	 */
	public static Workbook createWorkbook(Map<String, List<Object[]>> dataMap, String type) {
		Workbook workbook = newWorkbook(type);
		if (Utils.isEmpty(dataMap)) {
			return workbook;
		}
		int sheetIndex = 0;
        for (Entry<String, List<Object[]>> entry : dataMap.entrySet()) {
        	fillWorkbook(workbook, sheetIndex++, entry.getKey(), entry.getValue());
        }
        return workbook;
	}
	
	/**
	 * 填充行数据
	 * @param row 工作行
	 * @param cellArray 行数据数组
	 */
	private static void fillRow(Workbook workbook, Row row, Object[] cellArray) {
        short column = 0;
        if (cellArray != null && cellArray.length > 1) {
        	int columnIndex = 0;
            for (Object cellValue : cellArray) {
                int cellType = fillCell(workbook, row.createCell(column++, HSSFCell.ENCODING_UTF_16), cellValue);
                if (cellType == CELL_TYPE_DATE) {
                	// 保证日期yyyy-MM-dd能看见
                	row.getSheet().setColumnWidth(columnIndex, 3000);
                }
                columnIndex++;
            }
        }
    }
	
	/**
	 * 填充单元格数据
	 * @param cell 单元格
	 * @param cellValue 数据
	 */
	private static int fillCell(Workbook workbook, Cell cell, Object cellValue) {
		// FIXME 这里的类型理论上应该与#getCellValue中的相对应
		int cellType = Cell.CELL_TYPE_STRING;
        if (cellValue == null) { // 为空不显示
            cell.setCellValue("");
        } else if (cellValue instanceof String) { // 显示字符型
            cell.setCellValue(String.valueOf(cellValue));
        } else if (cellValue instanceof Boolean) { // 显示布尔型
            cell.setCellValue(Boolean.parseBoolean(String.valueOf(cellValue)));
            cellType = Cell.CELL_TYPE_BOOLEAN;
        } else if (cellValue instanceof Number) { // 显示数值型
            cell.setCellValue(Double.parseDouble(String.valueOf(cellValue)));
            cellType = Cell.CELL_TYPE_NUMERIC;
        } else if (cellValue instanceof Date) { // 显示日期型
        	DataFormat df = workbook.createDataFormat();
        	CellStyle cellStyle = workbook.createCellStyle();
        	cellStyle.setDataFormat(df.getFormat("yyyy-MM-dd"));
        	cell.setCellStyle(cellStyle);
        	cell.setCellValue((Date) cellValue);
        	cellType = CELL_TYPE_DATE;
        } else if (cellValue instanceof Calendar) { // 显示日历型
        	DataFormat df = workbook.createDataFormat();
        	CellStyle cellStyle = workbook.createCellStyle();
        	cellStyle.setDataFormat(df.getFormat("yyyy-MM-dd"));
        	cell.setCellStyle(cellStyle);
            cell.setCellValue((Calendar) cellValue);
            cellType = CELL_TYPE_DATE;
        } else {
            // 默认为字符串
            cell.setCellValue(String.valueOf(cellValue));
        }
        if (cellType != CELL_TYPE_DATE) {
        	cell.setCellType(cellType);
        }
        return cellType;
    }
	
	/**
	 * 获取工作薄中的数据
	 * 默认从0行开始
	 * @param workbook 工作薄
	 * @return 数据列表
	 */
	public static List<Object[]> getWorkbookData(Workbook workbook) {
        return getWorkbookData(workbook, 0);
    }
	
	/**
	 * 获取工作薄中的数据
	 * @param workbook 工作薄
	 * @param fromRow 从第几行开始获取数据,0代表第1行
	 * @return 数据列表
	 */
	public static List<Object[]> getWorkbookData(Workbook workbook, int fromRow) {
		if (workbook == null) {
			return Collections.emptyList();
		}
		int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount > 0) {
            List<Object[]> dataList = new ArrayList<Object[]>();
            for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
                dataList.addAll(getWorkbookData(workbook, sheetIndex, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
	}
	
	/**
	 * 获取工作薄中的数据
	 * @param workbook 工作薄
	 * @param sheetIndex 工作表的下标,0代表第一张表
	 * @param fromRow 从第几行开始获取数据,0代表第1行
	 * @return 数据列表
	 */
	public static List<Object[]> getWorkbookData(Workbook workbook, int sheetIndex, int fromRow) {
		if (workbook == null) {
			return Collections.emptyList();
		}
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        return getSheetData(sheet, fromRow);
    }
	
	/**
	 * 获取工作薄中的数据
	 * @param workbook 工作薄
	 * @param sheetName 工作表的名称
	 * @param fromRow 从第几行开始获取数据,0代表第1行
	 * @return 数据列表
	 */
	public static List<Object[]> getWorkbookData(Workbook workbook, String sheetName, int fromRow) {
		if (workbook == null || Utils.isEmpty(sheetName)) {
			return Collections.emptyList();
		}
        Sheet sheet = workbook.getSheet(sheetName);
        return getSheetData(sheet, fromRow);
    }
	
	/**
	 * 获取工作表中的数据
	 * @param sheet 工作表
	 * @param fromRow 从第几行开始获取数据,0代表第1行
	 * @return 数据列表
	 */
	private static List<Object[]> getSheetData(Sheet sheet, int fromRow) {
		if (sheet == null) {
			return Collections.emptyList();
		}
		if (fromRow < 0) {
			fromRow = 0;
		}
		List<Object[]> dataList = new ArrayList<Object[]>();
        if (sheet != null) {
        	// 实际有数据的起始行下标
        	int firstRowIndex = sheet.getFirstRowNum();
        	if (fromRow < firstRowIndex) {
        		fromRow = firstRowIndex;
        	}
        	// 实际有数据最后一行下标
        	int lastRowIndex = firstRowIndex + sheet.getPhysicalNumberOfRows();
            for (int rowIndex = fromRow; rowIndex < lastRowIndex; rowIndex++) {
            	Row row = sheet.getRow(rowIndex);
            	if (row == null) {
            		continue;
            	}
                int cellCount = row.getLastCellNum() + 1;
                Object[] data = new Object[cellCount];
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) {
                        data[cellNum] = "";
                    } else {
                        data[cellNum] = getCellValue(cell);
                    }
                }
                if (!isEmptyData(data)) {
                	dataList.add(data);
                }
            }
        }
        return dataList;
	}
	
	private static boolean isEmptyData(Object[] data) {
		if (data == null) {
			return true;
		}
		for (Object d : data) {
			if (d != null && !"".equals(d)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取单元格数据的值
	 * @param cell 单元格
	 * @return
	 */
	private static Object getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}
        Object cellValue = null;
        try {
        	switch (cell.getCellType()) {
        	case Cell.CELL_TYPE_STRING: // 字符型
        		cellValue = cell.getStringCellValue();
        		break;
        	case Cell.CELL_TYPE_BOOLEAN: // 布尔型
        		cellValue = cell.getBooleanCellValue();
        		break;
        	case Cell.CELL_TYPE_NUMERIC: // 数值型
//        		short dataFormat = cell.getCellStyle().getDataFormat();
//        		|| dataFormat == 14 // yyyy-MM-dd
//        				|| dataFormat == 31 // yyyy年m月d日
//        				|| dataFormat == 57 // yyyy年m月
//        				|| dataFormat == 58 // m月d日
//        				|| dataFormat == 176 //yyyy年MM月dd日??
        		if (DateUtil.isCellDateFormatted(cell)) {
        			cellValue = cell.getDateCellValue();
        		} else {
//        			DecimalFormat df = new DecimalFormat("#.##");
//        			try {
//        				cellValue = df.parse(df.format(cell.getNumericCellValue()));
//        			} catch (ParseException e) {
//        				e.printStackTrace();
//        			}
        			cellValue = cell.getNumericCellValue();
        		}
        		break;
        	case Cell.CELL_TYPE_FORMULA: // 公式
        		//cellValue = cell.getCellFormula();
    			if (DateUtil.isCellDateFormatted(cell)) {  
    				cellValue = cell.getDateCellValue();
    			} else {
    				cellValue = cell.getNumericCellValue();
    			}
        		break;  
        	default:
        		cellValue = cell.getRichStringCellValue().toString();
        		break;
        	}
        } catch (Exception e) {
        	cellValue = cell.getRichStringCellValue() != null ? cell.getRichStringCellValue().toString() : null;
        }
        return cellValue;
    }
	
	/**
	 * 获取工作簿中所有的工作表名
	 * @param workbook 工作簿
	 * @return 工作表名列表
	 */
	public static List<String> getSheetNames(Workbook workbook) {
		if (workbook == null) {
			return Collections.emptyList();
		}
        List<String> sheetNames = new ArrayList<String>();
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }
	
    /*==================03版Excel==================*/
    /**
     * 创建03版Excel的工作簿
     * 
     * @return
     */
	@Deprecated
    private static HSSFWorkbook getNewHSSFWorkbook() {
        return new HSSFWorkbook();
    }

    /**
     * 创建03版Excel工作簿,默认从第1行开始创建,表名为Sheet
     * 
     * @param dataList
     *            需要导出的数据集合,数组的格式顺序与首行字段说明数组一致
     * @return Excel工作簿
     */
	@Deprecated
    public static HSSFWorkbook createHSSFSheet(List<Object[]> dataList) {
        return createHSSFSheet(getNewHSSFWorkbook(), 0, "Sheet", dataList);
    }

    /**
     * 创建03版Excel工作簿
     * 
     * @param workbook
     *            需要填充数据的Excel工作簿,可以为空,若为空则新建一个Excel工作簿
     * @param sheetIndex
     *            表的序号,0代表第1行
     * @param sheetName
     *            表的名称
     * @param dataList
     *            需要导出的数据集合
     * @return 填充好数据的Excel工作簿
     */
	@Deprecated
    public static HSSFWorkbook createHSSFSheet(HSSFWorkbook workbook, int sheetIndex, String sheetName,
            List<Object[]> dataList) {
        workbook = (workbook == null) ? getNewHSSFWorkbook() : workbook;
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetIndex, sheetName);
        int fromRow = 0;
        for (Object[] data : dataList) {
            createHSSFRow(sheet.createRow(fromRow++), data);
        }
        return workbook;
    }

    /**
     * 根据03版Excel工作簿和数据创建Excel工作簿
     * 
     * @param workbook
     *            需要创建表的工作簿
     * @param dataMap
     *            数据集合,Map的key代表表名,value代表数据列表
     * @return 填充好数据的Excel工作簿
     */
	@Deprecated
    public static HSSFWorkbook createHSSFSheets(HSSFWorkbook workbook, Map<String, List<Object[]>> dataMap) {
        int sheetIndex = 0;
        workbook = (workbook == null) ? getNewHSSFWorkbook() : workbook;
        if (dataMap != null) {
            for (Entry<String, List<Object[]>> entry : dataMap.entrySet()) {
                createHSSFSheet(workbook, sheetIndex++, entry.getKey(), entry.getValue());
            }
        }
        return workbook;
    }
    
    /**
     * 读取03版Excel中所有表的所有数据
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @return 表中的数据集合,若无数据返回空集合
     */
	@Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook) {
        int sheetsCount = workbook.getNumberOfSheets();
        int fromRow = 0;
        if (sheetsCount > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
                dataList.addAll(getDataByHSSF(workbook, sheetIndex, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 读取03版Excel中所有表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook, int fromRow) {
        int sheetsCount = workbook.getNumberOfSheets();
        if (sheetsCount > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
                dataList.addAll(getDataByHSSF(workbook, sheetIndex, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 读取03版Excel中选择的多个工作表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetIndexArray
     *            需要导入表下标数组
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook, int[] sheetIndexArray, int fromRow) {
        int sheetsCount = workbook.getNumberOfSheets();
        if (sheetIndexArray != null && sheetIndexArray.length > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex : sheetIndexArray) {
                if (sheetIndex < sheetsCount) {
                    dataList.addAll(getDataByHSSF(workbook, sheetIndex, fromRow));
                }
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 读取03版Excel中选择的工作表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetIndex
     *            Excel文件的第几张表,0代表第1张表
     * @param fromRow
     *            从表的第几行开始读取,0代表第1行
     * @return 读取到的数据集合
     */
    @Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook, int sheetIndex, int fromRow) {
        List<String[]> dataList = new ArrayList<String[]>();
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        HSSFRow row = null;
        if (sheet != null) {
            while ((row = sheet.getRow(fromRow++)) != null) {
                int cellCount = row.getLastCellNum() + 1;
                String[] data = new String[cellCount];
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    HSSFCell cell = row.getCell(cellNum);
                    if (cell == null) {
                        data[cellNum] = "";
                    } else {
                        data[cellNum] = getHSSFCellValue(cell);
                    }
                }
                dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * 得到03版Excel单元格的字符值
     * 
     * @param cell
     *            单元格
     * @return 单元格的字符值
     */
    @Deprecated
    private static String getHSSFCellValue(HSSFCell cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            cellValue = cell.getStringCellValue().trim();
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            cellValue = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
            cellValue = cell.getCellFormula();
            break;
        default:
            cellValue = cell.getStringCellValue();
            break;
        }
        return cellValue;
    }

    /**
     * 读取03版Excel中选择的工作表名称的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetName
     *            Excel文件的工作表的名称
     * @param fromRow
     *            从表的第几行开始读取,0代表第1行
     * @return 读取到的数据集合
     */
    @Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook, String sheetName, int fromRow) {
        List<String[]> dataList = new ArrayList<String[]>();
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFRow row = null;
        if (sheet != null) {
            while ((row = sheet.getRow(fromRow++)) != null) {
                int cellCount = row.getLastCellNum() + 1;
                String[] data = new String[cellCount];
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    HSSFCell cell = row.getCell(cellNum);
                    if (cell == null) {
                        data[cellNum] = "";
                    } else {
                        data[cellNum] = cell.getStringCellValue().trim();
                    }
                }
                dataList.add(data);
            }
        }
        return dataList;
    }

    /**
     * 读取03版Excel中选择的多个工作表名称的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetNameArray
     *            需要导入的工作表的名称
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByHSSF(HSSFWorkbook workbook, String[] sheetNameArray, int fromRow) {
        if (sheetNameArray != null && sheetNameArray.length > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (String sheetName : sheetNameArray) {
                dataList.addAll(getDataByHSSF(workbook, sheetName, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 读取03版Excel工作簿中所有表的名字
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @return 所有表名的数组
     */
    @Deprecated
    public static List<String> getHSSFSheetNames(HSSFWorkbook workbook) {
        List<String> sheetNames = new ArrayList<String>();
        int sheetsCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetsCount; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    /**
     * 获取03版Excel工作簿的中某个工作表数据的物理行数
     * 
     * @param workbook
     *            工作簿
     * @param sheetName
     *            工作表的名称
     * @return 工作表有数据的行数
     */
    @Deprecated
    public static int getRowsByHSSFSheetName(HSSFWorkbook workbook, String sheetName) {
        int rows = 0;
        HSSFSheet sheet = workbook.getSheet(sheetName);
        rows = sheet.getPhysicalNumberOfRows();
        return rows;
    }

    /**
     * 获取03版Excel工作簿的中某个工作表数据的物理行数
     * 
     * @param workbook
     *            工作簿
     * @param sheetNo
     *            工作表的下标(从0开始计算,0代表第1张工作表)
     * @return 工作表有数据的行数
     */
    @Deprecated
    public static int getRowsByHSSFSheetNo(HSSFWorkbook workbook, int sheetNo) {
        int rows = 0;
        HSSFSheet sheet = workbook.getSheetAt(sheetNo);
        rows = sheet.getPhysicalNumberOfRows();
        return rows;
    }

    /**
     * 填充03版Excel表格行数据
     * 
     * @param row
     *            表格的行元素
     * @param cellArray
     *            单元格数据数组
     * @return 表格的行元素
     */
    @Deprecated
    private static HSSFRow createHSSFRow(HSSFRow row, Object[] cellArray) {
        short column = 0;
        if (cellArray != null && cellArray.length > 1) {
            for (Object cellValue : cellArray) {
                createHSSFCell(row, cellValue, column++);
            }
        }
        return row;
    }

    /**
     * 填充03版Excel单元格里的数据
     * 
     * @param row
     *            表格的行元素
     * @param cellValue
     *            单元格的值
     * @param column
     *            行的第几个单元格,0代表第1个单元格
     * @return 单元格元素
     */
    @Deprecated
    private static HSSFCell createHSSFCell(HSSFRow row, Object cellValue, int column) {
        HSSFCell cell = row.createCell(column, HSSFCell.ENCODING_UTF_16);
        if (cellValue == null) {
            cell.setCellValue("");
        } else if (cellValue instanceof String) {
            cell.setCellValue(String.valueOf(cellValue));
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue(Boolean.parseBoolean(String.valueOf(cellValue)));
        } else if (cellValue instanceof Double) {
            cell.setCellValue(Double.parseDouble(String.valueOf(cellValue)));
        } else if (cellValue instanceof Date) {
            cell.setCellValue((Date) cellValue);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
        } else {
            // 默认为字符串
            cell.setCellValue(String.valueOf(cellValue));
        }
        return cell;
    }
    /*==================03版Excel==================*/

    /*==================07版Excel==================*/
    /**
     * 创建07版Excel的工作簿
     * 
     * @return
     */
    @Deprecated
    private static XSSFWorkbook getNewXSSFWorkbook() {
        return new XSSFWorkbook();
    }
    
    /**
     * 创建07版Excel工作簿,默认从第1行开始创建,表名为Sheet
     * 
     * @param dataList
     *            需要导出的数据集合,数组的格式顺序与首行字段说明数组一致
     * @return Excel工作簿
     */
    @Deprecated
    public static XSSFWorkbook createXSSFSheet(List<Object[]> dataList) {
        return createXSSFSheet(getNewXSSFWorkbook(), 0, "Sheet", dataList);
    }
    
    /**
     * 创建07版Excel工作簿
     * 
     * @param workbook
     *            需要填充数据的Excel工作簿,可以为空,若为空则新建一个Excel工作簿
     * @param sheetIndex
     *            表的序号,0代表第1行
     * @param sheetName
     *            表的名称
     * @param dataList
     *            需要导出的数据集合
     * @return 填充好数据的Excel工作簿
     */
    @Deprecated
    public static XSSFWorkbook createXSSFSheet(XSSFWorkbook workbook, int sheetIndex, String sheetName,
            List<Object[]> dataList) {
        workbook = (workbook == null) ? getNewXSSFWorkbook() : workbook;
        XSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetIndex, sheetName);
        int fromRow = 0;
        for (Object[] data : dataList) {
            createXSSFRow(sheet.createRow(fromRow++), data);
        }
        return workbook;
    }
    
    /**
     * 根据07版Excel工作簿和数据创建Excel工作簿
     * 
     * @param workbook
     *            需要创建表的工作簿
     * @param dataMap
     *            数据集合,Map的key代表表名,value代表数据列表
     * @return 填充好数据的Excel工作簿
     */
    @Deprecated
    public static XSSFWorkbook createXSSFSheets(XSSFWorkbook workbook, Map<String, List<Object[]>> dataMap) {
        int sheetIndex = 0;
        workbook = (workbook == null) ? getNewXSSFWorkbook() : workbook;
        if (dataMap != null) {
            for (Entry<String, List<Object[]>> entry : dataMap.entrySet()) {
                createXSSFSheet(workbook, sheetIndex++, entry.getKey(), entry.getValue());
            }
        }
        return workbook;
    }
    
    /**
     * 读取07版Excel中所有表的所有数据
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook) {
        int sheetsCount = workbook.getNumberOfSheets();
        int fromRow = 0;
        if (sheetsCount > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
                dataList.addAll(getDataByXSSF(workbook, sheetIndex, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }
    
    /**
     * 读取07版Excel中所有表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook, int fromRow) {
        int sheetsCount = workbook.getNumberOfSheets();
        if (sheetsCount > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex = 0; sheetIndex < sheetsCount; sheetIndex++) {
                dataList.addAll(getDataByXSSF(workbook, sheetIndex, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }
    
    /**
     * 读取07版Excel中选择的多个工作表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetIndexArray
     *            需要导入表下标数组
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook, int[] sheetIndexArray, int fromRow) {
        int sheetsCount = workbook.getNumberOfSheets();
        if (sheetIndexArray != null && sheetIndexArray.length > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (int sheetIndex : sheetIndexArray) {
                if (sheetIndex < sheetsCount) {
                    dataList.addAll(getDataByXSSF(workbook, sheetIndex, fromRow));
                }
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }
    
    /**
     * 读取07版Excel中选择的工作表的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetIndex
     *            Excel文件的第几张表,0代表第1张表
     * @param fromRow
     *            从表的第几行开始读取,0代表第1行
     * @return 读取到的数据集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook, int sheetIndex, int fromRow) {
        List<String[]> dataList = new ArrayList<String[]>();
        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        XSSFRow row = null;
        if (sheet != null) {
            while ((row = sheet.getRow(fromRow++)) != null) {
                int cellCount = row.getLastCellNum() + 1;
                String[] data = new String[cellCount];
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    XSSFCell cell = row.getCell(cellNum);
                    if (cell == null) {
                        data[cellNum] = "";
                    } else {
                        data[cellNum] = getXSSFCellValue(cell);
                    }
                }
                dataList.add(data);
            }
        }
        return dataList;
    }
    
    /**
     * 得到07版Excel单元格的字符值
     * 
     * @param cell
     *            单元格
     * @return 单元格的字符值
     */
    @Deprecated
    private static String getXSSFCellValue(XSSFCell cell) {
        String cellValue = null;
        switch (cell.getCellType()) {
        case XSSFCell.CELL_TYPE_STRING:
            cellValue = cell.getStringCellValue().trim();
            break;
        case XSSFCell.CELL_TYPE_BOOLEAN:
            cellValue = String.valueOf(cell.getBooleanCellValue());
            break;
        case XSSFCell.CELL_TYPE_NUMERIC:
            cellValue = String.valueOf(cell.getNumericCellValue());
            break;
        case XSSFCell.CELL_TYPE_FORMULA:
            cellValue = cell.getCellFormula();
            break;
        default:
            cellValue = "";
            break;
        }
        return cellValue;
    }
    
    /**
     * 读取07版Excel中选择的工作表名称的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetName
     *            Excel文件的工作表的名称
     * @param fromRow
     *            从表的第几行开始读取,0代表第1行
     * @return 读取到的数据集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook, String sheetName, int fromRow) {
        List<String[]> dataList = new ArrayList<String[]>();
        XSSFSheet sheet = workbook.getSheet(sheetName);
        XSSFRow row = null;
        if (sheet != null) {
            while ((row = sheet.getRow(fromRow++)) != null) {
                int cellCount = row.getLastCellNum() + 1;
                String[] data = new String[cellCount];
                for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                    XSSFCell cell = row.getCell(cellNum);
                    if (cell == null) {
                        data[cellNum] = "";
                    } else {
                        data[cellNum] = cell.getStringCellValue().trim();
                    }
                }
                dataList.add(data);
            }
        }
        return dataList;
    }
    
    /**
     * 读取07版Excel中选择的多个工作表名称的数据(扣除前面几行的内容,一般是标题或说明的行)
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @param sheetNameArray
     *            需要导入的工作表的名称
     * @param fromRow
     *            从表的第几行开始读取数据,0代表第1行
     * @return 表中的数据集合,若无数据返回空集合
     */
    @Deprecated
    public static List<String[]> getDataByXSSF(XSSFWorkbook workbook, String[] sheetNameArray, int fromRow) {
        if (sheetNameArray != null && sheetNameArray.length > 0) {
            List<String[]> dataList = new ArrayList<String[]>();
            for (String sheetName : sheetNameArray) {
                dataList.addAll(getDataByXSSF(workbook, sheetName, fromRow));
            }
            return dataList;
        } else {
            return Collections.emptyList();
        }
    }
    
    /**
     * 读取07版Excel工作簿中所有表的名字
     * 
     * @param workbook
     *            需要导入的Excel文件的工作簿
     * @return 所有表名的数组
     */
    @Deprecated
    public static List<String> getXSSFSheetNames(XSSFWorkbook workbook) {
        List<String> sheetNames = new ArrayList<String>();
        int sheetsCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetsCount; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }
    
    /**
     * 获取07版Excel工作簿的中某个工作表数据的物理行数
     * 
     * @param workbook
     *            工作簿
     * @param sheetName
     *            工作表的名称
     * @return 工作表有数据的行数
     */
    @Deprecated
    public static int getRowsByXSSFSheetName(XSSFWorkbook workbook, String sheetName) {
        int rows = 0;
        XSSFSheet sheet = workbook.getSheet(sheetName);
        rows = sheet.getPhysicalNumberOfRows();
        return rows;
    }
    
    /**
     * 获取07版Excel工作簿的中某个工作表数据的物理行数
     * 
     * @param workbook
     *            工作簿
     * @param sheetNo
     *            工作表的下标(从0开始计算,0代表第1张工作表)
     * @return 工作表有数据的行数
     */
    @Deprecated
    public static int getRowsByXSSFSheetNo(XSSFWorkbook workbook, int sheetNo) {
        int rows = 0;
        XSSFSheet sheet = workbook.getSheetAt(sheetNo);
        rows = sheet.getPhysicalNumberOfRows();
        return rows;
    }
    
    /**
     * 填充07版Excel表格行数据
     * 
     * @param row
     *            表格的行元素
     * @param cellArray
     *            单元格数据数组
     * @return 表格的行元素
     */
    @Deprecated
    private static XSSFRow createXSSFRow(XSSFRow row, Object[] cellArray) {
        short column = 0;
        if (cellArray != null && cellArray.length > 1) {
            for (Object cellValue : cellArray) {
                createXSSFCell(row, cellValue, column++);
            }
        }
        return row;
    }
    
    /**
     * 填充07版Excel单元格里的数据
     * 
     * @param row
     *            表格的行元素
     * @param cellValue
     *            单元格的值
     * @param column
     *            行的第几个单元格,0代表第1个单元格
     * @return 单元格元素
     */
    @Deprecated
    private static XSSFCell createXSSFCell(XSSFRow row, Object cellValue, int column) {
        XSSFCell cell = row.createCell(column);
        if (cellValue == null) {
            cell.setCellValue("");
        } else if (cellValue instanceof String) {
            cell.setCellValue(String.valueOf(cellValue));
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue(Boolean.parseBoolean(String.valueOf(cellValue)));
        } else if (cellValue instanceof Double) {
            cell.setCellValue(Double.parseDouble(String.valueOf(cellValue)));
        } else if (cellValue instanceof Date) {
            cell.setCellValue((Date) cellValue);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
        } else {
            // 默认为字符串
            cell.setCellValue(String.valueOf(cellValue));
        }
        return cell;
    }
    /*==================07版Excel==================*/
}
