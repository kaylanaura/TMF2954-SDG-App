// Interface  : Answerable
// Creator    : NURIRZAM ZEANA BINTI MUHAMMAD ZAMRI (102885)
// Description: Defines the contract that every question type must follow.
//              Any question class (MCQ, TrueFalse, FillBlank) must implement
//              this interface. This ensures QuizManager can handle all question
//              types in the same way without needing to know the specific type.

public interface Answerable {

    // Return the question text to show the user
    String getQuestion();

    // Check if the given answer is correct — returns true or false
    boolean checkAnswer(String userAnswer);

    // Return the correct answer (used to show user after they answer)
    String getCorrectAnswer();

    // Return the question type as a label e.g. "MCQ", "True/False", "Fill Blank"
    String getQuestionType();

    // Return hint text to show the user (e.g. "Choose A, B, C or D")
    String getHint();
}
