// ============================================================
// Class      : ProgressRecord
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: A simple helper class that stores one entry
//              in the user's progress history.
//              Each record holds the topic name and the date
//              and time it was completed.
//              Used by UserProfile to keep a history log.
// ============================================================

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgressRecord {

    // --- Attributes -------------------------------------------
    private String        topicName;   // name of the topic that was completed
    private LocalDateTime completedAt; // date and time of completion

    // --- Constructor ------------------------------------------
    // Creates a new record for a topic completed right now
    public ProgressRecord(String topicName) {
        this.topicName   = topicName;
        this.completedAt = LocalDateTime.now(); // record the current time
    }

    // --- Getters ----------------------------------------------
    public String getTopicName() {
        return topicName;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // Returns the completion time in a readable format: "2026-06-01 14:30"
    public String getFormattedTime() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return completedAt.format(fmt);
    }

    // --- toString ---------------------------------------------
    @Override
    public String toString() {
        return "ProgressRecord{topic='" + topicName
             + "', completedAt='" + getFormattedTime() + "'}";
    }
}
