package customcompiler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.LinkedList;

/**
 *
 * @author reynaldoalvarez
 */
public class cstNodes {
    LinkedList<cstNodes> children = new LinkedList<cstNodes>();
    cstNodes parent = null;
    String name = null;
        
    public cstNodes() { }
    
    public cstNodes(String name) {
        this.name = name;
    }
    
    public cstNodes(String name, cstNodes parent, LinkedList<cstNodes> children) {
        this.name = name;
        this.parent = parent;
        this.children = children;
    }
    
    public void push(cstNodes child) {
        child.parent = this;
        children.add(child);
    }
}
