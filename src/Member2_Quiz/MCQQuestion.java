// Class      : MCQQuestion
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Tester     : Kayla Binti Mohamad (102641)
// Description: Represents a multiple-choice question with 4 options (A/B/C/D).
//              Implements the Answerable interface so QuizManager can handle
//              it the same way as all other question types.

// ── 'public' KEYWORD ─────────────────────────────────────────────────────────
// 'public' → this class is visible to all other classes in the project.
// Without 'public', only classes in the same package could use it.

// ── 'class' KEYWORD ──────────────────────────────────────────────────────────
// 'class' declares a blueprint for creating objects.
// Each object (instance) of MCQQuestion holds its own question text,
// options array, and correct letter — independent of other MCQQuestion objects.

// ── 'implements Answerable' ───────────────────────────────────────────────────
// 'implements' means this class agrees to the contract set by the Answerable
// interface. Java will force this class to provide a body for ALL methods
// declared in Answerable (getQuestion, checkAnswer, getCorrectAnswer,
// getQuestionType, getHint). If any method is missing → compile error.
// This is an example of ABSTRACTION + POLYMORPHISM in OOP.

public class MCQQuestion implements Answerable {

    // ── ENCAPSULATION — PRIVATE FIELDS ───────────────────────────────────────
    // 'private' → these fields can only be read/changed from inside this class.
    // Outside classes (e.g. QuizManager) must call the getter methods instead.
    // This is the OOP principle of ENCAPSULATION — hiding internal data.

    private String   question;      // the question text shown to the user
                                    // String = a sequence of characters (text)

    private String[] options;       // an ARRAY of 4 strings, one per choice
                                    // e.g. {"A. Yes", "B. No", "C. Maybe", "D. Never"}
                                    // String[] = array that holds String values
                                    // [0] = "A. ...", [1] = "B. ...", etc.

    private String   correctLetter; // stores which letter is correct, e.g. "C"
                                    // always stored in UPPERCASE (see constructor)

    // ── CONSTRUCTOR ───────────────────────────────────────────────────────────
    // A constructor is a special method that runs when 'new MCQQuestion(...)' is called.
    // It has the SAME NAME as the class and NO return type (not even void).
    // 'public' → can be called from outside, e.g. QuizManager calls it to create questions.
    //
    // Parameters:
    //   String question      — the question text passed in
    //   String[] options     — the 4 answer choices passed in as an array
    //   String correctLetter — the letter of the correct answer passed in (e.g. "C")
    //
    // 'this.' refers to the current OBJECT's own field, to distinguish it
    // from the parameter with the same name. Without 'this.', Java would
    // think you're assigning the parameter to itself (no effect).
    public MCQQuestion(String question, String[] options, String correctLetter) {
        this.question      = question;
        this.options       = options;
        this.correctLetter = correctLetter.toUpperCase();
        // .toUpperCase() → converts the letter to uppercase so comparisons are
        // consistent. e.g. if someone passes "c" it becomes "C" here.
    }

    // ── @Override ANNOTATION ─────────────────────────────────────────────────
    // '@Override' tells Java: "this method is fulfilling a contract from an
    // interface or parent class."
    // It's optional but recommended — Java will show an error if the method
    // signature doesn't actually match one from the interface.

    // ── METHOD: getQuestion() ─────────────────────────────────────────────────
    // 'public'  → accessible from outside (QuizManager calls this)
    // 'String'  → return type; this method gives back a text value
    // 'void'    → means NO return value. This method does NOT use void — it
    //             returns a String. Just noting void for contrast.
    // Returns the stored question text so the quiz screen can display it.
    @Override
    public String getQuestion() {
        return question;
        // 'return' sends the value of the 'question' field back to the caller.
    }

    // ── METHOD: checkAnswer(String userAnswer) ────────────────────────────────
    // 'boolean' return type → can only return true or false
    // Receives the answer the user selected (a letter like "B")
    // and checks if it matches the stored correctLetter.
    //
    // .trim()        → removes any accidental spaces before/after the text
    // .toUpperCase() → converts to uppercase so "b" and "B" both work
    // .equals()      → compares two String values character by character
    //                  (never use == for Strings; == checks memory address,
    //                   not content)
    @Override
    public boolean checkAnswer(String userAnswer) {
        return userAnswer.trim().toUpperCase().equals(correctLetter);
    }

    // ── METHOD: getCorrectAnswer() ────────────────────────────────────────────
    // Returns the FULL option text for the correct letter, e.g. "C. 17"
    // Used by QuizManager to display "Correct answer: C. 17" after a wrong attempt.
    //
    // How it works:
    //   Loop through the options array with a for-each loop.
    //   .startsWith(correctLetter) checks if the option begins with that letter.
    //   e.g. correctLetter = "C", option = "C. 17" → starts with "C" → return it.
    //   If no match found (shouldn't happen), fall back to returning just the letter.
    @Override
    public String getCorrectAnswer() {
        for (String opt : options) {
            // 'for (String opt : options)' = enhanced for-loop
            // reads: "for each String opt in the options array"
            if (opt.startsWith(correctLetter)) return opt;
        }
        return correctLetter; // fallback — just return the letter
    }

    // ── METHOD: getQuestionType() ─────────────────────────────────────────────
    // Returns the string "MCQ" — used by QuizManager to display the type badge.
    // This is a simple getter; it just returns a hardcoded label string.
    @Override
    public String getQuestionType() {
        return "MCQ";
    }

    // ── METHOD: getHint() ─────────────────────────────────────────────────────
    // Returns the instruction text shown below the question on the quiz screen.
    // Tells the user what format to answer in.
    @Override
    public String getHint() {
        return "Choose A, B, C or D";
    }

    // ── EXTRA METHOD: getOptions() ────────────────────────────────────────────
    // 'public'   → accessible from outside this class
    // 'String[]' → return type is an array of Strings (the 4 choices)
    // This method is NOT in the Answerable interface — it is EXTRA.
    // QuizManager needs to call this to build the 4 clickable option buttons
    // on the MCQ answer panel. The interface only declares the shared methods;
    // class-specific methods (like this one) are added directly to the class.
    public String[] getOptions() {
        return options;
        // Returns the whole array reference. Caller can access each element
        // using options[0], options[1], options[2], options[3].
    }
}
