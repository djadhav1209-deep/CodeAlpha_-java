import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class ChatGUI {

    private static Map<String, String> faq;

    static {
        faq = new HashMap<>();
        faq.put("hello", "Hello! How can I help you today?");
        faq.put("hi", "Hi there! How are you?");
        faq.put("how are you", "I am a bot, functioning perfectly!");
        faq.put("what is your name", "I am ChatGUI, your virtual assistant.");
        faq.put("bye", "Goodbye! Have a nice day!");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create frame
            JFrame frame = new JFrame("ChatGUI Bot");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 500);
            frame.setLocationRelativeTo(null);

            // Chat area
            JTextArea chatArea = new JTextArea();
            chatArea.setEditable(false);
            chatArea.setLineWrap(true);
            chatArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(chatArea);

            // Input field
            JTextField userInput = new JTextField();
            JButton sendButton = new JButton("Send");

            // Input panel
            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(userInput, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            // Main layout
            frame.setLayout(new BorderLayout());
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(inputPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

            // Ensure input field has focus
            userInput.requestFocusInWindow();

            // Send action
            ActionListener sendAction = e -> {
                String input = userInput.getText().trim();
                if (input.isEmpty()) return;

                chatArea.append("You: " + input + "\n");
                String normalized = input.toLowerCase().replaceAll("[^a-zA-Z ]", "");

                boolean answered = false;
                for (String key : faq.keySet()) {
                    if (normalized.contains(key)) {
                        chatArea.append("Bot: " + faq.get(key) + "\n\n");
                        answered = true;
                        break;
                    }
                }
                if (!answered) {
                    chatArea.append("Bot: Sorry, I don't understand. Can you rephrase?\n\n");
                }

                userInput.setText("");
                userInput.requestFocusInWindow(); // Keep focus on input field
            };

            sendButton.addActionListener(sendAction);
            userInput.addActionListener(sendAction); // Enter key works
        });
    }
}
