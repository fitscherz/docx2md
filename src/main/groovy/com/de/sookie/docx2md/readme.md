src/main/groovy
├── reader
│   ├── DocumentReader.groovy
│   ├── ParagraphReader.groovy
│   ├── RunReader.groovy
│   ├── TableReader.groovy
│   ├── ImageReader.groovy
│   ├── ListReader.groovy
│   └── HyperlinkReader.groovy
├── model
│   ├── Document.groovy
│   ├── Block.groovy
│   ├── Paragraph.groovy
│   ├── Heading.groovy
│   ├── Table.groovy
│   ├── Image.groovy
│   ├── BulletList.groovy
│   ├── CodeBlock.groovy
│   └── inline
│       ├── Inline.groovy
│       ├── Text.groovy
│       ├── Bold.groovy
│       ├── Italic.groovy
│       ├── Link.groovy
│       └── ImageRef.groovy
└── renderer
└── MarkdownRenderer.groovy