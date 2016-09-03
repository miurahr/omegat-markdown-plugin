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
