package com.cigniti.compare;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.jupiter.api.Test;
import com.cigniti.compare.cli.CliArgumentsImpl;
import com.cigniti.compare.cli.CliComparator;

public class CliComparatorTest {

    @Test
    public void comparesTwoEqualFilesAndReturnsZero() {
        CliComparator testCliComparator = new CliComparator(new CliArgumentsImpl(new String[]{"actual.pdf", "actual.pdf"}));

        assertThat(testCliComparator.getResult(), equalTo(0));
    }

    @Test
    public void comparesTwoDifferentFilesAndReturnsOne() {
        CliComparator testCliComparator = new CliComparator(new CliArgumentsImpl(new String[]{"expected.pdf", "actual.pdf"}));

        assertThat(testCliComparator.getResult(), equalTo(1));
    }

}
