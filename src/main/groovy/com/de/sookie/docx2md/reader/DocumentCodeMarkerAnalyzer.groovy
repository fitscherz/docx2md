package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text

class DocumentCodeMarkerAnalyzer {

    void analyze(List<Block> blocks) {
        List<Block> result = []

        boolean inCode = false
        String language = null
        List<String> codeLines = []

        blocks.each { block ->

            if (!(block instanceof Paragraph)) {
                result << block
                return
            }

            String text = paragraphText(block)

            if (!inCode) {

                def matcher = text =~ /(?s)(.*?)\[CODE\\?:([^\]]+)\]/

                if (matcher.find()) {

                    String before = matcher.group(1).trim()

                    if (before) {
                        Paragraph paragraph = copyParagraph(block)
                        paragraph.add(new Text(value: before))
                        result << paragraph
                    }

                    inCode = true
                    language = matcher.group(2)
                    codeLines.clear()

                    return
                }

                result << block
                return
            }


            if (text.trim() == "[/CODE]") {

                CodeBlock codeBlock = new CodeBlock()
                codeBlock.language = language
                codeBlock.listId = null
                codeBlock.listLevel = 0

                if (!codeLines.isEmpty()) {
                    codeBlock.add(new Text(
                            value: codeLines.join("\n"),
                            code: true
                    ))
                }

                result << codeBlock

                inCode = false
                language = null
                codeLines.clear()

                return
            }

            codeLines << text
        }

        blocks.clear()
        blocks.addAll(result)
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


    private Paragraph copyParagraph(Paragraph source) {
        Paragraph paragraph = new Paragraph()
        paragraph.type = source.type
        paragraph.listId = source.listId
        paragraph.listLevel = source.listLevel
        return paragraph
    }
}