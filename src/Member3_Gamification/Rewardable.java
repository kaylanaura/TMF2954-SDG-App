// Interface  : Rewardable
// Creator    : Izwan bin Omar
// Tester     : Kayla Binti Mohamad (102641)
// Description: Defines reward behaviour for the Gamification Module.

public interface Rewardable {
    String awardBadge(int percent);
    int calculateStars(int percent);
    int calculatePoints(int score);
    String getRank(int percent);
    String getMotivationalMessage(int percent);
}