// Class      : LearningModulePanel
// Creator    : Victoria Ngui Fong Eik (106647)
// Tester     : Nurirzam Zeana (102885)
// Description: The home screen of the Learning Module.
//              Updated to support 3 callbacks from Main.java (Member 4):
//              1. onAllComplete  - all 10 topics done
//              2. onGoToQuiz     - Take the Quiz button pressed
//              3. onTopicDone    - one topic completed (passes topic name)

// ── WHAT DOES THIS CLASS DO? ─────────────────────────────────────────────────
// LearningModulePanel is the HOME SCREEN / DASHBOARD of the learning module.
// It shows a scrollable grid of topic cards. When a card is tapped, it switches
// to ContentPanel to show that topic's pages. It tracks which topics are done
// and updates a progress bar.

import java.awt.*;              // Color, Font, BorderLayout, GridLayout, etc.
import java.awt.event.*;        // MouseEvent, MouseAdapter for mouse click/hover events
import java.util.*;             // Map, List, ArrayList, etc.
import java.util.List;          // Explicit import of List to avoid ambiguity
import java.util.function.Consumer; // Consumer<T>: a functional interface for callbacks that accept one argument
import javax.swing.*;           // All Swing components: JPanel, JLabel, JScrollPane, etc.
import javax.swing.border.*;    // Border classes

/**
 * LearningModulePanel serves as the main entry point dashboard for the educational module.
 * It coordinates the presentation of individual study topic cards within a grid layout and
 * triggers callbacks to communicate module progression events to the broader application framework.
 */

public class LearningModulePanel extends JPanel {

    // ── UI Theme Design Palette Constants ──────────────────────────────────────
    private static final Color GREEN      = new Color(0x1D9E75);
    private static final Color GREEN_DARK = new Color(0x085041);
    private static final Color BG_LIGHT   = new Color(0xF0FAF5);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT_DARK  = new Color(0x1A1A1A);
    private static final Color TEXT_MED   = new Color(0x555555);
    private static final Color BORDER_CLR = new Color(0xDDDDDD);

    // ── Data and State Tracking Elements ──────────────────────────────────────
    private Map<String, List<LearningContent>> allTopics; // Stores ALL 10 topics and their pages — loaded from LearningData.getAllTopics()
    private boolean[] completed; // Array of booleans, one per topic.
    private int completedCount = 0; // Counter: how many topics have been completed so far.
    private String lastCompletedTopic = ""; // Stores the name of the most recently opened topic.

    // ── Core UI Container Subcomponents ────────────────────────────────────────
    private CardLayout   cardLayout;    // Controls which panel is currently visible
    private JPanel       cardContainer; // Container that holds both homePanel and contentPanel
    private JPanel       homePanel;     // The dashboard with the topic cards grid
    private ContentPanel contentPanel;  // The panel that shows a topic's pages
    private JProgressBar progressBar;   // Green progress bar in the header
    private JLabel       progressLabel; // "X of 10 topics completed" text label
    private JPanel       cardsGrid;     // The 2-column grid holding all topic cards

    // ── Function Callbacks (Member 4 Integration Hooks) ───────────────────────
    // ── WHAT ARE CALLBACKS? ───────────────────────────────────────────────────
    // These are function references passed in FROM OUTSIDE (from Main.java).
    // Instead of LearningModulePanel knowing what to do when events happen,
    // it lets the calling class (Main.java) decide by passing in functions.
    // This is called the CALLBACK PATTERN.
    //
    // Runnable           → functional interface with one method: run()
    //                      Used for zero-argument actions ("do this when X happens")
    // Consumer<String>   → functional interface with one method: accept(String t)
    //                      Used for actions that need one String argument
    // ══════════════════════════════════════════════════════════════════════════
    
    private Runnable         onAllComplete; // Called when ALL 10 topics are finished
    private Runnable         onGoToQuiz;    // Called when user presses "Take the Quiz"
    private Consumer<String> onTopicDone;   // Called each time ONE topic is finished; receives topic name

    // 3-callback constructor for Member 4's Main.java
    public LearningModulePanel(Runnable onAllComplete,
                                Runnable onGoToQuiz,
                                Consumer<String> onTopicDone) {
         this.onAllComplete = onAllComplete; // Store the "all done" callback
        this.onGoToQuiz    = onGoToQuiz;    // Store the "go to quiz" callback
        this.onTopicDone   = onTopicDone;   // Store the "one topic done" callback
        init(); // Delegate the real setup work to init()
    }

    // 1-callback constructor for backward compatibility
    public LearningModulePanel(Runnable onAllComplete) {
        this.onAllComplete = onAllComplete;
        this.onGoToQuiz    = null; // Not provided — set to null (won't be triggered)
        this.onTopicDone   = null; // Not provided — set to null
        init(); // Same init() call — code is not duplicated
    }

