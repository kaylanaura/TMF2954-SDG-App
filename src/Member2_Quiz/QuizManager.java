// Class      : QuizManager
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Tester     : Kayla Binti Mohamad (102641)
// Description: The main class for the Quiz Module. It stores all 20+ questions,
//              tracks the user's score, checks answers, and shows the result with
//              a motivational message. It also saves scores to a text file.
//              Implements the quiz screen as a Swing GUI panel.
//              Interacts with GamificationEngine to trigger badges and points.

// ── IMPORT STATEMENTS ────────────────────────────────────────────────────────
// 'import' tells Java where to find classes that this file uses.
// Without imports, Java only knows about classes in the java.lang package
// (e.g. String, Math, Boolean) — everything else must be imported.

import java.awt.*;          // AWT = Abstract Window Toolkit (older GUI layer)
                            // Provides: Color, Font, BorderLayout, Dimension,
                            //           Component, Cursor, GridLayout, etc.

import java.io.*;           // I/O = Input / Output
                            // Provides: FileWriter, BufferedWriter, IOException
                            // Used here to save quiz scores to a text file.

import java.util.*;         // Java utilities
                            // Provides: ArrayList, Date, Collections
                            // The '*' means "import everything in java.util"

import java.util.List;      // Explicit import of List interface from java.util
                            // Needed because java.awt.List also exists —
                            // without this explicit import, Java might get confused
                            // which 'List' you mean.

import javax.swing.*;       // Swing = the main Java GUI library (newer than AWT)
                            // Provides: JPanel, JLabel, JButton, JTextField,
                            //           JScrollPane, JProgressBar, CardLayout,
                            //           BoxLayout, BorderFactory, SwingConstants
                            // Explicit import used here to avoid clash with java.awt.List

import javax.swing.border.*; // Swing border classes
                             // Provides: EmptyBorder (padding around components)

// ── CLASS DECLARATION ─────────────────────────────────────────────────────────
// 'public'          → accessible from all classes in the project
// 'class'           → declares a blueprint for objects
// 'extends JPanel'  → INHERITANCE. QuizManager IS-A JPanel.
//                     It inherits all Swing panel functionality (layout,
//                     painting, adding children, etc.) from JPanel.
//                     'extends' = inherit from a parent class (superclass).
//                     A class can only extend ONE parent (single inheritance).
//                     This is different from 'implements' (interface).
public class QuizManager extends JPanel {

    // ── COLOUR CONSTANTS ─────────────────────────────────────────────────────
    // 'private'       → only used inside this class
    // 'static'        → belongs to the CLASS, not to any individual object.
    //                   All QuizManager instances share the same colour values.
    //                   You don't need to create an object to access a static field.
    // 'final'         → the value cannot be changed after it is set (constant).
    //                   Convention: constants are named in ALL_CAPS_WITH_UNDERSCORES.
    // 'Color'         → a class from java.awt representing an RGB colour.
    // 'new Color(hex)' → creates a Color using a hexadecimal (0x...) RGB value.
    //                   0x7F77DD = hex for a medium purple colour.
    private static final Color PURPLE       = new Color(0x7F77DD);
    private static final Color PURPLE_DARK  = new Color(0x3C3489);  // darker purple
    private static final Color PURPLE_LIGHT = new Color(0xEEEDFE);  // very light purple (almost white)
    private static final Color GREEN        = new Color(0x1D9E75);  // teal-green for correct/home buttons
    private static final Color WHITE        = Color.WHITE;          // Color.WHITE is a pre-defined constant
    private static final Color TEXT_DARK    = new Color(0x1A1A1A);  // near-black for main text
    private static final Color TEXT_MED     = new Color(0x555555);  // medium grey for secondary text
    private static final Color CORRECT_CLR  = new Color(0xD4EDDA);  // light green background for correct answer
    private static final Color WRONG_CLR    = new Color(0xF8D7DA);  // light red background for wrong answer

    // ── SCORE FILE PATH CONSTANT ─────────────────────────────────────────────
    // 'private static final String' → a constant String shared across all instances.
    // Scores are written to this file on disk so they persist between sessions.
    // Hardcoding scores directly in code is NOT allowed per project spec —
    // saving to a file satisfies the requirement for persistent score storage.
    private static final String SCORE_FILE = "quiz_scores.txt";

    // ── INSTANCE FIELDS — QUESTION LIST AND QUIZ STATE ───────────────────────
    // 'private' → encapsulation; only accessible within QuizManager.
    // These are INSTANCE fields (no 'static') — each QuizManager object
    // gets its own copy of these values.

    private List<Answerable> questions;
    // List<Answerable> → a generic list that holds objects of type Answerable.
    // Because Answerable is an interface, this list can hold MCQQuestion,
    // TrueFalseQuestion, and FillBlankQuestion objects — all in the same list.
    // This is POLYMORPHISM — one variable stores many different types.
    // List is an interface from java.util; the actual object used is ArrayList (see constructor).

    private int currentIndex = 0;
    // 'int'  → primitive integer type; holds whole numbers.
    // Tracks which question we are currently on (0-based index).
    // e.g. currentIndex=0 means Question 1, currentIndex=4 means Question 5.
    // '= 0' → initialised to 0 when the field is declared (default would also be 0 for int).

    private int score = 0;
    // Counts how many questions the user has answered correctly so far.
    // Starts at 0; incremented by 1 each time checkAnswer() returns true.

