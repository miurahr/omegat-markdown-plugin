package tokyo.northside.omegat;

import org.pegdown.ast.*;

import static org.parboiled.common.Preconditions.checkArgNotNull;

/**
 * Markdown parser and serializer class.
 * Created by miurahr on 16/08/22.
 */
class MarkdownSerializer implements Visitor {
    private EntryHandler handler;

    MarkdownSerializer(EntryHandler entryHandler) {
        handler = entryHandler;
    }

    /**
     * Process root node of PegDown parser's AST.
     * @param astRoot root node of AST.
     */
    void processNodes(RootNode astRoot) {
        checkArgNotNull(astRoot, "astRoot");
        astRoot.accept(this);
    }

    /**
     * Accept root node.
     * @param node root node.
     */
    public void visit(RootNode node) {
        node.getReferences().stream().forEachOrdered(this::visitChildren);
        node.getAbbreviations().stream().forEachOrdered(this::visitChildren);
        visitChildren(node);
    }

    /**
     * Accept text node.
     * @param node text node.
     */
    public void visit(TextNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Anchor link node.
     * @param node link node.
     */
    public void visit(AnchorLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Auto link node.
     * @param node link node.
     */
    public void visit(AutoLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept code node.
     * @param node code node.
     */
    public void visit(CodeNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept inline html node.
     * @param node html node.
     */
    public void visit(InlineHtmlNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept mail link node.
     * @param node mail link.
     */
    public void visit(MailLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept special text node.
     * @param node text node.
     */
    public void visit(SpecialTextNode node) {
        handler.putEntry(node);
    }

    public void visit(HtmlBlockNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept verbatim node.
     * @param node verbatim node.
     */
    public void visit(VerbatimNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept table node.
     * @param node table node.
     */
    public void visit(TableNode node) {
        visitChildren(node);
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    public void visit(ParaNode node) {
        visitChildren(node);
    }

    /**
     * Accept other nodes which should visit children.
     * @param node super node.
     */
    public void visit(BlockQuoteNode node) {
        visitChildren(node);
    }

    public void visit(BulletListNode node) {
        visitChildren(node);
    }

    public void visit(OrderedListNode node) {
        visitChildren(node);
    }

    public void visit(ListItemNode node) {
        visitChildren(node);
    }

    public void visit(ExpLinkNode node) {
        visitChildren(node);
    }

    public void visit(HeaderNode node) {
        visitChildren(node);
    }

    public void visit(QuotedNode node) {
        visitChildren(node);
    }

    public void visit(RefLinkNode node) {
        visitChildren(node);
    }

    public void visit(StrikeNode node) {
        visitChildren(node);
    }

    public void visit(StrongEmphSuperNode node) {
        visitChildren(node);
    }

    public void visit(TableBodyNode node) {
        visitChildren(node);
    }

    public void visit(TableCaptionNode node) {
        visitChildren(node);
    }

    public void visit(TableCellNode node) {
        visitChildren(node);
    }

    public void visit(TableColumnNode node) {
        visitChildren(node);
    }

    public void visit(TableHeaderNode node) {
        visitChildren(node);
    }

    public void visit(TableRowNode node) {
        visitChildren(node);
    }

    public void visit(SuperNode node) {
        visitChildren(node);
    }

    // helper
    private void visitChildren(SuperNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    /**
     * Accept node which can be ignored.
     * @param node some node.
     */
    public void visit(RefImageNode node) {}

    public void visit(Node node) {}

    public void visit(AbbreviationNode node) {}

    public void visit(ReferenceNode node) {}

    public void visit(SimpleNode node) {}

    public void visit(WikiLinkNode node) {}

    public void visit(DefinitionListNode node) {}

    public void visit(DefinitionNode node) {}

    public void visit(DefinitionTermNode node) {}

    public void visit(ExpImageNode node) {}
}
