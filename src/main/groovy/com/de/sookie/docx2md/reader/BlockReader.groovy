package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block

interface BlockReader<T> {

    Class<T> supports()

    List<Block> read(T value)
}