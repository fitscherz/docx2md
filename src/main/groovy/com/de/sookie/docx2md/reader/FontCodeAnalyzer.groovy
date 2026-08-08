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
        List<Inline> normalized = createInlineCode(paragraph.inlines)
        int codeStart = findMultiLineCodeStart(normalized)
        if (codeStart < 0) {
            paragraph.inlines.clear()
            paragraph.inlines.addAll(normalized)
            return [paragraph]
        }

        List<Inline> paragraphInlines = normalized.subList(0, codeStart)
        List<Inline> codeInlines = normalized.subList(codeStart, normalized.size())

        paragraph.inlines.clear()
        paragraph.inlines.addAll(paragraphInlines)

        CodeBlock codeBlock = new CodeBlock(
                listId: paragraph.listId,
                listLevel: paragraph.listLevel,
                type: paragraph.type
        )
        codeBlock.inlines.addAll(codeInlines)

        return [paragraph, codeBlock]
    }

    private int findMultiLineCodeStart(List<Inline> inlines) {
        for (int i = 0; i < inlines.size(); i++) {
            if (!(inlines[i] instanceof LineBreak)) {
                continue
            }

            List<Inline> tail = inlines.subList(i + 1, inlines.size())
            if (isMultiLineCode(tail)) {
                return i + 1
            }
        }

        return -1
    }

    private boolean isMultiLineCode(List<Inline> inlines) {
        int codeLines = 0
        boolean hasCode = false

        inlines.each { inline ->
            if (inline instanceof LineBreak) {
                if (hasCode) {
                    codeLines++
                    hasCode = false
                }
                return
            }

            if (inline instanceof InlineCode) {
                hasCode = true
                return
            }

            return
        }

        if (hasCode) {
            codeLines++
        }

        return codeLines >= 2 && inlines.every {
            it instanceof InlineCode || it instanceof LineBreak
        }
    }

    private List<Inline> createInlineCode(List<Inline> source) {
        List<Inline> result = []
        InlineCode codeBuffer = null

        source.each { inline ->
            if (isCodeInline(inline)) {
                if (!codeBuffer) {
                    codeBuffer = new InlineCode()
                }
                codeBuffer.add(copyText(inline))
                return
            }

            if (codeBuffer) {
                result << codeBuffer
                codeBuffer = null
            }

            result << inline
        }

        if (codeBuffer) {
            result << codeBuffer
        }

        return result
    }

    private boolean isCodeInline(Inline inline) {
        if (!(inline instanceof Text)) {
            return false
        }

        return isCodeFont(inline.fontFamily)
    }

    private Text copyText(Text source) {
        new Text(
                value: source.value,
                fontFamily: source.fontFamily,
                fontSize: source.fontSize,
                bold: source.bold,
                italic: source.italic,
                underline: source.underline,
                strike: source.strike
        )
    }

    private boolean isCodeFont(String fontFamily) {
        if (!fontFamily) {
            return false
        }

        [
                "Consolas",
                "Courier New",
                "Courier",
                "Lucida Console",
                "Monaco",
                "DejaVu Sans Mono",
                "Fira Code",
                "JetBrains Mono"
        ].any {
            it.equalsIgnoreCase(fontFamily)
        }
    }
}