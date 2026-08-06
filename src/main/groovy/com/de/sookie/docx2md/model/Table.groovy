package com.de.sookie.docx2md.model

class Table extends Block {

    final List<TableRow> rows = []

    void add(TableRow row) {
        if (row != null) {
            rows.add(row)
        }
    }
}