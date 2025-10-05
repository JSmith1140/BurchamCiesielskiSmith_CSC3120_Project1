package ast.nodes;

import lexer.Token;
public class ValNode extends SyntaxNode {

    private Token name;        
    private SyntaxNode rhs;

    public ValNode(long lineNumber, Token name, SyntaxNode rhs){
        /*
         * ValNode constructor
         */
        super(lineNumber);
        this.name = name;
        this.rhs = rhs;
    }
    
    @Override
    public void displaySubtree(int indentAmt) {
        /*
         * Method to display the ValNode
         */
        printIndented("Val[" + name.getValue() + "](", indentAmt); // get value of the token and put it in between Val[]

        if (rhs != null)     
            rhs.displaySubtree(indentAmt + 2);

        printIndented(")", indentAmt);
    }
}
