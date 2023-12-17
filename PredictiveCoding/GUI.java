import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUI {
    static PredictiveCoding predictiveCoding = new PredictiveCoding();

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Vector Quantization Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(350, 450));
        frame.setLayout(new FlowLayout());

        JTextField inputField = new JTextField("image.jpg", 20);

        JButton compressButton = new JButton("Compress");
        JButton decompressButton = new JButton("Decompress");
        compressButton.setBackground(Color.BLUE);
        decompressButton.setBackground(Color.BLACK);
        compressButton.setForeground(Color.WHITE);
        decompressButton.setForeground(Color.WHITE);

        compressButton.addActionListener(e -> {
            String inputFile = inputField.getText();
            if (fileExist(inputFile)) {
                compress(inputFile);
            } else {
                showError();
            }
        });
        decompressButton.addActionListener(e -> {
            String inputFile = inputField.getText();
            if (fileExist(inputFile)) {
                try {
                    decompress(inputFile);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showError();
            }
        });

        frame.add(new JLabel("Input file: "));
        frame.add(inputField);
        frame.add(compressButton);
        frame.add(decompressButton);

        frame.pack();
        frame.setVisible(true);
    }

    private static boolean fileExist(String inputFile) {
        File file = new File(inputFile);
        return file.exists();
    }

    private static void showError() {
        JOptionPane.showMessageDialog(null, "Input file does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void compress(String inputFile) {
        predictiveCoding.compress(inputFile);
        JOptionPane.showMessageDialog(null, "Compression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void decompress(String inputFile) throws IOException {
        predictiveCoding.decompress(inputFile);
        JOptionPane.showMessageDialog(null, "Decompression completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
