import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * AI Chatbot - Java Swing GUI + Google Gemini API
 * BCA (Data Science) - Java Programming Practicals project.
 *
 * How to run:
 *   1) javac ChatbotGUI.java GeminiAPI.java
 *   2) java ChatbotGUI
 *   3) Paste your free Gemini API key when asked (get one at
 *      https://aistudio.google.com/apikey)
 */
public class ChatbotGUI extends JFrame {

    private JTextPane chatPane;
    private JTextField inputField;
    private JButton sendButton;
    private JLabel statusLabel;
    private final GeminiAPI api;

    public ChatbotGUI(String apiKey) {
        super("AI Chatbot - Java Swing + Gemini API");
        this.api = new GeminiAPI(apiKey);
        buildUI();
        appendMessage("Bot", "Hi! I'm your AI assistant. Ask me anything.");
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatPane.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(this::onSend);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this::onSend);

        JPanel inputRow = new JPanel(new BorderLayout(6, 6));
        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(sendButton, BorderLayout.EAST);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);

        JPanel bottomPanel = new JPanel(new BorderLayout(6, 6));
        bottomPanel.add(inputRow, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onSend(ActionEvent e) {
        String message = inputField.getText().trim();
        if (message.isEmpty()) return;

        appendMessage("You", message);
        inputField.setText("");
        setInputEnabled(false);
        statusLabel.setText("Bot is typing...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return api.getReply(message);
            }

            @Override
            protected void done() {
                try {
                    appendMessage("Bot", get());
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    appendMessage("Bot", "Sorry, something went wrong: " + cause.getMessage());
                }
                statusLabel.setText(" ");
                setInputEnabled(true);
                inputField.requestFocusInWindow();
            }
        };
        worker.execute();
    }

    private void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }

    private void appendMessage(String sender, String message) {
        StyledDocument doc = chatPane.getStyledDocument();

        Style nameStyle = chatPane.addStyle("name", null);
        StyleConstants.setBold(nameStyle, true);
        StyleConstants.setForeground(nameStyle, sender.equals("You")
                ? new Color(0, 90, 200) : new Color(20, 130, 60));

        Style textStyle = chatPane.addStyle("text", null);
        StyleConstants.setForeground(textStyle, Color.BLACK);

        try {
            doc.insertString(doc.getLength(), sender + ": ", nameStyle);
            doc.insertString(doc.getLength(), message + "\n\n", textStyle);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        chatPane.setCaretPosition(doc.getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String key = System.getenv("GEMINI_API_KEY");
            if (key == null || key.isBlank()) {
                key = JOptionPane.showInputDialog(
                        null,
                        "Enter your Gemini API key:\n(Free key: https://aistudio.google.com/apikey)",
                        "API Key Required",
                        JOptionPane.PLAIN_MESSAGE
                );
                if (key == null || key.isBlank()) {
                    JOptionPane.showMessageDialog(null, "An API key is required to run the chatbot.");
                    System.exit(0);
                    return;
                }
            }
            new ChatbotGUI(key).setVisible(true);
        });
    }
}
