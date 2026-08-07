package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.Inline
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

/**
 * Automatische Code-Erkennung über Font
 */
class CodeBlockAnalyzer {

    List<Block> analyze(Block block) {
        if (!(block instanceof Paragraph)) {
            return [block]
        }

        List<Block> singleLine = extractSingleLineCode(block)

        if (!singleLine.isEmpty()) {
            return singleLine
        }

        List trailingCode = extractCodeBlock(block)

        if (!trailingCode.isEmpty()) {
            List<Block> result = []

            List<Inline> normal = block.inlines[0..<block.inlines.size() - trailingCode.size()]

            if (!normal.isEmpty()) {
                result << createParagraph(block, normal)
            }

            result << createCodeBlock(block, trailingCode)

            return result
        }

        if (isCodeParagraph(block)) {
            return [createCodeBlock(block, block.inlines)]
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

    private boolean isCodeParagraph(Paragraph paragraph) {
        def texts = paragraph.inlines.findAll {
            it instanceof Text
        }

        return !texts.isEmpty() &&
                texts.every { it.code }
    }

    private List<Inline> extractCodeBlock(Paragraph paragraph) {
        List<Inline> result = []
        boolean foundLineBreak = false
        boolean started = false
        int codeLines = 0

        paragraph.inlines.each { inline ->
            if (inline instanceof LineBreak) {
                if (started) {
                    result << inline
                }

                foundLineBreak = true
                return
            }

            if (inline instanceof Text && inline.code) {
                if (foundLineBreak && !started) {
                    started = true
                }

                if (started) {
                    result << inline
                    codeLines++
                }

                return
            }

            if (!started) {
                foundLineBreak = false
            }
        }

        if (codeLines < 2) {
            return []
        }

        return result
    }

    private List<Block> extractSingleLineCode(Paragraph paragraph) {
        int lineBreakIndex = paragraph.inlines.findIndexOf {
            it instanceof LineBreak
        }

        if (lineBreakIndex < 0) {
            return []
        }

        List<Inline> before = paragraph.inlines[0..<lineBreakIndex]
        List<Inline> after = paragraph.inlines[(lineBreakIndex + 1)..<paragraph.inlines.size()]

        if (after.size() != 1) {
            return []
        }

        Inline inline = after[0]

        if (!(inline instanceof Text) || !inline.code) {
            return []
        }

        List<Block> result = []

        Paragraph text = createParagraph(paragraph, before)

        if (!text.inlines.isEmpty()) {
            result << text
        }

        result << createCodeBlock(paragraph, after)

        return result
    }
}