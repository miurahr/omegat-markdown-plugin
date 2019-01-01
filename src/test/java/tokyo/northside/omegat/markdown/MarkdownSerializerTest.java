/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2016 Hiroshi Miura
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Test class for Markdown elements.
 * Created by miurahr on 16/08/26.
 */
public class MarkdownSerializerTest {

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
        expected.add("Normal clause part 1\n" +
                "[External link part 1](https://example.com/link/to/external/url)\n" +
                "continuous clause sentense.");
        expected.add("Heading level3 **Bold part 1**");
        expected.add("quote part1\n");
        expected.add("Normal clause part 2 __Italic__");
        expected.add("quote part2 (code)\n" +
                "#! /bin/sh\n" +
                "#\n" +
                "echo hello world.\n");
        expected.add("Heading level 4");
        expected.add("Normal clause part 3. ~~strikethroughs~~\n" +
                "Test for Styling text **bold and __italic__**");
        expected.add("Heading with under lines (level2)");
        expected.add("In the word of abraham");
        expected.add("quote.");
        expected.add("ordered list (1)");
        expected.add("ordered list (2)\n" +
                " multiline");
        expected.add("<!-- HTML style comment -->");
        expected.add("unordered list (1)");
        expected.add("unordered list (2)");
        expected.add("unordered list (3)\n" +
                " continuous line.");
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
    }

   @Test
    public void testVisit_inlineCode() throws Exception {
        String testInput = "By default, when building the site, all files are copied to the destination `_site` folder." +
                "Some files are excluded in the `_config.yml` and `sdkdocs-template/jekyll/_config-defaults.yml` files.";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_inlineCode_in_listItem() throws Exception {
        String testInput = "Here is a list.\n\n" +
                "* Some are excluded in the `_config.yml` file.\n";
        List<String> expected = new ArrayList<>();
        expected.add("Here is a list.");
        expected.add("Some are excluded in the `_config.yml` file.");
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
        assertEquals(filter.getEntries(), expected);
    }

}
