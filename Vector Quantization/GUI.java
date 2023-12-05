import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GUI {
    static VectorQuantization vectorQuantization = new VectorQuantization();

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Vector Quantization Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(350, 450));
        frame.setLayout(new FlowLayout());

        JTextField inputField = new JTextField("outputC.txt", 20);
        JTextField outputField = new JTextField("outputD.txt", 20);

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
                    try {
                        decompress(inputFile, outputFile);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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

    private static boolean fileExist(String inputFile, String outputFile) {
        File file = new File(inputFile);
        return file.exists();
    }

    private static void showError() {
        JOptionPane.showMessageDialog(null, "Input file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void compress(String inputFile, String outputFile) {
        vectorQuantization.compress(inputFile, outputFile);
        JOptionPane.showMessageDialog(null, "Compression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void decompress(String inputFile, String outputFile) throws IOException {
        vectorQuantization.decompress(inputFile, outputFile);
        JOptionPane.showMessageDialog(null, "Decompression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
