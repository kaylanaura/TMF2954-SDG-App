// ============================================================
// Class      : ProgressRecord
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Helper class that stores ONE entry in user's
//              progress history (topic + completion time).
//              Used by UserProfile for history log.
//              Demonstrates: ENCAPSULATION, GETTERS, toString() OVERRIDE
// ============================================================


// Import: LocalDateTime class for date/time handling
import java.time.LocalDateTime;
// Import: DateTimeFormatter for formatting date/time to readable string
import java.time.format.DateTimeFormatter;


public class ProgressRecord {


    // --- Attributes -------------------------------------------
    // private: only accessible within THIS class (ENCAPSULATION)
    // Stores the name of the topic that was completed
    private String        topicName;   // name of the topic that was completed
    
    // private: only accessible within THIS class (ENCAPSULATION)
    // Stores the exact date and time when topic was completed
    private LocalDateTime completedAt; // date and time of completion


    // --- Constructor ------------------------------------------
    // public: accessible from any class
    // Constructor: initializes new ProgressRecord object
    // Parameter: topicName (String) - name of completed topic
    // this.topicName = topicName: assigns parameter to attribute
    // LocalDateTime.now(): gets current date/time automatically
    // Creates a new record for a topic completed right now
    public ProgressRecord(String topicName) {
        this.topicName   = topicName;
        this.completedAt = LocalDateTime.now(); // record the current time
    }


    // --- Getters ----------------------------------------------
    // public: accessible from any class
    // Getter: returns the 'topicName' attribute (ENCAPSULATION)
    // Returns: String containing the topic name
    public String getTopicName() {
        return topicName;
    }


    // public: accessible from any class
    // Getter: returns the 'completedAt' attribute (ENCAPSULATION)
    // Returns: LocalDateTime object with completion time
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }


    // public: accessible from any class
    // Method: formats completion time to readable string "2026-06-01 14:30"
    // DateTimeFormatter.ofPattern(): creates formatter with pattern "yyyy-MM-dd HH:mm"
    // completedAt.format(fmt): formats LocalDateTime using the formatter
    // Returns the completion time in a readable format: "2026-06-01 14:30"
    public String getFormattedTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return completedAt.format(fmt);
    }


    // --- toString ---------------------------------------------
    // @Override: indicates this method overrides parent class's toString()
    // public: accessible from any class
    // toString(): returns String representation of the object
    // getFormattedTime(): calls method to format time nicely
    // Returns: String like "ProgressRecord{topic='Math', completedAt='2026-06-01 14:30'}"
    @Override
    public String toString() {
        return "ProgressRecord{topic='" + topicName
             + "', completedAt='" + getFormattedTime() + "'}";
    }
}