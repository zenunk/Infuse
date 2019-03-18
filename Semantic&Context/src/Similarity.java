package Similarity;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;

public class Similarity {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Semantic similarity");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        JPanel panel = new GUI();
        frame.add(panel);
        frame.pack();  
    }
    
}
