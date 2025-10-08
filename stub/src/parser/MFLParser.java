/*
 *   Copyright (C) 2022 -- 2025  Zachary A. Kissel
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package parser;

import java.io.File;
import java.io.FileNotFoundException;
import ast.SyntaxTree;
import java.util.LinkedList;
import ast.nodes.SyntaxNode;
import ast.nodes.RelOpNode;
import ast.nodes.TokenNode;
import ast.nodes.UnaryOpNode;
import ast.nodes.BinOpNode;
import ast.nodes.ProgNode;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

/**
 * <p>
 * Parser for the MFL language. This is largely private methods where
 * there is one method the "eval" method for each non-terminal of the grammar.
 * There are also a collection of private "handle" methods that handle one
 * production associated with a non-terminal.
 * </p>
 * <p>
 * Each of the private methods operates on the token stream. It is important to
 * remember that all of our non-terminal processing methods maintain the
 * invariant
 * that each method leaves the concludes such that the next unprocessed token is
 * at
 * the front of the token stream. This means each method can assume the current
 * token
 * has not yet been processed when the method begins. The methods
 * {@code checkMatch}
 * and {@code match} are methods that maintain this invariant in the case of a
 * match.
 * The method {@code tokenIs} does NOT advnace the token stream. To advance the
 * token
 * stream the {@code nextTok} method can be used. In the rare cases that the
 * token
 * at the head of the stream must be accessed directly, the {@code getCurrToken}
 * method can be used.
 * </p>
 * 
 * @author Zach Kissel
 */
public class MFLParser extends Parser {

    /**
     * Constructs a new parser for the file {@code source} by setting up lexer.
     * 
     * @param src the source code file to parse.
     * @throws FileNotFoundException if the file can not be found.
     */
    public MFLParser(File src) throws FileNotFoundException {
        super(new Lexer(src));
    }

    /**
     * Construct a parser that parses the string {@code str}.
     * 
     * @param str the code to evaluate.
     */
    public MFLParser(String str) {
        super(new Lexer(str));
    }

    /**
     * Parses the file according to the grammar.
     * 
     * @return the abstract syntax tree representing the parsed program.
     * @throws ParseException when parsing fails.
     */
    @Override
    public SyntaxTree parse() throws ParseException {
        nextToken();          
        SyntaxNode root = parseProg(); 
        match(TokenType.EOF, "EOF"); 
        return new SyntaxTree(root);
    }

    /**
     * Parses a <prog>:
     * <prog> → val <id> ; { <val ; }
    */
    private SyntaxNode parseProg() throws ParseException {
        LinkedList<SyntaxNode> statements = new LinkedList<>();

        statements.add(parseVal());
        match(TokenType.SEMI, ";");

        while (!tokenIs(TokenType.EOF)) {
            statements.add(parseVal());
            match(TokenType.SEMI, ";");
        }

        return new ProgNode(getCurrLine(), statements);
    }

    /**
     * Parses a <val>:
     * <val> → val <id> := <expr> | <expr>
    */
    private SyntaxNode parseVal() throws ParseException {
        // Case 1: "val <id> := <expr>"
        if (tokenIs(TokenType.VAL)) {
            nextToken(); // consume 'val'

            if (!tokenIs(TokenType.ID)) {
                logError("Expected identifier after 'val'");
                throw new ParseException();
            }

            Token id = getCurrToken();
            nextToken(); // consume identifier

            match(TokenType.ASSIGN, ":="); // ':='

            SyntaxNode expr = parseExpr();
            return new BinOpNode(getCurrLine(), new TokenNode(getCurrLine(), id), expr, TokenType.ASSIGN);
        }

        // Case 2: just an expression <expr>
        return parseExpr();
    }

