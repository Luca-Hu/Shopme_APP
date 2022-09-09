package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

public class UserExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook = new XSSFWorkbook();
	private XSSFSheet sheet = workbook.createSheet("Users");
	
	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}
	
	private void writeHeaderLine() {  // write the Header Line
		sheet = workbook.createSheet("Users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // configure the cellStyle for the Header Line's cells
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createCell(row, 0 , "User Id", cellStyle);
		createCell(row, 1 , "Email", cellStyle);
		createCell(row, 2 , "First Name", cellStyle);
		createCell(row, 3 , "Last Name", cellStyle);
		createCell(row, 4 , "Roles", cellStyle);
		createCell(row, 5 , "Enabled", cellStyle);
	}
	
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) { // create a cell
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) { // set value to this cell depend on the value's data-type.
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style); // set style to this cell
	}
	

	private void writeDataLines(List<User> listUsers) {
		int rowIndex = 1; // Note : row == 0 is the Header Line.
		
		XSSFCellStyle cellStyle = workbook.createCellStyle(); // configure the cellStyle for the Data Lines' cells
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for(User user: listUsers) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			
			createCell(row, columnIndex++ , user.getId(), cellStyle);
			createCell(row, columnIndex++ , user.getEmail() , cellStyle);
			createCell(row, columnIndex++ , user.getFirstName() , cellStyle);
			createCell(row, columnIndex++ , user.getLastName() , cellStyle);
			createCell(row, columnIndex++ , user.getRoles().toString(), cellStyle); // Note: need convert set to string.
			createCell(row, columnIndex++ , user.isEnabled(), cellStyle);
		}
		
	}
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException { // export the Excel file
		super.setResponseHeader(response, "application/octet-stream", ".xslx");
		
		writeHeaderLine();
		writeDataLines(listUsers);
		
		ServletOutputStream outputStream = response.getOutputStream(); 
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
}
