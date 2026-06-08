// Interface  : Answerable
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Tester     : Kayla Binti Mohamad (102641)
// Description: Defines the contract that every question type must follow.
//              Any question class (MCQ, TrueFalse, FillBlank) must implement
//              this interface. This ensures QuizManager can handle all question
//              types in the same way without needing to know the specific type.
// ── 'public' KEYWORD ─────────────────────────────────────────────────────────
// 'public' means this interface can be accessed / used by ANY class
// in the entire project, not just classes in the same file/package.
// If it were 'package-private' (no keyword), only classes in the same
// package could use it.

public interface Answerable {

    // ── WHY NO 'private' or 'static' on methods inside an interface? ─────────
    // By default, all methods declared inside an interface are:
    //   • public  — must be reachable by whoever implements the interface
    //   • abstract — has no body here; the implementing class provides the body
    // You don't need to write "public abstract" — Java adds it automatically.

    // ── METHOD: getQuestion() ─────────────────────────────────────────────────
    // Return type : String  → gives back a text value (the question sentence)
    // No parameters needed — the question text is stored inside each object
    // The implementing class (e.g. MCQQuestion) must override this and
    // return its own 'question' field.
    String getQuestion();

    // ── METHOD: checkAnswer(String userAnswer) ────────────────────────────────
    // Return type : boolean → true if correct, false if wrong
    // Parameter   : String userAnswer — the text the user typed or selected
    // Each implementing class compares 'userAnswer' to its own stored answer.
    // MCQ compares letters (A/B/C/D), TrueFalse parses "true"/"false",
    // FillBlank does a case-insensitive text match.
    boolean checkAnswer(String userAnswer);

    // ── METHOD: getCorrectAnswer() ────────────────────────────────────────────
    // Return type : String → the correct answer as readable text
    // Used by QuizManager to show "Correct answer: ___" after a wrong attempt.
    // MCQ returns the full option string e.g. "C. 17"
    // TrueFalse returns "True" or "False"
    // FillBlank returns the expected word/number
    String getCorrectAnswer();

    // ── METHOD: getQuestionType() ─────────────────────────────────────────────
    // Return type : String → a short label describing the question format
    // Used by QuizManager to display the type badge on screen.
    // MCQ returns "MCQ", TrueFalse returns "True / False",
    // FillBlank returns "Fill in the Blank"
    String getQuestionType();

    // ── METHOD: getHint() ─────────────────────────────────────────────────────
    // Return type : String → a short instruction to help the user
    // Displayed below the question text on the quiz screen.
    // e.g. "Choose A, B, C or D" | "Select True or False" | "Type your answer"
    String getHint();
}
