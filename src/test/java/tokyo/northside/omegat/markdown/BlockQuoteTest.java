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
 * Created by miurahr on 16/09/08.
 */
public class BlockQuoteTest {
    @Test
    public void testBlockQuote() throws Exception {
        String testInput = "\n> Example:\n" +
                "> \n" +
                ">     sub status {\n" +
                ">         print \"working\";\n" +
                ">     }\n" +
                "> \n" +
                "> Or:\n" +
                "> \n" +
                ">     sub status {\n" +
                ">         return \"working\";\n" +
                ">     }\n\n";
        List<String> expected = new ArrayList<>();
        expected.add("Example:\n\n" +
                     "    sub status {\n" +
                     "        print \"working\";\n" +
                     "    }\n" +
                     "\n" +
                     " Or:\n" +
                     "\n" +
                     "    sub status {\n" +
                     "        return \"working\";\n" +
                     "    }\n");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
        assertEquals(filter.getOutbuf(), testInput);
    }
}
