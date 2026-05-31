// Class      : LearningModulePanel
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: The home screen of the Learning Module.
//              Displays a header with progress bar and a 2-column grid of
//              clickable topic cards. Switches to ContentPanel when a card
//              is clicked. Notifies the parent app when all topics are done.

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class LearningModulePanel extends JPanel {

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color GREEN       = new Color(0x1D9E75);
    private static final Color GREEN_DARK  = new Color(0x085041);
    private static final Color BG_LIGHT    = new Color(0xF0FAF5);
    private static final Color WHITE       = Color.WHITE;
    private static final Color TEXT_DARK   = new Color(0x1A1A1A);
    private static final Color TEXT_MED    = new Color(0x555555);
    private static final Color BORDER_CLR  = new Color(0xDDDDDD);

    // ── State ─────────────────────────────────────────────────────────────────
    private Map<String, List<LearningContent>> allTopics;
    private boolean[] completed;
    private int completedCount = 0;

    // ── UI ────────────────────────────────────────────────────────────────────
    private CardLayout cardLayout;
    private JPanel     cardContainer;
    private JPanel     homePanel;
    private ContentPanel contentPanel;
    private JProgressBar progressBar;
    private JLabel       progressLabel;
    private JPanel       cardsGrid;

    // Callbacks to other modules
    private Runnable onAllComplete;  // called when all 10 topics done
    private Runnable onGoToQuiz;     // called when user clicks "Take the Quiz" button

    public LearningModulePanel(Runnable onAllComplete, Runnable onGoToQuiz) {
        this.onAllComplete = onAllComplete;
        this.onGoToQuiz    = onGoToQuiz;
        this.allTopics     = LearningData.getAllTopics();
        this.completed     = new boolean[allTopics.size()];

        cardLayout     = new CardLayout();
        cardContainer  = new JPanel(cardLayout);
        cardContainer.setBackground(WHITE);

        buildHomePanel();
        // Second callback = what happens when user clicks "Take the Quiz" button
        // This fires onGoToQuiz so Main can switch to the QuizManager panel
        contentPanel = new ContentPanel(this::showHome, () -> {
            handleTopicComplete(); // still mark the topic as done
            if (onGoToQuiz != null) onGoToQuiz.run(); // then switch to quiz screen
        });

        cardContainer.add(homePanel,    "HOME");
        cardContainer.add(contentPanel, "CONTENT");

        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
        cardLayout.show(cardContainer, "HOME");
    }

    // ── Home panel ────────────────────────────────────────────────────────────
    private void buildHomePanel() {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(BG_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN);
        header.setBorder(new EmptyBorder(18, 16, 14, 16));

        JLabel appTitle = new JLabel("❤ HealthLearn");
        appTitle.setFont(new Font("Arial", Font.BOLD, 20));
        appTitle.setForeground(WHITE);

        JLabel appSub = new JLabel("SDG 3 — Good Health & Well-Being");
        appSub.setFont(new Font("Arial", Font.PLAIN, 13));
        appSub.setForeground(new Color(0xCCEEE5));

        // Progress bar
        progressBar = new JProgressBar(0, allTopics.size());
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setBackground(new Color(0x55BB99));
        progressBar.setForeground(WHITE);
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(0, 8));

        progressLabel = new JLabel("0 of " + allTopics.size() + " topics completed");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        progressLabel.setForeground(new Color(0xCCEEE5));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(appTitle);
        titlePanel.add(Box.createVerticalStrut(2));
        titlePanel.add(appSub);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(progressBar);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(progressLabel);

        header.add(titlePanel, BorderLayout.CENTER);
        homePanel.add(header, BorderLayout.NORTH);

        // Cards grid in a scroll pane
        cardsGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        cardsGrid.setBackground(BG_LIGHT);
        cardsGrid.setBorder(new EmptyBorder(14, 14, 14, 14));

        int idx = 0;
        for (Map.Entry<String, List<LearningContent>> entry : allTopics.entrySet()) {
            cardsGrid.add(buildTopicCard(entry.getKey(), entry.getValue(), idx));
            idx++;
        }

        JScrollPane scroll = new JScrollPane(cardsGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        homePanel.add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildTopicCard(String topicName, List<LearningContent> pages, int idx) {
        LearningContent first = pages.get(0);
        Color theme = parseColor(first.getColorTheme());
        Color bgLight = tint(theme);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        card.setName("card-" + idx);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon circle
        JLabel icon = new JLabel(iconFor(first.getIconName()), SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        icon.setOpaque(true);
        icon.setBackground(bgLight);
        icon.setPreferredSize(new Dimension(42, 42));
        icon.setMaximumSize(new Dimension(42, 42));
        icon.setBorder(new EmptyBorder(4, 4, 4, 4));

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconWrap.setOpaque(false);
        iconWrap.add(icon);

        JLabel titleLbl = new JLabel("<html><b>" + topicName + "</b></html>");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 13));
        titleLbl.setForeground(TEXT_DARK);

        String pageCount = pages.size() + " page" + (pages.size() > 1 ? "s" : "");
        JLabel countLbl = new JLabel(pageCount);
        countLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        countLbl.setForeground(TEXT_MED);

        JLabel badge = new JLabel(idx == 0 ? "Start here" : pageCount);
        badge.setFont(new Font("Arial", Font.PLAIN, 11));
        badge.setOpaque(true);
        badge.setBackground(bgLight);
        badge.setForeground(theme.darker());
        badge.setBorder(new EmptyBorder(2, 8, 2, 8));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(10, 12, 10, 12));
        content.add(iconWrap);
        content.add(Box.createVerticalStrut(8));
        content.add(titleLbl);
        content.add(Box.createVerticalStrut(2));
        content.add(countLbl);
        content.add(Box.createVerticalStrut(5));
        content.add(badge);

        card.add(content);

        // Click handler
        MouseAdapter click = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openTopic(topicName, pages); }
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(new Color(0xF8FFF8)); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(WHITE); }
        };
        card.addMouseListener(click);
        for (Component c : getAllComponents(card)) c.addMouseListener(click);

        return card;
    }

    // ── Navigation ────────────────────────────────────────────────────────────
    private void openTopic(String name, List<LearningContent> pages) {
        contentPanel.loadTopic(name, pages);
        cardLayout.show(cardContainer, "CONTENT");
    }

    private void showHome() {
        cardLayout.show(cardContainer, "HOME");
    }

    private void handleTopicComplete() {
        // Find which topic just completed based on content panel's current topic
        // Mark all topics with matching names as done
        completedCount = 0;
        for (boolean b : completed) if (b) completedCount++;

        // Simple: increment until all done
        for (int i = 0; i < completed.length; i++) {
            if (!completed[i]) {
                completed[i] = true;
                completedCount++;
                markCardDone(i);
                break;
            }
        }

        updateProgress();
        showHome();

        if (completedCount >= allTopics.size() && onAllComplete != null) {
            onAllComplete.run();
        }
    }

    private void markCardDone(int idx) {
        for (Component c : cardsGrid.getComponents()) {
            if (("card-" + idx).equals(c.getName())) {
                JPanel card = (JPanel) c;
                card.setBackground(new Color(0xE1F5EE));
                // Add a tick label
                JLabel tick = new JLabel("✓");
                tick.setFont(new Font("Arial", Font.BOLD, 12));
                tick.setForeground(GREEN_DARK);
                card.add(tick);
                card.revalidate();
                break;
            }
        }
    }

    private void updateProgress() {
        progressBar.setValue(completedCount);
        progressLabel.setText(completedCount + " of " + allTopics.size() + " topics completed");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Color parseColor(String hex) {
        try { return Color.decode(hex); }
        catch (Exception e) { return GREEN; }
    }

    private Color tint(Color c) {
        return new Color(
            Math.min(255, c.getRed()   / 5 + 210),
            Math.min(255, c.getGreen() / 5 + 210),
            Math.min(255, c.getBlue()  / 5 + 210)
        );
    }

    private String iconFor(String name) {
        switch (name) {
            case "brain":            return "🧠";
            case "run":              return "🏃";
            case "virus":            return "🦠";
            case "stethoscope":      return "🩺";
            case "needle":           return "💉";
            case "apple":            return "🍎";
            case "building-hospital":return "🏥";
            case "ban":              return "🚭";
            case "map-pin":          return "📍";
            case "hand":             return "✋";
            default:                 return "❤";
        }
    }

    private java.util.List<Component> getAllComponents(Container c) {
        java.util.List<Component> list = new ArrayList<>();
        for (Component comp : c.getComponents()) {
            list.add(comp);
            if (comp instanceof Container) list.addAll(getAllComponents((Container) comp));
        }
        return list;
    }
}
