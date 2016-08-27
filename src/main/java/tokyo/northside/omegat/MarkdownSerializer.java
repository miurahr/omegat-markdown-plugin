package tokyo.northside.omegat;

import org.pegdown.ast.*;

import static org.parboiled.common.Preconditions.checkArgNotNull;

/**
 * Markdown parser and serializer class.
 * Created by miurahr on 16/08/22.
 */
class MarkdownSerializer implements Visitor {
    private AbstractMarkdownFilter markdownFilter;

    MarkdownSerializer(AbstractMarkdownFilter filter) {
        markdownFilter = filter;
    }

    void processNodes(RootNode astRoot) {
        checkArgNotNull(astRoot, "astRoot");
        astRoot.accept(this);
    }

    public void visit(RootNode node) {
        node.getReferences().stream().forEachOrdered(refNode -> visitChildren(refNode));
        node.getAbbreviations().stream().forEachOrdered(abbrNode -> visitChildren(abbrNode));
        visitChildren(node);
    }

    public void visit(TextNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(AnchorLinkNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(AutoLinkNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(CodeNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(InlineHtmlNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(MailLinkNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(SpecialTextNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

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

    public void visit(ParaNode node) {
        visitChildren(node);
    }

    public void visit(QuotedNode node) {
        visitChildren(node);
    }

    public void visit(RefImageNode node) {
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

    public void visit(TableNode node) {
        markdownFilter.startTable();
        visitChildren(node);
        markdownFilter.endTable();
    }

    public void visit(TableRowNode node) {
        visitChildren(node);
    }

    public void visit(SuperNode node) {
        visitChildren(node);
    }

    // helpers
    private void visitChildren(SuperNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    public void visit(Node node) {
    }

    public void visit(AbbreviationNode node) {
    }

    public void visit(ReferenceNode node) {
    }

    public void visit(HtmlBlockNode node) {
    }

    public void visit(SimpleNode node) {
    }

    public void visit(VerbatimNode node) {
    }

    public void visit(WikiLinkNode node) {
    }

    public void visit(DefinitionListNode node) {
    }

    public void visit(DefinitionNode node) {
    }

    public void visit(DefinitionTermNode node) {
    }

    public void visit(ExpImageNode node) {
    }
}
