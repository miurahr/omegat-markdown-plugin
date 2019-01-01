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
 * Test class for Markdown List elements.
 * Created by miurahr on 16/08/26.
 */
public class ListTest {

    @Test
    public void testVisit_bulletList() throws Exception {
        String testInput = "- Item1\n\n" +
                "- Item2\n\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_orderedList() throws Exception {
        String testInput = "1. Ordered Item1\n\n" +
                "2. Ordered Item2\n\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testOrderedListMultiple() throws Exception {
        String testInput = "Multiple paragraphs:\n" +
                "\n" +
                "1.\tItem 1, graf one.\n" +
                "\n" +
                "\tItem 2. graf two. The quick brown fox jumped over the lazy dog's\n" +
                "\tback.\n" +
                "\t\n" +
                "2.\tItem 2.\n" +
                "\n" +
                "3.\tItem 3.\n" +
                "\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        List<String> expected = new ArrayList<>();
        expected.add("Multiple paragraphs:");
        expected.add("Item 1, graf one." +
                " Item 2. graf two. The quick brown fox jumped over the lazy dog's\n" +
                "back.");
        expected.add("Item 2.");
        expected.add("Item 3.");
        assertEquals(filter.getEntries(), expected);
        assertEquals(filter.getOutbuf(), testInput);
    }

 }
