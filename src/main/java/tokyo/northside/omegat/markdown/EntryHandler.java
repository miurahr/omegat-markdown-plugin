package tokyo.northside.omegat.markdown;

import org.apache.commons.lang.StringEscapeUtils;
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
     * <p>
     *     This escapes embed HTML.
     * </p>
     * @param node PegDown's TextNode node.
     */
    void putEntry(final TextNode node) {
        String text = StringEscapeUtils.escapeHtml(node.getText());
        int start = node.getStartIndex();
        int end = node.getEndIndex();
        putEntry(text, start, end);
    }

    void putMark(final String chars) {
        if (chars.startsWith("**")) {
            putEntry("<b>");
        } else if (chars.startsWith("__")) { // italic
            putEntry("<i>");
        } else if (chars.startsWith("~~")) { //strikethrough
            putEntry("<s>");
        } else if (chars.startsWith("<")) {
            putEntry(chars);
        }
    }

    void putMark(final String chars, final int next) {
        putMark(chars);
        currentBufPosition = next;
    }

    String convMark(final String text) {
        return text.replaceAll("<b>", "**")
                .replaceAll("<i>", "__")
                .replaceAll("<a>", "[")
                .replaceAll("</a>", "]")
                .replaceAll("<h>", "(")
                .replaceAll("</h>", ")")
                .replaceAll("<s>", "~~");
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

    void putEntry(final String text, final int index) {
        putEntry(text);
        currentBufPosition = index;
    }

    void putEntry(final String text) {
        if (para > 0) {
            entryBuf.append(text);
        } else {
            filter.writeTranslate(text, true);
        }
    }

    private void putEntry(final String text, final int start, final int end) {
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
