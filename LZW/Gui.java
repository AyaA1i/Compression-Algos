import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Gui extends JFrame implements ActionListener {
    private JTextField textField;
    private JButton button1;
    private JButton button2;

    public Gui() {
        JFrame frame = new JFrame("LZW");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        JPanel buttonPanel = new JPanel();
        textField = new JTextField(5);
        JLabel label = new JLabel("Enter the name of the file:");
        button1 = new JButton("Compress");
        button2 = new JButton("Decompress");
        button1.addActionListener(this);
        button2.addActionListener(this);
        button1.setBackground(Color.BLUE);
        button2.setBackground(Color.BLACK);
        button1.setForeground(Color.WHITE);
        button2.setForeground(Color.WHITE);
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(textField, BorderLayout.CENTER);
        frame.add(label, BorderLayout.NORTH);
        frame.add(textField, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String inputText = textField.getText();
        LZW.readFiles(inputText);
        if (e.getSource() == button1) {
            try {
                LZW.compress();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == button2) {
            try {
                LZW.decompress();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        new Gui();
    }
}
