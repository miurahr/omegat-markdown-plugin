package tokyo.northside.omegat;

import static org.testng.Assert.*;

import org.omegat.filters2.FilterContext;
import org.testng.annotations.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test for MarkdownFilter plugin for omegat.
 * Created by miurahr on 16/08/23.
 */
class MarkdownFilterTest extends TestFilterBase {
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
    void testIsFileSupported_true() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        File target = new File(this.getClass().getResource("/test.md").getFile());
        FilterContext fc = new FilterContext();
        assertTrue(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testIsFileSupported_false() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        File target = new File(this.getClass().getResource("/nomarkdown.txt").getFile());
        FilterContext fc = new FilterContext();
        assertFalse(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testProcessFile() throws Exception {
        MarkdownFilter mdf = new MarkdownFilter();
        Map<String, String> options = new HashMap<>(64);
        List<String> entries = parse(mdf, "/complex.md", options);
        assertNotNull(entries);
        assertNotEquals(entries.size(), 0);
        for (String en: entries) {
            System.out.print("test: ");
            System.out.println(en);
        }
    }
}
