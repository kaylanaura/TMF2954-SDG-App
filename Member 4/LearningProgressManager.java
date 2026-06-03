// Class      : LearningProgressManager
// Creator    : KAYLA BINTI MOHAMAD (102641)
// Description: Tracks user learning progress, completed topics,
//              quiz attempts and best score.
//              Saves and loads records from a text file.
//              Implements the Trackable interface.

import java.io.*;

public class LearningProgressManager implements Trackable {

    private static final String FILE_NAME = "progress.txt";

    private ProgressRecord record;

    public LearningProgressManager() {

        record = new ProgressRecord(
                0,
                0,
                0
        );

        loadProgress();
    }

    public void completeTopic() {
        record = new ProgressRecord(
                record.getCompletedTopics() + 1,
                record.getBestScore(),
                record.getQuizAttempts()
        );

        saveProgress();
    }

    public void recordQuizScore(int score) {

        record.incrementAttempts();

        record.updateBestScore(score);

        saveProgress();
    }

    @Override
    public void saveProgress() {

        try {

            BufferedWriter bw =
                new BufferedWriter(
                    new FileWriter(FILE_NAME)
                );

            bw.write(
                record.getCompletedTopics() + ","
                + record.getBestScore() + ","
                + record.getQuizAttempts()
            );

            bw.close();

        }
        catch(IOException e) {

            System.out.println(
                "Error saving progress: "
                + e.getMessage()
            );
        }
    }

    @Override
    public void loadProgress() {

        try {

            File file =
                new File(FILE_NAME);

            if(!file.exists()) {
                return;
            }

            BufferedReader br =
                new BufferedReader(
                    new FileReader(file)
                );

            String line =
                br.readLine();

            br.close();

            if(line != null) {

                String[] data =
                    line.split(",");

                record =
                    new ProgressRecord(

                        Integer.parseInt(data[0]),
                        Integer.parseInt(data[1]),
                        Integer.parseInt(data[2])
                    );
            }

        }
        catch(Exception e) {

            System.out.println(
                "Error loading progress: "
                + e.getMessage()
            );
        }
    }

    @Override
    public int getCompletedTopics() {
        return record.getCompletedTopics();
    }

    @Override
    public int getBestScore() {
        return record.getBestScore();
    }

    public int getQuizAttempts() {
        return record.getQuizAttempts();
    }

    @Override
    public String toString() {

        return "ProgressRecord { "
            + "Completed Topics = "
            + record.getCompletedTopics()
            + ", Best Score = "
            + record.getBestScore()
            + ", Quiz Attempts = "
            + record.getQuizAttempts()
            + " }";
    }
}