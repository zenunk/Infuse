package Similarity;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class GUI extends javax.swing.JPanel {

    String tablePath;
    JFrame frame;
    JPanel cosinePanel, wuPalmerPanel, averagesPanel, synsetPanel, occurrencePanel;
    JTable cosineTable, wuPalmerTable, averagesTable, synsetTable, occurrenceTable;
    JScrollPane cosineScroll, wuPalmerScroll, averagesScroll, synsetScroll, occurrenceScroll;
    JTabbedPane tabbedPane;
    ILexicalDatabase db = new NictWordNet();
    RelatednessCalculator rcs = new WuPalmer(db);    
    Stack list = new Stack();
    
    public GUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        selectButton = new javax.swing.JButton();
        pathTextField = new javax.swing.JTextField();
        computeButton = new javax.swing.JButton();
        selectComboBox = new javax.swing.JComboBox();
        computingLabel = new javax.swing.JLabel();

        jLabel1.setText("Select input table");

        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        pathTextField.setEditable(false);

        computeButton.setText("Compute");
        computeButton.setEnabled(false);
        computeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                computeButtonActionPerformed(evt);
            }
        });

        selectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "First sense", "Maximum similarity" }));

        computingLabel.setText("Waiting");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(computeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(computingLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(selectButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(selectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(computeButton)
                    .addComponent(selectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(computingLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select table");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            tablePath = chooser.getSelectedFile().getAbsolutePath();
            pathTextField.setText(tablePath);
            computeButton.setEnabled(true);
        }           
    }//GEN-LAST:event_selectButtonActionPerformed

    private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_computeButtonActionPerformed
        computingLabel.setText("Computing");
        

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    countSimilarity();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                computingLabel.setText("Finished");
            }
        });
    }//GEN-LAST:event_computeButtonActionPerformed
   
    
    private double wordNetSimilarity(String firstWord, String secondWord) {
        double value = 0;
        if(firstWord.equals(secondWord)) {
            value = 1;
        } else {
            value = rcs.calcRelatednessOfWords(firstWord, secondWord); 
            
        }
        return value;
    }
    
    private double synsetSimilarity(String firstWord, String secondWord) {
        double value = 0;
        if(firstWord.equals(secondWord)) {
            value = 1;
        } else {        
            List<POS[]> posPairs = rcs.getPOSPairs();    
            for(POS[] posPair: posPairs) {
                List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(firstWord, posPair[0].toString());
                List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(secondWord, posPair[1].toString());

                for(Concept synset1: synsets1) {
                    for (Concept synset2: synsets2) {
                        Relatedness relatedness = rcs.calcRelatednessOfSynset(synset1, synset2);
                        double score = relatedness.getScore();
                        if (score > value) { 
                            value = score;
                        }
                    }
                }
            }            
        }
        return value;
    }
    
        
    private void createTable() {
        frame = new JFrame("Matrix");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //frame.setMinimumSize(new Dimension(600, 600));
        
        averagesPanel = new JPanel();
        wuPalmerPanel = new JPanel();
        cosinePanel = new JPanel();
        synsetPanel = new JPanel();
        occurrencePanel = new JPanel();

        averagesPanel.setLayout(new GridLayout(1, 1));
        wuPalmerPanel.setLayout(new GridLayout(1, 1));
        cosinePanel.setLayout(new GridLayout(1, 1));
        synsetPanel.setLayout(new GridLayout(1, 1));
        occurrencePanel.setLayout(new GridLayout(1, 1));
        
        cosineScroll = new JScrollPane(cosineTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        wuPalmerScroll = new JScrollPane(wuPalmerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        averagesScroll = new JScrollPane(averagesTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);   
        synsetScroll = new JScrollPane(synsetTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);   
        occurrenceScroll = new JScrollPane(occurrenceTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
        
        averagesPanel.add(averagesScroll);
        wuPalmerPanel.add(wuPalmerScroll);
        cosinePanel.add(cosineScroll);
        synsetPanel.add(synsetScroll);
        occurrencePanel.add(occurrenceScroll);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Averages", averagesPanel);    
        tabbedPane.addTab("Cosine", cosinePanel); 
        tabbedPane.addTab("First sense", wuPalmerPanel); 
        tabbedPane.addTab("Maximum similarity", synsetPanel);
        tabbedPane.addTab("Table", occurrencePanel);
        
        frame.add(tabbedPane);
        frame.setVisible(true);
        frame.pack();
    }
    
    private void fillOccurrenceTable(int size) {
        try {
            //System.out.println("check 3");
            String word;
            String[] wordSplit;
            String[] argSplit;
            int argColNumber;
            
            //System.out.println("check 5");
            
            for(int i = 0; i < size; i++) {
                word = list.pop().toString();
                wordSplit = word.split(" ");
                occurrenceTable.setValueAt(wordSplit[0], i, 0);

                argSplit = wordSplit[1].split(",");
                for(int j = 0; j < argSplit.length; j++) {
                    argColNumber = Integer.parseInt(argSplit[j].substring(argSplit[j].lastIndexOf("(")+1, argSplit[j].lastIndexOf(")")));
                    occurrenceTable.setValueAt(argSplit[0].substring(0, argSplit[0].indexOf("(")), i, argColNumber + 1);
                }
                wordSplit = null;
                argSplit = null;
                word = null;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        //value row col
        
        
        
    }
    
    private void countSimilarity() throws FileNotFoundException, IOException {
       
        File excel =  new File (tablePath);
        //System.out.println("check 1");
        int rowCount = 0;
        int columnCount = 0;
        int currentCol = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(excel))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
                rowCount++;
                currentCol = Integer.parseInt(line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")")));
                if(currentCol > columnCount) {
                    columnCount = currentCol;
                }
            }
            br.close();
            System.out.println(columnCount);
        }
        
        //System.out.println("check 2");
        occurrenceTable = new JTable(rowCount, columnCount+2);  
        occurrenceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        
        fillOccurrenceTable(rowCount);
        
        //System.out.println("check 4");
        cosineTable = new JTable(rowCount+1, rowCount+1);  
        cosineTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        wuPalmerTable = new JTable(rowCount+1, rowCount+1);  
        wuPalmerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        averagesTable = new JTable(rowCount+1, rowCount+1);  
        averagesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        
        synsetTable = new JTable(rowCount+1, rowCount+1);
        synsetTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  

        
        for(int i = 0; i < rowCount; i++) {
            double [] vector1 = new double[columnCount+1];
            for(int k = 1; k < columnCount+2; k++) {
                if(occurrenceTable.getValueAt(i, k)!=null) {
                    vector1[k-1] = Double.parseDouble(occurrenceTable.getValueAt(i, k).toString());
                } else {
                    vector1[k-1] = 0.0;
                }
            }
            for(int j = i; j < rowCount; j++) {
                double [] vector2 = new double[columnCount+1];
                for(int k = 1; k < columnCount+2; k++) {
                    if(occurrenceTable.getValueAt(j, k)!=null) {
                        vector2[k-1] = Double.parseDouble(occurrenceTable.getValueAt(j, k).toString());
                    } else {
                        vector2[k-1] = 0.0;
                    }
                }
                //System.out.println(vector1.length + " " + vector2.length);
                double averages = 0;
                double cosine = cosine(vector1, vector2);

                double wuPalmer = wordNetSimilarity(occurrenceTable.getValueAt(j, 0).toString(), occurrenceTable.getValueAt(i, 0).toString());
                double synset = synsetSimilarity(occurrenceTable.getValueAt(j, 0).toString(), occurrenceTable.getValueAt(i, 0).toString());
                if(selectComboBox.getSelectedIndex() == 0) {
                    averages = (cosine + wuPalmer) / 2;
                } else {
                    averages = (cosine + synset) / 2;
                }
                cosineTable.setValueAt(occurrenceTable.getValueAt(j, 0), 0, j+1);
                cosineTable.setValueAt(occurrenceTable.getValueAt(j, 0), j+1, 0);
                wuPalmerTable.setValueAt(occurrenceTable.getValueAt(j, 0), 0, j+1);
                wuPalmerTable.setValueAt(occurrenceTable.getValueAt(j, 0), j+1, 0);
                averagesTable.setValueAt(occurrenceTable.getValueAt(j, 0), 0, j+1);
                averagesTable.setValueAt(occurrenceTable.getValueAt(j, 0), j+1, 0);   
                synsetTable.setValueAt(occurrenceTable.getValueAt(j, 0), 0, j+1);
                synsetTable.setValueAt(occurrenceTable.getValueAt(j, 0), j+1, 0);                    
                
                averagesTable.setValueAt(String.format("%.2f", averages), i+1, j+1);
                averagesTable.setValueAt(String.format("%.2f", averages), j+1, i+1);
                cosineTable.setValueAt(String.format("%.2f", cosine), i+1, j+1);
                cosineTable.setValueAt(String.format("%.2f", cosine), j+1, i+1);
                wuPalmerTable.setValueAt(String.format("%.2f", wuPalmer), i+1, j+1);
                wuPalmerTable.setValueAt(String.format("%.2f", wuPalmer), j+1, i+1);  
                synsetTable.setValueAt(String.format("%.2f", synset), i+1, j+1); 
                synsetTable.setValueAt(String.format("%.2f", synset), j+1, i+1); 
            }
       }   
        createTable();
    }
       
    private double cosine(double[] firstVector, double[] secondVector) {
        double top = 0;
        double bottomA = 0;
        double bottomB = 0;
        double result = 0;
        
        for(int i = 0; i < firstVector.length; i++) {
            top = (firstVector[i] * secondVector[i]) + top;
            bottomA = (firstVector[i] * firstVector[i]) + bottomA;
            bottomB = (secondVector[i] * secondVector[i]) + bottomB;
            System.out.println(firstVector[i] + " " + secondVector[i]);
        }
        
        
        result = (top/(sqrt(bottomA) * sqrt(bottomB)));
        
        //13,5/19,08
        return result;
                
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton computeButton;
    private javax.swing.JLabel computingLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JButton selectButton;
    private javax.swing.JComboBox selectComboBox;
    // End of variables declaration//GEN-END:variables
}
