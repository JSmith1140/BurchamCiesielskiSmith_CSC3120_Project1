package ast.nodes;

import lexer.TokenType;

public class BinOpNode extends SyntaxNode {

    private SyntaxNode left;
    private SyntaxNode right;
    private TokenType op;

    public BinOpNode(long lineNumber, SyntaxNode left, SyntaxNode right, TokenType op) {
        /*
         * BinOpNode constructor
         */
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public void displaySubtree(int indentAmt) {
        /*
         * Method to display the BinOpNode
         */
        printIndented("BinOp[" + op + "](", indentAmt);

        if (left != null)
            left.displaySubtree(indentAmt + 2);

        if (right != null)
            right.displaySubtree(indentAmt + 2);

        printIndented(")", indentAmt);
    }
}