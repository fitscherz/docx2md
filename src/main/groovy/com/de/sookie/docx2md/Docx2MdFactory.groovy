package com.de.sookie.docx2md

import com.de.sookie.docx2md.reader.BlockReaderService
import com.de.sookie.docx2md.reader.CodeBlockAnalyzer
import com.de.sookie.docx2md.reader.CodeMarkerAnalyzer
import com.de.sookie.docx2md.reader.DocumentCodeMarkerAnalyzer
import com.de.sookie.docx2md.reader.DocumentReader
import com.de.sookie.docx2md.reader.InlineNormalizer
import com.de.sookie.docx2md.reader.InlineReaderService
import com.de.sookie.docx2md.reader.ListNormalizer
import com.de.sookie.docx2md.reader.ListStructureBuilder
import com.de.sookie.docx2md.reader.ParagraphAnalyzer
import com.de.sookie.docx2md.reader.ParagraphReader
import com.de.sookie.docx2md.reader.RunContentReaderService
import com.de.sookie.docx2md.reader.RunReader
import com.de.sookie.docx2md.reader.TableReader
import com.de.sookie.docx2md.reader.TextReader
import com.de.sookie.docx2md.reader.BreakReader
import com.de.sookie.docx2md.reader.TabReader
import com.de.sookie.docx2md.renderer.MarkdownRenderer
import com.de.sookie.docx2md.renderer.block.BlockRendererService
import com.de.sookie.docx2md.renderer.block.CodeBlockRenderer
import com.de.sookie.docx2md.renderer.block.HeadingRenderer
import com.de.sookie.docx2md.renderer.block.ListItemRenderer
import com.de.sookie.docx2md.renderer.block.ParagraphRenderer
import com.de.sookie.docx2md.renderer.inline.InlineRendererService
import com.de.sookie.docx2md.renderer.inline.LineBreakRenderer
import com.de.sookie.docx2md.renderer.inline.TabStopRenderer
import com.de.sookie.docx2md.renderer.inline.TextRenderer

class Docx2MdFactory {

    static DocumentReader createDocumentReader() {
        RunContentReaderService runContentReaderService =
                new RunContentReaderService([
                        new TextReader(),
                        new BreakReader(),
                        new TabReader()
                ])

        RunReader runReader =
                new RunReader(runContentReaderService)

        InlineReaderService inlineReaderService =
                new InlineReaderService([
                        runReader
                ])

        ParagraphAnalyzer paragraphAnalyzer =
                new ParagraphAnalyzer()

        CodeMarkerAnalyzer codeMarkerAnalyzer =
                new CodeMarkerAnalyzer()

        InlineNormalizer inlineNormalizer = new InlineNormalizer()
        CodeBlockAnalyzer codeBlockAnalyzer = new CodeBlockAnalyzer()

        ParagraphReader paragraphReader =
                new ParagraphReader(
                        inlineReaderService,
                        paragraphAnalyzer,
                        codeMarkerAnalyzer,
                        codeBlockAnalyzer,
                        inlineNormalizer
                )

        BlockReaderService blockReaderService =
                new BlockReaderService([
                        paragraphReader
                ])

        TableReader tableReader =
                new TableReader(blockReaderService)

        blockReaderService =
                new BlockReaderService([
                        paragraphReader,
                        tableReader
                ])

        ListNormalizer listNormalizer = new ListNormalizer()
        ListStructureBuilder listStructureBuilder = new ListStructureBuilder()

        DocumentCodeMarkerAnalyzer documentCodeMarkerAnalyzer =
                new DocumentCodeMarkerAnalyzer()

        return new DocumentReader(blockReaderService, paragraphAnalyzer, listNormalizer, listStructureBuilder, documentCodeMarkerAnalyzer)
    }

    static MarkdownRenderer createMarkdownRenderer() {
        InlineRendererService inlineRendererService =
                new InlineRendererService([
                        new TextRenderer(),
                        new LineBreakRenderer(),
                        new TabStopRenderer()
                ])

        List renderers = []

        BlockRendererService blockRendererService =
                new BlockRendererService(renderers)

        renderers.add(new CodeBlockRenderer(inlineRendererService))
        renderers.add(new HeadingRenderer(inlineRendererService))
        renderers.add(new ParagraphRenderer(inlineRendererService))

        renderers.add(new ListItemRenderer(
                blockRendererService,
                inlineRendererService
        ))

        return new MarkdownRenderer(blockRendererService)
    }
}