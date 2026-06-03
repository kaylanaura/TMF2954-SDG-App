# SDG 3 Health & Well-Being App
### TMF 2954 Java Programming — Group Project



## Project Structure

```
SDG-App/
├── src/                           All Java source files
│
│   ── Member 1: Learning Module (Victoria Ngui Fong Eik, 106647)
│   ├── LearningModulePanel.java   Home screen with topic cards
│   ├── ContentPanel.java          Displays one topic's content
│   ├── LearningContent.java       Stores text + image for one page
│   ├── LearningData.java          All 10 educational topics data
│   ├── ImageLoader.java           Loads PNG images for content pages
│   └── Displayable.java           Interface: display(), getTitle(), etc.
│
│   ── Member 2: Quiz Module (Nurirzam Zeana, 102885)
│   ├── QuizManager.java           Runs the quiz, shows score
│   ├── MCQQuestion.java           Multiple-choice question type
│   ├── TrueFalseQuestion.java     True/False question type
│   ├── FillBlankQuestion.java     Fill-in-the-blank question type
│   └── Answerable.java            Interface: checkAnswer(), getQuestion(), etc.
│
│   ── Member 3: Gamification (Izwan bin Omar)
│   ├── GamificationEngine.java    Shows badges, stars, points, rank
│   ├── InvalidScoreException.java Custom exception for bad score values
│   └── Rewardable.java            Interface: awardBadge(), calculateStars(), etc.
│
│   ── Member 4: User Profile & Integration (Kayla Binti Mohamad, 102641)
│   ├── Main.java                  Entry point — wires all modules together
│   ├── UserProfile.java           Stores user name, score history, progress
│   ├── Person.java                Base class that UserProfile extends
│   ├── ProgressRecord.java        Helper: stores one topic completion entry
│   └── Trackable.java             Interface: saveProgress(), loadProgress(), etc.
│
├── assets/final_images/           PNG images used in the Learning Module
├── compile_and_run.bat            Windows build + run script
├── compile_and_run.sh             Mac / Linux build + run script
└── README.md                      This file
```

---

## OOP Concepts Used

| Concept         | Where used |
|-----------------|------------|
| Inheritance     | `UserProfile extends Person` (Member 4) |
| Method Override | `UserProfile.toString()` overrides `Person.toString()` |
| Method Overload | `calculatePoints(score)` and `calculatePoints(score, bonus)` in GamificationEngine |
| Interface       | Displayable, Answerable, Rewardable, Trackable |
| Polymorphism    | QuizManager handles MCQ, TrueFalse, FillBlank all as `Answerable` |
| Exception       | `InvalidScoreException` (custom) caught in GamificationEngine |
| File I/O        | Quiz scores → `user_scores.txt`, Rewards → `reward_scores.txt`, Progress → `user_progress.txt` |

---

## Data Files Created at Runtime

| File                  | Contents |
|-----------------------|----------|
| `user_progress.txt`   | List of topics the user has completed |
| `user_scores.txt`     | Quiz score history (appended each attempt) |
| `reward_scores.txt`   | Gamification reward log (appended each attempt) |

---


