// Class      : TrueFalseQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Tester     : Kayla Binti Mohamad (102641)
// Description: Represents a True or False question.
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

// ── 'public class' ───────────────────────────────────────────────────────────
// 'public' → visible to all other classes in the project.
// 'class'  → declares a blueprint. Each TrueFalseQuestion object created from
//            this blueprint will have its own 'question' and 'correctAnswer'.

// ── 'implements Answerable' ───────────────────────────────────────────────────
// This class signs the Answerable contract.
// It MUST provide bodies for all 5 methods in Answerable:
// getQuestion(), checkAnswer(), getCorrectAnswer(), getQuestionType(), getHint()
// This is POLYMORPHISM — a TrueFalseQuestion object can be stored in a
// List<Answerable> and called through the interface, just like MCQ or FillBlank.

public class TrueFalseQuestion implements Answerable {

    // ── ENCAPSULATION — PRIVATE FIELDS ───────────────────────────────────────
    // 'private' → hidden from outside. Only accessible inside this class.
    // OOP principle: don't let other classes directly change internal data.

    private String  question;      // the statement shown to the user as a question
                                   // e.g. "SDG 3 stands for Good Health and Well-Being."
                                   // String = a text value (sequence of characters)

    private boolean correctAnswer; // stores whether the statement is TRUE or FALSE
                                   // 'boolean' = a primitive type that can ONLY be
                                   //   true  → the statement is correct
                                   //   false → the statement is incorrect
                                   // Unlike String, boolean is NOT an object — it's
                                   // a primitive data type (lowercase 'b').

    // ── CONSTRUCTOR ───────────────────────────────────────────────────────────
    // Called when QuizManager does: new TrueFalseQuestion("...", true)
    // Same name as the class, no return type.
    // 'public' → can be called from outside (QuizManager needs to call it).
    //
    // Parameters:
    //   String question      — the statement text for this question
    //   boolean correctAnswer — true if the statement is correct, false if not
    //
    // 'this.' distinguishes the FIELD from the PARAMETER with the same name.
    // this.question = the field stored in the object
    // question      = the parameter passed into this constructor call
    public TrueFalseQuestion(String question, boolean correctAnswer) {
        this.question      = question;
        this.correctAnswer = correctAnswer;
    }

    // ── @Override ANNOTATION ─────────────────────────────────────────────────
    // Signals that this method fulfils a method declared in the Answerable interface.
    // Java checks at compile time that the signature matches.

    // ── METHOD: getQuestion() ─────────────────────────────────────────────────
    // 'public'  → QuizManager (outside class) can call this.
    // 'String'  → return type; sends back the question text as a String.
    // 'return question' → sends the stored field value back to the caller.
    @Override
    public String getQuestion() {
        return question;
    }

    // ── METHOD: checkAnswer(String userAnswer) ────────────────────────────────
    // 'boolean' return type → returns true if correct, false if wrong.
    // Parameter: String userAnswer — the text "True" or "False" that the user clicked.
    //
    // How it works step by step:
    //   1. userAnswer.trim()        → removes leading/trailing spaces
    //   2. .toLowerCase()           → converts to lowercase, e.g. "True" → "true"
    //   3. Boolean.parseBoolean(…)  → converts the String "true" or "false"
    //                                 into the actual boolean value true or false.
    //                                 Note: Boolean (capital B) = the wrapper class
    //                                 for the primitive boolean.
    //                                 Boolean.parseBoolean("true")  → true
    //                                 Boolean.parseBoolean("false") → false
    //                                 Boolean.parseBoolean("yes")   → false (default)
    //   4. userBool == correctAnswer → compares two boolean primitives with ==
    //                                 For primitives (int, boolean, char), == is fine.
    //                                 It's only for String OBJECTS that we must use .equals().
    @Override
    public boolean checkAnswer(String userAnswer) {
        boolean userBool = Boolean.parseBoolean(userAnswer.trim().toLowerCase());
        // 'boolean userBool' → a local variable that lives only inside this method
        return userBool == correctAnswer;
    }

    // ── METHOD: getCorrectAnswer() ────────────────────────────────────────────
    // Returns "True" or "False" as a human-readable String.
    // Used by QuizManager to show the correct answer after a wrong attempt.
    //
    // Ternary operator syntax:   condition ? valueIfTrue : valueIfFalse
    // e.g. correctAnswer ? "True" : "False"
    //   → if correctAnswer is true  → returns the String "True"
    //   → if correctAnswer is false → returns the String "False"
    // This is a shorter way of writing an if-else that returns a value.
    @Override
    public String getCorrectAnswer() {
        return correctAnswer ? "True" : "False";
    }

    // ── METHOD: getQuestionType() ─────────────────────────────────────────────
    // Returns the label "True / False" used to display the type badge on screen.
    // Simple getter — returns a hardcoded String literal.
    @Override
    public String getQuestionType() {
        return "True / False";
    }

    // ── METHOD: getHint() ─────────────────────────────────────────────────────
    // Returns the instruction text shown below the question.
    // Guides the user to select from the True/False buttons.
    @Override
    public String getHint() {
        return "Select True or False";
    }
}
