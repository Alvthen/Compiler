/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customcompiler;


import static customcompiler.Lexer.TokenType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JTextArea;


/**
 *
 * @author Reynaldo Alvarez
 */

public class Lexer extends javax.swing.JFrame {

    public Pattern tokenPatterns;
    public Matcher matcher;
    /**
     * Creates lexer JFrame
     */
    public Lexer() {
        // Components for the Jframe
        initComponents();
    }
    
    
    // ------------------------------------------------------------
    // -----------------[Global Variables]-------------------------
    // ------------------------------------------------------------
    
    // Creates the ArrayList of Tokens
    public ArrayList<Token> tokens = new ArrayList<Token>();
    
    // Gets the current Token position
    private int curToken = 0;
    
    // Gets the line number of the current Token
    private int lineNumber = 1;
    
    private int errorCount = 0;
    
    private int warningCount = 0;
    
    
    // ------------------------------------------------------------
    // -----------------[Getters and Setters]----------------------
    // ------------------------------------------------------------

    public JTextArea getInputArea() {
        return this.inputArea;
    }
    
    public String getInput() {
        String input = inputArea.getText();
        return input;
    }
    
    public String getOutputArea() {
        String output = outputArea.getText();
        return output;
    }

    public JTextArea getOutputAreaParser() {
        return this.outputAreaParser;
    }
    
    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public int getErrorCount() {
        return this.errorCount;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public JTextArea getAstOutputArea() {
        return astOutputArea;
    }

    public void setAstOutputAreaCodeGen(JTextArea astOutputAreaCodeGen) {
        this.astOutputAreaCodeGen = astOutputAreaCodeGen;
    }
    
    public Pattern getPatterns() {
         
        // Lexer takes the input, finds the patterns and places them into token format
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for(TokenType tokenType : TokenType.values()) 
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        this.tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1), Pattern.CASE_INSENSITIVE);

