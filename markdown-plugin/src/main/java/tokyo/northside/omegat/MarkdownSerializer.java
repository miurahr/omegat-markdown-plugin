package tokyo.northside.omegat;

import org.omegat.filters2.IFilter;
import org.parboiled.common.StringUtils;
import org.pegdown.Printer;
import org.pegdown.ast.*;

import java.util.HashMap;
import java.util.Map;

import static org.parboiled.common.Preconditions.checkArgNotNull;


/**
 * Created by miurahr on 16/08/22.
 */
public class MarkdownSerializer implements Visitor {
    protected final Map<String, ReferenceNode> references = new HashMap<String, ReferenceNode>();
    protected final Map<String, String> abbreviations = new HashMap<String, String>();
    protected TableNode currentTableNode;
    protected int currentTableColumn;
    protected boolean inTableHeader;

    protected IFilter omegatFilter;

    public MarkdownSerializer(MarkdownFilter omtFilter){
        omegatFilter = omtFilter;
    }

    public void processNode(RootNode astRoot) {
        checkArgNotNull(astRoot, "astRoot");
        astRoot.accept(this);
    }

    public void visit(RootNode node) {
    }

    public void visit(Node node) {

    }

    public void visit(AbbreviationNode node) {
    }

    public void visit(AnchorLinkNode node) {
    }

    public void visit(AutoLinkNode node) {
    }

    public void visit(BlockQuoteNode node) {}

    public void visit(BulletListNode node) {}

    public void visit(CodeNode node) {}

    public void visit(DefinitionListNode node) {}

    public void visit(DefinitionNode node) {}

    public void visit(DefinitionTermNode node) {}

    public void visit(ExpImageNode node) {}

    public void visit(ExpLinkNode node) {}

    public void visit(HeaderNode node) {}

    public void visit(HtmlBlockNode node) {
    }

    public void visit(InlineHtmlNode node) {}

    public void visit(ListItemNode node) {}

    public void visit(MailLinkNode node) {}

    public void visit(OrderedListNode node) {}

    public void visit(ParaNode node) {}

    public void visit(QuotedNode node) {}

    public void visit(ReferenceNode node) {

    }

    public void visit(RefImageNode node) {

    }

    public void visit(RefLinkNode node) {

    }

    public void visit(SimpleNode node) {

    }
    public void visit(SpecialTextNode node) {

    }

    public void visit(StrikeNode node) {

    }
    public void visit(StrongEmphSuperNode node) {

    }

    public void visit(TableBodyNode node) {

    }

    public void visit(TableCaptionNode node) {}

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

    }
    public void visit(SuperNode node) {

    }
}
