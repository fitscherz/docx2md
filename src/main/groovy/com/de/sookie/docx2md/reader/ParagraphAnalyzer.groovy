package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Heading
import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.ParagraphStyle
import com.de.sookie.docx2md.model.ParagraphType
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Numbering
import org.docx4j.wml.P

class ParagraphAnalyzer {

    private StyleResolver styleResolver
    private Numbering numbering
    private WordprocessingMLPackage word

    ParagraphAnalyzer(StyleResolver styleResolver) {
        this.styleResolver = styleResolver
    }

    void setStyleResolver(StyleResolver styleResolver) {
        this.styleResolver = styleResolver
    }

    void setNumbering(Numbering numbering) {
        this.numbering = numbering
    }

    void setWord(WordprocessingMLPackage word) {
        this.word = word
    }

    InlineBlock createBlock(P paragraph) {
        ParagraphStyle style = resolveStyle(paragraph)

        switch (style) {
            case ParagraphStyle.HEADING_1:
                return new Heading(level: 1)
            case ParagraphStyle.HEADING_2:
                return new Heading(level: 2)
            case ParagraphStyle.HEADING_3:
                return new Heading(level: 3)
            default:
                Paragraph block = new Paragraph()
                block.type = resolveType(paragraph)
                block.listLevel = resolveListLevel(paragraph)
                block.listId = resolveListId(paragraph)
                return block
        }
    }

    private ParagraphType resolveType(P paragraph) {
        if (!paragraph.pPr?.numPr) {
            return ParagraphType.NORMAL
        }

        if (isNumbered(paragraph)) {
            return ParagraphType.NUMBERED
        }

        return ParagraphType.BULLET
    }

    private boolean isNumbered(P paragraph) {
        if (!numbering || !paragraph.pPr?.numPr?.numId) {
            return false
        }

        def numId = paragraph.pPr.numPr.numId.val.toString()

        def num = numbering.num.find {
            it.numId.toString() == numId
        }

        if (!num) {
            return false
        }

        def abstractId = num.abstractNumId.val

        def abstractNum = numbering.abstractNum.find {
            it.abstractNumId.toString() == abstractId.toString()
        }

        if (!abstractNum) {
            return false
        }

        def lvl = abstractNum.lvl.find {
            it.ilvl.toString() == (paragraph.pPr.numPr.ilvl?.val ?: 0).toString()
        }

        if (!lvl) {
            return false
        }

        return lvl.numFmt?.val?.toString() != "bullet"
    }

    private ParagraphStyle resolveStyle(P paragraph) {
        styleResolver?.resolve(readStyle(paragraph)) ?: ParagraphStyle.NORMAL
    }

    private String readStyle(P paragraph) {
        paragraph.pPr?.pStyle?.val?.toString()
    }

    private int resolveListLevel(P paragraph) {
        paragraph.pPr?.numPr?.ilvl?.val?.toInteger() ?: 0
    }

    private String resolveListId(P paragraph) {
        paragraph.pPr?.numPr?.numId?.val?.toString()
    }
}