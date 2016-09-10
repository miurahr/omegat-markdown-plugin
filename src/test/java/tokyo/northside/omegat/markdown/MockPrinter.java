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

import org.omegat.util.NullBufferedWriter;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Markdown printer from MarkdownEntries to output.
 * @Author Hiroshi Miura
 */
public class MockPrinter extends MarkdownPrinter {
    private MarkdownState status;
    private String lineFeed;
    private StringBuilder sb;

    public MockPrinter() {
        this(new StringBuilder(), "\n");
    }

    public MockPrinter(final BufferedWriter writer, final String lineFeed) {
        this(new StringBuilder(), lineFeed);
    }

    /**
     * Constructor with writer and LF string.
     *
     * @param sb  to be write.
     * @param lineFeed LF character, "\n" or "\r\n"
     */
    public MockPrinter(final StringBuilder sb, final String lineFeed) {
        this.sb = sb;
        status = MarkdownState.NORMAL;
        this.lineFeed = lineFeed;
    }

    @Override
    public void write(final String entry) throws IOException {
        String out = replaceEntry(entry);
        sb.append(out);
    }

    protected String getOutput() {
        return sb.toString();
    }
}
