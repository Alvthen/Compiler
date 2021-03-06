/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customcompiler;

import javax.swing.JTextArea;



/**
 *
 * @author reynaldoalvarez
 */
public class LexerTestCasesFrame extends javax.swing.JFrame {

    /**
     * Creates new form LexerTestCasesFrame
     */
    public LexerTestCasesFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle1 = new javax.swing.JLabel();
        checkBox2 = new javax.swing.JCheckBox();
        labelInput = new javax.swing.JLabel();
        labelInput1 = new javax.swing.JLabel();
        checkBox4 = new javax.swing.JCheckBox();
        checkBox3 = new javax.swing.JCheckBox();
        checkBox1 = new javax.swing.JCheckBox();
        buttonConfirm = new javax.swing.JButton();
        checkBox5 = new javax.swing.JCheckBox();
        checkBox6 = new javax.swing.JCheckBox();
        checkBox7 = new javax.swing.JCheckBox();
        checkBox8 = new javax.swing.JCheckBox();
        checkBox9 = new javax.swing.JCheckBox();
        checkBox10 = new javax.swing.JCheckBox();
        checkBox11 = new javax.swing.JCheckBox();
        checkBox12 = new javax.swing.JCheckBox();
        checkBox13 = new javax.swing.JCheckBox();
        checkBox15 = new javax.swing.JCheckBox();
        labelInput2 = new javax.swing.JLabel();
        checkBox14 = new javax.swing.JCheckBox();
        checkBox16 = new javax.swing.JCheckBox();
        checkBox17 = new javax.swing.JCheckBox();
        checkBox18 = new javax.swing.JCheckBox();
        menuLexer = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemExit = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Custom Compiler: Lexer");
        setLocation(new java.awt.Point(20, 20));
        setName("LexerTestCasesFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(730, 550));

        labelTitle1.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        labelTitle1.setText("Test Cases");
        labelTitle1.setAlignmentX(45.0F);
        labelTitle1.setAlignmentY(15.0F);

        checkBox2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox2.setText("Chars and Quotes");
        checkBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox2ActionPerformed(evt);
            }
        });

        labelInput.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput.setText("Error Cases");

        labelInput1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput1.setText("Working Cases");

        checkBox4.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox4.setText("Illegal Input");
        checkBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox4ActionPerformed(evt);
            }
        });

        checkBox3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox3.setText("Alan's Case");
        checkBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox3ActionPerformed(evt);
            }
        });

        checkBox1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox1.setText("Empty Brackets");
        checkBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox1ActionPerformed(evt);
            }
        });

        buttonConfirm.setFont(new java.awt.Font("Helvetica", 0, 16)); // NOI18N
        buttonConfirm.setText("Confirm");
        buttonConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfirmActionPerformed(evt);
            }
        });

        checkBox5.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox5.setText("Random");
        checkBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox5ActionPerformed(evt);
            }
        });

        checkBox6.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox6.setText("Print Statement (StringExpr)");
        checkBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox6ActionPerformed(evt);
            }
        });

        checkBox7.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox7.setText("Print Statement (IntExpr)");
        checkBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox7ActionPerformed(evt);
            }
        });

        checkBox8.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox8.setText("Print Statement (ID)");
        checkBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox8ActionPerformed(evt);
            }
        });

        checkBox9.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox9.setText("Print Statement (BooleanExpr)");
        checkBox9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox9ActionPerformed(evt);
            }
        });

        checkBox10.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox10.setText("Variable Declaration");
        checkBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox10ActionPerformed(evt);
            }
        });

        checkBox11.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox11.setText("Assignment Statement");
        checkBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox11ActionPerformed(evt);
            }
        });

        checkBox12.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox12.setText("If Statement");
        checkBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox12ActionPerformed(evt);
            }
        });

        checkBox13.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox13.setText("While Statement");
        checkBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox13ActionPerformed(evt);
            }
        });

        checkBox15.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox15.setText("Project 1 & 2 (Same Input file)");
        checkBox15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox15ActionPerformed(evt);
            }
        });

        labelInput2.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        labelInput2.setText("Project Input Files");

        checkBox14.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox14.setText("Project 3");
        checkBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox14ActionPerformed(evt);
            }
        });

        checkBox16.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox16.setText("One Line of nestedness");
        checkBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox16ActionPerformed(evt);
            }
        });

        checkBox17.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox17.setText("Line of nestedness");
        checkBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox17ActionPerformed(evt);
            }
        });

        checkBox18.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        checkBox18.setText("Input file");
        checkBox18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBox18ActionPerformed(evt);
            }
        });

        menuLexer.setToolTipText("");

        menuFile.setText("File");

        menuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemExit);

        menuLexer.add(menuFile);

        menuHelp.setText("Help");
        menuLexer.add(menuHelp);

        setJMenuBar(menuLexer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(260, 260, 260)
                        .addComponent(labelTitle1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(labelInput1)
                        .addGap(215, 215, 215)
                        .addComponent(labelInput))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(checkBox1)
                        .addGap(247, 247, 247)
                        .addComponent(checkBox2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(checkBox8)
                        .addGap(224, 224, 224)
                        .addComponent(checkBox3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(checkBox7)
                        .addGap(193, 193, 193)
                        .addComponent(checkBox4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkBox9)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkBox6)
                                    .addComponent(checkBox11)
                                    .addComponent(checkBox10)
                                    .addComponent(checkBox12)
                                    .addComponent(checkBox13)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(checkBox16)
                                            .addComponent(checkBox17))))
                                .addGap(172, 172, 172)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkBox15)
                                    .addComponent(labelInput2)
                                    .addComponent(checkBox5)
                                    .addComponent(checkBox14)
                                    .addComponent(checkBox18))))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(312, Short.MAX_VALUE)
                .addComponent(buttonConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(312, 312, 312))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(labelTitle1)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelInput1)
                    .addComponent(labelInput))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBox1)
                    .addComponent(checkBox2))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBox8)
                    .addComponent(checkBox3))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBox7)
                    .addComponent(checkBox4))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBox6)
                    .addComponent(checkBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkBox9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(labelInput2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkBox18)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Lexer().setVisible(true);
            }
        });
        this.dispose();
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void checkBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox2ActionPerformed

    private void checkBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox4ActionPerformed

    private void checkBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox3ActionPerformed

    private void checkBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox1ActionPerformed

    private void buttonConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfirmActionPerformed
        
        Lexer lex = new Lexer();
        JTextArea inputArea = lex.getInputArea();
        
        
        if(checkBox1.isSelected()) {
            inputArea.append("{}$");
        }
        
        if(checkBox2.isSelected()) {
            inputArea.append("print()&"
               + "if(a)$"
               + "while(true)$\n"
               + "{print(\"b\")}$");
        }
        
        if(checkBox3.isSelected()) {
            inputArea.append("{}$\n" +
            "{{{{{{}}}}}}$\n" +
            "{{{{{{}}} /* comments are ignored */ }}}}$\n" +
            "{ /* comments are still ignored */ int @}$");
        }
        
        if(checkBox4.isSelected()) {
            inputArea.append("{int @}$"
                + "{\"$\"}$");
        }
        
        if(checkBox5.isSelected()) {
            inputArea.append("{\n"
                    + "\t int a = 4"
                    + "\n"
                    + "\n"
                    + "\t int b \n\n"
                    + "\t b = a + a"
                    + "\n"
                    + "}$");
        }
        
        if(checkBox6.isSelected()) {
            inputArea.append("{ /* PrintStatement(StringExpr) */\n"
                + "\t print( \" asdasdas \" )"
                + "\n"
                + "}$");
        }
        
        if(checkBox7.isSelected()) {
            inputArea.append("{ /* PrintStatement(IntExpr) */\n"
                + "\t print( 2 + 2 )"
                + "\n"
                + "}$");
        }
    
        if(checkBox8.isSelected()) {
            inputArea.append("{ /* PrintStatement(ID) */\n"
                + "\t print(z)"
                + "\n"
                + "}$");
        }
        
        if(checkBox9.isSelected()) {
            inputArea.append("{ /* PrintStatement(BooleanExpr) */\n"
                + "\t print( ( w == w ) )"
                + "\n"
                + "\t print( (5 + 2 != 6 + 4) )"
                + "\n"
                + "\t print( (\"dlaslkdmasd\" != \" dasdasd \") )"
                + "\n"
                + "\t print( (a == 3) )"
                + "\n"
                + "\t print( (\"sadklmasd\" == a) )"
                + "\n"
                + "\t print( (\"sd\" != 2) )"
                + "\n"
                + "\t print(false)"
                + "\n"
                + "\t print(true)"
                + "\n"
                + "}$"); 
        }
        
        if(checkBox10.isSelected()) {
            inputArea.append("{ /* Variable Declaration */\n"
                    + "\t int a"
                    + "\n"
                    + "\t string b"
                    + "\n"
                    + "\t boolean c"
                    + "\n"
                    + "}$");
        }
        
        if(checkBox11.isSelected()) {
            inputArea.append("{ /* Assignment Statement */\n"
                    + "   int i\n" 
                    + "   i = 5 \n" 
                    + "   print(i)\n" 
                    + "}$");
        }
        
        if(checkBox12.isSelected()) {
            inputArea.append("{ /* if Statement */\n"
                    + "\t if(5 + 2 != 6 + 4) {  }"
                    + "\n"
                    + "}$");
        }
        
        if(checkBox13.isSelected()) {
            inputArea.append("{ /* while Statement */\n"
                    + "\t while(true) { /* What now */ }"
                    + "\n"
                    + "}$");
        }

        if(checkBox14.isSelected()) {
            inputArea.append("{ /* Project 3 Input file */\n"
                    + "\tint a"
                    + "\n"
                    + "\tboolean b"
                    + "\n"
                    + "\t{"
                    + "\n"
                    + "\t\tstring c"
                    + "\n"
                    + "\t\ta = 5"
                    + "\n"
                    + "\t\tb = true /* no comment */"
                    + "\n"
                    + "\t\tc = \"inta\""
                    + "\n"
                    + "\t\tprint(c)"
                    + "\n"
                    + "\t}"
                    + "\n"
                    + "\tprint(b)"
                    + "\n"
                    + "\tprint(a)"
                    + "\n"
                    + "}$"
                    + "\n\n"
                    + "{"
                    + "\n"
                    + "\tint a"
                    + "\n"
                    + "\t{"
                    + "\n"
                    + "\t\tboolean b"
                    + "\n"
                    + "\t\ta = 1"
                    + "\n"
                    + "\t}"
                    + "\n"
                    + "\tprint(b)"
                    + "\n"
                    + "}$");
        }
        
        if(checkBox15.isSelected()) {
            inputArea.append("{}$ /* Project 1 & 2 Input file */\n" +
            "{{{{{{}}}}}}$\n" +
            "{{{{{{}}} /* comments are ignored */ }}}}$\n" +
            "{ /* comments are still ignored */ int @}$");
        }
        
        if(checkBox16.isSelected()) {
            inputArea.append("{/*OneLongLineOfNestedNess*/while(true){print(g)while(x==2){print(\"ssss\")while(2+2){if(\"s\"!=\"ss\"){print((\"x\"!=y))}}}}}$");
        }
        
        if(checkBox17.isSelected()) {
            inputArea.append("{/* Multiple Lines of NestedNess*/\n" +
                "	while(true) {\n" +
                "		print(g)\n" +
                "		while(x==2) {\n" +
                "			print(\"ssss\") \n" +
                "			while(2+2) {\n" +
                "				if(\"s\" != \"ss\") {\n" +
                "					print( (\"x\" != y) ) \n" +
                "				}\n" +
                "			}\n" +
                "		}\n" +
                "	}\n" +
                "}$");
        }
        if(checkBox18.isSelected()) {
            inputArea.append("{\n" +
                "   print (false)\n" +
                "   print (true)\n" +
                "   print (\"two\")\n" +
                "   print (3)\n" +
                "   print (\"four\")\n" +
                "   print (5)\n" +
                "   print (6)\n" +
                "   print (7)\n" +
                "   print (8)\n" +
                "   print (\"nine\")\n" +
                "}$" +
                "\n" +
                "{\n" +
                "   int x\n" +
                "   x = 7\n" +
                "   \n" +
                "   string y\n" +
                "   y = \"bond\"\n" +
                "      \n" +
                "   boolean z\n" +
                "   z = false\n" +
                "   \n" +
                "   print (z)\n" +
                "   print (y)\n" +
                "   print (x)\n" +
                "}$");
        }
        
        lex.setVisible(true);
        lex.setEnabled(true);
        this.dispose();
    }//GEN-LAST:event_buttonConfirmActionPerformed

    private void checkBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox5ActionPerformed

    private void checkBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox6ActionPerformed

    private void checkBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox7ActionPerformed

    private void checkBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox8ActionPerformed

    private void checkBox9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox9ActionPerformed

    private void checkBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox10ActionPerformed

    private void checkBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox12ActionPerformed

    private void checkBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox13ActionPerformed

    private void checkBox15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox15ActionPerformed

    private void checkBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox14ActionPerformed

    private void checkBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox16ActionPerformed

    private void checkBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox17ActionPerformed

    private void checkBox18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox18ActionPerformed

    private void checkBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBox11ActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LexerTestCasesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LexerTestCasesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LexerTestCasesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LexerTestCasesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new LexerTestCasesFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonConfirm;
    private javax.swing.JCheckBox checkBox1;
    private javax.swing.JCheckBox checkBox10;
    private javax.swing.JCheckBox checkBox11;
    private javax.swing.JCheckBox checkBox12;
    private javax.swing.JCheckBox checkBox13;
    private javax.swing.JCheckBox checkBox14;
    private javax.swing.JCheckBox checkBox15;
    private javax.swing.JCheckBox checkBox16;
    private javax.swing.JCheckBox checkBox17;
    private javax.swing.JCheckBox checkBox18;
    private javax.swing.JCheckBox checkBox2;
    private javax.swing.JCheckBox checkBox3;
    private javax.swing.JCheckBox checkBox4;
    private javax.swing.JCheckBox checkBox5;
    private javax.swing.JCheckBox checkBox6;
    private javax.swing.JCheckBox checkBox7;
    private javax.swing.JCheckBox checkBox8;
    private javax.swing.JCheckBox checkBox9;
    private javax.swing.JLabel labelInput;
    private javax.swing.JLabel labelInput1;
    private javax.swing.JLabel labelInput2;
    private javax.swing.JLabel labelTitle1;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuBar menuLexer;
    // End of variables declaration//GEN-END:variables
}
