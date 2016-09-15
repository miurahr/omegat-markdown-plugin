package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by miurahr on 16/09/08.
 */
public class AutolinkTest extends TestFilterBase {

    @Test
    void testProcess_autolink() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/autolink.md");
        List<String> expected = new ArrayList<>();
        expected.add("Link: <http://example.com/>.");
        expected.add("With an ampersand: <http://example.com/?foo=1&bar=2>");
        expected.add("In a list?");
        expected.add("<http://example.com/>");
        expected.add("It should.");
        expected.add("Blockquoted: <http://example.com/>");
        expected.add("Auto-links should not occur here: `<http://example.com/>`");
        expected.add("or here: <http://example.com/>");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_autolink() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/autolink.md");
    }

}
