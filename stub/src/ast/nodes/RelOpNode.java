package ast.nodes;

import lexer.TokenType;

public class RelOpNode extends SyntaxNode {
    private SyntaxNode left;
    private SyntaxNode right;
    private TokenType op;

    /**
     * Constructs a relational operation node.
     * 
     * @param lineNumber the line number where this operation appears
     * @param left the left operand
     * @param right the right operand
     * @param op the operation to perform (e.g., LT, GT, EQ, etc.)
     */
    public RelOpNode(long lineNumber, SyntaxNode left, SyntaxNode right, TokenType op) {
        super(lineNumber);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public void displaySubtree(int indentAmt) {
        printIndented("RelOp[" + op + "](", indentAmt);
        
        if (left != null)
            left.displaySubtree(indentAmt + 2);
            
        if (right != null)
            right.displaySubtree(indentAmt + 2);
        
        printIndented(")", indentAmt);
    }
}