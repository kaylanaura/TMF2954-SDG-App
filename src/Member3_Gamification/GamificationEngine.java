// Class      : GamificationEngine
// Creator    : Izwan bin Omar
// Tester     : Kayla Binti Mohamad (102641)
// Description: Handles badges, stars, points, rank, motivational messages,
//              and reward score saving for the SDG 3 Health & Well-Being app.
//
// 1. Custom Class and Object:
//    - This class represents the Gamification Module object with its own data and behaviours.
// 2. Encapsulation:
//    - Attributes are declared as private to protect object data.
// 3. Interface Implementation:
//    - This class implements Rewardable and provides concrete method bodies.
// 4. GUI Components:
//    - Uses JPanel, JLabel, JButton, and layout managers from Swing.
// 5. Conditional Statements:
//    - Uses if-else conditions to determine badge, stars, rank, and message.
// 6. Method Overloading:
//    - calculatePoints() is overloaded with different parameter lists.
// 7. Exception Handling:
//    - Uses try-catch and InvalidScoreException to validate percentage.
// 8. File I/O:
//    - Saves reward result into reward_scores.txt using FileWriter and BufferedWriter.

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

public class GamificationEngine extends JPanel implements Rewardable {

    // Instance variables / attributes.
    // Based on encapsulation concept, all data is private and can only be managed by this class.
    private String username;
    private int score;
    private int total;
    private int percent;

    // GUI labels used to display reward information to the user.
    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel badgeLabel;
    private JLabel starsLabel;
    private JLabel pointsLabel;
    private JLabel rankLabel;
    private JLabel messageLabel;

    // Button and callback used to return to the Learning Module.
    private JButton homeBtn;
    private Runnable onBackHome;

    // Constructor:
    // Sets the initial state of the object and builds the GUI.
    public GamificationEngine(Runnable onBackHome) {
        this.username = "Guest";
        this.onBackHome = onBackHome;

        // BorderLayout is used to arrange header, body, and bottom button.
        setLayout(new BorderLayout());
        setBackground(new Color(0xFAEEDA));

        buildUI();
    }

    // Private support method:
    // Builds the reward screen interface.
    private void buildUI() {
        JPanel header = new JPanel();
        header.setBackground(new Color(0xBA7517));
        header.setBorder(new EmptyBorder(18, 16, 18, 16));

        titleLabel = new JLabel("Health Reward");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        header.add(titleLabel);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();

        // BoxLayout arranges reward labels vertically from top to bottom.
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(0xFAEEDA));
        body.setBorder(new EmptyBorder(35, 25, 35, 25));

        scoreLabel = createLabel();
        badgeLabel = createLabel();
        starsLabel = createLabel();
        pointsLabel = createLabel();
        rankLabel = createLabel();
        messageLabel = createLabel();

        body.add(Box.createVerticalGlue());
        body.add(scoreLabel);
        body.add(Box.createVerticalStrut(12));
        body.add(badgeLabel);
        body.add(Box.createVerticalStrut(12));
        body.add(starsLabel);
        body.add(Box.createVerticalStrut(12));
        body.add(pointsLabel);
        body.add(Box.createVerticalStrut(12));
        body.add(rankLabel);
        body.add(Box.createVerticalStrut(12));
        body.add(messageLabel);
        body.add(Box.createVerticalGlue());

        add(body, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(12, 14, 12, 14));

        homeBtn = new JButton("Back to Home");
        homeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        homeBtn.setBackground(new Color(0x1D9E75));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setBorderPainted(false);
        homeBtn.setPreferredSize(new Dimension(340, 42));

        // Event listener:
        // When the button is clicked, the app returns to the Learning Module.
        homeBtn.addActionListener(e -> {
            if (onBackHome != null) {
                onBackHome.run();
            }
        });

