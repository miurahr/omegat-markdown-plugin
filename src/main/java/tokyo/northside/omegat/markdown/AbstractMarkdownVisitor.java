package tokyo.northside.omegat.markdown;

import com.vladsch.flexmark.ast.*;

/**
 * @author Hiroshi Miura
 */
public abstract class AbstractMarkdownVisitor extends NodeVisitorBase implements Visitor {

    public AbstractMarkdownVisitor() {
        super();
    }

    public void visit(final Node node) {
        if (node instanceof AutoLink) {
            visitAutoLink(node);
        } else if (node instanceof Content) {
            visitContent(node);
        } else if (node instanceof BlockQuote) {
            visitBlockQuote(node);
        } else if (node instanceof Emphasis) {
            visitEmphasis(node);
        } else if (node instanceof Heading) {
            visitHeader(node);
        } else if (node instanceof Link) {
            visitLink(node);
        } else if (node instanceof HtmlBlock) {
            visitHtmlBlock(node);
        } else if (node instanceof HtmlInline) {
            visitHtmlInline(node);
        } else if (node instanceof ListItem) {
            visitListItem(node);
        } else if (node instanceof MailLink) {
            visitMailLink(node);
        } else if (node instanceof Code) {
            visitCode(node);
        } else if (node instanceof Paragraph) {
            visitPara(node);
        } else if (node instanceof RefNode) {
            visitRefNode(node);
        } else if (node instanceof Reference) {
            visitReference(node);
        } else if (node instanceof StrongEmphasis) {
            visitStrongEmphasis(node);
        } else if (node instanceof Text) {
            visitText(node);
        }
    }

    abstract void visitAutoLink(final Node node);
    abstract void visitBlockQuote(final Node node);
    abstract void visitContent(final Node node);
    abstract void visitCode(final Node node);
    abstract void visitEmphasis(final Node node);
    abstract void visitHeader(final Node node);
    abstract void visitHtmlBlock(final Node node);
    abstract void visitHtmlInline(final Node node);
    abstract void visitLink(final Node node);
    abstract void visitListItem(final Node node);
    abstract void visitMailLink(final Node node);
    abstract void visitPara(final Node node);
    abstract void visitReference(final Node node);
    abstract void visitRefNode(final Node node);
    abstract void visitStrongEmphasis(final Node node);
    abstract void visitText(final Node node);

}

