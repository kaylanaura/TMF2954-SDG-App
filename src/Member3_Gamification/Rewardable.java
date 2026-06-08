// Interface  : Rewardable
// Creator    : Izwan bin Omar
// Tester     : Kayla Binti Mohamad (102641)
// Description: Defines reward behaviour for the Gamification Module.
//
// Lecture Concept Applied:
// Interface:
// - An interface contains abstract method headers without method bodies.
// - Any class that implements this interface must provide the method definitions.
// - GamificationEngine implements this interface to ensure all reward behaviours are included.

public interface Rewardable {

    // Abstract method:
    // Awards a badge based on quiz percentage.
    String awardBadge(int percent);

    // Abstract method:
    // Calculates star rating based on quiz percentage.
    int calculateStars(int percent);

    // Abstract method:
    // Calculates points based on quiz score.
    int calculatePoints(int score);

    // Abstract method:
    // Returns rank based on quiz percentage.
    String getRank(int percent);

    // Abstract method:
    // Returns motivational message based on quiz percentage.
    String getMotivationalMessage(int percent);
}
