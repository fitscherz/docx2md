package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

class CodeMarkerAnalyzer {

    List<Block> analyze(Block block) {
        if (!(block instanceof Paragraph)) {
            return [block]
        }

        String content = block.inlines.collect { inline ->
            if (inline instanceof Text) {
                return inline.value
            }

            if (inline instanceof LineBreak) {
                return "\n"
            }

            return ""
        }.join("")

        def matcher = content =~ /(?s)\[CODE:([^\]]+)\](.*?)\[\/CODE\]/

        if (!matcher.find()) {
            return [block]
        }

        List<Block> result = []

        String before = content.substring(0, matcher.start()).trim()
        String language = matcher.group(1)
        String code = matcher.group(2).trim()

        if (before) {
            Paragraph paragraph = copyParagraph(block)

            before.split("\n").eachWithIndex { line, index ->
                if (index > 0) {
                    paragraph.add(new LineBreak())
                }

                paragraph.add(new Text(value: line))
            }

            result << paragraph
        }

        CodeBlock codeBlock = new CodeBlock()
        codeBlock.language = language
        codeBlock.listId = block.listId
        codeBlock.listLevel = block.listLevel
        codeBlock.type = block.type

        codeBlock.add(new Text(
                value: code,
                code: true
        ))

        result << codeBlock

        return result
    }

    private Paragraph copyParagraph(Paragraph source) {
        Paragraph paragraph = new Paragraph()
        paragraph.type = source.type
        paragraph.listId = source.listId
        paragraph.listLevel = source.listLevel
        return paragraph
    }
}