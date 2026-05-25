// Class      : ContentPanel
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : [Member 2 Name]
// Description: Displays a single topic's pages one at a time.
//              Shows title, body text, and a fact box.
//              Includes Next/Back navigation and page dot indicators.

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class ContentPanel extends JPanel {

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color BG          = new Color(0xF0FAF5);
    private static final Color GREEN       = new Color(0x1D9E75);
    private static final Color WHITE       = Color.WHITE;
    private static final Color TEXT_DARK   = new Color(0x1A1A1A);
    private static final Color TEXT_MED    = new Color(0x555555);
    private static final Color TEXT_LIGHT  = new Color(0x888888);
    private static final Color BORDER_CLR  = new Color(0xDDDDDD);

    // ── State ─────────────────────────────────────────────────────────────────
    private List<LearningContent> pages;
    private int currentPage = 0;
    private Runnable onBack;
    private Runnable onComplete;

    // ── UI components ─────────────────────────────────────────────────────────
    private JLabel  titleLabel;
    private JLabel  badgeLabel;
    private JLabel  pageTitleLabel;
    private JTextArea bodyText;
    private JPanel  factBox;
    private JLabel  factLabelLbl;
    private JTextArea factTextArea;
    private JPanel  dotsPanel;
    private JButton backBtn;
    private JButton nextBtn;
    private JButton homeBtn;
    private JPanel  iconPanel;
    private JLabel  iconLabel;
    private JPanel  quizPrompt;

    public ContentPanel(Runnable onBack, Runnable onComplete) {
        this.onBack     = onBack;
        this.onComplete = onComplete;
        setLayout(new BorderLayout());
        setBackground(WHITE);
        buildUI();
    }

    private void buildUI() {
        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(12, 14, 12, 14)
        ));

        JButton backArrow = new JButton("←");
        backArrow.setFont(new Font("Arial", Font.PLAIN, 16));
        backArrow.setForeground(TEXT_DARK);
        backArrow.setBackground(new Color(0xF5F5F5));
        backArrow.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        backArrow.setPreferredSize(new Dimension(36, 36));
        backArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backArrow.addActionListener(e -> onBack.run());

        titleLabel = new JLabel("Topic");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        badgeLabel = new JLabel("");
        badgeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(new EmptyBorder(3, 8, 3, 8));

        header.add(backArrow, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        header.add(badgeLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Scroll body ───────────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(WHITE);
        body.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Page dots
        dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        dotsPanel.setBackground(WHITE);
        dotsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dotsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        body.add(dotsPanel);
        body.add(Box.createVerticalStrut(12));

        // Icon area
        iconPanel = new JPanel(new BorderLayout());
        iconPanel.setPreferredSize(new Dimension(340, 110));
        iconPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        iconPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        iconLabel = new JLabel("❤", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        body.add(iconPanel);
        body.add(Box.createVerticalStrut(14));

        // Page title — stored as field so renderPage() can update it directly
        pageTitleLabel = new JLabel("Title");
        pageTitleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        pageTitleLabel.setForeground(TEXT_DARK);
        pageTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(pageTitleLabel);
        body.add(Box.createVerticalStrut(8));

        // Body text
        bodyText = new JTextArea();
        bodyText.setFont(new Font("Arial", Font.PLAIN, 13));
        bodyText.setForeground(TEXT_MED);
        bodyText.setLineWrap(true);
        bodyText.setWrapStyleWord(true);
        bodyText.setEditable(false);
        bodyText.setOpaque(false);
        bodyText.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(bodyText);
        body.add(Box.createVerticalStrut(12));

        // Fact box
        factBox = new JPanel();
        factBox.setLayout(new BoxLayout(factBox, BoxLayout.Y_AXIS));
        factBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        factBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        factBox.setBorder(new EmptyBorder(10, 12, 10, 12));

        factLabelLbl = new JLabel("Label");
        factLabelLbl.setFont(new Font("Arial", Font.BOLD, 11));
        factLabelLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        factBox.add(factLabelLbl);
        factBox.add(Box.createVerticalStrut(4));

        factTextArea = new JTextArea();
        factTextArea.setFont(new Font("Arial", Font.PLAIN, 13));
        factTextArea.setLineWrap(true);
        factTextArea.setWrapStyleWord(true);
        factTextArea.setEditable(false);
        factTextArea.setOpaque(false);
        factTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        factBox.add(factTextArea);
        body.add(factBox);
        body.add(Box.createVerticalStrut(12));

        // Quiz prompt (shown on last page)
        quizPrompt = buildQuizPrompt();
        quizPrompt.setAlignmentX(Component.LEFT_ALIGNMENT);
        quizPrompt.setVisible(false);
        body.add(quizPrompt);
        body.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);

        // ── Navigation row ────────────────────────────────────────────────────
        JPanel navRow = new JPanel(new GridLayout(1, 0, 8, 0));
        navRow.setBackground(WHITE);
        navRow.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(10, 16, 10, 16)
        ));

        backBtn = createNavButton("← Back", false);
        backBtn.addActionListener(e -> {
            currentPage--;
            renderPage();
        });

        nextBtn = createNavButton("Next →", true);
        nextBtn.addActionListener(e -> {
            currentPage++;
            renderPage();
        });

        homeBtn = createNavButton("⌂ Home", false);
        homeBtn.addActionListener(e -> onBack.run());

        navRow.add(backBtn);
        navRow.add(nextBtn);
        navRow.add(homeBtn);
        add(navRow, BorderLayout.SOUTH);
    }

    private JPanel buildQuizPrompt() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(0xEEEDFE));
        panel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCECBF6)),
            new EmptyBorder(14, 14, 14, 14)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel icon = new JLabel("★ Topic Complete!", SwingConstants.CENTER);
        icon.setFont(new Font("Arial", Font.BOLD, 15));
        icon.setForeground(new Color(0x3C3489));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(icon);
        panel.add(Box.createVerticalStrut(6));

        JLabel sub = new JLabel("Test your knowledge with a quiz!", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(0x534AB7));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sub);
        panel.add(Box.createVerticalStrut(10));

        JButton quizBtn = new JButton("Take the Quiz →");
        quizBtn.setFont(new Font("Arial", Font.BOLD, 13));
        quizBtn.setBackground(new Color(0x7F77DD));
        quizBtn.setForeground(WHITE);
        quizBtn.setOpaque(true);
        quizBtn.setBorderPainted(false);
        quizBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quizBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        quizBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quizBtn.addActionListener(e -> onComplete.run());
        panel.add(quizBtn);

        return panel;
    }

    private JButton createNavButton(String text, boolean primary) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        if (primary) {
            btn.setBackground(GREEN);
            btn.setForeground(WHITE);
        } else {
            btn.setBackground(new Color(0xF0F0F0));
            btn.setForeground(TEXT_DARK);
        }
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Public API ────────────────────────────────────────────────────────────
    public void loadTopic(String topicName, List<LearningContent> topicPages) {
        this.pages       = topicPages;
        this.currentPage = 0;
        titleLabel.setText(topicName);
        renderPage();
    }

    private void renderPage() {
        LearningContent page = pages.get(currentPage);
        int total = pages.size();

        // Page dots
        dotsPanel.removeAll();
        for (int i = 0; i < total; i++) {
            JPanel dot = new JPanel();
            dot.setBackground(i == currentPage ? GREEN : new Color(0xCCCCCC));
            int w = (i == currentPage) ? 18 : 8;
            dot.setPreferredSize(new Dimension(w, 8));
            dot.setBorder(BorderFactory.createLineBorder(
                i == currentPage ? GREEN : new Color(0xCCCCCC)));
            dotsPanel.add(dot);
        }
        dotsPanel.revalidate();

        // Icon panel colour
        Color theme = parseColor(page.getColorTheme());
        Color lightTheme = blend(theme, WHITE, 0.15f);
        iconPanel.setBackground(lightTheme);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        iconLabel.setText(iconFor(page.getIconName()));
        iconLabel.setForeground(theme);

        // Texts
        pageTitleLabel.setText(page.getTitle());
        bodyText.setText(page.getContent());

        // Fact box
        factBox.setBackground(lightTheme);
        factLabelLbl.setText(page.getFactLabel().toUpperCase());
        factLabelLbl.setForeground(theme.darker());
        factTextArea.setText(page.getFactText());
        factTextArea.setForeground(theme.darker().darker());
        factBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,
            80 + page.getFactText().split("\n").length * 18));

        // Badge
        badgeLabel.setText("Page " + (currentPage + 1) + "/" + total);
        badgeLabel.setBackground(lightTheme);
        badgeLabel.setForeground(theme.darker());

        // Quiz prompt on last page
        quizPrompt.setVisible(currentPage == total - 1);

        // Nav buttons
        backBtn.setVisible(currentPage > 0);
        nextBtn.setVisible(currentPage < total - 1);
        homeBtn.setVisible(true);

        revalidate();
        repaint();
    }

    private Color parseColor(String hex) {
        try { return Color.decode(hex); }
        catch (Exception e) { return GREEN; }
    }

    private Color blend(Color c, Color bg, float alpha) {
        int r = (int)(c.getRed()   * alpha + bg.getRed()   * (1 - alpha));
        int g = (int)(c.getGreen() * alpha + bg.getGreen() * (1 - alpha));
        int b = (int)(c.getBlue()  * alpha + bg.getBlue()  * (1 - alpha));
        // Use a fixed light background tint
        return new Color(
            Math.min(255, c.getRed()   / 4 + 200),
            Math.min(255, c.getGreen() / 4 + 200),
            Math.min(255, c.getBlue()  / 4 + 200)
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
}
