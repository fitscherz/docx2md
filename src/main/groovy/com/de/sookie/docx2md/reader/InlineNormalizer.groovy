package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.inline.Inline
import com.de.sookie.docx2md.model.inline.InlineCode
import com.de.sookie.docx2md.model.inline.Text

class InlineNormalizer {

    List<Inline> normalize(List<Inline> source) {
        List<Inline> result = []
        Text buffer = null
        InlineCode codeBuffer = null

        source.each { inline ->

            if (!(inline instanceof Text)) {
                flushCode(result, codeBuffer)
                codeBuffer = null
                result << inline
                buffer = null
                return
            }

            if (inline.code) {
                if (!codeBuffer) {
                    codeBuffer = new InlineCode()
                }

                codeBuffer.add(new Text(
                        value: inline.value,
                        fontFamily: inline.fontFamily
                ))

                buffer = null
                return
            }

            flushCode(result, codeBuffer)
            codeBuffer = null

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

        flushCode(result, codeBuffer)

        return result
    }

    private void flushCode(List<Inline> result, InlineCode codeBuffer) {
        if (codeBuffer) {
            result << codeBuffer
        }
    }
}