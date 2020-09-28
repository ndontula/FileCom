package com.cigniti.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DoxcToPDF {

	public static void main(String[] args) {

		String expectedDocx = "C:\\Users\\E001987\\Desktop\\MP_10_Sep_2020_manual.docx";
		try {
			docxToPDF(expectedDocx, "Expected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String docxToPDF(String expectedDocx, String expectedOrActual) {
		String pdfPath = "";
		try {
			pdfPath= "Temp"+File.separator+GetFileName.getPDFFile(expectedDocx, expectedOrActual);
			InputStream doc = new FileInputStream(new File(expectedDocx));
			XWPFDocument document = new XWPFDocument(doc);
			PdfOptions options = PdfOptions.create();
			OutputStream out = new FileOutputStream(new File(pdfPath));
			PdfConverter.getInstance().convert(document, out, options);
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return pdfPath;
	}
}