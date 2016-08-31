package tokyo.northside.omegat.markdown;

import org.pegdown.ast.AbbreviationNode;
import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.AutoLinkNode;
import org.pegdown.ast.BlockQuoteNode;
import org.pegdown.ast.BulletListNode;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.DefinitionListNode;
import org.pegdown.ast.DefinitionNode;
import org.pegdown.ast.DefinitionTermNode;
import org.pegdown.ast.ExpImageNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.HtmlBlockNode;
import org.pegdown.ast.InlineHtmlNode;
import org.pegdown.ast.ListItemNode;
import org.pegdown.ast.MailLinkNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.OrderedListNode;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.QuotedNode;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.RefLinkNode;
import org.pegdown.ast.ReferenceNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SimpleNode;
import org.pegdown.ast.SpecialTextNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.StrongEmphSuperNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TableBodyNode;
import org.pegdown.ast.TableCaptionNode;
import org.pegdown.ast.TableCellNode;
import org.pegdown.ast.TableColumnNode;
import org.pegdown.ast.TableHeaderNode;
import org.pegdown.ast.TableNode;
import org.pegdown.ast.TableRowNode;
import org.pegdown.ast.TextNode;
import org.pegdown.ast.VerbatimNode;
import org.pegdown.ast.Visitor;
import org.pegdown.ast.WikiLinkNode;

import static org.parboiled.common.Preconditions.checkArgNotNull;

/**
 * Markdown parser and serializer class.
 * @author Hiroshi Miura
 */
class MarkdownSerializer implements Visitor {
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
     * @param node text node.
     */
    public void visit(final TextNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Anchor link node.
     * @param node link node.
     */
    public void visit(final AnchorLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Auto link node.
     * @param node link node.
     */
    public void visit(final AutoLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept code node.
     * @param node code node.
     */
    public void visit(final CodeNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept inline html node.
     * @param node html node.
     */
    public void visit(final InlineHtmlNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept mail link node.
     * @param node mail link.
     */
    public void visit(final MailLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept special text node.
     * @param node text node.
     */
    public void visit(final SpecialTextNode node) {
        handler.putEntry(node);
    }

    public void visit(final HtmlBlockNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept verbatim node.
     * @param node verbatim node.
     */
    public void visit(final VerbatimNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept table node.
     * @param node table node.
     */
    public void visit(final TableNode node) {
        visitChildren(node);
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    public void visit(final ParaNode node) {
        visitChildren(node);
    }

    /**
     * Accept other nodes which should visit children.
     * @param node super node.
     */
    public void visit(final BlockQuoteNode node) {
        visitChildren(node);
    }

    public void visit(final BulletListNode node) {
        visitChildren(node);
    }

    public void visit(final OrderedListNode node) {
        visitChildren(node);
    }

    public void visit(final ListItemNode node) {
        visitChildren(node);
    }

    public void visit(final ExpLinkNode node) {
        visitChildren(node);
    }

    public void visit(final HeaderNode node) {
        visitChildren(node);
    }

    public void visit(final QuotedNode node) {
        visitChildren(node);
    }

    public void visit(final RefLinkNode node) {
        visitChildren(node);
    }

    public void visit(final StrikeNode node) {
        visitChildren(node);
    }

    public void visit(final StrongEmphSuperNode node) {
        visitChildren(node);
    }

    public void visit(final TableBodyNode node) {
        visitChildren(node);
    }

    public void visit(final TableCaptionNode node) {
        visitChildren(node);
    }

    public void visit(final TableCellNode node) {
        visitChildren(node);
    }

    public void visit(final TableColumnNode node) {
        visitChildren(node);
    }

    public void visit(final TableHeaderNode node) {
        visitChildren(node);
    }

    public void visit(final TableRowNode node) {
        visitChildren(node);
    }

    public void visit(final SuperNode node) {
        visitChildren(node);
    }

    // helper
    private void visitChildren(final SuperNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    /**
     * Accept node which can be ignored.
     * @param node some node.
     */
    public void visit(final RefImageNode node) {
        // ignored.
    }

    public void visit(final Node node) {
        // ignored.
    }

    public void visit(final AbbreviationNode node) {
        // ignored.
    }

    public void visit(final ReferenceNode node) {
        // ignored.
    }

    public void visit(final SimpleNode node) {
        // ignored.
    }

    public void visit(final WikiLinkNode node) {
        // ignored.
    }

    public void visit(final DefinitionListNode node) {
        // ignored.
    }

    public void visit(final DefinitionNode node) {
        // ignored.
    }

    public void visit(final DefinitionTermNode node) {
        // ignored.
    }

    public void visit(final ExpImageNode node) {
        // ignored.
    }
}
