// ============================================================
// Class      : UserProfile
// Creator    : Kayla Binti Mohamad (102641)
// Tester     : Victoria Ngui Fong Eik (106647)
// Description: Stores user's name, ID, quiz scores, and
//              learning progress.
//
//              INHERITANCE  : extends Person (gets name, ID)
//              OVERRIDING   : toString() overridden to show full profile
//              INTERFACE    : implements Trackable (6 methods)
//              ENCAPSULATION: private attributes + public getters
//              FILE HANDLING: saves/loads progress to text files
// ============================================================


// Import: FileInputStream, FileOutputStream, etc. for FILE HANDLING
import java.io.*;
// Import: ArrayList for storing list of completed topics
import java.util.ArrayList;


// public class: accessible from any class
// extends Person: INHERITANCE - gets name, ID from Person class
// implements Trackable: INTERFACE - must implement all 6 Trackable methods
public class UserProfile extends Person implements Trackable {


    // --- File names for saving data ---------------------------
    // private: only accessible in THIS class (ENCAPSULATION)
    // static: belongs to class, not instance
    // final: cannot be changed (constant)
    // String: stores file name for progress data
    private static final String PROGRESS_FILE = "user_progress.txt";
    
    // private static final: constant file name for quiz score data
    private static final String SCORE_FILE      = "user_scores.txt";


    // --- Attributes -------------------------------------------
    // private: only accessible in THIS class (ENCAPSULATION)
    // int: integer storing latest quiz score (e.g., 8 out of 10)
    private int           quizScore;           // latest quiz score (out of total)
    
    // private int: total number of quiz questions (e.g., 10)
    private int           quizTotal;           // total number of quiz questions
    
    // private ArrayList: stores list of topic names (String)
    // ArrayList: dynamic list that grows as user completes topics
    private ArrayList<String> completedTopics; // list of topic names the user finished


    // --- Constructor ------------------------------------------
    // public: accessible from any class
    // Constructor: creates new UserProfile with name and student ID
    // super(name, id): calls Person constructor (INHERITANCE)
    // quizScore = 0: initializes score to 0
    // quizTotal = 0: initializes total to 0
    // completedTopics = new ArrayList<>(): creates empty list
    // loadProgress(): automatically loads saved topics from file
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


    // public: accessible from any class
    // void: returns nothing
    // Parameters: score (int), total (int)
    // setQuizScore(): stores latest quiz result
    // this.quizScore = score: assigns parameter to attribute
    // this.quizTotal = total: assigns parameter to attribute
    // Store the latest quiz result (score and total questions)
    public void setQuizScore(int score, int total) {
        this.quizScore = score;
        this.quizTotal = total;
    }


    // public: accessible from any class
    // int: returns integer
    // getQuizScore(): returns the latest quiz score
    // Return the latest quiz score
    public int getQuizScore() {
        return quizScore;
    }


    // public: accessible from any class
    // int: returns integer
    // getQuizTotal(): returns total number of quiz questions
    // Return the total number of questions in the latest quiz
    public int getQuizTotal() {
        return quizTotal;
    }


    // public: accessible from any class
    // int: returns integer percentage
    // getQuizPercent(): calculates quiz percentage (score/total * 100)
    // if (quizTotal == 0): checks if no quiz taken yet
    // return 0: returns 0 if no quiz taken
    // Math.round(): rounds decimal to nearest integer
    // (quizScore * 100.0) / quizTotal: calculates percentage
    // Calculate and return the quiz percentage (0 if no quiz taken)
    public int getQuizPercent() {
        if (quizTotal == 0) return 0;
        return (int) Math.round((quizScore * 100.0) / quizTotal);
    }


