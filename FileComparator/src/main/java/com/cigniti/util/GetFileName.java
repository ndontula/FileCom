package com.cigniti.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.relevantcodes.extentreports.LogStatus;

public class GetFileName {
	public static String getPDFFile(final String filePath, String expectedOrActual)
	{
        Path path = Paths.get(filePath); 
        String fileName = path.getFileName().toString();
        String fileNameWithoutExtension = fileName != null && fileName.lastIndexOf(".") > 0 ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
	    return expectedOrActual+"_"+fileNameWithoutExtension+".pdf";
	}
	
	public static void deleteAllFilesFromFolder() throws Throwable {
		String path = System.getProperty("user.dir") + "\\Temp";
		File file = new File(path);
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile() && f.exists()) {
				f.delete();
			}
		}
	}
}
