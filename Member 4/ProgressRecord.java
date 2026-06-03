// Class      : ProgressRecord
// Creator    : KAYLA BINTI MOHAMAD (102641)
// Description: Stores learning progress information for a user.
//              Demonstrates encapsulation through private attributes
//              and public getter methods.

public class ProgressRecord {

    private int completedTopics;
    private int bestScore;
    private int quizAttempts;

    public ProgressRecord(int completedTopics,
                          int bestScore,
                          int quizAttempts) {

        this.completedTopics = completedTopics;
        this.bestScore = bestScore;
        this.quizAttempts = quizAttempts;
    }

    public int getCompletedTopics() {
        return completedTopics;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getQuizAttempts() {
        return quizAttempts;
    }

    public void incrementAttempts() {
        quizAttempts++;
    }

    public void updateBestScore(int score) {
        if(score > bestScore) {
            bestScore = score;
        }
    }
}