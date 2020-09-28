package com.cigniti.util;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class HtmlToPdfToImage {

	public static void main(String[] args) {
		try
        {
            OutputStream file1 = new FileOutputStream(new File("HTMLtoPDF.pdf"));
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, file1);
            StringBuilder htmlString = new StringBuilder();
            try {
                BufferedReader in = new BufferedReader(new FileReader("src\\main\\resources\\finalDiff.html"));
                String str="";
                while ((str = in.readLine()) != null) {
                    htmlString.append(new String(str));
                }
                in.close();
            } catch (IOException e) {
            }
            testMethod(htmlString.toString());
            
            document.open();
            InputStream is = new ByteArrayInputStream(htmlString.toString().getBytes());
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
            document.close();
            file1.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
public static void testMethod(String html){
	int width = 1024, height = 1024;

	BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment()
	    .getDefaultScreenDevice().getDefaultConfiguration()
	    .createCompatibleImage(width, height);

	Graphics graphics = image.createGraphics();

	JEditorPane jep = new JEditorPane("text/html", html);
	jep.setSize(width, height);
	jep.print(graphics);

	try {
		ImageIO.write(image, "png", new File("ImageTTT.png"));
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	
}
