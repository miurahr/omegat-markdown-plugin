package tokyo.northside.omegat;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Test for MarkdownFilter plugin for omegat.
 * Created by miurahr on 16/08/23.
 */
class MarkdownFilterTest  {
    @Test
    void testGetFileFormatName() throws Exception {
        String expected = "Markdown Filter";
        MarkdownFilter mdf = new MarkdownFilter();
        assertEquals(mdf.getFileFormatName(), expected);
    }

    @Test
    void testGetHint() throws Exception {
        String expected = "Note: Filter to translate Markdown files.";
        MarkdownFilter mdf = new MarkdownFilter();
        assertEquals(mdf.getHint(), expected);
    }

    @Test
    void testIsSourceEncodingVariable() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        assertFalse(mdf.isSourceEncodingVariable());
    }

    @Test
    void testIsTargetEncodingVariable() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        assertFalse(mdf.isTargetEncodingVariable());
    }

    @Test
    void testRequirePrevNextFields() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        assertFalse(mdf.requirePrevNextFields());
    }

    @Test
    void testIsFileSupported_true() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        try (BufferedReader reader = new BufferedReader(new
                FileReader(this.getClass().getResource("/test.md").getFile()))) {
            assertTrue(mdf.isFileSupported(reader));
        }
    }

    @Test
    void testIsFileSupported_false() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        try (BufferedReader reader = new BufferedReader(new
                FileReader(this.getClass().getResource("/nomarkdown.txt").getFile()))) {
            assertFalse(mdf.isFileSupported(reader));
        }
    }

    @Test
    void testProcessFile() throws Exception {

    }

    @Test
    void testGetHeadingLevel_1() {
        String source = "# HEADING";
        int expect = 1;
        MarkdownFilter mdf = new MarkdownFilter();
        assertEquals(mdf.getHeadingLevel(source), expect);
    }

    @Test
    void testGetHeadingLevel_2() {
        String source = "## HEADING";
        int expect = 2;
        MarkdownFilter mdf = new MarkdownFilter();
        assertEquals(mdf.getHeadingLevel(source), expect);
    }

}
