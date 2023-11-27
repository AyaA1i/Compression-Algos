import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI{
    public static void createAndShowGUI()  {
        JFrame frame = new JFrame("Huffman Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(350, 450));
        frame.setLayout(new FlowLayout());

        JTextField inputField = new JTextField("input.txt", 20);
        JTextField outputField = new JTextField("output.txt", 20);

        JButton compressButton = new JButton("Compress");
        JButton decompressButton = new JButton("Decompress");
        compressButton.setBackground(Color.BLUE);
        decompressButton.setBackground(Color.BLACK);
        compressButton.setForeground(Color.WHITE);
        decompressButton.setForeground(Color.WHITE);

        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFile = inputField.getText();
                String outputFile = outputField.getText();
                if (fileExist(inputFile, outputFile)) {
                    compress(inputFile, outputFile);
                } else {
                    showError();
                }
            }
        });
        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFile = inputField.getText();
                String outputFile = outputField.getText();
                if (fileExist(inputFile, outputFile)) {
                    decompress(inputFile, outputFile);
                } else {
                    showError();
                }
            }
        });

        frame.add(new JLabel("Input file: "));
        frame.add(inputField);
        frame.add(new JLabel("Output file: "));
        frame.add(outputField);
        frame.add(compressButton);
        frame.add(decompressButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static boolean fileExist(String inputFile, String outputFile){
        File file = new File(inputFile);
        File file2 = new File(outputFile);
        if(!file2.exists()){
            File output = new File(outputFile);
        }
        return file.exists();
    }
    private static void showError() {
        JOptionPane.showMessageDialog(null, "Input file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    private static void compress(String inputFile, String outputFile) {
        StandardHuffman standardHuffman = new StandardHuffman();
        try {
            standardHuffman.compress(inputFile, outputFile);
            JOptionPane.showMessageDialog(null, "Compression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void decompress(String inputFile, String outputFile) {
        StandardHuffman standardHuffman = new StandardHuffman();
        standardHuffman.decompress(inputFile, outputFile);
        JOptionPane.showMessageDialog(null, "Decompression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
