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

    char[] getArticle() {
        return articleBuf;
    }

    private void resetEntryBuf() {
        entryBuf = new StringBuilder();
        para = 0;
    }

    /**
     * Convenient function to call from Serializer.
     * @param node PegDown's TextNode node.
     */
    void putEntry(final TextNode node) {
        this.putEntry(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    void startPara() {
        para++;
    }

    void endPara() {
        para--;
        if (para <= 0) {
            filter.writeTranslate(entryBuf.toString(), true);
            resetEntryBuf();
        }
    }

    private String getMark(String tag) {
        return tag;
    }

    private void putEntry(final String text, final int start, final int end) {
        if (start - currentBufPosition > 0) {
            char[] buf = new char[start - currentBufPosition];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, start - currentBufPosition);
            String token = String.valueOf(buf);
            if (para > 0) {
                entryBuf.append(text);
            } else {
                filter.writeTranslate(token, false);
                filter.writeTranslate(text, true);
            }
            currentBufPosition = end;
        } else if (start - currentBufPosition == 0) {
            currentBufPosition = end;
            if (para > 0) {
                entryBuf.append(text);
            } else {
                filter.writeTranslate(text, true);
            }
        }
    }

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
