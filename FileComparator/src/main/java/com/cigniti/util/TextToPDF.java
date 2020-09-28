package com.cigniti.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class TextToPDF {

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\E001987\\IBO\\IBO_412020\\Automation\\File1.txt");
		System.out.println( textToPDF(file.toString(), "Expected"));
	}
	
	public static String textToPDF(String filePath, String expectedOrActual) throws Exception {
		
		String pdfPath = System.getProperty("user.dir") + File.separator +"Temp"+File.separator+GetFileName.getPDFFile(filePath, expectedOrActual);
		Document pdfDoc = new Document(PageSize.A4);
    	PdfWriter.getInstance(pdfDoc, new FileOutputStream(pdfPath))
    	  .setPdfVersion(PdfWriter.PDF_VERSION_1_7);
    	pdfDoc.open();
    	Font myfont = new Font();
    	myfont.setStyle(Font.NORMAL);
    	myfont.setSize(11);
    	pdfDoc.add(new Paragraph("\n"));
    	
    	BufferedReader br = new BufferedReader(new FileReader(filePath));
    	String strLine;
    	while ((strLine = br.readLine()) != null) {
    	    Paragraph para = new Paragraph(strLine + "\n", myfont);
    	    para.setAlignment(Element.ALIGN_JUSTIFIED);
    	    pdfDoc.add(para);
    	}	
    	pdfDoc.close();
    	br.close();
    	return pdfPath;
	}
}
