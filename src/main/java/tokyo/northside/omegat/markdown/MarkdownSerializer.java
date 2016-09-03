package tokyo.northside.omegat.markdown;

import org.pegdown.ast.*;


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
     * Process root node of PegDown parser's AST.
     * @param astRoot root node of AST.
     */
    void processNodes(final RootNode astRoot) {
        checkArgNotNull(astRoot, "astRoot");
        astRoot.accept(this);
    }

    /**
     * Accept root node.
     * @param node root node.
     */
    public void visit(final RootNode node) {
        node.getReferences().stream().forEachOrdered(this::visitChildren);
        node.getAbbreviations().stream().forEachOrdered(this::visitChildren);
        visitChildren(node);
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
     * Accept code node.
     * <p>
     *     put literals to translation entry.
     * @param node code node.
     */
    public void visit(final CodeNode node) {
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
     *     omegat tag.
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

}
