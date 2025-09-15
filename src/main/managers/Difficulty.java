package main.managers;

public enum Difficulty {
    EASY(45, "easy_sentences.txt"),
    MEDIUM(30, "medium_sentences.txt"),
    HARD(20, "hard_sentences.txt");

    private final int initialTime;
    private final String sentenceFile;

    Difficulty(int initialTime, String sentenceFile) {
        this.initialTime = initialTime;
        this.sentenceFile = sentenceFile;
    }

    public int getInitialTime() {
        return initialTime;
    }

    public String getSentenceFile() {
        return sentenceFile;
    }
}