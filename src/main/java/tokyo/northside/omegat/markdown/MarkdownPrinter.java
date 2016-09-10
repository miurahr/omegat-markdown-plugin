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
import java.util.regex.Pattern;

/**
 * Markdown printer from MarkdownEntries to output.
 * @Author Hiroshi Miura
 */
public class MarkdownPrinter {
    private int status;
    private String lineFeed;
    private BufferedWriter writer;

    public MarkdownPrinter() {
        this(new NullBufferedWriter(), "\n");
    }

    public MarkdownPrinter(final BufferedWriter writer) {
        this(writer, "\n");
    }

    /**
     * Constructor with writer and LF string.
     *
     * @param writer BufferedWriter to be write.
     * @param lineFeed LF character, "\n" or "\r\n"
     */
    public MarkdownPrinter(final BufferedWriter writer, final String lineFeed) {
        this.writer = writer;
        status = MarkdownState.NORMAL.flag;
        this.lineFeed = lineFeed;
    }

    public void write(final String entry) throws IOException {
        String out = replaceEntry(entry);
        writer.write(out);
    }

    public void setMode(final int status) {
        this.status = status;
    }

    protected String replaceEntry(final String text) {
        String tmp;
        StringBuilder sb = new StringBuilder();
        if ((status & MarkdownState.VERBATIM.flag) > 0) {
            sb.append("\t");
            tmp = text.replaceAll("\\n", "\n\t");
            sb.append(tmp);
        } else if ((status & MarkdownState.BLOCKQUOTE.flag) > 0) {
            tmp = text.replaceAll("(.+)(\n)(.*)", "$1\n> $3");
            sb.append(tmp);
        }
        // default
        if (status == MarkdownState.NORMAL.flag) {
            sb.append(text);
        }
        return sb.toString();
    }

    /** Interface for debug */
    protected String getOutput() {
        return "";
    }
}
