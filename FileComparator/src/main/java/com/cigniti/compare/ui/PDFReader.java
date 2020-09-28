package com.cigniti.compare.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFReader {
   public static void main(String[] args) throws IOException {
	   imageComprison();
       //System.out.println(readParaFromPDF("C:/Users/E001987/Desktop/Expected.pdf",20, "Feedback", "feedback"));
    //Enter FilePath, Page Number, StartsWith, EndsWith
   }
   public static String readParaFromPDF(String pdfPath, int pageNo, String strStartIndentifier, String strEndIdentifier) {
       String returnString = "";
       try {
           PDDocument document = PDDocument.load(new File(pdfPath));
           document.getClass();        
           if (!document.isEncrypted()) {
               PDFTextStripperByArea stripper = new PDFTextStripperByArea();
               stripper.setSortByPosition(true);
               PDFTextStripper tStripper = new PDFTextStripper();
               //tStripper.setStartPage(pageNo);
               //tStripper.setEndPage(pageNo);
               String pdfFileInText = tStripper.getText(document);
               String strStart = strStartIndentifier;
               String strEnd = strEndIdentifier;
               int startInddex = pdfFileInText.indexOf(strStart);
               int endInddex = pdfFileInText.indexOf(strEnd);
               returnString = pdfFileInText.substring(startInddex, endInddex) + strEnd;
           }
          } catch (Exception e) {
              returnString = "No ParaGraph Found";
       }
            return returnString;
   }
   
   private static void imageComprison() throws IOException{

       BufferedImage img1 = ImageIO.read(new File("C:/Users/E001987/Desktop/Electricity Bill.jpeg"));
       BufferedImage img2 = ImageIO.read(new File("C:/Users/E001987/Desktop/ElectricityBill2.jpg"));
       int w1 = img1.getWidth();
       int w2 = img2.getWidth();
       int h1 = img1.getHeight();
       int h2 = img2.getHeight();
       if ((w1!=w2)||(h1!=h2)) {
          System.out.println("Both images should have same dimwnsions");
       } else {
          long diff = 0;
          for (int j = 0; j < h1; j++) {
             for (int i = 0; i < w1; i++) {
                //Getting the RGB values of a pixel
                int pixel1 = img1.getRGB(i, j);
                Color color1 = new Color(pixel1, true);
                int r1 = color1.getRed();
                int g1 = color1.getGreen();
                int b1 = color1.getBlue();
                int pixel2 = img2.getRGB(i, j);
                Color color2 = new Color(pixel2, true);
                int r2 = color2.getRed();
                int g2 = color2.getGreen();
                int b2= color2.getBlue();
                //sum of differences of RGB values of the two images
                long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                diff = diff+data;
             }
          }
          double avg = diff/(w1*h1*3);
          double percentage = (avg/255)*100;
          System.out.println("Difference: "+percentage);
       }
   }
}