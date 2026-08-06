package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.Text
import org.docx4j.XmlUtils
import org.docx4j.wml.ContentAccessor
import org.docx4j.wml.P

class ParagraphReader implements BlockReader<P> {

    private final InlineReaderService inlineReaderService
    private final ParagraphAnalyzer paragraphAnalyzer
    private final CodeMarkerAnalyzer codeMarkerAnalyzer
    private final CodeBlockAnalyzer codeBlockAnalyzer
    private final InlineNormalizer inlineNormalizer

    ParagraphReader(
            InlineReaderService inlineReaderService,
            ParagraphAnalyzer paragraphAnalyzer,
            CodeMarkerAnalyzer codeMarkerAnalyzer,
            CodeBlockAnalyzer codeBlockAnalyzer,
            InlineNormalizer inlineNormalizer
    ) {
        this.inlineReaderService = inlineReaderService
        this.paragraphAnalyzer = paragraphAnalyzer
        this.codeMarkerAnalyzer = codeMarkerAnalyzer
        this.codeBlockAnalyzer = codeBlockAnalyzer
        this.inlineNormalizer = inlineNormalizer
    }

    @Override
    Class<P> supports() {
        return P
    }

    @Override
    List<Block> read(P p) {
        InlineBlock block = paragraphAnalyzer.createBlock(p)

        readContent(p, block)

        List normalized = inlineNormalizer.normalize(block.inlines)

        block.inlines.clear()
        block.inlines.addAll(normalized)

        List<Block> result = []

        println "=== BEFORE CODEMARKER CONTENT ==="
        block.inlines.each {
            println "${it.class.simpleName}: '${it instanceof Text ? it.value.replace("\n","\\n") : it}'"
        }
        println "================================="

        List<Block> marked = codeMarkerAnalyzer.analyze(block)

        marked.each { current ->
            if (current instanceof Paragraph) {
                List<Block> analyzed = codeBlockAnalyzer.analyze(current)

                if (current instanceof Paragraph && current.listId && analyzed.size() > 1) {
                    current.inlines.clear()
                    analyzed.findAll { it instanceof CodeBlock }
                            .each { current.add(it) }

                    result << current
                } else {
                    result.addAll(analyzed)
                }
            } else {
                result << current
            }
        }

        return result
    }

    private void readContent(ContentAccessor parent, InlineBlock block) {
        parent.content.each { item ->
            def value = XmlUtils.unwrap(item)

            if (value instanceof org.docx4j.wml.R) {
                inlineReaderService.read(value, block)
            } else if (value instanceof ContentAccessor) {
                readContent(value, block)
            } else {
                inlineReaderService.read(value, block)
            }
        }
    }
}