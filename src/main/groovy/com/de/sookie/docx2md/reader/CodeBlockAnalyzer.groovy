package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.Inline
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

class CodeBlockAnalyzer {

    List<Block> analyze(Block block) {
        if (!(block instanceof Paragraph)) {
            return [block]
        }

        List<Block> result = []
        List<Inline> buffer = []
        List<Inline> codeBuffer = []

        block.inlines.each { inline ->
            if (inline instanceof Text && inline.code) {
                codeBuffer << inline
            } else if (inline instanceof LineBreak && !codeBuffer.isEmpty()) {
                codeBuffer << inline
            } else {
                if (isRealCodeBlock(codeBuffer)) {
                    if (!buffer.isEmpty()) {
                        result << createParagraph(block, buffer)
                        buffer = []
                    }

                    result << createCodeBlock(block, codeBuffer)
                    codeBuffer = []
                } else {
                    buffer.addAll(codeBuffer)
                    codeBuffer = []
                }

                buffer << inline
            }
        }

        if (isRealCodeBlock(codeBuffer)) {
            if (!buffer.isEmpty()) {
                result << createParagraph(block, buffer)
            }

            result << createCodeBlock(block, codeBuffer)
        } else {
            buffer.addAll(codeBuffer)

            if (!buffer.isEmpty()) {
                result << createParagraph(block, buffer)
            }
        }

        return result
    }

    private boolean isRealCodeBlock(List<Inline> inlines) {
        long lineBreaks = inlines.count { it instanceof LineBreak }

        String text = inlines
                .findAll { it instanceof Text }
                .collect { it.value }
                .join("")

        return lineBreaks > 1 && text.contains("\n")
    }

    private Paragraph createParagraph(Paragraph source, List<Inline> inlines) {
        Paragraph paragraph = new Paragraph()
        paragraph.type = source.type
        paragraph.listLevel = source.listLevel
        paragraph.listId = source.listId
        paragraph.inlines.addAll(inlines)
        return paragraph
    }

    private CodeBlock createCodeBlock(Paragraph source, List<Inline> inlines) {
        CodeBlock block = new CodeBlock()

        block.listId = source.listId
        block.listLevel = source.listLevel
        block.type = source.type

        block.inlines.addAll(inlines)

        return block
    }
}