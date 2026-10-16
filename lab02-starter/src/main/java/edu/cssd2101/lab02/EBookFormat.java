package edu.cssd2101.lab02;

/** Supported e-book formats; arbitrary strings cannot enter the domain model. */
public enum EBookFormat {
    /** Portable Document Format. */
    PDF,
    /** Electronic Publication. */
    EPUB,
    /** Legacy Mobipocket format, retained as catalogue data. */
    MOBI
}
