// Class      : ContentPanel
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Displays a single topic's pages one at a time.
//              Designed to fit within 390x700 smartphone resolution.
//              Shows image, title, body text, fact box, and navigation.

// ── WHAT DOES THIS CLASS DO? ─────────────────────────────────────────────────
// ContentPanel displays ONE topic's pages one at a time (like a slideshow).
// It shows: image, title, body text, fact box, navigation buttons, and a
// quiz prompt on the last page.
//
// ── KEY OOP CONCEPTS IN THIS FILE ────────────────────────────────────────────
// 1. INHERITANCE      : 'extends JPanel' — ContentPanel IS a JPanel.
//                        It inherits all panel functionality and adds our own.
// 2. ENCAPSULATION    : Fields are private; only accessible via methods.
// 3. CONSTRUCTOR      : Sets up callbacks and builds the UI.
// 4. Runnable         : A functional interface used to pass "callback functions."
// 5. LAMBDA (→)       : Short way to write anonymous Runnable/ActionListener.
// 6. POLYMORPHISM     : @Override used where relevant from JPanel parent.
// ─────────────────────────────────────────────────────────────────────────────

import java.awt.*;              // Color, Font, BorderLayout, FlowLayout, etc.
import java.util.List;          // List interface for the pages collection
import javax.swing.*;           // All Swing GUI components: JPanel, JLabel, JButton, etc.
import javax.swing.border.*;    // Border classes: EmptyBorder, CompoundBorder, MatteBorder

/**
 * ContentPanel — Swing JPanel that renders paginated learning content.
 *
 * Each "page" is a LearningContent object containing:
 *   - title, body text, fact label & fact text
 *   - image path and emoji-style icon fallback
 *   - colour theme used to tint the page accent
 *
 * The panel provides:
 *   - Horizontal dot navigation to indicate current page
 *   - "Back / Next / Home" footer buttons
 *   - A quiz prompt banner that appears only on the final page
 *   - Automatic scroll-to-top when the page changes
 */

public class ContentPanel extends JPanel {

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color GREEN      = new Color(0x1D9E75); // Primary green for buttons/accents
    private static final Color GREEN_DARK = new Color(0x085041); // Darker green for back arrow button
    private static final Color WHITE      = Color.WHITE;         // Pure white background
    private static final Color TEXT_DARK  = new Color(0x1A1A1A); // Near-black for titles and important text
    private static final Color TEXT_MED   = new Color(0x444444); // Medium grey for body paragraph text
    private static final Color BORDER_CLR = new Color(0xDDDDDD); // Light grey for divider borders
    private static final Color BG_PAGE    = new Color(0xF8FFFE); // Very pale green-white for the page background
    
    // ── State (NHERITANCE FIELDS) ─────────────────────────────────────────────────────────────────
    // ── ENCAPSULATION ─────────────────────────────────────────────────────────
    // 'private' → these fields are hidden from outside classes.
    // They represent the current STATE of this panel.
    private List<LearningContent> pages; // The list of pages for the currently loaded topic
    private int currentPage = 0;         // Index of the page currently being shown (0-based)
    private Runnable onBack;             // Callback: what to do when user clicks "Back to Home"
    private Runnable onComplete;         // Callback: what to do when user completes the topic
    private String currentTopicName = ""; // stores the name of the topic being viewed

    // ── UI components ─────────────────────────────────────────────────────────
    // These are the Swing GUI components that make up the visible interface.
    // They are declared here (as fields) so ALL methods in this class can access them.
    // If declared inside a method, they'd be local variables and inaccessible elsewhere.
    //══════════════════════════════════════════════════════════════════════════
    private JLabel    topicTitleLabel; // Shows the current topic name in the header
    private JLabel    badgeLabel;      // Shows "Page 2/3" badge in the top-right
    private JPanel    dotsPanel;       // Contains the page indicator dots
    private JLabel    imageLabel;      // Displays the topic image (or emoji fallback)
    private JLabel    pageTitleLabel;  // Shows the specific page title
    private JTextArea bodyText;        // Displays the main educational paragraph text
    private JPanel    factBox;         // The coloured highlighted fact/tip box
    private JLabel    factLabelLbl;    // Label inside fact box, e.g. "DID YOU KNOW?"
    private JTextArea factTextArea;    // Text content inside the fact box
    private JPanel    quizPrompt;      // The "Topic Complete! Take the Quiz" banner (last page only)
    private JButton   backBtn;         // "← Back" button (hidden on first page)
    private JButton   nextBtn;         // "Next →" button (hidden on last page)
    private JButton   homeBtn;         // "⌂ Home" button (always visible)

