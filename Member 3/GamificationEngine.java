// Class      : GamificationEngine
// Creator    : Izwan bin Omar
// Description: Handles badges, stars, points, rank, motivational messages,
//              and reward score saving for the SDG 3 Health & Well-Being app.

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

public class GamificationEngine extends JPanel implements Rewardable {

    private String username;
    private int score;
    private int total;
    private int percent;

    private JLabel titleLabel;
    private JLabel scoreLabel;
    private JLabel badgeLabel;
    private JLabel starsLabel;
    private JLabel pointsLabel;
    private JLabel rankLabel;
    private JLabel messageLabel;

    private JButton homeBtn;
    private Runnable onBackHome;

    public GamificationEngine(Runnable onBackHome) {
        this.username = "Guest";
        this.onBackHome = onBackHome;

        setLayout(new BorderLayout());
        setBackground(new Color(0xFAEEDA));

        buildUI();
    }

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

        homeBtn.addActionListener(e -> {
            if (onBackHome != null) {
                onBackHome.run();
            }
        });

        bottom.add(homeBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private JLabel createLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(0x3A2A10));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public void showReward(int score, int total, int percent) {
        try {
            if (percent < 0 || percent > 100) {
                throw new InvalidScoreException("Invalid percentage. Score must be between 0 and 100.");
            }

            this.score = score;
            this.total = total;
            this.percent = percent;

            scoreLabel.setText("Score: " + score + " / " + total + " (" + percent + "%)");
            badgeLabel.setText("Badge: " + awardBadge(percent));
            starsLabel.setText("Stars: " + getStarText(calculateStars(percent)));
            pointsLabel.setText("Points: " + calculatePoints(score));
            rankLabel.setText("Rank: " + getRank(percent));
            messageLabel.setText("Message: " + getMotivationalMessage(percent));

            saveRewardToFile();

        } catch (InvalidScoreException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Score", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    @Override
    public int calculatePoints(int score) {
        return score * 10;
    }

    // Method overloading
    public int calculatePoints(int score, int bonus) {
        return (score * 10) + bonus;
    }

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

private String getStarText(int stars) {
    return stars + " star(s)";
}

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
            JOptionPane.showMessageDialog(this, "Reward score could not be saved.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return "GamificationEngine{score=" + score + ", total=" + total + ", percent=" + percent + "}";
    }
}