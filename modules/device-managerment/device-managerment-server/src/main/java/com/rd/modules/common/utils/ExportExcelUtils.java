package com.rd.modules.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author xuejh
 * @description 导出excel列表工具类
 * @create 2020-05-28 11:34
 **/
public class ExportExcelUtils {

    /**
     * 报表导出公共方法
     *
     * @param title 表格首行标题
     * @param exportHead 表格列属性名称数组
     * @param dataArray  导出数据集合JSONArray
     * @param totalData 合计数据map集合
     * @param page 分页对象
     */
    public static void exportExcelLargeData(String title, String exportHead, JSONArray dataArray, Map<String, Object> totalData, Page page, ServletOutputStream os) {
        // 创建工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // 表格列标题
        JSONArray exportHeads = JSONArray.parseArray(exportHead);

        // 设置列标题样式
        CellStyle styleHeader = getStyle(workbook, 11, true);

        // 设置数据内容样式
        CellStyle styleContent = getStyle(workbook, 10, false);

        // 设置首行标题样式
        CellStyle styleFirstRow = getStyle(workbook, 15, true);

        // 合计行空行样式
        CellStyle styleTotalRow = workbook.createCellStyle();
        styleTotalRow.setBorderBottom(BorderStyle.THIN); //下边框

        Sheet sheet = null;
        Row row = null;
        Cell cell = null;
        int headerLength = 0;
        headerLength = exportHeads.size();

//        if (page.getPageNo() == 1) {
            // 创建sheet
            sheet = workbook.createSheet(title);
            sheet.setDefaultColumnWidth(10);

            // 合并首行，创建标题
            row = sheet.createRow(0);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellStyle(styleFirstRow);
            cell.setCellValue(new XSSFRichTextString(title));
            CellRangeAddress callRangeAddress = new CellRangeAddress(0, 0, 0, exportHeads.size() - 1);
            sheet.addMergedRegion(callRangeAddress);

            // 生成标题行
            row = sheet.createRow(1);
            row.setHeight((short) 350);

            // 创建明细标题
            if (exportHeads != null) {
                for (int i = 0; i < headerLength; i++) {
                    JSONObject header = exportHeads.getJSONObject(i);
                    cell = row.createCell(i);
                    cell.setCellStyle(styleHeader);
                    RichTextString text = new XSSFRichTextString(header.getString("name"));
                    cell.setCellValue(text);
                }
            }
//        } else {
//            sheet = workbook.getSheet(title);
//        }

        // 遍历数据
        int rowNum = (page.getPageNo() -1) * page.getPageSize();
        int i = 0;
        if (dataArray != null && dataArray.size() > 0) {
            for (i = 0; i < dataArray.size(); i++) {
                row = sheet.createRow(rowNum + i + 2);

                row.setHeight((short) 290);
                JSONObject data = dataArray.getJSONObject(i);
                for (int j = 0; j < headerLength; j++) {
                    JSONObject header = exportHeads.getJSONObject(j);
                    cell = row.createCell(j);
                    cell.setCellStyle(styleContent);

                    String textValue = "";
                    String columnTitle = header.getString("field");
                    // 序号列
                    if (columnTitle.equals("sequence")) {
                        textValue = String.valueOf(rowNum + i + 1);
                    } else {
                        // 处理时间格式数据
                        if (columnTitle.indexOf("Date") != -1) {
                            sheet.setColumnWidth((short) j, (short) 5000);
                            Object value = data.get(columnTitle);
                            if (value != null) {
                                textValue = DateUtils.formatDate((Long)value, "yyyy-MM-dd HH:mm:ss");
                            }
                        } else if (columnTitle.indexOf("state") != -1) {
                            String value = data.getString("state");
                            textValue = DictUtils.getDictLabel(columnTitle, value, "");
                        } else {
                            String value = data.getString(columnTitle);
                            if (StringUtils.isNotBlank(value)) {
                                textValue = value;
                            }
                        }
                    }

                    if (StringUtils.isNotBlank(textValue)) {
                        Boolean isNum = false;//textValue是否为数值型
                        //Boolean isInteger=false;//textValue是否为整数
                        if (textValue != null || !"".equals(textValue)) {
                            //判断data是否为数值型
                            isNum = textValue.matches("^(-?\\d+)(\\.\\d+)?$");
                            //判断data是否为整数（小数部分是否为0）
                            //isInteger=textValue.matches("^[-\\+]?[\\d]*$");
                        }

                        //如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型
                        if (isNum) {
                            // 设置单元格格式
                            cell.setCellType(CellType.NUMERIC);
                            // 设置单元格内容为double类型
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            cell.setCellType(CellType.STRING);
                            // 设置单元格内容为字符型
                            cell.setCellValue(textValue);
                        }
                    }
                }
            }
        }

        // 生成合计行
//        if (page.getPageNo()*page.getPageSize() >= page.getCount()) {
            if (totalData != null && totalData.size() > 0) {
                rowNum = rowNum + i + 2;
                row = sheet.createRow(rowNum);
                row.setHeight((short) 310);
                cell = row.createCell(0);
                cell.setCellStyle(styleHeader);
                cell.setCellValue(new XSSFRichTextString("合计"));

                for (int j = 0; j < exportHeads.size(); j++) {
                    if (row.getCell(j) != null) {
                        continue;
                    }

                    JSONObject header = exportHeads.getJSONObject(j);
                    String columnTitle = header.getString("field");
                    // 该列是否有合计行数据
                    boolean existFlag = false;
                    for (Map.Entry<String, Object> entry : totalData.entrySet()) {
                        if (entry.getKey().equals(columnTitle)) {
                            existFlag = true;

                            String textValue = entry.getValue().toString();
                            cell = row.createCell(j);

                            // 设置单元格格式
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellStyle(styleHeader);
                            // 设置单元格内容为double类型
                            cell.setCellValue(Double.parseDouble(textValue));
                        }
                    }

                    if (!existFlag) {
                        // 没有，则设置空值样式
                        cell = row.createCell(j);
                        cell.setCellType(CellType.STRING);
                        cell.setCellStyle(styleTotalRow);
                        cell.setCellValue("");
                    }
                }
            }
//        }

        // 设置为根据内容自动调整列宽
        setSizeColumn(sheet, headerLength);

        // 输出到文件
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 刷新、关闭输出流
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放资源
            workbook.dispose();
        }
    }

    /**
     * 水平居中、垂直居中
     * 字体：宋体
     * 字体大小：16号
     * 加粗
     * @param workbook
     * @param fontPoints    字号大小
     * @param boldFlag      是否加粗
     * @return
     */
    public static CellStyle getStyle(SXSSFWorkbook workbook, int fontPoints, boolean boldFlag) {
        CellStyle cellstyle=workbook.createCellStyle();
        cellstyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        Font font=workbook.createFont();//字体
        font.setFontHeightInPoints((short)fontPoints);//字号
        font.setBold(boldFlag);//加粗
        cellstyle.setFont(font);
        setBorderStyle(cellstyle);
        return cellstyle;
    }

    /**
     * 边框样式
     * @param style
     */
    public static void setBorderStyle(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
    }

    /**
     * 自适应宽度(中文支持)
     * @author xuejh
     * @create 2020/5/29 10:08
     * @param sheet
     * @param size
     * @return void
     */
    private static void setSizeColumn(Sheet sheet, int size) {
        Cell currentCell;
        Row currentRow;
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
}

