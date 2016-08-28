package tokyo.northside.omegat;

import static org.testng.Assert.*;

import org.omegat.filters2.FilterContext;
import org.testng.annotations.*;

import java.io.File;
import java.util.List;

/**
 * Test for OmegatMarkdownFilter plugin for omegat.
 * Created by miurahr on 16/08/23.
 */
class OmegatMarkdownFilterTest extends TestFilterBase {
    @Test
    void testGetFileFormatName() throws Exception {
        String expected = "Markdown Filter";
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertEquals(mdf.getFileFormatName(), expected);
    }

    @Test
    void testGetHint() throws Exception {
        String expected = "Note: Filter to translate Markdown files.";
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertEquals(mdf.getHint(), expected);
    }

    @Test
    void testIsSourceEncodingVariable() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertFalse(mdf.isSourceEncodingVariable());
    }

    @Test
    void testIsTargetEncodingVariable() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertFalse(mdf.isTargetEncodingVariable());
    }

    @Test
    void testIsFileSupported_true() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        File target = new File(this.getClass().getResource("/source/case0.md").getFile());
        FilterContext fc = new FilterContext();
        assertTrue(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testIsFileSupported_false() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        File target = new File(this.getClass().getResource("/source/nomarkdown.txt").getFile());
        FilterContext fc = new FilterContext();
        assertFalse(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testProcess_case1() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/case1.md");
    }

    @Test
    void testTranslate_case1() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/case1.md");
    }
}
