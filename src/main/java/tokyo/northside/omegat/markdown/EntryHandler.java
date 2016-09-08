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

import org.pegdown.ast.TextNode;


/**
 * Entry Handler for PegDown Markdown parser.
 * <p>
 * Convert serializer callback to OmegaT entries.
 * @author Hiroshi Miura
 */
class EntryHandler {

    private OmegatMarkdownFilter filter;
    private char[] articleBuf;
    private int currentBufPosition;

    private StringBuilder entryBuf;
    private int para;

    EntryHandler(final OmegatMarkdownFilter filter, final char[] article) {
        this.filter = filter;

        articleBuf = article;
        currentBufPosition = 0;

        entryBuf = new StringBuilder();
        para = 0;
    }

    /**
     * Get source article.
     * @return source article i.
     */
    char[] getArticle() {
        return articleBuf;
    }

    /**
     * Start paragraph.
     */
    void startPara() {
        para++;
    }

    /**
     * End paragraph.
     */
    void endPara() {
        para--;
        if (para <= 0) {
            filter.writeTranslate(entryBuf.toString(), true);
            entryBuf = new StringBuilder();
            para = 0;
        }
    }

    /**
     * Functions called from Serializer.
     * @param node PegDown's TextNode node.
     */
    void putEntry(final TextNode node) {
        String text = node.getText();
        int start = node.getStartIndex();
        int end = node.getEndIndex();

        if (start - currentBufPosition > 0) {
            char[] buf = new char[start - currentBufPosition];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, start - currentBufPosition);
            String token = String.valueOf(buf);
            filter.writeTranslate(token, false);
            putEntry(text, end);
        } else if (start - currentBufPosition == 0) {
            putEntry(text, end);
        }
     }

    /**
     * Put entry and move current position.
     * @param text
     * @param index
     */
    void putEntry(final String text, final int index) {
        putEntry(text);
        currentBufPosition = index;
    }

    /**
     * Put entry without moving current position.
     * @param text
     */
    void putEntry(final String text) {
        if (para > 0) {
            entryBuf.append(text);
        } else {
            filter.writeTranslate(text, true);
        }
    }

    /**
     * Put protected entry and move current position.
     * @param chars
     * @param next
     */
    void putMark(final String chars, final int next) {
        putMark(chars);
        currentBufPosition = next;
    }

    void putMarkOut(final String chars) {
        if (para == 0) {
            filter.writeTranslate(chars, false);
        }
    }

    /**
     * Put protected entry.
     * @param chars
     */
    void putMark(final String chars) {
       putEntry(chars);
    }

    /**
     * flush rest of untranslated source.
     */
    void finish() {
        int restSize = articleBuf.length - currentBufPosition;
        if (restSize > 0) {
            char[] buf = new char[restSize];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, restSize);
            filter.writeTranslate(String.valueOf(buf), false);
            currentBufPosition += restSize;
        }
    }
}
