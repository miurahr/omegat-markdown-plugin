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

import java.util.Stack;


/**
 * Entry Handler for PegDown Markdown parser.
 * <p>
 * Convert serializer callback to OmegaT entries.
 * @author Hiroshi Miura
 */
class EntryHandler {

    private OmegatMarkdownFilter filter;
    private char[] articleBuf;
    private StringBuilder sb;
    private int currentBufPosition;
    private int para;
    private Stack<Integer> st = new Stack<>();

    EntryHandler(final OmegatMarkdownFilter filter, final char[] article) {
        this.filter = filter;

        articleBuf = article;
        currentBufPosition = 0;

        para = 0;
        st.empty();
        st.push(MarkdownState.NORMAL.flag);
    }

    /**
     * Start paragraph.
     */
    void startPara(final MarkdownState status) {
        int currentState = st.peek();
        st.push(currentState | status.flag);
        filter.setMode(st.peek());
        if (para == 0) {
            sb = new StringBuilder();
        }
        para++;
    }

    /**
     * End paragraph.
     */
    void endPara() {
        para--;
        if (para <= 0) {
            putEntry(sb.toString());
            para = 0;
            st.empty();
            st.push(MarkdownState.NORMAL.flag);
        }
        st.pop();
        filter.setMode(st.peek());
    }

    /**
     * Functions called from Serializer.
     * @param node PegDown's TextNode node.
     */
    void putEntry(final TextNode node) {
        String text = node.getText();
        int start = node.getStartIndex();
        int end = node.getEndIndex();
        putEntry(text, start, end);
     }

     void putEntry(final  String text, final int start, final int end) {
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

    void putEntry(final int start, final int end) {
        if (currentBufPosition < start) {
            char[] buf = new char[start - end];
            System.arraycopy(articleBuf, start, buf, 0, start - end);
            String token = String.valueOf(buf);
            putEntry(token);
            currentBufPosition = end;
        }
    }

    /**
     * Put entry without moving current position.
     * @param text
     */
    void putEntry(final String text) {
         if (para > 0) {
            sb.append(text);
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
