/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customcompiler;

import customcompiler.Lexer.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JTextArea;
import javax.xml.bind.DatatypeConverter;



/**
 *
 * @author reynaldoalvarez
 */
public class Assembler {
    int heapNum = 0;
    int heapCount = 0;
    int g = 0;
    int savedPoint;
    int safePoint;
    int loadPoint;
    int stringSave;
    int firstSave;
   
    
    
    char[] varNumList;
    char[] currentRegister = {'T','0'};
    
    String[] heap = new String[256];
    
    Lexer lex = new Lexer();
    Parser parser;
    customAST ast;
    
    ArrayList<Integer> scopeList;
    ArrayList<Integer> accumulator;
    ArrayList<Integer> storeLocation;
    ArrayList<Integer> valueList;
    
    ArrayList<String> variables;
    ArrayList<String> regVariables;
    ArrayList<String> idList;
    ArrayList<String> stringList;
    ArrayList<String> printList;
    ArrayList<String> typeList;
    
    
  
    /**
     * Starts the Code Generation phase
     * Initializes and declares variables that we will use
     * Leads into the initialize class
     * @param parser 
     */
    public Assembler(Parser parser) {
        variables = new ArrayList<String>();
        regVariables = new ArrayList<String>();
        this.parser = parser;
        ast = parser.getAst();
        idList = parser.getIdList();
        scopeList = parser.getScopeList();
        stringList = new ArrayList<String>();
        printList = new ArrayList<String>();
        typeList = new ArrayList<String>();
        accumulator = new ArrayList<Integer>();
        storeLocation = new ArrayList<Integer>();
        valueList = new ArrayList<Integer>();
    }
    
    
    /**
     * First step and Last step
     * Begins by searching and storing information
     * needed for code generation
     * Ends by calling generateCode, which outputs the opCode
     */
    public void gatherAndGenerate() {
        // Grabs the already created AST and places the pointer at the root node
        LinkedList<astNodes> operations = searchChildren(ast.root);
        
        // Takes the AST information and implements code gen
        disassembleOperations(operations);
        
        generateCode(); // Output generated code
    }
    
    
    /**
     * Searches through AST
     * Finds Parent info and children info
     * Stores children information for later use
     * @param node
     * @return 
     */
    private LinkedList<astNodes> searchChildren(astNodes node) {
        // Location where important information found will be stored
        LinkedList<astNodes> output = new LinkedList<astNodes>();
        
        // Pointers only hit Parents and checks if there are children
        if(node.hasChildren()) { 
            if(node.name.equals("Variable Declaration")) {
                output.add(node);
            } else if(node.name.equals("Assignment Statement")) {
                output.add(node);
            } else if(node.name.equals("Print Statement")) {
                output.add(node);
            }
            
            // Searching if current parent has any children and stores the value 
            for(int i = 0; i < node.children.size(); i++) { 
                LinkedList<astNodes> temp = searchChildren(node.children.get(i));
                // When the parents has a child that is also a parent keep searching through their children
                for(int j = 0; j < temp.size(); j++) { // Eventually moving back up the tree if there still more children
                    output.add(temp.get(j));
                }
            }
        }  
        return output;
    }
    
    
    /**
     * Loops through the stored operations and 
     * if one of these options are found then execute
     * the proper action for that operation
     * @param operations 
     */
    private void disassembleOperations(LinkedList<astNodes> operations) {
        for(int i = 0; i < operations.size(); i++) { // Loops through operations and finds specific names
            if(operations.get(i).name.equals("Variable Declaration")) {
               handleVarDecl(operations.get(i));
            } else if(operations.get(i).name.equals("Assignment Statement")) {
               handleAssignStatement(operations.get(i));
            } else if(operations.get(i).name.equals("Print Statement")) {
               handlePrintStatement(operations.get(i)); 
            } else {
               System.out.println("Error: Improper operation attempted");
            }
        }        
    }
    
    
    /**
     * When a Parent is VarDecl
     * This contains the correct OPCodes for VarDecl instances
     * @param varDecl 
     */
    private void handleVarDecl(astNodes varDecl) {
        heap[heapNum] = "A9";
        heapNum++;
        heap[heapNum] = "00";
        heapNum++;
        heap[heapNum] = "8D";
        heapNum++;
        firstSave = heapNum;
        heapNum++;
        
        regVariables.add("" + currentRegister[0] + currentRegister[1]);
        variables.add(varDecl.children.get(1).name);
        typeList.add(varDecl.children.get(0).name);
        
        incrementRegister();
        System.out.println("type: " + varDecl.children.get(0).name);
        System.out.println("VARDEcl " + regVariables.get(0));
        endOperation();
    }
    
    
    /**
     * When a Parent is Assignment Statement
     * This contains the correct OPCodes for Assignment Statement instances
     * @param assignStatement
     */
    private void handleAssignStatement(astNodes assignStatement) { 
        String currentVariable = assignStatement.children.get(0).name;
        String currentValue = assignStatement.children.get(1).name;
        
        heap[heapNum] = "A9";
        for(int i = 0; i < heap[heapNum].length(); i++) {
            System.out.println(Arrays.toString(heap[heapNum].toCharArray()));
            System.out.println(Arrays.toString(heap[heapNum].getBytes()));
            break;
        }
        heapNum++;

        for(int h = 0; h < variables.size(); h++) {
            if(variables.get(h).equals(currentVariable)) {
                System.out.println("matched");
                if(variables.indexOf(h) == typeList.indexOf(h)) { // If this var pos matches their type position
                   System.out.println("value: " + currentValue);
                   System.out.println("var: " + currentVariable);
                   System.out.println("type: " + typeList.get(h));
                    if(typeList.get(h).contains("string")) {
                        System.out.println("CHECK: "+ DatatypeConverter.parseByte(Integer.toString(heapNum)));
                        for(int p = heapNum; p < heap.length; p++) {
                            if(heap[p] == null) {
                                System.out.println("Found.");
                                stringSave = p;
                                heapNum++;
                                GetAccumulator2(currentValue);
                                break;
                            }
                        }
                    } else if(typeList.get(h).contains("int")) {
                        heap[heapNum] = "0" + currentValue;
                        heapNum++;
                    } else if(typeList.get(h).contains("boolean")) {
                       for(int p = heapNum; p < heap.length; p++) {
                            if(heap[p] == null) {
                                System.out.println("Found.");
                                stringSave = p;
                                heapNum++;
                                GetAccumulator2(currentValue);
                                break;
                            }
                        }
                    } else {
                        System.out.println("blues clues");
                    }
                }
            } else {
               System.out.println("not a match");
            }
        }
        
        
        System.out.println(currentValue);
        
        heap[heapNum] = "8D";
        heapNum++;
        heapCount = heapNum;
        LoadCommands(currentValue);
//        valueList.clear();
//        for(int i = 0; i < variables.size(); i++) {
//            if(variables.get(i).equals(currentVariable)) {
//                heap[heapNum] = "" + regVariables.get(i);
//                break;
//            }
//        }
       
        endOperation(); 
    }
    
    
    /**
     * When a Parent is Print Statement
     * This contains the correct OPCodes for Print Statement instances
     * @param printStatement 
     */
    private void handlePrintStatement(astNodes printStatement) {
        System.out.println("there"); 
        String currentPrintStatement = printStatement.children.get(0).name;
        if(Character.isLetter(currentPrintStatement.charAt(0))) {
            if(variables.size() > 0 && currentPrintStatement.length() == 1) {
                heap[heapNum] = "AC";
                heapNum++;
                stringSave = heapNum;
                heapNum++;
                endOperation();
                heap[heapNum] = "A2";
                heapNum++;
                
                for(int m = 0; m < typeList.size(); m++) {
                    if(typeList.get(m).contains("int")) {
                        heap[heapNum] = "01";
                        heapNum++;
                        break;
                    } else if(typeList.get(m).contains("string")) {
                        heap[heapNum] = "02";
                        heapNum++;
                        break;
                    } else if(typeList.get(m).contains("boolean")) {
                        heap[heapNum] = "02";
                        heapNum++;
                        break;
                    } else {
                        heap[heapNum] = "01";
                        heapNum++;
                        break;
                    }
                }
                SystemCall();
                for(int y = heapNum; y < heap.length; y++) {
                    if(heap[y] == null) {
                        y++;
                        if(!(heap[y] == null)) {
                            // Not done looping
                            break;
                        } else {
                            storeLocation.add(y);
                            if(storeLocation.get(0) < 16) {
                                heap[heapCount] = "0" + Integer.toString(storeLocation.get(0), 16).toUpperCase(); 
                                heap[stringSave] = "0" + Integer.toString(storeLocation.get(0), 16).toUpperCase(); 
                                heap[firstSave] = "0" + Integer.toString(storeLocation.get(0), 16).toUpperCase(); 
                                storeLocation.clear();
                                break;
                            } else {
                                heap[heapCount] = Integer.toString(storeLocation.get(0), 16).toUpperCase();
                                heap[stringSave] = Integer.toString(storeLocation.get(0), 16).toUpperCase(); 
                                heap[firstSave] = Integer.toString(storeLocation.get(0), 16).toUpperCase(); 
                                storeLocation.clear();
                                break;
                            }
                        }
                    }
                }
            } else {
                if(Character.isLetter(currentPrintStatement.charAt(0))) {
                    System.out.println("I am a letter");
                    System.out.println(currentPrintStatement.charAt(0));
                    AD(); // Load the accumulator from memory
                    savedPoint = heapNum;
                    System.out.println("sa: " + savedPoint);
                    System.out.println("he: " + heapNum);
                    LoadCommands(currentPrintStatement); // Loads OpCode commands for this print statement
                } else {
                    System.out.println("I am a digit");
                    System.out.println(currentPrintStatement.charAt(0));
                    A0(); // Load the Y register with a constant
                    GetConstant(currentPrintStatement); // Returns the Current integer being evaluated
                    A2(); // Load the X register with a constant
                    Num01(); // Print the integer stored in the Y register
                    SystemCall();
                    System.out.println("var: " + currentPrintStatement);
                    System.out.println("NOT a var: " + currentPrintStatement.length());
                    System.out.println("current: " + printStatement.children.get(0).name);
                }
            }
        } else {
            System.out.println("I am a digit");
            System.out.println(currentPrintStatement.charAt(0));
            A0(); // Load the Y register with a constant
            GetConstant(currentPrintStatement); // Returns the Current integer being evaluated
            A2(); // Load the X register with a constant
            Num01(); // Print the integer stored in the Y register
            SystemCall();
            System.out.println("var: " + currentPrintStatement);
            System.out.println("NOT a var: " + currentPrintStatement.length());
            System.out.println("current: " + printStatement.children.get(0).name);
        }
    }
    private String GetAccumulator2(String OpCodes) {
        StringToHex(OpCodes);
        System.out.println("printList: " + printList.get(0));
        System.out.println("sa: " + stringSave);
        for(int n = 0; n < heap.length; n++) {
            if(n == (heap.length - (1 + stringSave))) {
                heap[firstSave] = Integer.toString(accumulator.get(0), 16).toUpperCase();
                heapNum++;
                break;
            } else {
                heapNum--;
            }
        }
        accumulator.clear(); // Clear for next accumulator 
        System.out.println("printList: " + printList.get(0));
        
        return heap[heapNum];
    }
    
    
    
