
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import jxl.Cell;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class Occurrence extends javax.swing.JFrame {
    //Path to the folder where pdf files are
    String pdfFolderPath;
    //List of words that we get from pdf files
    List<String> words = new LinkedList<String> ();
    //Custom stripper from main class that we use to get words from pdf file
    PDFTextStripper stripper;
    //File reference for excel table
    File tableFile;
    //excel sheet
    WritableSheet excelSheet;
    //excel workbook
    WritableWorkbook workbook;
    //Excel reference for last eddited column and row
    int currentColumn;
    int currentRow;
    //Excel cells for referencing used and empty cells
    Cell cell, secondCell, dataCell, currentCell;
    //Initializing the frame window and setting the pdf stipper to this class
    public Occurrence(PDFTextStripper stripper) {
        this.stripper = stripper;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        folderButton = new javax.swing.JButton();
        folderPathTextField = new javax.swing.JTextField();
        startJButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Occurrence");
        setName("frame"); // NOI18N
        setResizable(false);

        outputTextArea.setEditable(false);
        outputTextArea.setColumns(20);
        outputTextArea.setRows(5);
        outputTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));
        jScrollPane1.setViewportView(outputTextArea);

        folderButton.setText("Select folder");
        folderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                folderButtonActionPerformed(evt);
            }
        });

        folderPathTextField.setEditable(false);
        folderPathTextField.setText("Folder Path");

        startJButton.setText("Start");
        startJButton.setEnabled(false);
        startJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startJButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(folderButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(folderPathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(startJButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(folderButton)
                    .addComponent(folderPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startJButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //Button click listener
    private void folderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_folderButtonActionPerformed
        //Creates a new file chooser to select the pdf folder
        JFileChooser chooser = new JFileChooser(); 
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select PDF folder");
        //Lets you select only directories, no files
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        //When ok is pressed data is saved
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            //Setting the pdf folder path
            pdfFolderPath = chooser.getSelectedFile().toString();
            //Setting the text for text field to show selected folder
            folderPathTextField.setText(pdfFolderPath);
            //Everything is set, start button is enabled
            startJButton.setEnabled(true);
        }        
    }//GEN-LAST:event_folderButtonActionPerformed
    //Start button click listener  
    private void startJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startJButtonActionPerformed
        //Disables start button
        startJButton.setEnabled(false);
        //Gets pdf folder path and creates an empty excel table in that path
        File dir = new File(pdfFolderPath);
        createNewTable(pdfFolderPath);
        //Puts all files that are in the selected folder to an array
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            //For every file in selected folder:
            for (File child : directoryListing) {
                //We ignore the created table
                if(!child.getName().equals("Table.xls")) {
                    writeOutputText("Processing file: " + child.getName());
                    //Calling the main method, sending only the name of the pdf file, which is being processed
                    stripText(child.getName());
                    writeOutputText(child.getName() + " Processing done!");
                    writeOutputText("------------------------");
                }
            }
        }
        //Some output when the cycle is done
        writeOutputText("All files processed! Output directory:");
        writeOutputText(pdfFolderPath);
        try {
            //Finishing up writing the excel file and closing that file
            workbook.write();
            workbook.close();
        } catch (IOException ex) {
            Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startJButtonActionPerformed
    //Main method that is being called after start button is pressed, input file path
    //is the name of the pdf file
    private void stripText(String filePath) {
        try {
            //we load an pdf with a given name from the selected pdf folder
            PDDocument document = new PDDocument().load(new File(pdfFolderPath + "\\" + filePath));
            //We use stripper method (defined in main class) to get the text from the pdf
            String text = stripper.getText(document);
            System.out.print(text);
            //We send the whole pdf text to another method to process the text further
            //Also sending pdf file name (used for creating output)
            countWordOccurrences(text, filePath);
            //We close this document, releasing the resources
            document.close();
        } catch (IOException ex) {
            Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method to write some output in the text area of the frame in a seperate thread
    private void writeOutputText(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputTextArea.append(text);
                outputTextArea.append(System.getProperty("line.separator"));
                outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());  
                outputTextArea.revalidate();
                outputTextArea.repaint();
            }
        });   
    }
    //Method to add two counted values of words, when they are combined
    private double add(String s1, String s2) {
        double first = Double.parseDouble(s1);
        double second = Double.parseDouble(s2);
        double result = first + second;
        return result;
    }
    //Checks if the given string contains a double type number
    private boolean isDouble(String string) {
        try {
            //if parsing method goes without error it means that string indeed
            //contains a double
            Double.parseDouble(string);
            return true;
        }
        //If we encounter an error, there is no number in the string
        catch(NumberFormatException e) {
            return false;
        }
    }   
    //method to add up all the repetetive words in our word list
    private void countRepetitives() {
        //List is being sorted in order, so it would be easier to add the 
        //same ones
        Collections.sort(words);
        //We convert our list to an array
        Object[] wordArray = words.toArray();
        //and create an array to store word values
        String[] wordStringArray = new String[wordArray.length];
        //and an array to store the corresponding numeric values
        String[] countedSums = new String[wordArray.length];
        //We empty out our word list, as we will refill the counted list afterwards
        words.clear();
        
        for(int i = 0; i < wordArray.length; i++) {
            //for each word that was in our list we seperate the word from its 
            //numeric value and store them to their corresponding arrays
            String[] wordSplit = wordArray[i].toString().split(" ");
            //Some words from tagger method come in as formatting data
            //which does not have a numeric value where numeric value shoud be
            if(isDouble(wordSplit[1])) {
                //if the word is with a numeric value we add it to word and numbers arrays
                //wordSplit[0] corresponds to the word and wordSplit[1] - numeric value
                wordStringArray[i] = wordSplit[0];
                countedSums[i] = wordSplit[1];
                //if its formating data - we discard it
            } else {
                wordStringArray[i] = "";
                countedSums[i] = "0";
            }
        }
        //When we have our arrays filled we cound the reoccurring values and add them up
        for(int i = 0; i < wordArray.length - 1; i++) {
            //if the first element is equal to the next one, we delete the first value
            //and add the first and second word numeric values
            //then we delete the first word from the list. We can do this because our 
            //words are listed in order
            if(wordStringArray[i].equals(wordStringArray[i+1])) {
                wordStringArray[i] = null;
                countedSums[i+1] = "" + add(countedSums[i], countedSums[i+1]);
                countedSums[i] = null;
            } else {
                //Else if the first element is not equal to the second, and it has a numeric value
                //it means that there is just one occurrence of this word left so we add it to our word list
                if(!countedSums[i].equals("0")) {
                    words.add(wordStringArray[i] + " " + countedSums[i]);
                }
            }
        }
        //we add the last occurring word to the list, because our loop doesn't cover it
        words.add(wordStringArray[wordArray.length-1] + " " + countedSums[wordArray.length-1]);
    }
    //method to add new pdf column and append word list with a new pdf file
    //input is path to the text file, which has all the information about words in a pdf file
    //and a file name for column name
    private void addToTable(String textFilePath, String pdfFileName) {
        try {
            //To the next empty column we add our pdf file name
            excelSheet.addCell(new Label(currentColumn, 0, pdfFileName));
            //Here we read the text file line by line to add to our sheet
            try(BufferedReader br = new BufferedReader(new FileReader(textFilePath))) {
                for(String line; (line = br.readLine()) != null; ) {
                    if(!line.equals("") || !line.equals(" ")) {
                        //Line is word value format, so we split it at blank space
                        String[] lineSplit = line.split(" ");
                        //append the first value of the line (word) to our word column
                        excelSheet.addCell(new Label(0, currentRow, lineSplit[0]));
                        //adding numeric values to the corresponding word in the word column
                        excelSheet.addCell(new Label(currentColumn, currentRow, lineSplit[1]));
                        //our current row goes up for later refference for next word
                        currentRow++;
                    }
                }
                //after all the words are added, column number goes up for next pdf file
                currentColumn++;
                //we call this method to add zeros to all empty cells
                addZeros();
                //we call this method to find repeating words in our table and add up their values
                excelMatch();
        }   catch (WriteException ex) {            
                Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
            }            
            
        } catch (IOException | WriteException ex) {
            Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Method to add zeros to all empty cells. We need to do this because when we add a new
    //pdf file, only that pdfs column has word numeric values, so we need to add numbers
    //to all the first columns so we could add them later
    private void addZeros() {
        //for each cell that is before current row (each cell until last word in the sheet)
        for(int i = 1; i < currentRow; i++) {
            //and for each column before current column (last column with a pdf file
            for(int j = 1; j < currentColumn; j++) {
                cell = excelSheet.getCell(j, i);
                //if any cell has empty value we write a zero in that cell
                if(cell.getContents().equals("")) {
                    try {
                        excelSheet.addCell(new Label(j, i, "0.0"));
                    } catch (WriteException ex) {
                        Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }        
    }
    //This method matches all the appended words in the word list with the words
    //that already were in the word list
    private void excelMatch() throws WriteException {
        for(int i = 1; i < currentRow; i++) {
            //for each cell in our word list we reference a cell
            cell = excelSheet.getCell(0, i);
            for(int j = i + 1; j < currentRow; j++) {
                //then we reference each second word
                //its a cycle that we pick the first word and then go through
                //every other word to check if they are equal
                secondCell = excelSheet.getCell(0, j);
                //so if our first cell contains the same word as any other following word
                if(cell.getContents().equals(secondCell.getContents())) {
                    //we select whole row where the occurring word is
                    for(int k = 1; k < currentColumn; k++) {
                        //and refference all the numeric values in that row
                        dataCell = excelSheet.getCell(k, j);
                        if(!dataCell.getContents().equals("0.0")) {
                            //and if those numeric values arent zeros push numeric values
                            //from the down word to the first occurrence in the correct pdf column
                            excelSheet.addCell(new Label(k, i, dataCell.getContents()));
                            //then we remove that second row 
                            excelSheet.removeRow(j);
                            //and shrink our current row cound, because excel shrinks the table
                            //when a row is deleted
                            currentRow--;
                        }
                    }             
                }
            }
        }
    }
    //Method to create a new excel table in our pdf folder path
    private void createNewTable(String folderPath) {
        try {
            File file = new File(folderPath, "Table.xls");
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("Occurrence matrix", 0);
            excelSheet = workbook.getSheet(0);
            //we set our current column 1, because column 0 is reserved for our pdf file names
            currentColumn = 1;
            //same with the row, so it wont mix with pdf file names
            currentRow = 1;
        } catch (IOException ex) {
            Logger.getLogger(Occurrence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Removes all not noun words from our word list using tagger
    private void removeNonNouns() {
        try {
            //IMPORTANT! you need to specify the path of our tagger as it's not a library file
            //we can't add this to our libraries
            //If this path is not correct, our output will be all words, not just nouns
            MaxentTagger tagger = new MaxentTagger("lib\\Taggers\\left3words-wsj-0-18.tagger");
            //we create an array from our word list and delete the list
            //so we can fill it up with only nouns later
            Object[] allWords = words.toArray();
            words.clear();
            String tagged = "";
            for(int i = 0; i < allWords.length; i++) {
                //we split our word from its numeric value 
                String[] wordSplit = allWords[i].toString().split(" ");
                //if the word isn't empty we use the tagger to check if it's a noun
                if(!wordSplit[0].equals("")) { 
                    tagged = tagger.tagString(wordSplit[0]);
                    //if the word is a noun, tagger appeds /NN to the end of it
                    //so if the tagged word ends in NN, it means it's a noun
                    if(tagged.endsWith("NN ")) {
                        //we remove the /NN part, get the numeric value
                        //and put the joint string to our word list
                        tagged = tagged.replace("/NN ", "");
                        words.add(tagged + " " + wordSplit[1]);
                    }
                }
            }
        } catch (IOException ex) {            
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }      
    //method to remove all additional symbols from the word
    private String strip(String input) {
        if(input.contains("\"")) {
            input = input.replace("\"", "");
        }
        if(input.contains("*")) {
            input = input.replace("*", "");
        }
        if(input.contains(".")) {
            input = input.replace(".", "");
        }
        if(input.contains(",")) {
            input = input.replace(",", "");
        }        
        if(input.contains(";")) {
            input = input.replace(";", "");
        }
        if(input.contains(":")) {
            input = input.replace(":", "");
        }        
        if(input.contains("(")) {
            input = input.replace("(", "");
        }   
        if(input.contains(")")) {
            input = input.replace(")", "");
        }           
        //Basicly we replace any not wanted symbols to empty space
        //and return lower case value of the word so there won't be any
        //mix ups between lower and upper case words
        return input.toLowerCase();
    } 
    //This method counts every words value 
    //input is whole pdf text
    private void splitToBlocks(String text) {
        //we have our text parameters between x%% seperators so we
        //split the text by these seperators
        String[] textBlocks = text.split("x%%");
        Scanner scanner;
        //every other string of the pdf text is actual pdf text and the other part is formatting data
        for(int i = 1; i < textBlocks.length; i = i + 2) {
            //we split the formatting data as it comes:
            //Bold size or Italic size or Underline size or Regular size
            //So by each format we check for size and give the correct value
            //This part is for bold words
            String[] dataBlock = textBlocks[i].split(" ");
            if(dataBlock[0].equals("Bold")) {
                if(Double.parseDouble(dataBlock[1]) < 10) {
                    //The scanner reads word by word from pdf string block
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        //and adds them to the word list with words value
                        words.add(strip(word) + " 2.0"); 
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 14 && Double.parseDouble(dataBlock[1]) > 9) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.25");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 18 && Double.parseDouble(dataBlock[1]) > 13) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.5");
                    }      
                } else if(Double.parseDouble(dataBlock[1]) > 17) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 3.0");
                    }      
                }
                //Italic words
            } else if(dataBlock[0].equals("Italic")) {
                if(Double.parseDouble(dataBlock[1]) < 10) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.5");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 14 && Double.parseDouble(dataBlock[1]) > 9) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.75"); 
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 18 && Double.parseDouble(dataBlock[1]) > 13) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.0");
                    }      
                } else if(Double.parseDouble(dataBlock[1]) > 17) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.25");
                    }      
                }
                //Underlined words
            } else if(dataBlock[0].equals("Underline")) {
                if(Double.parseDouble(dataBlock[1]) < 10) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.75");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 14 && Double.parseDouble(dataBlock[1]) > 9) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.0");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 18 && Double.parseDouble(dataBlock[1]) > 13) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.25");
                    }      
                } else if(Double.parseDouble(dataBlock[1]) > 17) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.5");
                    }      
                }
                //Regular words
            } else if(dataBlock[0].equals("Regular")) {
                    if(Double.parseDouble(dataBlock[1]) < 10) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.25");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 14 && Double.parseDouble(dataBlock[1]) > 9) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.5");
                    }
                } else if(Double.parseDouble(dataBlock[1]) < 18 && Double.parseDouble(dataBlock[1]) > 13) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 1.75");
                    }      
                } else if(Double.parseDouble(dataBlock[1]) > 17) {
                    scanner = new Scanner(textBlocks[i+1]);
                    while(scanner.hasNext()) {
                        String word = scanner.next();
                        words.add(strip(word) + " 2.0");
                    }      
                }               
            }            
        }
    }  
    //Method to describe the flow of program
    private void countWordOccurrences(String text, String fileName) {
        //Splits the text into different format blocks
        splitToBlocks(text);
        //Removes non nouns
        removeNonNouns();
        //and count reoccurring words
        countRepetitives();
        try {
            //then write the word list to a file
            writeToFile(fileName);
        } catch (IOException ex) {
            writeOutputText("Failed to save output file");
            ex.printStackTrace();
        }
        words.clear();
    }  
    
    //Method to write all pdf words with their values to a txt file with the same name
    private void writeToFile(String fileName) throws IOException {
        String outputPath = pdfFolderPath + "/" + fileName.replace(".pdf", ".txt");
        File file = new File(pdfFolderPath + "/", fileName.replace(".pdf", ".txt"));
        PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
        Object[] wordList = words.toArray();
        for (int i = 0; i < wordList.length; i++) {
            writer.println(wordList[i].toString());   
            }          
        writer.close();
        addToTable(file.getAbsolutePath(), fileName);
    }
    //Main class, initiates the frame window and has custom stripper
    public static void main(String args[]) throws IOException {

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
            java.util.logging.Logger.getLogger(Occurrence.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Occurrence.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Occurrence.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Occurrence.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //This text stripper reads each word and its font information, if the next words font is the same
        //as current word font, it doesnt write anything, but if the font changes, the whole word block with the
        //same font info is assigned one font value, and as font changes, blocks are assigned values, which are 
        //later chopped up in above methods
        PDFTextStripper stripper = new PDFTextStripper() {
        String prevBaseFont = "";
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            StringBuilder builder = new StringBuilder();
            for (TextPosition position : textPositions) {
                String baseFont = position.getFont().getBaseFont();
                //This is the font format, it generaly goes like
                //timesNewRoman-aft-56412BOLD-NT ect to we generaly only want the bold part
                //and remove all other irrelevant information
                if(baseFont.contains("Bold")) {
                    baseFont = "Bold";
                } else
                if(baseFont.contains("Italic")) {
                    baseFont = "Italic";
                } else
                if(baseFont.contains("Underline")) {
                    baseFont = "Underline";
                } else baseFont = "Regular";
                //after we got font info, we get font size for each block, as the blocks
                //also change if the size changes
                float baseSize = position.getFontSizeInPt();
                baseFont = baseFont + " " + baseSize;

                if (baseFont != null && !baseFont.equals(prevBaseFont)) {
                    builder.append("x%%").append(baseFont).append("x%%");
                    prevBaseFont = baseFont;
                }
                builder.append(position.getCharacter());
            }
            writeString(builder.toString());
        }};

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Occurrence(stripper).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton folderButton;
    private javax.swing.JTextField folderPathTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextArea outputTextArea;
    private javax.swing.JButton startJButton;
    // End of variables declaration//GEN-END:variables
}
