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
import org.pegdown.ast.CodeNode;
import org.pegdown.ast.ExpLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.HtmlBlockNode;
import org.pegdown.ast.InlineHtmlNode;
import org.pegdown.ast.ListItemNode;
import org.pegdown.ast.MailLinkNode;
import org.pegdown.ast.ParaNode;
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
     * @param node link node.
     */
    @Override
    public void visit(final AnchorLinkNode node) {
        handler.putEntry(node);
    }

    /**
     * Accept Auto link node.
     * <p>
     *     put literals to translation entry.
     * @param node link node.
     */
    @Override
    public void visit(final AutoLinkNode node) {
        handler.putEntry(node);
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
        handler.putMarkOut("```\n");
        handler.startPara();
        handler.putEntry(node);
        handler.endPara();
        handler.putMarkOut("```");
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    @Override
    public void visit(final ParaNode node) {
        handler.startPara();
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Start list item paragraph.
     * @param node list item node.
     */
    @Override
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
    @Override
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
    @Override
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
    @Override
    public void visit(final ExpLinkNode node) {
        handler.startPara();
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
        handler.startPara();
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
        if (nodeLen - textLen == 2) { // inline code
            handler.startPara();
            handler.putMark("`");
            handler.putEntry(node);
            handler.putMark("`");
            handler.endPara();
        }
    }


    /**
     * Accept Table cess node.
     * @param node
     */
    @Override
    public void visit(final TableCellNode node) {
        handler.startPara();
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept simple node.
     * @param node
     */
    @Override
    public void visit(final SimpleNode node) {
        switch(node.getType()) {
            case Apostrophe:
                handler.putMark("'", node.getEndIndex());
                break;
            case Ellipsis:
                handler.putMark("\u2026", node.getEndIndex());
                break;
        }
    }


}