    private String LoadCommands(String OpCodes) {
        GetAccumulator(OpCodes);
        for(int y = heapNum; y < heap.length; y++) {
            if(heap[y] == null) {
                y++;
                if(!(heap[y] == null)) {
                    // Not done looping
                    GetAccumulator(OpCodes);
                    break;
                } else {
                    storeLocation.add(y);
                    if(storeLocation.get(0) < 16) {
                        heap[loadPoint] = "0" + Integer.toString(storeLocation.get(0), 16).toUpperCase();     
                    } else {
                        heap[loadPoint] = Integer.toString(storeLocation.get(0), 16).toUpperCase();
                    }                    
                    //storeLocation.clear();
                    
                    break;
                }
            }
        }
        
        return heap[heapNum];
    }
    
 
    /**
     * Returns the Current integer being evaluated
     * @return
     */
    private String GetAccumulator(String currentString) {
        StringToHex(currentString);
        System.out.println("printList: " + printList.get(0));
        System.out.println("sa: " + savedPoint);
        for(int n = 0; n < heap.length; n++) {
            if(n == (heap.length - (1 + savedPoint))) {
                heap[heapNum] = Integer.toString(accumulator.get(0), 16).toUpperCase();
                heapNum++;
                endOperation();
                A0(); // Load the Y register with a constant
                heap[heapNum] = Integer.toString(accumulator.get(0), 16).toUpperCase();
                heapNum++;
                safePoint = heapNum;
                Num8D(); // Store the accumulator in memory
                loadPoint = heapNum;
                heapNum++;
                endOperation();
                A2(); // Load the X register with a constant
                Num02(); // Print the 00-terminated string stored at the address in the Y register
                SystemCall(); // Print letter out
                break;
            } else {
                heapNum--;
            }
        }
        accumulator.clear(); // Clear for next accumulator 
        System.out.println("printList: " + printList.get(0));
        return heap[heapNum];
    }
    
    
    /**
     * Returns the Current integer being evaluated
     * @return
     */
    private String GetConstant(String constant) {
        heap[heapNum] = "0" + constant;
        heapNum++;
        return heap[heapNum];
    }
    
    
    /**
     * Load the X register with a constant
     */
    private void A2() {
        heap[heapNum] = "A2";
        heapNum++;
    }
    
    
    /**
     * Print the 00-terminated string stored 
     * at the address in the Y register
     */
    private void Num02() {
        heap[heapNum] = "02";
        heapNum++;
    }
    
    
    /**
     * Print the integer stored in the Y register
     */
    private void Num01() {
        heap[heapNum] = "01";
        heapNum++;
    }
    
    
    /**
     * Store the accumulator in memory
     */
    private void Num8D() {
        heap[heapNum] = "8D";
        heapNum++;
    }
    
    
    /**
     * Load the Y register with a constant
     */
    private void A0() {
        heap[heapNum] = "A0";
        heapNum++;
    }
    
    
    /**
     * Load the accumulator from memory
     */
    private void AD() {
        heap[heapNum] = "AD";
        heapNum++;
    }
    

