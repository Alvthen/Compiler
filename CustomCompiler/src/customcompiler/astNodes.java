package customcompiler;

import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author reynaldoalvarez
 */
public class astNodes {
    LinkedList<astNodes> children = new LinkedList<astNodes>();
    astNodes parent = null;
    String name = null;
   
        
    public astNodes() { }
    
    public astNodes(String name) {
        this.name = name;
    }
    
    public astNodes(String name, astNodes parent, LinkedList<astNodes> children) {
        this.name = name;
        this.parent = parent;
        this.children = children;
    }
    
    public void push(astNodes child) {
        child.parent = this;
        children.add(child);
    }
    
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
