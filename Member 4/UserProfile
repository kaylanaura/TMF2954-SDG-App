// ============================================================
// Class      : UserProfile
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Stores a user's name, ID, quiz score history,
//              and learning topic progress.
//
//              INHERITANCE  : UserProfile extends Person
//              OVERRIDING   : toString() is overridden to show
//                             full profile details instead of
//                             just name and ID.
//              INTERFACE    : Implements Trackable to handle
//                             saveProgress(), loadProgress(),
//                             getProgress(), markTopicComplete(),
//                             isTopicComplete(), getCompletedCount()
//
//              Progress and scores are saved to a text file
//              so data is kept between sessions.
// ============================================================

import java.io.*;
import java.util.ArrayList;

public class UserProfile extends Person implements Trackable {

    // --- File names for saving data ---------------------------
    private static final String PROGRESS_FILE = "user_progress.txt";
    private static final String SCORE_FILE     = "user_scores.txt";

    // --- Attributes -------------------------------------------
    private int           quizScore;           // latest quiz score (out of total)
    private int           quizTotal;           // total number of quiz questions
    private ArrayList<String> completedTopics; // list of topic names the user finished

    // --- Constructor ------------------------------------------
    // Creates a new user profile with a given name and student ID.
    // Automatically loads any previously saved progress.
    public UserProfile(String name, String id) {
        super(name, id);            // call the Person constructor
        this.quizScore       = 0;
        this.quizTotal       = 0;
        this.completedTopics = new ArrayList<>();
        loadProgress();             // restore saved topics from file
    }

    // --- Quiz score methods -----------------------------------

    // Store the latest quiz result (score and total questions)
    public void setQuizScore(int score, int total) {
        this.quizScore = score;
        this.quizTotal = total;
    }

    // Return the latest quiz score
    public int getQuizScore() {
        return quizScore;
    }

    // Return the total number of questions in the latest quiz
    public int getQuizTotal() {
        return quizTotal;
    }

    // Calculate and return the quiz percentage (0 if no quiz taken)
    public int getQuizPercent() {
        if (quizTotal == 0) return 0;
        return (int) Math.round((quizScore * 100.0) / quizTotal);
    }

    // Save the quiz score to a text file (appended, not overwritten)
    // This is called after every quiz attempt
    public void saveQuizScore() {
        try {
            FileWriter fw  = new FileWriter(SCORE_FILE, true); // true = append
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("User: " + name
                   + " | ID: " + id
                   + " | Score: " + quizScore + "/" + quizTotal
                   + " | Percent: " + getQuizPercent() + "%");
            bw.newLine();
            bw.close();
            System.out.println("Quiz score saved for " + name);
        } catch (IOException e) {
            System.out.println("Could not save quiz score: " + e.getMessage());
        }
    }

    // --- Trackable interface implementation -------------------

    // Save the list of completed topics to a text file (one topic per line)
    @Override
    public void saveProgress() {
        try {
            FileWriter fw  = new FileWriter(PROGRESS_FILE, false); // false = overwrite
            BufferedWriter bw = new BufferedWriter(fw);
            for (String topic : completedTopics) {
                bw.write(topic);
                bw.newLine();
            }
            bw.close();
            System.out.println("Progress saved for " + name);
        } catch (IOException e) {
            System.out.println("Could not save progress: " + e.getMessage());
        }
    }

    // Load the list of completed topics from the text file
    @Override
    public void loadProgress() {
        completedTopics.clear(); // clear first to avoid duplicates
        File file = new File(PROGRESS_FILE);

        // If no file exists yet, there is nothing to load
        if (!file.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    completedTopics.add(line);
                }
            }
            br.close();
            System.out.println("Progress loaded for " + name);
        } catch (IOException e) {
            System.out.println("Could not load progress: " + e.getMessage());
        }
    }

    // Return a short summary of the user's progress as a String
    @Override
    public String getProgress() {
        return "User: " + name
             + " | Topics completed: " + completedTopics.size()
             + " | Latest quiz: " + quizScore + "/" + quizTotal
             + " (" + getQuizPercent() + "%)";
    }

    // Mark a topic as completed (adds it to the list if not already there)
    @Override
    public void markTopicComplete(String topicName) {
        if (topicName == null || topicName.isEmpty()) return;

        if (!completedTopics.contains(topicName)) {
            completedTopics.add(topicName);
            saveProgress(); // save to file every time a topic is completed
        }
    }

    // Check if a specific topic is already completed
    @Override
    public boolean isTopicComplete(String topicName) {
        return completedTopics.contains(topicName);
    }

    // Return the number of topics the user has completed
    @Override
    public int getCompletedCount() {
        return completedTopics.size();
    }

    // Return a copy of the completed topics list
    public ArrayList<String> getCompletedTopics() {
        return new ArrayList<>(completedTopics); // return a copy, not the original
    }

    // --- toString (OVERRIDDEN from Person) --------------------
    // Shows the full profile: name, ID, score, and progress.
    // This OVERRIDES the simpler toString() in Person.
    @Override
    public String toString() {
        return "UserProfile{"
             + "name='" + name + "'"
             + ", id='" + id + "'"
             + ", quizScore=" + quizScore + "/" + quizTotal
             + ", topicsCompleted=" + completedTopics.size()
             + "}";
    }
}
