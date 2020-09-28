package com.cigniti.compare;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import com.cigniti.compare.CompareResultImpl;
import com.cigniti.compare.CompareResultWithPageOverflow;
import com.cigniti.compare.PdfComparator;

public class TestMain {

    public static void main(String[] args) throws IOException, InterruptedException {

        String file1 = "src/test/resources/expected.pdf";
        String file2 = "src/test/resources/actual.pdf";

        for (int i = 0; i < 1; i++) {
            Instant start = Instant.now();
//            final CompareResult result = new DiskUsingCompareResult();
            final CompareResultImpl result = new CompareResultWithPageOverflow();
            new PdfComparator(file1, file2, result)
//                    .withIgnore("ignore.conf")
                    .compare().writeTo("out");
            Instant end = Instant.now();
            System.out.println("Duration: " + Duration.between(start, end).toMillis() + "ms");
        }
//        printMemory("finished");
//        if (result.isNotEqual()) {
//            System.out.println("Differences found!");
//        }
//        result.writeTo("test_with_ignore");
//
//        start = Instant.now();
//        final CompareResult result2 = new PdfComparator(file1, file2).compare();
//        end = Instant.now();
//        System.out.println("Duration: " + Duration.between(start, end).toMillis() + "ms");
//        if (result2.isNotEqual()) {
//            System.out.println("Differences found!");
//        }
//        result2.writeTo("test");
    }


    private static long oldTotal;
    private static long oldFree;

    public static void printMemory(final String s) {
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        final long consumed = totalMemory - freeMemory;
        System.out.println("==========================================================================");
        System.out.println("Memory " + s);
        System.out.printf("Total Memory: %6dMB  |  %d\n", toMB(totalMemory), toMB(totalMemory - oldTotal));
        System.out.printf("Free Memory:  %6dMB  |  %d\n", toMB(freeMemory), toMB(freeMemory - oldFree));
        System.out.printf("Consumed:     %6dMB  |  %d\n", toMB(consumed), toMB(consumed - (oldTotal - oldFree)));
        System.out.println("==========================================================================");
        oldTotal = totalMemory;
        oldFree = freeMemory;
    }

    private static long toMB(final long memory) {
        return memory / (1024 * 1024);
    }
}
