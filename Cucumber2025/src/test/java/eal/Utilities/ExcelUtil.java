package eal.Utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {

	private static final Logger logger = LogManager.getLogger(ExcelUtil.class);
	private static final String BASE_PATH = "src/test/resources/Data_Files/";

	private Workbook workBook;
	private Sheet workSheet;
	private String currentFilePath;
	private DataFormatter formatter;

	// Performance Cache: Maps Header Name -> Column Index (e.g., "Password" -> 2)
	private Map<String, Integer> columnMap;

	/**
	 * Empty Constructor for Static Instantiation in CommonMethods
	 */
	public ExcelUtil() {
		this.formatter = new DataFormatter();
		this.columnMap = new HashMap<>();
	}

	// ================================
	// 🔹 SETUP: Open File & Sheet
	// ================================

	/**
	 * Loads an Excel file and Sheet. Call this before reading/writing.
	 * 
	 * @param fileName  - "LoginData.xlsx"
	 * @param sheetName - "SmokeTest"
	 */
	// Inside ExcelUtil.java

	// Inside ExcelUtil.java

	/**
	 * 🔹 Core SETUP Method 🔹
	 * Loads the specified Excel file and Sheet into memory.
	 */
	public void openExcel(String fileName, String sheetName) {
	    // 1. Determine Full Path
	    this.currentFilePath = BASE_PATH + fileName;

	    // 2. Resource Cleanup: Close the previously loaded workbook to free memory.
	    close();

	    // 3. Open File Stream (Try-with-resources guarantees file stream is closed)
	    try (FileInputStream fis = new FileInputStream(currentFilePath)) {
	        
	        // 4. Universal Workbook Creation: This line can throw exceptions
	        this.workBook = WorkbookFactory.create(fis);

	        this.workSheet = workBook.getSheet(sheetName);

	        // 5. Sheet Existence Check
	        if (this.workSheet == null) {
	            // Throw as unchecked exception, test fails immediately and clearly.
	            throw new RuntimeException("❌ Sheet [" + sheetName + "] not found in " + fileName);
	        }

	        // 6. Performance Optimization: Cache all header names to column indices.
	        mapColumns();

	    } catch (Exception e) { // 🌟 MODIFIED: Catch the broader 'Exception' for POI issues
	        // 7. Robust Error Handling: Catch ALL checked exceptions (like IOException, InvalidFormatException) and wrap them in a single, descriptive RuntimeException.
	        logger.error("Failed to read Excel file: " + fileName, e);
	        throw new RuntimeException("❌ Failed to read Excel file: " + fileName, e);
	    }
	}

	/**
	 * Internal helper to cache header names to column indices. Assumes Row 0 is the
	 * Header Row.
	 */
	private void mapColumns() {
		columnMap.clear();
		Row headerRow = workSheet.getRow(0);
		if (headerRow != null) {
			for (Cell cell : headerRow) {
				String header = formatter.formatCellValue(cell).trim();
				columnMap.put(header, cell.getColumnIndex());
			}
		}
	}

	// ================================
	// 🔹 READ: Get Data
	// ================================

	/** Basic: Get by Row and Column Index */
	public String getCellData(int rowNum, int colNum) {
		try {
			Row row = workSheet.getRow(rowNum);
			if (row == null)
				return "";
			Cell cell = row.getCell(colNum);
			return formatter.formatCellValue(cell).trim();
		} catch (Exception e) {
			return "";
		}
	}

	/** Advanced: Get by Row Index and Column Name */
	public String getCellData(int rowNum, String columnName) {
		if (!columnMap.containsKey(columnName)) {
			logger.error("Column [" + columnName + "] not found in map!");
			return "";
		}
		return getCellData(rowNum, columnMap.get(columnName));
	}

	/**
	 * * 🌟 SUPER METHOD: Get Value by Row Key (e.g., TestCaseID) and Column Name
	 * Iterates rows to find the 'key', then grabs data from 'colName'.
	 */
	public String getCellData(String rowKey, String columnName) {
		int rowIndex = getRowIndexByKey(rowKey);
		if (rowIndex == -1)
			return "";
		return getCellData(rowIndex, columnName);
	}

	/** Helper: Find row index where the first column matches the key */
	public int getRowIndexByKey(String key) {
		for (int i = 1; i <= getRowCount(); i++) {
			// Assuming Key is always in Column 0
			String cellValue = getCellData(i, 0);
			if (cellValue.equalsIgnoreCase(key)) {
				return i;
			}
		}
		logger.warn("Row Key [" + key + "] not found.");
		return -1;
	}

	public int getRowCount() {
		return (workSheet == null) ? 0 : workSheet.getLastRowNum();
	}

	// ================================
	// 🔹 WRITE: Set Data
	// ================================

	public void setCellData(String value, int rowNum, int colNum) {
		Row row = workSheet.getRow(rowNum);
		if (row == null)
			row = workSheet.createRow(rowNum);
		Cell cell = row.getCell(colNum);
		if (cell == null)
			cell = row.createCell(colNum);
		cell.setCellValue(value);
	}

	public void setCellData(String value, int rowNum, String columnName) {
		if (columnMap.containsKey(columnName)) {
			setCellData(value, rowNum, columnMap.get(columnName));
		} else {
			logger.error("Cannot write. Column [" + columnName + "] does not exist.");
		}
	}

	/**
	 * * 🌟 SUPER METHOD: Set Value by Row Key (e.g., TestCaseID) and Column Name
	 */
	public void setCellData(String value, String rowKey, String columnName) {
		int rowIndex = getRowIndexByKey(rowKey);
		if (rowIndex != -1) {
			setCellData(value, rowIndex, columnName);
		}
	}

	// ================================
	// 🔹 SAVE & CLEANUP
	// ================================

	public void save() {
		try (FileOutputStream fos = new FileOutputStream(currentFilePath)) {
			workBook.write(fos);
		} catch (IOException e) {
			throw new RuntimeException("Failed to save Excel file", e);
		}
	}

	public void close() {
		try {
			if (workBook != null)
				workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
