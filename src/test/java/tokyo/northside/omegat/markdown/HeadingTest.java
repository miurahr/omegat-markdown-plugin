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

import static org.testng.Assert.*;

/**
 * Test class for Markdown HEADING elements.
 * Created by miurahr on 16/08/26.
 */
public class HeadingTest {

    @Test
    public void testVisit_heading1() throws Exception {
        String testInput = "# HEADING1\n\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_heading2() throws Exception {
        String testInput = "## HEADING2\n\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testVisit_heading6() throws Exception {
        String testInput = "###### HEADING6\n\n";
        OmegatMarkdownFilter filter = new OmegatMarkdownFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
    }
}
