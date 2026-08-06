package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.inline.Inline
import com.de.sookie.docx2md.model.inline.Text

class InlineNormalizer {

    List<Inline> normalize(List<Inline> source) {
        List<Inline> result = []
        Text buffer = null

        source.each { inline ->
            if (!(inline instanceof Text)) {
                result << inline
                buffer = null
                return
            }

            if (inline.code) {
                result << inline
                buffer = null
                return
            }

            if (buffer &&
                    buffer.bold == inline.bold &&
                    buffer.italic == inline.italic &&
                    buffer.underline == inline.underline &&
                    buffer.strike == inline.strike) {

                buffer.value += inline.value
            } else {
                buffer = inline
                result << inline
            }
        }

        return mergeCode(result)
    }

    private List<Inline> mergeCode(List<Inline> inlines) {
        List<Inline> result = []
        Text current = null

        inlines.each { inline ->
            if (inline instanceof Text && inline.code) {
                if (current) {
                    current.value += inline.value
                } else {
                    current = new Text(
                            value: inline.value,
                            code: true
                    )
                }
            } else {
                if (current) {
                    result << current
                    current = null
                }
                result << inline
            }
        }

        if (current) {
            result << current
        }

        result
    }
}