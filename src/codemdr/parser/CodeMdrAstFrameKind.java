package codemdr.parser;

/**
 * Enum representing the kind of frame in the AST
 * <br>
 * An AST frame represents a different set of instructions for the parser to execute in a given context.
 * <br>
 * (E.g. the rules for parsing a statement could be different in a normal block statement and in a class body)
 */
public enum CodeMdrAstFrameKind {
    DEFAULT
}
