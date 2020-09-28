package com.cigniti.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class ImageToPDF {

	public static void main(String[] args) {
		main();

	}
	
    public static void main() {

        try {
            //Scanner sc = new Scanner(System.in);
            //System.out.print("Enter your destination folder where save PDF \n");
            // Destination = D:/Destination/;
           // String destination = sc.nextLine();

           // System.out.print("Enter your PDF File Name \n");
            // Name = test;
            //String name = sc.nextLine();

            //System.out.print("Enter your selected image files name with source folder \n");
            //String sourcePath = sc.nextLine();
            String sourcePath = "C:\\Users\\E001987\\Desktop\\ElectricityBill2.jpg";
            if (sourcePath != null || sourcePath != "") {
            	imageToPDF(sourcePath, "imageTo");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
    public static String imageToPDF(String imageFilePath, String expectedOrActual) throws Exception {
    	String pdfPath = System.getProperty("user.dir") + File.separator +"Temp"+File.separator+GetFileName.getPDFFile(imageFilePath, expectedOrActual);
        Document document = new Document(PageSize.A4, 20.0f, 20.0f, 20.0f, 150.0f);
        
        System.out.println(pdfPath);
       /* File destinationDirectory = new File(pdfPath);
        if (!destinationDirectory.exists()){
            destinationDirectory.mkdir();
        }*/

        File file = new File(pdfPath);

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        PdfWriter.getInstance(document, fileOutputStream);
        document.open();

        String[] splitImagFiles = imageFilePath.split(",");

        for (String singleImage : splitImagFiles) {
        Image image = Image.getInstance(singleImage);
        document.setPageSize(image);
        document.newPage();
        image.setAbsolutePosition(0, 0);
            document.add(image);
        }

        document.close();
        return pdfPath;
    }

}
