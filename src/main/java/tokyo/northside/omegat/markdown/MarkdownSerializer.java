package tokyo.northside.omegat.markdown;

import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.AutoLinkNode;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.HtmlBlockNode;
import org.pegdown.ast.InlineHtmlNode;
import org.pegdown.ast.ListItemNode;
import org.pegdown.ast.MailLinkNode;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.SpecialTextNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.StrongEmphSuperNode;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.VerbatimNode;
import org.pegdown.ast.Visitor;


/**
 * Markdown parser and serializer class.
 * @author Hiroshi Miura
 */
class MarkdownSerializer extends AbstractMarkdownSerializer implements Visitor {
    private EntryHandler handler;

    MarkdownSerializer(final EntryHandler entryHandler) {
        handler = entryHandler;
    }

    /**
     * Accept text node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    public void visit(final TextNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Anchor link node.
     * <p>
     *     put literals to translation entry.
     * @param node link node.
     */
    public void visit(final AnchorLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Auto link node.
     * <p>
     *     put literals to translation entry.
     * @param node link node.
     */
    public void visit(final AutoLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept inline html node.
     * <p>
     *     put literals to translation entry.
     * @param node html node.
     */
    public void visit(final InlineHtmlNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept mail link node.
     * <p>
     *     put literals to translation entry.
     * @param node mail link.
     */
    public void visit(final MailLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept special text node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    public void visit(final SpecialTextNode node) {
        handler.putEntry(node);
    }

     /**
     * Accept HTML Block node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    public void visit(final HtmlBlockNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept verbatim node.
     * <p>
     *     put literals to translation entry.
     * @param node verbatim node.
     */
    public void visit(final VerbatimNode node) {
        handler.putEntry(node);
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    public void visit(final ParaNode node) {
        handler.startPara();
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Start list item paragraph.
     * @param node list item node.
     */
    public void visit(final ListItemNode node) {
        handler.startPara();
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Strong or Emphasis node.
     * <p>
     *     it is inline component and replace token with OmegaT tag.
     * </p>
     * @param node
     */
    public void visit(final StrongEmphSuperNode node) {
        handler.startPara();
        handler.putMark(node.getChars(), node.getStartIndex() + node.getChars().length());
        visitChildren(node);
        handler.putMark(node.getChars(), node.getEndIndex());
        handler.endPara();
    }

    /**
     * Accept Strike node.
     * <p>
     *     it is inline component and replace token with
     *     OmegaT tag.
     * </p>
     * @param node
     */
    public void visit(final StrikeNode node) {
        handler.startPara();
        handler.putMark(node.getChars(), node.getStartIndex() + node.getChars().length());
        visitChildren(node);
        handler.putMark(node.getChars(), node.getEndIndex());
        handler.endPara();
    }

    /**
     * Accept external link node.
     * <p>
     *     handle node as single paragraph and replace token
     *     with OmegaT tag.
     * </p>
     * @param node
     */
    public void visit(final ExpLinkNode node) {
        handler.startPara();
        handler.putMark("[", node.getStartIndex() + 1);
        visitChildren(node);
        handler.putMark("](");
        handler.putEntry(node.url);
        handler.putMark(")", node.getEndIndex());
        handler.endPara();
    }

    public void visit(final HeaderNode node) {
        handler.startPara();
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Code node.
     *
     * @param node
     */
    public void visit(final CodeNode node) {
        int textLen = node.getText().length();
        int nodeLen = node.getEndIndex() - node.getStartIndex();
        if (nodeLen - textLen < 3) { // inline code
            handler.startPara();
            handler.putMark("`");
            handler.putEntry(node);
            handler.putMark("`");
            handler.endPara();
        }
        // FIXME: else code block, ignored currently.
    }


}
