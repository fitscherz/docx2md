package com.de.sookie.docx2md.model

enum ParagraphStyle {
    NORMAL,
    HEADING_1,
    HEADING_2,
    HEADING_3,
    LIST

    static ParagraphStyle fromDocxName(String name) {
        if (!name) {
            return NORMAL
        }

        String normalized = name
                .replaceAll(/[\s_-]+/, "_")
                .toUpperCase()

        try {
            if(normalized.equalsIgnoreCase("LIST_PARAGRAPH")) {
                normalized = "LIST"
            }
            return valueOf(normalized)
        } catch (IllegalArgumentException ignored) {
            return NORMAL
        }

        /*switch (normalized) {
            case "HEADING1":
                return HEADING_1
            case "HEADING2":
                return HEADING_2
            case "HEADING3":
                return HEADING_3
            case "LIST":
            case "LISTPARAGRAPH":
                return LIST
            default:
                return NORMAL
        }*/
    }
}