package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

class CustomCodeAnalyzer {

    List<Block> analyze(List<Block> blocks) {
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

        return blocks
    }

    private List<Block> analyzeParagraph(Paragraph paragraph) {
        String content = paragraphText(paragraph)

        def matcher = content =~ /(?s)\[CODE\\?(?::([^\]]+))?\](.*?)\[\/CODE\]/

        if (!matcher.find()) {
            return [paragraph]
        }

        List<Block> result = []

        String before = content.substring(0, matcher.start()).trim()
        String language = matcher.group(1) ?: "text"
        String code = matcher.group(2).trim()
        String after = content.substring(matcher.end()).trim()

        if (before) {
            result << createParagraph(paragraph, before)
        }

        CodeBlock codeBlock = new CodeBlock()
        codeBlock.language = language
        codeBlock.listId = paragraph.listId
        codeBlock.listLevel = paragraph.listLevel
        codeBlock.type = paragraph.type

        codeBlock.add(new Text(
                value: code,
                code: true
        ))

        result << codeBlock

        if (after) {
            result << createParagraph(paragraph, after)
        }

        return result
    }

    private Paragraph createParagraph(Paragraph source, String value) {
        Paragraph paragraph = new Paragraph()

        paragraph.type = source.type
        paragraph.listId = source.listId
        paragraph.listLevel = source.listLevel

        paragraph.add(new Text(value: value))

        return paragraph
    }

    private String paragraphText(Paragraph paragraph) {
        paragraph.inlines.collect { inline ->
            if (inline instanceof Text) {
                return inline.value
            }

            if (inline instanceof LineBreak) {
                return "\n"
            }

            return ""
        }.join("")
    }
}