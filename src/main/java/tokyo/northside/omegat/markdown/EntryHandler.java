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

    EntryHandler(OmegatMarkdownFilter filter, final char[] article) {
        this.filter = filter;
        articleBuf = article;
        currentBufPosition = 0;
    }

    char[] getArticle() {
        return articleBuf;
    }

    void putEntry(TextNode node) {
        this.putEntry(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    void putEntry(String text, int start, int end) {
        if (start - currentBufPosition > 0) {
            char[] buf = new char[start - currentBufPosition];
            System.arraycopy(articleBuf, currentBufPosition, buf, 0, start - currentBufPosition);
            filter.writeTranslate(String.valueOf(buf), false);
            currentBufPosition = end;
            filter.writeTranslate(text, true);
        } else if (start - currentBufPosition == 0) {
            currentBufPosition = end;
            filter.writeTranslate(text, true);
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
