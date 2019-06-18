package javaProgramming.finalproject;


import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import java.util.zip.*;
import net.sf.jazzlib.ZipEntry;
import net.sf.jazzlib.ZipInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.nio.file.*;


public class ZipReader {

	private final static String EXTENSION = ".zip";
	private final static int BUFFER_SIZE = 2048;


	public void run(String args, String destination) {
		int stId;
		String sendingPath;
		
		for(stId = 1; stId < 6; stId++) {
		 try {
			 String sid = String.format("%04d", stId);
			 String zipPath = args + File.separator + sid + EXTENSION;
			   String zipfile = zipPath;
			   ZipInputStream in = new ZipInputStream(new FileInputStream(zipfile));

			   ZipEntry entry = null;
			   while ((entry = in.getNextEntry()) != null) {
			    System.out.println("Unzipping at:" + entry.getName());
			    File resultFile = new File(destination);
			    resultFile.getParentFile().mkdirs();
			    OutputStream out = new FileOutputStream(new File(destination, entry.getName()));

			    byte[] buf = new byte[1024];
			    int len;
			    while ((len = in.read(buf)) > 0) {
			     out.write(buf, 0, len);
			    }
			    
			    in.closeEntry();
			    // out닫기
			    out.close();
			    
			    sendingPath = entry.getName();
			    
			    if(sendingPath.contains("요약문")) {
			    	System.out.println(sendingPath);
				    readExcelForSummary(sendingPath, sid);
			    }
			    if(sendingPath.contains("표")) {
			    	System.out.println(sendingPath);
			    	readExcelForCharts(sendingPath, sid);
			    }
			    
			    
			   }
			   in.close();
			   
			   

			  } catch (IOException e) {
			   e.printStackTrace();
			  }	
		}
		
	}
	
	
	public HashMap<String,LinkedList<Object>> readExcelForSummary(String fileName, String studentID) throws IOException {
        
		LinkedList<Object> datas = new LinkedList<Object>();
		int i = 0;
		HashMap<String, LinkedList<Object>> valuesForSum = new HashMap<String, LinkedList<Object>>();
		String relativePath = "." + File.separator + fileName;
		try {
			FileInputStream file = new FileInputStream(new File(fileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			   while (rowIterator.hasNext())
	            {
	                Row row = rowIterator.next();
	                //For each row, iterate through all the columns
	                Iterator<Cell> cellIterator = row.cellIterator();
	                 
	                while (cellIterator.hasNext())
	                {
	                    Cell cell = cellIterator.next();
	                  datas.add(i, cell.getStringCellValue());        
	                  i++;
	                    
	                }
	            }
	            file.close();
			
		} catch (IOException e){
			System.out.println("IOException in reading excel For summary");
		}
		
		int linkedListSize = datas.getSize();

		
		valuesForSum.put(studentID, datas);
		writeSummary(datas, studentID);
		
		//System.out.println(studentID);
		return valuesForSum;
	}


public HashMap<String,LinkedList<Object>> readExcelForCharts(String fileName, String studentID) throws IOException {
    
	LinkedList<Object> datasForCharts = new LinkedList<Object>();
	int i = 0;
	HashMap<String, LinkedList<Object>> valuesForCharts = new HashMap<String, LinkedList<Object>>();
	String relativePath = "." + File.separator + fileName;
	try {
		FileInputStream file = new FileInputStream(new File(fileName));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		   while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                 
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                  datasForCharts.add(i, cell.getStringCellValue());        
                  i++;
                    
                }
            }
            file.close();
		
	} catch (IOException e){
		System.out.println("IOException in read Excel For Charts");
	}
	
		valuesForCharts.put(studentID, datasForCharts);
		
		writeCharts(datasForCharts, studentID);
		
		System.out.println(studentID);
	return valuesForCharts;
}



public void writeSummary(LinkedList<Object> values, String studentID) throws IOException{
	String filePath = Worker.output;
	String fileName = "result1.csv";
	String targetFileName = filePath + File.separator + fileName;
	
	System.out.println(targetFileName);
	

		FileWriter fw = new FileWriter(targetFileName, true);
	
		int size = values.getSize();
		String header = null;
		int l = 0;
		System.out.println(size);
		
		for(l = 0; l < 7; l++) {
			if(l == 0) fw.write(",");
			header += values.getValue(l) + ",";
		}
		
		
		
		if(studentID.contentEquals("0001")) {
		fw.append(header);
		fw.append("\n");
		}
		
		
		int co = 0;
		
		System.out.println("2");
		
		while(l <= size) {
			if(co % 8 == 0) {
				fw.append(studentID);
				fw.append(",");
			}
			fw.append(values.getValue(l));
			fw.append(",");
			
			if(l % 7 == 0) {
				fw.append("\n");
				System.out.println(values.getValue(l));
			}
			
			
			l++;
			co++;
		}
		

		fw.flush();
		fw.close();

}


public void writeCharts(LinkedList<Object> values, String studentID) throws IOException{
	String filePath = Worker.output;
	String fileName = "result2.csv";
	String targetFileName = filePath + File.separator + fileName;
	
	System.out.println(targetFileName);
	
		FileWriter fw = new FileWriter(targetFileName, true);
		int size = values.getSize();
		String header = null;
		int l = 0;
		//System.out.println(size);
		
		for(l = 0; l < 5; l++) {
			if(l == 0) fw.write(",");
			header += values.getValue(l) + ",";
		}
		
		
		if(studentID.contentEquals("0001")) {
		fw.append(header);
		fw.append("\n");
		}
		
		
		int co = 0;
		
		System.out.println("2");
		
		while(l <= size) {
			if(co % 6 == 0) {
				fw.append(studentID);
				fw.append(",");
			}
			fw.append(values.getValue(l));
			fw.append(",");
			
			if(l % 5 == 0) {
				fw.append("\n");
				//System.out.println(values.getValue(l));
			}
			
			
			l++;
			co++;
		}

		fw.flush();
		fw.close();

}
		
}