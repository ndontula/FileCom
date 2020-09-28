package com.cigniti.util;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class XmlToPDF {
	public static void main(String[] args) {
		final String xmlFilePath = "employees.xml";
		// Use method to convert XML string content to XML Document object
		xmlToPDF(xmlFilePath, "Expected");
	}

	public static String xmlToPDF(String xmlFilePath, String expectedOrActual) {
		String fileName = Paths.get(xmlFilePath).getFileName().toString();
		String fileNameWithoutExtension = fileName != null && fileName.lastIndexOf(".") > 0? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();
			// Parse the content to Document object
			Document xmlDocument = builder.parse(new File(xmlFilePath));
			// Write to file or print XML
			return writeXmlDocumentToXmlFile(xmlDocument, expectedOrActual, fileNameWithoutExtension);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String writeXmlDocumentToXmlFile(Document xmlDocument, String expectedOrActual, String fileNameWithoutExtension) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String pdfPath = "";
		String fileName = fileNameWithoutExtension + ".txt";
		try {
			transformer = tf.newTransformer();
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
			String xmlString = writer.getBuffer().toString();
			try {
				FileWriter fw = new FileWriter(fileName);
				fw.write(xmlString);
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			File file = new File(fileName);
			pdfPath = TextToPDF.textToPDF(file.toString(), expectedOrActual);
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfPath;
	}
}