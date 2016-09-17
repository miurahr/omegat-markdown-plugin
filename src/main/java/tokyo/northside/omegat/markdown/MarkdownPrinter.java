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
 * @author Hiroshi Miura
 */
class MarkdownPrinter {
    private int status = MarkdownState.NORMAL.flag;
    protected String lineFeed;
    private BufferedWriter writer;
    private String fencedLang = "";

    /**
     * A Markdown printer to produce markdown file according to status.
     * Default construction with Null writer.
     */
    MarkdownPrinter() {
        this(new NullBufferedWriter(), "\n");
    }

    /**
     * Constructor with specified writer.
     * @param writer buffered writer to output.
     */
    MarkdownPrinter(final BufferedWriter writer) {
        this(writer, "\n");
    }

    /**
     * Constructor with writer and LF string.
     *
     * @param writer BufferedWriter to be write.
     * @param lineFeed LF character, "\n" or "\r\n"
     */
    MarkdownPrinter(final BufferedWriter writer, final String lineFeed) {
        this.writer = writer;
        this.lineFeed = lineFeed;
    }

    void write(final String entry) throws IOException {
        String out = replaceEntry(entry);
        writer.write(out);
    }

    void setMode(final int val) {
        this.status = val;
    }

    int getMode() {
        return status;
    }

    void setFencedLang(final String lang) {
        fencedLang = lang;
    }

    void resetFencedLang() {
        fencedLang = "";
    }

    protected String replaceEntry(final String text) {
        String tmp;
        StringBuilder sb = new StringBuilder();
        if ((status & MarkdownState.VERBATIM.flag) > 0) {
            sb.append("\t");
            tmp = text.replaceAll("\\n", "\n\t");
            sb.append(tmp);
        } else if ((status & MarkdownState.FENCED.flag) > 0) {
            sb.append("```").append(fencedLang).append("\n").append(text).append("```");
        } else if ((status & MarkdownState.BLOCKQUOTE.flag) > 0) {
            tmp = text.replaceAll("(.+)(\n)(.*)", "$1\n> $3");
            sb.append(tmp);
        }
        // default
        if (status == MarkdownState.NORMAL.flag) {
            sb.append(text);
        }
        return sb.toString().replaceAll("\\n", lineFeed);
    }

    /** Interface for debug. */
    protected String getOutput() {
        return "";
    }
}
