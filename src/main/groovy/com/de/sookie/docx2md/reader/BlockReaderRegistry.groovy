package com.de.sookie.docx2md.reader

class BlockReaderRegistry {

    private final List<BlockReader<?>> readers

    BlockReaderRegistry(List<BlockReader<?>> readers) {
        this.readers = readers
    }

    BlockReader<?> find(Object object) {
        if (object == null) {
            return null
        }

        return readers.find {
            it.supports().isAssignableFrom(object.class)
        }
    }
}