    /**
     * Double 00's are placed after a statement
     */
    private void endOperation() {
        heap[heapNum] = "00";
        heapNum++;
    }
    
    
    /**
     * Double FF's are placed after
     * finishing a print statement
     */
    private void SystemCall() {
        heap[heapNum] = "FF";
        heapNum++;
    }
    
    
    /**
     * Converts Strings into Hexadecimals and 
     * adds them to the heap at the end of table
     * @return 
     */
    private String StringToHex(String currentString) {
        byte[] f = currentString.getBytes();
            
        String stringStream = DatatypeConverter.printHexBinary(f);

        stringStream = stringStream.replaceAll("..", "$0 ").trim();

        for(int k = 0; k < stringStream.length(); k+=1) {
            char charAt = stringStream.charAt(k);
            stringList.add("" + charAt);
        }
        
        for(int i = 0; i < stringList.size(); i+=4) {
            printList.add(stringList.get(i) + stringList.get(i + 1));                                                               
            i--;
        }
        
        stringList.clear();
        for(int j = heapNum; j < heap.length - 1; j++) {
            heapNum++;
        }
        
        for(int b = 0; b < printList.size(); b++) { 
            heapNum--;
        }
        
        
        accumulator.add(heapNum); // Storing the accumulators location on heap
        System.out.println("reg: " + heapNum);
        for(int b = 0; b < printList.size(); b++) { 
            heap[heapNum] = printList.get(b);
            heapNum++;
            if(b == printList.size() - 1) {
                System.out.println("here");
                printList.add(printList.size(), "00");
                break;
            }
        }       
        
        return heap[heapNum];
    }
    
    
    /**
     * Increments the Saved Number of VarDecls accordingly
     * When 9 is reached the saving Letter e.g T turns into next Letter
     * This is based off of ASCII order
     */
    private void incrementRegister() {
        if((int)currentRegister[1] < 57) {
            currentRegister[1] = (char)(currentRegister[1] + 1);
        } else {
            currentRegister[0] = (char)(currentRegister[0] + 1);
            currentRegister[1] = '0';
        }
    }
    
    
    /**
     * Increments the Letter part of the VarDecl
     * When 9 is reached the saving Letter e.g T turns into next Letter
     * This is based off of ASCII order
     * @param n
     * @return 
     */
    private char[] getVariableInRegister(int n) {
        char[] output = new char[2];
        output[0] = (char) ('T' + (n / 10));
        output[1] = (char) (n % 10);
        return output;
    }
    
    
    /**
     * Outputs generated code from the disassembled
     */
    private void generateCode() {
        parser.getAstOutputAreaCodeGen().append("\n                Program 1 Code Generation\n" +
                                        "   -----------------------------------------\n\t   ");
        for(int k = 0; k < heap.length; k++) {
            if(g == 12) {
                g = 0;
                parser.getAstOutputAreaCodeGen().append("\n\t   ");
            }
            if(heap[k] == null) {
                g++;
                parser.getAstOutputAreaCodeGen().append("00 ");
            } else {
                g++;
                parser.getAstOutputAreaCodeGen().append(heap[k] + " ");
            }
        }
        parser.getAstOutputAreaCodeGen().append("\n   -----------------------------------------\n");
    }
}
