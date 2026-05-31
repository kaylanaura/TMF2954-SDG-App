// Class      : MCQQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Represents a multiple-choice question with 4 options (A/B/C/D).
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

public class MCQQuestion implements Answerable {

    // ── Attributes ────────────────────────────────────────────────────────────
    private String   question;      // the question text shown to the user
    private String[] options;       // 4 choices e.g. {"A. Yes", "B. No", ...}
    private String   correctLetter; // the correct option letter e.g. "C"

    // ── Constructor ───────────────────────────────────────────────────────────
    public MCQQuestion(String question, String[] options, String correctLetter) {
        this.question      = question;
        this.options       = options;
        this.correctLetter = correctLetter.toUpperCase(); // always store as uppercase
    }

    // ── Answerable interface methods ──────────────────────────────────────────

    @Override
    public String getQuestion() {
        return question;
    }

    // Check if the user picked the right letter (A/B/C/D), ignores case
    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer.trim().toUpperCase().equals(correctLetter);
    }

    // Returns the full option text for the correct letter e.g. "C. 17"
    @Override
    public String getCorrectAnswer() {
        for (String opt : options) {
            if (opt.startsWith(correctLetter)) return opt;
        }
        return correctLetter; // fallback — just return the letter
    }

    @Override
    public String getQuestionType() {
        return "MCQ";
    }

    @Override
    public String getHint() {
        return "Choose A, B, C or D";
    }

    // ── Extra getter — QuizManager needs this to build the option buttons ─────
    public String[] getOptions() {
        return options;
    }
}