    // CONSTRUCTOR
    // ── WHAT HAPPENS HERE ────────────────────────────────────────────────────
    // When LearningModulePanel creates ContentPanel, it passes two Runnable
    // callbacks (lambda expressions). This constructor stores them and builds the UI.
    //
    // 'public' → any class can create a ContentPanel
    // 'Runnable onBack' → the lambda to call when user wants to go home
    // 'Runnable onComplete' → the lambda to call when user finishes a topic
    // ══════════════════════════════════════════════════════════════════════════
    public ContentPanel(Runnable onBack, Runnable onComplete) {
        tthis.onBack     = onBack;     // Store the "go home" callback
        this.onComplete = onComplete; // Store the "topic finished" callback

        // setLayout() is INHERITED from JPanel.
        // BorderLayout divides the panel into NORTH, SOUTH, EAST, WEST, CENTER zones.
        setLayout(new BorderLayout());
        setBackground(WHITE); // Inherited from JComponent (Grandparen of JPanel)
        buildUI(); // Call our private method ti construct all the sub components
    }

    private void buildUI() {
        // ── PURPOSE ───────────────────────────────────────────────────────────────
    // Constructs every visual element of the content viewer:
    //   1. TOP HEADER  → back arrow, topic title, page badge
    //   2. BODY        → dots, image, page title, body text, fact box, quiz prompt
    //   3. BOTTOM NAV  → back, next, home buttons

        // ── TOP HEADER ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setBackground(WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JButton backArrow = new JButton("←"); // Back button arrow
        backArrow.setFont(new Font("Arial", Font.BOLD, 16));
        backArrow.setForeground(GREEN_DARK);
        backArrow.setBackground(new Color(0xE1F5EE));
        backArrow.setBorder(BorderFactory.createLineBorder(new Color(0xA8DFC8)));
        backArrow.setPreferredSize(new Dimension(36, 36));
        backArrow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backArrow.setFocusPainted(false);
        backArrow.addActionListener(e -> onBack.run()); // registers what to do when the button is clicked.

        // ── TOPIC TITLE LABEL ─────────────────────────────────────────────────
        topicTitleLabel = new JLabel("Topic"); // Placeholder text; updated by loadTopic()
        topicTitleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        topicTitleLabel.setForeground(TEXT_DARK);

        // ── PAGE BADGE ────────────────────────────────────────────────────────
        badgeLabel = new JLabel("Page 1/1");
        badgeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        badgeLabel.setOpaque(true);
        badgeLabel.setBorder(new EmptyBorder(3, 8, 3, 8));

        // Add components to header using BorderLayout zones
        header.add(backArrow,        BorderLayout.WEST);
        header.add(topicTitleLabel,  BorderLayout.CENTER);
        header.add(badgeLabel,       BorderLayout.EAST);
        add(header, BorderLayout.NORTH); // inherited from JPanel → adds header to the NORTH zone of ContentPanel

        // ── SCROLLABLE BODY ───────────────────────────────────────────────────
        // A vertical BoxLayout that holds dots, image, title, body text,
        // the fact box, and the optional quiz prompt.
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_PAGE);
        body.setBorder(new EmptyBorder(10, 14, 10, 14));

        // Page dots - visual page indicator
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

    /**
     * Builds the purple quiz-prompt banner that appears at the end of a topic.
     * Contains a title, subtitle, and a button that triggers onComplete.
     */
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
        this.pages            = topicPages;
        this.currentPage      = 0;
        this.currentTopicName = topicName; // remember which topic is open
        topicTitleLabel.setText(topicName);
        renderPage();
    }

    // Returns the name of the topic currently being displayed
    public String getCurrentTopicName() {
        return currentTopicName;
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
        // Extract just the filename so ImageLoader can resolve it from its resource path
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

        // Quiz prompt on last page - only visible when the user reaches the final page
        quizPrompt.setVisible(currentPage == total - 1);

        // Nav buttons
        backBtn.setVisible(currentPage > 0);            // hide on first page
        nextBtn.setVisible(currentPage < total - 1);    // hide on last page
        homeBtn.setVisible(true);                       // always visible

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

// ── FINAL SUMMARY OF OOP CONCEPTS IN THIS FILE ───────────────────────────
    // • INHERITANCE    : 'extends JPanel' — ContentPanel inherits all JPanel
    //                    behaviour; add(), setBackground(), revalidate() etc.
    //                    are all inherited, not written here.
    // • ENCAPSULATION  : All fields private; accessed/modified only through
    //                    loadTopic(), renderPage(), and the helper methods.
    // • CONSTRUCTOR    : Takes two Runnable callbacks and calls buildUI().
    // • Runnable/Lambda: onBack and onComplete store callback functions.
    //                    'e -> onBack.run()' is a lambda (anonymous function).
    // • POLYMORPHISM   : The same renderPage() updates every UI element by
    //                    calling methods on the Displayable-implementing page object.
    // • static final   : Colour constants are class-level, shared, and immutable.
