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
    private final InlineNormalizer inlineNormalizer

    ParagraphReader(
            InlineReaderService inlineReaderService,
            ParagraphAnalyzer paragraphAnalyzer,
            InlineNormalizer inlineNormalizer
    ) {
        this.inlineReaderService = inlineReaderService
        this.paragraphAnalyzer = paragraphAnalyzer
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

        return [block]
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