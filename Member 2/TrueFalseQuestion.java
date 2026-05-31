// Class      : TrueFalseQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Represents a True or False question.
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

public class TrueFalseQuestion implements Answerable {

    // ── Attributes ────────────────────────────────────────────────────────────
    private String  question;      // the statement shown to the user
    private boolean correctAnswer; // true if the statement is correct, false if not

    // ── Constructor ───────────────────────────────────────────────────────────
    public TrueFalseQuestion(String question, boolean correctAnswer) {
        this.question      = question;
        this.correctAnswer = correctAnswer;
    }

    // ── Answerable interface methods ──────────────────────────────────────────

    @Override
    public String getQuestion() {
        return question;
    }

    // Accepts "True" or "False" string (any case) and compares to the correct boolean
    @Override
    public boolean checkAnswer(String userAnswer) {
        boolean userBool = Boolean.parseBoolean(userAnswer.trim().toLowerCase());
        return userBool == correctAnswer;
    }

    // Returns "True" or "False" as a readable string
    @Override
    public String getCorrectAnswer() {
        return correctAnswer ? "True" : "False";
    }

    @Override
    public String getQuestionType() {
        return "True / False";
    }

    @Override
    public String getHint() {
        return "Select True or False";
    }
}
