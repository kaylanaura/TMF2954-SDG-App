@echo off
echo ========================================
echo  SDG 3 Health ^& Well-Being App
echo  TMF2954 Java Programming S2025/26
echo ========================================
echo.

echo [1/2] Compiling all Java files...
javac -cp . -d . ^
  src\Member1_LearningContent\Displayable.java ^
  src\Member1_LearningContent\LearningContent.java ^
  src\Member1_LearningContent\LearningData.java ^
  src\Member1_LearningContent\ImageLoader.java ^
  src\Member1_LearningContent\ContentPanel.java ^
  src\Member1_LearningContent\LearningModulePanel.java ^
  src\Member2_Quiz\Answerable.java ^
  src\Member2_Quiz\MCQQuestion.java ^
  src\Member2_Quiz\TrueFalseQuestion.java ^
  src\Member2_Quiz\FillBlankQuestion.java ^
  src\Member2_Quiz\QuizManager.java ^
  src\Member3_Gamification\Rewardable.java ^
  src\Member3_Gamification\InvalidScoreException.java ^
  src\Member3_Gamification\GamificationEngine.java ^
  src\Member4_UserProfile\Trackable.java ^
  src\Member4_UserProfile\Person.java ^
  src\Member4_UserProfile\ProgressRecord.java ^
  src\Member4_UserProfile\UserProfile.java ^
  Main.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed. Please check the errors above.
    pause
    exit /b 1
)

echo [2/2] Compilation successful! Starting app...
echo.
java -cp . Main

pause
