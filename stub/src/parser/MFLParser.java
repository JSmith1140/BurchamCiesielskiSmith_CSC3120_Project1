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
import lexer.Lexer;

/**
 * <p>
 * Parser for the MFL language. This is largely private methods where
 * there is one method the "eval" method for each non-terminal of the grammar.
 * There are also a collection of private "handle" methods that handle one
 * production associated with a non-terminal.
 * </p>
 * <p>
 * Each of the private methods operates on the token stream. It is important to 
 * remember that all of our non-terminal processing methods maintain the invariant
 * that each method leaves the concludes such that the next unprocessed token is at
 * the front of the token stream. This means each method can assume the current token
 * has not yet been processed when the method begins. The methods {@code checkMatch}
 * and {@code match} are methods that maintain this invariant in the case of a match. 
 * The method {@code tokenIs} does NOT advnace the token stream. To advance the token
 * stream the {@code nextTok} method can be used. In the rare cases that the token 
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
    public SyntaxTree parse() throws ParseException {
       return null;
    }

    /************
     * Evaluation methods to constrct the AST associated with the non-terminals
     ***********/

    }
