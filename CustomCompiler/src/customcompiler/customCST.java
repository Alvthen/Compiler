//-----------------------------------------
// treeDemo.js
//
// By Alan G. Labouseur, based on the 2009
// work by Michael Ardizzone and Tim Smith.
// Modified by Reynaldo Alvarez
//-----------------------------------------


package customcompiler;


import customcompiler.Lexer.Token;
import customcompiler.Lexer.TokenType;
import static jdk.nashorn.internal.objects.Global.undefined;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Alan G. Labouseur
 * @author reynaldoalvarez
 */
public class customCST {
    
    cstNodes root;
    cstNodes cur = new cstNodes();
    Token token;
    
    public customCST() {
       // Root node is Program
       this.root = null;
    } 
    
    
    // -- ------- --
    // -- Methods --
    // -- ------- --
    
    /**
     *
     * Add a node: kind in {branch, leaf}
     * @param name
     * @param kind 
     */
    public void addNode(String name, String kind) {
        // Construct the node object.
        cstNodes node = new cstNodes(name);
        
        // Check to see if it needs to be the root node.
        if (this.root == null) {
            // We are the root node.
            this.root = node;
        } else {
            // We are the children.
            // Make our parent the CURrent node...
                node.parent = this.cur;
            // ... and add ourselves (via the unfrotunately-named
            // "push" function) to the children array of the current node.
            this.cur.push(node);
            
                
        }
        // If we are an interior/branch node, then...
        if (kind.equals("branch")) {
            // ... update the CURrent node pointer to ourselves.
            this.cur = node;
        }       
    }
    
    
    /**
     * 
     * Aligns the end of program marker to the block branch
     * This allows the tree follows the grammar format
     */
    public void scaleToRoot() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
        }
    }
    
    
    /**
     * 
     * When a closed parenthesis is added 
     * it must be aligned to its previous open parenthesis
     */
    public void scaleToPrintStatement() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("Print Statement".equals(this.cur.parent.name)) {
                /**
                 * stops one before print statement, 
                 * so this is a little push in order for close parenthesis
                 * to land as a child in print statement accordingly  
                 */
                endChildren();
                break;
            }
        }
    }
    
    public void scaleToBooleanExpression() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("Boolean Expression".equals(this.cur.parent.name)) {
                /**
                 * stops one before print statement, 
                 * so this is a little push in order for close parenthesis
                 * to land as a child in print statement accordingly  
                 */
                endChildren();
                break;
            }
        }
    }
    

    /**
     * 
     * When a a finishing quote is added 
     * it must be aligned to its previous open quote
     */
    public void scaleToQuote() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("String Expression".equals(this.cur.parent.name)) {
                /**
                 * stops an extra - before String Expression, 
                 * so this is a little push in order for quote
                 * to land as a child in String Expression accordingly  
                 */
                endChildren();
                break;
            }
        }
    }
    
    /**
     * 
     * Increments specifically the Statement List branch
     * This brings the tree together and avoids a wider display
     * Gets called before the Statement List branch is created
     */
    public void statementListIncrement() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("Statement List".equals(this.cur.parent.name)) {
                endChildren(); // Needs a little push to be aligned correctly
                break;
            }
        }
    }
    
    public void endStatementListIncrement() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("Block".equals(this.cur.parent.name)) {  
                endChildren();
                this.cur = this.cur.parent;
                if("Statement List".equals(this.cur.parent.name)) {
                    endChildren(); // Needs a little push to be aligned correctly
                    break;
                }
            }
        }
    }
    
    /**
     * 
     * Aligns close brackets 
     * to its appropriate open bracket
     */
    public void scaleToBlock() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("Block".equals(this.cur.parent.name)) {
                /**
                 * stops one before Program, 
                 * so this is a little push in order for close bracket
                 * to land as a child in the block branch accordingly  
                 */
                endChildren();
                break;
            }
        }
    }
       
    
    public void scaleToCondition() {
        while((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
            if("If Statement".equals(this.cur.parent.name)) {
                /**
                 * stops an extra - before String Expression, 
                 * so this is a little push in order for quote
                 * to land as a child in String Expression accordingly  
                 */
                endChildren();
                break;
            } else if("While Statement".equals(this.cur.parent.name)) {
                endChildren();
                break;
            }
        }
    }
    
    
    /**
     * 
     * When Program runs more than once we must
     * remove all family members from List
     * so that a new family can be created without duplicate families
     */
    public void restartFamily() {
        this.root = null;
    }
    
    
    // Note that we're done with this branch of the tree...
    public void endChildren() {
        //Node node = new cstNodes();
        // ... by moving "up" to our parent node (if possible).
        if ((this.cur.parent != null) && (this.cur.parent.name != undefined)) {
            this.cur = this.cur.parent;
        } else {
            // TODO: Some sort of error logging.
            // This really should not happen, but it will, of course.
        }
    }

    
    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        // Make the initial call to expand from the root.
        return expand((cstNodes) this.root, 0);
        // Return the result.
    } 
    
    // Recursive function to handle the expansion of the nodes.
    public String expand(cstNodes node, int depth) {
        String traversalResult = "";

        // Space out based on the current depth so
        // this looks at least a little tree-like.
        for (int i = 0; i < depth; i++) {
            traversalResult += "-";
        }
        
        // If there are no children (i.e., leaf nodes)...
        if (node.children.isEmpty()) {
            if(node.name.equals("Statement List")) { // If we t.endChildren after StatementList
                traversalResult += "<" + node.name + ">";
                traversalResult += "\n";
            } else if(node.name.equals("Char List")) {
                traversalResult += "<" + node.name + ">";
                traversalResult += "\n";
            } else if(node.name.equals("Block")) {
                traversalResult += "<" + node.name + ">";
                traversalResult += "\n";
            } else {
                // ... note the leaf node.
                traversalResult += "[" + node.name + "]";
                traversalResult += "\n";
            }
        } else {
            // There are children, so note these interior/branch nodes and ...
            traversalResult += "<" + node.name + "> \n";
            // .. recursively expand them.
            for (int i = 0; i < node.children.size(); i++) {
                traversalResult += expand(node.children.get(i), depth + 1);
            }
        }
        return traversalResult;
    }   
}
