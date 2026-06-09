// ============================================================
// Class      : Main
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Entry point for the SDG 3 Health &
//              Well-Being desktop application.
//
//              This class belongs to Member 4 and connects all
//              four modules into one working app:
//
//              1. Creates the main JFrame window
//                 (smartphone-sized: 390 x 700 pixels)
//              2. Creates a UserProfile to store quiz scores
//                 and learning progress
//              3. Uses CardLayout to switch between screens:
//                 LEARNING, QUIZ, GAME, PROFILE
//              4. Connects modules so they can communicate:
//                 - Learning marks topics as completed in UserProfile
//                 - Quiz sends final score to GamificationEngine
//                   and saves score in UserProfile
//              5. Builds the User Profile screen


import java.awt.*;
import javax.swing.*;


public class Main {


    // App window size required by the project spec
    private static final int APP_WIDTH  = 390;
    private static final int APP_HEIGHT = 700;


    public static void main(String[] args) {


        // Swing GUI should run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {


            // Try to use the operating system's native look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // If it fails, Swing will keep using the default style
            }


            // --------------------------------------------------
            // Create the main application window
            // --------------------------------------------------
            JFrame frame = new JFrame("SDG 3 — Health & Well-Being App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(APP_WIDTH, APP_HEIGHT);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null); // center the window on screen


            // --------------------------------------------------
            // Member 4: UserProfile object
            // Stores the user's name, score history, and progress
            // "Guest" is used because there is no login screen yet
            // --------------------------------------------------
            UserProfile userProfile = new UserProfile("Guest", "000000");


            // --------------------------------------------------
            // CardLayout setup
            // Each module is a separate screen/card
            // --------------------------------------------------
            CardLayout mainLayout = new CardLayout();
            JPanel mainContainer = new JPanel(mainLayout);


            // --------------------------------------------------
            // Member 3: GamificationEngine
            // Created first because QuizManager needs to send results here
            // --------------------------------------------------
            GamificationEngine gamePanel = new GamificationEngine(() -> {
                // "Back to Home" on reward screen returns to Learning
                mainLayout.show(mainContainer, "LEARNING");
            });


            // --------------------------------------------------
            // Member 2: QuizManager
            // An array is used so the callback can access the quiz object
            // inside the lambda expression
            // --------------------------------------------------
            final QuizManager[] quizHolder = new QuizManager[1];


            quizHolder[0] = new QuizManager(() -> {


                // Quiz finished: collect score details
                int score = quizHolder[0].getScore();
                int total = quizHolder[0].getTotal();
                int percent = quizHolder[0].getPercent();


                // Member 4: save quiz result into UserProfile
                userProfile.setQuizScore(score, total);
                userProfile.saveQuizScore();


                // Print progress info to console for checking/testing
                System.out.println("[Quiz complete] " + userProfile.getProgress());
                System.out.println("[UserProfile]   " + userProfile.toString());


                // Member 3: show the reward screen
                gamePanel.showReward(score, total, percent);
                mainLayout.show(mainContainer, "GAME");
            });


            QuizManager quizPanel = quizHolder[0];


            // --------------------------------------------------
            // Member 1: LearningModulePanel
            // Three callbacks are used:
            // 1. all topics completed
            // 2. go directly to quiz
            // 3. mark one topic as complete
            // --------------------------------------------------
            LearningModulePanel learningPanel = new LearningModulePanel(
                () -> {
                    // All topics completed
                    JOptionPane.showMessageDialog(frame,
                        "Great work! You have finished all learning topics.\n"
                        + "Now let's test your knowledge with a quiz!",
                        "Learning Complete!", JOptionPane.INFORMATION_MESSAGE);
                    mainLayout.show(mainContainer, "QUIZ");
                },
                () -> {
                    // "Take the Quiz" button pressed
                    mainLayout.show(mainContainer, "QUIZ");
                },
                (topicName) -> {
                    // Record completed topic in UserProfile
                    userProfile.markTopicComplete(topicName);
                    System.out.println("[Progress] Topic completed: " + topicName);
                }
            );


            // --------------------------------------------------
            // Member 4: User Profile screen
            // Displays name, ID, score, and progress summary
            // --------------------------------------------------
            JPanel profilePanel = buildProfilePanel(userProfile, mainLayout, mainContainer);


            // --------------------------------------------------
            // Add all screens to the CardLayout container
            // --------------------------------------------------
            mainContainer.add(learningPanel, "LEARNING");
            mainContainer.add(quizPanel, "QUIZ");
            mainContainer.add(gamePanel, "GAME");
            mainContainer.add(profilePanel, "PROFILE");


            // Start the app on the Learning Module home screen
            mainLayout.show(mainContainer, "LEARNING");


            frame.add(mainContainer);
            frame.setVisible(true);


            // Startup messages for debugging
            System.out.println("SDG 3 Health & Well-Being App started.");
            System.out.println("Window size : " + APP_WIDTH + " x " + APP_HEIGHT);
            System.out.println("Current user: " + userProfile);
        });
    }


    // ----------------------------------------------------------
    // Member 4: buildProfilePanel
    // Builds the User Profile screen
    // Shows name, ID, latest quiz score, completed topics,
    // and a button to return to the Learning Module
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


        // Body area: profile details
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


        // Progress summary from Trackable interface
        JLabel progressLabel = makeInfoLabel(profile.getProgress());
        progressLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        body.add(progressLabel);


        panel.add(body, BorderLayout.CENTER);


        // Footer section with return button
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


    // Helper method: creates a consistent label style for profile details
    private static JLabel makeInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setForeground(new Color(0x1A1A1A));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}