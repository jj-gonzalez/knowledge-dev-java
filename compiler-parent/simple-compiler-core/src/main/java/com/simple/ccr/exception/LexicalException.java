package com.simple.ccr.exception;

/**
 * @author job
 * @since 27/02/26
 */
public class LexicalException extends RuntimeException{
    public LexicalException(String message, int line, int column) {
        super(String.format(
                "Lexical error at line %d, column %d: %s",
                line, column, message
        ));
    }
}
