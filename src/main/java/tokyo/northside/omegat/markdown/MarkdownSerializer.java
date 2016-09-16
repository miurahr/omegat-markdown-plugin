/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2016 Hiroshi Miura
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package tokyo.northside.omegat.markdown;

import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.AutoLinkNode;
import org.pegdown.ast.BlockQuoteNode;
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.HtmlBlockNode;
import org.pegdown.ast.InlineHtmlNode;
import org.pegdown.ast.ListItemNode;
import org.pegdown.ast.MailLinkNode;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.QuotedNode;
import org.pegdown.ast.RefLinkNode;
import org.pegdown.ast.ReferenceNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SimpleNode;
import org.pegdown.ast.SpecialTextNode;
import org.pegdown.ast.StrikeNode;
import org.pegdown.ast.StrongEmphSuperNode;
import org.pegdown.ast.TableCellNode;
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
     * Accept root node.
     *
     * @param node root node.
     */
    public void visit(final RootNode node) {
        visitChildren(node);
        //node.getReferences().stream().forEachOrdered(this::visitChildren);
        //node.getAbbreviations().stream().forEachOrdered(this::visitChildren);
    }

    /**
     * Accept text node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    @Override
    public void visit(final TextNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Anchor link node.
     * <p>
     *     put literals to translation entry.
     *     Header is recognized as anchor link.
     *
     * @param node link node.
     */
    @Override
    public void visit(final AnchorLinkNode node) {
        handler.startPara(node.getStartIndex(), MarkdownState.NORMAL);
        handler.putEntry(node);
        handler.endPara();
    }

    /**
     * Accept Auto link node.
     * <p>
     *     put literals to translation entry.
     * @param node link node.
     */
    @Override
    public void visit(final AutoLinkNode node) {
        String startmark = handler.getChars(node.getStartIndex(), 1);
        String endmark = handler.getChars(node.getEndIndex() - 1, 1);
        handler.putMark(startmark);
        handler.putEntry(node);
        handler.putMark(endmark);

    }

    /**
     * Accept inline html node.
     * <p>
     *     put literals to translation entry.
     * @param node html node.
     */
    @Override
    public void visit(final InlineHtmlNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept mail link node.
     * <p>
     *     put literals to translation entry.
     * @param node mail link.
     */
    @Override
    public void visit(final MailLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept special text node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    @Override
    public void visit(final SpecialTextNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept HTML Block node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    @Override
    public void visit(final HtmlBlockNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept verbatim node.
     * <p>
     *     put literals to translation entry.
     * @param node verbatim node.
     */
    @Override
    public void visit(final VerbatimNode node) {
        if (handler.getChars(node.getStartIndex(), 3).equals("```")) {
            handler.startPara(node.getStartIndex(), MarkdownState.FENCED);
        } else {
            handler.startPara(MarkdownState.VERBATIM);
        }
        handler.putEntry(node);
        handler.endPara();
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    @Override
    public void visit(final ParaNode node) {
        handler.startPara(node.getStartIndex(), MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Start list item paragraph.
     * @param node list item node.
     */
    @Override
    public void visit(final ListItemNode node) {
        handler.startPara(node.getStartIndex(), MarkdownState.NORMAL);
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
    @Override
    public void visit(final StrongEmphSuperNode node) {
        handler.startPara(MarkdownState.NORMAL);
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
    @Override
    public void visit(final StrikeNode node) {
        handler.startPara(MarkdownState.NORMAL);
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
    @Override
    public void visit(final ExpLinkNode node) {
        handler.startPara(MarkdownState.NORMAL);
        handler.putMark("[", node.getStartIndex() + 1);
        visitChildren(node);
        handler.putMark("](");
        handler.putEntry(node.url);
        handler.putMark(")", node.getEndIndex());
        handler.endPara();
    }

    /**
     * Accept header node.
     * <p>
     *     Treat header as single paragraph.
     * </p>
     * @param node header node.
     */
    @Override
    public void visit(final HeaderNode node) {
        handler.startPara(node.getStartIndex(), MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Code node.
     *
     * @param node
     */
    @Override
    public void visit(final CodeNode node) {
        int textLen = node.getText().length();
        int nodeLen = node.getEndIndex() - node.getStartIndex();
        int markLen = (nodeLen - textLen) / 2; // FIXME: odd?
        String startmark = handler.getChars(node.getStartIndex(), markLen);
        String endmark = handler.getChars(node.getEndIndex() - markLen, markLen);
        handler.startPara(MarkdownState.NORMAL);
        handler.putMark(startmark);
        handler.putEntry(node);
        handler.putMark(endmark);
        handler.endPara();
    }

    /**
     * Accept Table cess node.
     * @param node table node.
     */
    @Override
    public void visit(final TableCellNode node) {
        handler.startPara(MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Block quote node.
     * @param node block quote.
     */
    public void visit(final BlockQuoteNode node) {
        handler.startPara(MarkdownState.BLOCKQUOTE);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept simple node.
     * @param node
     */
    @Override
    public void visit(final SimpleNode node) {
        switch (node.getType()) {
            case Apostrophe:
                handler.putMark("'", node.getEndIndex());
                break;
            case Ellipsis:
                handler.putMark("\u2026", node.getEndIndex());
                break;
            case Linebreak:
                handler.putEntry("\n", node.getEndIndex());
                break;
            case Emdash:
                break;
            case Endash:
                break;
            case Nbsp:
                break;
            default:
                break;
        }
    }

    public void visit(final ReferenceNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getUrl()).append(" \"").append(node.getTitle()).append("\"");
        handler.putEntry(sb.toString(), node.getStartIndex());
    }

    public void visit(final RefLinkNode node) {
        handler.putMark("[");
        visitChildren(node);
        handler.putMark("]");
        handler.putMark("[");
        visitChildren(node.referenceKey);
        handler.putMark("]");
    }

    public void visit(final QuotedNode node) {
        String startmark;
        String endmark;
        switch (node.getType()) {
            case Single:
            case Double:
                startmark = handler.getChars(node.getStartIndex(), 1);
                endmark = handler.getChars(node.getEndIndex() - 1, 1);
                break;
            case DoubleAngle:
                startmark = handler.getChars(node.getStartIndex(), 2);
                endmark = handler.getChars(node.getEndIndex() - 1, 2);
                break;
            default:
                startmark = "";
                endmark = "";
                break;
        }
        handler.putMark(startmark);
        visitChildren(node);
        handler.putMark(endmark);
    }

}
