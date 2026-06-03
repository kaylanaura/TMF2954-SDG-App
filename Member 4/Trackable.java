// Interface  : Trackable
// Creator    : KAYLA BINTI MOHAMAD (102641)
// Description: Defines behaviour for tracking user learning progress.
//              Any class that manages learning records must implement
//              this interface.

public interface Trackable {

    void saveProgress();

    void loadProgress();

    int getCompletedTopics();

    int getBestScore();
}