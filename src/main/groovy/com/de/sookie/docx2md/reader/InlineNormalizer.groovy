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

            if (buffer &&
                    buffer.fontFamily == inline.fontFamily &&
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

        return result
    }
}