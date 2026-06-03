// ============================================================
// Class      : Main
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: The entry point for the SDG 3 Health &
//              Well-Being desktop application.
//
//              This class is owned by Member 4. It wires all
//              four modules together so the app works as one:
//
//              1. Creates the main JFrame window
//                 (smartphone-sized: 390 x 700 pixels)
//              2. Creates a UserProfile to track the user's
//                 quiz scores and learning progress
//              3. Uses CardLayout to switch between all screens:
//                 LEARNING, QUIZ, GAME, PROFILE
//              4. Connects each module so they can communicate:
//                 - Learning tells UserProfile when a topic is done
//                 - Quiz sends the final score to GamificationEngine
//                   and also saves it to UserProfile
//              5. Builds the User Profile screen (Member 4 GUI)
//
//              To run from command line:
//                 javac -d out src/*.java
//                 java -cp out Main
// ============================================================

import java.awt.*;
import javax.swing.*;

public class Main {

    // App window dimensions — smartphone size as required by spec
    private static final int APP_WIDTH  = 390;
    private static final int APP_HEIGHT = 700;

    public static void main(String[] args) {

        // All Swing GUI code must run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {

            // Try to use the operating system's native look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Falls back to default Swing look and feel — safe to ignore
            }

            // --------------------------------------------------
            // Create the main application window
            // --------------------------------------------------
            JFrame frame = new JFrame("SDG 3 — Health & Well-Being App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(APP_WIDTH, APP_HEIGHT);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // centre on screen

            // --------------------------------------------------
            // Member 4: Create the UserProfile
            // Tracks the user's quiz score and completed topics.
            // Uses "Guest" as the default name (no login screen yet).
            // --------------------------------------------------
            UserProfile userProfile = new UserProfile("Guest", "000000");

            // --------------------------------------------------
            // Set up CardLayout to switch between all screens.
            // Each module is a "card" — we show it by its name.
            // --------------------------------------------------
            CardLayout mainLayout    = new CardLayout();
            JPanel     mainContainer = new JPanel(mainLayout);

            // --------------------------------------------------
            // Member 3: GamificationEngine
            // Created first because the Quiz needs to call it
            // when the quiz finishes.
            // --------------------------------------------------
            GamificationEngine gamePanel = new GamificationEngine(() -> {
                // "Back to Home" in the reward screen goes back to Learning
                mainLayout.show(mainContainer, "LEARNING");
            });

            // --------------------------------------------------
            // Member 2: QuizManager
            // Uses an array holder so the callback can call
            // methods on the QuizManager object itself.
            // --------------------------------------------------
            final QuizManager[] quizHolder = new QuizManager[1];

            quizHolder[0] = new QuizManager(() -> {

                // Quiz is finished — collect the result
                int score   = quizHolder[0].getScore();
                int total   = quizHolder[0].getTotal();
                int percent = quizHolder[0].getPercent();

                // Member 4: Save the quiz result to UserProfile
                userProfile.setQuizScore(score, total);
                userProfile.saveQuizScore();

                // Print a progress summary to the console (for testing / debugging)
                System.out.println("[Quiz complete] " + userProfile.getProgress());
                System.out.println("[UserProfile]   " + userProfile.toString());

                // Member 3: Show the reward / gamification screen
                gamePanel.showReward(score, total, percent);
                mainLayout.show(mainContainer, "GAME");
            });

            QuizManager quizPanel = quizHolder[0];

            // --------------------------------------------------
            // Member 1: LearningModulePanel
            // Three callbacks:
            //   onAllComplete    : all 10 topics finished → show message → go to quiz
            //   onGoToQuiz       : "Take the Quiz" button pressed → go to quiz
            //   onTopicComplete  : one topic finished → tell UserProfile (Member 4)
            // --------------------------------------------------
            LearningModulePanel learningPanel = new LearningModulePanel(
                () -> {
                    // All topics done
                    JOptionPane.showMessageDialog(frame,
                        "Great work! You have finished all learning topics.\n"
                        + "Now let's test your knowledge with a quiz!",
                        "Learning Complete!", JOptionPane.INFORMATION_MESSAGE);
                    mainLayout.show(mainContainer, "QUIZ");
                },
                () -> {
                    // "Take the Quiz" button pressed on any topic page
                    mainLayout.show(mainContainer, "QUIZ");
                },
                (topicName) -> {
                    // Member 4: record this topic as completed in UserProfile
                    userProfile.markTopicComplete(topicName);
                    System.out.println("[Progress] Topic completed: " + topicName);
                }
            );

            // --------------------------------------------------
            // Member 4: User Profile screen
            // Shows the user's name, ID, score, and progress.
            // --------------------------------------------------
            JPanel profilePanel = buildProfilePanel(userProfile, mainLayout, mainContainer);

            // --------------------------------------------------
            // Add all screens to the CardLayout container
            // --------------------------------------------------
            mainContainer.add(learningPanel, "LEARNING");
            mainContainer.add(quizPanel,     "QUIZ");
            mainContainer.add(gamePanel,     "GAME");
            mainContainer.add(profilePanel,  "PROFILE");

            // Start the app on the Learning Module home screen
            mainLayout.show(mainContainer, "LEARNING");

            frame.add(mainContainer);
            frame.setVisible(true);

            // Print startup info to the console
            System.out.println("SDG 3 Health & Well-Being App started.");
            System.out.println("Window size : " + APP_WIDTH + " x " + APP_HEIGHT);
            System.out.println("Current user: " + userProfile);
        });
    }

    // ----------------------------------------------------------
    // Member 4: buildProfilePanel
    // Builds and returns the User Profile screen panel.
    // Shows the user's name, ID, latest quiz score,
    // topics completed, and a "Back to Home" button.
    // ----------------------------------------------------------
    private static JPanel buildProfilePanel(UserProfile profile,
                                             CardLayout layout,
                                             JPanel container) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xF0FAF5));

        // Header bar
        JPanel header = new JPanel();
        header.setBackground(new Color(0x1D9E75));
        header.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        JLabel headerLabel = new JLabel("My Profile");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);
        panel.add(header, BorderLayout.NORTH);

        // Body — shows profile details
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(0xF0FAF5));
        body.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        body.add(makeInfoLabel("Name : " + profile.getName()));
        body.add(Box.createVerticalStrut(12));
        body.add(makeInfoLabel("ID   : " + profile.getId()));
        body.add(Box.createVerticalStrut(20));
        body.add(makeInfoLabel("Quiz Score : "
            + profile.getQuizScore() + " / " + profile.getQuizTotal()
            + "  (" + profile.getQuizPercent() + "%)"));
        body.add(Box.createVerticalStrut(12));
        body.add(makeInfoLabel("Topics Completed : " + profile.getCompletedCount()));
        body.add(Box.createVerticalStrut(20));

        // Progress summary — calls getProgress() from the Trackable interface
        JLabel progressLabel = makeInfoLabel(profile.getProgress());
        progressLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        body.add(progressLabel);

        panel.add(body, BorderLayout.CENTER);

        // Footer — Back to Home button
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JButton backBtn = new JButton("Back to Home");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setBackground(new Color(0x1D9E75));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setPreferredSize(new Dimension(340, 42));
        backBtn.addActionListener(e -> layout.show(container, "LEARNING"));

        footer.add(backBtn);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    // Helper: creates a styled label for the profile screen
    private static JLabel makeInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(new Color(0x1A1A1A));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
