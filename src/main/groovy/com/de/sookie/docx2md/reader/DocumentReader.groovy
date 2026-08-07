package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.Document
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.Text
import org.docx4j.XmlUtils
import org.docx4j.openpackaging.packages.WordprocessingMLPackage

class DocumentReader {

    private final BlockReaderService blockReaderService
    private final ParagraphAnalyzer paragraphAnalyzer
    private final ListNormalizer listNormalizer
    private final ListStructureBuilder listStructureBuilder
    private final DocumentCodeMarkerAnalyzer documentCodeMarkerAnalyzer

    DocumentReader(BlockReaderService blockReaderService, ParagraphAnalyzer paragraphAnalyzer, ListNormalizer listNormalizer, ListStructureBuilder listStructureBuilder, DocumentCodeMarkerAnalyzer documentCodeMarkerAnalyzer) {
        this.blockReaderService = blockReaderService
        this.paragraphAnalyzer = paragraphAnalyzer
        this.listNormalizer = listNormalizer
        this.listStructureBuilder = listStructureBuilder
        this.documentCodeMarkerAnalyzer = documentCodeMarkerAnalyzer
    }

    Document read(File file) {
        return read(WordprocessingMLPackage.load(file))
    }

    Document read(WordprocessingMLPackage word) {
        paragraphAnalyzer.setStyleResolver(new StyleResolver(word))
        paragraphAnalyzer.setNumbering(word.mainDocumentPart.numberingDefinitionsPart?.contents)
        paragraphAnalyzer.setWord(word)

        Document document = new Document()

        word.mainDocumentPart.content.each {
            def value = XmlUtils.unwrap(it)

            blockReaderService.read(value).each { block ->
                document.add(block)
            }
        }

        documentCodeMarkerAnalyzer.analyze(document.blocks)

        println "=== AFTER PARAGRAPH READER ==="

        document.blocks.eachWithIndex { block, index ->
            println "${index}: ${block.class.simpleName}"

            if (block instanceof Paragraph) {
                println "type=${block.type} listId=${block.listId} level=${block.listLevel}"

                block.inlines.each { inline ->
                    if (inline instanceof Text) {
                        println "  Text: '${inline.value}'"
                    } else {
                        println "  ${inline.class.simpleName}: ${inline}"
                    }
                }
            }
        }

        println "=== BEFORE NORMALIZER ==="
        document.blocks.eachWithIndex { block, index ->
            println "${index}: ${block.class.simpleName}"

            if (block instanceof Paragraph) {
                println "    listId=${block.listId} level=${block.listLevel}"
                block.inlines.each {
                    println "       ${it.class.simpleName}: ${it instanceof Text ? it.value : ''}"
                }
            }
        }

        listNormalizer.normalize(document.blocks)
        println "=== AFTER NORMALIZER ==="

        document.blocks.eachWithIndex { block, index ->
            println "${index}: ${block.class.simpleName}"

            if (block instanceof Paragraph) {
                println "Paragraph properties: ${block.properties}"

                block.inlines.each { inline ->
                    if (inline instanceof Text) {
                        println "  Text: '${inline.value}'"
                    } else {
                        println "  ${inline.class.simpleName}: ${inline}"
                    }
                }
            }

            if (block instanceof CodeBlock) {
                println "listId=${block.listId} level=${block.listLevel}"
                println "  code=${block}"
            }
        }

        println "=== BEFORE LIST BUILDER ==="
        document.blocks.eachWithIndex { block, index ->
            println "${index}: ${block.class.simpleName}"

            if (block instanceof Paragraph || block instanceof CodeBlock) {
                println "    listId=${block.listId} level=${block.listLevel}"
            }
        }

        List<Block> structured = listStructureBuilder.build(document.blocks)

        println "=== AFTER LIST BUILDER ==="
        dumpBlocks(structured)

        document.blocks.clear()
        document.blocks.addAll(structured)

        return document
    }

    private void dumpBlocks(List<Block> blocks, String indent = "") {
        blocks.each { block ->
            println "${indent}${block.class.simpleName}"

            if (block instanceof com.de.sookie.docx2md.model.ListItem) {
                dumpBlocks(block.children, indent + "  ")
            }
        }
    }
}