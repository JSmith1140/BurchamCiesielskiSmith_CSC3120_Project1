package ast.nodes;

import java.util.LinkedList;

public class ProgNode extends SyntaxNode {
    private LinkedList<SyntaxNode> statements; 

    public ProgNode(long lineNumber, LinkedList<SyntaxNode> statements){
        /*
         * ProgNode constructor
         */
        super(lineNumber);
        this.statements = statements;
    }

    @Override
    public void displaySubtree(int indentAmt){
        /*
         * Method to display the subtree
         */
        printIndented("Prog(", indentAmt);
        for (SyntaxNode stmt : statements) {
            if (stmt != null)
                stmt.displaySubtree(indentAmt + 2);
        }
        printIndented(")", indentAmt);
    }
}