    /**
     * Parses an expression according to the grammar rule:
     * <expr> → <rexpr> { ( and | or ) <rexpr> }
     * Handles logical binary operations (and/or).
     */
    private SyntaxNode parseExpr() throws ParseException {
        SyntaxNode left = parseRelOp(parseMExpr());

        while (tokenIs(TokenType.AND) || tokenIs(TokenType.OR)) {
            TokenType op = getCurrToken().getType();
            nextToken();
            SyntaxNode right = parseRelOp(parseMExpr());
            left = new BinOpNode(getCurrLine(), left, right, op);
        }

        return left;
    }

    /**
     * Parses a relational expression according to the grammar rule:
     * <rexpr> → <mexpr> [ ( < | > | >= | <= | = | != ) <mexpr> ]
     * This method handles the relational operator part [...] of the rule.
     */
    private SyntaxNode parseRelOp(SyntaxNode left) throws ParseException {
        // Check for relational operators
        if (tokenIs(TokenType.LT) || tokenIs(TokenType.LTE) ||
                tokenIs(TokenType.GT) || tokenIs(TokenType.GTE) ||
                tokenIs(TokenType.EQ) || tokenIs(TokenType.NEQ)) {

            TokenType operator = getCurrToken().getType();
            nextToken(); // consume the operator

            SyntaxNode right = parseMExpr();
            return new RelOpNode(getCurrLine(), left, right, operator);
        }

        return left;
    }


    /**
     * Parses a multiplicative expression:
     * <term> → <factor> { ( * | / ) <factor> }
     * Handles multiplication and division.
     */
    private SyntaxNode parseTerm() throws ParseException {
        SyntaxNode left = parseFactor();

        while (tokenIs(TokenType.MULT) || tokenIs(TokenType.DIV)) {
            TokenType op = getCurrToken().getType();
            nextToken(); // consume '*' or '/'
            SyntaxNode right = parseFactor();
            left = new BinOpNode(getCurrLine(), left, right, op);
        }

        return left;
    }

    /**
     * Parses an additive expression:
     * <mexpr> → <term> { ( + | - ) <term> }
     * Handles addition and subtraction.
     */
    private SyntaxNode parseMExpr() throws ParseException {
        SyntaxNode left = parseTerm();

        while (tokenIs(TokenType.ADD) || tokenIs(TokenType.SUB)) {
            TokenType op = getCurrToken().getType();
            nextToken(); // consume '+' or '-'
            SyntaxNode right = parseTerm();
            left = new BinOpNode(getCurrLine(), left, right, op);
        }

        return left;
    }

    /**
     * Parses a factor which could be a basic token or parenthesized expression
     */
    private SyntaxNode parseFactor() throws ParseException {   
    long line = getCurrLine();

    // Match boolean, int, real, or identifier tokens
    if (tokenIs(TokenType.TRUE) || tokenIs(TokenType.FALSE) ||
        tokenIs(TokenType.INT) || tokenIs(TokenType.REAL) ||
        tokenIs(TokenType.ID)) 
    {
        Token token = getCurrToken();
        nextToken(); // consume the token
        return new TokenNode(line, token);
    } 
    // Otherwise, expect a parenthesized expression
    else if (checkMatch(TokenType.LPAREN)) {
        SyntaxNode expr = parseExpr();
        match(TokenType.RPAREN, ")"); // must match closing paren
        return expr;
    } 
    else {
        logError("Expected a value or '('");
        throw new ParseException();
    }
}

    /**
     * Parses a unary operation according to the grammar rule:
     * <term> → not <rexpr> | ...
     * This method handles the 'not' part of the rule.
     */
    private SyntaxNode parseUnaryOp() throws ParseException {
        if (tokenIs(TokenType.NOT)) {
            long line = getCurrLine();
            nextToken(); // consume 'not'
            SyntaxNode operand = parseFactor();
            return new UnaryOpNode(line, operand, TokenType.NOT);
        }

        // If not a unary operation, try parsing as a relational expression
        SyntaxNode left = parseMExpr();
        return parseRelOp(left);
    }
}
