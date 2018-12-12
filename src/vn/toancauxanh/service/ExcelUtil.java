package vn.toancauxanh.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zul.Filedownload;

import vn.toancauxanh.gg.model.ChiTietThuChi;
import vn.toancauxanh.gg.model.LyDoThuChi;
import vn.toancauxanh.gg.model.ThuChi;
import vn.toancauxanh.model.Model;
import vn.toancauxanh.model.NhanVien;

public class ExcelUtil {

	private static ExcelUtil instance;

	public static ExcelUtil getInStance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		return instance;
	}
	
	public static String formatDouble(double number) {
		//#.###,##
		final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator(',');
		decimalFormatSymbols.setGroupingSeparator('.');
		final DecimalFormat decimalFormat = new DecimalFormat("#.###", decimalFormatSymbols);
		return decimalFormat.format(number);
	}
	
	private static void setBorderMore(Workbook wb, Row row, Cell c, int begin, int end, int fontSize) {
		final CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setBorderLeft((short) 1); 
		Font font = wb.createFont();
		font.setFontName("Times New Roman");
		
	}
	
	private static void setBorderMore(int flag, Workbook wb, Row row, Cell c, int begin, int end, int fontSize) {
		final CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setBorderLeft((short) 1); 
		
		Font font = wb.createFont();
		font.setFontName("Times New Roman");
		for (int i = begin; i < end; i++) {
			Cell c1 = row.createCell(i);
			if (flag==1) {
				cellStyle.setBorderTop((short) 2);
				font.setFontHeightInPoints((short) fontSize);
				cellStyle.setFont(font);
			} else {
				cellStyle.setBorderTop((short) 1);
			}
			if (flag==2) {
				cellStyle.setBorderBottom((short) 2); 
				font.setFontHeightInPoints((short) fontSize);
				cellStyle.setFont(font);
			} else {
				cellStyle.setBorderBottom((short) 1); 
			}
			
			if (i == (end-1)) {
				cellStyle.setBorderRight((short) 2);
			} else {
				cellStyle.setBorderRight((short) 1); 
			}
			if (flag==3) {
				cellStyle.setBorderTop((short) 1);
				cellStyle.setBorderBottom((short) 1); 
				font.setFontHeightInPoints((short) fontSize);
				cellStyle.setFont(font);
				cellStyle.setBorderRight((short) 1); 
				font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			} 
			c1.setCellStyle(cellStyle);
		}
	}
	
	public static CellStyle setBorderAndFont(final Workbook workbook,
			final int borderSize, final boolean isTitle, final int fontSize,
			final String fontColor, final String textAlign) {
		final CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		

		cellStyle.setBorderTop((short) borderSize); // single line border
		cellStyle.setBorderBottom((short) borderSize); // single line border
		cellStyle.setBorderLeft((short) borderSize); // single line border
		cellStyle.setBorderRight((short) borderSize); // single line border
		cellStyle.setAlignment(CellStyle.VERTICAL_CENTER);

		if (textAlign.equals("RIGHT")) {
			cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		} else if (textAlign.equals("CENTER")) {
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		} else if (textAlign.equals("LEFT")) {
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		}
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
		final Font font = workbook.createFont();
		font.setFontName("Times New Roman");
		if (isTitle) {
			
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			
		} 
		if (fontColor.equals("RED")) {
			font.setColor(Font.COLOR_RED);
		} else if (fontColor.equals("BLUE")) {
			font.setColor(HSSFColor.BLUE.index);
		} else if (fontColor.equals("ORANGE")){
			font.setColor(HSSFColor.ORANGE.index);
		} else {
			
		}
		font.setFontHeightInPoints((short) fontSize);
		cellStyle.setFont(font);

		return cellStyle;
	}
	/*
	public static void exportThongKe(String title, String fileName, 
			String sheetname, List<Object[]> list) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		try {
		Cell c = null;
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet(sheetname);
		// Row and column indexes
		int idx = 0;
		// Generate column headings
		Row row;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(1);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(2);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(3);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(4);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		// set column width
		sheet1.setColumnWidth(0, 30 * 256);
		sheet1.setColumnWidth(1, 16 * 256);
		sheet1.setColumnWidth(2, 16 * 256);
		sheet1.setColumnWidth(3, 16 * 256);
		sheet1.setColumnWidth(4, 16 * 256);
		sheet1.setColumnWidth(5, 16 * 256);
		// Generate rows header of grid
		idx++;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue("Tên đơn vị");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(1);
		c.setCellValue("Việc đã giao");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(2);
		c.setCellValue("Chưa xử lý");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(3);
		c.setCellValue("Đã xử lý");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(4);
		c.setCellValue("Trễ hạn");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		
		idx++;
		int i = 1;
		CellStyle cellStyleLeft = setBorderAndFont(wb, 1, i == list.size(), 11, "","LEFT");
		CellStyle cellStyleCenter = setBorderAndFont(wb, 1, i == list.size(), 11, "","CENTER");
		for (Object[] ob: list) {
			row = sheet1.createRow(idx);
			c = row.createCell(0);			
			c.setCellValue(ob[0] + "");
			c.setCellStyle(cellStyleLeft);				
			c = row.createCell(1);
			c.setCellValue(ob[1] + "");
			c.setCellStyle(cellStyleCenter);				
			c = row.createCell(2);
			c.setCellValue(ob[2] + "");
			c.setCellStyle(cellStyleCenter);		
			c = row.createCell(3);
			c.setCellValue(ob[3] + "");
			c.setCellStyle(cellStyleCenter);		
			c = row.createCell(4);
			c.setCellValue(ob[4] + "");
			c.setCellStyle(cellStyleCenter);
			i++;
			idx++;
		}
		
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		wb.write(fileOut);
		Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	*/
	public static void exportThongKeChiTiet(String title, String fileName, 
			String sheetname, List<Object[]> list) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		try {
		Cell c = null;
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet(sheetname);
		// Row and column indexes
		int idx = 0;
		// Generate column headings
		Row row;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(1);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(2);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(3);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(4);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(5);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// set column width
		sheet1.setColumnWidth(0, 7 * 256);
		sheet1.setColumnWidth(1, 30 * 256);
		sheet1.setColumnWidth(2, 16 * 256);
		sheet1.setColumnWidth(3, 16 * 256);
		sheet1.setColumnWidth(4, 16 * 256);
		sheet1.setColumnWidth(5, 16 * 256);
		sheet1.setColumnWidth(6, 16 * 256);
		// Generate rows header of grid
		idx++;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue("STT");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(1);
		c.setCellValue("Nội dung công việc");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(2);
		c.setCellValue("Văn bản chỉ đạo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(3);
		c.setCellValue("Loại công việc");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(4);
		c.setCellValue("Thời hạn báo cáo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(5);
		c.setCellValue("Tình trạng");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		
		idx++;
		int i = 1;
		CellStyle cellStyleLeft = setBorderAndFont(wb, 1, i == list.size(), 11, "","LEFT");
		CellStyle cellStyleCenter = setBorderAndFont(wb, 1, i == list.size(), 11, "","CENTER");
		for (Object[] ob: list) {
			row = sheet1.createRow(idx);
			c = row.createCell(0);			
			c.setCellValue(ob[0] + "");
			c.setCellStyle(cellStyleCenter);				
			c = row.createCell(1);
			c.setCellValue(ob[1] + "");
			c.setCellStyle(cellStyleLeft);				
			c = row.createCell(2);
			c.setCellValue(ob[2] + "");
			c.setCellStyle(cellStyleCenter);		
			c = row.createCell(3);
			c.setCellValue(ob[3] + "");
			c.setCellStyle(cellStyleLeft);		
			c = row.createCell(4);
			c.setCellValue(ob[4] + "");
			c.setCellStyle(cellStyleCenter);
			c = row.createCell(5);
			c.setCellValue(ob[5] + "");
			c.setCellStyle(cellStyleCenter);
			i++;
			idx++;
		}
		
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		wb.write(fileOut);
		Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	
	public static void exportChiDaoDieuHanh(String title, String fileName, 
			String sheetname, List<Object[]> list) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		try {
		Cell c = null;
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet(sheetname);
		// Row and column indexes
		int idx = 0;
		// Generate column headings
		Row row;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(1);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(2);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(3);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(4);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		// set column width
		sheet1.setColumnWidth(0, 16 * 256);
		sheet1.setColumnWidth(1, 30 * 256);
		sheet1.setColumnWidth(2, 36 * 256);
		sheet1.setColumnWidth(3, 16 * 256);
		sheet1.setColumnWidth(4, 16 * 256);
		// Generate rows header of grid
		idx++;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue("STT");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(1);
		c.setCellValue("Nội dung chỉ đạo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(2);
		c.setCellValue("Đơn vị thực hiện");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(3);
		c.setCellValue("Thời hạn báo cáo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(4);
		c.setCellValue("Tình trạng xử lý");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		
		idx++;
		int i = 1;
		CellStyle cellStyleLeft = setBorderAndFont(wb, 1, i == list.size(), 11, "","LEFT");
		CellStyle cellStyleCenter = setBorderAndFont(wb, 1, i == list.size(), 11, "","CENTER");
		for (Object[] ob: list) {
			row = sheet1.createRow(idx);
			c = row.createCell(0);			
			c.setCellValue(ob[0] + "");
			c.setCellStyle(cellStyleCenter);				
			c = row.createCell(1);
			c.setCellValue(ob[1] + "");
			c.setCellStyle(cellStyleLeft);				
			c = row.createCell(2);
			c.setCellValue(ob[2] + "");
			c.setCellStyle(cellStyleLeft);		
			c = row.createCell(3);
			c.setCellValue(ob[3] + "");
			c.setCellStyle(cellStyleCenter);	
			c = row.createCell(4);
			c.setCellValue(ob[4] + "");
			c.setCellStyle(cellStyleCenter);
			i++;
			idx++;
		}
		
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		wb.write(fileOut);
		Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	public static void exportDonThu(String title, String fileName, 
			String sheetname, List<Object[]> list) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		try {
		Cell c = null;
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet(sheetname);
		// Row and column indexes
		int idx = 0;
		// Generate column headings
		Row row;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(1);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(2);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(3);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(4);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(5);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(6);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(7);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		// set column width
		sheet1.setColumnWidth(0, 7 * 256);
		sheet1.setColumnWidth(1, 30 * 256);
		sheet1.setColumnWidth(2, 16 * 256);
		sheet1.setColumnWidth(3, 16 * 256);
		sheet1.setColumnWidth(4, 20 * 256);
		sheet1.setColumnWidth(5, 30 * 256);
		sheet1.setColumnWidth(6, 16 * 256);
		sheet1.setColumnWidth(7, 16 * 256);
		// Generate rows header of grid
		idx++;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue("STT");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(1);
		c.setCellValue("Nội dung đơn thư");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(2);
		c.setCellValue("Ngày nhận");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(3);
		c.setCellValue("Người đứng đơn");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(4);
		c.setCellValue("Lĩnh vực đơn");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
		c = row.createCell(5);
		c.setCellValue("Đơn vị");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(6);
		c.setCellValue("Thời hạn báo cáo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(7);
		c.setCellValue("Tình trạng xử lý");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		
		idx++;
		CellStyle cellStyleLeft = setBorderAndFont(wb, 1, false, 11, "","LEFT");
		CellStyle cellStyleCenter = setBorderAndFont(wb, 1, false, 11, "","CENTER");
		for (Object[] ob: list) {
			row = sheet1.createRow(idx);
			c = row.createCell(0);			
			c.setCellValue(ob[0] + "");
			c.setCellStyle(cellStyleCenter);				
			c = row.createCell(1);
			c.setCellValue(ob[1] + "");
			c.setCellStyle(cellStyleLeft);				
			c = row.createCell(2);
			c.setCellValue(ob[2] + "");
			c.setCellStyle(cellStyleCenter);		
			c = row.createCell(3);
			c.setCellValue(ob[3] + "");
			c.setCellStyle(cellStyleCenter);	
			c = row.createCell(4);
			c.setCellValue(ob[4] + "");
			c.setCellStyle(cellStyleCenter);
			c = row.createCell(5);
			c.setCellValue(ob[5] + "");
			c.setCellStyle(cellStyleLeft);
			c = row.createCell(6);
			c.setCellValue(ob[6] + "");
			c.setCellStyle(cellStyleCenter);
			c = row.createCell(7);
			c.setCellValue(ob[7] + "");
			c.setCellStyle(cellStyleCenter);
			idx++;
		}
		
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		wb.write(fileOut);
		Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	public static void exportYKienPhanAnh(String title, String fileName, 
			String sheetname, List<Object[]> list) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		try {
		Cell c = null;
		// New Sheet
		Sheet sheet1 = null;
		sheet1 = wb.createSheet(sheetname);
		// Row and column indexes
		int idx = 0;
		// Generate column headings
		Row row;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue(title);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(1);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(2);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(3);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		c = row.createCell(4);
		c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		// set column width
		sheet1.setColumnWidth(0, 7 * 256);
		sheet1.setColumnWidth(1, 30 * 256);
		sheet1.setColumnWidth(2, 30 * 256);
		sheet1.setColumnWidth(3, 16 * 256);
		sheet1.setColumnWidth(4, 16 * 256);
		// Generate rows header of grid
		idx++;
		row = sheet1.createRow(idx);
		c = row.createCell(0);
		c.setCellValue("STT");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
		c = row.createCell(1);
		c.setCellValue("Nội dung kiến nghị");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(2);
		c.setCellValue("Đơn vị thực hiện");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(3);
		c.setCellValue("Thời hạn báo cáo");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		c = row.createCell(4);
		c.setCellValue("Tình trạng xử lý");
		c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
		
		idx++;
		CellStyle cellStyleLeft = setBorderAndFont(wb, 1, false, 11, "","LEFT");
		CellStyle cellStyleCenter = setBorderAndFont(wb, 1, false, 11, "","CENTER");
		for (Object[] ob: list) {
			row = sheet1.createRow(idx);
			c = row.createCell(0);			
			c.setCellValue(ob[0] + "");
			c.setCellStyle(cellStyleCenter);				
			c = row.createCell(1);
			c.setCellValue(ob[1] + "");
			c.setCellStyle(cellStyleLeft);				
			c = row.createCell(2);
			c.setCellValue(ob[2] + "");
			c.setCellStyle(cellStyleLeft);		
			c = row.createCell(3);
			c.setCellValue(ob[3] + "");
			c.setCellStyle(cellStyleCenter);	
			c = row.createCell(4);
			c.setCellValue(ob[4] + "");
			c.setCellStyle(cellStyleCenter);
			idx++;
		}
		
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		wb.write(fileOut);
		Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	
	public static void exportThongKe(String title, String fileName, String sheetName,
			List<NhanVien> listResult) throws IOException {
		// New Workbook
		Workbook wb = new XSSFWorkbook();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Cell c = null;
			// New Sheet
			Sheet sheet1 = null;
			sheet1 = wb.createSheet(sheetName);
			sheet1.getPrintSetup().setLandscape(true);
			sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
			// Row and column indexes
			int idx = 0;
			// Generate column headings
			Row row;
			int countTitle = 0;
			CellStyle cellStyleDataRight = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_RIGHT);
			CellStyle cellStyleDataLeft = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_LEFT);
			CellStyle cellStyleDataCenter = setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_NORMAL, CellStyle.ALIGN_CENTER);
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue(title);
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.addMergedRegion(new CellRangeAddress(countTitle, countTitle + 1, 0, 5));
			for (idx = 1; idx <= 1; idx++) {
				row = sheet1.createRow(idx);
				c = row.createCell(1);
			}
			idx++;
			row = sheet1.createRow(idx);

			// set column width
			sheet1.setColumnWidth(0, 16 * 256);
			// Generate rows header of grid
			row = sheet1.createRow(idx);
			idx++;

			c = row.createCell(0);
			c.setCellValue("STT");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(0, 6 * 256);
			c = row.createCell(1);
			c.setCellValue("Họ và Tên");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(1, 20 * 256);
			c = row.createCell(2);
			c.setCellValue("Email");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(2, 30 * 256);
			c = row.createCell(3);
			c.setCellValue("Nhóm máu");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(3, 15 * 256);
			c = row.createCell(4);
			c.setCellValue("Ngày sinh");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(4, 20 * 256);
			c = row.createCell(5);
			c.setCellValue("Số điện thoại");
			c.setCellStyle(setBorderAndFont(wb, 12, false, 11, Font.BOLDWEIGHT_BOLD, CellStyle.ALIGN_CENTER));
			sheet1.setColumnWidth(5, 20 * 256);

			int i = 1;
			for (NhanVien map : listResult) {
				row = sheet1.createRow(idx);
				c = row.createCell(0);
				c.setCellValue(i);
				c.setCellStyle(cellStyleDataCenter);

				c = row.createCell(1);
				c.setCellValue(map.getHoVaTen());
				c.setCellStyle(cellStyleDataLeft);

				c = row.createCell(2);
				c.setCellValue(map.getEmail());
				c.setCellStyle(cellStyleDataLeft);

				c = row.createCell(4);
				if(map.getNgaySinh() != null)
					c.setCellValue(sdf.format(map.getNgaySinh()));
				else c.setCellValue("");
				c.setCellStyle(cellStyleDataCenter);

				c = row.createCell(5);
				c.setCellValue(map.getSoDienThoai());
				c.setCellStyle(cellStyleDataLeft);
				i++;
				idx++;
			}

			idx++;
			//createNoteRow(wb, sheet1, idx);
			idx++;

			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()), "application/octet-stream",
					fileName + ".xlsx");
		} finally {
			//wb.close();
		}
	}
	
	private static CellStyle setBorderAndFont(Workbook wb, int i, boolean b, int j, Short weight, Short align) {
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		style.setAlignment(align);
		font.setBoldweight(weight);
		style.setFont(font);
		return style;
	}
	
	public static void exportDSNopTien(String title, String fileName, String sheetName1,
			LyDoThuChi lydothuchi, String sheetName2, List<ThuChi> listResult) throws IOException {
		Workbook wb = new XSSFWorkbook();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat numberFormatter = new DecimalFormat("#,###");
		try {
			Cell c = null;
			CellStyle cellStyleLeft = setBorderAndFont(wb, 1, false, 11, "","LEFT");
			CellStyle cellStyleCenter = setBorderAndFont(wb, 1, false, 11, "","CENTER");
			// New Sheet 1
			Sheet sheet1 = null;
			sheet1 = wb.createSheet(sheetName1);
			// Row and column indexes
			int idx = 0;
			// Generate column headings
			Row row;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Lý do thu chi");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(2);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			// set column width
			sheet1.setColumnWidth(0, 18 * 256);
			sheet1.setColumnWidth(1, 45 * 256);
			sheet1.setColumnWidth(2, 18 * 256);
			// Generate rows header of grid
			idx++;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Ngày tạo");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row.createCell(1);
			c.setCellValue("Nội dung lý do");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","LEFT"));
			c = row.createCell(2);
			c.setCellValue("Mức thu (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
			
			idx++;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue(sdf.format(lydothuchi.getNgayTao()));
			c.setCellStyle(cellStyleCenter);	
			c = row.createCell(1);
			c.setCellValue(lydothuchi.getContent());
			c.setCellStyle(cellStyleLeft);
			c = row.createCell(2);
			c.setCellValue(numberFormatter.format(lydothuchi.getMucThu()));
			c.setCellStyle(cellStyleCenter);
			idx++;
			
			// New Sheet 2
			Sheet sheet2 = null;
			sheet2 = wb.createSheet(sheetName2);
			// Row and column indexes
			idx = 0;
			// Generate column headings
			Row row2;
			row2 = sheet2.createRow(idx);
			c = row2.createCell(0);
			c.setCellValue("Danh sách thành viên nộp tiền");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row2.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row2.createCell(2);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row2.createCell(3);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			// set column width
			sheet2.setColumnWidth(0, 7 * 256);
			sheet2.setColumnWidth(1, 30 * 256);
			sheet2.setColumnWidth(2, 30 * 256);
			sheet2.setColumnWidth(3, 16 * 256);
			
			idx++;
			
			row2 = sheet2.createRow(idx);
			c = row2.createCell(0);
			c.setCellValue("STT");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row2.createCell(1);
			c.setCellValue("Họ và tên người nộp");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row2.createCell(2);
			c.setCellValue("Tình trạng nộp");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row2.createCell(3);
			c.setCellValue("Số tiền (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			
			idx++;
			int i=1;
			for (ThuChi ob: listResult) {
				row2 = sheet2.createRow(idx);
				c = row2.createCell(0);			
				c.setCellValue(i);
				c.setCellStyle(cellStyleCenter);				
				c = row2.createCell(1);
				c.setCellValue((ob.getNguoiNop() == null)? "Người khác" : ob.getNguoiNop().getHoVaTen());
				c.setCellStyle(cellStyleLeft);				
				c = row2.createCell(2);
				c.setCellValue((ob.isDaNop()) ? "Đã nộp" : "Chưa nộp");
				c.setCellStyle(cellStyleLeft);		
				c = row2.createCell(3);
				c.setCellValue(numberFormatter.format(ob.getSoTienNop()));
				c.setCellStyle(cellStyleCenter);	
				
				i++;
				idx++;
			}
			
			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	
	public static void exportChiTietChiTieu(String title, String fileName, String sheetName1,
			LyDoThuChi lydothuchi, String sheetName2, List<ChiTietThuChi> listResult) throws IOException {
		Workbook wb = new XSSFWorkbook();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat numberFormatter = new DecimalFormat("#,###");
		try {
			Cell c = null;
			CellStyle cellStyleLeft = setBorderAndFont(wb, 1, false, 11, "","LEFT");
			CellStyle cellStyleCenter = setBorderAndFont(wb, 1, false, 11, "","CENTER");
			// New Sheet 1
			Sheet sheet1 = null;
			sheet1 = wb.createSheet(sheetName1);
			// Row and column indexes
			int idx = 0;
			// Generate column headings
			Row row;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Lý do chi tiêu");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
			// set column width
			sheet1.setColumnWidth(0, 18 * 256);
			sheet1.setColumnWidth(1, 45 * 256);
			// Generate rows header of grid
			idx++;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Ngày tạo");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row.createCell(1);
			c.setCellValue("Nội dung lý do");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","LEFT"));
			
			idx++;
			row = sheet1.createRow(idx);
			c = row.createCell(0);
			c.setCellValue(sdf.format(lydothuchi.getNgayTao()));
			c.setCellStyle(cellStyleCenter);	
			c = row.createCell(1);
			c.setCellValue(lydothuchi.getContent());
			c.setCellStyle(cellStyleLeft);
			
			// New Sheet 2
			Sheet sheet2 = null;
			sheet2 = wb.createSheet(sheetName2);
			// Row and column indexes
			idx = 0;
			// Generate column headings
			Row row2;
			row2 = sheet2.createRow(idx);
			c = row2.createCell(0);
			c.setCellValue(title);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row2.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row2.createCell(2);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			// set column width
			sheet2.setColumnWidth(0, 7 * 256);
			sheet2.setColumnWidth(1, 63 * 256);
			sheet2.setColumnWidth(2, 36 * 256);
			
			idx++;
			
			row2 = sheet2.createRow(idx);
			c = row2.createCell(0);
			c.setCellValue("STT");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row2.createCell(1);
			c.setCellValue("Nội dung chi tiêu");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row2.createCell(2);
			c.setCellValue("Số tiền (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			
			idx++;
			int i=1;
			int tongChi = 0;
			for (ChiTietThuChi ob: listResult) {
				row2 = sheet2.createRow(idx);
				c = row2.createCell(0);			
				c.setCellValue(i);
				c.setCellStyle(cellStyleCenter);				
				c = row2.createCell(1);
				c.setCellValue(ob.getContent());
				c.setCellStyle(cellStyleLeft);				
				c = row2.createCell(2);
				c.setCellValue(numberFormatter.format(ob.getSoTien()));
				c.setCellStyle(cellStyleLeft);	
				
				tongChi += ob.getSoTien();
				i++;
				idx++;
			}
			
			idx++;
			row2 = sheet2.createRow(idx);
			c = row2.createCell(0);
			c.setCellValue("Tổng tiền đã chi: ");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row2.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
			sheet2.addMergedRegion(new CellRangeAddress(idx, idx, 0, 1));
			c = row2.createCell(2);
			c.setCellValue(numberFormatter.format(tongChi) + " vnd");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
			
			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
	
	public static void exportThongKeQuy(String title, String fileName, String sheetName,
			List<Object[]> listResult, int tienQuy) throws IOException {
		
		Workbook wb = new XSSFWorkbook();
		NumberFormat numberFormatter = new DecimalFormat("#,###");
		try {
			Cell c = null;
			CellStyle cellStyleLeft = setBorderAndFont(wb, 1, false, 11, "","LEFT");
			CellStyle cellStyleCenter = setBorderAndFont(wb, 1, false, 11, "","CENTER");
			// New Sheet
			Sheet sheet = null;
			sheet = wb.createSheet(sheetName);
			// Row and column indexes
			int idx = 0;
			// Generate column headings
			Row row;
			row = sheet.createRow(idx);
			c = row.createCell(0);
			c.setCellValue(title);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(2);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(3);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			c = row.createCell(4);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 14, "BLUE", "CENTER"));
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			// set column width
			sheet.setColumnWidth(0, 7 * 256);
			sheet.setColumnWidth(1, 27 * 256);
			sheet.setColumnWidth(2, 36 * 256);
			sheet.setColumnWidth(3, 36 * 256);
			sheet.setColumnWidth(4, 36 * 256);
			
			idx++;
			row = sheet.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("STT");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));	
			c = row.createCell(1);
			c.setCellValue("Tháng thống kê");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","LEFT"));		
			c = row.createCell(2);
			c.setCellValue("Tổng tiền thu (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row.createCell(3);
			c.setCellValue("Tổng tiền chi (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row.createCell(4);
			c.setCellValue("Tiền quỹ còn (vnd)");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
			
			idx++;
			int tongThu = 0, tongChi = 0, tongConLai = 0;
			int i=1;
			for (Object[] ob: listResult) {
				row = sheet.createRow(idx);
				c = row.createCell(0);			
				c.setCellValue(i);
				c.setCellStyle(cellStyleCenter);				
				c = row.createCell(1);
				c.setCellValue(ob[0] + "");
				c.setCellStyle(cellStyleLeft);				
				c = row.createCell(2);
				c.setCellValue(numberFormatter.format(ob[1]));
				c.setCellStyle(cellStyleCenter);
				tongThu += (int) ob[1];
				c = row.createCell(3);
				c.setCellValue(numberFormatter.format(ob[2]));
				c.setCellStyle(cellStyleCenter);
				tongChi += (int) ob[2];
				c = row.createCell(4);
				c.setCellValue(numberFormatter.format(ob[3]));
				c.setCellStyle(cellStyleCenter);
				tongConLai += (int) ob[3];
				
				i++;
				idx++;
			}
			
			row = sheet.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Tổng tiền (vnd): ");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","LEFT"));	
			c = row.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","LEFT"));
			sheet.addMergedRegion(new CellRangeAddress(idx, idx, 0, 1));
			
			c = row.createCell(2);
			c.setCellValue(numberFormatter.format(tongThu));
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row.createCell(3);
			c.setCellValue(numberFormatter.format(tongChi));
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));		
			c = row.createCell(4);
			c.setCellValue(numberFormatter.format(tongConLai));
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "","CENTER"));
			
			idx++;
			idx++;
			row = sheet.createRow(idx);
			c = row.createCell(0);
			c.setCellValue("Quỹ còn: ");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "BLUE","LEFT"));	
			c = row.createCell(1);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "BLUE","LEFT"));
			c = row.createCell(2);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "BLUE","LEFT"));
			c = row.createCell(3);
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "BLUE","LEFT"));
			sheet.addMergedRegion(new CellRangeAddress(idx, idx, 0, 3));
			c = row.createCell(4);
			c.setCellValue(numberFormatter.format(tienQuy) + " vnd");
			c.setCellStyle(setBorderAndFont(wb, 1, true, 11, "RED","CENTER"));
			
			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			wb.write(fileOut);
			Filedownload.save(new ByteArrayInputStream(fileOut.toByteArray()),"application/octet-stream", fileName + ".xlsx");
		} finally {
			wb.close();
		}
	}
}