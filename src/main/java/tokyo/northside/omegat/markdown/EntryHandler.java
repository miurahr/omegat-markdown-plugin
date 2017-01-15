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

import com.vladsch.flexmark.ast.Node;
import org.omegat.filters2.TranslationException;


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
    private boolean exception = false;

    EntryHandler(final OmegatMarkdownFilter filter, final char[] article){
        this.filter = filter;

        articleBuf = article;
        currentBufPosition = 0;

        para = 0;
        st.push(MarkdownState.NORMAL.flag);
    }

    /**
     * Return specified Strings.
     */
    String getChars(final int index, final int length) {
        char[] buf = new char[length];
        System.arraycopy(articleBuf, index, buf, 0, length);
        return String.valueOf(buf);
    }

    int findFirst(final int index, final char c) {
        assert (index >= 0);
        int i;
        for (i = index; i < articleBuf.length; i++) {
            if (articleBuf[i] == c) {
                return i;
            }
        }
        return -1;
    }

    void startVerbatim(final int index) {
        startPara(index, MarkdownState.VERBATIM);
    }

    void endVerbatim() {
        endPara();
    }

    void startFenced(final int start, final String lang) {
        assert(lang != null);
        if (para > 0) {
            int index = start + lang.length() + 4;
            if (index - currentBufPosition > 0) {
                char[] buf = new char[index - currentBufPosition];
                System.arraycopy(articleBuf, currentBufPosition, buf, 0, index - currentBufPosition);
                String token = String.valueOf(buf);
                putMark(token);
                currentBufPosition = index;
            }
            startPara(MarkdownState.FENCEDINLINE);
        } else {
            filter.setFencedLang(lang);
            startPara(MarkdownState.FENCED);
        }
    }

    void endFenced() {
        if (filter.isMode(MarkdownState.FENCEDINLINE)) {
            putMark("```");
        }
        endPara();
        filter.resetFencedLang();
    }

    void startPara(final int index, final MarkdownState status) {
        if (index - currentBufPosition > 0) {
            char[] buf = new char[index - currentBufPosition];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, index - currentBufPosition);
            String token = String.valueOf(buf);
            filter.writeTranslate(token, false);
            currentBufPosition = index;
        }
        startPara(status);

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
        }
        st.pop();
        filter.setMode(st.peek());
    }

    /**
     * Functions called from Serializer.
     *
     * @param node PegDown's TextNode node.
     */
    void putEntry(final Node node) {
        String text = node.getChars().toVisibleWhitespaceString();
        putEntry(text);
        currentBufPosition = node.getEndOffset();
    }

    /**
     * Put entry and move current position.
     *
     * @param text
     * @param index
     */
    void putEntry(final String text, final int index) {
        putEntry(text);
        currentBufPosition = index;
    }

    /**
     * Put entry without moving current position.
     *
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
     *
     * @param chars
     * @param next
     */
    void putMark(final String chars, final int next) {
        putMark(chars);
        currentBufPosition = next;
    }

    /**
     * Put protected entry.
     *
     * @param chars
     */
    void putMark(final String chars) {
        putEntry(chars);
    }

    /**
     * flush rest of untranslated source.
     */
    void finish() throws TranslationException {
        int restSize = articleBuf.length - currentBufPosition;
        if (restSize > 0) {
            char[] buf = new char[restSize];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, restSize);
            filter.writeTranslate(String.valueOf(buf), false);
            currentBufPosition += restSize;
        }
        if (exception) {
            throw new TranslationException("Markdown plugin got internal error(s).");
        }
    }

    void throwTranslationException() {
        exception = true;
    }
}
