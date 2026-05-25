// Class      : Main
// Creator    : [Member 4 Name]  ← Member 4 owns this file
// Tester     : [Member 1 Name]
// Description: Entry point for the SDG 3 Health & Well-Being desktop app.
//              Sets up the main JFrame with smartphone-sized dimensions
//              and launches the Learning Module.
//              Run from command line: javac src/*.java && java -cp src Main

import java.awt.*;
import javax.swing.*;

public class Main {

    // Smartphone-like resolution as required by project spec
    private static final int APP_WIDTH  = 390;
    private static final int APP_HEIGHT = 700;

    public static void main(String[] args) {
        // Run on the Event Dispatch Thread (EDT) — Swing best practice
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fall back to default look and feel
            }

            JFrame frame = new JFrame("SDG 3 — Health & Well-Being App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(APP_WIDTH, APP_HEIGHT);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // centre on screen

            // Use a CardLayout for the main app to allow switching between modules
            CardLayout mainLayout = new CardLayout();
            JPanel mainContainer = new JPanel(mainLayout);

            // ── Learning Module (Victoria Ngui Fong Eik - 106647) ────────────────────────────────────
            LearningModulePanel learningPanel = new LearningModulePanel(() -> {
                // Called when all 10 topics are complete — switch to Quiz
                JOptionPane.showMessageDialog(frame,
                    "Great work! You have completed all learning topics.\n" +
                    "Now let's test your knowledge with a quiz!",
                    "Learning Complete!", JOptionPane.INFORMATION_MESSAGE);
                mainLayout.show(mainContainer, "QUIZ");
            });

            // ── Placeholder panels for other modules ──────────────────────────
            // These will be replaced by Members 2, 3, and 4's actual panels

            JPanel quizPlaceholder = createPlaceholder(
                "Quiz Module", "Member 2 — QuizManager",
                new Color(0x7F77DD), new Color(0xEEEDFE));

            JPanel gamePlaceholder = createPlaceholder(
                "Gamification", "Member 3 — GamificationEngine",
                new Color(0xBA7517), new Color(0xFAEEDA));

            JPanel profilePlaceholder = createPlaceholder(
                "User Profile", "Member 4 — UserProfile",
                new Color(0xD85A30), new Color(0xFAECE7));

            mainContainer.add(learningPanel,     "LEARNING");
            mainContainer.add(quizPlaceholder,   "QUIZ");
            mainContainer.add(gamePlaceholder,   "GAME");
            mainContainer.add(profilePlaceholder,"PROFILE");

            // Start on Learning Module
            mainLayout.show(mainContainer, "LEARNING");

            frame.add(mainContainer);
            frame.setVisible(true);

            System.out.println("SDG 3 Health & Well-Being App started.");
            System.out.println("Window size: " + APP_WIDTH + "x" + APP_HEIGHT + " (smartphone display)");
        });
    }

    // Creates a placeholder panel for modules not yet implemented
    private static JPanel createPlaceholder(String moduleName, String owner,
                                             Color accent, Color bg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel icon = new JLabel("🚧", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel(moduleName, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(accent.darker());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel(owner, SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 14));
        sub.setForeground(accent);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel note = new JLabel("<html><center>This module will be implemented<br>by the assigned group member.</center></html>",
            SwingConstants.CENTER);
        note.setFont(new Font("Arial", Font.PLAIN, 12));
        note.setForeground(new Color(0x888888));
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(16));
        panel.add(title);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sub);
        panel.add(Box.createVerticalStrut(20));
        panel.add(note);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}
