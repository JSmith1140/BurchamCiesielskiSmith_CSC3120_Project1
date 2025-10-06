package ast.nodes;

import lexer.TokenType;

public class UnaryOpNode extends SyntaxNode {
    private SyntaxNode operand;
    private TokenType op;

    /**
     * Constructs a unary operation node.
     * 
     * @param lineNumber the line number where this operation appears
     * @param operand the operand of the unary operation
     * @param op the operation to perform (e.g., NOT)
     */
    public UnaryOpNode(long lineNumber, SyntaxNode operand, TokenType op) {
        super(lineNumber);
        this.operand = operand;
        this.op = op;
    }

    @Override
    public void displaySubtree(int indentAmt) {
        printIndented("UnaryOp[" + op + "](", indentAmt);
        
        if (operand != null)
            operand.displaySubtree(indentAmt + 2);
        
        printIndented(")", indentAmt);
    }
}