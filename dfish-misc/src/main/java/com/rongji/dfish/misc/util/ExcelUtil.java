package com.rongji.dfish.misc.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.rongji.dfish.base.util.LogUtil;
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

import com.rongji.dfish.base.util.Utils;

/**
 * Excel工具类(POI实现方式),用于Excel数据的导入导出,使用此工具类,需要导入的包有:
 * (poi-3.x-yyyyMMdd.jar,poi-ooxml-3.x-yyyyMMdd.jar,poi-ooxml-schemas-3.x-yyyyMMdd.jar,xmlbeans-2.x.jar)
 * 其中方法名中带有HSSF是代表03版Excel的方法,带有XSSF是代表07版Excel的方法
 *
 * @author DFish Team
 */
public class ExcelUtil {
    /**
     * 03版Excel
     */
    public static final String TYPE_HSSF = "xls";
    /**
     * 07版Excel及以上
     */
    public static final String TYPE_XSSF = "xlsx";

    /**
     * 日期格式类型;poi中存的类型是0-5;我们这里使用9,以防版本更新添加了类型导致冲突
     */
    private static final int CELL_TYPE_DATE = 9;

    /**
     * 新建Excel工作簿实例
     * 默认创建03版工作簿
     *
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
     *
     * @param is   输入流
     * @param type 工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版;
     * @return 工作簿
     * @throws Exception 工作簿创建异常
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
     *
     * @param dataList 数据列表
     * @return 工作簿
     */
    public static Workbook createWorkbook(List<Object[]> dataList) {
        return createWorkbook(dataList, TYPE_HSSF);
    }

    /**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     *
     * @param dataList 数据列表
     * @param type     工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版;
     * @return 工作簿
     */
    public static Workbook createWorkbook(List<Object[]> dataList, String type) {
        return createWorkbook(0, "Sheet", dataList, type);
    }

    /**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     *
     * @param sheetIndex 工作表的序号,0代表第1张表
     * @param sheetName  工作表的名称
     * @param dataList   数据列表
     * @param type       工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版;
     * @return 工作簿
     */
    public static Workbook createWorkbook(int sheetIndex, String sheetName, List<Object[]> dataList, String type) {
        Workbook workbook = newWorkbook(type);
        fillWorkbook(workbook, sheetIndex, sheetName, dataList);
        return workbook;
    }

    /**
     * 填充工作簿中工作表的内容
     *
     * @param workbook   工作簿
     * @param sheetIndex 工作表的序号,0代表第1张表
     * @param sheetName  工作表的名称
     * @param dataList   数据列表
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
     *
     * @param dataMap 数据集合
     * @return 工作薄
     */
    public static Workbook createWorkbook(Map<String, List<Object[]>> dataMap) {
        return createWorkbook(dataMap, TYPE_XSSF);
    }

    /**
     * 创建Excel工作簿
     * 默认创建03版工作簿
     *
     * @param dataMap 数据集合
     * @param type    工作簿类型  {@link #TYPE_HSSF}:03版; {@link #TYPE_XSSF}:07版;
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
     *
     * @param row       工作行
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
     *
     * @param cell      单元格
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
     *
     * @param workbook 工作薄
     * @return 数据列表
     */
    public static List<Object[]> getWorkbookData(Workbook workbook) {
        return getWorkbookData(workbook, 0);
    }

    /**
     * 获取工作薄中的数据
     *
     * @param workbook 工作薄
     * @param fromRow  从第几行开始获取数据,0代表第1行
     * @return 数据列表
     */
    public static List<Object[]> getWorkbookData(Workbook workbook, int fromRow) {
        if (workbook == null) {
            return Collections.emptyList();
        }
        int sheetCount = workbook.getNumberOfSheets();
        if (sheetCount > 0) {
            List<Object[]> dataList = new ArrayList<>();
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
     *
     * @param workbook   工作薄
     * @param sheetIndex 工作表的下标,0代表第一张表
     * @param fromRow    从第几行开始获取数据,0代表第1行
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
     *
     * @param workbook  工作薄
     * @param sheetName 工作表的名称
     * @param fromRow   从第几行开始获取数据,0代表第1行
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
     *
     * @param sheet   工作表
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
        List<Object[]> dataList = new ArrayList<>();
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
     *
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
                case Cell.CELL_TYPE_STRING:
                	// 字符型
                    cellValue = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                	// 布尔型
                    cellValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    // 数值型
                case Cell.CELL_TYPE_FORMULA:
                    // 公式
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
            LogUtil.error(null, e);
            cellValue = cell.getRichStringCellValue() != null ? cell.getRichStringCellValue().toString() : null;
        }
        return cellValue;
    }

    /**
     * 获取工作簿中所有的工作表名
     *
     * @param workbook 工作簿
     * @return 工作表名列表
     */
    public static List<String> getSheetNames(Workbook workbook) {
        if (workbook == null) {
            return Collections.emptyList();
        }
        List<String> sheetNames = new ArrayList<>();
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

}
