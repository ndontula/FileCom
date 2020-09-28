package com.cigniti.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.util.Map;

public class JSONtoPDF {

	public static void main(String[] args) throws Exception {
		
		String filePata = "t.json";
		System.out.println(jsonToPDF(filePata, "expectedOrActual"));
	}

	public static String jsonToPDF(String filePath, String expectedOrActual) throws Exception {
		
		String pdfPath = System.getProperty("user.dir") + File.separator +"Temp"+File.separator+GetFileName.getPDFFile(filePath, expectedOrActual);
		

		File jsonFile = new File(filePath).getAbsoluteFile();

		ObjectMapper mapper = new ObjectMapper();
		// enable pretty printing
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// read map from file
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> map = mapper.readValue(jsonFile, mapType);

		// generate pretty JSON from map
		String json = mapper.writeValueAsString(map);
		// split by system new lines
		String[] strings = json.split(System.lineSeparator());

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);

		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		contentStream.setFont(PDType1Font.COURIER, 12);
		contentStream.beginText();
		contentStream.setLeading(14.5f);
		contentStream.newLineAtOffset(25, 725);
		for (String string : strings) {
			contentStream.showText(string);
			contentStream.newLine();
		}
		contentStream.endText();
		contentStream.close();

		document.save(pdfPath);
		document.close();
		return pdfPath;

	}
}