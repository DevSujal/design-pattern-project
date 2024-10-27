import java.util.*;
import java.util.concurrent.TimeUnit;

// Achievement System
class Achievement {
    private final String name;
    private final String description;
    private boolean unlocked;

    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        this.unlocked = false;
    }

    public void unlock() {
        if (!unlocked) {
            unlocked = true;
            GameUtils.slowPrint("\n🏆 Achievement Unlocked: " + name + "\n");
            GameUtils.slowPrint("📜 " + description + "\n");
        }
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

// Player Profile System
class PlayerProfile {
    private final String name;
    private int score;
    private final List<Achievement> achievements;
    private final Map<String, Integer> eraVisits;
    private final List<String> unlockedArtifacts;

    public PlayerProfile(String name) {
        this.name = name;
        this.score = 0;
        this.achievements = initializeAchievements();
        this.eraVisits = new HashMap<>();
        this.unlockedArtifacts = new ArrayList<>();
    }

    private List<Achievement> initializeAchievements() {
        return Arrays.asList(
            new Achievement("Time Traveler", "Visit all historical eras"),
            new Achievement("History Scholar", "Score 100% in any era quiz"),
            new Achievement("Artifact Hunter", "Discover 10 historical artifacts"),
            new Achievement("Cultural Expert", "Learn about all cultural aspects of an era"),
            new Achievement("Master Historian", "Complete all eras with perfect scores")
        );
    }

    public void addScore(int points) {
        this.score += points;
        checkScoreAchievements();
    }

    public void visitEra(String eraName) {
        eraVisits.put(eraName, eraVisits.getOrDefault(eraName, 0) + 1);
        checkEraAchievements();
    }

    public void unlockArtifact(String artifactName) {
        if (!unlockedArtifacts.contains(artifactName)) {
            unlockedArtifacts.add(artifactName);
            GameUtils.slowPrint("\n🏺 New Artifact Discovered: " + artifactName + "\n");
            checkArtifactAchievements();
        }
    }

    private void checkScoreAchievements() {
        if (score >= 1000) {
            achievements.get(4).unlock(); // Master Historian
        }
    }

    private void checkEraAchievements() {
        if (eraVisits.size() >= 5) {
            achievements.get(0).unlock(); // Time Traveler
        }
    }

    private void checkArtifactAchievements() {
        if (unlockedArtifacts.size() >= 10) {
            achievements.get(2).unlock(); // Artifact Hunter
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }
}

// Base Question Class
class Question {
    private final String questionText;
    private final String correctAnswer;
    private final List<String> choices;
    private final String explanation;

    public Question(String questionText, String correctAnswer, List<String> choices, String explanation) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
        this.explanation = explanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public String getExplanation() {
        return explanation;
    }
}

// Multiple Choice Question Class
class MultiChoiceQuestion extends Question {
    private final List<String> correctAnswers;

    public MultiChoiceQuestion(String questionText, List<String> correctAnswers, List<String> choices, String explanation) {
        super(questionText, correctAnswers.get(0), choices, explanation);
        this.correctAnswers = correctAnswers;
    }

    public boolean checkAnswer(List<String> userAnswers) {
        return new HashSet<>(userAnswers).containsAll(correctAnswers) && 
               new HashSet<>(correctAnswers).containsAll(userAnswers);
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }
}

// Base Era Interface
interface Era {
    void describeEra();
    List<String> getKeyEvents();
    List<Question> getQuizQuestions();
    Map<String, String> getCulturalHighlights();
    List<String> getHistoricalFigures();
    List<String> getArtifacts();
    String getName();
    String getTimespan();
}

// Abstract Base Era Class
abstract class BaseEra implements Era {
    protected final String eraName;
    protected final String timespan;
    protected final Map<String, String> culturalHighlights;
    protected final List<String> historicalFigures;
    protected final List<String> artifacts;
    protected final List<String> keyEvents;

    public BaseEra(String eraName, String timespan) {
        this.eraName = eraName;
        this.timespan = timespan;
        this.culturalHighlights = new HashMap<>();
        this.historicalFigures = new ArrayList<>();
        this.artifacts = new ArrayList<>();
        this.keyEvents = new ArrayList<>();
    }

    @Override
    public void describeEra() {
        GameUtils.slowPrint("\n🏛️ Welcome to " + eraName + " (" + timespan + ")!\n");
        GameUtils.displayDecorativeLine();
    }

    @Override
    public Map<String, String> getCulturalHighlights() {
        return culturalHighlights;
    }

    @Override
    public List<String> getHistoricalFigures() {
        return historicalFigures;
    }

    @Override
    public List<String> getArtifacts() {
        return artifacts;
    }

    @Override
    public String getName() {
        return eraName;
    }

    @Override
    public String getTimespan() {
        return timespan;
    }

    @Override
    public List<String> getKeyEvents() {
        return keyEvents;
    }
}

// Quiz Strategy Interface
interface QuizStrategy {
    boolean administerQuiz(List<Question> questions);
}

// Enhanced Quiz Strategy Implementation
class EnhancedQuizStrategy implements QuizStrategy {
    private final Scanner scanner;
    private int score;
    private int totalQuestions;
    private final List<String> incorrectAnswers;
    private boolean hintUsed;

    public EnhancedQuizStrategy() {
        this.scanner = new Scanner(System.in);
        this.incorrectAnswers = new ArrayList<>();
        this.hintUsed = false;
    }

    @Override
    public boolean administerQuiz(List<Question> questions) {
        resetQuiz();
        
        for (Question question : questions) {
            handleQuestion(question);
        }
        
        return calculateResults();
    }

    private void resetQuiz() {
        score = 0;
        totalQuestions = 0;
        incorrectAnswers.clear();
        hintUsed = false;
    }

    private void handleQuestion(Question question) {
        displayQuestion(question);
        if (!hintUsed && offerHint()) {
            provideHint(question);
            hintUsed = true;
        }
        processAnswer(question);
        totalQuestions++;
    }

    private void displayQuestion(Question question) {
        GameUtils.slowPrint("\n📝 Question: " + question.getQuestionText() + "\n");
        List<String> choices = question.getChoices();
        for (int i = 0; i < choices.size(); i++) {
            GameUtils.slowPrint((i + 1) + ". " + choices.get(i) + "\n");
        }
    }

    private boolean offerHint() {
        GameUtils.slowPrint("\nWould you like to use a hint? (y/n): ");
        return scanner.nextLine().trim().toLowerCase().equals("y");
    }

    private void provideHint(Question question) {
        GameUtils.slowPrint("\n💡 Hint: Consider the time period and cultural context.\n");
    }

    private void processAnswer(Question question) {
        if (question instanceof MultiChoiceQuestion) {
            handleMultiChoiceAnswer((MultiChoiceQuestion) question);
        } else {
            handleSingleChoiceAnswer(question);
        }
    }

    private void handleSingleChoiceAnswer(Question question) {
        GameUtils.slowPrint("Your answer (1-" + question.getChoices().size() + "): ");
        try {
            int answerIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            checkAnswer(question, answerIndex);
        } catch (NumberFormatException e) {
            GameUtils.slowPrint("Invalid input. Please enter a number.\n");
            handleSingleChoiceAnswer(question);
        }
    }

    private void handleMultiChoiceAnswer(MultiChoiceQuestion question) {
        GameUtils.slowPrint("Enter your answers separated by spaces (e.g., 1 3 4): ");
        String[] inputs = scanner.nextLine().trim().split("\\s+");
        List<String> userAnswers = new ArrayList<>();
        
        for (String input : inputs) {
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < question.getChoices().size()) {
                    userAnswers.add(question.getChoices().get(index));
                }
            } catch (NumberFormatException ignored) {
            }
        }
        
        if (question.checkAnswer(userAnswers)) {
            score++;
            GameUtils.slowPrint("Correct!\n");
        } else {
            incorrectAnswers.add(question.getQuestionText());
            GameUtils.slowPrint("Incorrect. The correct answers were: " + 
                String.join(", ", question.getCorrectAnswers()) + "\n");
        }
    }

    private void checkAnswer(Question question, int answerIndex) {
        if (question.getChoices().get(answerIndex).equals(question.getCorrectAnswer())) {
            score++;
            GameUtils.slowPrint("Correct!\n");
        } else {
            incorrectAnswers.add(question.getQuestionText());
            GameUtils.slowPrint("Incorrect. The correct answer was: " + question.getCorrectAnswer() + "\n");
        }
    }

    private boolean calculateResults() {
        GameUtils.slowPrint("\n🎓 Quiz Results: " + score + " / " + totalQuestions + " correct.\n");
        if (score == totalQuestions) {
            GameUtils.slowPrint("Perfect score! Well done!\n");
            return true;
        } else {
            GameUtils.slowPrint("Keep trying to improve your knowledge!\n");
            if (!incorrectAnswers.isEmpty()) {
                GameUtils.slowPrint("Review the following questions:\n");
                incorrectAnswers.forEach(question -> GameUtils.slowPrint("❌ " + question + "\n"));
            }
            return false;
        }
    }
}

