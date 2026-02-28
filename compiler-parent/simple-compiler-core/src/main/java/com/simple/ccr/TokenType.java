package com.simple.ccr;

/**
 * @author job
 * @since 27/02/26
 */
public enum TokenType {
    // Preprocessor
    HASH,
    INCLUDE,

    // Keywords
    USING,
    NAMESPACE,
    INT,
    DOUBLE,
    CHAR,
    RETURN,
    SWITCH,
    CASE,
    BREAK,
    DEFAULT,

    // Identifiers
    IDENTIFIER,

    // Literals
    INTEGER_LITERAL,
    DOUBLE_LITERAL,
    STRING_LITERAL,
    CHAR_LITERAL,

    // Operators
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    ASSIGN,
    SHIFT_LEFT,      // <<
    SHIFT_RIGHT,     // >>
    LESS_THAN,
    GREATER_THAN,

    // Delimiters
    SEMICOLON,
    COMMA,
    COLON,
    LBRACE,
    RBRACE,
    LPAREN,
    RPAREN,

    // Special
    END_OF_FILE
}
