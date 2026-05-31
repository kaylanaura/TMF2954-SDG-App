// Class      : QuizManager
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: The main class for the Quiz Module. It stores all 20+ questions,
//              tracks the user's score, checks answers, and shows the result with
//              a motivational message. It also saves scores to a text file.
//              Implements the quiz screen as a Swing GUI panel.
//              Interacts with GamificationEngine to trigger badges and points.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;   // explicit import to avoid clash with java.awt.List
import javax.swing.*;
import javax.swing.border.*;

public class QuizManager extends JPanel {

    // ── Colours (matching the app's purple quiz theme) ────────────────────────
    private static final Color PURPLE       = new Color(0x7F77DD);
    private static final Color PURPLE_DARK  = new Color(0x3C3489);
    private static final Color PURPLE_LIGHT = new Color(0xEEEDFE);
    private static final Color GREEN        = new Color(0x1D9E75);
    private static final Color WHITE        = Color.WHITE;
    private static final Color TEXT_DARK    = new Color(0x1A1A1A);
    private static final Color TEXT_MED     = new Color(0x555555);
    private static final Color CORRECT_CLR  = new Color(0xD4EDDA);  // light green for correct
    private static final Color WRONG_CLR    = new Color(0xF8D7DA);  // light red for wrong

    // ── Score file path ───────────────────────────────────────────────────────
    // Scores are saved here — hardcoding scores is NOT allowed per project spec
    private static final String SCORE_FILE = "quiz_scores.txt";

    // ── Question list and state ───────────────────────────────────────────────
    private List<Answerable> questions;   // all 20+ questions
    private int currentIndex = 0;        // which question we're on (0-based)
    private int score = 0;               // how many correct so far
    private boolean answered = false;    // has the user answered this question yet

    // ── CardLayout to switch between quiz screen and result screen ────────────
    private CardLayout cardLayout;
    private JPanel cardContainer;

    // ── Quiz screen UI components ─────────────────────────────────────────────
    private JLabel  questionNumberLabel;  // shows "Question 3 / 20"
    private JLabel  questionTypeLabel;    // shows "MCQ" or "True/False" etc.
    private JLabel  questionLabel;        // the actual question text
    private JLabel  hintLabel;            // hint e.g. "Choose A, B, C or D"
    private JPanel  answerPanel;          // holds answer buttons/field
    private JButton submitBtn;            // submit answer button
    private JLabel  feedbackLabel;        // shows "Correct!" or "Wrong!"
    private JLabel  correctAnswerLabel;   // shows correct answer after attempt
    private JButton nextBtn;             // go to next question
    private JProgressBar quizProgress;   // shows how far through the quiz

    // ── Result screen UI components ───────────────────────────────────────────
    private JLabel scoreLabel;           // e.g. "You scored 15 / 20"
    private JLabel percentLabel;         // e.g. "75%"
    private JLabel motivationLabel;      // e.g. "That's good!"
    private JButton retryBtn;            // retake the quiz
    private JButton homeBtn;             // go back to main menu

    // ── MCQ option buttons (up to 4 choices) ─────────────────────────────────
    private JButton[] optionButtons = new JButton[4];

    // ── Fill-in-the-blank text field ──────────────────────────────────────────
    private JTextField fillField;

    // ── Callback to switch back to learning or gamification panel ────────────
    private Runnable onQuizComplete;  // called when user finishes and clicks home