    /**
     * Bootstraps foundational datasets, instantiates subcomponents, 
     * registers primary layouts, and loads the Home dashboard state.
     */
    private void init() {
      // ── PURPOSE ───────────────────────────────────────────────────────────────
    // Central setup method called by BOTH constructors.
    // Loads data, creates sub-panels, sets up the CardLayout, and shows the home screen.
    // Load all 10 topics from LearningData.
        this.allTopics = LearningData.getAllTopics();
        // getAllTopics() is static — called on the class, no object needed.
        
        this.completed = new boolean[allTopics.size()];
        // Create the completed array with one boolean per topic.
        // allTopics.size() → returns 10 (number of topics)
        // new boolean[10] → creates an array of 10 booleans, all defaulting to false

        // Set up CardLayout — this controls which screen is visible
        cardLayout     = new CardLayout();
        cardContainer  = new JPanel(cardLayout); // JPanel using CardLayout
        cardContainer.setBackground(WHITE);
        
        buildHomePanel(); // Build the home dashboard panel (method below)
        // Pass references into ContentPanel to support seamless dashboard navigation backflows
        
        contentPanel = new ContentPanel(this::showHome, this::handleTopicComplete);
        cardContainer.add(homePanel,    "HOME");
        cardContainer.add(contentPanel, "CONTENT");
        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);
        cardLayout.show(cardContainer, "HOME"); // Explicit initialization default screen
    }

    /**
     * Constructs the home panel UI, including the progress header bar 
     * and the scrollable card selection layout matrix.
     */
    private void buildHomePanel() {
        homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(BG_LIGHT);

        // Header Panel Definition
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(GREEN);
        header.setBorder(new EmptyBorder(18, 16, 14, 16));

        JLabel appTitle = new JLabel("❤ HealthLearn");
        appTitle.setFont(new Font("Arial", Font.BOLD, 20));
        appTitle.setForeground(WHITE);

        JLabel appSub = new JLabel("SDG 3 — Good Health & Well-Being");
        appSub.setFont(new Font("Arial", Font.PLAIN, 13));
        appSub.setForeground(new Color(0xCCEEE5));

        // Horizontol Progress Feedback Assembly
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

        // Assemble title text elements vertically inside header space
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

        // Layout definitions for the topic cards grid
        cardsGrid = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 columns wide, dynamic row counts
        cardsGrid.setBackground(BG_LIGHT);
        cardsGrid.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Generate individual display cards step-by-step from database keys
        int idx = 0;
        for (Map.Entry<String, List<LearningContent>> entry : allTopics.entrySet()) {
            cardsGrid.add(buildTopicCard(entry.getKey(), entry.getValue(), idx));
            idx++;
        }

        // Embed the grid inside a standard JScrollPane for smooth vertical scrolling
        JScrollPane scroll = new JScrollPane(cardsGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        homePanel.add(scroll, BorderLayout.CENTER);
    }

    /**
     * Dynamically builds a single interactable topic item card component.
     * Extracts parameters safely from nested learning data frames.
     */
    private JPanel buildTopicCard(String topicName, List<LearningContent> pages, int idx) {
        LearningContent first = pages.get(0);
        Color theme   = parseColor(first.getColorTheme());
        Color bgLight = tint(theme);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        card.setName("card-" + idx); // // Trackable tag lookup identifier used during state transitions
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Graphic icon wrapper initialization with support for emoji rendering fallbacks
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

        // Stack labels into the inner container layout
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

        // Click interaction and hover styling overrides
        MouseAdapter click = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { openTopic(topicName, pages); }
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(new Color(0xF8FFF8)); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(WHITE); }
        };
        card.addMouseListener(click);
        // Recursively apply mouse event listeners to nested child labels so clicking anywhere works
        for (Component c : getAllComponents(card)) c.addMouseListener(click);

        return card;
    }

    /**
     * Transitions the UI viewport to display pages inside ContentPanel.
     */
    private void openTopic(String name, List<LearningContent> pages) {
        lastCompletedTopic = name;
        contentPanel.loadTopic(name, pages);
        cardLayout.show(cardContainer, "CONTENT");
    }

    /**
     * Transitions the main view back to the dashboard.
     */
    private void showHome() {
        cardLayout.show(cardContainer, "HOME");
    }

    /**
     * Callback function triggered by ContentPanel whenever a user reaches 
     * the end of a reading sequence. Checks off elements sequentially.
     */
    private void handleTopicComplete() {
        for (int i = 0; i < completed.length; i++) {
            if (!completed[i]) {
                completed[i] = true;
                completedCount++;
                markCardDone(i); // Injects visual checkmark styling onto the completed dashboard item card
                if (onTopicDone != null) onTopicDone.accept(lastCompletedTopic); // Notifies Member 4 code
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

    private java.util.List<Component> getAllComponents(Container c) {
        java.util.List<Component> list = new ArrayList<>();
        for (Component comp : c.getComponents()) {
            list.add(comp);
            if (comp instanceof Container) list.addAll(getAllComponents((Container) comp));
        }
        return list;
    }
}

    // ── FINAL SUMMARY OF OOP CONCEPTS IN THIS FILE ───────────────────────────
    // • INHERITANCE          : extends JPanel — inherits all panel behaviour.
    // • ENCAPSULATION        : All fields private; external access only via methods.
    // • CONSTRUCTOR OVERLOADING: Two constructors (3-param and 1-param versions).
    //                           Both call init() — no duplicated setup code.
    // • Runnable             : onAllComplete, onGoToQuiz — zero-argument callbacks.
    // • Consumer<String>     : onTopicDone — a callback that receives a String.
    // • METHOD REFERENCE     : this::showHome, this::handleTopicComplete passed as lambdas.
    // • ANONYMOUS INNER CLASS: MouseAdapter used inline to handle card hover/click.
    // • @Override            : Used in MouseAdapter to override mouseClicked/Entered/Exited.
    // • instanceof           : Used in getAllComponents() to check container type.
    // • CardLayout           : Manages HOME/CONTENT screen switching without new windows.
    // • RECURSION            : getAllComponents() calls itself for nested containers.
    // ─────────────────────────────────────────────────────────────────────────
