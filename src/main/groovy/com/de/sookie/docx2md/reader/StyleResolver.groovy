package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.ParagraphStyle
import org.docx4j.openpackaging.packages.WordprocessingMLPackage
import org.docx4j.wml.Style

class StyleResolver {

    private final Map<String, Style> styles = [:]

    StyleResolver(WordprocessingMLPackage word) {
        def stylesPart = word.mainDocumentPart.styleDefinitionsPart

        if (stylesPart) {
            stylesPart.jaxbElement.style.each {
                styles[it.styleId] = it
            }
        }
    }

    ParagraphStyle resolve(String styleId) {
        Style style = styles[styleId]

        if (!style) {
            return ParagraphStyle.NORMAL
        }

        return resolveStyle(style)
    }

    private ParagraphStyle resolveStyle(Style style) {
        String name = style.name?.val?.toLowerCase()

        return ParagraphStyle.fromDocxName(name)

        /*if (name == "heading1") {
            return ParagraphStyle.HEADING_1
        }

        if (name == "heading2") {
            return ParagraphStyle.HEADING_2
        }

        if (name == "heading3") {
            return ParagraphStyle.HEADING_3
        }

        if (name?.contains("list")) {
            return ParagraphStyle.LIST
        }

        if (style.basedOn?.val) {
            Style parent = styles[style.basedOn.val]

            if (parent) {
                return resolveStyle(parent)
            }
        }

        return ParagraphStyle.NORMAL*/
    }
}