    // ── Currently selected answer (for MCQ and T/F) ──────────────────────────
    private String selectedAnswer = "";

    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────────────────────────────────────
    public QuizManager(Runnable onQuizComplete) {
        this.onQuizComplete = onQuizComplete;

        // Load all questions into the list
        questions = new ArrayList<>();
        loadQuestions();

        // Set up the CardLayout to switch between quiz and result screens
        cardLayout    = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(WHITE);

        // Build both screens and add to card container
        cardContainer.add(buildQuizScreen(),   "QUIZ");
        cardContainer.add(buildResultScreen(), "RESULT");

        // Add the card container to this panel
        setLayout(new BorderLayout());
        add(cardContainer, BorderLayout.CENTER);

        // Start on the quiz screen
        cardLayout.show(cardContainer, "QUIZ");
        showQuestion();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // LOAD QUESTIONS
    // Adds all 20+ questions. Mix of MCQ, True/False, and Fill-in-blank.
    // ─────────────────────────────────────────────────────────────────────────
    private void loadQuestions() {

        // ── TRUE / FALSE QUESTIONS (Q1 – Q8) ──────────────────────────────────

        // Q1
        questions.add(new TrueFalseQuestion(
            "SDG 3 stands for 'Good Health and Well-Being'.",
            true
        ));

        // Q2
        questions.add(new TrueFalseQuestion(
            "Vaccines have eliminated smallpox completely from the world.",
            true
        ));

        // Q3
        questions.add(new TrueFalseQuestion(
            "Depression affects fewer than 100 million people worldwide.",
            false  // actually 280 million
        ));

        // Q4
        questions.add(new TrueFalseQuestion(
            "Tobacco kills more than 8 million people per year globally.",
            true
        ));

        // Q5
        questions.add(new TrueFalseQuestion(
            "Non-communicable diseases (NCDs) can spread from person to person.",
            false  // NCDs are NOT contagious
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
            false  // herd immunity requires ENOUGH people to be vaccinated
        ));

        // ── MCQ QUESTIONS (Q9 – Q16) ───────────────────────────────────────────

        // Q9
        questions.add(new MCQQuestion(
            "How many Sustainable Development Goals (SDGs) did the United Nations set?",
            new String[]{"A. 10", "B. 15", "C. 17", "D. 20"},
            "C"  // correct answer is option C
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

        // Q17
        questions.add(new FillBlankQuestion(
            "The United Nations goal to ensure healthy lives for all is called SDG ___.",
            "3"  // answer is just the number
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
    // This is the screen the user sees while answering questions.
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildQuizScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(new Color(0xF5F4FF));

        // ── HEADER ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE);
        header.setBorder(new EmptyBorder(14, 16, 12, 16));

        JLabel title = new JLabel("Quiz — SDG 3 Health");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(WHITE);

        // Progress bar shows how many questions completed
        quizProgress = new JProgressBar(0, questions.size());
        quizProgress.setValue(0);
        quizProgress.setStringPainted(false);
        quizProgress.setBackground(new Color(0xAA99EE));
        quizProgress.setForeground(WHITE);
        quizProgress.setBorderPainted(false);
        quizProgress.setPreferredSize(new Dimension(0, 7));

        questionNumberLabel = new JLabel("Question 1 / " + questions.size());
        questionNumberLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        questionNumberLabel.setForeground(new Color(0xDDDDFF));

        JPanel headerTop = new JPanel();
        headerTop.setLayout(new BoxLayout(headerTop, BoxLayout.Y_AXIS));
        headerTop.setOpaque(false);
        headerTop.add(title);
        headerTop.add(Box.createVerticalStrut(6));
        headerTop.add(questionNumberLabel);
        headerTop.add(Box.createVerticalStrut(8));
        headerTop.add(quizProgress);

        header.add(headerTop, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH);

        // ── SCROLLABLE BODY ───────────────────────────────────────────────────
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(new Color(0xF5F4FF));
        body.setBorder(new EmptyBorder(14, 14, 14, 14));

        // Question type badge (MCQ / True/False / Fill Blank)
        questionTypeLabel = new JLabel("MCQ");
        questionTypeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        questionTypeLabel.setOpaque(true);
        questionTypeLabel.setBackground(PURPLE_LIGHT);
        questionTypeLabel.setForeground(PURPLE_DARK);
        questionTypeLabel.setBorder(new EmptyBorder(3, 10, 3, 10));
        questionTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // The question text
        questionLabel = new JLabel("<html>Question text here</html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        questionLabel.setForeground(TEXT_DARK);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Hint text (e.g. "Choose A, B, C or D")
        hintLabel = new JLabel("Hint");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        hintLabel.setForeground(TEXT_MED);
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Panel that holds the answer options — rebuilt for each question
        answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setOpaque(false);
        answerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Submit button — user clicks this after choosing/typing their answer
        submitBtn = new JButton("Submit Answer");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setBackground(PURPLE);
        submitBtn.setForeground(WHITE);
        submitBtn.setOpaque(true);
        submitBtn.setBorderPainted(false);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        submitBtn.addActionListener(e -> submitAnswer());

        // Feedback label — shows "✓ Correct!" or "✗ Wrong!" after submit
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Shows the correct answer after the user submits
        correctAnswerLabel = new JLabel("");
        correctAnswerLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        correctAnswerLabel.setForeground(TEXT_MED);
        correctAnswerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Next question button — only visible after submitting
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
        nextBtn.setVisible(false);  // hidden until user submits
        nextBtn.addActionListener(e -> goToNextQuestion());

        // Add all parts to the body panel
        body.add(questionTypeLabel);
        body.add(Box.createVerticalStrut(10));
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

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        screen.add(scroll, BorderLayout.CENTER);

        return screen;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILD RESULT SCREEN
    // Shown after all questions are answered. Displays score + motivation.
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildResultScreen() {
        JPanel screen = new JPanel(new BorderLayout());
        screen.setBackground(PURPLE_LIGHT);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE);
        header.setBorder(new EmptyBorder(16, 16, 14, 16));
        JLabel title = new JLabel("** Quiz Results **");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.CENTER);
        screen.add(header, BorderLayout.NORTH);

        // Body — centred vertically
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(30, 20, 30, 20));

        // Big score display e.g. "15 / 22"
        scoreLabel = new JLabel("0 / 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48));
        scoreLabel.setForeground(PURPLE_DARK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Percentage e.g. "68%"
        percentLabel = new JLabel("0%", SwingConstants.CENTER);
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        percentLabel.setForeground(PURPLE);
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Motivational message e.g. "That's good!"
        motivationLabel = new JLabel("", SwingConstants.CENTER);
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

        // Home / back button
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
            // Tell the main app the quiz is done
            if (onQuizComplete != null) onQuizComplete.run();
        });

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
    // ─────────────────────────────────────────────────────────────────────────
    private void showQuestion() {
        // Safety check — if we've gone past all questions, show results
        if (currentIndex >= questions.size()) {
            showResults();
            return;
        }

        Answerable q = questions.get(currentIndex);

        // Reset state for this question
        answered       = false;
        selectedAnswer = "";
        feedbackLabel.setText("");
        correctAnswerLabel.setText("");
        nextBtn.setVisible(false);
        submitBtn.setVisible(true);
        submitBtn.setEnabled(true);

        // Update header info
        questionNumberLabel.setText("Question " + (currentIndex + 1) + " / " + questions.size());
        quizProgress.setValue(currentIndex);

        // Update question type badge
        questionTypeLabel.setText(q.getQuestionType());

        // Update the question text (wrapped in HTML so it wraps properly)
        questionLabel.setText("<html>" + q.getQuestion() + "</html>");

        // Update the hint
        hintLabel.setText(q.getHint());

        // Clear the answer panel and rebuild it for this question type
        answerPanel.removeAll();

        if (q instanceof MCQQuestion) {
            buildMCQAnswerPanel((MCQQuestion) q);
        } else if (q instanceof TrueFalseQuestion) {
            buildTrueFalseAnswerPanel();
        } else if (q instanceof FillBlankQuestion) {
            buildFillBlankAnswerPanel();
        }

        answerPanel.revalidate();
        answerPanel.repaint();
        revalidate();
        repaint();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILD ANSWER PANELS — one method per question type
    // ─────────────────────────────────────────────────────────────────────────

    // MCQ: 4 clickable option buttons (A, B, C, D)
    private void buildMCQAnswerPanel(MCQQuestion q) {
        String[] options = q.getOptions();
        for (int i = 0; i < options.length; i++) {
            JButton btn = new JButton(options[i]);
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            btn.setBackground(WHITE);
            btn.setForeground(TEXT_DARK);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            // Clicking an option highlights it and stores the letter (A/B/C/D)
            String letter = options[i].substring(0, 1); // e.g. "A" from "A. Diabetes"
            btn.addActionListener(e -> {
                // Unhighlight all buttons first
                for (Component c : answerPanel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(WHITE);
                        ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
                    }
                }
                // Highlight the selected button
                btn.setBackground(PURPLE_LIGHT);
                btn.setBorder(BorderFactory.createLineBorder(PURPLE, 2));
                selectedAnswer = letter;
            });

            optionButtons[i] = btn;
            answerPanel.add(btn);
            answerPanel.add(Box.createVerticalStrut(8));
        }
    }

    // True/False: two buttons — "True" and "False"
    private void buildTrueFalseAnswerPanel() {
        String[] choices = {"True", "False"};
        for (String choice : choices) {
            JButton btn = new JButton(choice);
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
                // Unhighlight both, then highlight chosen
                for (Component c : answerPanel.getComponents()) {
                    if (c instanceof JButton) {
                        c.setBackground(WHITE);
                        ((JButton) c).setBorder(BorderFactory.createLineBorder(new Color(0xCCCCFF), 1));
                    }
                }
                btn.setBackground(PURPLE_LIGHT);
                btn.setBorder(BorderFactory.createLineBorder(PURPLE, 2));
                selectedAnswer = choice; // "True" or "False"
            });

            answerPanel.add(btn);
            answerPanel.add(Box.createVerticalStrut(8));
        }
    }

    // Fill in the blank: a text input field
    private void buildFillBlankAnswerPanel() {
        fillField = new JTextField();
        fillField.setFont(new Font("Arial", Font.PLAIN, 14));
        fillField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE, 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        fillField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        fillField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Pressing Enter in the field also submits the answer
        fillField.addActionListener(e -> submitAnswer());

        answerPanel.add(fillField);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SUBMIT ANSWER
    // Checks the answer, shows feedback, updates score.
    // ─────────────────────────────────────────────────────────────────────────
    private void submitAnswer() {
        // Don't allow re-submission
        if (answered) return;

        Answerable q = questions.get(currentIndex);

        // Get the user's answer depending on question type
        String userAnswer;
        if (q instanceof FillBlankQuestion) {
            // For fill-in-the-blank, read the text field
            userAnswer = (fillField != null) ? fillField.getText().trim() : "";
        } else {
            // For MCQ and T/F, use the selected button text
            userAnswer = selectedAnswer;
        }

        // Handle empty answer — prompt user to answer first
        if (userAnswer.isEmpty()) {
            feedbackLabel.setText("⚠  Please choose or type an answer first!");
            feedbackLabel.setForeground(new Color(0xCC6600)); // orange warning
            return;
        }

        // Mark as answered so they can't change it
        answered = true;
        submitBtn.setEnabled(false); // grey out submit button

        // Check the answer using the interface method
        boolean isCorrect = q.checkAnswer(userAnswer);

        if (isCorrect) {
            score++; // increment score
            feedbackLabel.setText("✓  Correct!");
            feedbackLabel.setForeground(new Color(0x1D7A45)); // dark green
            highlightAnswerFeedback(true);
        } else {
            feedbackLabel.setText("✗  Wrong!");
            feedbackLabel.setForeground(new Color(0xCC2222)); // red
            // Show what the correct answer was
            correctAnswerLabel.setText("Correct answer: " + q.getCorrectAnswer());
            highlightAnswerFeedback(false);
        }

        // Show "Next" button (or "See Results" on the last question)
        nextBtn.setVisible(true);
        if (currentIndex == questions.size() - 1) {
            nextBtn.setText("See Results →");
        } else {
            nextBtn.setText("Next Question →");
        }
    }

    // Highlights the answer area green (correct) or red (wrong)
    private void highlightAnswerFeedback(boolean correct) {
        Color bg = correct ? CORRECT_CLR : WRONG_CLR;
        for (Component c : answerPanel.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                // Only highlight the selected button
                if (btn.getText().startsWith(selectedAnswer) || btn.getText().equals(selectedAnswer)) {
                    btn.setBackground(bg);
                }
                btn.setEnabled(false); // disable all options after submit
            } else if (c instanceof JTextField) {
                c.setBackground(bg);
                ((JTextField) c).setEditable(false);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GO TO NEXT QUESTION
    // ─────────────────────────────────────────────────────────────────────────
    private void goToNextQuestion() {
        currentIndex++;
        if (currentIndex >= questions.size()) {
            showResults(); // all questions done
        } else {
            showQuestion(); // next question
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SHOW RESULTS
    // Calculates percentage, picks motivation message, awards badge, saves score.
    // ─────────────────────────────────────────────────────────────────────────
    private void showResults() {
        int total = questions.size();

        // Calculate percentage — avoid dividing by zero
        int percent = (total > 0) ? (score * 100 / total) : 0;

        // Update score display labels
        scoreLabel.setText(score + " / " + total);
        percentLabel.setText(percent + "%");

        // Pick motivational message based on score range (from project spec)
        // Note: plain text symbols used instead of emojis for Windows compatibility
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

        motivationLabel.setText(motivation);

        // Save the score to file (project spec: no hardcoding of scores)
        saveScoreToFile(score, total, percent);

        // Switch to the result screen
        cardLayout.show(cardContainer, "RESULT");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SAVE SCORE TO FILE
    // Appends the new score to quiz_scores.txt so it persists between sessions.
    // ─────────────────────────────────────────────────────────────────────────
    private void saveScoreToFile(int score, int total, int percent) {
        try {
            // Use FileWriter with append=true so old scores are not overwritten
            FileWriter fw = new FileWriter(SCORE_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Write one line per attempt: date, score, total, percent
            String line = new java.util.Date() + " | Score: " + score
                        + " / " + total + " | " + percent + "%";
            bw.write(line);
            bw.newLine();
            bw.close();
            System.out.println("[QuizManager] Score saved to " + SCORE_FILE);

        } catch (IOException e) {
            // If file write fails, print the error but don't crash the app
            System.out.println("[QuizManager] Could not save score: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RESET QUIZ
    // Resets all state so the user can retake the quiz from Question 1.
    // ─────────────────────────────────────────────────────────────────────────
    private void resetQuiz() {
        currentIndex   = 0;
        score          = 0;
        answered       = false;
        selectedAnswer = "";
        cardLayout.show(cardContainer, "QUIZ");
        showQuestion();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GETTER — used by GamificationEngine to read the score after quiz ends
    // ─────────────────────────────────────────────────────────────────────────
    public int getScore()  { return score; }
    public int getTotal()  { return questions.size(); }
    public int getPercent() {
        return (questions.size() > 0) ? (score * 100 / questions.size()) : 0;
    }


}
