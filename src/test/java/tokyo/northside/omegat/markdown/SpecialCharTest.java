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

import static org.testng.Assert.assertEquals;

/**
 * Test for quote elements.
 * @author Hiroshi Miura
 */
public class SpecialCharTest {
    @Test
    public void testDoubleQuote() throws Exception {
        String testInput = "Here is a \"special character\" embedded.";
        List<String> expected = new ArrayList<>();
        expected.add("Here is a \"special character\" embedded.");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getOutbuf(), testInput);
        assertEquals(filter.getEntries(), expected);
    }

    @Test
    public void testSingleQuote() throws Exception {
        String testInput = "Show file differences that **haven't been** staged";
        List<String> expected = new ArrayList<>();
        expected.add(testInput);
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
        assertEquals(filter.getOutbuf(), testInput);
    }
}