        bottom.add(homeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // Private support method:
    // Creates a reusable label style for all reward details.
    private JLabel createLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0x3A2A10));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Public service method:
    // Receives score data from QuizManager and displays the reward result.
    public void showReward(int score, int total, int percent) {
        try {
            // Conditional validation:
            // Percentage must be within valid range 0 to 100.
            if (percent < 0 || percent > 100) {
                throw new InvalidScoreException("Invalid percentage. Score must be between 0 and 100.");
            }

            // Assignment statements:
            // Store method parameters into object attributes.
            this.score = score;
            this.total = total;
            this.percent = percent;

            // String concatenation is used to combine text with numeric values.
            scoreLabel.setText("Score: " + score + " / " + total + " (" + percent + "%)");
            badgeLabel.setText("Badge: " + awardBadge(percent));
            starsLabel.setText("Stars: " + getStarText(calculateStars(percent)));
            pointsLabel.setText("Points: " + calculatePoints(score));
            rankLabel.setText("Rank: " + getRank(percent));
            messageLabel.setText("Message: " + getMotivationalMessage(percent));

            saveRewardToFile();

        } catch (InvalidScoreException e) {
            // Exception handling:
            // Displays an error dialog if invalid percentage is detected.
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Score", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Overriding interface method from Rewardable.
    // Awards a badge based on quiz percentage.
    @Override
    public String awardBadge(int percent) {
        if (percent >= 80) {
            return "Health Champion";
        } else if (percent >= 60) {
            return "Wellness Hero";
        } else if (percent >= 40) {
            return "Healthy Learner";
        } else if (percent >= 20) {
            return "Keep Improving";
        } else {
            return "Don't Give Up Badge";
        }
    }

    // Overriding interface method from Rewardable.
    // Calculates the star rating using if-else conditions.
    @Override
    public int calculateStars(int percent) {
        if (percent >= 80) {
            return 5;
        } else if (percent >= 60) {
            return 4;
        } else if (percent >= 40) {
            return 3;
        } else if (percent >= 20) {
            return 2;
        } else {
            return 1;
        }
    }

    // Overriding interface method from Rewardable.
    // Calculates points based on the number of correct answers.
    @Override
    public int calculatePoints(int score) {
        return score * 10;
    }

    // Method overloading:
    // Same method name as calculatePoints(int score), but different parameter list.
    public int calculatePoints(int score, int bonus) {
        return (score * 10) + bonus;
    }

    // Overriding interface method from Rewardable.
    // Determines rank based on quiz percentage.
    @Override
    public String getRank(int percent) {
        if (percent >= 80) {
            return "Expert";
        } else if (percent >= 60) {
            return "Intermediate";
        } else if (percent >= 40) {
            return "Beginner";
        } else {
            return "Starter";
        }
    }

    // Overriding interface method from Rewardable.
    // Returns motivational message based on assignment score scale.
    @Override
    public String getMotivationalMessage(int percent) {
        if (percent >= 80) {
            return "Outstanding!";
        } else if (percent >= 60) {
            return "That's good!";
        } else if (percent >= 40) {
            return "Good try!";
        } else if (percent >= 20) {
            return "You can do better!";
        } else {
            return "Don't give up!";
        }
    }

    // Private support method:
    // Converts number of stars into readable text.
    private String getStarText(int stars) {
        return stars + " star(s)";
    }

    // Private support method:
    // Saves reward data into a text file using file I/O.
    private void saveRewardToFile() {
        try {
            FileWriter fw = new FileWriter("reward_scores.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(new java.util.Date()
                    + " | User: " + username
                    + " | Score: " + score + "/" + total
                    + " | Percent: " + percent + "%"
                    + " | Badge: " + awardBadge(percent)
                    + " | Points: " + calculatePoints(score)
                    + " | Rank: " + getRank(percent));

            bw.newLine();
            bw.close();

        } catch (IOException e) {
            // Handles file writing error without crashing the program.
            JOptionPane.showMessageDialog(this, "Reward score could not be saved.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // toString method override:
    // Returns a string representation of this object for debugging.
    @Override
    public String toString() {
        return "GamificationEngine{score=" + score + ", total=" + total + ", percent=" + percent + "}";
    }
}
