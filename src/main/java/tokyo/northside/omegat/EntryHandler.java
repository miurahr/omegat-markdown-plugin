package tokyo.northside.omegat;

import org.pegdown.ast.TextNode;

/**
 * Entry Handler for PegDown Markdown parser.
 * <p>
 * Convert serializer callback to OmegaT entries.
 * @author Hiroshi Miura
 */
class EntryHandler {

    private OmegatMarkdownFilter filter;

    EntryHandler(OmegatMarkdownFilter filter) {
        this.filter = filter;
    }

    void putEntry(TextNode node) {
        filter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    void finish() {
        filter.flushToEof();
    }
}
