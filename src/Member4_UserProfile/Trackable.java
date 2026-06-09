// ============================================================
// Interface  : Trackable
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Contract (interface) for tracking learning
//              progress in SDG 3 app.
//              ANY class implementing this MUST define all methods.
//              Demonstrates: INTERFACE, ABSTRACTION
// ============================================================


// public interface: defines a contract with abstract methods
// Classes use "implements Trackable" to follow this contract
// INTERFACE: no implementation here - only method signatures
public interface Trackable {


    // public (implicit): accessible from any class
    // void: returns nothing
    //saveProgress(): saves user's current progress to a file
    // FILE HANDLING: writes data to file (implemented in class)
    // Save the user's current progress to a file
    void saveProgress();


    // public (implicit): accessible from any class
    // void: returns nothing
    // loadProgress(): loads previously saved progress FROM a file
    // FILE HANDLING: reads data from file (implemented in class)
    // Load previously saved progress from a file
    void loadProgress();


    // public (implicit): accessible from any class
    // String: returns a String summary of progress
    // getProgress(): returns user's progress summary as text
    // Returns: String like "Completed: 5 topics, Total: 10 topics"
    // Return a summary of the user's progress as a String
    String getProgress();


    // public (implicit): accessible from any class
    // void: returns nothing
    // Parameter: topicName (String) - name of topic to mark
    // markTopicComplete(): marks ONE topic as completed in progress
    // Mark one topic as completed
    void markTopicComplete(String topicName);


    // public (implicit): accessible from any class
    // boolean: returns true or false
    // Parameter: topicName (String) - name of topic to check
    // isTopicComplete(): checks IF a topic has been completed
    // Returns: true = completed, false = not completed
    // Check if a specific topic has been completed
    boolean isTopicComplete(String topicName);


    // public (implicit): accessible from any class
    // int: returns integer number
    // getCompletedCount(): returns TOTAL number of completed topics
    // Returns: int (e.g., 5 = user completed 5 topics)
    // Return the number of topics the user has completed
    int getCompletedCount();
}