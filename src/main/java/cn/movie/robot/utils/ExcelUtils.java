package cn.movie.robot.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 根据传入的数据生成 Excel
 * 
 */
public class ExcelUtils {

  /**
   * 传入二维数组生成 Excel，第一列默认为表头
   * @param rows
   * @return InputStream
   */
  public static XSSFWorkbook generateWorkbook(List<String[]> rows){
    XSSFWorkbook wb =  new XSSFWorkbook();
    XSSFSheet sheet = wb.createSheet();

    for (int i = 0; i < rows.size(); i++) {
      String[] rowItem = rows.get(i);
      XSSFRow row = sheet.createRow(i);

      for (int j = 0; j < rowItem.length; j++) {
        XSSFCell cell = row.createCell(j);
        cell.setCellValue(rowItem[j]);
      }
    }
    return wb;
  }
  
  /**
   * 传入二维数组生成 Excel，第一列默认为表头
   * @param rows
   * @return InputStream
   */
  public static InputStream generate(List<String[]> rows){
    HSSFWorkbook wb =  new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet();

    for (int i = 0; i < rows.size(); i++) {
      String[] rowItem = rows.get(i);
      HSSFRow row = sheet.createRow(i);

      for (int j = 0; j < rowItem.length; j++) {
        HSSFCell cell = row.createCell(j);
        cell.setCellValue(rowItem[j]);
      }
    }
    return new ByteArrayInputStream(wb.getBytes());
  }

  public static InputStream generateXlsx(List<String[]> rows) throws Exception{
    XSSFWorkbook wb =  new XSSFWorkbook();
    XSSFSheet sheet = wb.createSheet();

    for (int i = 0; i < rows.size(); i++) {
      String[] rowItem = rows.get(i);
      XSSFRow row = sheet.createRow(i);

      for (int j = 0; j < rowItem.length; j++) {
        XSSFCell cell = row.createCell(j);
        cell.setCellValue(rowItem[j]);
      }
    }

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    wb.write(out);
    byte [] bookByteAry = out.toByteArray();

    return new ByteArrayInputStream(bookByteAry);
  }
}
