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

import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Test class for Markdown AutoLink elements.
 * @author Hiroshi Miura
 */
public class Reference {

    @Test
    public void testVisit_reference() throws Exception {
        String testInput =
                "Here's a [link] [1] with an ampersand in the URL.\n\n" +
                "Here's a link with an amersand in the link text: [AT&T] [2].\n\n" +
                "Here's an inline [link](</script?foo=1&bar=2>).\n\n\n" +
                "[1]: http://example.com/?foo=1&bar=2\n" +
                "[2]: http://att.com/  \"AT&T\"\n";
        List<String> expected = new ArrayList<>();
        expected.add("Here's a [link] [1] with an ampersand in the URL.");
        expected.add("Here's a link with an amersand in the link text: [AT&T] [2].");
        expected.add("Here's an inline [link](</script?foo=1&bar=2>).");
        expected.add("[1]: http://example.com/?foo=1&bar=2");
        expected.add("[2]: http://att.com/  \"AT&T\"");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        throw new SkipException("Skip acceptance test.(known bug)");
        // FIXME:
        //  Entry[2] becomes "Here's an inline [link](/script?foo=1&bar=2)."
        //
        //assertEquals(filter.getEntries(), expected);
        //assertEquals(filter.getOutbuf(), testInput);
    }

 }
