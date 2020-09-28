package com.cigniti.utility.textcompare;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.diff.CommandVisitor;

/*
 * Custom visitor for file comparison which stores comparison & also generates
 * HTML in the end.
 */
public class FileCommandsVisitor implements CommandVisitor<Character> {
	// Spans with red & green highlights to put highlighted characters in HTML
	private static final String DELETION = "<span style=\"background-color: #FB504B\">${text}</span>";
	private static final String INSERTION = "<span style=\"background-color: #45EA85\">${text}</span>";

	private String left = "";
	private String right = "";

	@Override
	public void visitKeepCommand(Character c) {
		// For new line use <br/> so that in HTML also it shows on next line.
		String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
		// KeepCommand means c present in both left & right. So add this to both without
		// any
		// highlight.
		left = left + toAppend;
		right = right + toAppend;
	}

	@Override
	public void visitInsertCommand(Character c) {
		// For new line use <br/> so that in HTML also it shows on next line.
		String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
		// InsertCommand means character is present in right file but not in left. Show
		// with green highlight on right.
		right = right + INSERTION.replace("${text}", "" + toAppend);
	}

	@Override
	public void visitDeleteCommand(Character c) {
		// For new line use <br/> so that in HTML also it shows on next line.
		String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
		// DeleteCommand means character is present in left file but not in right. Show
		// with red highlight on left.
		left = left + DELETION.replace("${text}", "" + toAppend);
	}

	public void generateHTML() throws IOException {

		// Get template & replace placeholders with left & right variables with actual
		// comparison
		String template = FileUtils.readFileToString(new File("src/main/resources/difftemplate.html"), "utf-8");
		String out1 = template.replace("${left}", left);
		String output = out1.replace("${right}", right);
		// Write file to disk.
		//FileUtils.write(new File("src/main/resources/finalDiff.html"), output, "utf-8");
		FileUtils.writeStringToFile(new File("src/main/resources/finalDiff.html"), output, StandardCharsets.UTF_8.name());
		System.out.println("HTML diff generated.");
	}


}
