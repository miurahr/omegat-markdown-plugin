package tokyo.northside.omegat;

import org.pegdown.ast.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.parboiled.common.Preconditions.checkArgNotNull;


/**
 * Markdown parser and serializer class.
 * Created by miurahr on 16/08/22.
 */
class MarkdownSerializer implements Visitor {
    private StringBuilder printer = new StringBuilder();
    private final Map<String, ReferenceNode> references = new HashMap<>();
    private MarkdownFilter markdownFilter;
    private int listOrder;

    MarkdownSerializer(MarkdownFilter filter) {
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
        // node.getName()
    }

    public void visit(AutoLinkNode node) {
        // node.getText()
    }

    public void visit(BlockQuoteNode node) {
    }

    public void visit(BulletListNode node) {

    }

    public void visit(CodeNode node) {
        try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(DefinitionListNode node) {
    }

    public void visit(DefinitionNode node) {
    }

    public void visit(DefinitionTermNode node) {
    }

    public void visit(ExpImageNode node) {
        String text = printChildrenToString(node);
        // node.url
        // node.title

    }

    public void visit(ExpLinkNode node) {
    }

    public void visit(HeaderNode node) {
        int level = node.getLevel();
        try {
            markdownFilter.writeTranslate("<h" + Integer.toString(level) + ">", false);
        } catch (IOException ioe) {
            // FIXME
        }
        visitChildren(node);
    }

    public void visit(HtmlBlockNode node) {
    }

    public void visit(InlineHtmlNode node) {
         try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(ListItemNode node) {
        String markChar = markdownFilter.getChars(node.getStartIndex(), node.getEndIndex());
        try {
            markdownFilter.writeTranslate(markChar, false);
        } catch (IOException ioe) {
            // FIXME
        }
        visitChildren(node);
    }

    public void visit(MailLinkNode node) {
         try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(OrderedListNode node) {
        String markChar = markdownFilter.getChars(node.getStartIndex(), node.getEndIndex());
        try {
            markdownFilter.writeTranslate(markChar, false);
        } catch (IOException ioe) {
            // FIXME
        }
        visitChildren(node);
    }

    /**
     * Start paragraph.
     * @param node
     */
    public void visit(ParaNode node) {
        try {
            markdownFilter.writeTranslate(node.getStartIndex(), node.getEndIndex(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    /**
     * Start quote block.
     * @param node
     */
    public void visit(QuotedNode node) {
        switch (node.getType()) {
            case DoubleAngle:
                printer.append("&laquo;");
                visitChildren(node);
                printer.append("&raquo;");
                break;
            case Double:
                printer.append("&ldquo;");
                visitChildren(node);
                printer.append("&rdquo;");
                break;
            case Single:
                printer.append("&lsquo;");
                visitChildren(node);
                printer.append("&rsquo;");
                break;
        }
        System.out.println(printer.toString());
    }

    public void visit(RefImageNode node) {

    }

    public void visit(RefLinkNode node) {
        String text = printChildrenToString(node);
        String key = node.referenceKey != null ? printChildrenToString(node.referenceKey) : text;
        ReferenceNode refNode = references.get(key);
        // TODO: handle refNode
         try {
            markdownFilter.writeTranslate(text, true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(SimpleNode node) {
        switch(node.getType()) {
            case Apostrophe:
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
         try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(StrikeNode node) {
        if (node.isClosed())
            return;
        try {
            if (node.isStrong()) {
                markdownFilter.writeTranslate("**", false);
                visitChildren(node);
                markdownFilter.writeTranslate("**", false);
            } else {
                markdownFilter.writeTranslate("__", false);
                visitChildren(node);
                markdownFilter.writeTranslate("__", false);
            }
        } catch (IOException ioe) {
            // FIXME
        }
          System.out.print("StrikeNode: ");
         System.out.println(node.getChars());
    }

    public void visit(StrongEmphSuperNode node) {
        if (node.isClosed())
            return;

        try {
            if (node.isStrong()) {
                markdownFilter.writeTranslate("**", false);
                visitChildren(node);
                markdownFilter.writeTranslate("**", false);
            } else {
                markdownFilter.writeTranslate("__", false);
                visitChildren(node);
                markdownFilter.writeTranslate("__", false);
            }
        } catch (IOException ioe) {
            // FIXME
        }
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
        try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(TextNode node) {
        try {
            markdownFilter.writeTranslate(node.getText(), true);
        } catch (IOException ioe) {
            // FIXME
        }
    }

    public void visit(SuperNode node) {
        visitChildren(node);
    }

    // helpers
    protected void visitChildren(SuperNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    protected String printChildrenToString(SuperNode node) {
        StringBuilder priorPrinter = printer;
        printer = new StringBuilder();
        visitChildren(node);
        String result = printer.toString();
        printer = priorPrinter;
        return result;
    }
}
