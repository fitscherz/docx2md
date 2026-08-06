package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block

class BlockReaderSupport {

    private final BlockReaderRegistry registry

    BlockReaderSupport(BlockReaderRegistry registry) {
        this.registry = registry
    }

    Block read(Object object) {
        if (object == null) {
            return null
        }

        BlockReader<?> reader = registry.find(object)

        if (reader == null) {
            return null
        }

        return reader.read(object)
    }
}