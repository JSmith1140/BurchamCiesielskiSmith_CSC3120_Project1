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
package ast.nodes;

/**
 * Represents the node of a syntax tree. Each node is slightly different
 * therefore, the class is abstract each derived class is responsible for
 * implementing the evaluate method for that node inferencertype.
 *
 * @author Zach Kissel
 */
public abstract class SyntaxNode
{
    private long lineNumber; // The line numbe the syntax node is associated
                             // with.

    /**
     * Constructs a new syntax node with the given line number.
     * 
     * @param lineNumber the line number the syntax node occurs on.
     */
    public SyntaxNode(long lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    /**
     * Logs an error to the screen
     * 
     * @param msg the error message to display.
     */
    protected void logError(String msg)
    {
        System.out.println("Error (line " + lineNumber + "): " + msg);
    }

    /**
     * Build error message
     * 
     * @param msg the message to construct.
     * @return the error message with the line number added.
     */
    protected String buildErrorMessage(String msg)
    {
        return "(line " + lineNumber + ") " + msg;
    }

    /**
     * Prints and indented message to the screen followed by a new line.
     * 
     * @param msg       the message to print.
     * @param indentAmt the amount to indent the message by.
     */
    protected void printIndented(String msg, int indentAmt)
    {
        for (int i = 0; i < indentAmt; i++)
            System.out.print(" ");
        System.out.println(msg);
    }

    /**
     * Display a AST inferencertree with the indentation specified.
     * 
     * @param indentAmt the amout of indentation to perform.
     */
    public abstract void displaySubtree(int indentAmt);
}