        return tokenPatterns;
    }
    
    public Matcher getMatcher() {
        Pattern patterns = getPatterns();
        
        String input = inputArea.getText();
        
        // Lexer Matches the patterns and if they are valid, they will be added to the new tokens array for output
        this.matcher = patterns.matcher(input);
        
        return matcher;
    }

    public JButton getButtonClearAll() {
        return this.buttonClearAll;
    }

    public void setButtonClearAll(JButton buttonClearAll) {
        this.buttonClearAll = buttonClearAll;
    }
    
    
    // ------------------------------------------------------------
    // ---------------------[Definitions]--------------------------
    // ------------------------------------------------------------
    
    // Defining our tokens with their corresponding expression names 
    public static enum TokenType {
        
        // -----------------|End of Program Marker|--------------------- \\
        EOP("[$]"),
        
       
        
        // Quote
        Quote("(\")|(\\”)|(\\“)"),
        
        // -------------|Types|Numbers|Statements|Identifiers|Booleans|---------------- \\
        // Types
        typeInt("((?<!\")|(?<![a-z]))int\\s(?![a-z]{2}|[a-z]\")"),
        typeString("((?<!\")|(?<![a-z]))string\\s(?![a-z]{2}|[a-z]\")"),
        typeBoolean("((?<!\")|(?<![a-z]))boolean\\s(?![a-z]{2}|[a-z]\")"),
        
        // Statemenetsrive
        ifStatement("if"),
        whileStatement("while"),
        printStatement("print"),
        
        // Booleans
        boolvalFalse("false"),
        boolvalTrue("true"),
        
        // Identifiers 
        
       
        // Numbers
        digit("[0-9]"), 
        
        // --------------------|Symbols|--------------------------- \\
        // Arithmetic Operator
        intopAddition("[+]"),
        
        // Unary Operator
        boolopNotEqualTo("!="),
        boolopEqualTo("([=]=)"),
        assignmentStatement("="),
        
        // Brackets
        openBracket("[{]"),
        closeBracket("[}]"),
        
        // Parenthesis
        openParenthesis("[(]"),
        closeParenthesis("[)]"),
        
        // Whitespace
        whiteSpace("[ \t\f\r]+"),
        
        // New line
        newLine("[\n|\r]"),
        
        // CHAR
        CHAR("[a-z]"), 
        
         // Unrecognized Tokens
        unrecognized("[A-Z|~|!|@|#|%|^|&|*|_|:|<|>|?|;|'|,|.|`|-]"),
        
        // Comments
        comment("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)");
        
        Pattern pattern;
        
        private TokenType(String pattern) {
            this.pattern = Pattern.compile(pattern);
        } 
        
        public Pattern getPattern() {
            return this.pattern;
        }
        
        public TokenType getType() {
            return this;
        }    
    }
    
    // Stores token type and data
    public class Token {
        public TokenType type;
        public String data;
         
        // Creating the characteristics of a token
        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }
        
        // Getter method for getting Token types
        public TokenType getType() {
            return type;
        }
        
        // Getter method for getting Token Data;
        public String getData() {
            return data;
        }
        
        @Override
        public String toString() { // Structures token type and data for output
            return String.format("\"%s\" --> [%s]" , data, type);
        }
       
        /**
         * Lex the input of characters 
         * and make them tokens 
         * if they are in our grammar
         * else they are unrecognized 
         */
        public Token() {
            
            int i = 1;

            Parser info = new Parser();

            String input = inputArea.getText();
            String output = outputArea.getText();
                    
            Matcher tokenMatcher = getMatcher();
                
            // Loops through the input and finds valid tokens
            while(tokenMatcher.find()) {
                if(tokenMatcher.group(TokenType.newLine.name()) != null) {
                    tokens.add(new Token(TokenType.newLine, tokenMatcher.group(TokenType.newLine.name())));
                } else if(tokenMatcher.group(TokenType.whiteSpace.name()) != null) {
                    continue;
                } else if(tokenMatcher.group(TokenType.comment.name()) != null) {
                    continue;
                } else if(tokenMatcher.group(TokenType.typeInt.name()) != null) {
                    tokens.add(new Token(TokenType.typeInt, tokenMatcher.group(TokenType.typeInt.name())));
                } else if(tokenMatcher.group(TokenType.typeString.name()) != null) {
                    tokens.add(new Token(TokenType.typeString, tokenMatcher.group(TokenType.typeString.name())));
                } else if(tokenMatcher.group(TokenType.typeBoolean.name()) != null) {
                    tokens.add(new Token(TokenType.typeBoolean, tokenMatcher.group(TokenType.typeBoolean.name())));
                } else if(tokenMatcher.group(TokenType.ifStatement.name()) != null) {
                    tokens.add(new Token(TokenType.ifStatement, tokenMatcher.group(TokenType.ifStatement.name())));
                } else if(tokenMatcher.group(TokenType.whileStatement.name()) != null) {
                    tokens.add(new Token(TokenType.whileStatement, tokenMatcher.group(TokenType.whileStatement.name())));
                } else if(tokenMatcher.group(TokenType.printStatement.name()) != null) {
                    tokens.add(new Token(TokenType.printStatement, tokenMatcher.group(TokenType.printStatement.name())));
                } else if(tokenMatcher.group(TokenType.assignmentStatement.name()) != null) {
                    tokens.add(new Token(TokenType.assignmentStatement, tokenMatcher.group(TokenType.assignmentStatement.name())));
                } else if(tokenMatcher.group(TokenType.CHAR.name()) != null) {
                    tokens.add(new Token(TokenType.CHAR, tokenMatcher.group(TokenType.CHAR.name())));
                } else if(tokenMatcher.group(TokenType.boolvalFalse.name()) != null) {
                    tokens.add(new Token(TokenType.boolvalFalse, tokenMatcher.group(TokenType.boolvalFalse.name())));
                } else if(tokenMatcher.group(TokenType.boolvalTrue.name()) != null) {
                    tokens.add(new Token(TokenType.boolvalTrue, tokenMatcher.group(TokenType.boolvalTrue.name())));
                } else if(tokenMatcher.group(TokenType.digit.name()) != null) {
                    tokens.add(new Token(TokenType.digit, tokenMatcher.group(TokenType.digit.name())));
                } else if(tokenMatcher.group(TokenType.intopAddition.name()) != null) {
                    tokens.add(new Token(TokenType.intopAddition, tokenMatcher.group(TokenType.intopAddition.name())));
                } else if(tokenMatcher.group(TokenType.boolopNotEqualTo.name()) != null) {
                    tokens.add(new Token(TokenType.boolopNotEqualTo, tokenMatcher.group(TokenType.boolopNotEqualTo.name())));
                } else if(tokenMatcher.group(TokenType.boolopEqualTo.name()) != null) {
                    tokens.add(new Token(TokenType.boolopEqualTo, tokenMatcher.group(TokenType.boolopEqualTo.name())));
                } else if(tokenMatcher.group(TokenType.openBracket.name()) != null) {
                    tokens.add(new Token(TokenType.openBracket, tokenMatcher.group(TokenType.openBracket.name())));
                } else if(tokenMatcher.group(TokenType.closeBracket.name()) != null) {
                    tokens.add(new Token(TokenType.closeBracket, tokenMatcher.group(TokenType.closeBracket.name())));
                } else if(tokenMatcher.group(TokenType.openParenthesis.name()) != null) {
                    tokens.add(new Token(TokenType.openParenthesis, tokenMatcher.group(TokenType.openParenthesis.name())));
                } else if(tokenMatcher.group(TokenType.closeParenthesis.name()) != null) {
                    tokens.add(new Token(TokenType.closeParenthesis, tokenMatcher.group(TokenType.closeParenthesis.name())));
                } else if(tokenMatcher.group(TokenType.EOP.name()) != null) {
                    tokens.add(new Token(TokenType.EOP, tokenMatcher.group(TokenType.EOP.name())));
                } else if(tokenMatcher.group(TokenType.Quote.name()) != null) {
                    tokens.add(new Token(TokenType.Quote, tokenMatcher.group(TokenType.Quote.name())));
                } else if(tokenMatcher.group(TokenType.unrecognized.name()) != null) {
                    tokens.add(new Token(TokenType.unrecognized, tokenMatcher.group(TokenType.unrecognized.name())));
                    errorCount++;
                } else {
                    System.out.println("Unrecognized token found."); // Catches other tokens that aren'cst allowed if not in (unrecognized)
                    errorCount++;    
                } 
            }

            // Error if there is no input
            if((input.isEmpty())) { 
                outputArea.append("~ERROR: No input found~\n");
                errorCount++;
            }
            
            // Prints befeore anything else at the top once
            outputArea.append("\nLEXER: Lexing program 1...\n");
            outputArea.append("-----------------------------\n");
            
            // Outputs a stream of tokens from the given input
            for(Token token : tokens) {  
                // When an unrecognized token is found print error message else print the token
                if(token.type == unrecognized) {
                    outputArea.append("LEXER: ERROR: Unrecognized token: " + token.data + " on line " + lineNumber + "\n");
                } else if(token.type == newLine) { // Gets the current token line number and recognizes if new program is lexing
                    lineNumber++;
                } else {
                    outputArea.append("LEXER:" + token + " on line " + lineNumber + "\n"); // Prints out tokens
                }
                
                if(token.type == EOP) {
                    if(errorCount > 0) {
                        outputArea.append("LEXER: Lex completed with " + errorCount + " error(s)\n\n");
                        errorCount = 0; // Reset for next program
                        i++;
                        outputArea.append("\nLEXER: Lexing program " + i + "...\n");
                        outputArea.append("-----------------------------\n");
                    } else {
                        outputArea.append("LEXER: Lex completed successfully.\n\n"); 
                        i++;
                        outputArea.append("\nLEXER: Lexing program " + i + "...\n");
                        outputArea.append("-----------------------------\n");
                    }
                }
            }

            // Spits out a warning when input string does not end with a $ symbol
            if(!input.endsWith("$")) {
                if(input.endsWith("\n")) { // ignores newline and prints results
                    // Prints out total number of errors and warnings at the end of program
                    outputArea.append("Lex completed with:\n [" + warningCount + "] Warning(s) "
                                + "and [" + errorCount + "] Error(s).\n\n"); 
                } else {
                    outputArea.append("LEXER: WARNING: Missing a \"$\"" + " on line " + lineNumber + "\n");
                    outputArea.append("LEXER: Lex completed with mistakes\n\n");
                    warningCount++;
                }
            } else { // Ends with $
                // Prints out total number of errors and warnings at the end of program
                outputArea.append("Lex completed with:\n [" + warningCount + "] Warning(s) "
                                + "and [" + errorCount + "] Error(s).\n\n"); 
            }
        }
    }     
    
    /**
     * 
     * PARSER CLASS
     */
    public class Parser {
        Lexer lex = new Lexer();
        private int currentToken = 0;
        private int i = 1;
        private int lexWarning = 0;
        private int lineCount = 1;
        private int scope = 0;
        private int lexError = 0;
        private int openBraceCount = 0;
        private int closeBraceCount = 0;
        private int printCount = 0;
        private int semanticError = 0;
        private int parseError = 0;
        private int parseWarning = 0;
        private int indexOfTypeAndID = 0;
        private int semanticCount = 0;
        private int idAndLocation = 0;
        TokenType tokenType;
        customCST cst = new customCST();
        customAST ast = new customAST();
        ArrayList<String> charList = new ArrayList<String>();
        ArrayList<String> idList = new ArrayList<String>();  
        ArrayList<String> printList = new ArrayList<String>();
        ArrayList<String> typeList = new ArrayList<String>();
        ArrayList<Integer> scopeList = new ArrayList<Integer>();
        ArrayList<Integer> idLocation = new ArrayList<Integer>();
        ArrayList<String> semanticErrorList = new ArrayList<String>();
        
        //-----------------------------------
        //-----------GETTERS-----------------
        //-----------------------------------
        
        public Lexer getLex() {
            return lex;
        }

        public int getI() {
            return i;
        }

        public int getLexWarning() {
            return lexWarning;
        }

        public int getLineCount() {
            return lineCount;
        }

        public int getScope() {
            return scope;
        }

        public int getLexError() {
            return lexError;
        }

        public int getOpenBraceCount() {
            return openBraceCount;
        }

        public int getCloseBraceCount() {
            return closeBraceCount;
        }

        public int getPrintCount() {
            return printCount;
        }

        public int getSemanticError() {
            return semanticError;
        }

        public int getParseError() {
            return parseError;
        }

        public int getParseWarning() {
            return parseWarning;
        }

        public TokenType getTokenType() {
            return tokenType;
        }

        public customCST getCst() {
            return cst;
        }

        public customAST getAst() {
            return ast;
        }

        public ArrayList<String> getCharList() {
            return charList;
        }

        public ArrayList<String> getIdList() {
            return idList;
        }

        public ArrayList<String> getPrintList() {
            return printList;
        }

        public ArrayList<String> getTypeList() {
            return typeList;
        }

        public ArrayList<Integer> getScopeList() {
            return scopeList;
        }
        
        public JTextArea getAstOutputAreaCodeGen() {
            return astOutputAreaCodeGen;
        }
        
        public Parser() { }
        
        
        //----------------------------------------------------
        //-------------------------[METHODS]------------------
        //----------------------------------------------------
        /**
         * Starts and finishes the parse - will be called through button run
         * @param token
         */
        public Parser(Token token) {
            if(errorCount > 0) {
                CheckForErrors();
            } else {
                outputAreaParser.append("\nPARSER: Parsing program" + i + "...\n");
                outputAreaParser.append("------------------------------------------\n");
                outputAreaParser.append("PARSER: parse()\n");
                outputAreaParser.append("PARSER: parseProgram()\n");

                Program();
            }
        }
        
        public void matchAndDevour(TokenType tokenMatch) {
            if(tokenMatch.equals(tokens.get(currentToken).getType())) {
                System.out.println("current token: " + tokenMatch + "\n"); 
                System.out.println("current token pos: " + currentToken + "\n");
                currentToken++;
            } 
        }
        
        public int getCurrentToken() {
            return currentToken;
        }
        
        private void SemanticErrors() {
            // Checking to see if there are duplicates within the same scope
            for(int iD1 = 0; iD1 < idList.size(); iD1++) {
                for(int iD2 = 0; iD2 < idList.size(); iD2++) {
                    if((iD1 != iD2) && (idList.get(iD1).equals(idList.get(iD2)))) {
                        if(scopeList.get(iD1).equals(scopeList.get(iD2))) {
                            outputAreaSemantics.append("Error: The id " + idList.get(iD1) + " on line " + idLocation.get(iD1) + " is a duplicate\n");
                            semanticError++;
                        }
                    } 
                }
            }
            
            if(semanticCount > 0) {
                semanticError++;
                String result = ""; // CHARLIST is the very first space
                    
                for(String list : semanticErrorList) {  // We loop through the newly created array list of chars
                   result = result + list + ""; // Back "" is the space after every char they are closed to keep chars together  
                } 

                outputAreaSemantics.append(result);
            }            
        }
        
        private void Semantics() {
            outputAreaSemantics.append("\nProgram " + i + " Lexical Analysis\n");
            outputAreaSemantics.append("Program " + i + " Lexical analysis produced " + lexError + " error(s) and " + lexWarning + " warning(s)\n\n");

            outputAreaSemantics.append("Program " + i + " Parsing\n");
            outputAreaSemantics.append("Program " + i + " Parsing produced " + parseError + " error(s) and " + parseWarning + " warning(s)\n\n");

            outputAreaSemantics.append("Program " + i + " Semantic Analysis\n");
            SemanticErrors();
            outputAreaSemantics.append("Program " + i + " Semantic analysis produced " + semanticError + " error(s) and 0 warning(s)\n\n");

            outputAreaSemantics.append("--------------------------------------------------------------------------------------\n\n");
        }
        
        private void SymbolTable() {
            if(semanticError > 1) { // If there is a semantic error skip tree
                    outputAreaSymbolTable.append("\nProgram " + i + " Symbol Table\n");
                    outputAreaSymbolTable.append("not produced due to error(s) detected by \nsemantic analysis.\n\n"); 
                    outputAreaSymbolTable.append("------------------------------------------------------------------\n");
            } else if(idList.isEmpty()) {
                outputAreaSymbolTable.append("\nProgram " + i + " Symbol Table\n");
                outputAreaSymbolTable.append("not produced due to no variable declarations.\n\n"); 
                outputAreaSymbolTable.append("------------------------------------------------------------------\n");
            } else {    
                if(i > 1) { // Separates trees accordingly
                    outputAreaSymbolTable.append("\nProgram " + i + " Symbol Table\n");
                    outputAreaSymbolTable.append("---------------------------------------\n");
                    outputAreaSymbolTable.append("Name Type        Scope  Line\n");
                    outputAreaSymbolTable.append("---------------------------------------\n");
                    
                    String result = ""; // CHARLIST is the very first space
                    
                    for(String list : printList) {  // We loop through the newly created array list of chars
                       result = result + list + ""; // Back "" is the space after every char they are closed to keep chars together  
                    } 
                    
                    outputAreaSymbolTable.append(result);
                    
                } else {
                    outputAreaSymbolTable.append("\nProgram " + i + " Symbol Table\n");
                    outputAreaSymbolTable.append("---------------------------------------\n");
                    outputAreaSymbolTable.append("Name Type        Scope  Line\n");
                    outputAreaSymbolTable.append("---------------------------------------\n");
                    
                    String result = ""; // CHARLIST is the very first space
                    
                    for(String list : printList) {  // We loop through the newly created array list of chars
                       result = result + list + ""; // Back "" is the space after every char they are closed to keep chars together  
                    } 
                    
                    outputAreaSymbolTable.append(result);
                }
            }
        }
        
        private void Trees() {
            if(i > 1) { // Separates trees accordingly
                cstOutputArea.append("\nProgram " + i + " Concrete Syntax Tree\n");
                cstOutputArea.append("-----------------------------\n");
            } else {
                cstOutputArea.append("\nProgram " + i + " Concrete Syntax Tree\n");
                cstOutputArea.append("-----------------------------\n");
            }

            if(i > 1) { // Separates trees accordingly
                astOutputArea.append("\n\nProgram " + i + " Abstract Syntax Tree\n");
                astOutputArea.append("-----------------------------\n");
            } else {
                astOutputArea.append("\n\nProgram " + i + " Abstract Syntax Tree\n");
                astOutputArea.append("-----------------------------\n");
            }
        }
        
        private void TreeErrors() {
            cstOutputArea.append("\nCST for program " + i + ": Skipped due to PARSER error(s).\n");
            cstOutputArea.append("--------------------------------------------------\n");
            astOutputArea.append("\nAST for program " + i + ": Skipped due to PARSER error(s).\n");
            astOutputArea.append("--------------------------------------------------\n");
        }
        
        private void FinishErrors() {
            while(tokens.size() > currentToken) { // Finish off the rest and end the program
                if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                    if(errorCount > 0) {
                        matchAndDevour(tokenType.EOP);
                        TreeErrors();
                        Semantics();
                        SymbolTable();
                        ResetData();
                        Program();
                    } else {
                        matchAndDevour(tokenType.EOP);
                        TreeErrors();
                        Semantics();
                        SymbolTable();
                        ResetData();
                        Program();
                    }
                } else {
                    if(tokens.get(currentToken).getType().equals(tokenType.unrecognized)) { // If theere are more unrecognized errors
                        lexError++;
                        matchAndDevour(tokens.get(currentToken).getType());
                    } else {
                        matchAndDevour(tokens.get(currentToken).getType());
                    }
                }
            }
        }
        
        private void ContinueProgram() {
            if(currentToken < tokens.size()) { // in case Program is not finished
                System.out.println("Program running more than once\n");
                i++;
                outputAreaParser.append("\nPARSER: Parsing program " + i + "...\n");
                outputAreaParser.append("------------------------------------------\n");
                outputAreaParser.append("PARSER: parse()\n");
                outputAreaParser.append("PARSER: parseProgram()\n");

                ResetData(); // Resets all data for next program

                Program(); // when end reached loop back to the top
            }
        }
        
        private void ResetData() {
            cst.restartFamily(); // Clear family tree for next program
                    
            ast.restartFamily(); // Clear family tree for next program

            idList.clear(); // restarts list of ids before new program

            printList.clear(); // restarts list of ids before new program
            
            charList.clear();
            
            typeList.clear();
            
            semanticError = 0;
            
            openBraceCount = 0;
            
            closeBraceCount = 0;
            
            lexError = 0;
            
            parseError = 0;
            
            parseWarning = 0;
            
            lexWarning = 0;
            
            scope = 0;
        }
        
        private void CheckForErrors() {
            if(tokens.get(currentToken).getType().equals(tokenType.unrecognized)) {
                lexError++;
                outputAreaParser.append("PARSER: Skipped due to LEXER error(s)");
                
                FinishErrors(); // Finishes errors and continue program if not finish
            } else if(tokens.get(currentToken).getType().equals(tokenType.EOP)) { // Error EOP
                if((!tokens.get(currentToken - 1).getType().equals(tokenType.closeBracket)) || (!tokens.get(currentToken - 2).getType().equals(tokenType.closeBracket)) || (!tokens.get(currentToken - 3).getType().equals(tokenType.closeBracket))) { // Error case
                    parseError++;
                    outputAreaParser.append("PARSER: ERROR: Got [" + tokens.get(currentToken).getType() + "] expected [" + tokens.get(currentToken - 1).getType() + "] on line " + lineNumber + "\n");
                    outputAreaParser.append("PARSER: Lexer failed with " + parseError + " error\n\n");
                    FinishErrors(); // Finishes errors and continue program if not finish
                }
            } else {
                parseError++;
                outputAreaParser.append("PARSER: ERROR: Got [" + tokens.get(currentToken).getType() + "] expected [" + tokens.get(currentToken - 1).getType() + "] on line " + lineNumber + "\n");
                outputAreaParser.append("PARSER: Parse failed with " + parseError + " error\n\n");
                FinishErrors(); // Finishes errors and continue program if not finish
            }
        }
        
        
        /**
         * 
         * Program       ::== Block $
         */        
        private void Program() {
            if(tokens.get(currentToken).getType().equals(tokenType.EOP)) { // In case end comes sooner than expected
                // Error case when parser finishes with uneven number of '{' and '}'
                if(closeBraceCount != openBraceCount) {
                    parseError++;
                    outputAreaParser.append("PARSER: ERROR: Expected [" + tokens.get(currentToken).getType() + "] got [" + tokens.get(currentToken - 1).getType() + "] on line " + lineNumber + "\n");
                    outputAreaParser.append("PARSER: Parse failed with " + parseError + " error\n\n"); // incase of dupilicates (Block())
                    matchAndDevour(tokenType.EOP); 
                    System.out.println("matched $\n");
                    
                    TreeErrors();
                    Semantics();
                    SymbolTable();
                    System.out.println("scope: " + scope);
                    System.out.println("openBracketCount: " + openBraceCount);
                    System.out.println("closeBracketCount: " + closeBraceCount);
                    
                    ContinueProgram(); // If program not done
                } else { // Program found no bracket errors or parse errors - finish parse and cst 
                    Semantics();
                    SymbolTable();
                    
                    // loops the $ node to match the Block branch
                    cst.scaleToRoot();
                    
                    // Adding EOP leaf Node
                    cst.addNode("$", "leaf");
                    Trees();
                    
                    System.out.println("scope: " + scope);
                    System.out.println("openBracketCount: " + openBraceCount);
                    System.out.println("openBracketCount: " + openBraceCount);
                    
                    cstOutputArea.append(cst.toString());
                    
                    astOutputArea.append(ast.toString());
                    
                    matchAndDevour(tokenType.EOP); 
                    System.out.println("matched $\n");
                    
                    outputAreaParser.append("PARSER: Parse completed successfully\n\n");
                    
                    Assembler assembler = new Assembler(this);
                    assembler.gatherAndGenerate();
                    
                    ContinueProgram(); // If program not done
                }  
            } else if(tokens.get(currentToken).getType().equals(tokenType.unrecognized)) { // In case end comes sooner than expected
                FinishErrors(); // Finishes errors and continue program if not finish
            } else if(parseError > 0) { // In case end comes sooner than expected
                FinishErrors();    
            } else {
                
                // Adding the root node
                cst.addNode("Program", "branch");
                
                // Adds the block Node to the tree
                cst.addNode("Block", "branch");
                
                ast.addNode("Program", "branch");
                
                // Adds the block Node to the tree
                ast.addNode("Block", "branch");
                
                Block();
            }
        }
        
        
        /**
         * 
         * Block     ::== { StatementList }
         */        
        private void Block() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) {
                openBraceCount++;
                if(scope > 0) { // If new block is created with while or if
                    System.out.print("scope: " + scope);
                    System.out.print("i: " + i);
                    cst.statementListIncrement(); // Matching last StatementList before new open bracket
                    
                    // Adds Statement List branch to tree
                    cst.addNode("Statement List", "branch");
                
                    // Adds Statement branch to tree
                    cst.addNode("Statement", "branch");
                    
                    // Starts the new block within a block
                    cst.addNode("Block", "branch");
                    
                    // Adds the block Node to the ast tree
                    ast.addNode("Block", "branch"); 
                    
                    scope++;
                }
                    
                //Creates the leaf node of Block {
                cst.addNode("{", "leaf");
                
                matchAndDevour(tokenType.openBracket);
                
                outputAreaParser.append("PARSER: parseBlock()\n");
                System.out.println("matched {\n");
                
                StatementList();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.closeBracket)) { 
                closeBraceCount++;
                scope--;  
                cst.scaleToBlock();
                
                
                //Creates the leaf node of Block }
                cst.addNode("}", "leaf");
                
                matchAndDevour(tokenType.closeBracket);
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: }\n");
                
                
                // When looping of } finishes there should be a $
                if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                    System.out.println("mmmmm");
                    Program(); // Goes to program to finish program
                } else {
                    Block(); // Continue if there are more programs
                }
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                matchAndDevour(tokenType.newLine);
                System.out.println("matched: \n");
                if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // incase of dupilicates (Block())
                    openBraceCount++;
                    scope++;
                    //Creates the leaf node of Block { (Incase of this case)
                    cst.addNode("{", "leaf");
                    
                    matchAndDevour(tokenType.openBracket);
                    
                    outputAreaParser.append("PARSER: parseBlock()\n");
                    System.out.println("matched: {\n");
                    
                    StatementList();
                } else if(tokens.get(currentToken).getType().equals(tokenType.closeBracket)) { // incase of dupilicates (Block())
                    Block(); // Goes back into block to find finishing touches
                } else if(tokens.get(currentToken).getType().equals(tokenType.EOP)) { // When looping of } finishes there should be a $
                    Program(); // Goes to program to finish program
                } else {
                    StatementList(); // More to be done...
                }    
            } else {
                if(!tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // Avoid runtime error
                    parseError++;
                    outputAreaParser.append("PARSER: Parser failed with " + parseError + " error on line " + lineNumber + "\n\n");
                    Program();
                } else if(!tokens.get(currentToken).getType().equals(tokenType.closeBracket)) { // Avoid runtime error
                    parseError++;
                    outputAreaParser.append("PARSER: Parser failed with " + parseError + " error on line " + lineNumber + "\n\n");
                    Program();
                } else {
                    CheckForErrors();
                }
            }              
        }
        
        
        /**
         * 
         * StatementList ::== Statement StatementList
         *               ::== ε <-- (empty set)
         */
        private void StatementList() {   
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.printStatement)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: print\n");
                Statement();
            
            } else if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: ID\n");
                Statement(); 
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeInt)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: int\n");
                Statement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeString)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: string\n");
                Statement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeBoolean)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: booleant\n");
                Statement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.ifStatement)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: if\n");
                Statement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.whileStatement)) {
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                System.out.println("matched: while\n");
                Statement();
             
            } else if(tokens.get(currentToken).getType().equals(tokenType.closeBracket)) {
                // IF THE INPUT IS AN EMPTY SET ---> ε
                if(tokens.get(currentToken - 1).getType().equals(tokenType.openBracket)) { // last } in an empty condition on same line
                    closeBraceCount++;
                    scope--;
                    // Adds Statement List branch to tree
                    cst.addNode("Statement List", "branch");
                    cst.endChildren();
                    
                    //Creates the leaf node of Block }
                    cst.addNode("}", "leaf");
                    
                    matchAndDevour(tokenType.closeBracket);
                    
                    outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                    System.out.println("matched: }\n");
                    
                    // If EOP is found
                    if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                        Program(); // Goes to program to finish program and continue if there are more programs
                    } else {
                        StatementList(); // If this not the only }
                    }
                } else if(tokens.get(currentToken - 1).getType().equals(tokenType.closeBracket)) { // repeating } for ending conditions on the one line
                    closeBraceCount++;
                    
                    scope--;
                    
                    if(scope == 0) {
                        for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                            cst.scaleToBlock();
                        }      
                    } else if(scope == -1) {
                       for(int scaleBlock = 0; scaleBlock < closeBraceCount - 2; scaleBlock++) {
                            cst.scaleToBlock();
                        }       
                    } else {    
                        cst.scaleToBlock();
                    }
                    
                    //Creates the leaf node of Block }
                    cst.addNode("}", "leaf");
                    
                    matchAndDevour(tokenType.closeBracket);
                    
                    outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                    System.out.println("matched: }\n");
                    
                    // If EOP is found
                    if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                        Program(); // Goes to program to finish program and continue if there are more programs
                    } else {
                        StatementList(); // If this not the only }
                    }
                } else if(tokens.get(currentToken - 1).getType().equals(tokenType.closeParenthesis)) { // For ending print statements within conditions on one line
                        closeBraceCount++;
                        scope--;
                        // Adds Statement List branch to tree
                        cst.addNode("Statement List", "branch"); // last statement list
                        if(scope == 0) {
                            for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }      
                        } else if(scope == -1) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount - 2; scaleBlock++) {
                                cst.scaleToBlock();
                            }       
                        } else {    
                            cst.scaleToBlock();
                        } 

                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");

                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");
                        
                        
                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        }
                } else if(tokens.get(currentToken - 1).getType().equals(tokenType.digit)) { // For ending with an assignment integer on one line
                    closeBraceCount++;
                    scope--;
                    // Adds Statement List branch to tree
                    cst.addNode("Statement List", "branch"); // last statement list
                    if(scope == 0) {
                        for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                            cst.scaleToBlock();
                        }      
                    } else if(scope == -1) {
                       for(int scaleBlock = 0; scaleBlock < closeBraceCount - 2; scaleBlock++) {
                            cst.scaleToBlock();
                        }       
                    } else {    
                        cst.scaleToBlock();
                    }

                    //Creates the leaf node of Block }
                    cst.addNode("}", "leaf");

                    matchAndDevour(tokenType.closeBracket);

                    outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                    System.out.println("matched: }\n");

                    // If EOP is found
                    if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                        Program(); // Goes to program to finish program and continue if there are more programs
                    } else {
                        StatementList(); // If this not the only }
                    }
                } else if(tokens.get(currentToken - 1).getType().equals(tokenType.CHAR)) { // For ending with an assignment, which was a CHAR on one line
                    closeBraceCount++;
                    scope--;
                    // Adds Statement List branch to tree
                    cst.addNode("Statement List", "branch"); // last statement list
                    if(scope == 0) {
                        for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                            cst.scaleToBlock();
                        }      
                    } else if(scope == -1 && closeBraceCount <= 2) {
                       for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                            cst.scaleToBlock();
                        }
                    } else if(scope == -1 && closeBraceCount < 2) {
                       for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                            cst.scaleToBlock();
                        }
                    } else {    
                        cst.scaleToBlock();
                    }

                    //Creates the leaf node of Block }
                    cst.addNode("}", "leaf");

                    matchAndDevour(tokenType.closeBracket);

                    outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                    System.out.println("matched: }\n");

                    // If EOP is found
                    if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                        Program(); // Goes to program to finish program and continue if there are more programs
                    } else {
                        StatementList(); // If this not the only }
                    }
                } else if(tokens.get(currentToken - 1).getType().equals(tokenType.newLine)) { // newline cases
                    if(tokens.get(currentToken - 2).getType().equals(tokenType.closeBracket)) { // repeating } for ending conditions on new line
                        closeBraceCount++;
                        scope--;
                           
                        cst.scaleToBlock();
                        

                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");
                        
                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");
                        
                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        }
                    } else if(tokens.get(currentToken - 2).getType().equals(tokenType.closeParenthesis)) { // For ending print statements within conditions
                        closeBraceCount++;
                        scope--;
                        // Adds Statement List branch to tree
                        cst.addNode("Statement List", "branch"); // last statement list
                        
                        if(scope == 0) {
                            for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }      
                        } else if(scope == -1 && closeBraceCount <= 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else if(scope == -1 && closeBraceCount < 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else {    
                            cst.scaleToBlock();
                        }
                        
                        //Creates the leaf node of Block }s
                        cst.addNode("}", "leaf");
                        
                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");

                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        } 
                    } else if(tokens.get(currentToken - 2).getType().equals(tokenType.digit)) { // For ending with an assignment, which was an int on two lines
                        closeBraceCount++;
                        scope--;
                        // Adds Statement List branch to tree
                        cst.addNode("Statement List", "branch"); // last statement list
                         if(scope == 0) {
                            for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }      
                        } else if(scope == -1 && closeBraceCount <= 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else if(scope == -1 && closeBraceCount < 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else {    
                            cst.scaleToBlock();
                        }
                        
                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");
                        
                        
                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");

                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        }
                    } else if(tokens.get(currentToken - 2).getType().equals(tokenType.CHAR)) { // For ending with an assignment, which was a CHAR on two lines
                        closeBraceCount++;
                        scope--;
                        // Adds Statement List branch to tree
                        cst.addNode("Statement List", "branch"); // last statement list
                        if(scope == 0) {
                            for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }      
                        } else if(scope == -1 && closeBraceCount <= 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else if(scope == -1 && closeBraceCount < 2) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }
                        } else {    
                            cst.scaleToBlock();
                        }
                        
                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");
                        
                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");

                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        } 
                    } else if(tokens.get(currentToken - 2).getType().equals(tokenType.openBracket)) { // last } in an empty condition has new line
                        closeBraceCount++;
                        scope--;
                        cst.addNode("Statement List", "branch");
                        cst.endChildren();

                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");

                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");

                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        }
                    } else { // error
                        closeBraceCount++;

                        scope--;
                        // Adds Statement List branch to tree
                        cst.addNode("Statement List", "branch"); // last statement list
                        
                        if(scope == 0) {
                            for(int scaleBlock = 0; scaleBlock < closeBraceCount; scaleBlock++) {
                                cst.scaleToBlock();
                            }      
                        } else if(scope == -1) {
                           for(int scaleBlock = 0; scaleBlock < closeBraceCount - 2; scaleBlock++) {
                                cst.scaleToBlock();
                            }       
                        } else {    
                            cst.scaleToBlock();
                        }
                        
                        //Creates the leaf node of Block }
                        cst.addNode("}", "leaf");

                        matchAndDevour(tokenType.closeBracket);
                        
                        outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                        System.out.println("matched: }\n");
                        
                        // If EOP is found
                        if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                            Program(); // Goes to program to finish program and continue if there are more programs
                        } else {
                            StatementList(); // If this not the only }
                        } 
                    }
                } else { // error                   
                    CheckForErrors();
                }
            } else if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // incase of dupilicates (Block())
                openBraceCount++;
                scope++;
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds the block Node to the tree
                cst.addNode("Block", "branch");
                
                //Creates the leaf node of Block {
                cst.addNode("{", "leaf"); 
                
                ast.addNode("Block", "branch");
                
                matchAndDevour(tokenType.openBracket);
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                outputAreaParser.append("PARSER: parseStatement()\n");
                outputAreaParser.append("PARSER: parseBlock()\n");
                System.out.println("matched: {\n");
                
                Statement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                matchAndDevour(tokenType.newLine);
                System.out.println("matched: \n");
                StatementList(); // loops to next section when end reached loop back to the top
            } else if(tokens.get(currentToken).getType().equals(tokenType.EOP)) { // Accounting for an early stop
                Program(); // loops back to the top
                  
            } else {                
                CheckForErrors();
            }   
        }
        
        
        /**
         * 
         * Statement ::== PrintStatement        ::== print ( Expr )
         *           ::== AssignmentStatement   ::== Id = Expr
         *           ::== VarDecl               ::== type Id
         *           ::== WhileStatement        ::== while BooleanExpr Block
         *           ::== IfStatement           ::== if BooleanExpr Block
         *           ::== Block                 ::== Program
         */ 
        private void Statement() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.printStatement)) {
                printCount++;
                System.out.println("printCount: " + printCount);
                System.out.println("scope: " + scope);
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds Print Statement branch to tree
                cst.addNode("Print Statement", "branch");
                
                // Adds print statement as a branch to AST
                ast.addNode("Print Statement", "branch");
                
                matchAndDevour(tokenType.printStatement);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: print\n");
                PrintStatement();
            
            } else if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
               
                // Adds Assignment Statement branch to tree
                cst.addNode("Assignment Statement", "branch");
                
                // Displays what the ID is in this assignment statement
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                // Adds Variable Declaration as a branch to AST
                ast.addNode("Assignment Statement", "branch");
                
                // Displays what the ID is in this assignment statement
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                if(!idList.contains(tokens.get(currentToken).getData())) {
                    semanticCount++;
                    semanticErrorList.add("Error: The id " + tokens.get(currentToken).getData() + " on line " + lineCount + " was used before being declared\n");
                }
                
                
                matchAndDevour(tokenType.CHAR);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: ID\n");
                AssignmentStatement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeInt)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds Variable Declaration branch to tree
                cst.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                cst.addNode("int", "leaf");
                
                // Adds Variable Declaration as a branch to AST
                ast.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.typeInt);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: int\n");
                VarDecl();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeString)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
               
                // Adds Variable Declaration branch to tree
                cst.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                cst.addNode("string", "leaf");
                
                // Adds Variable Declaration as a branch to AST
                ast.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.typeString);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: string\n");
                VarDecl();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.typeBoolean)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds Variable Declaration branch to tree
                cst.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                cst.addNode("boolean", "leaf");
                
                // Adds Variable Declaration as a branch to AST
                ast.addNode("Variable Declaration", "branch");
                
                // Adding the specific type to the VarDecl branch
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.typeBoolean);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: boolean\n");
                VarDecl();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.ifStatement)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds If Statement branch to tree
                cst.addNode("If Statement", "branch");
                
                // Adds If Statement branch to the ast tree
                ast.addNode("If Statement", "branch");
                
                matchAndDevour(tokenType.ifStatement);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: if\n");
                IfStatement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.whileStatement)) {
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");
                
                // Adds While Statement branch to tree
                cst.addNode("While Statement", "branch");
                
                // Adds While Statement branch to the ast tree
                ast.addNode("While Statement", "branch");
                
                matchAndDevour(tokenType.whileStatement);
                outputAreaParser.append("PARSER: parseStatement()\n");
                System.out.println("matched: while\n");
                WhileStatement();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.closeBracket)) {
                closeBraceCount++;
                scope--;
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                cst.endChildren();
                
                //Creates the leaf node of Block }
                cst.addNode("}", "leaf"); 
                
                matchAndDevour(tokenType.closeBracket);
                
                outputAreaParser.append("PARSER: parseStatementList()\n"); // incase of dupilicates (Block())
                System.out.println("matched: }\n");
                
                // If EOP is found
                if(tokens.get(currentToken).getType().equals(tokenType.EOP)) {
                    Program(); // Goes to program to finish program and continue if there are more programs
                } else if(tokens.get(currentToken).getType().equals(tokenType.closeBracket)) {
                    Block(); // If there are a repeating number of } Block will loop them
                } else {
                    StatementList();
                }
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // incase of dupilicates (Block())
                openBraceCount++;
                
                scope++;
                // Adds Statement List branch to tree
                cst.addNode("Statement List", "branch");
                
                // Adds Statement branch to tree
                cst.addNode("Statement", "branch");

                // Adds the block Node to the tree
                cst.addNode("Block", "branch");
                
                //Creates the leaf node of Block {
                cst.addNode("{", "leaf"); 
                
                matchAndDevour(tokenType.openBracket);
                
                outputAreaParser.append("PARSER: parseStatementList()\n");
                outputAreaParser.append("PARSER: parseStatement()\n");
                outputAreaParser.append("PARSER: parseBlock()\n");
                System.out.println("matched: {\n");                
                
                StatementList(); // Considered as Block() loops back to begining to find possible $
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                matchAndDevour(tokenType.newLine);
                System.out.println("matched: \n");
                StatementList(); // loops to next section when end reached loop back to the top
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.EOP)) { // In case end comes sooner than expected
                Program(); // Goes to program to finish program and continue if there are more programs
            } else {
                CheckForErrors();
            }
        }
        
        
        //---------------------STATEMENTS---------------------------------------
        /**
         * 
         * Statement ::== PrintStatement   ::== print ( Expr )
         */ 
        private void PrintStatement() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.openParenthesis)) {
                //Creates the leaf node print
                cst.addNode("print", "leaf"); 
                
                //Creates the leaf node openParen
                cst.addNode("(", "leaf");
                
                matchAndDevour(tokenType.openParenthesis);
                outputAreaParser.append("PARSER: parsePrintStatement()\n");
                outputAreaParser.append("PARSER: parseOpenParethesis()\n");
                Expr();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {
                // Match first then see whats next
                matchAndDevour(tokenType.closeParenthesis);
                outputAreaParser.append("PARSER: parseCloseParenthesis()\n");
                
                // If next token continues a boolean expressions -- Finish the Boolean Expression
                if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {
                    // Aligns first close paren in boolean expression
                    cst.scaleToBooleanExpression();

                    // Creates the leaf node closeParen
                    cst.addNode(")", "leaf");
                    
                    PrintStatement(); // Finish Print Statement
                } else if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // For If and While statements or single line
                    // Lines close parenthesis to open parenthesis within print statement
                    cst.scaleToCondition();

                    // Creates the leaf node closeParen
                    cst.addNode(")", "leaf");
                    ast.scaleToBlock();
                    scope++; // New block
                    StatementList(); // Continue code
                    
                } else {
                    // Lines close parenthesis to open parenthesis within print statement
                    cst.scaleToPrintStatement();

                    // Creates the leaf node closeParen
                    cst.addNode(")", "leaf");
                    
                    // Increments branch correctly
                    cst.statementListIncrement();
                    
                    ast.scaleToBlock(); // Alsigns AST parent to its current Block

                    StatementList(); // If there are more statements left
                }
            } else if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // In case of new line
                matchAndDevour(tokenType.newLine);
                System.out.println("matched: \n");
                PrintStatement(); // Loop back into statement to finish or continue properly
            } else {
                CheckForErrors();
            }    
        }
        
        
        /**
         * 
         * Statement ::== AssignmentStatement   ::== Id = Expr
         */ 
        private void AssignmentStatement() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.assignmentStatement)) { // Checking for CHARS
                // Allows me to get the current = and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.assignmentStatement);
                outputAreaParser.append("PARSER: parseAssignmentStatement()\n"); // incase of dupilicates (Block())
                outputAreaParser.append("PARSER: parseAssignment()\n"); // Assignment symbol is valid
                Expr(); // Gets Expr
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Checking for a new line
                cst.statementListIncrement(); // Attaches to previous Statement List
                
                ast.scaleToBlock(); // Alsigns AST parent to its current Block
                
                StatementList(); // Check to see if there are more statement lists
            } else {
                CheckForErrors();
           }
        }
        
        
        /**
         * 
         * Statement ::== VarDecl    ::== type Id
         */ 
        private void VarDecl() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) { // Checking for CHARS
                // Allows me to get the current CHAR and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
               
                // Allows me to get the current ID (char) and add to the ast
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                // keep track of ID and Type positions
                indexOfTypeAndID++;
                
                scopeList.add(scope); // Add scope to scopeList
                idList.add(tokens.get(currentToken).getData()); // Add char to ID list
                
                idLocation.add(lineCount);
                
                System.out.println(idList); 
                System.out.println(scopeList);
                
                if(tokens.get(currentToken -1).getType().equals(tokenType.typeInt)) {
                   typeList.add(tokens.get(currentToken -1).getData()); // Type Int
                   printList.add(tokens.get(currentToken).getData() + "          " + tokens.get(currentToken - 1).getData() + "           " + scope + "         " + lineCount+ "\n");
                } else if(tokens.get(currentToken -1).getType().equals(tokenType.typeString)) {
                    typeList.add(tokens.get(currentToken -1).getData()); // Type String
                    printList.add(tokens.get(currentToken).getData() + "          " + tokens.get(currentToken - 1).getData() + "      " + scope + "         " + lineCount + "\n");
                } else if(tokens.get(currentToken -1).getType().equals(tokenType.typeBoolean)) {
                    typeList.add(tokens.get(currentToken -1).getData()); // Type Boolean
                    printList.add(tokens.get(currentToken).getData() + "          " + tokens.get(currentToken - 1).getData() + "  " + scope + "         " + lineCount + "\n");
                } 
                
               
                matchAndDevour(tokenType.CHAR);
                outputAreaParser.append("PARSER: parseVarDecl()\n"); // VarDecl is valid
                outputAreaParser.append("PARSER: parseType()\n"); // ID is valid
                outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
               
                // Attaches to previous Statement List
                cst.statementListIncrement();
                
                // Aligns branch to its block
                ast.scaleToBlock();
                
                StatementList();
            } else {
                CheckForErrors();
            }
        }
        
        
        /**
         * 
         * Statement ::== WhileStatement   ::== while BooleanExpr Block
         */ 
        private void WhileStatement() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            if(tokens.get(currentToken).getType().equals(tokenType.openParenthesis)) { // Checking for openParenthesis
                // Adds while to node as leaf
                cst.addNode("while", "leaf");
                
                outputAreaParser.append("PARSER: WhileStatement()\n"); // While is valid
                BooleanExpr();
            } else {
                CheckForErrors(); 
            } 
        }
        
        
        /**
         * 
         * Statement ::== IfStatement     ::== if BooleanExpr Block
         */ 
        private void IfStatement() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.openParenthesis)) { // Checking for openParenthesis
                // Adds if to node as leaf
                cst.addNode("if", "leaf");
                
                outputAreaParser.append("PARSER: IfStatement()\n"); // IF is valid
                BooleanExpr();
            } else {
                CheckForErrors(); 
            }
        }
        
        
        //------------------EXPRESSIONS------------------------------------------
        /**
         * 
         * Expr ::== IntExpr        ::== digit intopExpr, digit
         *      ::== StringExpr     ::== " CharList "
         *      ::== BooleanExpr    ::== ( Expr boolop Expr ), boolval
         *      ::== ID             ::== char
         */
        private void Expr() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.digit)) { // Checking for digits
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds Integer Expression branch to tree
                cst.addNode("Integer Expression", "branch");
                
                outputAreaParser.append("PARSER: parseExpr()\n");
                IntExpr(); // If its a digit we will see if its valid IntExpr
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) { // Checking for CHARS 
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds ID branch to tree
                cst.addNode("ID", "branch");
                
                outputAreaParser.append("PARSER: parseExpr()\n"); 
                ID();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.openParenthesis)) { // Checking for openParenthesis 
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds BooleanExpr branch to tree
                cst.addNode("Boolean Expression", "branch");
                
                outputAreaParser.append("PARSER: parseExpr()\n"); 
                BooleanExpr();
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.boolvalTrue)) { // Checking for boolval 
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds Boolean Expression branch to tree
                cst.addNode("Boolean Expression", "branch");
                
                // Allows me to get the current boolval and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                // Allows me to get the current boolval and add to the ast
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                outputAreaParser.append("PARSER: parseExpr()\n");
                matchAndDevour(tokenType.boolvalTrue);
                outputAreaParser.append("PARSER: parseBoolvalTrue()\n");
                
                if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) { 
                    PrintStatement(); // Checking for closeParenthesis in order to finish print statement
                    
                } else {
                    cst.statementListIncrement(); // Attaches to previous Statement List
                    ast.scaleToBlock(); // Aligns AST parent to its current Block
                    StatementList();
                } 
            } else if(tokens.get(currentToken).getType().equals(tokenType.boolvalFalse)) { // Checking for boolval 
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds Integer Expression branch to tree
                cst.addNode("Boolean Expression", "branch");
                
                // Allows me to get the current boolval and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                // Allows me to get the current boolval and add to the ast
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                outputAreaParser.append("PARSER: parseExpr()\n");
                matchAndDevour(tokenType.boolvalFalse);
                outputAreaParser.append("PARSER: parseBoolvalFalse()\n");
                
                if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) { 
                    PrintStatement(); // Checking for closeParenthesis in order to finish print statement
                    
                } else {
                    cst.statementListIncrement(); // Attaches to previous Statement List
                    ast.scaleToBlock(); // Aligns AST parent to its current Block
                    StatementList();
                } 
            } else if(tokens.get(currentToken).getType().equals(tokenType.Quote)) { // Checking for Quotes 
                // Adds Expression branch to tree
                cst.addNode("Expression", "branch");
                
                // Adds String Expression branch to tree
                cst.addNode("String Expression", "branch");
                
                outputAreaParser.append("PARSER: parseExpr()\n"); 
                StringExpr();
            } else {
                CheckForErrors();
            }
        }
        
        
        /**
         * 
         * Expr ::== IntExpr   ::== digit intopExpr, digit
         */
        private void IntExpr() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.digit)) { // Checking for digits
                // Allows me to get the current digit and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                // Allows me to get the current digit and add to the ast
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.digit);
                outputAreaParser.append("PARSER: parseIntExpr()\n"); // IntExpr is valid
                outputAreaParser.append("PARSER: parseDigit()\n");
                System.out.println("matched: Digit\n");
                
                if(tokens.get(currentToken).getType().equals(tokenType.intopAddition)) { // Checking for intop   
                    // Allows me to get the current intop and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf");
                    
                    // If there is an addition symbol in the statement
                    ast.addNode("Plus", "branch");
                    
                    matchAndDevour(tokenType.intopAddition);
                    outputAreaParser.append("PARSER: parseIntop()\n");
                    System.out.println("matched: Intop\n");
                
                    if(tokens.get(currentToken).getType().equals(tokenType.digit)) { // Checking for digits
                        // Allows me to get the current digit and add to node as leaf
                        cst.addNode(tokens.get(currentToken).getData(), "leaf");
                        
                        // Allows me to get the current digit and add to the ast
                        ast.addNode(tokens.get(currentToken).getData(), "leaf");
                        
                        matchAndDevour(tokenType.digit);
                        outputAreaParser.append("PARSER: parseDigit()\n");
                        
                        if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                            BooleanExpr(); // continues the BooleanExpr
                        
                        } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                            BooleanExpr(); // continues the BooleanExpr
                            
                        } else if(tokens.get(currentToken).getType().equals(tokenType.intopAddition)) {                    
                            // Allows me to get the current intop and add to node as leaf
                            cst.addNode(tokens.get(currentToken).getData(), "leaf");

                            // If there is an addition symbol in the statement
                            ast.addNode("Plus", "branch");

                            matchAndDevour(tokenType.intopAddition);
                            outputAreaParser.append("PARSER: parseIntop()\n");
                            System.out.println("matched: Intop\n");
                            
                            IntExpr();
                        
                        } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                            PrintStatement(); // Loop back to PrintStatement 
                        
                        } else { // New Lines and 
                            cst.statementListIncrement(); // Attaches to previous Statement List
                            ast.scaleToBlock(); // Aligns AST parent to its current Block
                            StatementList();
                        }
                    } else if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) {
                        if(!idList.contains(tokens.get(currentToken).getData())) {
                            semanticCount++;
                            semanticErrorList.add("Error: The id " + tokens.get(currentToken).getData() + " on line " + lineCount + " was used before being declared\n");
                            
                            // Allows me to get the String of current CHAR and add to node as leaf
                            cst.addNode(tokens.get(currentToken).getData(), "leaf"); 
                        
                            // Allows me to get the current ID (char) and add to the ast
                            ast.addNode(tokens.get(currentToken).getData(), "leaf");
                            
                            matchAndDevour(tokenType.CHAR);
                            outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
                            outputAreaParser.append("PARSER: parseCHAR()\n");
                            System.out.println("matched: CHAR\n");
                            if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                                BooleanExpr(); // continues the BooleanExpr
                        
                            } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                                BooleanExpr(); // continues the BooleanExpr
                                
                            } else if(tokens.get(currentToken).getType().equals(tokenType.intopAddition)) {                    
                                // Allows me to get the current intop and add to node as leaf
                                cst.addNode(tokens.get(currentToken).getData(), "leaf");

                                // If there is an addition symbol in the statement
                                ast.addNode("Plus", "branch");
                                
                                matchAndDevour(tokenType.intopAddition);
                                outputAreaParser.append("PARSER: parseIntop()\n");
                                System.out.println("matched: Intop\n");
                        
                                IntExpr();
                                
                            } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                                PrintStatement(); // Loop back to PrintStatement 

                            } else {
                                AssignmentStatement(); // Go finish AssignmentStatement
                            }
                        } else if(idList.contains(tokens.get(currentToken).getData())) {
                            // Allows me to get the String of current CHAR and add to node as leaf
                            cst.addNode(tokens.get(currentToken).getData(), "leaf"); 
                
                            // Allows me to get the current ID (char) and add to the ast
                            ast.addNode(tokens.get(currentToken).getData(), "leaf");
                            
                            matchAndDevour(tokenType.CHAR);
                            outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
                            outputAreaParser.append("PARSER: parseCHAR()\n");
                            System.out.println("matched: CHAR\n");
                            
                            if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                                BooleanExpr(); // continues the BooleanExpr
                        
                            } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                                BooleanExpr(); // continues the BooleanExpr

                            } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                                PrintStatement(); // Loop back to PrintStatement 

                            } else {
                                AssignmentStatement(); // Go finish AssignmentStatement
                            }
                        } else {
                            CheckForErrors();
                        } 
                    } else {
                        CheckForErrors();
                    }
                } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                    PrintStatement(); // Loop back to PrintStatement
                     
                } else if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                    BooleanExpr(); // continues the BooleanExpr
                
                } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                    BooleanExpr(); // continues the BooleanExpr
                    
                } else {
                    cst.statementListIncrement(); // Attaches to previous Statement List
                    ast.scaleToBlock(); // Aligns AST parent to its current Block
                    StatementList();
                }
            } else if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) {
                if(!idList.contains(tokens.get(currentToken).getData())) {
                    semanticCount++;
                    semanticErrorList.add("Error: The id " + tokens.get(currentToken).getData() + " on line " + lineCount + " was used before being declared\n");

                    // Allows me to get the String of current CHAR and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf"); 

                    // Allows me to get the current ID (char) and add to the ast
                    ast.addNode(tokens.get(currentToken).getData(), "leaf");

                    matchAndDevour(tokenType.CHAR);
                    outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
                    outputAreaParser.append("PARSER: parseCHAR()\n");
                    System.out.println("matched: CHAR\n");
                    if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                        BooleanExpr(); // continues the BooleanExpr

                    } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                        BooleanExpr(); // continues the BooleanExpr

                    } else if(tokens.get(currentToken).getType().equals(tokenType.intopAddition)) {                    
                        // Allows me to get the current intop and add to node as leaf
                        cst.addNode(tokens.get(currentToken).getData(), "leaf");

                        // If there is an addition symbol in the statement
                        ast.addNode("Plus", "branch");

                        matchAndDevour(tokenType.intopAddition);
                        outputAreaParser.append("PARSER: parseIntop()\n");
                        System.out.println("matched: Intop\n");

                        IntExpr();

                    } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                        PrintStatement(); // Loop back to PrintStatement 

                    } else {
                        AssignmentStatement(); // Go finish AssignmentStatement
                    }
                } else if(idList.contains(tokens.get(currentToken).getData())) {
                    // Allows me to get the String of current CHAR and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf"); 

                    // Allows me to get the current ID (char) and add to the ast
                    ast.addNode(tokens.get(currentToken).getData(), "leaf");

                    matchAndDevour(tokenType.CHAR);
                    outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
                    outputAreaParser.append("PARSER: parseCHAR()\n");
                    System.out.println("matched: CHAR\n");

                    if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                        BooleanExpr(); // continues the BooleanExpr

                    } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { //Checking for BOOLOP
                        BooleanExpr(); // continues the BooleanExpr
                    
                    } else if(tokens.get(currentToken).getType().equals(tokenType.intopAddition)) {                    
                        // Allows me to get the current intop and add to node as leaf
                        cst.addNode(tokens.get(currentToken).getData(), "leaf");

                        // If there is an addition symbol in the statement
                        ast.addNode("Plus", "branch");

                        matchAndDevour(tokenType.intopAddition);
                        outputAreaParser.append("PARSER: parseIntop()\n");
                        System.out.println("matched: Intop\n");

                        IntExpr();
                        
                    } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                        PrintStatement(); // Loop back to PrintStatement 

                    } else {
                        AssignmentStatement(); // Go finish AssignmentStatement
                    }
                } else {
                    CheckForErrors();
                }
            } else { // If IntExpr doesn'cst start with a digit
                CheckForErrors();
            }
        }
        
        
        /**
         * 
         * Expr ::== StringExpr    ::== " CharList "
         */
        private void StringExpr() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.Quote)) { // Checking for Quotes
                
                // Allows me to get the current quote and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.Quote);
                outputAreaParser.append("PARSER: parseStringExpr()\n");
                outputAreaParser.append("PARSER: parseQuote()\n");
                 

                // Clears last list for new char list * Avoids duplication *
                charList.clear();

                while(tokens.get(currentToken).getType().equals(tokenType.CHAR)) { // Loops through charlist
                    // Allows me to get the current CHAR and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf");

                    // Adds  branch to tree
                    cst.addNode("Char List", "branch");

                    // Adds current char to new arraylist for ast disply *Helps with AssignmentStatement ambiguity*
                    charList.add(tokens.get(currentToken).getData()); 

                    matchAndDevour(tokenType.CHAR);
                    outputAreaParser.append("PARSER: parseCHAR()\n");
                }
                
                
                if(tokens.get(currentToken).getType().equals(tokenType.Quote)) {
                    
                    if(tokens.get(currentToken - 1).getType().equals(tokenType.Quote)) { // Last token was a quote
                        // Allows me to get the current quote and add to node as leaf
                        cst.addNode(tokens.get(currentToken).getData(), "leaf");
                        
                        matchAndDevour(tokenType.Quote);
                        outputAreaParser.append("PARSER: parseQuote()\n");
                        
                        // Next Token has to be a closed parenthesis
                        if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                            PrintStatement(); // Loop back to PrintStatement 
                            
                        } else { // Expected Token not found
                            CheckForErrors();  
                        }
                    } else if (tokens.get(currentToken - 1).getType().equals(tokenType.CHAR)) { // Last quote was a char
                        // We save it to CHARLIST and add CHARLIST because on every string add charlist saves the char in order for later output
                        // In order to remove , from the array list we must append ""
                        String CHARLIST = ""; // CHARLIST is the very first space
                        for(String CHAR : charList) {  // We loop through the newly created array list of chars
                           CHARLIST = CHARLIST + CHAR + ""; // Back "" is the space after every char they are closed to keep chars together  
                        }   
                        
                        // add charList to ast before next quote
                        ast.addNode(CHARLIST, "leaf");
                        
                        if(charList.size() > 2) {
                            // Matches position to last spotted quote
                            cst.scaleToQuote();
                        } else {
                            cst.endChildren(); // IF there is only one char
                        }
                        
                        
                        // Allows me to get the current quote and add to node as leaf
                        cst.addNode(tokens.get(currentToken).getData(), "leaf");
                        
                        matchAndDevour(tokenType.Quote);
                        outputAreaParser.append("PARSER: parseQuote()\n");

                        if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                            BooleanExpr(); // continues the BooleanExpr

                        } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { // Checking for BOOLOP
                            BooleanExpr(); // continues the BooleanExpr

                        } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                            PrintStatement(); // Loop back to PrintStatement    

                        } else {
                            cst.statementListIncrement(); // Attaches to previous Statement List
                            ast.scaleToBlock(); // Aligns AST parent to its current Block
                            StatementList();
                        }
                    } else { // Expected Token not found
                        CheckForErrors(); 
                    }   
                }
            } else { // Expected Token not found
                CheckForErrors(); 
            }     
        }
        
        
        /**
         * 
         * Expr ::== BooleanExpr  ::== ( Expr boolop Expr ), boolval
         */
        private void BooleanExpr() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.openParenthesis)) { // Checking for openParenthesis 
                // Allows me to get the current openParen and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                matchAndDevour(tokenType.openParenthesis);
                outputAreaParser.append("PARSER: parseBooleanExpr()\n"); // BooleanExpr is valid
                outputAreaParser.append("PARSER: parseOpenParenthesis()\n");
                
                if(tokens.get(currentToken).getType().equals(tokenType.boolvalTrue)) { // Checking for boolval 
                    // Allows me to get the current boolval and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf");
                    
                    // Allows me to get the current boolval to the ast
                    ast.addNode(tokens.get(currentToken).getData(), "leaf");
                    
                    outputAreaParser.append("PARSER: parseExpr()\n");
                    matchAndDevour(tokenType.boolvalTrue);
                    outputAreaParser.append("PARSER: parseBoolvalTrue()\n");
                    
                    if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) { // Checking for closeParenthesis
                        // Match first then see whats next
                        matchAndDevour(tokenType.closeParenthesis);
                        outputAreaParser.append("PARSER: parseCloseParenthesis()\n");
                        
                        ///-------CHECK IFNEED TO LINK TO LAST (
                        // Creates the leaf node closeParen
                        cst.addNode(")", "leaf");
                        
                        if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // For If and While statements
                            scope++; // Add scope level
                            ast.scaleToBlock();
                            StatementList(); // Restart new block
                        } else {
                            PrintStatement(); //In case of newlines
                        }
                    } else {
                        CheckForErrors(); 
                    }
                    
                } else if(tokens.get(currentToken).getType().equals(tokenType.boolvalFalse)) { // Checking for boolval 
                    // Allows me to get the current boolval and add to node as leaf
                    cst.addNode(tokens.get(currentToken).getData(), "leaf");
                    
                    outputAreaParser.append("PARSER: parseExpr()\n");
                    matchAndDevour(tokenType.boolvalFalse);
                    outputAreaParser.append("PARSER: parseBoolvalFalse()\n");
                    
                    if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) { // Checking for closeParenthesis
                        // Match first then see whats next
                        matchAndDevour(tokenType.closeParenthesis);
                        outputAreaParser.append("PARSER: parseCloseParenthesis()\n");

                        // Creates the leaf node closeParen
                        cst.addNode(")", "leaf");
                        
                        if(tokens.get(currentToken).getType().equals(tokenType.openBracket)) { // For If and While statements
                            scope++;
                            ast.scaleToBlock();
                            StatementList(); // Restart new block
                        } else {
                            PrintStatement();
                        }
                    } else {
                        CheckForErrors(); 
                    }
                } else {
                    Expr();
                }    
            } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) { // Checking for closeParenthesis                                
                PrintStatement(); // Finish the booleanExpr
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                // Allows me to get the current quote and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                ast.addNode("isNotEquivalentTo", "branch"); // Adds the boolop to the ast
                
                //ast.endChildren(); // So it next branch can stay aligned
                
                matchAndDevour(tokenType.boolopNotEqualTo);
                outputAreaParser.append("PARSER: parseBoolopNotEqualTo()\n");
                Expr(); // continues the BooleanExpr
                
            } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) {
                // Allows me to get the current quote and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf");
                
                ast.addNode("isEquivalentTo", "branch"); // Adds the boolop to the ast
                
                matchAndDevour(tokenType.boolopEqualTo);
                outputAreaParser.append("PARSER: parseBoolopEqualTo()\n");
                Expr(); // continues the BooleanExpr
            
            } else { // Not a BoolopExpr so go to finish the printStatement
                CheckForErrors(); 
            }   
        }   
        
        
        /**
         * 
         *  Expr ::== ID   ::== char
         */
        private void ID() {
            if(tokens.get(currentToken).getType().equals(tokenType.newLine)) { // Accounting for a new line
                lineCount++;
            }
            
            if(tokens.get(currentToken).getType().equals(tokenType.CHAR)) { // Checking for CHARS
                // Allows me to get the String of current CHAR and add to node as leaf
                cst.addNode(tokens.get(currentToken).getData(), "leaf"); 
                
                // Allows me to get the current ID (char) and add to the ast
                ast.addNode(tokens.get(currentToken).getData(), "leaf");
                if(!idList.contains(tokens.get(currentToken).getData())) {
                    semanticCount++;
                    semanticErrorList.add("Error: The id " + tokens.get(currentToken).getData() + " on line " + lineCount + " was used before being declared\n");
                }
                
                matchAndDevour(tokenType.CHAR);
                outputAreaParser.append("PARSER: parseID()\n"); // ID is valid
                outputAreaParser.append("PARSER: parseCHAR()\n");
                System.out.println("matched: CHAR\n");
                
                if(tokens.get(currentToken).getType().equals(tokenType.boolopNotEqualTo)) { // Checking for BOOLOP
                    BooleanExpr(); // continues the BooleanExpr
                    
                } else if(tokens.get(currentToken).getType().equals(tokenType.boolopEqualTo)) { // Checking for BOOLOP
                    BooleanExpr(); // continues the BooleanExpr

                } else if(tokens.get(currentToken).getType().equals(tokenType.closeParenthesis)) {                    
                    PrintStatement(); // Loop back to PrintStatement    
                    
                } else { // An AssignmentStatement
                    AssignmentStatement(); 
                }    
            } else {
                CheckForErrors();
            }    
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     * 
     * (CONTAINS THE JFRAME INFORMATION CREATED UTILIZING JAVA SWING)
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelLexer = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputArea = new javax.swing.JTextArea();
        scrollPaneOutput = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        buttonLex = new javax.swing.JButton();
        buttonQuit = new javax.swing.JButton();
        labelInput = new javax.swing.JLabel();
        labelOutput = new javax.swing.JLabel();
        buttonClearAll = new javax.swing.JButton();
        buttonTestCases = new javax.swing.JButton();
        labelTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelParser = new javax.swing.JPanel();
        scrollPaneInput1 = new javax.swing.JScrollPane();
        outputAreaParser = new javax.swing.JTextArea();
        scrollPaneOutput1 = new javax.swing.JScrollPane();
        cstOutputArea = new javax.swing.JTextArea();
        labelInput1 = new javax.swing.JLabel();
        labelTitle1 = new javax.swing.JLabel();
        labelOutput1 = new javax.swing.JLabel();
        buttonTestCases1 = new javax.swing.JButton();
        buttonQuit1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        buttonClearAll2 = new javax.swing.JButton();
        panelVisuals = new javax.swing.JPanel();
        scrollPaneInput2 = new javax.swing.JScrollPane();
        outputAreaSymbolTable = new javax.swing.JTextArea();
        scrollPaneOutput2 = new javax.swing.JScrollPane();
        astOutputArea = new javax.swing.JTextArea();
        labelInput2 = new javax.swing.JLabel();
        labelTitle2 = new javax.swing.JLabel();
        labelOutput2 = new javax.swing.JLabel();
        buttonTestCases2 = new javax.swing.JButton();
        buttonQuit2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        buttonClearAll3 = new javax.swing.JButton();
        panelSemantics = new javax.swing.JPanel();
        scrollPaneInput3 = new javax.swing.JScrollPane();
        outputAreaSemantics = new javax.swing.JTextArea();
        scrollPaneOutput3 = new javax.swing.JScrollPane();
        astOutputAreaCodeGen = new javax.swing.JTextArea();
        labelInput3 = new javax.swing.JLabel();
        labelTitle3 = new javax.swing.JLabel();
        labelOutput3 = new javax.swing.JLabel();
        buttonTestCases3 = new javax.swing.JButton();
        buttonQuit3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        buttonClearAll4 = new javax.swing.JButton();
        menuLexer = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemQuit = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuItemTestCases = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menutItemHelp = new javax.swing.JMenuItem();
        menutItemGrammar = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Custom Compiler: Lexer");
        setBounds(new java.awt.Rectangle(20, 20, 0, 0));
        setLocation(new java.awt.Point(20, 20));
        setName("frameLexer"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1156, 835));
        setSize(new java.awt.Dimension(0, 0));

        jTabbedPane1.setFont(new java.awt.Font("Helvetica", 1, 16)); // NOI18N

        inputArea.setColumns(20);
        inputArea.setFont(new java.awt.Font("Helvetica Neue", 0, 20)); // NOI18N
        inputArea.setRows(5);
        inputArea.setTabSize(2);
        inputArea.setToolTipText("");
        inputArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        inputArea.setVerifyInputWhenFocusTarget(false);
        jScrollPane1.setViewportView(inputArea);

        outputArea.setEditable(false);
        outputArea.setColumns(20);
        outputArea.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        outputArea.setRows(5);
        outputArea.setTabSize(2);
        outputArea.setToolTipText("");
        outputArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outputArea.setVerifyInputWhenFocusTarget(false);
        scrollPaneOutput.setViewportView(outputArea);

        buttonLex.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonLex.setText("Compile");
        buttonLex.setToolTipText("Execute program");
        buttonLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLexActionPerformed(evt);
            }
        });

        buttonQuit.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonQuit.setText("Quit");
        buttonQuit.setToolTipText("Exits Program");
        buttonQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuitActionPerformed(evt);
            }
        });

        labelInput.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput.setText("Input");

        labelOutput.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelOutput.setText("Lexical Analysis Output");

        buttonClearAll.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonClearAll.setText("Reset Program");
        buttonClearAll.setToolTipText("Removes text from input and output fields");
        buttonClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearAllActionPerformed(evt);
            }
        });

        buttonTestCases.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonTestCases.setText("Test Cases");
        buttonTestCases.setToolTipText("Opens the test case menu");
        buttonTestCases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestCasesActionPerformed(evt);
            }
        });

        labelTitle.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        labelTitle.setText("Custom Compiler: Lexical Analyzer");
        labelTitle.setAlignmentX(45.0F);
        labelTitle.setAlignmentY(15.0F);

        jLabel2.setFont(new java.awt.Font("Helvetica", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setText("* Reset program to erase previous information");

        javax.swing.GroupLayout panelLexerLayout = new javax.swing.GroupLayout(panelLexer);
        panelLexer.setLayout(panelLexerLayout);
        panelLexerLayout.setHorizontalGroup(
            panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLexerLayout.createSequentialGroup()
                .addGroup(panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLexerLayout.createSequentialGroup()
                        .addGap(268, 268, 268)
                        .addComponent(labelTitle))
                    .addGroup(panelLexerLayout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addComponent(labelInput)
                        .addGap(415, 415, 415)
                        .addComponent(labelOutput))
                    .addGroup(panelLexerLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel2)
                        .addGap(113, 113, 113)
                        .addComponent(buttonLex, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelLexerLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(buttonClearAll)
                        .addGap(367, 367, 367)
                        .addComponent(buttonTestCases)
                        .addGap(382, 382, 382)
                        .addComponent(buttonQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelLexerLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        panelLexerLayout.setVerticalGroup(
            panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLexerLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInput)
                    .addComponent(labelOutput))
                .addGap(18, 18, 18)
                .addGroup(panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPaneOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLexerLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2))
                    .addComponent(buttonLex, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(panelLexerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonClearAll, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonTestCases, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Lexer", panelLexer);

        outputAreaParser.setEditable(false);
        outputAreaParser.setColumns(20);
        outputAreaParser.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        outputAreaParser.setRows(5);
        outputAreaParser.setTabSize(2);
        outputAreaParser.setToolTipText("");
        outputAreaParser.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outputAreaParser.setVerifyInputWhenFocusTarget(false);
        scrollPaneInput1.setViewportView(outputAreaParser);

        cstOutputArea.setEditable(false);
        cstOutputArea.setColumns(20);
        cstOutputArea.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        cstOutputArea.setRows(5);
        cstOutputArea.setTabSize(2);
        cstOutputArea.setToolTipText("");
        cstOutputArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cstOutputArea.setVerifyInputWhenFocusTarget(false);
        scrollPaneOutput1.setViewportView(cstOutputArea);

        labelInput1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput1.setText("Parser Output");

        labelTitle1.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        labelTitle1.setText("Custom Compiler: Parser");
        labelTitle1.setAlignmentX(45.0F);
        labelTitle1.setAlignmentY(15.0F);

        labelOutput1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelOutput1.setText("CST Output");

        buttonTestCases1.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonTestCases1.setText("Test Cases");
        buttonTestCases1.setToolTipText("Opens the test case menu");
        buttonTestCases1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestCases1ActionPerformed(evt);
            }
        });

        buttonQuit1.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonQuit1.setText("Quit");
        buttonQuit1.setToolTipText("Exits Program");
        buttonQuit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuit1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Helvetica", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("* Reset program to erase previous information");

        buttonClearAll2.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonClearAll2.setText("Reset Program");
        buttonClearAll2.setToolTipText("Removes text from input and output fields");
        buttonClearAll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearAll2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelParserLayout = new javax.swing.GroupLayout(panelParser);
        panelParser.setLayout(panelParserLayout);
        panelParserLayout.setHorizontalGroup(
            panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParserLayout.createSequentialGroup()
                .addGroup(panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelParserLayout.createSequentialGroup()
                        .addGap(360, 360, 360)
                        .addComponent(labelTitle1))
                    .addGroup(panelParserLayout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(labelInput1)
                        .addGap(426, 426, 426)
                        .addComponent(labelOutput1))
                    .addGroup(panelParserLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(panelParserLayout.createSequentialGroup()
                                .addComponent(buttonClearAll2)
                                .addGap(356, 356, 356)
                                .addComponent(buttonTestCases1)
                                .addGap(399, 399, 399)
                                .addComponent(buttonQuit1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(panelParserLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(scrollPaneInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneOutput1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        panelParserLayout.setVerticalGroup(
            panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelParserLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInput1)
                    .addComponent(labelOutput1))
                .addGap(20, 20, 20)
                .addGroup(panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPaneOutput1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelParserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonTestCases1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonQuit1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonClearAll2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Parser", panelParser);

        outputAreaSymbolTable.setEditable(false);
        outputAreaSymbolTable.setColumns(20);
        outputAreaSymbolTable.setFont(new java.awt.Font("Helvetica", 0, 18)); // NOI18N
        outputAreaSymbolTable.setRows(5);
        outputAreaSymbolTable.setTabSize(2);
        outputAreaSymbolTable.setToolTipText("");
        outputAreaSymbolTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outputAreaSymbolTable.setVerifyInputWhenFocusTarget(false);
        scrollPaneInput2.setViewportView(outputAreaSymbolTable);

        astOutputArea.setEditable(false);
        astOutputArea.setColumns(20);
        astOutputArea.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        astOutputArea.setRows(5);
        astOutputArea.setTabSize(2);
        astOutputArea.setToolTipText("");
        astOutputArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        astOutputArea.setVerifyInputWhenFocusTarget(false);
        scrollPaneOutput2.setViewportView(astOutputArea);

        labelInput2.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput2.setText("Symbol Table Output");

        labelTitle2.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        labelTitle2.setText("Custom Compiler: Code Analysis");
        labelTitle2.setAlignmentX(45.0F);
        labelTitle2.setAlignmentY(15.0F);

        labelOutput2.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelOutput2.setText("AST Output");

        buttonTestCases2.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonTestCases2.setText("Test Cases");
        buttonTestCases2.setToolTipText("Opens the test case menu");
        buttonTestCases2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestCases2ActionPerformed(evt);
            }
        });

        buttonQuit2.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonQuit2.setText("Quit");
        buttonQuit2.setToolTipText("Exits Program");
        buttonQuit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuit2ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Helvetica", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        jLabel4.setText("* Reset program to erase previous information");

        buttonClearAll3.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonClearAll3.setText("Reset Program");
        buttonClearAll3.setToolTipText("Removes text from input and output fields");
        buttonClearAll3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearAll3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelVisualsLayout = new javax.swing.GroupLayout(panelVisuals);
        panelVisuals.setLayout(panelVisualsLayout);
        panelVisualsLayout.setHorizontalGroup(
            panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVisualsLayout.createSequentialGroup()
                .addGroup(panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelVisualsLayout.createSequentialGroup()
                        .addGap(281, 281, 281)
                        .addComponent(labelTitle2))
                    .addGroup(panelVisualsLayout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(labelInput2)
                        .addGap(391, 391, 391)
                        .addComponent(labelOutput2))
                    .addGroup(panelVisualsLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(panelVisualsLayout.createSequentialGroup()
                                .addComponent(buttonClearAll3)
                                .addGap(356, 356, 356)
                                .addComponent(buttonTestCases2)
                                .addGap(399, 399, 399)
                                .addComponent(buttonQuit2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(panelVisualsLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(scrollPaneInput2, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneOutput2, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        panelVisualsLayout.setVerticalGroup(
            panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelVisualsLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(labelTitle2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInput2)
                    .addComponent(labelOutput2))
                .addGap(20, 20, 20)
                .addGroup(panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneInput2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPaneOutput2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelVisualsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonTestCases2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonQuit2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonClearAll3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Visuals", panelVisuals);

        outputAreaSemantics.setEditable(false);
        outputAreaSemantics.setColumns(20);
        outputAreaSemantics.setFont(new java.awt.Font("Helvetica", 0, 14)); // NOI18N
        outputAreaSemantics.setRows(5);
        outputAreaSemantics.setTabSize(2);
        outputAreaSemantics.setToolTipText("");
        outputAreaSemantics.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outputAreaSemantics.setVerifyInputWhenFocusTarget(false);
        scrollPaneInput3.setViewportView(outputAreaSemantics);

        astOutputAreaCodeGen.setEditable(false);
        astOutputAreaCodeGen.setColumns(20);
        astOutputAreaCodeGen.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        astOutputAreaCodeGen.setRows(5);
        astOutputAreaCodeGen.setTabSize(2);
        astOutputAreaCodeGen.setToolTipText("");
        astOutputAreaCodeGen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        astOutputAreaCodeGen.setVerifyInputWhenFocusTarget(false);
        scrollPaneOutput3.setViewportView(astOutputAreaCodeGen);

        labelInput3.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput3.setText("Semantic Analysis Output");

        labelTitle3.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        labelTitle3.setText("Custom Compiler: Semantic Analyzer");
        labelTitle3.setAlignmentX(45.0F);
        labelTitle3.setAlignmentY(15.0F);

        labelOutput3.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelOutput3.setText("Code Generation");

        buttonTestCases3.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonTestCases3.setText("Test Cases");
        buttonTestCases3.setToolTipText("Opens the test case menu");
        buttonTestCases3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTestCases3ActionPerformed(evt);
            }
        });

        buttonQuit3.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonQuit3.setText("Quit");
        buttonQuit3.setToolTipText("Exits Program");
        buttonQuit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonQuit3ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Helvetica", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("* Reset program to erase previous information");

        buttonClearAll4.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonClearAll4.setText("Reset Program");
        buttonClearAll4.setToolTipText("Removes text from input and output fields");
        buttonClearAll4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearAll4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelSemanticsLayout = new javax.swing.GroupLayout(panelSemantics);
        panelSemantics.setLayout(panelSemanticsLayout);
        panelSemanticsLayout.setHorizontalGroup(
            panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSemanticsLayout.createSequentialGroup()
                .addGroup(panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSemanticsLayout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(labelTitle3))
                    .addGroup(panelSemanticsLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(labelInput3)
                        .addGap(324, 324, 324)
                        .addComponent(labelOutput3))
                    .addGroup(panelSemanticsLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(panelSemanticsLayout.createSequentialGroup()
                                .addComponent(buttonClearAll4)
                                .addGap(356, 356, 356)
                                .addComponent(buttonTestCases3)
                                .addGap(399, 399, 399)
                                .addComponent(buttonQuit3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(panelSemanticsLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(scrollPaneInput3, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrollPaneOutput3, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        panelSemanticsLayout.setVerticalGroup(
            panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSemanticsLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(labelTitle3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInput3)
                    .addComponent(labelOutput3))
                .addGap(20, 20, 20)
                .addGroup(panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPaneInput3, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scrollPaneOutput3, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(93, 93, 93)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelSemanticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonTestCases3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonQuit3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonClearAll4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jTabbedPane1.addTab("Semantics", panelSemantics);

        menuLexer.setToolTipText("");

        menuFile.setText("File");

        menuItemQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuItemQuit.setText("Quit");
        menuItemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemQuitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemQuit);

        menuLexer.add(menuFile);

        menuTools.setText("Tools");

        menuItemTestCases.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        menuItemTestCases.setText("Test Cases");
        menuItemTestCases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemTestCasesActionPerformed(evt);
            }
        });
        menuTools.add(menuItemTestCases);

        menuLexer.add(menuTools);

        menuHelp.setText("Help");

        menutItemHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        menutItemHelp.setText("User help");
        menutItemHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menutItemHelpActionPerformed(evt);
            }
        });
        menuHelp.add(menutItemHelp);

        menutItemGrammar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        menutItemGrammar.setText("Grammar");
        menutItemGrammar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menutItemGrammarActionPerformed(evt);
            }
        });
        menuHelp.add(menutItemGrammar);

        menuLexer.add(menuHelp);

        setJMenuBar(menuLexer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // ------------------------------------------------------------
    // -----------------[BUTTON PERFORMANCE]-----------------------
    // ------------------------------------------------------------
        
    // Executes the run (Lexer) prints results onto the Output box
    private void buttonLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLexActionPerformed
        
        Token token = new Token();
        
        Parser parse = new Parser(token);
        
    }//GEN-LAST:event_buttonLexActionPerformed
    
    // Exits the Lexer
    private void menuItemQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuItemQuitActionPerformed
    
    // Exits the lexer
    private void buttonQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_buttonQuitActionPerformed

    // Button that deletes both the input and output data
    private void buttonClearAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearAllActionPerformed
        /* Create and display the new form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lexer().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonClearAllActionPerformed

    // Opens the Test Cases frame which you can add onto the lexer input box
    private void menuItemTestCasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemTestCasesActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LexerTestCasesFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_menuItemTestCasesActionPerformed

    // Opens the test cases menu
    private void buttonTestCasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestCasesActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LexerTestCasesFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonTestCasesActionPerformed

    // Opens the helper test cases frame; Gives info on how to use Lexer;
    private void menutItemHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menutItemHelpActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LexerTestCasesHelpFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_menutItemHelpActionPerformed

    private void buttonTestCases1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestCases1ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LexerTestCasesFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonTestCases1ActionPerformed

    private void buttonQuit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuit1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_buttonQuit1ActionPerformed

    private void buttonClearAll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearAll2ActionPerformed
        /* Create and display the new form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lexer().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonClearAll2ActionPerformed

    private void buttonTestCases2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestCases2ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LexerTestCasesFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonTestCases2ActionPerformed

    private void buttonQuit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuit2ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_buttonQuit2ActionPerformed

    private void buttonClearAll3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearAll3ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lexer().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonClearAll3ActionPerformed

    private void menutItemGrammarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menutItemGrammarActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Grammar().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_menutItemGrammarActionPerformed

    private void buttonTestCases3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTestCases3ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LexerTestCasesFrame().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonTestCases3ActionPerformed

    private void buttonQuit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonQuit3ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_buttonQuit3ActionPerformed

    private void buttonClearAll4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearAll4ActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lexer().setVisible(true);
            }
        });
        this.setVisible(false);
        this.setEnabled(false);
    }//GEN-LAST:event_buttonClearAll4ActionPerformed

    
    // ------------------------------------------------------------
    // --------------------[MAIN]----------------------------------
    // ------------------------------------------------------------
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Lexer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Lexer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Lexer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Lexer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Lexer().setVisible(true);
            }
        });
    }
    
    
    // ------------------------------------------------------------
    // ----------------[JFRAME VARIABLES]--------------------------
    // ------------------------------------------------------------

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea astOutputArea;
    private javax.swing.JTextArea astOutputAreaCodeGen;
    private javax.swing.JButton buttonClearAll;
    private javax.swing.JButton buttonClearAll2;
    private javax.swing.JButton buttonClearAll3;
    private javax.swing.JButton buttonClearAll4;
    public javax.swing.JButton buttonLex;
    private javax.swing.JButton buttonQuit;
    private javax.swing.JButton buttonQuit1;
    private javax.swing.JButton buttonQuit2;
    private javax.swing.JButton buttonQuit3;
    private javax.swing.JButton buttonTestCases;
    private javax.swing.JButton buttonTestCases1;
    private javax.swing.JButton buttonTestCases2;
    private javax.swing.JButton buttonTestCases3;
    private javax.swing.JTextArea cstOutputArea;
    private javax.swing.JTextArea inputArea;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelInput;
    private javax.swing.JLabel labelInput1;
    private javax.swing.JLabel labelInput2;
    private javax.swing.JLabel labelInput3;
    private javax.swing.JLabel labelOutput;
    private javax.swing.JLabel labelOutput1;
    private javax.swing.JLabel labelOutput2;
    private javax.swing.JLabel labelOutput3;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JLabel labelTitle2;
    private javax.swing.JLabel labelTitle3;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemQuit;
    private javax.swing.JMenuItem menuItemTestCases;
    private javax.swing.JMenuBar menuLexer;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenuItem menutItemGrammar;
    private javax.swing.JMenuItem menutItemHelp;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JTextArea outputAreaParser;
    private javax.swing.JTextArea outputAreaSemantics;
    private javax.swing.JTextArea outputAreaSymbolTable;
    private javax.swing.JPanel panelLexer;
    private javax.swing.JPanel panelParser;
    private javax.swing.JPanel panelSemantics;
    private javax.swing.JPanel panelVisuals;
    private javax.swing.JScrollPane scrollPaneInput1;
    private javax.swing.JScrollPane scrollPaneInput2;
    private javax.swing.JScrollPane scrollPaneInput3;
    private javax.swing.JScrollPane scrollPaneOutput;
    private javax.swing.JScrollPane scrollPaneOutput1;
    private javax.swing.JScrollPane scrollPaneOutput2;
    private javax.swing.JScrollPane scrollPaneOutput3;
    // End of variables declaration//GEN-END:variables
}
