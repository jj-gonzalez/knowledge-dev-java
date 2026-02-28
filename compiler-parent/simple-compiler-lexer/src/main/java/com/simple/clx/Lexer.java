package com.simple.clx;

import com.simple.ccr.Token;

import java.util.List;

/**
 * @author job
 * @since 27/02/26
 */
public interface Lexer {
    List<Token> tokenize(String source);
}
