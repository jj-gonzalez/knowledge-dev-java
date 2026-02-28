package com.simple.clx.impl;

import com.simple.ccr.Token;
import com.simple.ccr.TokenType;
import com.simple.ccr.exception.LexicalException;
import com.simple.clx.Lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author job
 * @since 27/02/26
 */
public class LexerImpl implements Lexer {
    private String source;
    private final List<Token> tokens = new ArrayList<>();

    private int current = 0;
    private int line = 1;
    private int column = 1;

    private static final Map<String, TokenType> keywords = new HashMap<>();

    static {
        keywords.put("include", TokenType.INCLUDE);
        keywords.put("using", TokenType.USING);
        keywords.put("namespace", TokenType.NAMESPACE);
        keywords.put("int", TokenType.INT);
        keywords.put("double", TokenType.DOUBLE);
        keywords.put("char", TokenType.CHAR);
        keywords.put("return", TokenType.RETURN);
        keywords.put("switch", TokenType.SWITCH);
        keywords.put("case", TokenType.CASE);
        keywords.put("break", TokenType.BREAK);
        keywords.put("default", TokenType.DEFAULT);
    }

    @Override
    public List<Token> tokenize(String source) {
        this.source = source;
        while (!isAtEnd()) {
            scanToken();
        }
        tokens.add(new Token(TokenType.END_OF_FILE, "", line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '#': addToken(TokenType.HASH); break;
            case '+': addToken(TokenType.PLUS); break;
            case '-': addToken(TokenType.MINUS); break;
            case '*': addToken(TokenType.MULTIPLY); break;
            case '/':
                if (match('/')) {
                    skipSingleLineComment();
                } else if (match('*')) {
                    skipMultiLineComment();
                } else {
                    addToken(TokenType.DIVIDE);
                }
                break;
            case '=': addToken(TokenType.ASSIGN); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case ',': addToken(TokenType.COMMA); break;
            case ':': addToken(TokenType.COLON); break;
            case '{': addToken(TokenType.LBRACE); break;
            case '}': addToken(TokenType.RBRACE); break;
            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case '<':
                if (match('<')) addToken(TokenType.SHIFT_LEFT);
                else addToken(TokenType.LESS_THAN);
                break;
            case '>':
                if (match('>')) addToken(TokenType.SHIFT_RIGHT);
                else addToken(TokenType.GREATER_THAN);
                break;
            case '"': string(); break;
            case '\'': charLiteral(); break;

            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                column = 1;
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    throw new LexicalException("Unexpected character: " + c, line, column);
                }
        }
    }

    private void identifier() {
        int start = current - 1;
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type, text);
    }

    private void number() {
        int start = current - 1;
        while (isDigit(peek())) advance();
        boolean isDouble = false;
        if (peek() == '.' && isDigit(peekNext())) {
            isDouble = true;
            do advance();
            while (isDigit(peek()));
        }
        String number = source.substring(start, current);
        addToken(isDouble ? TokenType.DOUBLE_LITERAL : TokenType.INTEGER_LITERAL, number);
    }

    private void string() {
        int startLine = line;
        int startCol = column;

        int start = current;

        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd())
            throw new LexicalException("Unterminated string", startLine, startCol);

        advance();

        String value = source.substring(start, current - 1);
        addToken(TokenType.STRING_LITERAL, value);
    }

    private void charLiteral() {
        int startLine = line;
        int startCol = column;

        if (isAtEnd()) throw new LexicalException("Unterminated char literal", startLine, startCol);

        char value = advance();

        if (peek() != '\'')
            throw new LexicalException("Invalid char literal", startLine, startCol);

        advance(); // closing '

        addToken(TokenType.CHAR_LITERAL, String.valueOf(value));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        column++;
        return true;
    }
    private void skipSingleLineComment() {
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
    }
    private void skipMultiLineComment() {
        int startLine = line;
        int startColumn = column;
        while (!isAtEnd()) {
            if (peek() == '*' && peekNext() == '/') {
                advance(); // *
                advance(); // /
                return;
            }
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }
        throw new LexicalException(
                "Unterminated multi-line comment",
                startLine,
                startColumn
        );
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return Character.isLetter(c) || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private char advance() {
        char c = source.charAt(current++);
        column++;
        return c;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        addToken(type, String.valueOf(source.charAt(current - 1)));
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, line, column));
    }
}
