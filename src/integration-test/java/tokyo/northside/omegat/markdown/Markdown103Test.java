package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;


/**
 * Integration test class to read from resources and check with json entries.
 * @author Hiroshi Miura
 */
public class Markdown103Test extends TestFilterBase {

    /**
     * Markdown 103 Test are imported from pegdown test case.
     *
     * Test cases are distributed under Apache 2.0 license.
     *
     * @throws Exception
     */
    @Test
    public void markdown103Test() throws Exception {
        testTranslate("/MarkdownTest103/Hard-wrapped_paragraphs_with_list-like_lines");

        // FIXME: fails following test cases.
        test("/MarkdownTest103/Amps_and_angle_encoding");
        testTranslate("/MarkdownTest103/Auto_links");
        testTranslate("/MarkdownTest103/Backslash_escapes");
        testTranslate("/MarkdownTest103/Blockquotes_with_code_blocks");
        testTranslate("/MarkdownTest103/Code_Blocks");
        testTranslate("/MarkdownTest103/Code_Spans");
        testTranslate("/MarkdownTest103/Horizontal_rules");
        testTranslate("/MarkdownTest103/Inline_HTML_Simple");
        testTranslate("/MarkdownTest103/Inline_HTML_Advanced");
        testTranslate("/MarkdownTest103/Inline_HTML_comments");
        testTranslate("/MarkdownTest103/Links_inline_style");
        testTranslate("/MarkdownTest103/Links_reference_style");
        testTranslate("/MarkdownTest103/Links_shortcut_references");
        testTranslate("/MarkdownTest103/Literal_quotes_in_titles");
        testTranslate("/MarkdownTest103/Markdown_Documentation-Basic");
        testTranslate("/MarkdownTest103/Markdown_Documentation-Syntax");
        testTranslate("/MarkdownTest103/Nested_blockquotes");
        testTranslate("/MarkdownTest103/Ordered_and_unordered_lists");
        testTranslate("/MarkdownTest103/Strong_and_em_together");
        testTranslate("/MarkdownTest103/Tabs");
        testTranslate("/MarkdownTest103/Tidyness");

    }

}
