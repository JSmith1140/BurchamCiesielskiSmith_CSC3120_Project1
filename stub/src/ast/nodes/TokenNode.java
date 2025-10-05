package ast.nodes;

import lexer.Token;

public class TokenNode extends SyntaxNode {

    private Token token;

    public TokenNode(long lineNumber, Token token) {
        /*
         * TokenNode constructor
         */
        super(lineNumber);
        this.token = token;
    }

    @Override
    public void displaySubtree(int indentAmt) {
        /*
         * Method to display the TokenNode
         */
        printIndented("Token(" + token.toString() + ")", indentAmt);
    }
}