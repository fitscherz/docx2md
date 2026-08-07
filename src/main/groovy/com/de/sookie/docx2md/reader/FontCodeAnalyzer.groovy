package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.Inline
import com.de.sookie.docx2md.model.inline.InlineCode
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

class FontCodeAnalyzer {

    void analyze(List<Block> blocks) {
        List<Block> result = []

        blocks.each { block ->
            if (!(block instanceof Paragraph)) {
                result << block
                return
            }

            result.addAll(analyzeParagraph(block))
        }

        blocks.clear()
        blocks.addAll(result)
    }

    private List<Block> analyzeParagraph(Paragraph paragraph) {
        List<Block> result = []
        List<Inline> normal = []
        List<Inline> code = []

        boolean inCodeSequence = false

        paragraph.inlines.eachWithIndex { inline, index ->

            if (inline instanceof InlineCode || (inline instanceof Text && inline.code)) {

                boolean hasTextBefore = index > 0 &&
                        paragraph.inlines[0..<index].any {
                            it instanceof Text && !it.code
                        }

                boolean hasTextAfter = index + 1 < paragraph.inlines.size() &&
                        paragraph.inlines[(index + 1)..<paragraph.inlines.size()].any {
                            it instanceof Text && !it.code
                        }

                if (!hasTextBefore && !hasTextAfter) {
                    inCodeSequence = true
                    code << inline
                    return
                }

                if (inCodeSequence) {
                    code << inline
                    return
                }

                normal << inline
                return
            }

            if (inline instanceof LineBreak && inCodeSequence) {
                code << inline
                return
            }

            if (inCodeSequence) {
                result << createCodeBlock(paragraph, code)
                code.clear()
                inCodeSequence = false
            }

            normal << inline
        }

        if (!code.isEmpty()) {
            result << createCodeBlock(paragraph, code)
        }

        if (!normal.isEmpty()) {
            Paragraph copy = copyParagraph(paragraph)
            copy.inlines.addAll(normal)
            result.add(0, copy)
        }

        return result
    }

    private boolean isCodeInline(Inline inline) {
        if (inline instanceof Text) {
            return inline.code
        }

        return inline instanceof InlineCode
    }

    private CodeBlock createCodeBlock(Paragraph source, List<Inline> values) {
        CodeBlock block = new CodeBlock(
                language: "sql",
                listId: source.listId,
                listLevel: source.listLevel,
                type: source.type
        )

        String value = values.collect { inline ->
            if (inline instanceof Text) {
                return inline.value
            }

            if (inline instanceof InlineCode) {
                return inline.inlines.collect { child ->
                    child instanceof Text ? child.value : ""
                }.join("")
            }

            if (inline instanceof LineBreak) {
                return "\n"
            }

            return ""
        }.join("")

        block.add(new Text(
                value: value,
                code: true
        ))

        return block
    }

    private Paragraph copyParagraph(Paragraph source) {
        Paragraph p = new Paragraph()
        p.type = source.type
        p.listId = source.listId
        p.listLevel = source.listLevel
        return p
    }
}