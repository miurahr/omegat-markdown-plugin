package tokyo.northside.omegat.markdown;

import org.omegat.core.data.ProtectedPart;
import org.pegdown.ast.TextNode;

import java.util.ArrayList;
import java.util.List;


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

    private List<ProtectedPart> protectedParts;

    EntryHandler(final OmegatMarkdownFilter filter, final char[] article) {
        this.filter = filter;

        articleBuf = article;
        currentBufPosition = 0;

        entryBuf = new StringBuilder();
        para = 0;
        protectedParts = new ArrayList<>();
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
        putEntry(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    void putMark(final String chars, final int next) {
        putProtectedEntry(chars);
        currentBufPosition = next;
    }

    void putMark(final String chars) {
        putProtectedEntry(chars);
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
            filter.writeTranslate(text, ProtectedPart.extractFor(protectedParts, text));
        }
    }

    void putProtectedEntry(final String text) {
        ProtectedPart pp = new ProtectedPart();
        pp.setTextInSourceSegment(text);
        protectedParts.add(pp);
        if (para > 0) {
            entryBuf.append(text);
        } else {
            filter.writeTranslate(text, ProtectedPart.extractFor(protectedParts, text));
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
