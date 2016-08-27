package tokyo.northside.omegat;

import org.pegdown.ast.*;

import static org.parboiled.common.Preconditions.checkArgNotNull;
import static org.parboiled.common.StringUtils.repeat;

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

    public void visit(Node node) {
    }

    public void visit(AbbreviationNode node) {
    }

    public void visit(ReferenceNode node) {
    }


    public void visit(AnchorLinkNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(AutoLinkNode node) {
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

    public void visit(CodeNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(DefinitionListNode node) {
    }

    public void visit(DefinitionNode node) {
    }

    public void visit(DefinitionTermNode node) {
    }

    public void visit(ExpImageNode node) {
    }

    public void visit(ExpLinkNode node) {
        visitChildren(node);
    }

    public void visit(HeaderNode node) {
        visitChildren(node);
   }

    public void visit(HtmlBlockNode node) {
    }

    public void visit(InlineHtmlNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(MailLinkNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    /**
     * Start paragraph.
     * @param node
     */
    public void visit(ParaNode node) {
        visitChildren(node);
    }

    /**
     * Start quote block.
     * @param node
     */
    public void visit(QuotedNode node) {
        visitChildren(node);
    }

    public void visit(RefImageNode node) {
        // fixme
    }

    public void visit(RefLinkNode node) {
        // fixme
        visitChildren(node);
    }

    public void visit(SimpleNode node) {
        switch (node.getType()) {
            case Apostrophe:
                markdownFilter.writeTranslate("'", false);
                break;
            case Ellipsis:
                break;
            case Emdash:
                break;
            case Endash:
                break;
            case HRule:
                break;
            case Linebreak:
                break;
            case Nbsp:
                break;
            default:
                break;
        }
    }

    public void visit(SpecialTextNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
    }

    public void visit(StrikeNode node) {
        visitChildren(node);
    }

    public void visit(StrongEmphSuperNode node) {
        visitChildren(node);
    }

    public void visit(TableBodyNode node) {
    }

    public void visit(TableCaptionNode node) {
    }

    public void visit(TableCellNode node) {
    }

    public void visit(TableColumnNode node) {
    }

    public void visit(TableHeaderNode node) {
    }

    public void visit(TableNode node) {
    }

    public void visit(TableRowNode node) {
    }

    public void visit(VerbatimNode node) {
    }

    public void visit(WikiLinkNode node) {
    }

    public void visit(TextNode node) {
        markdownFilter.writeTranslate(node.getText(), node.getStartIndex(), node.getEndIndex());
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

}