    private boolean answered = false;
    // 'boolean' → primitive type; true or false only.
    // Prevents the user from submitting the same question twice.
    // Set to true once submitAnswer() runs; reset to false when the next question loads.

    // ── CARDLAYOUT FIELDS ────────────────────────────────────────────────────
    // CardLayout lets you stack multiple panels and show only one at a time —
    // like a deck of cards. Here, "QUIZ" card and "RESULT" card are stacked.
    // Switching between them feels like navigating to a new screen.

    private CardLayout cardLayout;
    // CardLayout is the layout manager that controls which card is visible.

    private JPanel cardContainer;
    // The JPanel that uses CardLayout. Both the quiz screen and result screen
    // are added as children of this panel.

    // ── QUIZ SCREEN UI COMPONENTS ─────────────────────────────────────────────
    // All declared as instance fields so multiple methods can access them.
    // 'private' → encapsulation; QuizManager manages its own UI internally.

    private JLabel  questionNumberLabel;
    // JLabel = a non-interactive text display component (no clicks, no input).
    // Shows text like "Question 3 / 22" in the header.

    private JLabel  questionTypeLabel;
    // Shows the question type badge: "MCQ", "True / False", or "Fill in the Blank".

    private JLabel  questionLabel;
    // Displays the actual question text. Uses HTML wrapping (<html>...</html>)
    // so long questions wrap onto multiple lines automatically.

    private JLabel  hintLabel;
    // Shows a small instruction below the question, e.g. "Choose A, B, C or D".

    private JPanel  answerPanel;
    // A JPanel that is cleared and rebuilt for each question.
    // For MCQ: contains 4 JButtons.
    // For TrueFalse: contains 2 JButtons ("True", "False").
    // For FillBlank: contains 1 JTextField.

    private JButton submitBtn;
    // JButton = a clickable button component.
    // User clicks this to submit their answer.
    // Gets disabled (greyed out) after submission to prevent re-answering.

    private JLabel  feedbackLabel;
    // Shows "✓ Correct!" in green or "✗ Wrong!" in red after submission.

    private JLabel  correctAnswerLabel;
    // Only visible after a wrong answer; shows "Correct answer: ..." to the user.

    private JButton nextBtn;
    // Appears after submission. Text changes:
    //   "Next Question →" for Q1–Q21
    //   "See Results →"   for the last question (Q22)
    // Hidden (setVisible(false)) until the user submits.

    private JProgressBar quizProgress;
    // JProgressBar = a bar that fills up to show progress.
    // Ranges from 0 to questions.size() (22).
    // Value updates as the user advances through questions.

    // ── RESULT SCREEN UI COMPONENTS ───────────────────────────────────────────
    private JLabel scoreLabel;
    // Displays the final score as "15 / 22" in large bold text.

    private JLabel percentLabel;
    // Displays the percentage score, e.g. "68%".

    private JLabel motivationLabel;
    // Displays a motivational message based on the score range.
    // e.g. "★ Outstanding!" for 80%+, "★ Don't give up!" for under 20%.

    private JButton retryBtn;
    // Button to retake the quiz from Question 1.
    // Calls resetQuiz() which resets currentIndex, score, and all UI state.

    private JButton homeBtn;
    // Button to return to the main application menu.
    // Calls onQuizComplete.run() which is a callback provided by the parent panel.

    // ── MCQ OPTION BUTTONS ARRAY ─────────────────────────────────────────────
    private JButton[] optionButtons = new JButton[4];
    // An array of 4 JButton references, one per MCQ option (A, B, C, D).
    // 'new JButton[4]' → allocates an array of 4 slots; buttons are created later
    // inside buildMCQAnswerPanel() and stored here so they can be accessed if needed.

    // ── FILL-IN-THE-BLANK TEXT FIELD ─────────────────────────────────────────
    private JTextField fillField;
    // JTextField = a single-line text input where the user can type.
    // Created inside buildFillBlankAnswerPanel().
    // submitAnswer() reads its content using fillField.getText().

    // ── CALLBACK FIELD ───────────────────────────────────────────────────────
    private Runnable onQuizComplete;
    // 'Runnable' → a Java functional interface with a single method: run().
    // Used here as a CALLBACK — a way to tell the parent screen "quiz is done".
    // The parent passes in a lambda (arrow function) when creating QuizManager,
    // e.g. new QuizManager(() -> showHomePage())
    // When quiz ends and user clicks Home, onQuizComplete.run() is called,
    // which triggers whatever the parent told us to do.

    // ── SELECTED ANSWER FIELD ─────────────────────────────────────────────────
    private String selectedAnswer = "";
    // Holds the currently selected option for MCQ ("A"/"B"/"C"/"D")
    // or the chosen button text for TrueFalse ("True"/"False").
    // Initialised to "" (empty string).
    // Reset to "" at the start of each new question in showQuestion().
    // FillBlank doesn't use this field — it reads fillField.getText() directly.

    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────
    // Called by the parent panel: new QuizManager(callbackLambda)
    // 'public'   → accessible from outside (parent panel calls it)
    // No return type — that's what makes it a constructor
    // Parameter: Runnable onQuizComplete — the callback to run when quiz ends
    public QuizManager(Runnable onQuizComplete) {
        this.onQuizComplete = onQuizComplete;
        // Store the callback. 'this.' distinguishes the field from the parameter.

        // Create the ArrayList to store all question objects.
        // ArrayList is the concrete class that implements the List interface.
        // We declare it as List<Answerable> (interface type) for flexibility —
        // if we ever switch to LinkedList, only this line changes.
        questions = new ArrayList<>();
        loadQuestions(); // fill the list with all 22 question objects

        // Set up CardLayout to switch between QUIZ and RESULT screens.
        // CardLayout = a layout manager that shows ONE panel at a time.
        cardLayout    = new CardLayout();
        cardContainer = new JPanel(cardLayout); // panel that uses CardLayout
        cardContainer.setBackground(WHITE);

        // Add both screens to the card container with string keys.
        // "QUIZ" and "RESULT" are the names used to switch cards later.
        cardContainer.add(buildQuizScreen(),   "QUIZ");
        cardContainer.add(buildResultScreen(), "RESULT");

        // QuizManager itself is a JPanel (from 'extends JPanel').
        // Set its layout to BorderLayout so cardContainer fills the centre.
        // BorderLayout divides space into NORTH, SOUTH, EAST, WEST, CENTER.
        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER); // fill the entire panel area