// Game Utilities
class GameUtils {
    public static void slowPrint(String message) {
        for (char ch : message.toCharArray()) {
            System.out.print(ch);
            try {
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void displayDecorativeLine() {
        System.out.println("═════════════════════════════════════════════════════════════════");
    }
}

// Main Class
class TimeTravelGame {
    private final PlayerProfile player;
    private final List<Era> eras;
    private final EnhancedQuizStrategy quizStrategy;

    public TimeTravelGame(String playerName) {
        this.player = new PlayerProfile(playerName);
        this.eras = new ArrayList<>();
        this.quizStrategy = new EnhancedQuizStrategy();
    }

    public void startGame() {
        GameUtils.slowPrint("\nWelcome to the Time Travel Simulator, " + player.getName() + "!\n");
        selectEra();
    }

    private void selectEra() {
        while (true) {
            GameUtils.slowPrint("\nSelect an Era to Explore:\n");
            for (int i = 0; i < eras.size(); i++) {
                GameUtils.slowPrint((i + 1) + ". " + eras.get(i).getName() + "\n");
            }
            GameUtils.slowPrint("Enter the number of your chosen Era: ");
            int choice = new Scanner(System.in).nextInt() - 1;
            if (choice >= 0 && choice < eras.size()) {
                visitEra(eras.get(choice));
            } else {
                GameUtils.slowPrint("Invalid choice. Please try again.\n");
            }
        }
    }

    private void visitEra(Era era) {
        player.visitEra(era.getName());
        era.describeEra();
        if (quizStrategy.administerQuiz(era.getQuizQuestions())) {
            player.addScore(100);
        }
        displayCulturalHighlights(era);
    }

    private void displayCulturalHighlights(Era era) {
        GameUtils.slowPrint("\nCultural Highlights of the " + era.getName() + ":\n");
        era.getCulturalHighlights().forEach((title, desc) -> GameUtils.slowPrint(title + ": " + desc + "\n"));
    }

    public static void main(String[] args) {
        TimeTravelGame game = new TimeTravelGame("Player1");
        game.startGame();
    }
}
