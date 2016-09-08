package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created by miurahr on 16/09/08.
 */
public class TableTest extends TestFilterBase {

    @Test
    void testProcess_table() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/table.md");
        List<String> expected = new ArrayList<>();
        expected.add("Tables");
        expected.add("First Header ");
        expected.add("Second Header ");
        expected.add("Content Cell ");
        expected.add("Content Cell ");
        expected.add("Content Cell ");
        expected.add("Content Cell ");
        expected.add("Command ");
        expected.add("Description ");
        expected.add("git status ");
        expected.add("List all new or modified files ");
        expected.add("git diff ");
        expected.add("Show file differences that haven't been staged ");
        expected.add("Inline formatting");
        expected.add("Command ");
        expected.add("Description ");
        expected.add("`git status` ");
        expected.add("List all *new or modified* files ");
        expected.add("`git diff` ");
        expected.add("Show file differences that **haven't been** staged ");
        expected.add("Alignment");
        expected.add("Left-aligned ");
        expected.add("Center-aligned ");
        expected.add("Right-aligned ");
        expected.add("git status ");
        expected.add("git status ");
        expected.add("git status ");
        expected.add("git diff ");
        expected.add("git diff ");
        expected.add("git diff ");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_table() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/table.md");
    }

}