    // public: accessible from any class
    // void: returns nothing
    // saveQuizScore(): saves quiz score to text file
    // try: starts exception handling for FILE HANDLING
    // FileWriter: writes text to file
    // new FileWriter(SCORE_FILE, true): true = APPEND mode (adds to file)
    // BufferedWriter: writes text efficiently to file
    // bw.write(): writes text line to file
    // bw.newLine(): adds new line after text
    // bw.close(): closes file (important!)
    // System.out.println: prints confirmation message
    // catch (IOException e): catches file errors
    // This is called after every quiz attempt
    // Save the quiz score to a text file (appended, not overwritten)
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


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // void: returns nothing
    // saveProgress(): saves completed topics to text file
    // try: starts exception handling for FILE HANDLING
    // new FileWriter(PROGRESS_FILE, false): false = OVERWRITE mode
    // for (String topic : completedTopics): loops through ALL topics in list
    // bw.write(topic): writes one topic per line
    // bw.close(): closes file
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


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // void: returns nothing
    // loadProgress(): loads completed topics FROM text file
    // completedTopics.clear(): clears list first to avoid duplicates
    // File: represents file in system
    // new File(PROGRESS_FILE): creates File object
    // if (!file.exists()): checks if file exists
    // return: exits method if no file
    // BufferedReader: reads text from file efficiently
    // new FileReader(file): reads from file
    // br.readLine(): reads ONE line from file
    // while ((line = br.readLine()) != null): loops until no more lines
    // line.trim(): removes extra spaces
    // if (!line.isEmpty()): skips empty lines
    // completedTopics.add(line): adds topic to list
    // br.close(): closes file
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


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // String: returns text string
    // getProgress(): returns user progress summary
    // completedTopics.size(): returns number of topics completed
    // Return a short summary of the user's progress as a String
    @Override
    public String getProgress() {
        return "User: " + name
             + " | Topics completed: " + completedTopics.size()
             + " | Latest quiz: " + quizScore + "/" + quizTotal
             + " (" + getQuizPercent() + "%)";
    }


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // void: returns nothing
    // Parameter: topicName (String) - name of topic to mark
    // markTopicComplete(): marks topic as completed
    // if (topicName == null || topicName.isEmpty()): checks for invalid input
    // return: exits if invalid
    // completedTopics.contains(topicName): checks if topic already in list
    // completedTopics.add(topicName): adds topic to list
    // saveProgress(): saves to file immediately after marking
    // Mark a topic as completed (adds it to the list if not already there)
    @Override
    public void markTopicComplete(String topicName) {
        if (topicName == null || topicName.isEmpty()) return;


        if (!completedTopics.contains(topicName)) {
            completedTopics.add(topicName);
            saveProgress(); // save to file every time a topic is completed
        }
    }


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // boolean: returns true or false
    // Parameter: topicName (String) - name of topic to check
    // isTopicComplete(): checks IF topic is completed
    // completedTopics.contains(topicName): returns true if found, false if not
    // Check if a specific topic is already completed
    @Override
    public boolean isTopicComplete(String topicName) {
        return completedTopics.contains(topicName);
    }


    // @Override: overrides method from Trackable INTERFACE
    // public: accessible from any class
    // int: returns integer number
    // getCompletedCount(): returns TOTAL completed topics
    // completedTopics.size(): returns number of items in list
    // Return the number of topics the user has completed
    @Override
    public int getCompletedCount() {
        return completedTopics.size();
    }


    // public: accessible from any class
    // Returns: ArrayList<String> (copy of completed topics)
    // getCompletedTopics(): returns COPY of list (not original)
    // new ArrayList<>(completedTopics): creates new list with same items
    // Return a copy of the completed topics list
    public ArrayList<String> getCompletedTopics() {
        return new ArrayList<>(completedTopics); // return a copy, not the original
    }


    // --- toString (OVERRIDDEN from Person) --------------------
    // @Override: overrides toString() from Person class (INHERITANCE)
    // public: accessible from any class
    // String: returns text string
    // toString(): returns FULL profile details
    // OVERRIDES: Person's simpler toString() (shows only name + ID)
    // This shows: name, ID, quiz score, topics completed
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