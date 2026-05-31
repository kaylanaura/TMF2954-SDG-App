// Class      : FillBlankQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Represents a fill-in-the-blank question where the user
//              types their answer into a text field.
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

public class FillBlankQuestion implements Answerable {

    // ── Attributes ────────────────────────────────────────────────────────────
    private String question;      // the question text, contains a blank "___"
    private String correctAnswer; // the expected word or number

    // ── Constructor ───────────────────────────────────────────────────────────
    public FillBlankQuestion(String question, String correctAnswer) {
        this.question      = question;
        this.correctAnswer = correctAnswer;
    }

    // ── Answerable interface methods ──────────────────────────────────────────

    @Override
    public String getQuestion() {
        return question;
    }

    // Case-insensitive so "coverage" and "Coverage" are both accepted
    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    @Override
    public String getQuestionType() {
        return "Fill in the Blank";
    }

    @Override
    public String getHint() {
        return "Type your answer in the box";
    }
}
