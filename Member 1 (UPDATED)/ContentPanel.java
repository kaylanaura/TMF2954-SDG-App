// Class      : ContentPanel
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Displays a single topic's pages one at a time.
//              Designed to fit within 390x700 smartphone resolution.
//              Shows image, title, body text, fact box, and navigation.

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class ContentPanel extends JPanel {

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color GREEN      = new Color(0x1D9E75);
    private static final Color GREEN_DARK = new Color(0x085041);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_MED   = new Color(0x444444);
    private static final Color BORDER_CLR = new Color(0xDDDDDD);
    private static final Color BG_PAGE    = new Color(0xF8FFFE);

    // ── State ─────────────────────────────────────────────────────────────────
    private List<LearningContent> pages;
    private int currentPage = 0;
    private Runnable onBack;
    private Runnable onComplete;

    // ── UI components ─────────────────────────────────────────────────────────
    private JLabel    topicTitleLabel;
    private JLabel    badgeLabel;
    private JPanel    dotsPanel;
    private JLabel    imageLabel;
    private JLabel    pageTitleLabel;
    private JTextArea bodyText;
    private JPanel    factBox;
    private JLabel    factLabelLbl;
    private JTextArea factTextArea;
    private JPanel    quizPrompt;
    private JButton   backBtn;
    private JButton   nextBtn;
    private JButton   homeBtn;

    public ContentPanel(Runnable onBack, Runnable onComplete) {
        this.onBack     = onBack;
        this.onComplete = onComplete;
        setLayout(new BorderLayout());
        setBackground(WHITE);
        buildUI();
    }

    private void buildUI() {

        // ── TOP HEADER ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setBackground(WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JButton backArrow = new JButton("←");
        backArrow.setFont(new Font("Arial", Font.BOLD, 16));
        backArrow.setForeground(GREEN_DARK);
        backArrow.setBackground(new Color(0xE1F5EE));
        backArrow.setBorder(BorderFactory.createLineBorder(new Color(0xA8DFC8)));
        backArrow.setPreferredSize(new Dimension(36, 36));
        backArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backArrow.setFocusPainted(false);
        backArrow.addActionListener(e -> onBack.run());

        topicTitleLabel = new JLabel("Topic");
        topicTitleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        topicTitleLabel.setForeground(TEXT_DARK);

        badgeLabel = new JLabel("Page 1/1");
        badgeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(new EmptyBorder(3, 8, 3, 8));

        header.add(backArrow,        BorderLayout.WEST);
        header.add(topicTitleLabel,  BorderLayout.CENTER);
        header.add(badgeLabel,       BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── SCROLLABLE BODY ───────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(10, 14, 10, 14));

        // Page dots
        dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        dotsPanel.setOpaque(false);
        dotsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dotsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 16));
        body.add(dotsPanel);
        body.add(Box.createVerticalStrut(8));

        // Image panel — 150px tall, rounded feel via border
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(0xE1F5EE));
        imageLabel.setPreferredSize(new Dimension(362, 150));
        imageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        imageLabel.setMinimumSize(new Dimension(362, 150));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(0xCCEEDD), 1));
        body.add(imageLabel);
        body.add(Box.createVerticalStrut(12));

        // Page title
        pageTitleLabel = new JLabel("Title");
        pageTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pageTitleLabel.setForeground(TEXT_DARK);
        pageTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(pageTitleLabel);
        body.add(Box.createVerticalStrut(7));

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
        factBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        factBox.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCCCCCC), 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        factLabelLbl = new JLabel("LABEL");
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

        // Quiz prompt (last page only)
        quizPrompt = buildQuizPrompt();
        quizPrompt.setAlignmentX(Component.LEFT_ALIGNMENT);
        quizPrompt.setVisible(false);
        body.add(quizPrompt);
        body.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(BG_PAGE);
        scroll.getViewport().setBackground(BG_PAGE);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);

        // ── BOTTOM NAV ────────────────────────────────────────────────────────
        JPanel navRow = new JPanel(new GridLayout(1, 0, 8, 0));
        navRow.setBackground(WHITE);
        navRow.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(10, 14, 10, 14)
        ));

        backBtn = makeBtn("← Back", false);
        backBtn.addActionListener(e -> { currentPage--; renderPage(); });

        nextBtn = makeBtn("Next →", true);
        nextBtn.addActionListener(e -> { currentPage++; renderPage(); });

        homeBtn = makeBtn("⌂ Home", false);
        homeBtn.addActionListener(e -> onBack.run());

        navRow.add(backBtn);
        navRow.add(nextBtn);
        navRow.add(homeBtn);
        add(navRow, BorderLayout.SOUTH);
    }

    private JPanel buildQuizPrompt() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(0xEEEDFE));
        p.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(0xCECBF6), 1),
            new EmptyBorder(12, 14, 12, 14)
        ));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JLabel title = new JLabel("★  Topic Complete!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(new Color(0x3C3489));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Test your knowledge with a quiz!", SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(0x534AB7));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton("Take the Quiz →");
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(new Color(0x7F77DD));
        btn.setForeground(WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> onComplete.run());

        p.add(title);
        p.add(Box.createVerticalStrut(4));
        p.add(sub);
        p.add(Box.createVerticalStrut(10));
        p.add(btn);
        return p;
    }

    private JButton makeBtn(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (primary) {
            b.setBackground(GREEN);
            b.setForeground(WHITE);
        } else {
            b.setBackground(new Color(0xEEEEEE));
            b.setForeground(TEXT_DARK);
        }
        return b;
    }

    // ── Public API ────────────────────────────────────────────────────────────
    public void loadTopic(String topicName, List<LearningContent> topicPages) {
        this.pages       = topicPages;
        this.currentPage = 0;
        topicTitleLabel.setText(topicName);
        renderPage();
    }

    private void renderPage() {
        LearningContent page = pages.get(currentPage);
        int total = pages.size();

        // Badge
        badgeLabel.setText("Page " + (currentPage + 1) + "/" + total);
        Color theme     = parseColor(page.getColorTheme());
        Color themLight = tint(theme);
        badgeLabel.setBackground(themLight);
        badgeLabel.setForeground(theme.darker());

        // Dots
        dotsPanel.removeAll();
        for (int i = 0; i < total; i++) {
            JPanel dot = new JPanel();
            int w = (i == currentPage) ? 20 : 8;
            dot.setPreferredSize(new Dimension(w, 8));
            dot.setBackground(i == currentPage ? GREEN : new Color(0xCCCCCC));
            dot.setBorder(null);
            dotsPanel.add(dot);
        }
        dotsPanel.revalidate();
        dotsPanel.repaint();

        // Image
        imageLabel.setBackground(themLight);
        imageLabel.setIcon(null);
        imageLabel.setText(iconFor(page.getIconName()));
        imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        imageLabel.setForeground(theme);

        String imgPath = page.getImagePath();
        // Extract just the filename from the path
        String filename = new java.io.File(imgPath).getName();
        ImageIcon icon = ImageLoader.load(filename, 362, 150);
        if (icon != null) {
            imageLabel.setIcon(icon);
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            imageLabel.setText(iconFor(page.getIconName()));
            imageLabel.setForeground(theme);
        }

        // Title
        pageTitleLabel.setText(page.getTitle());

        // Body
        bodyText.setText(page.getContent());

        // Fact box
        factBox.setBackground(themLight);
        factLabelLbl.setText(page.getFactLabel().toUpperCase());
        factLabelLbl.setForeground(theme.darker());
        factTextArea.setText(page.getFactText());
        factTextArea.setForeground(theme.darker().darker());
        factBox.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(theme, 1),
            new EmptyBorder(10, 12, 10, 12)
        ));

        // Quiz prompt on last page
        quizPrompt.setVisible(currentPage == total - 1);

        // Nav buttons
        backBtn.setVisible(currentPage > 0);
        nextBtn.setVisible(currentPage < total - 1);
        homeBtn.setVisible(true);

        // Scroll back to top
        SwingUtilities.invokeLater(() -> {
            Container c = getParent();
            while (c != null) {
                if (c instanceof JScrollPane) {
                    ((JScrollPane) c).getVerticalScrollBar().setValue(0);
                    break;
                }
                c = c.getParent();
            }
        });

        revalidate();
        repaint();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private Color parseColor(String hex) {
        try { return Color.decode(hex); } catch (Exception e) { return GREEN; }
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
            case "brain":             return "🧠";
            case "run":               return "🏃";
            case "virus":             return "🦠";
            case "stethoscope":       return "🩺";
            case "needle":            return "💉";
            case "apple":             return "🍎";
            case "building-hospital": return "🏥";
            case "ban":               return "🚭";
            case "map-pin":           return "📍";
            case "hand":              return "✋";
            default:                  return "❤";
        }
    }
}