        // Show the QUIZ card first and display Question 1.
        cardLayout.show(cardContainer, "QUIZ");
        showQuestion();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOAD QUESTIONS
    // Adds all 20+ questions. Mix of MCQ, True/False, and Fill-in-blank.
    // 'private' → only called from the constructor inside this class.
    // 'void'    → no return value; it just adds to the 'questions' list.
    // ─────────────────────────────────────────────────────────────────────────
    private void loadQuestions() {

        // ── TRUE / FALSE QUESTIONS (Q1 – Q8) ──────────────────────────────────
        // questions.add(...)  → ArrayList method that appends an object to the list.
        // new TrueFalseQuestion("statement", booleanAnswer) → creates a new object.
        // The boolean (true/false) is the second argument — no quotes needed for booleans.

        // Q1
        questions.add(new TrueFalseQuestion(
            "SDG 3 stands for 'Good Health and Well-Being'.",
            true  // this statement IS correct
        ));

        // Q2
        questions.add(new TrueFalseQuestion(
            "Vaccines have eliminated smallpox completely from the world.",
            true
        ));

        // Q3
        questions.add(new TrueFalseQuestion(
            "Depression affects fewer than 100 million people worldwide.",
            false  // actually 280 million — the statement is incorrect
        ));

        // Q4
        questions.add(new TrueFalseQuestion(
            "Tobacco kills more than 8 million people per year globally.",
            true
        ));

        // Q5
        questions.add(new TrueFalseQuestion(
            "Non-communicable diseases (NCDs) can spread from person to person.",
            false  // NCDs are NOT contagious (e.g. diabetes, cancer)
        ));

        // Q6
        questions.add(new TrueFalseQuestion(
            "The WHO recommends at least 150 minutes of moderate physical activity per week for adults.",
            true
        ));

        // Q7
        questions.add(new TrueFalseQuestion(
            "Over 50% of Malaysian adults are classified as overweight or obese.",
            true
        ));

        // Q8
        questions.add(new TrueFalseQuestion(
            "Herd immunity means only one person in a community is vaccinated.",
            false  // herd immunity requires ENOUGH people vaccinated (typically 60–95%)
        ));

        // ── MCQ QUESTIONS (Q9 – Q16) ───────────────────────────────────────────
        // new MCQQuestion("question", new String[]{...}, "correctLetter")
        // 'new String[]{...}' → creates an ANONYMOUS String array inline.
        // The third argument is the letter String of the correct option.

        // Q9
        questions.add(new MCQQuestion(
            "How many Sustainable Development Goals (SDGs) did the United Nations set?",
            new String[]{"A. 10", "B. 15", "C. 17", "D. 20"},
            "C"  // correct answer is option C (17 SDGs)
        ));

        // Q10
        questions.add(new MCQQuestion(
            "According to WHO, how many deaths per year do vaccines prevent?",
            new String[]{"A. 500,000", "B. 1 million", "C. 2-3 million", "D. 10 million"},
            "C"
        ));

        // Q11
        questions.add(new MCQQuestion(
            "Which of the following is a communicable disease?",
            new String[]{"A. Diabetes", "B. Heart disease", "C. Tuberculosis (TB)", "D. Cancer"},
            "C"
        ));

        // Q12
        questions.add(new MCQQuestion(
            "What percentage of global deaths do non-communicable diseases account for?",
            new String[]{"A. 30%", "B. 45%", "C. 63%", "D. 80%"},
            "C"
        ));

        // Q13
        questions.add(new MCQQuestion(
            "What is the 5-4-3-2-1 technique used for?",
            new String[]{"A. Counting calories", "B. Grounding during stress/anxiety", "C. Exercise sets", "D. Sleep routine"},
            "B"
        ));

        // Q14
        questions.add(new MCQQuestion(
            "Which disease is spread by infected mosquitoes?",
            new String[]{"A. Tuberculosis", "B. HIV", "C. Diabetes", "D. Malaria"},
            "D"
        ));

        // Q15
        questions.add(new MCQQuestion(
            "How much does an outpatient visit at a Malaysian public hospital cost for citizens?",
            new String[]{"A. RM5", "B. RM10", "C. RM1", "D. Free"},
            "C"
        ));

        // Q16
        questions.add(new MCQQuestion(
            "How many people worldwide suffer from diabetes?",
            new String[]{"A. 100 million", "B. 200 million", "C. 422 million", "D. 600 million"},
            "C"
        ));

        // ── FILL IN THE BLANK QUESTIONS (Q17 – Q22) ──────────────────────────
        // new FillBlankQuestion("question text with ___", "expectedAnswer")
        // The answer is compared case-insensitively in FillBlankQuestion.checkAnswer().

        // Q17
        questions.add(new FillBlankQuestion(
            "The United Nations goal to ensure healthy lives for all is called SDG ___.",
            "3"  // just the digit
        ));

        // Q18
        questions.add(new FillBlankQuestion(
            "Depression is the leading cause of ___ worldwide.",
            "disability"
        ));

        // Q19
        questions.add(new FillBlankQuestion(
            "Tobacco kills more than ___ million people per year globally.",
            "8"
        ));

        // Q20
        questions.add(new FillBlankQuestion(
            "Vaccines have reduced polio by ___% globally.",
            "99"
        ));

        // Q21
        questions.add(new FillBlankQuestion(
            "In Malaysia, 1 in ___ adults is diagnosed with diabetes.",
            "5"
        ));

        // Q22
        questions.add(new FillBlankQuestion(
            "The abbreviation UHC stands for Universal Health ___.",
            "Coverage"
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILD QUIZ SCREEN
    // Constructs and returns the JPanel shown while the user answers questions.
    // 'private' → only called from the constructor; internal UI setup.
    // 'JPanel'  → return type; gives back the fully built screen panel.
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildQuizScreen() {
        // JPanel = a lightweight container for grouping UI components.
        // new JPanel(new BorderLayout()) → creates a panel with BorderLayout.
        // BorderLayout divides space into NORTH, SOUTH, EAST, WEST, CENTER.
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(0xF5F4FF)); // very light purple background

        // ── HEADER ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE); // solid purple background for the header bar
        header.setBorder(new EmptyBorder(14, 16, 12, 16));
        // EmptyBorder(top, left, bottom, right) → adds padding (empty space) around content

        JLabel title = new JLabel("Quiz — SDG 3 Health");
        // JLabel = non-interactive text display component
        title.setFont(new Font("Arial", Font.BOLD, 18));
        // new Font(name, style, size) → Font.BOLD makes it bold; size 18pt
        title.setForeground(WHITE); // white text on purple background

        // Progress bar — shows how many questions completed out of total
        quizProgress = new JProgressBar(0, questions.size());
        // JProgressBar(min, max) → 0 to 22 (number of questions)
        quizProgress.setValue(0);              // start at 0
        quizProgress.setStringPainted(false);  // don't show number text on bar
        quizProgress.setBackground(new Color(0xAA99EE)); // lighter purple track
        quizProgress.setForeground(WHITE);     // white fill as it progresses
        quizProgress.setBorderPainted(false);  // no border around the bar
        quizProgress.setPreferredSize(new Dimension(0, 7)); // 7px tall, full width

        questionNumberLabel = new JLabel("Question 1 / " + questions.size());
        // String concatenation: "Question 1 / " + 22 → "Question 1 / 22"
        questionNumberLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        // Font.PLAIN = no bold, no italic
        questionNumberLabel.setForeground(new Color(0xDDDDFF)); // very light purple text

        // BoxLayout stacks components vertically (Y_AXIS)
        JPanel headerTop = new JPanel();
        headerTop.setLayout(new BoxLayout(headerTop, BoxLayout.Y_AXIS));
        // BoxLayout.Y_AXIS → children are laid out top to bottom
        headerTop.setOpaque(false); // transparent background (shows parent PURPLE)
        headerTop.add(title);
        headerTop.add(Box.createVerticalStrut(6));  // 6px empty vertical space
        headerTop.add(questionNumberLabel);
        headerTop.add(Box.createVerticalStrut(8));
        headerTop.add(quizProgress);

        header.add(headerTop, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH); // header sits at the top

        // ── SCROLLABLE BODY ───────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); // stack vertically
        body.setBackground(new Color(0xF5F4FF));
        body.setBorder(new EmptyBorder(14, 14, 14, 14)); // padding on all sides

        // Question type badge (MCQ / True/False / Fill Blank)
        questionTypeLabel = new JLabel("MCQ"); // placeholder; updated in showQuestion()
        questionTypeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        questionTypeLabel.setOpaque(true); // must be true for background colour to show
        questionTypeLabel.setBackground(PURPLE_LIGHT);
        questionTypeLabel.setForeground(PURPLE_DARK);
        questionTypeLabel.setBorder(new EmptyBorder(3, 10, 3, 10)); // pill-like padding
        questionTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Component.LEFT_ALIGNMENT → aligns this label to the left inside BoxLayout

        // The question text — wrapped in HTML tags so it word-wraps
        questionLabel = new JLabel("<html>Question text here</html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionLabel.setForeground(TEXT_DARK);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hint text below the question
        hintLabel = new JLabel("Hint"); // placeholder; updated in showQuestion()
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12)); // Font.ITALIC = italic style
        hintLabel.setForeground(TEXT_MED);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Answer panel — its contents are rebuilt for each question type
        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setOpaque(false); // transparent; shows the body background
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Submit button
        submitBtn = new JButton("Submit Answer");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setBackground(PURPLE);
        submitBtn.setForeground(WHITE);
        submitBtn.setOpaque(true);           // must be true to show background colour on some OS
        submitBtn.setBorderPainted(false);   // remove default button border
        submitBtn.setFocusPainted(false);    // remove dotted focus rectangle
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // pointer cursor on hover
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        // Integer.MAX_VALUE → makes the button stretch to full available width
        submitBtn.addActionListener(e -> submitAnswer());
        // addActionListener → registers a click listener
        // e -> submitAnswer() → lambda expression: when clicked, call submitAnswer()
        // 'e' is the ActionEvent object (we don't use it here, but it must be in the signature)

        // Feedback label — "✓ Correct!" or "✗ Wrong!"
        feedbackLabel = new JLabel(""); // empty until user submits
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Correct answer reveal label
        correctAnswerLabel = new JLabel(""); // empty until a wrong answer is submitted
        correctAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        correctAnswerLabel.setForeground(TEXT_MED);
        correctAnswerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Next question button — hidden until after submission
        nextBtn = new JButton("Next Question →");
        nextBtn.setFont(new Font("Arial", Font.BOLD, 13));
        nextBtn.setBackground(GREEN);
        nextBtn.setForeground(WHITE);
        nextBtn.setOpaque(true);
        nextBtn.setBorderPainted(false);
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nextBtn.setVisible(false);  // HIDDEN at first; made visible after user submits
        nextBtn.addActionListener(e -> goToNextQuestion());

        // Add all components to the body in order (top to bottom)
        body.add(questionTypeLabel);
        body.add(Box.createVerticalStrut(10)); // 10px gap
        body.add(questionLabel);
        body.add(Box.createVerticalStrut(6));
        body.add(hintLabel);
        body.add(Box.createVerticalStrut(14));
        body.add(answerPanel);
        body.add(Box.createVerticalStrut(14));
        body.add(submitBtn);
        body.add(Box.createVerticalStrut(10));
        body.add(feedbackLabel);
        body.add(Box.createVerticalStrut(4));
        body.add(correctAnswerLabel);
        body.add(Box.createVerticalStrut(10));
        body.add(nextBtn);

        // Wrap the body in a scroll pane so it's scrollable on small screens
        JScrollPane scroll = new JScrollPane(body);
        // JScrollPane adds vertical/horizontal scrollbars automatically if content overflows
        scroll.setBorder(null); // no border on the scroll pane itself
        scroll.getVerticalScrollBar().setUnitIncrement(12); // scroll speed (12px per click)
        screen.add(scroll, BorderLayout.CENTER); // fill the centre of the screen

        return screen;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILD RESULT SCREEN
    // Shown after all questions are answered. Displays score + motivation.
    // 'private' → internal UI setup; only called from constructor.
    // 'JPanel'  → return type; returns the completed result screen panel.
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(PURPLE_LIGHT);

        // Header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE);
        header.setBorder(new EmptyBorder(16, 16, 14, 16));
        JLabel title = new JLabel("** Quiz Results **");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH);

        // Body — centred vertically using BoxLayout + vertical glue
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); // vertical stack
        body.setOpaque(false); // transparent; shows PURPLE_LIGHT background
        body.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Big score display e.g. "15 / 22"
        scoreLabel = new JLabel("0 / 0", SwingConstants.CENTER);
        // SwingConstants.CENTER → aligns text to centre within the label's space
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48)); // large 48pt font
        scoreLabel.setForeground(PURPLE_DARK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Percentage e.g. "68%"
        percentLabel = new JLabel("0%", SwingConstants.CENTER);
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        percentLabel.setForeground(PURPLE);
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Motivational message e.g. "That's good!"
        motivationLabel = new JLabel("", SwingConstants.CENTER); // empty until showResults() fills it
        motivationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        motivationLabel.setForeground(PURPLE_DARK);
        motivationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Retry button — resets quiz from Question 1
        retryBtn = new JButton("Retry Quiz");
        retryBtn.setFont(new Font("Arial", Font.BOLD, 14));
        retryBtn.setBackground(PURPLE);
        retryBtn.setForeground(WHITE);
        retryBtn.setOpaque(true);
        retryBtn.setBorderPainted(false);
        retryBtn.setFocusPainted(false);
        retryBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        retryBtn.setMaximumSize(new Dimension(300, 44));
        retryBtn.addActionListener(e -> resetQuiz());
        // Lambda: when clicked, call resetQuiz() — resets all state and goes back to Q1

        // Home / back button — returns to main menu
        homeBtn = new JButton("Back to Home");
        homeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        homeBtn.setBackground(GREEN);
        homeBtn.setForeground(WHITE);
        homeBtn.setOpaque(true);
        homeBtn.setBorderPainted(false);
        homeBtn.setFocusPainted(false);
        homeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        homeBtn.setMaximumSize(new Dimension(300, 44));
        homeBtn.addActionListener(e -> {
            // Anonymous lambda block — runs when button is clicked
            if (onQuizComplete != null) onQuizComplete.run();
            // Null check: if no callback was provided, don't call it (avoids NullPointerException)
            // onQuizComplete.run() → executes the Runnable passed in the constructor
        });

        // Box.createVerticalGlue() → flexible space that pushes content toward the centre
        // Adding glue above and below centres the score/buttons vertically.
        body.add(Box.createVerticalGlue());
        body.add(scoreLabel);
        body.add(Box.createVerticalStrut(6));
        body.add(percentLabel);
        body.add(Box.createVerticalStrut(16));
        body.add(motivationLabel);
        body.add(Box.createVerticalStrut(30));
        body.add(retryBtn);
        body.add(Box.createVerticalStrut(12));
        body.add(homeBtn);
        body.add(Box.createVerticalGlue());

        screen.add(body, BorderLayout.CENTER);
        return screen;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHOW QUESTION
    // Updates all UI elements to match the current question.
    // 'private' → called internally whenever we move to a new question.
    // 'void'    → no return value; it just updates the existing components.
    // ─────────────────────────────────────────────────────────────────────────
    private void showQuestion() {
        // Safety check — if we've gone past all questions, show results instead
        if (currentIndex >= questions.size()) {
            showResults();
            return; // 'return' exits the method immediately (no more code runs below)
        }

        Answerable q = questions.get(currentIndex);
        // questions.get(currentIndex) → retrieves the object at position currentIndex
        // from the ArrayList. Returns type Answerable (the interface type).
        // We can then call .getQuestion(), .checkAnswer() etc. through the interface.

        // Reset per-question state before showing the new question
        answered       = false;    // allow submission again
        selectedAnswer = "";       // clear previously selected option
        feedbackLabel.setText(""); // clear "Correct!" or "Wrong!" from last question
        correctAnswerLabel.setText("");
        nextBtn.setVisible(false);     // hide Next button until user submits
        submitBtn.setVisible(true);
        submitBtn.setEnabled(true);    // re-enable submit button (was disabled after last submit)

        // Update "Question X / 22" counter in the header
        questionNumberLabel.setText("Question " + (currentIndex + 1) + " / " + questions.size());
        // currentIndex + 1 → convert 0-based index to 1-based display number

        // Update progress bar to show how far through the quiz we are
        quizProgress.setValue(currentIndex);

        // Update the type badge label (e.g. "MCQ", "True / False", "Fill in the Blank")
        questionTypeLabel.setText(q.getQuestionType());
        // q.getQuestionType() → calls the method through the Answerable interface
        // which dispatches to the actual class's implementation (polymorphism)

        // Update the question text, wrapped in HTML for automatic line-wrapping
        questionLabel.setText("<html>" + q.getQuestion() + "</html>");

        // Update the hint below the question
        hintLabel.setText(q.getHint());

        // Clear the answer panel completely and rebuild it for this question's type
        answerPanel.removeAll();
        // removeAll() → removes ALL child components from the panel

        // 'instanceof' → checks if the object is of a specific class type
        // e.g. q instanceof MCQQuestion → true if q is actually an MCQQuestion object
        // Even though q is declared as Answerable, it holds a real MCQ/TF/Fill object
        if (q instanceof MCQQuestion) {
            buildMCQAnswerPanel((MCQQuestion) q);
            // (MCQQuestion) q → CASTING: tells Java "treat q as MCQQuestion"
            // Needed to call getOptions() which is NOT in the Answerable interface
        } else if (q instanceof TrueFalseQuestion) {
            buildTrueFalseAnswerPanel();
            // No cast needed — no TrueFalse-specific methods called here
        } else if (q instanceof FillBlankQuestion) {
            buildFillBlankAnswerPanel();
        }

        // Refresh the UI to reflect the changes made above
        answerPanel.revalidate(); // recalculate layout of answerPanel's children
        answerPanel.repaint();    // redraw answerPanel on screen
        revalidate();             // recalculate QuizManager's own layout
        repaint();                // redraw QuizManager on screen
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILD ANSWER PANELS — one method per question type
    // Each method clears answerPanel and fills it with the right components.
    // ─────────────────────────────────────────────────────────────────────────

    // MCQ: 4 clickable option buttons (A, B, C, D)
    // 'private' → internal helper; only called from showQuestion()
    // 'void'    → no return value; directly modifies answerPanel
    // Parameter: MCQQuestion q — the current MCQ question (needed for its options array)
    private void buildMCQAnswerPanel(MCQQuestion q) {
        String[] options = q.getOptions();
        // q.getOptions() → calls the EXTRA getter on MCQQuestion (not in interface)
        // Returns String[] with 4 elements e.g. {"A. Yes", "B. No", "C. Maybe", "D. Never"}

        for (int i = 0; i < options.length; i++) {
            // Standard for-loop: i starts at 0, runs while i < 4, increments i each time
            JButton btn = new JButton(options[i]); // button label = full option text
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            btn.setBackground(WHITE);
            btn.setForeground(TEXT_DARK);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
            // BorderFactory.createLineBorder(color, thickness) → adds a coloured border
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // full width, 40px tall

            // Extract just the letter (first character) from the option text
            String letter = options[i].substring(0, 1);
            // .substring(0, 1) → returns characters from index 0 up to (not including) 1
            // e.g. "A. Diabetes".substring(0,1) → "A"

            // Lambda click handler for this button
            // 'e -> { ... }' is an ActionListener lambda
            // Note: 'letter' and 'btn' are captured from the enclosing scope (effectively final)
            btn.addActionListener(e -> {
                // Step 1: Reset all option buttons to unselected appearance
                for (Component c : answerPanel.getComponents()) {
                    // Enhanced for-loop over all children of answerPanel
                    if (c instanceof JButton) {
                        // Cast to JButton to call JButton-specific methods
                        c.setBackground(WHITE);
                        ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
                    }
                }
                // Step 2: Highlight THIS button as selected
                btn.setBackground(PURPLE_LIGHT);
                btn.setBorder(BorderFactory.createLineBorder(PURPLE, 2)); // thicker border = selected
                selectedAnswer = letter;
                // Store the letter so submitAnswer() can read which option was chosen
            });

            optionButtons[i] = btn; // store reference in the array
            answerPanel.add(btn);
            answerPanel.add(Box.createVerticalStrut(8)); // gap between buttons
        }
    }

    // True/False: two buttons — "True" and "False"
    private void buildTrueFalseAnswerPanel() {
        String[] choices = {"True", "False"};
        // A simple two-element String array defined inline

        for (String choice : choices) {
            // Enhanced for-loop: iterates over {"True", "False"}
            JButton btn = new JButton(choice); // button text = "True" or "False"
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(WHITE);
            btn.setForeground(TEXT_DARK);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

            btn.addActionListener(e -> {
                // Unhighlight both buttons first
                for (Component c : answerPanel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(WHITE);
                        ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
                    }
                }
                // Highlight the clicked button
                btn.setBackground(PURPLE_LIGHT);
                btn.setBorder(BorderFactory.createLineBorder(PURPLE, 2));
                selectedAnswer = choice;
                // 'choice' is "True" or "False" — stored so submitAnswer() can read it
                // TrueFalseQuestion.checkAnswer() calls Boolean.parseBoolean(selectedAnswer)
            });

            answerPanel.add(btn);
            answerPanel.add(Box.createVerticalStrut(8));
        }
    }

    // Fill in the blank: a text input field
    // 'void' → no return; directly adds the text field to answerPanel
    private void buildFillBlankAnswerPanel() {
        fillField = new JTextField();
        // JTextField = single-line text input component. User types their answer here.
        fillField.setFont(new Font("Arial", Font.PLAIN, 14));
        fillField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE, 1),        // outer: purple line border
            BorderFactory.createEmptyBorder(6, 8, 6, 8)       // inner: padding around typed text
        ));
        // createCompoundBorder(outsideBorder, insideBorder) → combines two borders
        fillField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // full width, 40px tall
        fillField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Pressing Enter in the text field also triggers submitAnswer() —
        // same as clicking the Submit button
        fillField.addActionListener(e -> submitAnswer());

        answerPanel.add(fillField);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SUBMIT ANSWER
    // Checks the answer, shows feedback, updates score.
    // 'private' → called by Submit button's ActionListener and fillField's Enter key listener
    // 'void'    → no return value; modifies UI state directly
    // ─────────────────────────────────────────────────────────────────────────
    private void submitAnswer() {
        // Guard clause: if user already answered this question, do nothing.
        // Prevents accidental double-submission.
        if (answered) return;

        Answerable q = questions.get(currentIndex);
        // Retrieve the current question object from the list

        // Determine what the user's answer is based on question type
        String userAnswer;
        if (q instanceof FillBlankQuestion) {
            // For fill-in-blank, read the JTextField content
            userAnswer = (fillField != null) ? fillField.getText().trim() : "";
            // Ternary: if fillField exists, get its text (trimmed); else use empty string
            // .getText() → returns the String currently in the text field
            // .trim()    → removes leading/trailing whitespace the user might have typed
        } else {
            // For MCQ and TrueFalse, use the selectedAnswer stored by the button click handler
            userAnswer = selectedAnswer;
        }

        // Handle empty answer — user clicked Submit without selecting anything
        if (userAnswer.isEmpty()) {
            // .isEmpty() → returns true if the String has length 0
            feedbackLabel.setText("⚠  Please choose or type an answer first!");
            feedbackLabel.setForeground(new Color(0xCC6600)); // orange warning colour
            return; // exit method — don't mark as answered, let them try again
        }

        // Lock in the answer — prevent re-submission
        answered = true;
        submitBtn.setEnabled(false);
        // setEnabled(false) → makes the button unclickable and visually greyed out

        // Check the answer using the interface method
        boolean isCorrect = q.checkAnswer(userAnswer);
        // Polymorphism: Java calls the correct checkAnswer() based on the actual object type
        // MCQQuestion.checkAnswer()       → compares letters
        // TrueFalseQuestion.checkAnswer() → parses to boolean and compares
        // FillBlankQuestion.checkAnswer() → equalsIgnoreCase comparison

        if (isCorrect) {
            score++; // '++' → increment operator; score = score + 1
            feedbackLabel.setText("✓  Correct!");
            feedbackLabel.setForeground(new Color(0x1D7A45)); // dark green
            highlightAnswerFeedback(true); // colour the answer area green
        } else {
            feedbackLabel.setText("✗  Wrong!");
            feedbackLabel.setForeground(new Color(0xCC2222)); // red
            // Reveal the correct answer
            correctAnswerLabel.setText("Correct answer: " + q.getCorrectAnswer());
            highlightAnswerFeedback(false); // colour the answer area red
        }

        // Show the Next button after submission
        nextBtn.setVisible(true); // was hidden; now visible
        if (currentIndex == questions.size() - 1) {
            // Last question → change button text to "See Results"
            nextBtn.setText("See Results →");
        } else {
            nextBtn.setText("Next Question →");
        }
    }

    // Highlights the answer area green (correct) or red (wrong) after submission.
    // 'private' → internal helper called only from submitAnswer()
    // Parameter: boolean correct — true = green highlight, false = red highlight
    private void highlightAnswerFeedback(boolean correct) {
        Color bg = correct ? CORRECT_CLR : WRONG_CLR;
        // Ternary: if correct → use light green, else use light red

        for (Component c : answerPanel.getComponents()) {
            // Iterate over everything in the answer panel
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                // Only highlight the selected button (not all 4 options)
                if (btn.getText().startsWith(selectedAnswer) || btn.getText().equals(selectedAnswer)) {
                    // .startsWith(selectedAnswer) handles MCQ: btn text "A. ..." starts with "A"
                    // .equals(selectedAnswer)     handles TrueFalse: btn text IS "True" or "False"
                    btn.setBackground(bg);
                }
                btn.setEnabled(false); // disable ALL buttons so user can't change selection after submit
            } else if (c instanceof JTextField) {
                // For fill-in-blank text field
                c.setBackground(bg);              // colour the background
                ((JTextField) c).setEditable(false); // prevent further typing
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GO TO NEXT QUESTION
    // Called when user clicks "Next Question →" or "See Results →"
    // ─────────────────────────────────────────────────────────────────────────
    private void goToNextQuestion() {
        currentIndex++;
        // '++' post-increment: currentIndex = currentIndex + 1
        // Move to the next question in the list

        if (currentIndex >= questions.size()) {
            showResults(); // all questions answered — go to result screen
        } else {
            showQuestion(); // load the next question
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHOW RESULTS
    // Calculates percentage, picks motivation message, awards badge, saves score.
    // 'private' → called from goToNextQuestion() when all questions are done
    // 'void'    → no return; modifies result screen labels and switches card
    // ─────────────────────────────────────────────────────────────────────────
    private void showResults() {
        int total = questions.size(); // e.g. 22

        // Calculate percentage — use ternary to avoid dividing by zero (ZeroDivisionError)
        int percent = (total > 0) ? (score * 100 / total) : 0;
        // Integer division: 15 * 100 / 22 = 1500 / 22 = 68 (truncated, not rounded)

        // Update result screen labels with actual values
        scoreLabel.setText(score + " / " + total);
        percentLabel.setText(percent + "%");

        // Pick motivational message based on score percentage range (from project spec)
        // Plain text symbols (* instead of emojis) used for cross-platform compatibility (Windows)
        String motivation;
        if (percent >= 80) {
            motivation = "★  Outstanding!";
        } else if (percent >= 60) {
            motivation = "★  That's good!";
        } else if (percent >= 40) {
            motivation = "★  Good try!";
        } else if (percent >= 20) {
            motivation = "★  You can do better!";
        } else {
            motivation = "★  Don't give up!";
        }
        // These are if-else if-else chains — each condition only runs if all previous were false.

        motivationLabel.setText(motivation);

        // Save the score to file
        saveScoreToFile(score, total, percent);

        // Switch the visible card from "QUIZ" to "RESULT"
        cardLayout.show(cardContainer, "RESULT");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SAVE SCORE TO FILE
    // Appends the new score to quiz_scores.txt so it persists between sessions.
    // 'private' → only called from showResults()
    // Parameters: score (int), total (int), percent (int) — the results to write
    // 'void'    → no return value; performs a file write side effect
    // ─────────────────────────────────────────────────────────────────────────
    private void saveScoreToFile(int score, int total, int percent) {
        try {
            // try-catch block: attempt risky code; catch errors if they occur

            FileWriter fw = new FileWriter(SCORE_FILE, true);
            // FileWriter → writes characters to a file
            // SCORE_FILE  → the constant "quiz_scores.txt"
            // true        → APPEND mode: adds to end of file instead of overwriting

            BufferedWriter bw = new BufferedWriter(fw);
            // BufferedWriter wraps FileWriter to add buffering.
            // Buffering = writes to memory first, then flushes to disk in one go.
            // More efficient than writing character by character.

            // Build the line to write: e.g. "Mon Jun 03 2024 | Score: 15 / 22 | 68%"
            String line = new java.util.Date() + " | Score: " + score
                        + " / " + total + " | " + percent + "%";
            // new java.util.Date() → creates a Date object representing right now
            // String concatenation with '+' joins all parts into one String

            bw.write(line);   // write the line to the buffer
            bw.newLine();     // write a line break (\n) so next score is on a new line
            bw.close();       // flush buffer to disk and close the file
            System.out.println("[QuizManager] Score saved to " + SCORE_FILE);
            // System.out.println → prints to the console (terminal), not the GUI

        } catch (IOException e) {
            // IOException is thrown when file operations fail (e.g. no write permission)
            // We catch it here so the app doesn't crash — it just prints an error message
            System.out.println("[QuizManager] Could not save score: " + e.getMessage());
            // e.getMessage() → returns the error description as a String
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RESET QUIZ
    // Resets all state so the user can retake the quiz from Question 1.
    // 'private' → called by the Retry button's ActionListener
    // 'void'    → no return value; resets fields and reloads the first question
    // ─────────────────────────────────────────────────────────────────────────
    private void resetQuiz() {
        currentIndex   = 0;   // go back to first question
        score          = 0;   // reset score to 0
        answered       = false;
        selectedAnswer = "";  // clear any previously selected answer
        cardLayout.show(cardContainer, "QUIZ"); // switch back to quiz card
        showQuestion(); // display Question 1
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUBLIC GETTERS — used by GamificationEngine to read quiz results
    // 'public'  → accessible from outside (GamificationEngine calls these)
    // 'int'     → return type; gives back an integer value
    // These are getter methods — they provide read-only access to private fields
    // without exposing the fields directly (encapsulation).
    // ─────────────────────────────────────────────────────────────────────────
    public int getScore()  { return score; }
    // Returns the number of correct answers (0 to 22)

    public int getTotal()  { return questions.size(); }
    // questions.size() → ArrayList method; returns the number of elements (22)

    public int getPercent() {
        return (questions.size() > 0) ? (score * 100 / questions.size()) : 0;
        // Ternary with null-safety: avoids dividing by zero if list is somehow empty
        // Integer division: result is truncated (e.g. 68.18 becomes 68)
    }
}
