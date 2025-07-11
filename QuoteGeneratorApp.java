import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class QuoteGeneratorApp {
    public static void main(String[] args) {
        new QuoteGenerator(); 
    }
}

class QuoteGenerator extends JFrame {
    Container c;
    JButton btnCoding, btnHappy, btnConfidence, btnWork;
    JTextArea quoteArea;

    QuoteGenerator() {
        c = getContentPane();
        c.setLayout(null);  // Absolute positioning

        // Title label
        JLabel title = new JLabel("Quote Generator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBounds(100, 20, 400, 40);
        c.add(title);

        // Buttons (in 2x2 grid)
        btnCoding = new JButton("Coding Quotes");
        btnHappy = new JButton("Happy Quotes");
        btnConfidence = new JButton("Confidence Quotes");
        btnWork = new JButton("Work Quotes");

        btnCoding.setBounds(100, 80, 180, 50);
        btnHappy.setBounds(320, 80, 180, 50);
        btnConfidence.setBounds(100, 150, 180, 50);
        btnWork.setBounds(320, 150, 180, 50);

        c.add(btnCoding);
        c.add(btnHappy);
        c.add(btnConfidence);
        c.add(btnWork);

        // Quote display area (larger but no scroll)
        quoteArea = new JTextArea();
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setEditable(false);
        quoteArea.setFont(new Font("Serif", Font.PLAIN, 18));
        quoteArea.setMargin(new Insets(10, 10, 10, 10));
        quoteArea.setBounds(80, 230, 440, 100);  // Resized for bigger layout
        c.add(quoteArea);

        // ActionListener
        ActionListener fetchQuote = e -> {
            String category = "";

            if (e.getSource() == btnCoding) category = "Coding";
            else if (e.getSource() == btnHappy) category = "Happy";
            else if (e.getSource() == btnConfidence) category = "Confidence";
            else if (e.getSource() == btnWork) category = "Work";

            String quote = getQuoteByCategory(category);
            quoteArea.setText(quote);
        };

        btnCoding.addActionListener(fetchQuote);
        btnHappy.addActionListener(fetchQuote);
        btnConfidence.addActionListener(fetchQuote);
        btnWork.addActionListener(fetchQuote);

        // Frame setup
        setTitle("Quote Generator");
        setSize(600, 500);  // Updated size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private String getQuoteByCategory(String category) {
        String quote = "No quote found.";
        String url = "jdbc:mysql://localhost:3306/quote_generator?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "Kitten@0203";

        try (
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = con.prepareStatement(
                "SELECT quote FROM quotes WHERE category = ? ORDER BY RAND() LIMIT 1")
        ) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                quote = rs.getString("quote");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(c, "Database Error: " + ex.getMessage());
        }

        return quote;
    }
}
