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

import com.vladsch.flexmark.ast.*;
import org.omegat.filters2.TranslationException;


/**
 * Markdown parser and serializer class.
 * @author Hiroshi Miura
 */
class MarkdownVisitor extends AbstractMarkdownVisitor implements Visitor {
    private EntryHandler handler;

    MarkdownVisitor(final EntryHandler entryHandler) {
        handler = entryHandler;
    }

    /**
     * Accept text node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    @Override
    public void visitContent(final Node node) {
        handler.putEntry((Node) node);
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
    public void visitLink(final Node node) {
        handler.startPara(node.getStartOffset(), MarkdownState.NORMAL);
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
    public void visitAutoLink(final Node node) {
        String startmark = handler.getChars(node.getStartOffset(), 1);
        String endmark = handler.getChars(node.getEndOffset() - 1, 1);
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
    public void visitHtmlInline(final Node node) {
        handler.putEntry(node);
    }

    /**
     * Accept mail link node.
     * <p>
     *     put literals to translation entry.
     * @param node mail link.
     */
    @Override
    public void visitMailLink(final Node node) {
        handler.putEntry(node);
    }

    /**
     * Accept HTML Block node.
     * <p>
     *     put literals to translation entry.
     * @param node text node.
     */
    @Override
    public void visitHtmlBlock(final Node node) {
        handler.putEntry(node);
    }

    /**
     * Start paragraph, also to start entry.
     * @param node paragraph node.
     */
    @Override
    public void visitPara(final Node node) {
        handler.startPara(node.getStartOffset(), MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Start list item paragraph.
     * @param node list item node.
     */
    @Override
    public void visitListItem(final Node node) {
        handler.startPara(node.getStartOffset(), MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Emphasis node.
     * <p>
     *     it is inline component and replace token with OmegaT tag.
     * </p>
     * @param node
     */
    @Override
    public void visitEmphasis(final Node node) {
        handler.startPara(MarkdownState.NORMAL);
        handler.putMark(node.getChars().toVisibleWhitespaceString(), node.getStartOffset() + node.getChars().length());
        visitChildren(node);
        handler.putMark(node.getChars().toVisibleWhitespaceString(), node.getEndOffset());
        handler.endPara();
    }


    /**
     * Accept Strong node.
     * <p>
     *     it is inline component and replace token with OmegaT tag.
     * </p>
     * @param node
     */
    @Override
    public void visitStrongEmphasis(final Node node) {
        handler.startPara(MarkdownState.NORMAL);
        handler.putMark(node.getChars().toVisibleWhitespaceString(), node.getStartOffset() + node.getChars().length());
        visitChildren(node);
        handler.putMark(node.getChars().toVisibleWhitespaceString(), node.getEndOffset());
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
    public void visitHeader(final Node node) {
        handler.startPara(node.getStartOffset(), MarkdownState.NORMAL);
        visitChildren(node);
        handler.endPara();
    }

    /**
     * Accept Code node.
     *
     * @param node
     */
    @Override
    public void visitCode(final Node node) {
        int textLen = node.getTextLength();
        int nodeLen = node.getEndOffset() - node.getStartOffset();
        int markLen = (nodeLen - textLen) / 2; // FIXME: odd?
        String startmark = handler.getChars(node.getStartOffset(), markLen);
        String endmark = handler.getChars(node.getEndOffset() - markLen, markLen);
        handler.startPara(MarkdownState.NORMAL);
        handler.putMark(startmark);
        handler.putEntry(node);
        handler.putMark(endmark);
        handler.endPara();
    }

    public void visitRefNode(final Node node) {
        RefNode link = (RefNode) node;
        StringBuilder sb = new StringBuilder();
        sb.append(link.getReference()).append(" \"").append(link.getText().toVisibleWhitespaceString()).append("\"");
        handler.putEntry(sb.toString(), node.getStartOffset());
    }

    public void visitReference(final Node node) {
        Reference ref = (Reference) node;
        handler.putMark("[");
        visitChildren(node);
        handler.putMark("]");
    }

    public void visitBlockQuote(final Node node) {
        BlockQuote quote = (BlockQuote) node;
        String startmark =  quote.getOpeningMarker().toVisibleWhitespaceString();
        handler.putMark(startmark);
        visitChildren(node);
        handler.putMark(startmark);
    }

    public void visitText(final Node node) {
        handler.putEntry(node.getChars().toString());
    }
}
