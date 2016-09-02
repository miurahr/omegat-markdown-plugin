package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Test class for MarkdownSerializer.
 * Created by miurahr on 16/08/26.
 */
public class MarkdownSerializerTest {

    private class MockFilter extends OmegatMarkdownFilter {
        private List<String> entries = new ArrayList<>();

        /**
         * Mock for putEntry()
         * <p>
         * Store to local variable instead of writing file.
         * It don't call translation.
         *
         * @param text entry text
         * @param trans entry to be translated.
         */
        @Override
        void writeTranslate(final String text, final boolean trans) {
            if (trans) {
                entries.add(text);
            }
            appendOutbuf(handler.convMark(text));
        }

        /** for test */
        List<String> getEntries() {
            return entries;
        }
    }

    @Test
    public void testNode_entries() throws Exception {
        String testInput = "# HEADING level 1\n" +
                "\n" +
                "Heading with under lines (level2)\n" +
                "----------\n" +
                "\n" +
                "Normal clause part 1\n" +
                "[External link part 1](https://example.com/link/to/external/url)\n" +
                "continuous clause sentense.\n" +
                "\n" +
                "### Heading level3 **Bold part 1**\n" +
                "\n" +
                "    quote part1\n" +
                "\n" +
                "Normal clause part 2  __Italic__\n" +
                "\n" +
                "    quote part2 (code)\n" +
                "    #! /bin/sh\n" +
                "    #\n" +
                "    echo hello world.\n" +
                "\n" +
                "#### Heading level 4\n" +
                "\n" +
                "Normal clause part 3. ~~strikethroughs~~\n" +
                "Test for Styling text **bold and __italic__**\n" +
                "\n" +
                "Heading with under lines (level2)\n" +
                "----------------------------\n" +
                "\n" +
                "In the word of abraham\n" +
                "\n" +
                "> quote.\n" +
                "\n" +
                "1.  ordered list (1)\n" +
                "2.  ordered list (2)\n" +
                "   multiline\n" +
                "\n" +
                "<!-- HTML style comment -->\n" +
                "\n" +
                "\n" +
                "-  unordered list (1)\n" +
                "\n" +
                "-  unordered list (2)\n" +
                "\n" +
                "-  unordered list (3)\n" +
                "   continuous line.\n";
        List<String> expected = new ArrayList<>();
        expected.add("HEADING level 1");
        expected.add("Heading with under lines (level2)");
        expected.add("Normal clause part 1 " +
                "<a>External link part 1</a><h>https://example.com/link/to/external/url</h> " +
                "continuous clause sentense.");
        expected.add("Heading level3 <b>Bold part 1</b>");
        expected.add("quote part1");
        expected.add("Normal clause part 2  <i>Italic</i>");
        expected.add("quote part2 (code)\n" +
                "#! /bin/sh\n" +
                "#\n" +
                "echo hello world.\n");
        expected.add("Heading level 4");
        expected.add("Normal clause part 3. <s>strikethrough</s>");
        expected.add("Test for Styling text <b>bold and <i>talic</i></b>");
        expected.add("Heading with under lines (level2)");
        expected.add("In the word of abraham");
        expected.add("quote.");
        expected.add("ordered list (1)");
        expected.add("ordered list (2)\n" + "multiline");
        expected.add("<!-- HTML style comment -->");
        expected.add("unordered list (1)");
        expected.add("unordered list (2)");
        expected.add("unordered list (3)\n" + "continuous line.");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
    }

    @Test
    public void testVisit_heading1() throws Exception {
        String testInput = "# HEADING1\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_heading2() throws Exception {
        String testInput = "## HEADING2\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_heading6() throws Exception {
        String testInput = "###### HEADING6\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_em() throws Exception {
        String testInput = "**Emphasis**\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_itaric() throws Exception {
        String testInput = "__itaric__\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_strike() throws Exception {
        String testInput = "~~strike~~\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_quote() throws Exception {
        String testInput = "In the words of Abraham Lincoln:\n" +
                "\n" +
                "> Pardon my French\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_bulletList() throws Exception {
        String testInput = "- Item1\n\n" +
                "- Item2\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_orderedList() throws Exception {
        String testInput = "1. Ordered Item1\n\n" +
                "2. Ordered Item2\n\n";
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

}
