package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock

interface InlineReader<T> {

    Class<T> supports()

    void read(T object, InlineBlock block)
}