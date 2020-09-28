package com.cigniti.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import au.com.bytecode.opencsv.CSVReader;

public class CsvToPDF {  
        public static void main(String[] args) throws Exception{
           
        }
        
        public static String csvToPDF(String csvFilePath, String expectedOrActual) throws Exception {
        	String pdfPath = System.getProperty("user.dir") + File.separator +"Temp"+File.separator+GetFileName.getPDFFile(csvFilePath, expectedOrActual);
            
            @SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new FileReader(csvFilePath));
            Document my_pdf_data = new Document();
            PdfWriter.getInstance(my_pdf_data, new FileOutputStream(pdfPath));
            my_pdf_data.open();  
            List<String[]> readAll = reader.readAll();
            PdfPTable my_first_table = new PdfPTable(readAll.get(1).length);
            PdfPCell table_cell;
            for (String[] line : readAll ) {
                    for(String cel: line){
                    	table_cell=new PdfPCell(new Phrase(cel));
                    	my_first_table.addCell(table_cell);
                    }
            }
            my_pdf_data.add(my_first_table);                       
            my_pdf_data.close(); 
        	
			return pdfPath;
        }
}