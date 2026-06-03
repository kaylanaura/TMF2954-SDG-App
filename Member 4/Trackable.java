// ============================================================
// Interface  : Trackable
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Defines the contract for tracking a user's
//              learning progress in the SDG 3 app.
//              Any class that wants to track progress must
//              implement these methods.
// ============================================================

public interface Trackable {

    // Save the user's current progress to a file
    void saveProgress();

    // Load previously saved progress from a file
    void loadProgress();

    // Return a summary of the user's progress as a String
    String getProgress();

    // Mark one topic as completed
    void markTopicComplete(String topicName);

    // Check if a specific topic has been completed
    boolean isTopicComplete(String topicName);

    // Return the number of topics the user has completed
    int getCompletedCount();
}
