// Class      : InvalidScoreException
// Creator    : Izwan bin Omar
// Tester     : Kayla Binti Mohamad (102641)
// Description: Custom exception for invalid quiz percentage.
//
// Lecture Concept Applied:
// Custom Exception:
// - This class extends the predefined Exception class.
// - It is used to represent an unusual situation where the percentage is below 0 or above 100.
// - This supports exception handling using try, throw, and catch.

public class InvalidScoreException extends Exception {

    // Constructor:
    // Sends the error message to the parent Exception class using super().
    public InvalidScoreException(String message) {
        super(message);
    }
}
