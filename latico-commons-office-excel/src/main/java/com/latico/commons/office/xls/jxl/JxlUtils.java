package com.latico.commons.office.xls.jxl;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * <PRE>
 *  jxl工具
 * </PRE>
 * @Author: latico
 * @Date: 2019-04-09 10:12:22
 * @Version: 1.0
 */
public class JxlUtils {

	//通过路径获取可写工作簿对象
	public static WritableWorkbook getWritableWorkbook(String path){
		try {
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();			
			}
			WritableWorkbook wwb= Workbook.createWorkbook(file);
			return wwb;
		} catch (IOException e) {
			//如果生成excel文件时，同名正在被打开的文件存在时，则会提示如下信息
			JOptionPane.showMessageDialog(null, e.getMessage(), "生成Excel文件异常提示", JOptionPane.DEFAULT_OPTION);
			e.printStackTrace();
		}
		return null;	
	}
	
	/**
	 * 
	 * @param file
	 * @param sheetName
	 * @param columnNames
	 * @param values
	 * @return
	 */
	public static boolean writeExcel(File file, String sheetName, Object[] columnNames, Object[][] values){
		
		//判断是不是excel文件
		if(!file.getName().matches(".+?\\.(?i)(xls|xlsx)")){
			return false;
		}
		boolean isNewFile = false;
		if(!file.exists()){
			System.out.println("新的excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName);
			isNewFile = true;
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace(System.err);
//				return false;
//			}
		}else{
			System.out.println("已存在的excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName);
		}
		Workbook wb = null;
		//从wb中读取数据，写进数据的文件是ReadXls.file,即是更新操作
		WritableWorkbook wwb = null;
		try {
			if(isNewFile){
				wwb = Workbook.createWorkbook(file);
			}else{
				wb = Workbook.getWorkbook(file);
				wwb = Workbook.createWorkbook(file, wb);
			}
			
			WritableSheet sheet = null;
			if(isNewFile){
				sheet = wwb.createSheet(sheetName, 0);
			}else{
				sheet = wwb.getSheet(sheetName);
				if(sheet == null){
					Sheet[] sheets = wwb.getSheets();
					sheet = wwb.createSheet(sheetName, sheets.length);
				}else{
					sheet.setName(sheetName);
				}
				
			}
			Object value = null;
			Label label = null;
			for(int col=0; col < columnNames.length; col++){
				value = columnNames[col];
				label = new Label(col, 0, value == null? "":value.toString());
				sheet.addCell(label);
			}
			
			
			for(int row=0; row<values.length; row++){
				for(int col=0; col <values[row].length; col++){
					value = values[row][col];
					label = new Label(col, row+1, value == null? "":value.toString());
					sheet.addCell(label);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return false;
		}finally{
			
			try {
				if(wwb != null){
					wwb.write();
				}
			} catch (IOException e) {
			}
			try {
				if(wwb != null){
					wwb.close();
				}
			} catch (Exception e) {
			}
			if(wb != null){
				wb.close();
			}
		}
		System.out.println("excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName +", 操作成功完成");
		return true;
	}
	public static boolean writeExcel(File file, String sheetName, Object[][] values){
		
		//判断是不是excel文件
		if(!file.getName().matches(".+?\\.(?i)(xls|xlsx)")){
			return false;
		}
		boolean isNewFile = false;
		if(!file.exists()){
			System.out.println("新的excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName);
			isNewFile = true;
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(System.err);
				return false;
			}
		}else{
			System.out.println("已存在的excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName);
		}
		Workbook wb = null;
		//从wb中读取数据，写进数据的文件是ReadXls.file,即是更新操作
		WritableWorkbook wwb = null;
		try {
			if(isNewFile){
				wwb = Workbook.createWorkbook(file);
			}else{
				wb = Workbook.getWorkbook(file);
				wwb = Workbook.createWorkbook(file, wb);
			}
			
			WritableSheet sheet = null;
			if(isNewFile){
				sheet = wwb.createSheet(sheetName, 0);
			}else{
				sheet = wwb.getSheet(sheetName);
				if(sheet == null){
					Sheet[] sheets = wwb.getSheets();
					sheet = wwb.createSheet(sheetName, sheets.length);
				}else{
					sheet.setName(sheetName);
				}
				
			}
			Object value = null;
			Label label = null;
			
			for(int row=0; row<values.length; row++){
				for(int col=0; col <values[row].length; col++){
					value = values[row][col];
					label = new Label(col, row, value == null? "":value.toString());
					sheet.addCell(label);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return false;
		}finally{
			
			try {
				if(wwb != null){
					wwb.write();
				}
			} catch (IOException e) {
			}
			try {
				if(wwb != null){
					wwb.close();
				}
			} catch (Exception e) {
			}
			if(wb != null){
				wb.close();
			}
		}
		System.out.println("excel文件："+file.getAbsolutePath() + "，sheet名称：" + sheetName +", 操作成功完成");
		return true;
	}


}
