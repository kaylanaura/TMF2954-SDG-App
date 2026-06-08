// Class      : FillBlankQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Tester     : Kayla Binti Mohamad (102641)
// Description: Represents a fill-in-the-blank question where the user
//              types their answer into a text field.
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

// ── 'public class' ───────────────────────────────────────────────────────────
// 'public' → accessible from all classes in the project.
// 'class'  → declares a blueprint for objects.
// Each FillBlankQuestion object has its own 'question' and 'correctAnswer'.

// ── 'implements Answerable' ───────────────────────────────────────────────────
// This class signs the Answerable interface contract.
// It MUST provide method bodies for all 5 methods declared in Answerable.
// This allows QuizManager to store FillBlankQuestion objects in a
// List<Answerable> and call .checkAnswer(), .getQuestion() etc. without
// needing to know the specific class — POLYMORPHISM in OOP.

public class FillBlankQuestion implements Answerable {

    // ── ENCAPSULATION — PRIVATE FIELDS ───────────────────────────────────────
    // 'private' → hidden from outside the class. OOP encapsulation principle.
    // Other classes cannot directly read or change these — they must use methods.

    private String question;      // the question sentence, contains a blank shown as "___"
                                  // e.g. "The United Nations goal is called SDG ___."
                                  // String = a text value (sequence of characters)

    private String correctAnswer; // the expected word or number the blank should be filled with
                                  // e.g. "3", "disability", "Coverage"
                                  // Comparison is done case-insensitively (see checkAnswer)

    // ── CONSTRUCTOR ───────────────────────────────────────────────────────────
    // Runs when QuizManager does: new FillBlankQuestion("question text", "answer")
    // Has the same name as the class and no return type — that's what makes it a constructor.
    // 'public' → can be called from outside this class (QuizManager needs it).
    //
    // Parameters:
    //   String question      — the question sentence with a blank
    //   String correctAnswer — the expected fill-in text
    //
    // 'this.' distinguishes the object's FIELD from the constructor PARAMETER
    // when they share the same name.
    public FillBlankQuestion(String question, String correctAnswer) {
        this.question      = question;
        this.correctAnswer = correctAnswer;
    }

    // ── @Override ANNOTATION ─────────────────────────────────────────────────
    // Marks that each method below fulfils the contract from the Answerable interface.
    // Java verifies the method signature matches what was declared in the interface.

    // ── METHOD: getQuestion() ─────────────────────────────────────────────────
    // 'public'  → QuizManager (outside class) can call this.
    // 'String'  → return type; gives back the question text.
    // 'return question' → sends the stored field value back to the caller.
    @Override
    public String getQuestion() {
        return question;
    }

    // ── METHOD: checkAnswer(String userAnswer) ────────────────────────────────
    // 'boolean' return type → returns true if correct, false if wrong.
    // Parameter: String userAnswer — whatever text the user typed into the text field.
    //
    // How it works:
    //   userAnswer.trim()              → removes spaces before/after the typed text
    //                                    e.g. "  Coverage  " becomes "Coverage"
    //   .equalsIgnoreCase(...)         → compares two Strings IGNORING uppercase/lowercase
    //                                    so "coverage", "Coverage", "COVERAGE" all match.
    //                                    This is DIFFERENT from .equals() which is case-sensitive.
    //   correctAnswer.trim()           → also trims the stored answer just to be safe
    //
    // Why not use == for String comparison?
    //   == compares memory addresses (references), not the actual characters.
    //   .equals() / .equalsIgnoreCase() compares the actual content character by character.
    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    // ── METHOD: getCorrectAnswer() ────────────────────────────────────────────
    // Returns the stored correct answer String.
    // Used by QuizManager to display "Correct answer: ___" after a wrong attempt.
    // No logic needed — just return the field value directly.
    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    // ── METHOD: getQuestionType() ─────────────────────────────────────────────
    // Returns the label "Fill in the Blank" for the quiz screen type badge.
    // Simple getter — returns a hardcoded String literal.
    @Override
    public String getQuestionType() {
        return "Fill in the Blank";
    }

    // ── METHOD: getHint() ─────────────────────────────────────────────────────
    // Returns the instruction text displayed below the question.
    // Tells the user to type their answer into the text field.
    @Override
    public String getHint() {
        return "Type your answer in the box";
    }
}
