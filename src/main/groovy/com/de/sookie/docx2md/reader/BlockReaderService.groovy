package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block

class BlockReaderService {

    private final List<BlockReader<?>> readers

    BlockReaderService(List<BlockReader<?>> readers) {
        this.readers = readers
    }

    List<Block> read(Object object) {
        if (object == null) {
            return []
        }

        BlockReader<?> reader = readers.find {
            it.supports().isAssignableFrom(object.class)
        }

        return reader ? reader.read(object) : []
    }
}