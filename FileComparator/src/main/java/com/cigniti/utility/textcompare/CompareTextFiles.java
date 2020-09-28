package com.cigniti.utility.textcompare;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.text.diff.StringsComparator;

public class CompareTextFiles {

	public static void testText(File expectedFile, File actualFile ) throws IOException {
		// Read both files with line iterator.
		LineIterator file1 = FileUtils.lineIterator(expectedFile, "utf-8");
		LineIterator file2 = FileUtils.lineIterator(actualFile, "utf-8");

		// Initialize visitor.
		FileCommandsVisitor fileCommandsVisitor = new FileCommandsVisitor();

		// Read file line by line so that comparison can be done line by line.
		while (file1.hasNext() || file2.hasNext()) {
			/*
			 * In case both files have different number of lines, fill in with empty
			 * strings. Also append newline char at end so next line comparison moves to
			 * next line.
			 */
			String left = (file1.hasNext() ? file1.nextLine() : "") + "\n";
			String right = (file2.hasNext() ? file2.nextLine() : "") + "\n";

			// Prepare diff comparator with lines from both files.
			StringsComparator comparator = new StringsComparator(left, right);

			if (comparator.getScript().getLCSLength() > (Integer.max(left.length(), right.length()) * 0.4)) {
				/*
				 * If both lines have atleast 40% commonality then only compare with each other
				 * so that they are aligned with each other in final diff HTML.
				 */
				comparator.getScript().visit(fileCommandsVisitor);
			} else {
				/*
				 * If both lines do not have 40% commanlity then compare each with empty line so
				 * that they are not aligned to each other in final diff instead they show up on
				 * separate lines.
				 */
				StringsComparator leftComparator = new StringsComparator(left, "\n");
				leftComparator.getScript().visit(fileCommandsVisitor);
				StringsComparator rightComparator = new StringsComparator("\n", right);
				rightComparator.getScript().visit(fileCommandsVisitor);
			}
		}

		fileCommandsVisitor.generateHTML();
	}

}